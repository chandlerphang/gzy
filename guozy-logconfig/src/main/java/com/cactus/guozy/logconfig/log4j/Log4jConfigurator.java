package com.cactus.guozy.logconfig.log4j;

import java.net.URL;
import java.util.Map;
import java.util.Properties;

import com.cactus.guozy.logconfig.LogConfigurator;

public class Log4jConfigurator extends LogConfigurator {
    @Override
    protected void doConfigure(URL configFile, Map<String, String> propsMap) {
        Properties props = new Properties();
        props.putAll(propsMap);

        DOMConfigurator.configure(configFile, props);
    }

    @Override
    public void shutdown() {
    }
}
