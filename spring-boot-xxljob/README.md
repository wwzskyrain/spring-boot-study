# spring-boot-xxljob —— XXL-JOB 调度中心接入 Demo

> 一个学习 XXL-JOB 的完整示例：封装调度中心管理 API、编写执行器 Handler、演示两种真实业务任务，
> 并支持启动时自动把任务注册到调度中心。
>
> ⚠️ 适配说明：本模块适配的是 **xxl-job 3.5.0-SNAPSHOT（GitHub master 源码）** 的调度中心，
> 需要 **JDK 17 + Spring Boot 3.x**（与父工程 Java 8 不同，本模块单独用 JDK 17 编译）。

## 一、整体架构

```
              调度中心 xxl-job-admin (http://localhost:8080，3.5.0-SNAPSHOT)
             /                                    \
  管理 API（/auth/doLogin + jobinfo/jobgroup）   调度回调（HTTP 触发，带 XXL-JOB-APPNAME 头）
             \                                    /
        本模块 = 执行器 (xxl-job-executor-study, 端口 9999)
                     |
              @XxlJob Handler 执行业务
```

- **调度中心**：管理任务、下发调度（cron 到点触发）、记录调度日志。
- **执行器**：本模块。启动时自动注册到调度中心，接收触发回调并执行 `@XxlJob` 方法。
- **管理 API**：本模块的 `JobAdminService` 通过调度中心后台 HTTP 接口自动完成“注册/更新/删除任务”。

## 二、模块结构

```
src/main/java/erik/study/spring/boot/xxljob/
├── Application.java                启动类
├── config/XxlJobConfig.java        执行器配置（注册中心地址、appname、端口等），启动自动注册执行器
├── admin/
│   ├── JobAdminProperties.java     调度中心管理接口配置（地址、登录账号）
│   ├── JobInfoRequest.java         任务参数 DTO（对应调度中心 JobInfo）
│   ├── JobParamUtils.java          任务参数解析工具（JSON 反序列化为业务 DTO）
│   └── JobAdminService.java        ★ 调度中心 API 封装：登录 / 注册任务 / 更新任务 / 删除任务 / 启停 / 触发
├── biz/
│   ├── Order / OrderService        订单业务（内存模拟，演示“订单自动取消”）
│   └── Coupon / CouponService      优惠券业务（内存模拟，演示“到期提醒”）
├── handler/
│   ├── DemoJobHandler.java         入门演示：打印 hello world + 任务业务信息
│   ├── OrderAutoCancelJobHandler.java     业务任务一：订单自动取消
│   ├── CouponExpireRemindJobHandler.java  业务任务二：优惠券到期前1天提醒
│   └── param/                      业务任务参数 DTO（与 executorParam 的 JSON 一一对应）
│       ├── OrderCancelParam.java        {"cancelMinutes":30}
│       └── CouponExpireRemindParam.java {"days":1}
└── init/JobAutoRegisterRunner.java 启动初始化：自动创建执行器 + 幂等注册/更新任务
```

## 三、启动前准备

1. 本地已部署并启动 xxl-job 调度中心（3.x 版，`http://localhost:8080`，**无 `/xxl-job-admin` 前缀**）。
2. 确认调度中心登录账号密码（默认 `admin / 123456`）与 `xxl.job.access-token`（默认 `default_token`）。
3. **安装匹配的 xxl-job-core（3.5.0-SNAPSHOT）到本地 Maven 仓库**（见下方“附录 A”）。
4. 本模块编译/运行需要 **JDK 17+**（Maven 编译目标为 17）。

## 四、运行

```bash
# 用 JDK 17 运行（若默认 JDK 非 17，可用 JAVA_HOME 指定）
JAVA_HOME=/path/to/jdk17 mvn -pl spring-boot-xxljob spring-boot:run
# 或直接在 IDE 里运行 Application 的 main 方法（选择 JDK 17）
```

启动后观察日志，会自动完成：

1. **执行器注册**：`XxlJobConfig` 把当前应用注册到调度中心（AppName：`xxl-job-executor-study`，端口 9999）。
2. **自动建任务**：`JobAutoRegisterRunner` 调用调度中心 API：
   - 创建/找到执行器（JobGroup）；
   - 注册 **订单自动取消任务**（`orderAutoCancelJobHandler`，cron：`0 0/1 * * * ?` 每分钟）；
   - 注册 **优惠券到期前1天提醒任务**（`couponExpireRemindJobHandler`，cron：`0 0 9 * * ?` 每天9点）。
   - 若同名任务已存在则改为“更新执行时间”，不会重复创建（幂等）。

然后到调度中心后台【任务管理】查看任务、【调度日志】观察执行，执行器控制台也会打印业务日志。

## 五、业务任务说明

### 1. 订单自动取消（`orderAutoCancelJobHandler`）
- 场景：下单后 N 分钟未支付自动取消订单、释放库存。
- 参数：JSON，如 `{"cancelMinutes":30}`（支付超时分钟数），对应 DTO：`handler/param/OrderCancelParam`。
- 逻辑：扫描内存模拟订单，超过 N 分钟仍未支付的置为“已取消”并输出日志。

### 2. 优惠券到期前1天提醒（`couponExpireRemindJobHandler`）
- 场景：优惠券即将到期，提前 1 天提醒用户使用。
- 参数：JSON，如 `{"days":1}`（提前提醒天数），对应 DTO：`handler/param/CouponExpireRemindParam`。
- 逻辑：扫描即将到期且未提醒过的优惠券，发送提醒（Demo 为打印日志）并标记 `reminded`，避免重复提醒。

### 3. 入门演示（`demoJobHandler`）
- 打印 `hello world`，并展示 `XxlJobHelper` 获取任务 ID、参数、分片序号/总数。
- 需要体验时，可在调度中心后台手动添加一个任务，Handler 填 `demoJobHandler`。

> 修改任务执行时间：直接改 `JobAutoRegisterRunner` 里的 `setScheduleConf(...)`，重启即可自动更新到调度中心；也可以在调度中心后台手动改。

## 六、JobAdminService 常用方法（对应调度中心 HTTP API，3.x 已实测）

| 方法 | 对应 HTTP 接口 | 说明 |
|------|----------------|------|
| `login()` | `POST /auth/doLogin` | 登录获取会话 Cookie（3.x 密码明文） |
| `registerJob(JobInfoRequest)` | `POST /jobinfo/insert` | 注册新任务（返回新 ID 在 `data`） |
| `updateJob(JobInfoRequest)` | `POST /jobinfo/update` | 更新任务（主要更新执行时间 scheduleConf） |
| `removeJob(jobId)` | `POST /jobinfo/delete` | 删除任务（`ids[]` 参数） |
| `startJob / stopJob` | `POST /jobinfo/start` / `stop` | 启动 / 停止任务（`ids[]` 参数） |
| `triggerJob(jobId, param)` | `POST /jobinfo/trigger` | 手动触发一次 |
| `findJobIdByHandler(...)` | `POST /jobinfo/pageList` | 按 Handler 幂等查询任务 |
| `findOrCreateJobGroup(...)` | `POST /jobgroup/pageList` + `insert` | 查找/创建执行器 |

对应 curl 示例（3.x、根路径、已实测）：

```bash
# 1) 登录拿 Cookie（3.x 密码明文，不用 MD5；登录页在 /auth/login）
curl -s -i -X POST 'http://localhost:8080/auth/doLogin' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  --data 'userName=admin&password=123456' -c /tmp/xxl.cookie
# 返回 Set-Cookie: xxl_job_login_token=...

# 2) 新增任务（name 字段；executorParam 为 URL 编码后的 JSON {"cancelMinutes":30}）
curl -X POST 'http://localhost:8080/jobinfo/insert' \
  -b /tmp/xxl.cookie -H 'Content-Type: application/x-www-form-urlencoded' \
  --data 'jobGroup=3&name=%E8%AE%A2%E5%8D%95%E8%87%AA%E5%8A%A8%E5%8F%96%E6%B6%88&author=erik&scheduleType=CRON&scheduleConf=0+0/1+*+*+*+?&glueType=BEAN&executorHandler=orderAutoCancelJobHandler&executorParam=%7B%22cancelMinutes%22%3A30%7D&executorRouteStrategy=FIRST&misfireStrategy=DO_NOTHING&executorBlockStrategy=SERIAL_EXECUTION&executorTimeout=0&executorFailRetryCount=0&triggerStatus=1'

# 3) 更新执行时间（需带任务 id）
curl -X POST 'http://localhost:8080/jobinfo/update' \
  -b /tmp/xxl.cookie -H 'Content-Type: application/x-www-form-urlencoded' \
  --data 'id=7&jobGroup=3&name=%E8%AE%A2%E5%8D%95%E8%87%AA%E5%8A%A8%E5%8F%96%E6%B6%88&author=erik&scheduleType=CRON&scheduleConf=0+0/5+*+*+*+?&glueType=BEAN&executorHandler=orderAutoCancelJobHandler&executorParam=%7B%22cancelMinutes%22%3A30%7D&executorRouteStrategy=FIRST&misfireStrategy=DO_NOTHING&executorBlockStrategy=SERIAL_EXECUTION&executorTimeout=0&executorFailRetryCount=0&triggerStatus=1'

# 4) 删除任务（ids[] 数组参数）
curl -X POST 'http://localhost:8080/jobinfo/delete' \
  -b /tmp/xxl.cookie -H 'Content-Type: application/x-www-form-urlencoded' \
  --data 'ids[]=7'

# 5) 官方 OpenAPI（无需登录，用 appname+accessToken 头，适合程序化注册）
curl -X POST 'http://localhost:8080/api/addJob' \
  -H 'Content-Type: application/json' \
  -H 'XXL-JOB-APPNAME: xxl-job-executor-study' \
  -H 'XXL-JOB-ACCESS-TOKEN: default_token' \
  --data '{"jobGroup":3,"name":"测试任务","scheduleType":"CRON","scheduleConf":"0 0 0 * * ? *","glueType":"BEAN","executorHandler":"orderAutoCancelJobHandler","executorParam":"{\"cancelMinutes\":30}","triggerStatus":1}'
```

## 七、任务参数如何用 DTO 序列化 / 反序列化

`executorParam` 只是一个字符串，我们约定它为 JSON，业务方自定义一个 DTO 与之对应即可：

```java
// 1. 业务方自定义参数 DTO（字段给默认值，JSON 缺省时自动兜底）
public class OrderCancelParam {
    private Integer cancelMinutes = 30;   // 支付超时分钟数，默认 30
    // getter / setter ...
}

// 2. 在 Handler 里反序列化
@XxlJob("orderAutoCancelJobHandler")
public void handle() {
    OrderCancelParam config = JobParamUtils.parse(XxlJobHelper.getJobParam(), OrderCancelParam.class);
    int cancelMinutes = config != null ? config.getCancelMinutes() : 30;
    // ...
}
```

- 调度中心任务配置里的“任务参数”填 JSON：`{"cancelMinutes":30}`；
- 模块内置的 `JobParamUtils.parse(json, XxxDto.class)` 用 fastjson 反序列化，
  参数为空 / 解析失败时返回 `null`，业务方再走默认值即可；
- 字段缺省会保留 DTO 里的默认值，所以 DTO 里建议给字段赋默认值。

> `JobParamUtils.getInt(...)` 仍保留，兼容旧的 `key=value` 写法。

## 八、关键点速记

- **调度中心 vs 执行器**：调度中心负责“管”，执行器负责“干”；两者用 `accessToken` 校验。
- **任务注册三种方式**：后台手动建、调用管理 API、本模块启动自动注册。
- **Handler 与任务的绑定**：`@XxlJob("orderAutoCancelJobHandler")` 的值 = 调度中心任务配置里的 `JobHandler`。
- **获取任务信息**：`XxlJobHelper.getJobId() / getJobParam() / getShardIndex() / getShardTotal()`，
  `XxlJobHelper.log()` 写调度日志，`handleSuccess/handleFail` 标记执行结果。
- **任务参数**：约定为 JSON，用 `JobParamUtils.parse` 反序列化为业务 DTO，避免字符串硬拼。
- **幂等注册**：先 `findJobIdByHandler` 判断，存在则 update，否则 add。
- **执行器 IP 要固定**：本机多网卡/虚拟网卡（如 `198.18.0.1`）会导致自动检测的 IP 异常，调度派发失败。
  务必在 `application.properties` 里设 `xxl.job.executor.ip=127.0.0.1`（本项目已设置）。

## 附录 A：构建并安装匹配的 xxl-job-core（3.5.0-SNAPSHOT）

你的本地调度中心是 GitHub master 源码构建的 **3.5.0-SNAPSHOT**，其 `/api/*` 协议要求
`XXL-JOB-ACCESS-TOKEN` + `XXL-JOB-APPNAME` 两个请求头；而 Maven 中央仓库最新发布的
`xxl-job-core 3.4.2` 只发 token 头，注册/回调都会失败。所以必须把匹配的 core 装进本地仓库：

```bash
git clone --depth 1 https://github.com/xuxueli/xxl-job.git /tmp/xxl-job-master
cd /tmp/xxl-job-master
mvn -pl xxl-job-core -am install -DskipTests -Dgpg.skip=true -Dmaven.javadoc.skip=true
# 验证：~/.m2/repository/com/xuxueli/xxl-job-core/3.5.0-SNAPSHOT/ 下应有 jar
```

> 若以后你换了调度中心版本，只需改 `pom.xml` 里的 `<xxl-job.version>`，并保证和调度中心协议一致。

## 附录 B：本次踩坑记录

| 问题 | 根因 | 解法 |
|------|------|------|
| `/jobgroup/pageList` 等 404 | 调度中心部署在**根路径**（无 `/xxl-job-admin` 前缀） | 基础地址用 `http://localhost:8080` |
| 登录 `/login` 404 | 3.x 登录接口是 `POST /auth/doLogin`，密码**明文** | 见上文 curl |
| `accessToken or appname is empty` | 官方 release core（3.4.2）不带 `XXL-JOB-APPNAME` 头，与 3.5.0-SNAPSHOT 调度中心不匹配 | 用 3.5.0-SNAPSHOT core（附录 A） |
| 执行器在线但任务触发失败 | 自动检测 IP 是虚拟网卡 `198.18.0.1`，调度中心派发失败 | `xxl.job.executor.ip=127.0.0.1` |
| 执行器日志路径 `/data/...` 只读 | xxl-job 默认 Linux 路径，macOS `/data` 不可写 | `xxl.job.executor.log-path=${user.home}/logs/xxl-job/jobhandler` |
| 新增任务用 `jobDesc` 字段无效 | 3.x 字段改名 `name`，新增接口是 `/jobinfo/insert`，删除/启停用 `ids[]` | 见 `JobAdminService` 实现 |

