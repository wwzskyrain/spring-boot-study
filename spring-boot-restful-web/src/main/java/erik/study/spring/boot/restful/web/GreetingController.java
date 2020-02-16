package erik.study.spring.boot.restful.web;

import java.util.concurrent.atomic.AtomicLong;

import erik.study.spring.boot.starter.message.MsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    private static final Logger logger = LoggerFactory.getLogger(GreetingController.class);
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

//    @Autowired
//    private ShowBean showBean;

    @Autowired
    private MsgService msgService;

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {

        msgService.sendMsg(name);

        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}