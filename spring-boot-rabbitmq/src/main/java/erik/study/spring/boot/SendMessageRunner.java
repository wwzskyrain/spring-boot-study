package erik.study.spring.boot;

import java.util.concurrent.TimeUnit;

import erik.study.spring.boot.rabbitmq.bean.Receiver;
import erik.study.spring.boot.rabbitmq.config.RabbitMqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class SendMessageRunner implements CommandLineRunner {

    private final static Logger logger = LoggerFactory.getLogger(SendMessageRunner.class);

    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;

    public SendMessageRunner(Receiver receiver, RabbitTemplate rabbitTemplate) {
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) throws Exception {

        logger.info("Sending-message start");

        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend(RabbitMqConfig.topicExchangeName,
                    "foo.bar.baz",
                    String.format("welcome to rabbitMQ! erik:[%d]", i));
            receiver.getLatch().await();
        }

        logger.info("Sending-message end.");
    }

}