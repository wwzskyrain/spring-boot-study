package erik.study.spring.boot.rabbitmq.bean;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    private final static Logger logger = LoggerFactory.getLogger(Receiver.class);

    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(String message) throws InterruptedException {
        logger.info("Received <{}>", message);

        TimeUnit.SECONDS.sleep(5);
        latch.countDown(); //上一句等了5秒，这里再放开这个'latch'.
        logger.info("Received-message over.");
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}