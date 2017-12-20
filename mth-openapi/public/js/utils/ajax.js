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