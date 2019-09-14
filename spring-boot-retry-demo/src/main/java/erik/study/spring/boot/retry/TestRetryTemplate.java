package erik.study.spring.boot.retry;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Date 2019-08-05
 * @Created by erik
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:context-retry-template.xml"})
public class TestRetryTemplate {

    @Autowired
    private PayService payService;

    @Autowired
    private RetryTemplate retryTemplate;

    @org.junit.Test
    public void payTest() throws Exception {
        retryTemplate.execute((context) -> payService.minGoodsnum(-1),
                (context) -> {
                    System.out.println("recover over!");
                    return 0;
                }
        );
    }

}
