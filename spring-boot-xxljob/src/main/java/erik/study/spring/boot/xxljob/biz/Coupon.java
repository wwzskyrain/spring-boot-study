package erik.study.spring.boot.xxljob.biz;

import java.time.LocalDateTime;

/**
 * 优惠券实体（Demo 用内存模拟，真实项目里对应数据库优惠券表）。
 */
public class Coupon {

    /** 优惠券编号 */
    private String couponNo;
    /** 优惠券标题/面额说明 */
    private String title;
    /** 过期时间 */
    private LocalDateTime expireTime;
    /** 是否已发送过到期提醒 */
    private boolean reminded;

    public Coupon() {
    }

    public Coupon(String couponNo, String title, LocalDateTime expireTime, boolean reminded) {
        this.couponNo = couponNo;
        this.title = title;
        this.expireTime = expireTime;
        this.reminded = reminded;
    }

    public String getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public boolean isReminded() {
        return reminded;
    }

    public void setReminded(boolean reminded) {
        this.reminded = reminded;
    }
}
