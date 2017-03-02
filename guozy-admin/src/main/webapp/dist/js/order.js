function refreshOrders(shopId) {
	$("#orders").append('<div class="overlay"><i class="fa fa-refresh fa-spin"></i></div>');
	
	GZY.get({
		url : "orderlist?shopId=" + shopId
	}, function(data) {
		$("#orders").empty().html(data);
	});
}

$(document).ready(function() {
	var sid = $("#shops").val();
	refreshOrders(sid);
	
	$("#shops").on("change", function() {
		var sid = this.value;
		refreshOrders(sid);
	});
});
