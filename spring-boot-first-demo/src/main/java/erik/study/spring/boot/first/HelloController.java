package erik.study.spring.boot.first;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.java2d.pipe.SpanShapeRenderer;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(value = "/erik")
public class HelloController {

    private final static String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Value("${dev.value.name}")
    public String name;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!" + getDate();
    }

    @RequestMapping("/hello")
    public String indexHello() {
        return "Greetings from Spring Boot! hello,  " + name + getDate();
    }

    private String getDate() {
        return new SimpleDateFormat(DATE_PATTERN).format(new Date());
    }

}