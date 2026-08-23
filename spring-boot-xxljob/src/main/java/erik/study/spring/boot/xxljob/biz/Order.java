package erik.study.spring.boot.xxljob.biz;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体（Demo 用内存模拟，真实项目里对应数据库订单表）。
 */
public class Order {

    /** 待支付 */
    public static final int STATUS_UNPAID = 0;
    /** 已支付 */
    public static final int STATUS_PAID = 1;
    /** 已取消 */
    public static final int STATUS_CANCELLED = 2;

    /** 订单号 */
    private String orderNo;
    /** 订单金额 */
    private BigDecimal amount;
    /** 订单状态：0 待支付 / 1 已支付 / 2 已取消 */
    private int status;
    /** 下单时间 */
    private LocalDateTime createTime;

    public Order() {
    }

    public Order(String orderNo, BigDecimal amount, int status, LocalDateTime createTime) {
        this.orderNo = orderNo;
        this.amount = amount;
        this.status = status;
        this.createTime = createTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
