package erik.study.spring.boot.xxljob.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 调度中心（xxl-job-admin 3.x）管理 API 封装服务。
 *
 * <p>适配 xxl-job 3.x 新版管理 API：
 * <ul>
 *   <li>登录：POST /auth/doLogin（明文密码），会话 Cookie 从 Set-Cookie 下发；</li>
 *   <li>分页参数：offset / pagesize，任务名与执行器名字段均为 name；</li>
 *   <li>新增任务：/jobinfo/insert；删除 / 启停使用 ids[] 数组参数；</li>
 *   <li>返回统一为 {"code":200,"data":...,"msg":"Success","success":true}，成功即 code==200。</li>
 * </ul>
 *
 * <p>对应 HTTP 接口（均已在本机 3.x 调度中心实测，见 README）：
 * <pre>
 *   POST /auth/doLogin         登录（明文密码，返回 Cookie）
 *   POST /jobinfo/insert       新增任务
 *   POST /jobinfo/update       更新任务
 *   POST /jobinfo/delete       删除任务（ids[]）
 *   POST /jobinfo/start|stop   启动 / 停止任务（ids[]）
 *   POST /jobinfo/trigger      手动触发一次
 *   POST /jobinfo/pageList     任务分页查询
 *   POST /jobgroup/pageList    执行器分页查询
 *   POST /jobgroup/insert      新建执行器
 * </pre>
 */
@Service
public class JobAdminService {

    private static final Logger logger = LoggerFactory.getLogger(JobAdminService.class);

    /** xxl-job 统一返回码，200 表示成功 */
    private static final int SUCCESS_CODE = 200;

    private final String adminAddresses;
    private final String loginUsername;
    private final String loginPassword;

    /** 与调度中心通信的 accessToken（创建执行器时写入，保证执行器能注册成功） */
    @Value("${xxl.job.access-token:}")
    private String accessToken;

    private final RestTemplate restTemplate = new RestTemplate();

    /** 登录后保存的会话 Cookie（3.x 名称为 xxl_job_login_token） */
    private volatile String loginCookie;

    public JobAdminService(JobAdminProperties properties) {
        this.adminAddresses = properties.getAddresses();
        this.loginUsername = properties.getLoginUsername();
        this.loginPassword = properties.getLoginPassword();
    }

    // ==================== 任务（JobInfo）API ====================

    /**
     * 注册新任务，返回调度中心分配的任务 ID。
     *
     * @param jobInfo 任务参数（jobGroup / name / executorHandler / scheduleConf 等）
     */
    public int registerJob(JobInfoRequest jobInfo) {
        JSONObject json = postForm("/jobinfo/insert", toForm(jobInfo));
        String content = json.getString("data");
        Integer jobId = content == null ? null : Integer.valueOf(content);
        logger.info("注册任务成功: handler={}, name={}, jobId={}",
                jobInfo.getExecutorHandler(), jobInfo.getName(), jobId);
        return jobId == null ? -1 : jobId;
    }

    /**
     * 更新任务。主要用来更新执行时间（scheduleConf），也可以更新参数、负责人等。
     * 注意：更新时 {@code jobInfo.id} 必须传任务 ID。
     */
    public void updateJob(JobInfoRequest jobInfo) {
        if (jobInfo.getId() == null) {
            throw new IllegalArgumentException("更新任务必须指定任务 id");
        }
        postForm("/jobinfo/update", toForm(jobInfo));
        logger.info("更新任务成功: id={}, handler={}, scheduleConf={}",
                jobInfo.getId(), jobInfo.getExecutorHandler(), jobInfo.getScheduleConf());
    }

    /** 删除任务 */
    public void removeJob(int jobId) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("ids[]", String.valueOf(jobId));
        postForm("/jobinfo/delete", form);
        logger.info("删除任务成功: id={}", jobId);
    }

    /** 启动任务调度 */
    public void startJob(int jobId) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("ids[]", String.valueOf(jobId));
        postForm("/jobinfo/start", form);
        logger.info("启动任务成功: id={}", jobId);
    }

    /** 停止任务调度 */
    public void stopJob(int jobId) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("ids[]", String.valueOf(jobId));
        postForm("/jobinfo/stop", form);
        logger.info("停止任务成功: id={}", jobId);
    }

    /** 手动触发一次任务（可在调度中心“调度日志”里看到本次执行记录） */
    public void triggerJob(int jobId, String executorParam) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", String.valueOf(jobId));
        form.add("executorParam", executorParam == null ? "" : executorParam);
        form.add("addressList", "");
        postForm("/jobinfo/trigger", form);
        logger.info("手动触发任务成功: id={}, param={}", jobId, executorParam);
    }

    /**
     * 按 Handler 名称查找任务 ID（用于幂等注册：已存在则更新，不存在则新增）。
     *
     * @return 任务 ID；不存在返回 null
     */
    public Integer findJobIdByHandler(int jobGroupId, String executorHandler) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("offset", "0");
        form.add("pagesize", "10");
        form.add("jobGroup", String.valueOf(jobGroupId));
        form.add("triggerStatus", "-1");
        form.add("name", "");
        form.add("executorHandler", executorHandler);
        form.add("author", "");
        JSONObject data = postPageList("/jobinfo/pageList", form);
        JSONArray rows = data.getJSONArray("data");
        if (rows != null && !rows.isEmpty()) {
            return rows.getJSONObject(0).getInteger("id");
        }
        return null;
    }

    // ==================== 执行器（JobGroup）API ====================

    /** 按 appname 查找执行器 ID，不存在返回 null */
    public Integer findJobGroupIdByAppname(String appname) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("offset", "0");
        form.add("pagesize", "10");
        form.add("appname", appname);
        form.add("name", "");
        JSONObject data = postPageList("/jobgroup/pageList", form);
        JSONArray rows = data.getJSONArray("data");
        if (rows != null && !rows.isEmpty()) {
            return rows.getJSONObject(0).getInteger("id");
        }
        return null;
    }

    /** 创建执行器（appname 需与执行器配置一致，addressType=0 自动注册，accessToken 需与执行器一致） */
    public void createJobGroup(String appname, String name) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("appname", appname);
        form.add("name", name);
        form.add("addressType", "0");
        form.add("addressList", "");
        form.add("accessToken", accessToken == null ? "" : accessToken);
        postForm("/jobgroup/insert", form);
        logger.info("创建执行器成功: appname={}, name={}, accessToken={}", appname, name, accessToken);
    }

    /** 找不到就创建，返回执行器 ID */
    public int findOrCreateJobGroup(String appname, String name) {
        Integer id = findJobGroupIdByAppname(appname);
        if (id != null) {
            return id;
        }
        createJobGroup(appname, name);
        id = findJobGroupIdByAppname(appname);
        if (id == null) {
            throw new IllegalStateException("创建执行器失败: appname=" + appname);
        }
        return id;
    }

    // ==================== 内部实现 ====================

    /** 把 JobInfoRequest 转成表单参数 */
    private MultiValueMap<String, String> toForm(JobInfoRequest req) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        if (req.getId() != null) {
            form.add("id", String.valueOf(req.getId()));
        }
        form.add("jobGroup", String.valueOf(req.getJobGroup()));
        form.add("name", nvl(req.getName()));
        form.add("author", nvl(req.getAuthor(), "erik"));
        form.add("alarmEmail", "");
        form.add("scheduleType", nvl(req.getScheduleType(), "CRON"));
        form.add("scheduleConf", nvl(req.getScheduleConf()));
        form.add("misfireStrategy", nvl(req.getMisfireStrategy(), "DO_NOTHING"));
        form.add("executorRouteStrategy", nvl(req.getExecutorRouteStrategy(), "FIRST"));
        form.add("executorHandler", nvl(req.getExecutorHandler()));
        form.add("executorParam", nvl(req.getExecutorParam()));
        form.add("executorBlockStrategy", nvl(req.getExecutorBlockStrategy(), "SERIAL_EXECUTION"));
        form.add("executorTimeout", String.valueOf(nvl(req.getExecutorTimeout(), 0)));
        form.add("executorFailRetryCount", String.valueOf(nvl(req.getExecutorFailRetryCount(), 0)));
        form.add("glueType", nvl(req.getGlueType(), "BEAN"));
        form.add("glueSource", "");
        form.add("glueRemark", nvl(req.getGlueRemark()));
        form.add("childJobId", "");
        form.add("triggerStatus", String.valueOf(nvl(req.getTriggerStatus(), 1)));
        return form;
    }

    private String fullUrl(String path) {
        return adminAddresses + path;
    }

    private HttpHeaders buildHeaders(MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        if (loginCookie != null && !loginCookie.isEmpty()) {
            headers.add(HttpHeaders.COOKIE, loginCookie);
        }
        return headers;
    }

    /** 登录调度中心，保存会话 Cookie。3.x 用 POST /auth/doLogin，密码明文。 */
    public synchronized void login() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("userName", loginUsername);
        form.add("password", loginPassword);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);
        ResponseEntity<String> response =
                restTemplate.postForEntity(fullUrl("/auth/doLogin"), entity, String.class);

        List<String> setCookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        if (setCookies == null || setCookies.isEmpty()) {
            throw new IllegalStateException("xxl-job 登录失败，未返回会话 Cookie，响应: " + response.getBody());
        }
        // 3.x 下发的是 xxl_job_login_token，直接取第一个 Set-Cookie 的 name=value 即可
        String cookie = setCookies.stream()
                .map(c -> c.split(";")[0].trim())
                .findFirst()
                .orElse(null);
        if (cookie == null) {
            throw new IllegalStateException("xxl-job 登录失败，无有效 Cookie，响应: " + response.getBody());
        }
        this.loginCookie = cookie;
        logger.info("xxl-job 调度中心登录成功: {}", adminAddresses);
    }

    private synchronized void ensureLogin() {
        if (loginCookie == null || loginCookie.isEmpty()) {
            login();
        }
    }

    /**
     * 通用表单 POST，期望返回 {@code {"code":200,...}} 格式的 ReturnT JSON。
     * 会话失效（返回登录页 HTML）或网络异常时自动重新登录重试一次。
     */
    private JSONObject postForm(String path, MultiValueMap<String, String> form) {
        String body = postRaw(path, form);
        JSONObject json = JSON.parseObject(body);
        if (json.getIntValue("code") != SUCCESS_CODE) {
            throw new IllegalStateException("xxl-job 接口 [" + path + "] 调用失败: " + json.toJSONString());
        }
        return json;
    }

    /**
     * 分页接口（jobinfo/pageList、jobgroup/pageList）返回
     * {@code {"code":200,"data":{"data":[...],"total":N},...}}，取内层 data 对象。
     */
    private JSONObject postPageList(String path, MultiValueMap<String, String> form) {
        JSONObject json = postForm(path, form);
        return json.getJSONObject("data");
    }

    private String postRaw(String path, MultiValueMap<String, String> form) {
        ensureLogin();
        for (int attempt = 0; attempt < 2; attempt++) {
            String body;
            try {
                HttpEntity<MultiValueMap<String, String>> entity =
                        new HttpEntity<>(form, buildHeaders(MediaType.APPLICATION_FORM_URLENCODED));
                body = restTemplate.postForObject(fullUrl(path), entity, String.class);
            } catch (Exception e) {
                if (attempt == 0) {
                    logger.warn("xxl-job 接口 [{}] 调用异常，重新登录后重试一次: {}", path, e.getMessage());
                    login();
                    continue;
                }
                throw new IllegalStateException("xxl-job 接口 [" + path + "] 调用异常", e);
            }
            if (body == null || !body.trim().startsWith("{")) {
                // 返回了登录页 HTML，说明会话失效
                if (attempt == 0) {
                    logger.warn("xxl-job 接口 [{}] 返回非 JSON（可能登录态失效），重新登录后重试", path);
                    login();
                    continue;
                }
                throw new IllegalStateException("xxl-job 接口 [" + path + "] 返回非 JSON（可能未登录）: " + body);
            }
            return body;
        }
        throw new IllegalStateException("xxl-job 接口 [" + path + "] 调用失败");
    }

    private static String nvl(String v) {
        return v == null ? "" : v;
    }

    private static String nvl(String v, String def) {
        return v == null || v.trim().isEmpty() ? def : v;
    }

    private static int nvl(Integer v, int def) {
        return v == null ? def : v;
    }
}
