package erik.study.spring.boot.integration.test;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author erik.wang
 * @date 2020-02-28 14:32
 * @description
 */
@SpringBootConfiguration
public class Config {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
