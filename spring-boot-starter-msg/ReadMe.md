# 1.项目介绍
1.  基本把spring-boot的自动配置弄懂之后，通过开发一个'自定义spring-boot-starter'
    来整体巩固一下今天的知识。
2.  上面说的好听，其项目的代码完全是copy网上的一个demo。然而就这样就够了。
3.  补充：
    1.  网上教程：[SpringBoot自定义starter及自动配置](https://www.cnblogs.com/secbro/p/11810105.html)
    2.  关键单是要把 MATA_INF/spring.factories放到resources目录下，这样在maven打jar包的时候才会把该配置文件打进去。
    3.  其实这不算什么spring-boot-starter，可以给他起任何一个名字；
4.  项目被用在了'spring-boot-restful-web'中。