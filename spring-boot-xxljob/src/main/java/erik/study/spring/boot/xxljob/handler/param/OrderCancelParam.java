package erik.study.spring.boot.xxljob.handler.param;

/**
 * 订单自动取消任务参数 DTO。
 *
 * <p>对应任务参数（executorParam）的 JSON 结构，例如：
 * <pre>{@code {"cancelMinutes":30}}</pre>
 * JSON 中缺省的字段会保留字段默认值，方便参数缺省时兜底。
 */
public class OrderCancelParam {

    /** 支付超时分钟数（默认 30 分钟） */
    private Integer cancelMinutes = 30;

    public Integer getCancelMinutes() {
        return cancelMinutes;
    }

    public void setCancelMinutes(Integer cancelMinutes) {
        this.cancelMinutes = cancelMinutes;
    }
}
