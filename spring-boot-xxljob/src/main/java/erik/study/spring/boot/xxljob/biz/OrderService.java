package erik.study.spring.boot.xxljob.biz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 订单服务（Demo 用内存 Map 模拟订单数据，演示“订单自动取消”业务的扫描逻辑）。
 */
@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final Map<String, Order> orders = new ConcurrentHashMap<>();

    public OrderService() {
        // 模拟数据：两条超时未支付、一条已支付、一条昨天未支付
        orders.put("O202608230001", new Order("O202608230001", new BigDecimal("199.00"),
                Order.STATUS_UNPAID, LocalDateTime.now().minusMinutes(2)));
        orders.put("O202608230002", new Order("O202608230002", new BigDecimal("59.90"),
                Order.STATUS_UNPAID, LocalDateTime.now().minusMinutes(40)));
        orders.put("O202608230003", new Order("O202608230003", new BigDecimal("999.00"),
                Order.STATUS_PAID, LocalDateTime.now().minusHours(1)));
        orders.put("O202608230004", new Order("O202608230004", new BigDecimal("39.90"),
                Order.STATUS_UNPAID, LocalDateTime.now().minusDays(1)));
    }

    /**
     * 取消超过 {@code cancelMinutes} 分钟仍未支付的订单。
     *
     * @param cancelMinutes 支付超时分钟数
     * @return 本次被取消的订单列表
     */
    public List<Order> cancelExpiredUnpaidOrders(int cancelMinutes) {
        LocalDateTime deadline = LocalDateTime.now().minusMinutes(cancelMinutes);
        List<Order> cancelled = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getStatus() == Order.STATUS_UNPAID && order.getCreateTime().isBefore(deadline)) {
                order.setStatus(Order.STATUS_CANCELLED);
                cancelled.add(order);
            }
        }
        logger.info("订单取消扫描完成: 超时[{}]分钟, 本次取消[{}]笔", cancelMinutes, cancelled.size());
        return cancelled;
    }
}
