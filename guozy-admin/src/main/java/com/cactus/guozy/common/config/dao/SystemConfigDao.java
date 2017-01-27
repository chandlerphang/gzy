package com.cactus.guozy.common.config.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cactus.guozy.common.config.SystemConfig;

public interface SystemConfigDao {
	
	SystemConfig readById(Long id);
	
	SystemConfig readPropertyByName(String name);
	
	List<SystemConfig> readAllProperties();
	
    int updateProperty(SystemConfig systemProperty);
    
    int update(@Param("name")String name, @Param("value")String value);
    
    int insertPropery(SystemConfig systemProperty);
    
    int insert(@Param("name")String name, @Param("value")String value);

    int deleteProperty(SystemConfig systemProperty);
    
}
