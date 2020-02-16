# 1.项目简介
1.  这是SpringBoot官网上的一个demo。
2.  也是一个Restful web的独立运行程序：不需要启动tomcat和配置web.xml，不需要配置idea
    可以直接java命令执行，也可以借助idea的快捷图标来运行。
3.  以此为基础，可以来看一下springboot的特性
    1.  自动配置
    2.  starter的pom原理
    3.  属性文件的加载
    4.  springboot的ut

# 2.springboot特性之自动配置
1.  特性解释：要配置的是类，或者叫做bean；如果没有自动配置呢，那就只能
    程序员自己写代码配置跟中bean了。自动配置之后，程序员如何使用这些
    bean呢？这个简单，和手动配置一样，直接@Autowird就可以了。

2.  如何实现：springboot预先定义一些配置类，就是被注解@Configuration的类
    这些类的作用就是为了配置bean的。但是这些配置类的执行是有条件的，即满足了
    一定的条件之后，该配置类才发挥其特定的'注入XXX bean'的职能。当需要
    配置的bean需要参数怎么办？简单，使用配置文件application和预定义的参数
    名称就可以了，即把参数当做配置文件中的一个key，则其value就是参数值了。
    不同的bean有各自的特定的参数。OK，大功告成。

3.  下面详细讲解自动配置的各个实现要点
    1.条件注解 @Condition
          

# 3.springboot的属性配置

# 4.springboot的starter的maven原理

# 5.springboot的unit test