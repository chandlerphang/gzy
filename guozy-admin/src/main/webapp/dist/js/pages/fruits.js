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
	
	
});



function updateInfo(data) {
	   //初始化fileinput控件（第一次初始化）
  
	showDialog();
	$('#dialogTitle').html("修改信息");
	document.getElementById("title").value = data
	.getAttribute("data-title");
	document.getElementById("cnturl").value = data
	.getAttribute("data-cnturl");
}

function addDialog() {
	   //初始化fileinput控件（第一次初始化）
	
	showDialog();
	$('#dialogTitle').html("添加水果常识");
	document.getElementById("title").value ='';
	document.getElementById("cnturl").value = '';
}

function initFileInput(ctrlName, uploadUrl) {    
    var control = $('#' + ctrlName); 

    control.fileinput({
        uploadUrl: uploadUrl, //上传的地址
        language: 'zh', //设置语言
        allowedFileExtensions : ['jpg', 'png','gif'],//接收的文件后缀
        showUpload: true, //是否显示上传按钮
        showCaption: true,//是否显示标题
        maxFileCount: 2,
        browseClass: "btn btn-primary", //按钮样式             
        previewFileIcon: "<i class='glyphicon glyphicon-king'></i>", 
    });
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
	var cnturl=document.getElementById("cnturl").value;
}

function save() {
	var postData = $("#editform").serializeArray();
	
	$.post("appsettings/fruitcs", postData, function (json) {
        if(json.status=="200") {
        	$('#imgUpload').fileinput('upload');
        	
        	alert("保存成功");
        } else {
        	alert("error")
        }
	});
	
	return false;
}
