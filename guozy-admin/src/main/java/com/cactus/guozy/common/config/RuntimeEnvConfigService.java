package com.cactus.guozy.common.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * @author Chaven Peng
 */
@Service("runtimeEnvConfigService")
public class RuntimeEnvConfigService implements ApplicationContextAware {

    protected static ApplicationContext applicationContext;

    public static String resolveSystemProperty(String name) {
        return getSystemPropertiesService().resolveProperty(name);
    }
    
    public static String resolveSystemProperty(String name, String defaultValue) {
        return getSystemPropertiesService().resolveProperty(name, defaultValue);
    }

    public static int resolveIntSystemProperty(String name) {
        return getSystemPropertiesService().resolveIntProperty(name);
    }
    
    public static int resolveIntSystemProperty(String name, int defaultValue) {
        return getSystemPropertiesService().resolveIntProperty(name, defaultValue);
    }

    public static boolean resolveBooleanSystemProperty(String name) {
        return getSystemPropertiesService().resolveBoolProperty(name);
    }
    
    public static boolean resolveBooleanSystemProperty(String name, boolean defaultValue) {
        return getSystemPropertiesService().resolveBoolProperty(name, defaultValue);
    }

    public static long resolveLongSystemProperty(String name) {
        return getSystemPropertiesService().resolveLongProperty(name);
    }
    
    public static long resolveLongSystemProperty(String name, long defaultValue) {
        return getSystemPropertiesService().resolveLongProperty(name, defaultValue);
    }

    protected static SystemConfigService getSystemPropertiesService() {
        return (SystemConfigService) applicationContext.getBean("systemConfigService");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    	RuntimeEnvConfigService.applicationContext = applicationContext;
    }

}
