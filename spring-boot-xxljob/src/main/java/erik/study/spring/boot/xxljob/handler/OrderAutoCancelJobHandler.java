package erik.study.spring.boot.xxljob.handler;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import erik.study.spring.boot.xxljob.admin.JobParamUtils;
import erik.study.spring.boot.xxljob.biz.Order;
import erik.study.spring.boot.xxljob.biz.OrderService;
import erik.study.spring.boot.xxljob.handler.param.OrderCancelParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 业务任务一：订单自动取消。
 *
 * <p>业务场景：用户下单后超过一定时间未支付，系统自动取消订单释放库存。
 * 这里用内存中的模拟订单数据演示扫描逻辑，真实项目里换成查库 + 批量更新即可。
 *
 * <p>任务参数（executorParam）为 JSON，例如 {@code {"cancelMinutes":30}}，
 * 反序列化为 {@link OrderCancelParam}，表示超过 30 分钟未支付就取消。
 */
@Component
public class OrderAutoCancelJobHandler {

    private static final Logger logger = LoggerFactory.getLogger(OrderAutoCancelJobHandler.class);

    private final OrderService orderService;

    public OrderAutoCancelJobHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    @XxlJob("orderAutoCancelJobHandler")
    public void handle() {
        String param = XxlJobHelper.getJobParam();
        // 任务参数为 JSON，反序列化为业务 DTO（参数为空/解析失败时返回 null，走默认值）
        OrderCancelParam config = JobParamUtils.parse(param, OrderCancelParam.class);
        int cancelMinutes = config != null ? config.getCancelMinutes() : 30;

        XxlJobHelper.log("===== 订单自动取消任务开始, cancelMinutes={} =====", cancelMinutes);
        List<Order> cancelled = orderService.cancelExpiredUnpaidOrders(cancelMinutes);
        for (Order order : cancelled) {
            XxlJobHelper.log("订单[{}] 金额[{}] 创建于[{}]，超过[{}]分钟未支付，已自动取消",
                    order.getOrderNo(), order.getAmount(), order.getCreateTime(), cancelMinutes);
            logger.info("订单已自动取消: orderNo={}, amount={}, createTime={}",
                    order.getOrderNo(), order.getAmount(), order.getCreateTime());
        }
        XxlJobHelper.log("===== 订单自动取消任务结束, 本次取消 {} 笔 =====", cancelled.size());
        XxlJobHelper.handleSuccess("本次自动取消订单 " + cancelled.size() + " 笔");
    }
}
