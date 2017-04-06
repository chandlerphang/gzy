(function (root, factory) {
	// 这里 root是浏览器全局对象 ==> window
	root.gzyadmin = factory(root.jQuery);
} (this, function init($, undefined) {
	
}));

;(function ($, window, undefined) {
    'use strict';
    
    var $doc = $(document);
    
    if (!String.prototype.endsWith) {
        String.prototype.endsWith = function(suffix) {
            return this.indexOf(suffix, this.length - suffix.length) !== -1;
        };
    }
      
    if (!Array.prototype.last) {
        Array.prototype.last = function() {
            return this[this.length - 1];
        };
    }
    
    if (!$.prototype.isOverflowed) {
        $.prototype.isOverflowed = function() {
            var element = $(this)
                .clone()
                .css({display: 'inline', width: 'auto', visibility: 'hidden'})
                .appendTo('body');

            var elementWidth = element.width();
            element.remove();
            
            return (elementWidth > $(this).width());
        };
    }
    
    if (!jQuery.fn.outerHTML) {
        jQuery.fn.outerHTML = function() {
            return jQuery('<div />').append(this.eq(0).clone()).html();
        };
    }
  
})(jQuery, this);

var GZYAdmin = (function($) {
    
	// 用来跟踪当前激活的modal
	var modals = [];
	// 表单验证处理器
	var preValidationFormSubmitHandlers = [];
	var validationFormSubmitHandlers = [];
	var postValidationFormSubmitHandlers = [];
	// 表单提交后置处理器
	var postFormSubmitHandlers = [];
	
	var dependentFieldFilterHandlers = {};
	var initializationHandlers = [];
	var updateHandlers = [];
	var stackedModalOptions = {
	    left: 20,
	    top: 20
	}
	var originalStickyBarOffset = 12;//$('.sticky-container').offset().top;
	
    var fieldSelectors = 'input, .custom-checkbox, .foreign-key-value-container span.display-value, .redactor_box, ' + 
                         '.asset-selector-container img, select, div.custom-checkbox, div.small-enum-container, ' + 
                         'textarea';
	
    // 弹出框骨架
    function getModalSkeleton() {
		var templates = {
				dialog: 
					"<div class='gzybox modal' tabindex='-1' role='dialog'>" + 
						"<div class='modal-dialog' style='width:80% !important;'>" +	
							"<div class='modal-content'>" +
								"<div class='modal-header bg-primary'>" +
									"<button type='button' class='gzybox-close-button close' data-dismiss='modal' aria-hidden='true'>&times;</button>" +
									"<h4 class='modal-title'></h4>" +
								"</div>" +
								"<div class='modal-body'><div class='gzybox-body'></div></div>" +
							"</div>" +
						"</div>" +
					"</div>",
				footer:
					"<div class='modal-footer'></div>",
				form:
					"<form class='gzybox-form'></form>",
		};
    	
		var $box = $(templates.dialog);
		var $modal = $(".modal-content", $box);
        var $modalFooter = $(templates.footer);
        $modal.append($modalFooter);
        
        return $box;
    }
    
	function showModal($data, onModalHide, onModalHideArgs) {
		$data.modal({
			backdrop: (modals.length < 1),
			keyboard: false,
			show: false
		});
		
		if (modals.length > 0) {
			modals.last().css('z-index', '1040');
			var $backdrop = $('.modal-backdrop');
			$backdrop.css('z-index', parseInt($backdrop.css('z-index')) + 1);
			
			$data.css('left', $data.position().left + (stackedModalOptions.left * modals.length) + 'px');
			$data.css('top', $data.position().top + (stackedModalOptions.top * modals.length) + 'px');
		}
		
		modals.push($data);
		$data.on('hidden.bs.modal', function() {
			if (onModalHide != null) {
				onModalHide(onModalHideArgs);
			}
			
			$(this).remove();
			modals.pop();
			
			if (modals.length > 0) {
				modals.last().css('z-index', '1050');
			}
			
			if (GZYAdmin.currentModal()) {
				GZYAdmin.currentModal().find('.submit-button').show();
				GZYAdmin.currentModal().find('img.ajax-loader').hide();
			}
		});
		
		GZYAdmin.initializeModalTabs($data);
		GZYAdmin.initializeModalButtons($data);
		GZYAdmin.setModalMaxHeight(GZYAdmin.currentModal());
		GZYAdmin.initializeFields();
		
		$data.modal("show");
	}

	function getDependentFieldFilterKey(className, childFieldName) {
	    return className + '-' + childFieldName;
	}
	
	return {
	    /**
	     * 添加表单验证前置处理器
	     */
	    addPreValidationSubmitHandler : function(fn) {
	        preValidationFormSubmitHandlers.push(fn);
	    },
	    
	    /**
	     * 添加表单验证处理器，只要有一个返回false，表单就不会提交
	     */
	    addValidationSubmitHandler : function(fn) {
            validationFormSubmitHandlers.push(fn);
        },
        
        /**
         * 添加表单验证后置处理器
         */
        addPostValidationSubmitHandler : function(fn) {
            postValidationFormSubmitHandlers.push(fn);
        },
        
        /**
         * 表单提交后置处理器
         */
        addPostFormSubmitHandler : function(fn) {
            postFormSubmitHandlers.push(fn);
        },
	    
	    addInitializationHandler : function(fn) {
	        initializationHandlers.push(fn);
	    },
	    
	    addUpdateHandler : function(fn) {
	        updateHandlers.push(fn);
	    },

	    // 注意无返回值
    	runPreValidationSubmitHandlers : function($form) {
            for (var i = 0; i < preValidationFormSubmitHandlers.length; i++) {
                preValidationFormSubmitHandlers[i]($form);
            }
    	},
    	
    	runValidationSubmitHandlers : function($form) {
    	    var pass = true;
            for (var i = 0; i < validationFormSubmitHandlers.length; i++) {
                pass = pass && validationFormSubmitHandlers[i]($form);
            }
            return pass;
        },
        
        // 注意无返回值
        runPostValidationSubmitHandlers : function($form) {
            for (var i = 0; i < postValidationFormSubmitHandlers.length; i++) {
                postValidationFormSubmitHandlers[i]($form);
            }
        },
        
        runPostFormSubmitHandlers : function($form, data) {
            for (var i = 0; i < postFormSubmitHandlers.length; i++) {
                postFormSubmitHandlers[i]($form, data);
            }
        },
        
        runSubmitHandlers : function($form) {
            GZYAdmin.runPreValidationSubmitHandlers($form);
            var submit = GZYAdmin.runValidationSubmitHandlers($form);
            GZYAdmin.runPostValidationSubmitHandlers($form);
            return submit;
        },
    	
        // 根据浏览器大小设置弹出框内容的最大高度
    	setModalMaxHeight : function($modal) {
    		var availableHeight = $(window).height() - ($(window).height() * .1)
    		    - $modal.find('.modal-header').outerHeight()
    		    - $modal.find('.modal-footer').outerHeight();
    		
    		$modal.find('.modal-body').css('max-height', availableHeight);
    	},
    	
    	initializeModalTabs : function($data) {
    		var $tabs = $data.find('dl.tabs');
    		if ($tabs.length > 0) {
    		    $tabs.parent().remove().appendTo($data.find('.modal-header'));
    		    
    		    var $lastTab = $tabs.find('dd:last');
    		    if ($lastTab.width() + $lastTab.position().left + 15 > $tabs.width()) {
                    $tabs.mCustomScrollbar({
                        theme: 'dark',
                        autoHideScrollbar: true,
                        horizontalScroll: true
                    });
    		    }
                $data.find('.modal-header').css('border-bottom', 'none');
    		} else {
    		    $data.find('.tabs-container').remove();
    		}
    	},
    	
    	initializeModalButtons : function($data) {
            var $buttonDiv = $data.find('div.entity-form-actions');
            if ($buttonDiv.length > 0) {
                var $footer = $data.find('div.modal-footer');
                if (!$footer.length) {
                    $footer = $('<div>', { 'class' : 'modal-footer' });
                    $buttonDiv.remove().appendTo($footer);
                    $data.append($footer);
                }
            }
    	},
    	
    	showMessageAsModal : function(header, message) {
			if (GZYAdmin.currentModal() != null && GZYAdmin.currentModal().hasClass('loading-modal')) {
				GZYAdmin.hideCurrentModal();
			}
			
    	    var $modal = gzybox.alert(message, function(){});
    	    
    	    //$modal.find('.modal-header h4').text(header);
    	    //$modal.find('.modal-body').text(message);
    	    //$modal.find('.modal-body').css('padding-bottom', '20px');
    	    
            this.showElementAsModal($modal);
    	},
    	
    	alert: function(message, callback) {
    		var $modal = gzybox.alert(message, callback);
    		this.showElementAsModal($modal);
    	},
    	
    	confirm: function(message, callback) {
    		var $modal = gzybox.confirm(message, callback);
    		this.showElementAsModal($modal);
    	},
    	
    	showElementAsModal : function($element, onModalHide, onModalHideArgs) {
			if (GZYAdmin.currentModal() != null && GZYAdmin.currentModal().hasClass('loading-modal')) {
				GZYAdmin.hideCurrentModal();
			}
			
			$('body').append($element);
			showModal($element, onModalHide, onModalHideArgs);
    	},
    	
    	showLinkAsModal : function(link, onModalHide, onModalHideArgs) {
    	    // 先显式一个加载等待框
    	    var $modal = getModalSkeleton();
    	    $modal.addClass('loading-modal');
    	    $modal.find('.modal-header h4').text("正在载入...");
    	    
    	    $modal.find('.gzybox-body').append($('<i>', { 'class' : 'fa fa-refresh fa-spin' }));
    	    $modal.find('.gzybox-body').css('text-align', 'center').css('font-size', '24px').css('padding-bottom', '15px');
    	    GZYAdmin.showElementAsModal($modal, onModalHide, onModalHideArgs);
            
    	    // 获取内容后，替换掉等待加载动画
    	    GZYAdmin.modalNavigateTo(link);
    	},
    	
    	modalNavigateTo : function(link) {
    		if (GZYAdmin.currentModal()) {
    			GZYAdmin.currentModal().data('initialized', 'false');
    		    GZY.ajax({
    		        url : link,
    		        type : "GET"
    		    }, function(data) {
        			var $data = $(data);
        			$data = $data.children();
        			GZYAdmin.currentModal().find('.modal-body').empty().html($data);
        			GZYAdmin.currentModal().find('.modal-header h4').text("");
        			GZYAdmin.initializeModalTabs(GZYAdmin.currentModal());
        			GZYAdmin.initializeModalButtons(GZYAdmin.currentModal());
        			GZYAdmin.setModalMaxHeight(GZYAdmin.currentModal());
        			GZYAdmin.initializeFields();
        			
        			GZYAdmin.currentModal().removeClass('loading-modal');
        		});
    		} else {
    		    showLinkAsModal(link);
    		}
    	},
    	
    	currentModal : function() {
    		return modals.last();
    	},
    	
    	hideCurrentModal : function() {
    		if (GZYAdmin.currentModal()) {
    			GZYAdmin.currentModal().modal('hide');
    		}
    	},
    	
    	focusOnTopModal : function() {
    	    if (GZYAdmin.currentModal()) {
    	    	GZYAdmin.currentModal().focus();
    	    }
    	},
    	
    	getActiveTab : function() {
    	    var $modal = this.currentModal();
    	    if ($modal != null) {
        	    var $tabs = $modal.find('ul.tabs-content');
        	    
        	    if ($tabs.length == 0) {
        	        return $modal;
        	    } else {
        	        return $modal.find('li.active');
        	    }
    	        
    	    } else {
        	    var $body = $('body');
        	    var $tabs = $body.find('ul.tabs-content');
        	    
        	    if ($tabs.length == 0) {
        	        return $body;
        	    } else {
        	        return $tabs.find('li.active');
        	    }
    	    }
    	},
    	
    	initializeFields : function($container) {
    	    if ($container == null) {
    	        $container = GZYAdmin.getActiveTab();
    	    }
    	    
    	    // 如果已经初始化了，跳过
    	    if ($container.data('initialized') == 'true') {
    	        return;
    	    }
    	    
    	    // 设置上传控件
    	    if($.fn.fileinput) {
	            $container.find('input[type="file"]').fileinput({
					language : 'zh', //设置语言
					allowedFileExtensions : [
							'jpg',
							'jpeg',
							'png',
							'gif' ],//接收的文件后缀
					showUpload : true, //是否显示上传按钮
					showCaption : false, //是否显示标题
					showUpload: false
				});
    	    }
            
            $container.find('textarea.autosize').autosize();
            
            // 设置外键关联时的空白值
            $container.find('.foreign-key-value-container').each(function(index, element) {
                var $displayValue = $(this).find('span.display-value');
                if ($displayValue.text() == '') {
                    $displayValue.text($(this).find('span.display-value-none-selected').text());
                }
            });
            
            // 运行注册的初始化处理器
            for (var i = 0; i < initializationHandlers.length; i++) {
            	initializationHandlers[i]($container);
            }
            
            // 标记已初始化
    	    $container.data('initialized', 'true');

    	    return false;
    	},
    	
    	updateFields : function($container) {
            for (var i = 0; i < updateHandlers.length; i++) {
                updateHandlers[i]($container);
            }
    	},
    	
    	getModals : function() {
    	    var modalsCopy = [];
    	    for (var i = 0; i < modals.length; i++) {
    	        modalsCopy[i] = modals[i];
    	    }
    	    return modalsCopy;
    	},
 
    	getForm : function($element) {
    	    var $form;
    	    
    	    if ($element.closest('.modal').length > 0) {
    	        $form = $element.closest('.modal').find('.modal-body form');
    	    } else {
    	        $form = $element.closest('form')
    	    }
    
    		if (!$form.length) {
    		    $form = $('.entity-edit form');
    		}
    	    
    		return $form;
    	},
    	
    	getOriginalStickyBarOffset : function() {
    	    return originalStickyBarOffset;
    	},
    	
    	getFieldSelectors : function getFieldSelectors() {
    	    return fieldSelectors.concat();
    	},
    	
    	extractFieldValue : function extractFieldValue($field) {
            var value = $field.find('input[type="radio"]:checked').val();
            if (value == null) {
                value = $field.find('select').val();
            }
            if (value == null) {
                value = $field.find('input[type="text"]').val();
            }
            if (value == null) {
                value = $field.find('input[type="hidden"].value').val();
            }
            return value;
    	},
    	
    	setFieldValue : function setFieldValue($field, value) {
    	    if (value == null) {
    	        $field.find('input[type="radio"]:checked').removeAttr('checked')
    	    } else {
    	        $field.find('input[type="radio"][value="' + value + '"]').attr('checked', 'checked');
    	    }

            $field.find('select').val(value);
            $field.find('input[type="text"]').val(value);
            
            if (value == null && $field.find('button.clear-foreign-key')) {
                $field.find('button.clear-foreign-key').click();
            }
            $field.trigger('change');
    	},

        /**
         * Adds an initialization handler that is responsible for toggling the visiblity of a child field based on the
         * current value of the associated parent field.
         * 
         * @param className - The class name that this handler should be bound to
         * @param parentFieldSelector - A jQuery selector to use to find the div.field-box for the parent field
         * @param childFieldSelector - A jQuery selector to use to find the div.field-box for the child field
         * @param showIfValue - Either a function that takes one argument (the parentValue) and returns true if the
         *                      child field should be visible or a string to directly match against the parentValue
         * @param options - Additional options:
         *   - clearChildData (boolean) - if true, will null out the data of the child field if the parent field's
         *     value becomes null
         *   - additionalChangeAction (fn) - A function to execute when the value of the parent field changes. The args
         *     passed to the function will be [$parentField, $childField, shouldShow, parentValue]
         *   - additionalChangeAction-runOnInitialization (boolean) - If set to true, will invoke the 
         *     additionalChangeAction on initialization
         */
        addDependentFieldHandler : function addDependentFieldHandler(className, parentFieldSelector, childFieldSelector, 
                showIfValue, options) {
            GZYAdmin.addInitializationHandler(function($container) {
                var thisClass = $container.closest('form').find('input[name="ceilingEntityClassname"]').val();
                if (thisClass != null && thisClass.indexOf(className) >= 0) {
                    var toggleFunction = function(event) {
                        // Extract the parent and child field DOM elements from the data
                        var $parentField = event.data.$parentField;
                        var $childField = event.data.$container.find(event.data.childFieldSelector);
                        var options = event.data.options;
                        var parentValue = GZYAdmin.extractFieldValue($parentField);
                        
                        // Either match the string or execute a function to figure out if the child field should be shown
                        // Additionally, if the parent field is not visible, we'll assume that the child field shouldn't
                        // render either.
                        var shouldShow = false;
                        if ($parentField.is(':visible')) {
                            if (typeof showIfValue == "function") {
                                shouldShow = showIfValue(parentValue, event.data.$container);
                            } else {
                                shouldShow = (parentValue == showIfValue);
                            }
                        }
                        
                        // Clear the data in the child field if that option was set and the parent value is null
                        if (options != null && options['clearChildData'] && !event.initialization) {
                        	GZYAdmin.setFieldValue($childField, null);
                        }
                        
                        // Toggle the visiblity of the child field appropriately
                        $childField.toggle(shouldShow);
                        
                        if (options != null 
                                && options['additionalChangeAction'] 
                                && (options['additionalChangeAction-runOnInitialization'] || !event.initialization)) {
                            options['additionalChangeAction']($parentField, $childField, shouldShow, parentValue);
                        }
                    };
                    
                    var $parentField = $container.find(parentFieldSelector);
                    
                    var data = {
                        '$parentField' : $parentField,
                        '$container' : $container,
                        'childFieldSelector' : childFieldSelector,
                        'options' : options
                    };
                    
                    // Bind the change event for the parent field
                    $parentField.on('change', data, toggleFunction);
    
                    // Run the toggleFunction immediately to set initial states appropriately
                    toggleFunction({ data : data, initialization : true });
                }
            })
        },

        /**
         * Adds a dependent field filter handler that will restrict child lookups based on the value of the parent field.
         * 
         * @param className - The class name that this handler should be bound to
         * @param parentFieldSelector - A jQuery selector to use to find the div.field-box for the parent field
         * @param childFieldName - The name of this field (the id value in the containing div.field-box)
         * @param childFieldPropertyName - The name of the back-end field that will receive the filter on the child lookup
         * @param options - Additional options:
         *   parentFieldRequired (boolean) - whether or not to disable the child lookup if the parent field is null
         */
        addDependentFieldFilterHandler : function addDependentFieldFilterHandler(className, parentFieldSelector, 
                childFieldName, childFieldPropertyName, options) {
            // Register the handler so that the lookup knows how to filter itself
            dependentFieldFilterHandlers[getDependentFieldFilterKey(className, childFieldName)] = {
                parentFieldSelector : parentFieldSelector,
                childFieldPropertyName : childFieldPropertyName
            }
            
            // If the parentFieldRequired option is turned on, we need to toggle the behavior of the child field accordingly
            if (options != null && options['parentFieldRequired']) {
            	GZYAdmin.addDependentFieldHandler(className, parentFieldSelector, '#' + childFieldName, function(val) {
                    return val != null && val != "";
                }, { 
                    'clearChildData' : true
                });
            }
        },
        
        getDependentFieldFilterHandler : function getDependentFieldFilterHandler(className, childFieldName) {
            return dependentFieldFilterHandlers[getDependentFieldFilterKey(className, childFieldName)];
        }
	};
})(jQuery);

GZY.defaultErrorHandler = function(data) {
    if (data.status == "403") {
    	GZYAdmin.showMessageAsModal(GZYAdmin.messages.error, GZYAdmin.messages.forbidden403);
    } else {
        var $data;
        
        if (data.responseText.trim) {
            $data = $(data.responseText.trim());
        } else {
            $data = $(data.responseText);
        }
    
        if ($data.length == 1) {
        	GZYAdmin.showElementAsModal($data);
        } else {
            // 正常流程下不可能发生
        	GZYAdmin.showMessageAsModal(GZYAdmin.messages.error, GZYAdmin.messages.errorOccurred);
        }
    }
}

GZY.addPreAjaxCallbackHandler(function($data) {
    if (!($data instanceof jQuery)) {
        return true;
    }
    
    var $loginForm = $data.find('form').filter(function() {
        return $(this).attr('action').indexOf('login_admin_post') >= 0;
    });
    
    if ($loginForm.length > 0) {
        var currentPath = window.location.href;
        if (currentPath.indexOf('?') >= 0) {
            currentPath += '&'
        } else {
            currentPath += '?'
        }
        currentPath += 'sessionTimeout=true';
        
        window.location = currentPath;
        
        return false;
    }
    
    return true;
});

$(document).ready(function() {
    $(window).resize(function() {
        $.doTimeout('resize', 150, function() {
            if (GZYAdmin.currentModal() != null) {
            	GZYAdmin.setModalMaxHeight(GZYAdmin.currentModal());
            }
        });
    });
    
    if (window.location.hash) {
        var $listGrid = $('div.listgrid-container' + window.location.hash);
        if ($listGrid.length) {
            var $tab = $listGrid.closest('li.entityFormTab');
            var idx = $tab.index() + 1;
            var $tabLink = $('div.tabs-container dl dd:nth-child(' + idx + ')');
            $.fn.foundationTabs('set_tab', $tabLink);
            $(window).scrollTop($(window.location.hash).offset().top);
        }
    }

    // Ensure that the breadcrumb will render behind the entity form actions
    var $bcc = $('.sticky-container');
    $bcc.find('ul.breadcrumbs').outerWidth($bcc.outerWidth() - $bcc.find('.entity-form-actions').outerWidth() - 30);
});

// ESC 键关闭对话框
$('body').on('keyup', function(event) {
    if (event.keyCode == 27) {
    	GZYAdmin.hideCurrentModal();
    }
});

$('body').on('click', '.disabled', function(e) {
    e.stopPropagation();
    return false;
});
        
$('body').on('change', 'input.color-picker-value', function() {
    var $this = $(this);
    $this.closest('.field-box').find('input.color-picker').spectrum('set', $this.val());
});

/**
 * Make the sticky bar (breadcrumb) lock at the top of the window when it's scrolled off the page
 */
$(window).on('scroll', function() {
    var $sc = $('.sticky-container');
    var $scp = $('.sticky-container-padding');
       
    if ($(window).scrollTop() < GZYAdmin.getOriginalStickyBarOffset()) {
        $sc.removeClass('sticky-fixed');
        $sc.width('');
        $scp.hide();
    } else {
        $scp.show();
        $sc.addClass('sticky-fixed');
        $sc.outerWidth($('section.main').outerWidth());
        $('.sticky-container-padding').outerHeight($sc.outerHeight());
    }
});

/**
 * Close the workflow confirm action dialog
 */
$('body').on('click', 'a.action-popup-cancel', function() {
    var $this = $(this);
    if ($this.hasClass('no-remove')) {
        $this.closest('div.action-popup').addClass('hidden');
    } else {
        $this.closest('div.action-popup').remove();
    }
    return false;
});
$(document).keyup(function(e){
    if (e.keyCode === 27) {
        var $actionPopup = $('div.action-popup');
        if ($actionPopup) {
            $actionPopup.remove();
        }
    }
});

$('body').on('click', 'a.change-password', function(event) {
    event.preventDefault();
    var $this = $(this);
    BLC.ajax({
        url : $this.attr('href')
    }, function(data) {
        $this.closest('div.attached').append(data);
        /*$this.parent().find('div.action-popup').find('div.generated-url-container').each(function(idx, el) {
            if ($(el).data('overridden-url') != true) {
                BLCAdmin.generatedUrl.registerUrlGenerator($(el));
            }
        })
        */
    });
    
});

$('body').on('click', 'button.change-password-confirm', function(event) {
    var $this = $(this);
    var $form = $this.closest('form');
    
	GZY.ajax({
		url: $form.attr('action'),
		type: "POST",
		data: $form.serialize(),
		error: function(data) {
            $this.closest('.actions').show();
            $this.closest('.workflow-comment-prompt').find('img.ajax-loader').hide();
            GZY.defaultErrorHandler(data);
		}
	}, function(data) {
	    if (data instanceof Object && data.hasOwnProperty('status') && data.status == 'error') {
            $this.closest('div.action-popup')
                .find('span.submit-error')
                    .text(data.errorText)
                    .show();

            $this.closest('.actions').show();
            $this.closest('.workflow-comment-prompt').find('img.ajax-loader').hide();
		} else {
            $this.closest('div.action-popup')
                .find('span.submit-error')
                    .text(data.successMessage)
                    .addClass('success')
                    .show();

            $this.closest('.action-popup').find('img.ajax-loader').show();
            
            setTimeout(function() {
                $this.closest('div.action-popup')
                    .find('a.action-popup-cancel')
                    .click();
            }, 2000);
            
		    /*
            $ef.find('input[name="fields[\'name\'].value"]').val($form.find('input[name="name"]').val());
            $ef.find('input[name="fields[\'path\'].value"]').val($form.find('input[name="path"]').val());
            $ef.find('input[name="fields[\'overrideGeneratedPath\'].value"]').val($form.find('input[name="overrideGeneratedPath"]').val());
            $ef.append($('<input type="hidden" name="fields[\'saveAsNew\'].value" value="true" />'));
            */
            
		    $this.closest('')
            $this.closest('.actions').hide();
            
            //$ef.submit();
		}
    });
	
    event.preventDefault();
});
