package com.cactus.guozy.logconfig.logback;

import java.net.URL;
import java.util.Map;

import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

import com.cactus.guozy.logconfig.LogConfigurator;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.LogbackException;

public class LogbackConfigurator extends LogConfigurator {
    @Override
    protected void doConfigure(URL configFile, Map<String, String> props) throws Exception {
        JoranConfigurator configurator = new JoranConfigurator();

        configurator.setContext(getLoggerContext(props));
        configurator.doConfigure(configFile);
    }

    private LoggerContext getLoggerContext(Map<String, String> props) {
        ILoggerFactory lcObject = LoggerFactory.getILoggerFactory();

        if (!(lcObject instanceof LoggerContext)) {
            throw new LogbackException(
                    "Expected LOGBACK binding with SLF4J, but another log system has taken the place: "
                    + lcObject.getClass().getSimpleName());
        }

        LoggerContext lc = (LoggerContext) lcObject;

        lc.reset();

        for (Map.Entry<String, String> entry : props.entrySet()) {
            lc.putProperty(entry.getKey(), entry.getValue());
        }

        return lc;
    }

    @Override
    public void shutdown() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.stop();
    }
}
