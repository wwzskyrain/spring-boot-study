# 1.spring-cloud-stream入门
1.  参考文章 [Spring Cloud Stream with RabbitMQ: Message-Driven Microservices](https://stackabuse.com/spring-cloud-stream-with-rabbitmq-message-driven-microservices)
2.  学的技能
    1.  SpringCloud对消息发送做了一层抽象：编程模型不在应对与具体的Broker，
    而是抽象出一种AMQP协议
    2.  spring-boot-cloud-stream-producer是消息发送方，spring-boot-cloud-stream-consumer是消息接收方

    3.  消息发送使用一个RestController来触发的：可见在spring-boot中
        构件一个Controller是多么容易。
        
    4.  在spring启动是，在命令行就可以指定spring-boot的参数，比如 
        `mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=9091`
    5.  测试了binding的group的概念

3.  思考：
    1.  对于抽象的思考：搞了一个抽象层，抽象是基于AMQP吗？它标准吗？
    2.  对于配置的思考：越来对底层知道的越少。
    
    
# 2.官方文档学习
1.  [官方文档地址](https://docs.spring.io/spring-cloud-stream/docs/Brooklyn.SR1/reference/htmlsingle/#_spring_cloud_stream_core)
2.  大体读了一遍，对编程模型、参数配置、抽象和实现大体有了一个了解，
    还对死信消息处理、分区等问题，看了一下。有官网代理demo，很详细，学习中。
    