package erik.study.spring.boot.xxljob.biz;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * 优惠券服务（Demo 用内存模拟，演示“优惠券到期提醒”业务的扫描逻辑）。
 */
@Service
public class CouponService {

    private final List<Coupon> coupons = new CopyOnWriteArrayList<>();

    public CouponService() {
        // 模拟数据：一张明天到期、一张5天后到期、一张今天到期、一张已提醒
        coupons.add(new Coupon("CPN0001", "满100减20", LocalDateTime.now().plusDays(1), false));
        coupons.add(new Coupon("CPN0002", "新人立减10", LocalDateTime.now().plusDays(5), false));
        coupons.add(new Coupon("CPN0003", "全场8折券", LocalDateTime.now().plusHours(10), false));
        coupons.add(new Coupon("CPN0004", "满300减50", LocalDateTime.now().plusDays(3), true));
    }

    /**
     * 查找 {@code days} 天内即将到期且尚未提醒过的优惠券。
     *
     * @param days 提前提醒天数
     * @return 需要提醒的优惠券列表
     */
    public List<Coupon> findExpiringSoon(int days) {
        LocalDateTime threshold = LocalDateTime.now().plusDays(days);
        return coupons.stream()
                .filter(c -> !c.isReminded())
                .filter(c -> c.getExpireTime().isBefore(threshold))
                .collect(Collectors.toList());
    }

    /** 标记某张优惠券已发送过提醒（防止重复提醒） */
    public void markReminded(String couponNo) {
        coupons.stream()
                .filter(c -> c.getCouponNo().equals(couponNo))
                .forEach(c -> c.setReminded(true));
    }
}
