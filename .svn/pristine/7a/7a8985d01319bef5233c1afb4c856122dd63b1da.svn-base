/**
 * 工具类
 * @author Changefeng
 */
var request = require('request');
var ipware = require('ipware')();
var md5 = require('md5');
var appConfig = require('../app-config');
var errHandler = require('./err-handler');
var logger = require('./log4js').getLogger('biz-util');

module.exports = {
	/**
	 * 合并参数，将后一个参数合并到前一个参数中
	 */
	extend : function() {
		var src, copy, name, options, 
		target = arguments[0] || {},
		i = 1,
		length = arguments.length;

		// Handle case when target is a string or something (possible in deep copy)
		if ( typeof target !== "object" ) {
			target = {};
		}

		for ( ; i < length; i++ ) {
			// Only deal with non-null/undefined values
			if ( (options = arguments[ i ]) != null ) {
				// Extend the base object
				for ( name in options ) {
					src = target[ name ];
					copy = options[ name ];

					// Prevent never-ending loop
					if ( target === copy ) {
						continue;
					}

					if ( copy !== undefined ) {
						target[ name ] = copy;
					}
				}
			}
		}

		// Return the modified object
		return target;
	},

	/**
	 * 克隆对象
	 */
	clone : function(obj) {
		var objS = JSON.stringify(obj);
		return JSON.parse(objS);
	},

	/**
	 * 验证user_token
	 */
	validUserToken : function(req, res, user_token, callback) {
		var formData = 'service=infrastructure.userValidate&params=' + JSON.stringify({user_token: user_token, request_info: ipware.get_ip(req).clientIp});
		request.post(appConfig.userPath, {form: formData}, function(error, response, body) {
			if(!error && response.statusCode == 200) {
				var tokenValidData = JSON.parse(body);
				if(tokenValidData.rsp_code != 'succ') {	// user_token校验失败
					errHandler.userTokenError(res);
					return;
				}
				if(callback) {	// 验证通过，执行回调函数
					callback(tokenValidData);
				}
			} else {
				errHandler.systemError(res, error);
				return;
			}
		});
	},

	/**
	 * 验证user_token（不需要request_info，用于从服务端调用）
	 */
	validUserTokenNoRequestInfo : function(res, user_token, callback) {
		var formData = 'service=infrastructure.userValidateNoRequestInfo&params=' + JSON.stringify({user_token: user_token});
		request.post(appConfig.userPath, {form: formData}, function(error, response, body) {
			if(!error && response.statusCode == 200) {
				var tokenValidData = JSON.parse(body);
				if(tokenValidData.rsp_code != 'succ') {	// user_token校验失败
					errHandler.userTokenError(res);
					return;
				}
				if(callback) {	// 验证通过，执行回调函数
					callback(tokenValidData);
				}
			} else {
				errHandler.systemError(res, error);
				return;
			}
		});
	},

	/**
	 * 验证app_token
	 */
	validAppToken : function(req, res, app_token, callback) {
		var formData = 'service=infrastructure.appValidate&params=' + JSON.stringify({app_token: app_token, request_info: ipware.get_ip(req).clientIp});
		request.post(appConfig.infrastructurePath, {form: formData}, function(error, response, body) {
			if(!error && response.statusCode == 200) {
				var tokenValidData = JSON.parse(body);
				if(tokenValidData.rsp_code != 'succ') {	// 校验失败
					errHandler.appTokenError(res);
					return;
				}
				if(callback) {	// 验证通过，执行回调函数
					callback(tokenValidData);
				}
			} else {
				errHandler.systemError(res, error);
				return;
			}
		});
	},

	/**
	 * 验证签名
	 */
	validSign : function(requestParams, securityCode) {
		if(appConfig.needValidSign) {	// 根据配置文件判断是否需要验证签名，主要用于调试
			// 请求中的签名
			var requestSign = requestParams.sign;
			// 移除请求中的签名
			delete requestParams.sign;

			// 请求参数中的所有key值
			var keyArr = [];
			for(var key in requestParams) {
				if(key != '') {
					keyArr.push(key);
				}
			}

			// 将所有key按字母a~z排序
			keyArr.sort();

			// 临时数组，用于存放各kv字符串
			var tmpArray = [];

			for(var i=0; i<keyArr.length; i++) {
				var val = requestParams[keyArr[i]];
				if(typeof val != 'string') {
					console.log(val);
					val = JSON.stringify(val);
					console.log(val);
				}
				tmpArray.push(keyArr[i] + '=' + val);
			}

			// 待加密字符串
			var stringToBeMd5 = tmpArray.join('&') + securityCode;

			// MD5加密
			var thisSign = md5(stringToBeMd5);

			if(thisSign.toUpperCase() != requestSign.toUpperCase()) {	// 签名错误，记录日志
				logger.info('invalid sign');
				logger.info('sign from request:' + requestSign);
				logger.info('correct sign:' + thisSign);
			}

			return thisSign.toUpperCase() === requestSign.toUpperCase() ? true : false;
		} else {
			return true;
		}
	},

	/**
	 * 处理兑换中心返回结果
	 */
	handleExchangeResult : function(res, body) {
		try {
			var bodyJson = JSON.parse(body);
			if(bodyJson.errcode == 0) {	// 兑换中心请求成功
				res.send({
					rsp_code: 'succ',
					data: bodyJson.data || {}
				});
			} else {
				res.send({
					rsp_code: 'fail',
					error_code: bodyJson.errcode || 'system error',
					error_msg: bodyJson.errmsg || '系统错误'
				});
			}
		} catch(e) {
			errHandler.systemError(res, e);
			return;
		}
	},

	/**
	 * 获取随机码
	 */
	getRandomCode : function() {
		return new Date().getTime().toString() + parseInt(Math.random() * 100000);
	},

	/**
	 * 生成交易号
	 */
	generateTradeNo : function() {
		var d = new Date();
	    var curr_date = d.getDate();
	    var curr_month = d.getMonth() + 1; 
	    String(curr_month).length < 2 ? (curr_month = "0" + curr_month): curr_month;
	    String(curr_date).length < 2 ? (curr_date = "0" + curr_date): curr_date;
	    var yyyyMMddHHmm = '' + d.getFullYear() + curr_month + curr_date + d.getHours() + d.getMinutes() + d.getSeconds() + d.getMilliseconds();
	    return yyyyMMddHHmm + parseInt((Math.random() * 1000000));
	},

	/**
	 * 获取交易号
	 */
	getTradeNo : function() {
		var tradeNo = '';
		var now = new Date();
		tradeNo += now.getFullYear();
		if(now.getMonth() + 1 < 10) {
			tradeNo += '0' + (now.getMonth() + 1);
		} else {
			tradeNo += (now.getMonth() + 1);
		}
		tradeNo += now.getDate().toString() + now.getHours().toString() + now.getMinutes().toString() + now.getSeconds().toString() + parseInt(Math.random() * 100000);
		return tradeNo;
	}
}