package erik.study.spring.boot.rabbitmq.bean;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    private final static Logger logger = LoggerFactory.getLogger(Receiver.class);

    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(String message) {
        logger.info("Received <{}>", message);
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}