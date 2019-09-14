package erik.study.spring.boot.aop;

import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    private final static Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("@annotation(print)")
    public void annotationPointcut(Print print) {
    }

    @Around("annotationPointcut(print)")
    public Object around(ProceedingJoinPoint pjp, Print print) throws Throwable {
        //获取请求方法
        Signature sig = pjp.getSignature();
        String method = pjp.getTarget().getClass().getName() + "." + sig.getName();

        //获取请求的参数
        Object[] args = pjp.getArgs();
        //fastjson转换
        String params = JSONObject.toJSONString(args);

        //打印请求参数
        log.info("参数:" + method + ":" + params);

        //执行方法
        Object result = pjp.proceed();

        //打印返回结果
        log.info("返回结果:" + method + ":" + result);
        return result;
    }
}