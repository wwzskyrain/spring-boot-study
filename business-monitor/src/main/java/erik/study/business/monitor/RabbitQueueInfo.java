package erik.study.business.monitor;

import com.alibaba.fastjson.annotation.JSONField;

public class RabbitQueueInfo {
    private String name;
    private String state;
    private String node;
    private String host;
    private String url;

    private int messages;

    @JSONField(name = "messages_ready")
    private int messagesReady;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMessages() {
        return messages;
    }

    public void setMessages(int messages) {
        this.messages = messages;
    }

    public int getMessagesReady() {
        return messagesReady;
    }

    public void setMessagesReady(int messagesReady) {
        this.messagesReady = messagesReady;
    }
}
