[toc]

#1.认识注解
1.  @RestController = @Controller + @ResponseBody
2.  @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan

   
#2.新的知识点 
1.  类HelloController在加上Spring-boot的注解@SpringBootApplication，就构成了一个最简单的Spring-web项目。
    而且是restful风格哦！！！
2.  CommandLineRunner接口，如果有这样的Bean在spring容器中，该bean的run方法就会自动执行。这是除了SpringMVCWeb方式外，另一种让Spring-boot程序触发执行自定义代码的方式。

#3.关于测试
1.  HelloControllerTest中，有启动相应java中Application的内容吗？看不到相关的启动配置啊。
    1.  问题归纳描述：HelloControllerTest是如何找到erik.spring.bootApplication这个SpringBoot启动类并启动它，然后开始测试的？
    2.  回答：@SpringBootTest可以自动搜索@SpringBootConfiguration的bean，这里就是被@SpringBootApplication所注解了的Application；
        因为@SpringBootApplication继承了@
2.  在HelloControllerIT中，学习知识点
    1.  监听随即端口
    2.  @Before注解来初始化测试类中的成员变量；@Autowired来注入成员变量
    3.  TestRestTemplate？没有使用过，哈哈
    4.  wiki中有个名词"IT-integration test"=集成测试，想了解一下的话，[what is integration test](https://www.softwaretestinghelp.com/what-is-integration-testing/)
3.  

# 10.小结
1.  这篇demo的出处
2.  
      