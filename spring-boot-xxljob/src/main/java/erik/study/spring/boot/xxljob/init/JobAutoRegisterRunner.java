package erik.study.spring.boot.xxljob.init;

import erik.study.spring.boot.xxljob.admin.JobAdminService;
import erik.study.spring.boot.xxljob.admin.JobInfoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 启动初始化：自动把两个业务任务注册到调度中心。
 *
 * <p>职责：
 * <ol>
 *   <li>确保“执行器（JobGroup）”在调度中心存在（不存在则通过 API 自动创建）；</li>
 *   <li>按 Handler 名称幂等注册任务：已存在则更新执行时间/参数，不存在则新增并启动；</li>
 *   <li>打印注册结果，方便在调度中心后台核对。</li>
 * </ol>
 *
 * <p>cron 说明（仅用于 Demo，便于快速看到效果）：
 * <ul>
 *   <li>订单自动取消：每分钟执行一次；</li>
 *   <li>优惠券到期提醒：每天 9 点执行一次（真实业务常用每天 9 点）。</li>
 * </ul>
 */
@Component
@Order(1)
public class JobAutoRegisterRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(JobAutoRegisterRunner.class);

    private final JobAdminService jobAdminService;

    @Value("${xxl.job.executor.appname}")
    private String appname;

    public JobAutoRegisterRunner(JobAdminService jobAdminService) {
        this.jobAdminService = jobAdminService;
    }

    @Override
    public void run(ApplicationArguments args) {
        logger.info("========== 启动初始化：自动注册 XXL-JOB 任务开始 ==========");
        try {
            // 1. 获取/创建执行器
            int jobGroupId = jobAdminService.findOrCreateJobGroup(appname, "spring-boot-study 示例执行器");
            logger.info("执行器就绪: appname={}, jobGroupId={}", appname, jobGroupId);

            // 2. 注册/更新：订单自动取消任务（每分钟扫描一次，演示用）
            JobInfoRequest orderJob = new JobInfoRequest();
            orderJob.setJobGroup(jobGroupId);
            orderJob.setName("订单自动取消任务");
            orderJob.setExecutorHandler("orderAutoCancelJobHandler");
            orderJob.setExecutorParam("{\"cancelMinutes\":30}");
            orderJob.setScheduleConf("0 0/1 * * * ?");
            registerOrUpdate(orderJob);

            // 3. 注册/更新：优惠券到期前1天提醒任务（每天9点执行）
            JobInfoRequest couponJob = new JobInfoRequest();
            couponJob.setJobGroup(jobGroupId);
            couponJob.setName("优惠券到期前1天提醒任务");
            couponJob.setExecutorHandler("couponExpireRemindJobHandler");
            couponJob.setExecutorParam("{\"days\":1}");
            couponJob.setScheduleConf("0 0 9 * * ?");
            registerOrUpdate(couponJob);

            logger.info("========== 启动初始化：自动注册 XXL-JOB 任务结束 ==========");
            logger.info("提示：可到调度中心后台【任务管理】查看/【调度日志】观察任务执行情况");
        } catch (Exception e) {
            logger.error("启动初始化自动注册 XXL-JOB 任务失败，请检查调度中心是否已启动、登录账号/地址配置是否正确", e);
        }
    }

    /** 幂等注册：任务已存在则更新执行时间/参数，否则新增 */
    private void registerOrUpdate(JobInfoRequest request) {
        Integer existId = jobAdminService.findJobIdByHandler(request.getJobGroup(), request.getExecutorHandler());
        if (existId != null) {
            request.setId(existId);
            jobAdminService.updateJob(request);
            logger.info("任务 [{}] 已存在(id={})，已更新执行时间 scheduleConf=[{}]",
                    request.getExecutorHandler(), existId, request.getScheduleConf());
        } else {
            int jobId = jobAdminService.registerJob(request);
            logger.info("任务 [{}] 注册成功(id={}), scheduleConf=[{}]",
                    request.getExecutorHandler(), jobId, request.getScheduleConf());
        }
    }
}
