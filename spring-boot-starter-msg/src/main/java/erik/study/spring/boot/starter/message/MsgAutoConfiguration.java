package erik.study.spring.boot.starter.message;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@ConditionalOnClass(MsgService.class)
@EnableConfigurationProperties(MsgProperties.class)
public class MsgAutoConfiguration {

    /**
     * 注入属性配置类
     */
    @Resource
    private MsgProperties msgProperties;

    @Bean
    @ConditionalOnMissingBean(MsgService.class)
    @ConditionalOnProperty(prefix = "msg", value = "enabled", havingValue = "true")
    public MsgService msgService() {
        MsgService msgService = new MsgService(msgProperties.getUrl(), msgProperties.getAccessKeyId(),
                msgProperties.getAccessKeySecret());
        // 如果提供了其他set方法，在此也可以调用对应方法对其进行相应的设置或初始化。
        return msgService;
    }
}