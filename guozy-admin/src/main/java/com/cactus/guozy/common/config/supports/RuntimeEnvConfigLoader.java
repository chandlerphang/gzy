package com.cactus.guozy.common.config.supports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.StringValueResolver;

/**
 * 系统运行时环境配置加载器。
 * 
 * <p>
 * 该类用来加载应用系统自身所有的配置文件，然后将各配置属性注入到spring容器中。
 * 其它类可以通过${}或RuntimeEnvironmentConfigurationManager来引用这些配置项
 * </p>
 * 
 * @author Chaven Peng
 */
public class RuntimeEnvConfigLoader extends PropertyPlaceholderConfigurer implements InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(RuntimeEnvConfigLoader.class);
    private static final String CONFIG_FILENAME = "sys.properties";
    
    protected static Set<Resource> defaultConfigDirs = new LinkedHashSet<Resource>();
    
    static {
        defaultConfigDirs.add(new ClassPathResource("config/guozy/"));
        defaultConfigDirs.add(new ClassPathResource("config/runtime/"));
    }

    protected Set<Resource> configLocations;
	protected StringValueResolver stringValueResolver;

    public RuntimeEnvConfigLoader() {
        super();
        setFileEncoding("utf-8");
        setIgnoreUnresolvablePlaceholders(true);
        setNullValue("@null");
    }

    @Override
    public void afterPropertiesSet() throws IOException {
        ArrayList<Resource> allResources = new ArrayList<Resource>();
        
        Set<Set<Resource>> testLocations = new LinkedHashSet<Set<Resource>>();
        if(!CollectionUtils.isEmpty(configLocations)) {
        	testLocations.add(configLocations);
        }
        testLocations.add(defaultConfigDirs);
        
        for (Set<Resource> locations : testLocations) {
            for (Resource resource : createSysResource(locations)) {
                if (resource.exists()) {
                    allResources.add(resource);
                }
            }
        }

        Properties props = new Properties();
        for (Resource resource : allResources) {
            if (resource.exists()) {
                if (LOG.isDebugEnabled()) {
                    props = new Properties(props);
                    props.load(resource.getInputStream());
                    for (Entry<Object, Object> entry : props.entrySet()) {
                    	LOG.debug("加载配置项 "+ entry.getKey() + "/" + entry.getValue() + " ,从文件 " + resource.getFilename());
                    }
                }
            } else {
                LOG.debug("找不到资源文件: " + resource.getFilename());
            }
        }

        setLocations(allResources.toArray(new Resource[] {}));
    }
    
    protected Resource[] createSysResource(Set<Resource> locations) throws IOException {
        Resource[] resources = new Resource[locations.size()];
        int index = 0;
        for (Resource resource : locations) {
            resources[index] = resource.createRelative(CONFIG_FILENAME);
            index++;
        }
        return resources;
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        stringValueResolver = new PlaceholderResolvingStringValueResolver(props);
    }

    private class PlaceholderResolvingStringValueResolver implements StringValueResolver {

        private final PropertyPlaceholderHelper helper;

        private final PropertyPlaceholderHelper.PlaceholderResolver resolver;

        public PlaceholderResolvingStringValueResolver(Properties props) {
            this.helper = new PropertyPlaceholderHelper("${", "}", ":", true);
            this.resolver = new PropertyPlaceholderConfigurerResolver(props);
        }

        public String resolveStringValue(String strVal) throws BeansException {
            String value = this.helper.replacePlaceholders(strVal, this.resolver);
            return (value.equals("") ? null : value);
        }
    }

    private class PropertyPlaceholderConfigurerResolver implements PropertyPlaceholderHelper.PlaceholderResolver {

        private final Properties props;

        private PropertyPlaceholderConfigurerResolver(Properties props) {
            this.props = props;
        }

        public String resolvePlaceholder(String placeholderName) {
            return RuntimeEnvConfigLoader.this.resolvePlaceholder(placeholderName, props, 1);
        }
    }

    public StringValueResolver getStringValueResolver() {
        return stringValueResolver;
    }
    
    public Set<Resource> getConfigLocations() {
		return configLocations;
	}

	public void setConfigLocations(Set<Resource> configLocations) {
		this.configLocations = configLocations;
	}
}
