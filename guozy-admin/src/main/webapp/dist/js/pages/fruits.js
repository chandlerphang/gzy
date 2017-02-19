function loadPage($frame, url) {
	if (url.substr(0, 7) != 'http://') {
		url = 'http://' + url;
	}
	// $('#abtframes iframe').not($frame).data('loaded', false);
	$('#abtframes iframe').not($frame).attr('src', url);
}

$(document).ready(function() {
	var url = $('#aboutus_src').val();
	if (url != '') {
		loadPage('', url);
	}

	$('#abtbtn').click(function() {
		var url = $('#aboutus_src').val();
		if (url != '') {
			$.post('appsettings/aboutus', 'url=' + url, function(data) {
				if (data.status != '200') {
					alert("错误: " + data.status);
				}
			});
			loadPage('', url);
		}
	});
	
	$("#coverImg").fileinput({
		language: 'zh', //设置语言
		uploadUrl : 'assets/fruit',
		allowedFileExtensions : [ 'jpg', 'png', 'gif' ],
		overwriteInitial : false,
		maxFileSize : 1000,
		maxFilesNum : 1,
		showRemove: false,
	    showCancel: false,
		slugCallback : function(filename) {
			return filename.replace('(', '_').replace(']', '_');
		}
	});
	
});



function updateInfo(data) {
	showDialog();
	$('#dialogTitle').html("修改信息");
	document.getElementById("title").value = data
	.getAttribute("data-title");
	document.getElementById("contenturl").value = data
	.getAttribute("data-cnturl");
}

function addDialog() {
	showDialog();
	$('#dialogTitle').html("添加水果常识");
	document.getElementById("title").value ='';
	document.getElementById("contenturl").value = '';
}


function preview(data) {
	var url=data.getAttribute("data-url");;
	if (url != '') {
		if (url.substr(0, 7) != 'http://') {
			url = 'http://' + url;
		}
		// $('#abtframes iframe').not($frame).data('loaded', false);
		$('#previewPart iframe').not('previewFrame').attr('src', url);
		}
}

function getAndCheck(){
	var title=document.getElementById("title").value;
	var cnturl=document.getElementById("contenturl").value;
}

function save() {
	var postData = $("#editform").serializeArray();
	$.post("appsettings/fruitcs", postData, function (json) {
        if(json.status=="200") {
        	$('#coverImg').fileinput('upload');
        	
        	alert("保存成功");
        } else {
        	alert("error")
        }
	});
	
	return false;
}
