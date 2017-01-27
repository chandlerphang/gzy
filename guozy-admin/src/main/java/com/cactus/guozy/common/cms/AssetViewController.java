package com.cactus.guozy.common.cms;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.cactus.guozy.common.utils.ThreadLocalManager;

public class AssetViewController extends AbstractController {

    private static final Logger LOG = LoggerFactory.getLogger(AssetViewController.class);
    
    protected String assetServerUrlPrefix;
    protected String viewResolverName;

    @Resource(name="assetStorageService")
    protected AssetStorageService staticAssetStorageService;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fullUrl = removeAssetPrefix(request.getRequestURI());

        try {
            Map<String, String> model = staticAssetStorageService.getCacheFileModel(fullUrl);
            return new ModelAndView(viewResolverName, model);
        } catch (AssetNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        } catch (Exception e) {
            LOG.error("Unable to retrieve static asset", e);
            throw new RuntimeException(e);
        } finally {
            ThreadLocalManager.remove();
        }
    }
    
    protected String removeAssetPrefix(String requestURI) {
        String fileName = requestURI;
        if (assetServerUrlPrefix != null) {
            int pos = fileName.indexOf(assetServerUrlPrefix);
            fileName = fileName.substring(pos+assetServerUrlPrefix.length());

            if (!fileName.startsWith("/")) {
                fileName = "/"+fileName;
            }
        }

        return fileName;
    }
    
    public String getAssetServerUrlPrefix() {
        return assetServerUrlPrefix;
    }

    public void setAssetServerUrlPrefix(String assetServerUrlPrefix) {        
        this.assetServerUrlPrefix = assetServerUrlPrefix;
    }

    public String getViewResolverName() {
        return viewResolverName;
    }

    public void setViewResolverName(String viewResolverName) {
        this.viewResolverName = viewResolverName;
    }
}
