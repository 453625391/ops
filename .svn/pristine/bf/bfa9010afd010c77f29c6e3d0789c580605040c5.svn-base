var express = require('express');
var router = express.Router();
var url = require('url');
var request = require('request');
var ipware = require('ipware')();
var md5 = require('md5');

var logger = require('../lib/log4js').getLogger('base-router');
var appConfig = require('../app-config');
var errHandler = require('../lib/err-handler');
var bizUtils = require('../lib/biz-utils');

/*
 * APP登录
 * @param app_id app接入时分配的id
 * @param private_key app接入时分配的密钥
 */
router.all('/app/login', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.app_id == 'undefined' || typeof requestParams.private_key == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}
		// 记录请求IP
		requestParams.request_info = ipware.get_ip(req).clientIp;

		// 根据app_id、private_key和请求IP获取appToken和security_code
		var formData = {
			service : 'infrastructure.appTokenAuth',
			params : JSON.stringify(requestParams)
		};
		request.post(appConfig.infrastructurePath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					res.send(JSON.parse(body));
				} else {
					errHandler.systemError(res, error);
					return;
				}
			} catch(e) {
				errHandler.systemError(res, e);
				return;
			}
		});
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

/*
 * 根据手机号校验是否为店东
 * @param app_token app令牌
 * @param mobile	手机号
 * @param sign 签名
 */
router.all('/user/valid_store_by_mobile', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.mobile == 'undefined'
			|| typeof requestParams.sign == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}

		// 验证app_token
		bizUtils.validAppToken(req, res, requestParams.app_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}

			// 根据手机号查询是否为店东
			var memberFormData = {
				service : 'infrastructure.memberTypeValidateByMobile',
				params : JSON.stringify({
					mobile : requestParams.mobile,
					member_type_key : 'stores'
				})
			};
			request.post(appConfig.userPath, {form: memberFormData}, function(error, response, body) {
				if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
					res.send(JSON.parse(body));
				} else {
					errHandler.systemError(res, error);
					return;
				}
			});
		});
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

/*
 * 根据手机号校验是否为加盟店
 * @param app_token app令牌
 * @param mobile	手机号
 * @param sign 签名
 */
router.all('/user/valid_franchisee_by_mobile', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.mobile == 'undefined'
			|| typeof requestParams.sign == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}

		// 验证app_token
		bizUtils.validAppToken(req, res, requestParams.app_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}

			// 根据手机号查询是否为加盟店
			var memberFormData = {
				service : 'member.memberInfoFindByMobile',
				params : JSON.stringify({
					mobile : requestParams.mobile,
					member_type_key : 'stores'
				})
			};
			request.post(appConfig.memberPath, {form: memberFormData}, function(error, response, body) {
				var result = {
					rsp_code : 'succ',
					data : {
						is_franchisee : false
					}
				};
				if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
					var bodyJson = JSON.parse(body);
					if(bodyJson.rsp_code == 'fail') {	// 查询出错，直接返回报错信息
						result = bodyJson;
					} else {
						if(bodyJson.data.stores_type_key && bodyJson.data.stores_type_key == 'HYLX_03') {	// 是加盟店
							result.data.is_franchisee = true;
						}
					}
					res.send(result);
					return;
				} else {
					errHandler.systemError(res, error);
					return;
				}
			});
		});
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

/*
 * 查询用户信息
 * @param user_token 用户令牌
 # @param sign 签名
 */
router.all('/user/info', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.user_token == 'undefined' || typeof requestParams.sign == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}

		// 验证user_token
		bizUtils.validUserToken(req, res, requestParams.user_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}

			// 根据返回的user_id查询用户信息
			var userInfoFormData = 'service=infrastructure.userFind&params=' + JSON.stringify({user_id: tokenValidData.data.user_id});
			request.post(appConfig.userPath, {form: userInfoFormData}, function(error, response, body) {
				if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
					res.send(JSON.parse(body));
				} else {
					errHandler.systemError(res, error);
					return;
				}
			});
		});
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

/*
 * 发送短信
 * @param app_token app令牌
 * @param mobiles 接收短信的手机号码集，多个号码用英文逗号隔开
 * @param msg 短信内容
 * @param sign 签名
 */
router.all('/notify/sms', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.mobiles == 'undefined'
			|| typeof requestParams.msg == 'undefined' || typeof requestParams.sign == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}

		// 验证app_token
		bizUtils.validAppToken(req, res, requestParams.app_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}

			// 发送短信
			var msgFormData = 'service=notification.SMSSend&params=' + JSON.stringify({sms_source: tokenValidData.data.app_id, mobiles: requestParams.mobiles, msg: requestParams.msg});
			request.post(appConfig.notificationPath, {form: msgFormData}, function(error, response, body) {
				if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
					res.send(JSON.parse(body));
				} else {
					errHandler.systemError(res, error);
					return;
				}
			});
		});
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

/*
 * 发送短信验证码
 * @param app_token app令牌
 * @param mobile 接收验证码的手机号
 * @param sign 签名
 */
router.all('/notify/send_sms_code', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.mobile == 'undefined' || typeof requestParams.sign == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}

		// 验证app_token
		bizUtils.validAppToken(req, res, requestParams.app_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}

			// 发送短信验证码
			var sendSMSCodeFormData = 'service=notification.sendCheckCode&params=' + JSON.stringify({sms_source: tokenValidData.data.app_id, mobile: requestParams.mobile});
			request.post(appConfig.notificationPath, {form: sendSMSCodeFormData}, function(error, response, body) {
				if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
					res.send(JSON.parse(body));
				} else {
					errHandler.systemError(res, error);
					return;
				}
			});
		});
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

/*
 * 校验短信验证码
 * @param app_token app令牌
 * @param mobile 接收到验证码的手机号
 * @param sms_code 短信验证码
 * @param sign 签名
 */
router.all('/notify/valid_sms_code', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.mobile == 'undefined' 
			|| typeof requestParams.sms_code == 'undefined' || typeof requestParams.sign == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}

		// 验证app_token
		bizUtils.validAppToken(req, res, requestParams.app_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}

			// 校验短信验证码
			var validSMSCodeFormData = 'service=notification.validateCheckCode&params=' + JSON.stringify({mobile: requestParams.mobile, check_code: requestParams.sms_code});
			request.post(appConfig.notificationPath, {form: validSMSCodeFormData}, function(error, response, body) {
				if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
					res.send(JSON.parse(body));
				} else {
					errHandler.systemError(res, error);
					return;
				}
			});
		});
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

/*
 * 手机号登录授权验证
 * @param app_token app令牌
 * @param code 		验证码
 * @param sign 		签名
 */
router.all('/base/mobile_login_verify', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.code == 'undefined' || typeof requestParams.sign == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}

		// 验证app_token
		bizUtils.validAppToken(req, res, requestParams.app_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}

			// 校验短信验证码
			var mobileLoginVerifyFormData = {
				service : 'infrastructure.mobileLoginVerify',
				params : JSON.stringify({
					code: requestParams.code
				})
			}
			request.post(appConfig.infrastructurePath, {form: mobileLoginVerifyFormData}, function(error, response, body) {
				if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
					res.send(JSON.parse(body));
				} else {
					errHandler.systemError(res, error);
					return;
				}
			});
		});
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

module.exports = router;