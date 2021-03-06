package erik.study.spring.boot.cloud.sream.consumer;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface HelloBinding {

    String GREETING = "greetingChannel";

    @Input(GREETING)    //此处只是一个声明
    SubscribableChannel greeting();
}