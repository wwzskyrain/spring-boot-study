# SpringBoot的运作原理和实现分析

## 1.启动流程
### 1.1 @SpringBootApplication和SpringApplication类
1.  SpringBootApplication注解是Configuration、EnableAutoConfiguration、ComponentScan的三位一体
2.  Configuration是IOC容器的配置类，
    1.  理论上程序运用中用到的大个头的类都要有IOC管理比较方便，方便之处在于用IOC提供控制反转功能和自动注入功能。
        控制反转是IOC的主要功能，而自动注入是它的特色功能。
    2.  哪些类需要交给IOC呢？
        1.  构造比较麻烦的对象，比如参数过多，如远端rpc的实现类对象，还有我们自定义的一些大对象。
        2.  构造成本比较高而且有复用价值的对象，比如RabbitMQ的链接对象、模板对象。
        3.  我们自定义的大对象，比如我们自定义的Util，而哪些小对象，比如XxxDto，就直接在用到的时候直接new就行了
        4.  【补】从以上三点可看出，能交给IOC控制的，一般都是'函数式对象'，就是他们内部没有状态。当然也有很多有状态的，比如Request级别的、Session级别的。
    3.  IOC如何定位这个配置类呢
        1.  在Spring中，使用`new AnnotationConfigApplicationContext(TestConfiguration.class)`
            由程序指出配置类；
        2.  在SpringBoot中呢，因为SpringBoot是不手动创建ApplicationContext的，所以在SpringBoot中如果定位
            配置类呢？这个问题我看了一个下午的源码，都没有看出端倪【太不熟悉源码了】。不过看一个帖子中有相应描述，
            `new PackageImport(metadata).getPackageName()，它其实返回了当前主程序类的同级以及子级的包组件`， 
            猜测SpringBoot是在启动类【SpringBoot启动类就是指运行SpringApplication.run的类】所在的目录及其子目录下查找被@Configuration修饰的配置类的。
            但是这个猜测是不正确的；通过实验发现，启动类并不会刻意的去找被Configuration注解的类，而是感知被ComponentScan注解的类；然后根据"组件扫描"去找
            被Configuration注解的类，这样就找到了spring的配置类了[ComponentScan默认是扫描当前包及其子包]。除了程序中用@Configuration注解的类，SpringBoot还有自动配置类，
            它们也是SpringBoot开启的IOC的配置类。请见继续看。
    4.  SpringBoot启动的IOC如何自动加载配置的类呢？
        1.  自动加载的效果：首先SpringBoot感知到classpath中有相应的工作类，比如RabbitMQ相关的类，这是就会加载相关的Bean，比如
        2.  如何实现：SpringBoot在启动时会读特定的配置文件WEB-INF/spring-factories.properties，这里面有特定的自动配置类【是配置类的一种，
            自动配置类的作用机制待研究】比如RabbitAutoConfiguration，其配置语法和平常程序中编写配置类是一样的。而且SpringBoot的自动加载Bean
            的功能就是在这里的配置类中实现的；具体是通过一系列的@ConditionalOnXxx注解实现的。SpringBoot条件配置的入门使用，
            可参见[Spring @Conditional注解 详细讲解及示例](https://blog.csdn.net/xcy1193068639/article/details/81491071)
        3.  IOC容器在加载了配置类之后，是如何按照配置类来获取Bean元数据并初始化它们的呢？这是一个后续问题
    5.  IOC如何获取配置类内配置的Bean呢？
        1.  这个问题就属于配置语法的问题了，是个大问题。
3.  SpringBoot的启动注解小结：
    1.  启动是通过在main方法中调用SpringApplication.run静态方法开启的。调用给静态方法的类叫做启动类
    2.  启动类会首先把先从自己头上开始定位注解并催动Bean的加载：
        1.  首先是ComponentScan注解；该注解有两个作用，一是可以通过指明'basePackageClasses'或者'basePackages'来明确定义要加载哪些Bean定义；
            二是从当前被ComponentScan注解的类所在位置开始寻找被Configuration注解的类，为作为IOC容器的Bean配置。【这样看来，Spring和SpringBoot
            在加载配置上除了一个是xml文件一个是基于Java类这一点不同外，还有就是加载触发不同。Spring是通过指定xml后，在配置文件xml中配置ComponentScan，而SpringBoot
            则刚好相反，而是通过@ComponentScan注解来配置JavaConfig配置类】
        2.  其次很重要的就是@EnableAutoConfiguration注解了，其作用效果是开启SpringBoot的自动配置【SpringBoot的自动配置具体表现是？】；

## 2.启动流程的源码分析
1.  其实Spring的源码分析我早就想看了，前年浏览过一次，没有看太懂，那时候心浮气躁，能力有限，今后肯定要有耐心的梳理一下它的。


## 3.小结
1.  Spring的启动过程基本弄懂了，下面要写另一个SpringBoot的demo，那就是SpringBoot-RabbitMQ相关
2.  这个demo似乎找不到出处了
3.  重点总结：
    1.  ComponentScan注解的两个作用
    2.  Spring和SpringBoot启动的Bean配置加载方式不同
    3.  SpringBoot的条件自动加载机制
4.  参考阅读
    1.  [spring4.0之二：@Configuration的使用]https://www.cnblogs.com/duanxz/p/7493276.html 其对Configuration注解的使用方法做了很详细的实例和说明