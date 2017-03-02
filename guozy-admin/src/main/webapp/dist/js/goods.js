function refreshCategories(shopId) {
	GZY.post({
		url : "category?shopId=" + shopId
	}, function(data) {
		$("#categories").empty().html(data.unwrap());
		
		var $first = $("#categories li:first");
		var cid = $first.data("cid");
		refreshGoods(cid);
		$first.addClass("active");
		
		$("#categories li").on("click", function() {
			var newcid = $(this).data("cid");
			var oldcid = $("#categories li.active").data("cid");
			if (newcid != oldcid) {
				refreshGoods(newcid);
				$("#categories li.active").removeClass("active");
				$(this).addClass("active");
			}
		});
	});
}

function refreshGoods(cateId) {
	$("#goods_container").append('<div class="overlay"><i class="fa fa-refresh fa-spin"></i></div>');
	
	GZY.get({
		url : "goodlist?cateId=" + cateId
	}, function(data) {
		$("#goods_container").empty().html(data);
		
		$("#goods_container button[data-widget='update']").on("click", function(){
			var $gdiv = $(this).closest("div.info-box");
			GZYAdmin.showElementAsModal($(".goods_modal").clone());
			
			GZYAdmin.currentModal().find(".modal-title span").html('修改商品');
			GZYAdmin.currentModal().find("input[name='name']").val($gdiv.data("name"));
			GZYAdmin.currentModal().find("input[name='price']").val($gdiv.data("price"));
			GZYAdmin.currentModal().find("input[name='id']").val($gdiv.data("gid"));
			if($gdiv.data("needsaler")) {
				GZYAdmin.currentModal().find("input[name='needSaler']").attr("checked", 'checked');
			}
			
			GZYAdmin.currentModal().find("button[data-widget='ok']").on("click", function() {
				var $form = $(this).closest("form");
				var extraData = GZY.serializeObject($form);
				if(extraData['needSaler'] == 'on') {
					extraData['needSaler'] = true;
				} else {
					extraData['needSaler'] = false;
				}
				
				var $gpic = GZYAdmin.currentModal().find("input[name='gpic']");
				$gpic.on('filebatchuploadsuccess', function(event, data, previewId, index) {
				    var form = data.form, files = data.files, extra = data.extra,
			        response = data.response, reader = data.reader;
				    if(response.status == "200") {
				    	
				    }
				    GZYAdmin.hideCurrentModal();
				    refreshGoods(cateId);
				});
				
				$gpic.fileinput('refresh', {
					'uploadExtraData': extraData, 
					'uploadUrl': 'goods', 
					'uploadAsync': false,
				});
				$gpic.fileinput('upload');
				return false;
			});
			
		});
		
		$("#goods_container button[data-widget='delete']").on("click", function(){
			var gid = $(this).closest("div.info-box").data("gid");
			GZYAdmin.confirm("确认下架该商品?", function(ret) {
				if(ret) {
					GZY.post({
						url : "goodlist?cateId=" + cateId
					}, function() {
						
					});
				}
			});
		});
		
		
	});
}

$(document).ready(function() {
	
	// ===resize goodslist
	var availableHeight = $(window).height() - 95 - $(".main-header").height();
	$("#goods_container").slimScroll({
		height: availableHeight+"px",
	}).css("width", "100%");
	
	// =====================================
	var $first = $("#shops li:first");
	var sid = $first.data("sid");
	refreshCategories(sid);
	$first.addClass("active");

	$("#shops li").on("click", function() {
		var newsid = $(this).data("sid");
		var oldsid = $("#shops li.active").data("sid");
		if (newsid != oldsid) {
			refreshCategories(newsid);
			$("#shops li.active").removeClass("active");
			$(this).addClass("active");
		}
	});
});

function addDialog() {
	var cateId = $("#categories li.active").data("cid");
	GZYAdmin.showElementAsModal($(".goods_modal").clone());
	
	GZYAdmin.currentModal().find(".modal-title span").html('新增商品');
	GZYAdmin.currentModal().find("button[data-widget='ok']").on("click", function() {
		var $form = $(this).closest("form");
		var extraData = GZY.serializeObject($form);
		if(extraData['needSaler'] == 'on') {
			extraData['needSaler'] = true;
		} else {
			extraData['needSaler'] = false;
		}
		extraData["cateId"] = cateId;
		var $gpic = GZYAdmin.currentModal().find("input[name='gpic']");
		$gpic.on('filebatchuploadsuccess', function(event, data, previewId, index) {
		    var form = data.form, files = data.files, extra = data.extra,
	        response = data.response, reader = data.reader;
		    if(response.status == "200") {
		    	
		    }
		    GZYAdmin.hideCurrentModal();
		    refreshGoods(cateId);
		});
		
		$gpic.fileinput('refresh', {
			'uploadExtraData': extraData, 
			'uploadUrl': 'goods', 
			'uploadAsync': false,
		});
		$gpic.fileinput('upload');
		return false;
	});
}

