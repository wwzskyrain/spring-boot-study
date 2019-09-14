package erik.study.spring.boot.aop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dean.lee
 */
@RestController
public class TestController {

    @GetMapping("getInfo")
    public String getInfo(String name, Integer age){
        return name + ":" + age;
    }

    @Print(value = "printParamAndResult")
    @GetMapping("getString")
    public String getString(String src){
        return src;
    }
}