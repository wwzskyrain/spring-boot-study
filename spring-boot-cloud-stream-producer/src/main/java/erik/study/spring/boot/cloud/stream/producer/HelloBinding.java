package erik.study.spring.boot.cloud.stream.producer;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

interface HelloBinding {

    @Output("greetingChannel")
    MessageChannel greeting();
}