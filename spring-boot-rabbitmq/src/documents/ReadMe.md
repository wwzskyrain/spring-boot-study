# 1.项目架构
## 1.1 类架构
1.  Application是SpringApplication.run启动的，是启动类；位于最顶层。SendMessageRunner是用于执行发送
    消息的逻辑载体。也位于最顶层，也可以放在bean包下。Application启动类会扫描当前路径和子路径并加载bean包下的
    Component组件【以及和Component同样用户标识组件的其他三个注解-Controller、Service】，和config包下面的
    配置类。
2.  Receiver类位于bean包下，被启动类Application的ComponentScan注解发现并加载。
3.  RabbitMqConfig类位于config包下，被启动类Application的ComponentScan注解发现并加载。
## 1.2 运行流程
1.  Application启动并加载需要的Bean之后，SendMessageRunner的run()方法首先被执行，进而发送消息到RabbitMQ的服务器
2.  Receiver则注册监听到消息，其被MessageListenerAdapter来催动调用其注册方法'receiveMessage(String message)'，
    进而消费消息。
3.  运行完毕，程序运行结束

# 2.收获小结
1.  log日志配置方法超简单，只需要在resources放log4j.properties文件就好
2.  rabbitMQ的相关bean都是被自动加载的，但是可以通过application.properties修改配置参数，比如host/port/
3.  在此熟悉了SpringBoot开启的IOC的配置方式
4.  关于RabbitMQ又get到新技能，比如MessageListenerAdapter，通过适配器将普通的POJO对象变成一个消息处理者，
    连消息的反序列化都做好了
5.  
    