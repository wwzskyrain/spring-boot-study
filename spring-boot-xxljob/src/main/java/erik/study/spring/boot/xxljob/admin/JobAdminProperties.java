package erik.study.spring.boot.xxljob.admin;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 调度中心（xxl-job-admin）管理接口相关配置，前缀 {@code xxl.job.admin}。
 *
 * <p>注意：这里的 {@code loginUsername / loginPassword} 是调度中心“管理后台”的登录账号，
 * 用于调用 jobinfo / jobgroup 等管理 API（需要登录会话 Cookie）。
 */
@Component
@ConfigurationProperties(prefix = "xxl.job.admin")
public class JobAdminProperties {

    /** 调度中心根地址，如 http://localhost:8080/xxl-job-admin */
    private String addresses;

    /** 管理后台登录账号（默认 admin） */
    private String loginUsername = "admin";

    /** 管理后台登录密码（明文，程序会自动做 MD5 后提交） */
    private String loginPassword = "123456";

    public String getAddresses() {
        return addresses;
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
    }

    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }
}
