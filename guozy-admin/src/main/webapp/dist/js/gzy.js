var GZY = (function($) {
    var redirectUrlDiv = "gzy-redirect-url",
        extraDataDiv   = "gzy-extra-data",
        internalDataDiv   = "gzy-internal-data",
        preAjaxCallbackHandlers = [],
        internalDataHandlers = [],
        servletContext = "//GZY-SERVLET-CONTEXT",
        siteBaseUrl = "//GZY-SITE-BASEURL";
    
    function addPreAjaxCallbackHandler(fn) {
        preAjaxCallbackHandlers.push(fn);
    }

    function runPreAjaxCallbackHandlers($data) {
        return runGenericHandlers($data, preAjaxCallbackHandlers);
    }

    function addInternalDataHandler(fn) {
        internalDataHandlers.push(fn);
    }
    
    function runInternalDataHandlers($data) {
        return runGenericHandlers($data, internalDataHandlers);
    }
    
    function runGenericHandlers($data, handlers) {
        for (var i = 0; i < handlers.length; i++) {
            if (!handlers[i]($data)) {
                return false;
            }
        }
        return true;
    }
    
    //============== 从html中抽取数据 ===========================
    function getInternalData($data) {
        var extractedData = extractData($data, internalDataDiv);

        if (($data instanceof jQuery) && ($data.attr('id') == internalDataDiv + "-container")) {
            $data.unwrap();
        }

        return extractedData;
    }

    function getExtraData($data) {
        return extractData($data, extraDataDiv);
    }
    
    function extractData($data, dataDivId) {
        if (!($data instanceof jQuery)) {
            return null;
        }
        
    	var extractedData = null;

	    var $dataDiv = $data.find('#' + dataDivId);
    	if ($dataDiv.length > 0) {
    	    try {
    	        extractedData = $.parseJSON($dataDiv.text());
    	    } catch (e) {
    	        console.log("Could not parse data as JSON: " + $dataDiv.text());
    	    }
	        $dataDiv.remove();
    	}

	    return extractedData;
    }
    //============== 从html中抽取数据 END =========================
    
    function get(options, callback) {
        if (options == null) {
            options = {};
        }
        options.type = 'GET';
        return GZY.ajax(options, callback);
    }

    function post(options, callback) {
        if (options == null) {
            options = {};
        }
        options.type = 'POST';
        return GZY.ajax(options, callback);
    }
    
    function ajax(options, callback) {
        if (options.type == null) {
            options.type = 'GET';
        }
        
        if (options.type.toUpperCase() == 'POST') {
        	// 拼接CSRF Token
            if (typeof options.data == 'string') {
                if (options.data.indexOf('csrfToken') < 0) {
                    var csrfToken = getCsrfToken();
                    if (csrfToken != null) {
                        if (options.data.indexOf('=') > 0) {
                            options.data += "&";
                        }
                        
                        options.data += "csrfToken=" + csrfToken;
                    }
                }
            } else if (typeof options.data == 'object') {
                if (options.data['csrfToken'] == null || options.data['csrfToken'] == '') {
                    var csrfToken = getCsrfToken();
                    if (csrfToken != null) {
                        options.data['csrfToken'] = csrfToken;
                    }
                }
            } else if (!options.data) {
                var csrfToken = getCsrfToken();
                if (csrfToken) {
                    options.data = { 'csrfToken': csrfToken }
                }
            }
        }
        
        options.success = function(data) {
            if (typeof data == "string" && !this.noParse) {
                data = $($.trim(data));
            }
            
            var internalData = getInternalData(data);
            if (internalData != null) {
                runInternalDataHandlers(internalData);
            }
            
            if (runPreAjaxCallbackHandlers(data)) {
                var extraData = getExtraData(data);
                callback(data, extraData);
            }
        };
        
        if (!options.error) {
            options.error = function(data) {
                GZY.defaultErrorHandler(data);
            };
        }
        
        return $.ajax(options);
    }
    
    function getCsrfToken() {
        var csrfTokenInput = $('input[name="csrfToken"]');
        if (csrfTokenInput.length == 0) {
            return null;
        }
        
        return csrfTokenInput.val();
    }
    
    function defaultErrorHandler(data) {
        if (data.getAllResponseHeaders()) {
            alert("请求处理错误.");
        }
    }
    
    // 处理参数多值问题
    function serializeObject($object) {
        var o = {};
        var a = $object.serializeArray();
		$.each(a, function() {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    }

    function addUrlParam(search, key, val){
        var newParam = key + '=' + val,
            params = '?' + newParam;

        if (search) {
            params = search.replace(new RegExp('[\?]' + key + '[^&]*'), '?' + newParam);
            if (params === search) {
                params = search.replace(new RegExp('[\&]' + key + '[^&]*'), '&' + newParam);
                if ((params === search) && (search.indexOf(val) == -1) ) {
                    params += '&' + newParam;
                }
            }
        }
        return document.location.search = params;
    };
    
    
    function redirectIfNecessary($data) {
        if (!($data instanceof jQuery)) {
            return true;
        }
        
        if ($data.attr('id') == redirectUrlDiv) {
            var redirectUrl = $data.text();
            if (redirectUrl != null && redirectUrl !== "") {
                window.location = redirectUrl;
                return false;
            }
        }
        return true;
    }
    addPreAjaxCallbackHandler(function($data) {
        return GZY.redirectIfNecessary($data);
    });
    
    return {
        addPreAjaxCallbackHandler : addPreAjaxCallbackHandler,
        addInternalDataHandler : addInternalDataHandler,
        redirectIfNecessary : redirectIfNecessary,
        getExtraData : getExtraData,
        get : get,
        post : post,
        ajax : ajax,
        defaultErrorHandler : defaultErrorHandler,
        serializeObject : serializeObject,
        addUrlParam : addUrlParam,
        servletContext : servletContext,
        siteBaseUrl : siteBaseUrl
    }
})($);
