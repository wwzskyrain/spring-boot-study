package erik.study.spring.boot.first.main;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Date 2019-08-25
 * @Created by erik
 */
@Configuration
public class InnerConfiguration {

    {
        System.out.println(InnerConfiguration.class.getName() + " instance block!!!");
    }

}
