package com.cactus.guozy.admin.dao;

import java.util.List;

import com.cactus.guozy.admin.domain.AdminFunction;
import com.cactus.guozy.admin.domain.AdminModule;

/**
 * @author Chaven Peng
 */
public interface AdminNavigationDao {
	
	/**
	 * <p>
	 * 读取所有系统模块，模块就是没有父级的AdminFunction
	 * </p>
	 */
    List<AdminModule> readAllModules();
    
    List<AdminModule> readAllModulesAndFuncs();
    
    List<AdminFunction> readFuncsWithoutModule();
    
    List<AdminFunction> readAllFuncs();
    
    AdminFunction readFuncByURI(String uri);

    AdminFunction readFuncByKey(String funcKey);

}
