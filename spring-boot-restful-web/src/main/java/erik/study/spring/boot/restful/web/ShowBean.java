package erik.study.spring.boot.restful.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author erik.wang
 * @date 2020-02-16 15:18
 * @description
 */
@Component
public class ShowBean implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(ShowBean.class);
    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.info("ShowBean has been set application");
        context = applicationContext;
    }

    /**
     * 打印出当前 ApplicationContext中的所有bean，主要是想看看那么多的 **AutoConfiguration，比如
     * PropertyPlaceholderAutoConfiguration
     * WebSocketServletAutoConfiguration
     * HttpMessageConvertersAutoConfiguration
     * ........
     *
     */
    public void showAllComponentName() {
        int i = 1;
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            logger.info("beanDefinitionName {} = {}", i++, beanDefinitionName);
        }
    }
}
