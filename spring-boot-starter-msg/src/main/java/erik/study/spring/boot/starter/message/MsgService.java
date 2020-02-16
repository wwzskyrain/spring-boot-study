package erik.study.spring.boot.starter.message;

/**
 * @author erik.wang
 * @date 2020-02-16 19:10
 * @description
 */
public class MsgService {

    /**
     * 访问发送短信的url地址
     */
    private String url;

    /**
     * 短信服务商提供的请求keyId
     */
    private String accessKeyId;

    /**
     * 短信服务商提供的KeySecret
     */
    private String accessKeySecret;

    public MsgService(String url, String accessKeyId, String accessKeySecret) {
        this.url = url;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
    }

    @Override
    public String toString() {
        return "MsgService{" +
                "url='" + url + '\'' +
                ", accessKeyId='" + accessKeyId + '\'' +
                ", accessKeySecret='" + accessKeySecret + '\'' +
                '}';
    }

    public int sendMsg(String msg) {
        // 调用http服务并发送消息，返回结果
        System.out.printf("msg %s has been send. with %s", msg, this);
        return 1;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }
}
