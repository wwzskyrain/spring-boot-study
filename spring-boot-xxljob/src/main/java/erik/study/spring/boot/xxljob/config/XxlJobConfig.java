package erik.study.spring.boot.xxljob.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * XXL-JOB 执行器配置。
 *
 * <p>作用：把当前应用变成“执行器”，启动时自动注册到调度中心（xxl-job-admin），
 * 并启动内嵌回调服务（默认端口 9999）等待调度中心触发任务。
 *
 * <p>对应 xxl-job 官方文档中的执行器配置项：
 * <ul>
 *   <li>{@code adminAddresses}：调度中心地址，逗号分隔可配多个；</li>
 *   <li>{@code appname}：执行器名称，调度中心“执行器管理”里 jobgroup 的 appname 需与之一致；</li>
 *   <li>{@code accessToken}：与调度中心通信的令牌，需和调度中心配置一致；</li>
 *   <li>{@code ip/port}：执行器回调地址；</li>
 *   <li>{@code logPath}：执行器日志目录；</li>
 *   <li>{@code logRetentionDays}：日志保留天数。</li>
 * </ul>
 */
@Configuration
public class XxlJobConfig {

    private static final Logger logger = LoggerFactory.getLogger(XxlJobConfig.class);

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor(
            @Value("${xxl.job.admin.addresses}") String adminAddresses,
            @Value("${xxl.job.access-token:}") String accessToken,
            @Value("${xxl.job.executor.appname}") String appname,
            @Value("${xxl.job.executor.ip:}") String ip,
            @Value("${xxl.job.executor.port}") int port,
            @Value("${xxl.job.executor.log-path}") String logPath,
            @Value("${xxl.job.executor.log-retention-days:30}") int logRetentionDays) {

        XxlJobSpringExecutor executor = new XxlJobSpringExecutor();
        executor.setAdminAddresses(adminAddresses);
        executor.setAppname(appname);
        executor.setIp(ip);
        executor.setPort(port);
        executor.setAccessToken(accessToken);
        executor.setLogPath(logPath);
        executor.setLogRetentionDays(logRetentionDays);

        logger.info("XXL-JOB 执行器初始化完成: adminAddresses={}, appname={}, ip={}, port={}",
                adminAddresses, appname, ip, port);
        return executor;
    }
}
