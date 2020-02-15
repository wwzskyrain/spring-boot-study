package erik.study.sprig.boot.property;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author erik.wang
 * @date 2020-02-15 17:01
 * @description
 */
@SpringBootApplication
public class Starter {

    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
        System.out.println("Starter over.");
    }

}
