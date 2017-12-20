var express = require('express');
var router = express.Router();
var url = require('url');
var request = require('request');
var ipware = require('ipware')();

var logger = require('../lib/log4js').getLogger('oauth-router');
var appConfig = require('../app-config');
var errHandler = require('../lib/err-handler');
var bizUtils = require('../lib/biz-utils');
var redis = require('redis');
var redisClient = redis.createClient(appConfig.redisPort, appConfig.redisIP);

var _APP_INFO_ = 'appinfo_';
var _REQUEST_PARAMS_ = 'requestparams_';

/*
 * 响应重定向至登录页面
 * @param app_id		APP授权id
 * @param response_type	表示授权类型，此处的值固定为"code"
 * @param redirect_uri	表示重定向URI
 * @param state			表示客户端的当前状态，可以指定任意值，认证服务器会原封不动地返回这个值
 */
router.get('/show', function(req, res, next) {
	try {

		logger.info('请求show开始');

		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		logger.info('请求参数：' + requestParams);

		// 判断参数的完整性
		if(typeof requestParams.app_id == 'undefined' || typeof requestParams.response_type == 'undefined' 
			|| typeof requestParams.redirect_uri == 'undefined' || typeof requestParams.state == 'undefined') {

			logger.info('参数完整性判断未通过');

			// 渲染参数缺失页面
			res.render('error', {
				'errMsg' : '参数缺失'
			});
			return;
		}

		// 判断response_type传值
		if(requestParams.response_type != 'code') {

			logger.info('判断response_type传值未通过');

			res.render('error', {
				'errMsg' : 'response_type参数传值有误'
			});
			return;
		}

		// 判断redirect_uri格式的合法性
		/*if(!/^https?:\/\//.test(requestParams.redirect_uri)) {
			res.render('error', {
				'errMsg' : '非法的redirect_uri'
			});
			return;
		}*/

		// 判断app_id是否是已注册的应用
		var appInfoFormData = 'service=infrastructure.findAppInfo&params=' + JSON.stringify({app_id: requestParams.app_id});
		request.post(appConfig.infrastructurePath, {form: appInfoFormData}, function(error, response, body) {
			if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
				var appInfo = JSON.parse(body);
				if(appInfo.rsp_code != 'succ') {	// 应用不存在

					logger.info('该应用尚未开通每天惠账号登录,app_id:' + requestParams.app_id);

					res.render('error', {
						'errMsg' : '该应用尚未开通每天惠账号登录'
					});
					return;
				}
				// 判断redirect_uri是否属于注册信息中的域名（先简单的判断是否包含顶级域名）
				var redirectUriFromRequest = decodeURIComponent(requestParams.redirect_uri);
				if(redirectUriFromRequest.indexOf(appInfo.data.callback_url) == '-1') {

					logger.info('非法的redirect_uri');

					res.render('error', {
						'errMsg' : '非法的redirect_uri'
					});
					return;
				}
				// 将应用信息存入redis
				redisClient.set(_APP_INFO_ + requestParams.app_id, JSON.stringify(appInfo.data));

				// 将第三方请求数据存入redis
				redisClient.set(_REQUEST_PARAMS_ + requestParams.app_id, JSON.stringify(requestParams));

				// 渲染登录授权页面
				res.render('login', {
					'app_id' : requestParams.app_id
				});
			} else {

				logger.error('查询APP信息系统异常：' + error);

				res.render('error', {
					'errMsg' : '系统异常'
				});
				return;
			}
		});
	} catch(e) {

		logger.error('渲染至登录授权页面系统异常：' + error);

		res.render('error', {
			'errMsg' : '系统异常'
		});
		return;
	}
});

/*
 * 用户登录
 * @param app_id		APP授权id
 * @param user_account	用户账号
 * @param password		用户密码
 */
router.post('/login', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证app_id是否缺失
		if(typeof requestParams.app_id == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}

		// 验证用户名密码是否缺失
		if(typeof requestParams.user_account == 'undefined' || typeof requestParams.password == 'undefined') {
			res.send({
				rsp_code : 'fail',
				error_code : 'incomplete params',
				error_msg : '用户名或密码缺失'
			});
		}
		requestParams.member_type_key = 'consumer';
		requestParams.request_info = ipware.get_ip(req).clientIp;

		// 用户登录
		var loginFormData = {
			service : 'infrastructure.consumerLoginForOAuth',
			params : JSON.stringify(requestParams)
		}
		request.post(appConfig.userPath, {form: loginFormData}, function(error, response, body) {
			logger.info('【用户登录结果】' + body);
			try {
				if(!error && response.statusCode == 200) {
					var loginResult = JSON.parse(body);
					if(loginResult.rsp_code == 'succ') {	// 登录成功
						var code = bizUtils.getRandomCode();	// 生成随机code，与user_token一起存入redis
						// 从redis中获取APP信息
						redisClient.get(_APP_INFO_ + requestParams.app_id, function(err1, reply1) {
							if(err1 != null) {
								errHandler.systemError(res);
								return;
							}
							var appInfo = JSON.parse(reply1);
							// 从redis中获取第三方请求信息
							redisClient.get(_REQUEST_PARAMS_ + requestParams.app_id, function(err2, reply2) {
								if(err2 != null) {
									errHandler.systemError(res);
									return;
								}
								var cp = JSON.parse(reply2);
								var codeJson = {
									appInfo : appInfo,
									userInfo : loginResult.data,
									redirect_uri : cp.redirect_uri
								};
								// 将code与对应的值存入redis
								redisClient.set(code, JSON.stringify(codeJson), function(err, r) {
									if(err != null) {
										errHandler.systemError(res);
										return;
									}
									redisClient.expire(code, 600);	// code有效期设置为10分钟
									res.send({
										rsp_code : 'succ',
										data : {
											code : code
										}
									});
									return;
								});
							});
						});
					} else {
						res.send(loginResult);
						return;
					}
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
 * 重定向至第三方
 * @param app_id		APP授权id
 * @param code			code
 */
router.get('/redirect', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		if(typeof requestParams.app_id == 'undefined' || typeof requestParams.code == 'undefined') {
			res.render('error', {
				'errMsg' : '参数缺失'
			});
			return;
		}

		// 从redis中获取第三方请求参数
		redisClient.get(_REQUEST_PARAMS_ + requestParams.app_id, function(err, reply) {
			if(err != null) {
				res.render('error', {
					'errMsg' : '参数异常'
				});
				return;
			}
			var rp = JSON.parse(reply);
			var redirect_uri = decodeURIComponent(rp.redirect_uri);
			if(redirect_uri.indexOf('?') == -1) {
				redirect_uri += '?code=' + requestParams.code + '&state=' + rp.state;
			} else {
				redirect_uri += '&code=' + requestParams.code + '&state=' + rp.state;
			}
			res.redirect(redirect_uri);
		});
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

/*
 * 获取user_token
 * @param grant_type	授权类型，此处固定为"authorization_code"
 * @param app_id		APP授权id
 * @param private_key	APP密钥
 * @param code			重定向中生成的随机码
 * @param redirect_uri	重定向地址，应与传递到授权页面的一致
 */
router.all('/token', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 校验参数是否缺失
		if(typeof requestParams.grant_type == 'undefined' || typeof requestParams.app_id == 'undefined' 
			|| typeof requestParams.private_key == 'undefined' || typeof requestParams.code == 'undefined' || typeof requestParams.redirect_uri == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}

		// 校验grant_type传值
		if(requestParams.grant_type != 'authorization_code') {
			res.send({
				rsp_code : 'fail',
				error_code : 'invalid param value',
				error_msg : 'grant_type参数传值有误'
			});
			return;
		}

		// 根据code从redis中获取对象
		redisClient.get(requestParams.code, function(err, reply) {
			if(err != null) {
				res.send({
					rsp_code : 'fail',
					error_code : 'invalid code',
					error_msg : '无效的code'
				});
				return;
			}

			var codeJson = JSON.parse(reply);

			// 删除redis中的数据，保证只能请求一次
			// redisClient.expire(requestParams.code, 0);

			// 校验app_id、private_key和redirect_uri
			if(codeJson.appInfo.app_id != requestParams.app_id 
				|| codeJson.appInfo.private_key != requestParams.private_key 
				|| decodeURIComponent(codeJson.redirect_uri) != decodeURIComponent(requestParams.redirect_uri)) {
				res.send({
					rsp_code : 'fail',
					error_code : 'invalid request',
					error_msg : '非法请求'
				});
				return;
			}

			var consumerFormData = {
				service : 'infrastructure.consumerInfoFindForOAuth',
				params : JSON.stringify({
					user_id : codeJson.userInfo.user_id
				})
			};

			// 查询消费者基本信息
			request.post(appConfig.userPath, {form: consumerFormData}, function(error, response, body) {
				logger.info('【查询消费者基本信息】' + body);
				try {
					if(!error && response.statusCode == 200) {
						var consumerInfo = JSON.parse(body);
						if(consumerInfo.rsp_code != 'succ') {
							errHandler.systemError(res, error);
							return;	
						} else {
							// 将user_id加入用户信息中，用来唯一标示用户
							consumerInfo.data.user_id = codeJson.userInfo.user_id;
							// 返回user_token、security_code和消费者基本信息
							res.send({
								rsp_code : 'succ',
								data : {
									user_token : codeJson.userInfo.user_token,
									security_code : codeJson.userInfo.security_code,
									user_info : consumerInfo.data
								}
							});
							return;
						}
					} else {
						errHandler.systemError(res, error);
						return;
					}
				} catch(e) {
					errHandler.systemError(res, e);
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