package erik.study.spring.boot.xxljob.handler;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import erik.study.spring.boot.xxljob.admin.JobParamUtils;
import erik.study.spring.boot.xxljob.biz.Coupon;
import erik.study.spring.boot.xxljob.biz.CouponService;
import erik.study.spring.boot.xxljob.handler.param.CouponExpireRemindParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 业务任务二：优惠券到期前 1 天提醒。
 *
 * <p>业务场景：优惠券即将到期时，提前提醒用户使用，提升核销率。
 * 扫描即将到期（默认 1 天内）且未提醒过的优惠券，发送提醒并标记，避免重复提醒。
 *
 * <p>任务参数（executorParam）为 JSON，例如 {@code {"days":1}}，
 * 反序列化为 {@link CouponExpireRemindParam}，表示提前 1 天提醒。
 */
@Component
public class CouponExpireRemindJobHandler {

    private static final Logger logger = LoggerFactory.getLogger(CouponExpireRemindJobHandler.class);

    private final static String JOB_NAME = "couponExpireRemindJobHandler";
    private final CouponService couponService;

    public CouponExpireRemindJobHandler(CouponService couponService) {
        this.couponService = couponService;
    }

    @XxlJob(JOB_NAME)
    public void handle() {
        String param = XxlJobHelper.getJobParam();
        // 任务参数为 JSON，反序列化为业务 DTO（参数为空/解析失败时返回 null，走默认值）
        CouponExpireRemindParam config = JobParamUtils.parse(param, CouponExpireRemindParam.class);
        int days = config != null ? config.getDays() : 1;

        XxlJobHelper.log("===== 优惠券到期提醒任务开始, 提前 days={} 天 =====", days);
        List<Coupon> expiring = couponService.findExpiringSoon(days);
        for (Coupon coupon : expiring) {
            couponService.markReminded(coupon.getCouponNo());
            XxlJobHelper.log("提醒用户: 优惠券[{}] 将于 [{}] 到期，请尽快使用！",
                    coupon.getTitle(), coupon.getExpireTime());
            logger.info("发送优惠券到期提醒: couponNo={}, title={}, expireTime={}",
                    coupon.getCouponNo(), coupon.getTitle(), coupon.getExpireTime());
        }
        XxlJobHelper.log("===== 优惠券到期提醒任务结束, 本次提醒 {} 张 =====", expiring.size());
        XxlJobHelper.handleSuccess("本次提醒 " + expiring.size() + " 张优惠券");
    }
}
