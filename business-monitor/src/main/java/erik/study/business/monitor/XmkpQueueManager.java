package erik.study.business.monitor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author erik.wang
 * @date 2020-02-20 12:46
 * @description
 */
@Component
public class XmkpQueueManager {

    private Map<String, RabbitQueueInfo> rabbitQueueInfoMap = new ConcurrentHashMap<>();

    private Set<String> queueNames = new HashSet<>();


    public void addQueueName(String queueName) {
        queueNames.add(queueName);
    }

    public void deleteQueueName(String queueName){
        queueNames.remove(queueName);
    }

    public Set<String> getAllQueueName() {
        return Collections.unmodifiableSet(queueNames);
    }



}
