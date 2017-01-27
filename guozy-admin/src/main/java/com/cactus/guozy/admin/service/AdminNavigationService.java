package com.cactus.guozy.admin.service;

import java.util.List;

import com.cactus.guozy.admin.domain.AdminFunction;
import com.cactus.guozy.admin.domain.AdminMenu;
import com.cactus.guozy.admin.domain.AdminUser;


/**
 * 导航菜单服务
 * 
 * @author Chaven Peng
 */
public interface AdminNavigationService {

    AdminMenu buildMenu(AdminUser adminUser);

    boolean isUserAuthorizedToViewFunc(AdminUser adminUser, AdminFunction function);

    AdminFunction findFuncByURI(String uri);

    AdminFunction findFuncByKey(String functionKey);

    List<AdminFunction> findAllFuncs();
    
}
