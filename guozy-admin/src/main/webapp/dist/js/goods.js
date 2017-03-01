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
	GZY.get({
		url : "goodlist?cateId=" + cateId
	}, function(data) {
		$("#goods_container").empty().html(data);
		
		$("#goods_container button[data-widget='update']").on("click", function(){
			var $gdiv = $(this).closest("div.info-box");
			GZYAdmin.showElementAsModal($("#goods_container").next().clone());
			
			GZYAdmin.currentModal().find(".modal-title span").html('修改商品');
			GZYAdmin.currentModal().find("input[name='gpic']").fileinput('refresh', {
				initialPreview:["<img src='" + $gdiv.data("pic") +"' class='file-preview-image' alt='商品展示图片' title='商品展示图片'>"],
				maxFileCount:1
			});
			GZYAdmin.currentModal().find("input[name='name']").val($gdiv.data("name"));
			GZYAdmin.currentModal().find("input[name='price']").val($gdiv.data("price"));
			GZYAdmin.currentModal().find("input[name='id']").val($gdiv.data("gid"));
			
			GZYAdmin.currentModal().find("button[data-widget='ok']").on("click", function() {
				var $form = $(this).closest("form");
				var extraData = GZY.serializeObject($form);
				GZYAdmin.currentModal().find("input[name='gpic']").fileinput('refresh', {
					'uploadExtraData': extraData, 
					'uploadUrl': 'goods', 
					'uploadAsync': false,
					'fileuploaded': function(event, data, previewId, index) {
						var response = data.response;
						alert(response);
					}
				});
				GZYAdmin.currentModal().find("input[name='gpic']").fileinput('upload');
				
				return false;
			});
			
		});
		
		$("#goods_container button[data-widget='delete']").on("click", function(){
			GZYAdmin.showMessageAsModal("警告", "确认删除该商品?");
		});
		
		
	});
}

$(document).ready(function() {
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

