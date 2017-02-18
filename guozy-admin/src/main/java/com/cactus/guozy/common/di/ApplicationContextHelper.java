package com.cactus.guozy.common.di;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.cactus.guozy.common.schedule.ScheduleService;

@Component("applicationContextHelper")
public class ApplicationContextHelper implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringHelper.setApplicationContext(applicationContext);
        ScheduleService scheduleService = SpringHelper.popBean(ScheduleService.class);
        scheduleService.scheduleAll();
    }
}