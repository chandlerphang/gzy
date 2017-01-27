package com.cactus.guozy.common.config.supports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * 运行时环境配置管理器，通过该类可以读取系统配置信息
 * 
 * @author Chaven Peng
 */
@Component("runtimeEnvConfigManager")
public class RuntimeEnvConfigManager implements BeanFactoryAware {

    private static final Logger LOG = LoggerFactory.getLogger(RuntimeEnvConfigManager.class);

    protected ConfigurableBeanFactory beanFactory;
    protected String prefix;

    public String getPrefix() {
        return prefix;
    }

    public String setPrefix(String prefix) {
        return this.prefix = prefix;
    }

    public String getProperty(String key, String suffix) {
        if(key==null) {
            return null;
        }
        String name = prefix + "." + key + "." + suffix;
        if (prefix == null) {
            name = key + "." + suffix;
        }
        String rv = beanFactory.resolveEmbeddedValue("${" + name + "}");
        if (rv == null || rv.equals("${" + name + "}")) {
            LOG.warn("未找到属性 ${" + name + "}, 尝试去掉后缀 "+ suffix + " 后重新查找");
            rv = getProperty(key);
        }
        return rv;

    }

    public String getProperty(String key) {
        if(key==null) {
            return null;
        }
        String name = prefix + "." + key;
        if (prefix == null) {
            name = key;
        }
        String rv = beanFactory.resolveEmbeddedValue("${" + name + "}");
        if(rv.equals("${" + name + "}")) {
            return null;
        }
        return rv;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableBeanFactory) beanFactory;
    }

}
