package erik.study.spring.boot.first;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Date 2019-08-25
 * @Created by erik
 */
@SpringBootConfiguration
@PropertySource(value = "classpath:application-dev.properties")
public class ErikSpringBootConfig {

    static {
        System.out.println(ErikSpringBootConfig.class.getName() + " static block!!!");
    }

    {
        System.out.println(ErikSpringBootConfig.class.getName() + " instance block!!!");
    }
}
