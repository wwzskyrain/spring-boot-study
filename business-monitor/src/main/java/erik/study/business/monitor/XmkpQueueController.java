package erik.study.business.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "business-monitor/xmkp/queue")
public class XmkpQueueController {

    private static final Logger logger = LoggerFactory.getLogger(XmkpQueueController.class);

    @Autowired
    private XmkpQueueManager xmkpQueueManager;

    @RequestMapping(value = "/names")
    public Response getCustomQueueNames() {
        return new Response(xmkpQueueManager.getAllQueueName());
    }

    @RequestMapping(value = "/name/{queueName}", method = RequestMethod.POST)
    public Response addCustomQueueName(@PathVariable("queueName") String queueName) {
        xmkpQueueManager.addQueueName(queueName);
        return new Response(queueName);
    }

    @RequestMapping(value = "/name/{queueName}", method = RequestMethod.DELETE)
    public Response deleteCustomQueueName(@PathVariable String queueName) {
        xmkpQueueManager.deleteQueueName(queueName);
        return new Response(queueName);
    }


}