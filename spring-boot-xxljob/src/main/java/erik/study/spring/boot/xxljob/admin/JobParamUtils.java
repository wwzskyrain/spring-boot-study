package erik.study.spring.boot.xxljob.admin;

import com.alibaba.fastjson.JSON;

/**
 * XXL-JOB 任务参数（executorParam）解析工具。
 *
 * <p>推荐用法：任务参数直接约定为 JSON 字符串，业务方自己定义 xxxDto 来做反序列化，
 * 例如任务参数 {@code {"cancelMinutes":30}} 反序列化为 {@code OrderCancelParam}。
 * 本工具也兼容旧的 {@code key=value&key2=value2} 形式（见 {@link #getInt}）。
 */
public final class JobParamUtils {

    private JobParamUtils() {
    }

    /**
     * 把任务参数（JSON 字符串）反序列化为业务 DTO。
     *
     * <p>DTO 字段建议直接给默认值（如 {@code private Integer cancelMinutes = 30;}），
     * JSON 中缺省的字段会保留默认值，方便参数缺省时兜底。
     *
     * @param jobParam xxl-job 传入的任务参数（JSON 字符串）
     * @param clazz    业务 DTO 类型
     * @param <T>      DTO 泛型
     * @return 反序列化后的 DTO；参数为空或解析失败时返回 null
     */
    public static <T> T parse(String jobParam, Class<T> clazz) {
        if (jobParam == null || jobParam.trim().isEmpty()) {
            return null;
        }
        try {
            return JSON.parseObject(jobParam, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从 key=value 形式的任务参数中解析指定 key 的 int 值（兼容旧格式）。
     *
     * @param jobParam      xxl-job 传入的任务参数
     * @param key           参数名
     * @param defaultValue  缺失或非法时的默认值
     */
    public static int getInt(String jobParam, String key, int defaultValue) {
        if (jobParam == null || jobParam.trim().isEmpty()) {
            return defaultValue;
        }
        for (String pair : jobParam.split("[&,]")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2 && kv[0].trim().equals(key)) {
                try {
                    return Integer.parseInt(kv[1].trim());
                } catch (NumberFormatException ignored) {
                    // 非法数值，走默认值
                }
            }
        }
        return defaultValue;
    }
}
