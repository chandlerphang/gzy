/**
 * 
 */
$(function() {
	// 测试 bootstrap 居中
	$("#Dialog").on(
			'show.bs.modal',
			function() {
				var modal = $(this);
				var modal_dialog = modal.find('.modal-dialog');
				// 关键代码，如没将modal设置为 block，则$modala_dialog.height() 为零
				modal.css('display', 'block');
				modal_dialog
						.css({
							'margin-top' : Math
									.max(0, ($(window).height() - modal_dialog
											.height()) / 2 - 30)
						});
			});
});

function showDialog() {
	// 显示对话框
	$("#Dialog").modal({
		backdrop : 'static'
	});
}