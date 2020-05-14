package erik.study.business.monitor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class Response {
    private int code;
    private String reason;
    private Object data;

    public Response(Object data) {
        code = 0;
        reason = "";
        this.data = data;
    }

    public void setException(Exception e) {
        e.printStackTrace();

        this.code = -1;
        this.reason = e.getClass().getSimpleName() + ": " + e.getMessage();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(stream));
        this.data = stream.toString().split("\n");
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
