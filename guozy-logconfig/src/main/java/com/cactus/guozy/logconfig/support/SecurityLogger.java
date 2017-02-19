package com.cactus.guozy.logconfig.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用来创建安全日志对象的工厂。
 */
public class SecurityLogger extends ConfigurableLogger {
    public static final Logger DEFAULT_LOGGER = LoggerFactory.getLogger("SECURITY");

    @Override
    protected Logger getDefaultLogger() {
        return DEFAULT_LOGGER;
    }
}
