package erik.study.spring.boot.xxljob.admin;

/**
 * 任务（JobInfo）请求参数，对应 xxl-job-admin 的 JobInfo 对象。
 * 新增 / 更新任务时都会用到，字段含义见注释。
 */
public class JobInfoRequest {

    /** 任务ID（更新时必填，新增时不需要） */
    private Integer id;

    /** 所属执行器 ID（执行器管理里的 jobGroupId） */
    private Integer jobGroup;

    /** 任务名称（3.x 版字段，对应旧版 jobDesc） */
    private String name;

    /** 负责人 */
    private String author = "erik";

    /** 调度类型：NONE / CRON / FIX_RATE */
    private String scheduleType = "CRON";

    /** 调度配置：CRON 时为 cron 表达式，FIX_RATE 时为秒数 */
    private String scheduleConf;

    /** 运行模式：BEAN / GLUE_GROOVY / GLUE_SHELL 等 */
    private String glueType = "BEAN";

    /** GLUE 模式备注 */
    private String glueRemark = "启动自动注册的任务";

    /** 执行器 Handler 名称（对应执行器里 @XxlJob 注解的值） */
    private String executorHandler;

    /** 任务参数（字符串，可自定义格式，例如 cancelMinutes=30&region=cn） */
    private String executorParam;

    /** 路由策略：FIRST / LAST / ROUND / RANDOM / CONSISTENT_HASH / FAILOVER / BUSYOVER 等 */
    private String executorRouteStrategy = "FIRST";

    /** 调度过期策略：DO_NOTHING / FIRE_ONCE_NOW */
    private String misfireStrategy = "DO_NOTHING";

    /** 阻塞处理策略：SERIAL_EXECUTION / DISCARD_LATER / COVER_EARLY */
    private String executorBlockStrategy = "SERIAL_EXECUTION";

    /** 任务超时时间（秒），0 表示不限制 */
    private Integer executorTimeout = 0;

    /** 失败重试次数 */
    private Integer executorFailRetryCount = 0;

    /** 调度状态：0 停止，1 运行（注册后默认直接启动，方便观察） */
    private Integer triggerStatus = 1;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(Integer jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getScheduleConf() {
        return scheduleConf;
    }

    public void setScheduleConf(String scheduleConf) {
        this.scheduleConf = scheduleConf;
    }

    public String getGlueType() {
        return glueType;
    }

    public void setGlueType(String glueType) {
        this.glueType = glueType;
    }

    public String getGlueRemark() {
        return glueRemark;
    }

    public void setGlueRemark(String glueRemark) {
        this.glueRemark = glueRemark;
    }

    public String getExecutorHandler() {
        return executorHandler;
    }

    public void setExecutorHandler(String executorHandler) {
        this.executorHandler = executorHandler;
    }

    public String getExecutorParam() {
        return executorParam;
    }

    public void setExecutorParam(String executorParam) {
        this.executorParam = executorParam;
    }

    public String getExecutorRouteStrategy() {
        return executorRouteStrategy;
    }

    public void setExecutorRouteStrategy(String executorRouteStrategy) {
        this.executorRouteStrategy = executorRouteStrategy;
    }

    public String getMisfireStrategy() {
        return misfireStrategy;
    }

    public void setMisfireStrategy(String misfireStrategy) {
        this.misfireStrategy = misfireStrategy;
    }

    public String getExecutorBlockStrategy() {
        return executorBlockStrategy;
    }

    public void setExecutorBlockStrategy(String executorBlockStrategy) {
        this.executorBlockStrategy = executorBlockStrategy;
    }

    public Integer getExecutorTimeout() {
        return executorTimeout;
    }

    public void setExecutorTimeout(Integer executorTimeout) {
        this.executorTimeout = executorTimeout;
    }

    public Integer getExecutorFailRetryCount() {
        return executorFailRetryCount;
    }

    public void setExecutorFailRetryCount(Integer executorFailRetryCount) {
        this.executorFailRetryCount = executorFailRetryCount;
    }

    public Integer getTriggerStatus() {
        return triggerStatus;
    }

    public void setTriggerStatus(Integer triggerStatus) {
        this.triggerStatus = triggerStatus;
    }
}
