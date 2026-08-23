package erik.study.spring.boot.xxljob.handler;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 入门演示 Handler。
 *
 * <p>打印 hello world，并展示如何通过 {@link XxlJobHelper} 获取本次调度的业务信息：
 * 任务 ID、任务参数、分片序号/总数。
 */
@Component
public class DemoJobHandler {

    private static final Logger logger = LoggerFactory.getLogger(DemoJobHandler.class);

    @XxlJob("demoJobHandler")
    public void handle() {
        long jobId = XxlJobHelper.getJobId();
        String jobParam = XxlJobHelper.getJobParam();
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();

        // XxlJobHelper.log 会写入调度中心“调度日志”，在管理后台可直接查看
        XxlJobHelper.log("demoJobHandler 开始执行, jobId={}, jobParam={}, shard={}/{}",
                jobId, jobParam, shardIndex, shardTotal);
        logger.info("hello world ! jobId={}, jobParam={}, shardIndex={}, shardTotal={}",
                jobId, jobParam, shardIndex, shardTotal);
        System.out.println("hello world ... jobId=" + jobId + ", jobParam=" + jobParam);

        XxlJobHelper.log("demoJobHandler 执行结束");
        XxlJobHelper.handleSuccess("demo done, jobId=" + jobId);
    }
}
