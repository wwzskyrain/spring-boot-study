package erik.study.spring.boot.xxljob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * XXL-JOB 学习 Demo 启动类。
 *
 * <p>启动流程：
 * <ol>
 *   <li>{@code XxlJobConfig} 创建执行器并自动注册到调度中心（xxl-job-admin）；</li>
 *   <li>{@code JobAutoRegisterRunner} 通过调度中心管理 API 自动创建/更新两个业务任务；</li>
 *   <li>调度中心按 cron 触发任务，回调执行器上的 @XxlJob Handler。</li>
 * </ol>
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
