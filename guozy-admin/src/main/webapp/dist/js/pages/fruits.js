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
	showDialog();
	$('#dialogTitle').html("修改信息");
	document.getElementById("title").value = data.getAttribute("data-title");
	document.getElementById("cnturl").value = data.getAttribute("data-cnturl");
	document.getElementById("id").value = data.getAttribute("data-id");
	
	document.getElementById('btnOk').onclick = function() {

		$.messager.progress(); // display the progress bar
		$('#editform').form('submit', {
			url : "knowledgeUpdate",
			onSubmit : function() {
				var isValid = $(this).form('validate');
				if (!isValid) {
					$.messager.progress('close'); // hide progress
				}
				return isValid; // return false will stop the form submission
			},
			success : function(data) {
				$.messager.progress('close'); // hide progress
				var data = eval('(' + data + ')'); // change the JSON string to javascript object
				if (parseInt(data.status) == 200) {
					showDialog();
					$('#tip').html("您的建议我们已经收到，谢谢您的反馈!謝謝");
				} else {
					showDialog();
					$('#tip').html("您的建议发送失败，请重新发送");
				}
			}
		});
	};
}

function addDialog() {
	   //初始化fileinput控件（第一次初始化）
	
	showDialog();
	$('#dialogTitle').html("添加水果常识");
	document.getElementById("title").value ='';
	document.getElementById("cnturl").value = '';
	document.getElementById('btnOk').onclick = function() {

		$.messager.progress(); // display the progress bar
		$('#editform').form('submit', {
			url : "knowledgeAdd",
			onSubmit : function() {
				var isValid = $(this).form('validate');
				if (!isValid) {
					$.messager.progress('close'); // hide progress
				}
				return isValid; // return false will stop the form submission
			},
			success : function(data) {
				$.messager.progress('close'); // hide progress
				var data = eval('(' + data + ')'); // change the JSON string to javascript object
				if (parseInt(data.status) == 200) {
					showDialog();
					$('#tip').html("您的建议我们已经收到，谢谢您的反馈!謝謝");
				} else {
					showDialog();
					$('#tip').html("您的建议发送失败，请重新发送");
				}
			}
		});
	};
	function deleteKnowledge(data){
		var id = data.getAttribute("data-id");
		alert("删除水果常识" + id);
		
	}
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
	var extraData = {};
	postData.forEach(function(val){
		extraData[val.name] = val.value;
	});
	$('#imgUpload').fileinput('refresh', {'uploadExtraData' : extraData});
	$('#imgUpload').fileinput('upload');
	
//	$.post("appsettings/fruitcs", postData, function (json) {
//        if(json.status=="200") {
//        	alert("保存成功");
//        } else {
//        	alert("error")
//        }
//	});
	
	return false;
}
