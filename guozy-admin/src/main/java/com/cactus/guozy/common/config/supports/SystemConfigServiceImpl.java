package com.cactus.guozy.common.config.supports;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Service;

import com.cactus.guozy.common.config.SystemConfigService;
import com.cactus.guozy.common.config.SystemConfig;
import com.cactus.guozy.common.config.SystemPropertyFieldType;
import com.cactus.guozy.common.config.dao.SystemConfigDao;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

@Service("systemConfigService")
public class SystemConfigServiceImpl implements SystemConfigService {
	
	private static final String DEFAULT_CACHE_NAME = "systemConfig";
	
	@Resource(name="cacheManager")
	protected EhCacheCacheManager springCacheManager;

    protected Cache systemPropertyCache;

    @Autowired
    protected SystemConfigDao systemPropertiesRepo;

    @Value("${system.config.service.cache.timeout:-1}")
    protected int systemPropertyCacheTimeout;

    @Autowired
    protected RuntimeEnvConfigManager propMgr;

    @Override
    public String resolveProperty(String name) {
        String result = getPropertyFromCache(name);
        if (result != null) {
            return result;
        }

        SystemConfig property = systemPropertiesRepo.readPropertyByName(name);
        if (property == null || StringUtils.isEmpty(property.getValue())) {
            result = propMgr.getProperty(name);
        } else {
            if ("_blank_".equals(property.getValue())) {
                result = "";
            } else {
                result = property.getValue();
            }
        }

        if (result != null) {
            addPropertyToCache(name, result);
        }
        return result;
    }
    
    @Override
    public String resolveProperty(String name, String defaultValue) {
        String result = resolveProperty(name);
        if (StringUtils.isBlank(result)) {
            return defaultValue;
        }
        return result;
    }
    
    protected void addPropertyToCache(String propertyName, String propertyValue) {
        String key = buildKey(propertyName);
        if (systemPropertyCacheTimeout < 0) {
            getSystemPropertyCache().put(new Element(key, propertyValue));
        } else {
            getSystemPropertyCache().put(new Element(key, propertyValue, systemPropertyCacheTimeout, systemPropertyCacheTimeout));
        }
    }

    protected String getPropertyFromCache(String propertyName) {
        String key = buildKey(propertyName);
        Element cacheElement = getSystemPropertyCache().get(key);
        if (cacheElement != null && cacheElement.getObjectValue() != null) {
            return (String) cacheElement.getObjectValue();
        }
        return null;
    }
    
    protected String buildKey(String propertyName) {
    	// TODO 添加上特定产品前缀
        String key = propertyName;
        return key;
    }
    
    protected Cache getSystemPropertyCache() {
        if (systemPropertyCache == null) {
            systemPropertyCache = springCacheManager.getCacheManager().getCache(DEFAULT_CACHE_NAME);
        }
        return systemPropertyCache;
    }

    @Override
    public SystemConfig findById(Long id) {
        return systemPropertiesRepo.readById(id);
    }
    
    @Override
    public int resolveIntProperty(String name) {
        String systemProperty = resolveProperty(name, "0");
        return Integer.valueOf(systemProperty).intValue();
    }
    
    @Override
    public int resolveIntProperty(String name, int defaultValue) {
        String systemProperty = resolveProperty(name, Integer.toString(defaultValue));
        return Integer.valueOf(systemProperty).intValue();
    }

    @Override
    public boolean resolveBoolProperty(String name) {
        String systemProperty = resolveProperty(name, "false");
        return Boolean.valueOf(systemProperty).booleanValue();
    }
    
    @Override
    public boolean resolveBoolProperty(String name, boolean defaultValue) {
        String systemProperty = resolveProperty(name, Boolean.toString(defaultValue));
        return Boolean.valueOf(systemProperty).booleanValue();
    }

    @Override
    public long resolveLongProperty(String name) {
        String systemProperty = resolveProperty(name, "0");
        return Long.valueOf(systemProperty).longValue();
    }
    
    @Override
    public long resolveLongProperty(String name, long defaultValue) {
        String systemProperty = resolveProperty(name, Long.toString(defaultValue));
        return Long.valueOf(systemProperty).longValue();
    }
    
    @Override
    public boolean isValueValidForType(String value, SystemPropertyFieldType type) {
        if (value == null) {
            return true;
        }

        if (type.equals(SystemPropertyFieldType.BOOLEAN)) {
            value = value.toUpperCase();
            if (value != null && (value.equals("TRUE") || value.equals("FALSE"))) {
                return true;
            }
        } else if (type.equals(SystemPropertyFieldType.INT)) {
            try {
                Integer.parseInt(value);
                return true;
            } catch (Exception e) {
                // Do nothing
            }
        } else if (type.equals(SystemPropertyFieldType.LONG)) {
            try {
                Long.parseLong(value);
                return true;
            } catch (Exception e) {
                // Do nothing
            }
        } else if (type.equals(SystemPropertyFieldType.DOUBLE)) {
            try {
                Double.parseDouble(value);
                return true;
            } catch (Exception e) {
                // Do nothing
            }
        } else if (type.equals(SystemPropertyFieldType.STRING)) {
            return true;
        }

        return false;
    }

	@Override
	public void removeFromCache(SystemConfig systemProperty) {
		String key = buildKey(systemProperty.getName());
        getSystemPropertyCache().remove(key);
	}

}
