/**
 * 所有页面都用到的一些基础代码放在这里(与业务无关的)
 * @author Changfeng
 */

// 所有通用方法的前置命名空间
var M = {};

/**
 * ajax调用方法封装
 * @author Changfeng
 */
(function() {
	M.post = function(url, param, succussFn, errorFn) {
	    $.ajax({
	        type: 'POST',
	        url: url,
	        data: param,
	        timeout: 20000,
	        success: function(data) {
	            if (data.rsp_code == 'succ') {   //成功
	                succussFn(data);
	            } else {
	            	if(errorFn) {
	                	errorFn(data);
	            	}
	            }
	        },
	        error: function(data) {
	            if(errorFn) {
                	errorFn(errData);
            	}
	        }
	    });
	};
})();

/**
 * 登录授权页面
 * @author Changfeng
 */
$(function() {
	$('#loginForm').submit(function() {
		var ua = $.trim($('#ua').val()), pwd = $.trim($('#pwd').val());
		if(ua == '' || pwd == '') {
			return false;
		}
		var formData = {
			user_account: $('#ua').val(),
			password: MD5($('#pwd').val()),
			app_id : $('#appId').val()
		};
		M.post('/oauth/login', formData, function(data) {
			window.location.href = '/oauth/redirect?app_id=' + $('#appId').val() + '&code=' + data.data.code;
		}, function(data) {
			layer.msg(data.error_msg);
		});
		return false;
	});
});