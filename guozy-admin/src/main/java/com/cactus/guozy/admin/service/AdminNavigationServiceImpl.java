package com.cactus.guozy.admin.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cactus.guozy.admin.dao.AdminNavigationDao;
import com.cactus.guozy.admin.domain.AdminFunction;
import com.cactus.guozy.admin.domain.AdminMenu;
import com.cactus.guozy.admin.domain.AdminModule;
import com.cactus.guozy.admin.domain.AdminPermission;
import com.cactus.guozy.admin.domain.AdminRole;
import com.cactus.guozy.admin.domain.AdminUser;

/**
 *
 * @author Chaven Peng
 */
@Service("adminNavigationService")
public class AdminNavigationServiceImpl implements AdminNavigationService {

	private static final Logger LOG = LoggerFactory.getLogger(AdminNavigationServiceImpl.class);
    private static final String PATTERN = "_";

    @Autowired
    protected AdminNavigationDao adminNavigationDao;

    @Override
    public AdminMenu buildMenu(AdminUser adminUser) {
        AdminMenu adminMenu = new AdminMenu();
        List<AdminModule> modules = adminNavigationDao.readAllModulesAndFuncs();
        List<AdminFunction> funcsWithoutModule = adminNavigationDao.readFuncsWithoutModule();
        for(AdminFunction func : funcsWithoutModule) {
        	AdminModule module = new AdminModule();
        	module.setDisplayOrder(func.getDisplayOrder());
        	module.getFuncs().add(func);
        	modules.add(module);
        }
        
        populateAdminMenu(adminUser, adminMenu, modules);
        return adminMenu;
    }

    protected void populateAdminMenu(AdminUser adminUser, AdminMenu adminMenu, List<AdminModule> modules) {
    	for (AdminModule module : modules) {
    		List<AdminFunction> funcList = buildAuthorizedFuncsList(adminUser, module);
            if (funcList != null && funcList.size() > 0) {
            	AdminModule mtmp = module.copyAdminModule();
            	mtmp.setFuncs(funcList);
            	adminMenu.getAdminModules().add(mtmp);
            }
        }
        
        Collections.sort(adminMenu.getAdminModules(), MODULE_COMPARATOR);
    }
    
    protected List<AdminFunction> buildAuthorizedFuncsList(AdminUser adminUser, AdminModule module) {
        List<AdminFunction> authorizedFuncs = new ArrayList<AdminFunction>();
        for (AdminFunction func : module.getFuncs()) {
            if (isUserAuthorizedToViewFunc(adminUser, func)) {
                authorizedFuncs.add(func);
            }
        }

        Collections.sort(authorizedFuncs, FUNCTION_COMPARATOR);
        return authorizedFuncs;
    }
    
    @Override
    public AdminFunction findFuncByURI(String uri) {
        return adminNavigationDao.readFuncByURI(uri);
    }
    
    @Override
    public AdminFunction findFuncByKey(String functionKey) {
        return adminNavigationDao.readFuncByKey(functionKey);
    }
    
    @Override
    public boolean isUserAuthorizedToViewFunc(AdminUser adminUser, AdminFunction function) {
        boolean response = false;
        List<AdminPermission> authorizedPermissions = function.getPermissions();
        if(CollectionUtils.isEmpty(authorizedPermissions)) return true;
        
        checkAuth: {
            if (!CollectionUtils.isEmpty(adminUser.getAllRoles())) {
                for (AdminRole role : adminUser.getAllRoles()) {
                    for (AdminPermission permission : role.getAllPermissions()){
                        if (checkPermissions(authorizedPermissions, permission)) {
                            response = true;
                            break checkAuth;
                        }
                    }
                }
            }
            if (!CollectionUtils.isEmpty(adminUser.getAllPermissions())) {
                for (AdminPermission permission : adminUser.getAllPermissions()){
                    if (checkPermissions(authorizedPermissions, permission)) {
                        response = true;
                        break checkAuth;
                    }
                }
            }
            for (String defaultPermission : AdminSecurityService.DEFAULT_PERMISSIONS) {
                for (AdminPermission authorizedPermission : authorizedPermissions) {
                    if (authorizedPermission.getName().equals(defaultPermission)) {
                        response = true;
                        break checkAuth;
                    }
                }
            }
        }

        return response;
    }
    
    @Override
    public List<AdminFunction> findAllFuncs() {
        List<AdminFunction> functions = adminNavigationDao.readAllFuncs();
        return functions;
    }

    protected boolean checkPermissions(List<AdminPermission> authorizedPermissions, AdminPermission permission) {
        if (authorizedPermissions != null) {
            if (authorizedPermissions.contains(permission)){
                return true;
            }

            for (AdminPermission authorizedPermission : authorizedPermissions) {
                if (permission.getName().equals(parseForAllPermission(authorizedPermission.getName()))) {
                    return true;
                }
            }
        }
        return false;
    }

    protected String parseForAllPermission(String currentPermission) {
        String[] pieces = currentPermission.split(PATTERN);
        StringBuilder builder = new StringBuilder(50);
        builder.append(pieces[0]);
        builder.append("_ALL_");
        for (int j = 2; j<pieces.length; j++) {
            builder.append(pieces[j]);
            if (j < pieces.length - 1) {
                builder.append('_');
            }
        }
        return builder.toString();
    }

    private static Comparator<AdminFunction> FUNCTION_COMPARATOR = new Comparator<AdminFunction>() {
		@Override
		public int compare(AdminFunction o1, AdminFunction o2) {
			if (o1.getDisplayOrder() != null) {
                if (o2.getDisplayOrder() != null) {
                    return o1.getDisplayOrder().compareTo(o2.getDisplayOrder());
                }
                else {
                    return -1;
                }
            } else if (o2.getDisplayOrder() != null) {
                return 1;
            }

	        return o1.getId().compareTo(o2.getId());
		}
    };
    
    private static Comparator<AdminModule> MODULE_COMPARATOR = new Comparator<AdminModule>() {
		@Override
		public int compare(AdminModule o1, AdminModule o2) {
			if (o1.getDisplayOrder() != null) {
                if (o2.getDisplayOrder() != null) {
                    return o1.getDisplayOrder().compareTo(o2.getDisplayOrder());
                }
                else {
                    return -1;
                }
            } else if (o2.getDisplayOrder() != null) {
                return 1;
            }

	        return o1.getId().compareTo(o2.getId());
		}
    };
    
}
