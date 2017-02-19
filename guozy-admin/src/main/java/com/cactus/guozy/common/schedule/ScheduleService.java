package com.cactus.guozy.common.schedule;import java.text.ParseException;import java.util.Date;import java.util.List;import javax.annotation.Nullable;import javax.annotation.Resource;import org.apache.commons.collections.CollectionUtils;import org.apache.commons.lang3.StringUtils;import org.quartz.CronScheduleBuilder;import org.quartz.CronTrigger;import org.quartz.JobBuilder;import org.quartz.JobDetail;import org.quartz.Scheduler;import org.quartz.SchedulerException;import org.quartz.SchedulerFactory;import org.quartz.TriggerBuilder;import org.quartz.TriggerKey;import org.quartz.TriggerUtils;import org.quartz.impl.StdSchedulerFactory;import org.quartz.impl.triggers.CronTriggerImpl;import org.springframework.stereotype.Service;import com.cactus.guozy.common.json.Jsons;import com.cactus.guozy.common.schedule.dao.ScheduleExecuteResultDao;import com.cactus.guozy.common.schedule.dao.ScheduleJobSettingDao;import com.cactus.guozy.core.util.DateTimeUtil;import com.google.common.base.Function;import com.google.common.collect.Lists;import lombok.extern.slf4j.Slf4j;@Slf4j@Servicepublic class ScheduleService {    @Resource    private ScheduleJobSettingDao scheduleJobSettingDao;    @Resource    private ScheduleExecuteResultDao scheduleExecuteResultDao;    private static Scheduler scheduler = null;    static {        if (scheduler == null) {            init();        }    }    public synchronized static void init() {        if (scheduler != null) {            return;        }        SchedulerFactory factory = new StdSchedulerFactory();        try {            scheduler = factory.getScheduler();            scheduler.start();        } catch (Throwable t) {            log.error("create schedule exception", t);        }    }    /**     * 把所有可能的job放入调度中, 项目启动时使用     */    public void scheduleAll() {        if (scheduler == null) {            init();        }        List<ScheduledJobSetting> list = scheduleJobSettingDao.getAll();        for (ScheduledJobSetting setting : list) {            try {                schedule(setting);            } catch (Throwable t) {                log.error("schedule current job exception, {}", Jsons.DEFAULT.toJson(setting));            }        }    }    /**     * 调度指定的job(调整job)     */    private void schedule(ScheduledJobSetting setting) throws SchedulerException {        // triggerId 使用 scheduledId 代替        String triggerId = setting.getScheduleId();        String groupId = setting.getGroupId();        String jobId = setting.getScheduleId();        if (!scheduler.isStarted()) {            throw new SchedulerException("scheduler is not started");        }        TriggerKey triggerKey = TriggerKey.triggerKey(triggerId, groupId);        if (scheduler.checkExists(triggerKey)) {            // 如果已经在调度            if (StringUtils.isBlank(setting.getCron()) || setting.getStatus() != ScheduleJobStatus.STARTED.getCode()) {                // 需要移除调度                removeSchedule(setting);            } else {                // 需要重新调度                CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(CronScheduleBuilder.cronSchedule(setting.getCron()))                        .build();                scheduler.rescheduleJob(TriggerKey.triggerKey(triggerId, groupId), trigger);            }        } else if (StringUtils.isNotBlank(setting.getCron()) && setting.getStatus() == ScheduleJobStatus.STARTED.getCode()) {            // 如果不再调度, 看看是否需要调度            @SuppressWarnings("rawtypes")			Class clazz;            try {                clazz = Class.forName(setting.getClassPath());            } catch (ClassNotFoundException e) {                log.error("not found class, cannot schedule, className:{}", setting.getClassPath());                throw new SchedulerException("not found class, cannot schedule");            }            @SuppressWarnings("unchecked")			JobDetail job = JobBuilder.newJob(clazz).withIdentity(jobId, groupId).build();            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(CronScheduleBuilder.cronSchedule(setting.getCron()))                    .build();            scheduler.scheduleJob(job, trigger);        }    }    /**     * 停止调度某个job     */    private void removeSchedule(ScheduledJobSetting setting) throws SchedulerException {        // triggerId 使用 scheduledId 代替        String triggerId = setting.getScheduleId();        String groupId = setting.getGroupId();        if (!scheduler.isStarted()) {            throw new SchedulerException("scheduler is not started");        }        scheduler.unscheduleJob(TriggerKey.triggerKey(triggerId, groupId));    }    /**     * 更新某个job     */    public void updateJobSetting(ScheduledJobSetting setting) throws SchedulerException {        scheduleJobSettingDao.update(setting);        schedule(setting);    }    public ScheduledJobSetting findJobSetting(int id) {        return scheduleJobSettingDao.findById(id);    }    public List<ScheduleJobDto> getAll() {        List<ScheduledJobSetting> settingList = scheduleJobSettingDao.getAll();        if (CollectionUtils.isEmpty(settingList)) {            return Lists.newArrayList();        }        List<ScheduleJobDto> scheduleJobDtoList = Lists.transform(settingList, new Function<ScheduledJobSetting, ScheduleJobDto>() {            @Nullable            @Override            public ScheduleJobDto apply(@Nullable ScheduledJobSetting setting) {                String resultScheduleId = setting.getGroupId() + "_" + setting.getScheduleId();                ScheduleExecuteResult lastResult = scheduleExecuteResultDao.findLastExecute(resultScheduleId);                ScheduleJobDto dto = ScheduleJobDto.builder().id(setting.getId()).groupId(setting.getGroupId()).scheduleId(setting.getScheduleId())                        .status(setting.getStatus()).cron(setting.getCron()).build();                if (lastResult != null) {                    dto.setLastExecuteTime(DateTimeUtil.dateTimeFrom(lastResult.getStartTime()));                    if (lastResult.getEndTime() != null) {                        dto.setCostMillSeconds(lastResult.getEndTime().getTime() - lastResult.getStartTime().getTime());                    } else {                        dto.setCostMillSeconds(0);                    }                }                if (setting.getStatus() == ScheduleJobStatus.STARTED.getCode() && StringUtils.isNotBlank(setting.getCron())) {                    CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();                    try {                        cronTriggerImpl.setCronExpression(setting.getCron());                    } catch (ParseException e) {                    }                    List<Date> dates = TriggerUtils.computeFireTimes(cronTriggerImpl, null, 1);                    if (CollectionUtils.isNotEmpty(dates)) {                        dto.setNextExecuteTime(DateTimeUtil.dateTimeFrom(dates.get(0)));                    }                }                return dto;            }        });        return scheduleJobDtoList;    }    public List<ScheduledJobSetting> getListByGroupId(String groupId) {        return scheduleJobSettingDao.getListByGroupId(groupId);    }}