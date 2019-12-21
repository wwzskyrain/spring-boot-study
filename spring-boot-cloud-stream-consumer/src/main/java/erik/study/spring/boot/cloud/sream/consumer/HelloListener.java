package erik.study.spring.boot.cloud.sream.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding(HelloBinding.class)
public class HelloListener {

    private static final Logger logger = LoggerFactory.getLogger(HelloListener.class);

    @StreamListener(target = HelloBinding.GREETING)
    public void processHelloChannelGreeting(String msg) {
        logger.info("Message content:{}",msg);
    }
}