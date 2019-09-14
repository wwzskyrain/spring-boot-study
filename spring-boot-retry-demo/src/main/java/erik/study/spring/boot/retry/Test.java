package erik.study.spring.boot.retry;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Date 2019-08-05
 * @Created by erik
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:context.xml"})
public class Test {

    @Autowired
    private PayService payService;

    @org.junit.Test
    public void payTest() throws Exception {
        int store = payService.minGoodsnum(-1);
        System.out.println("库存为：" + store);
    }

}
