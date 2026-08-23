package erik.study.spring.boot.xxljob.handler.param;

/**
 * 优惠券到期提醒任务参数 DTO。
 *
 * <p>对应任务参数（executorParam）的 JSON 结构，例如：
 * <pre>{@code {"days":1}}</pre>
 * JSON 中缺省的字段会保留字段默认值，方便参数缺省时兜底。
 */
public class CouponExpireRemindParam {

    /** 提前提醒天数（默认 1 天） */
    private Integer days = 1;

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }
}
