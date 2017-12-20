var express = require('express');
var router = express.Router();
var url = require('url');
var request = require('request');
var ipware = require('ipware')();
var md5 = require('md5');
var fs = require('fs');
var uuidv1 = require('uuid/v1');
var logger = require('../lib/log4js').getLogger('base-router');
var appConfig = require('../app-config');
var errHandler = require('../lib/err-handler');
var bizUtils = require('../lib/biz-utils');

router.all('/infrastructure',function(req,res,next){
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		// 验证请求参数是否完整
		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.service == 'undefined'
			 || typeof requestParams.sign == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}
		// 校验app_token
		bizUtils.validAppToken(req, res, requestParams.app_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}
		// 记录请求IP
		requestParams.request_info = ipware.get_ip(req).clientIp;

		// 根据app_id、private_key和请求IP获取appToken和security_code
		var formData = {
			service : requestParams.service,
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
		});
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}

});
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
 * APP登录（会从兑换中心获取app_token，暂时）
 * @param app_id app接入时分配的id
 * @param private_key app接入时分配的密钥
 */
router.all('/app/login_ex', function(req, res, next) {
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
			service : 'infrastructure.appTokenAuthForEx',
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
 * 用户登录
 * @param app_token app令牌
 * @param user_account 账号
 * @param password 密码
 * @param member_type_key 用户身份类型（消费者/店东/供应商/联盟商）
 * @param sign 签名
 */
router.all('/user/login', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.user_account == 'undefined' || typeof requestParams.password == 'undefined'
			|| typeof requestParams.member_type_key == 'undefined' || typeof requestParams.sign == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}

		// 用户身份值必须是如下四个之一
		if(requestParams.member_type_key != 'consumer' && requestParams.member_type_key != 'stores'
			&& requestParams.member_type_key != 'supplier' && requestParams.member_type_key != 'partner') {
			errHandler.invalidParams(res);
			return;
		}

		// 校验app_token
		bizUtils.validAppToken(req, res, requestParams.app_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}

			// 客户端IP
			requestParams.request_info = ipware.get_ip(req).clientIp;

			// 用户登录验证，如果是消费者登录，则调用兑换中心的用户登录接口获取user_token
			var formData = {
				service : 'infrastructure.userLogin',
				params : JSON.stringify(requestParams)
			};
			request.post(appConfig.userPath, {form: formData}, function(error, response, body) {
				try {
					if(!error && response.statusCode == 200) {	//请求成功
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
		});
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

/*
 * 消费者登录（若消费者未注册，则自动帮其注册成会员，然后再自动登录）
 * @param app_token app令牌
 * @param mobile 账号
 * @param sms_code 短信验证码
 * @param request_info 设备唯一ID（非必填）
 * @param device_model 设备型号（非必填）
 * @param sign 签名
 */
router.all('/consumer/login', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.mobile == 'undefined'
			|| typeof requestParams.sms_code == 'undefined' || typeof requestParams.sign == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}

		// 校验app_token
		bizUtils.validAppToken(req, res, requestParams.app_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}

			if(typeof appConfig.testConsumerAccount[requestParams.mobile] != 'undefined') {	// 特定测试账号，根据配置文件中的短信验证码进行校验
				if(appConfig.testConsumerAccount[requestParams.mobile] == requestParams.sms_code) {	// 短信校验通过，消费者登录
					var consumerLoginFormData = {
						service : 'infrastructure.consumerLogin',
						params : JSON.stringify({
							mobile : requestParams.mobile,
							member_type_key : 'consumer',
							request_info : requestParams.request_info || ipware.get_ip(req).clientIp,
							device_model : requestParams.device_model || '',
							data_source : requestParams.data_source || 'meitianhui'
						})
					};
					request.post(appConfig.userPath, {form: consumerLoginFormData}, function(error, response, loginBody) {
						console.log("loginBody249:%s",loginBody);
						try {
							if(!error && response.statusCode == 200) {	//请求成功
								res.send(JSON.parse(loginBody));
							} else {
								errHandler.systemError(res, error);
								return;
							}
						} catch(e) {
							errHandler.systemError(res, e);
							return;
						}
					});
				} else {
					res.send({
						'error_code' : 'check_code_disabled',
						'error_msg' : '验证码失效或不存在',
						'rsp_code' : 'fail'
					});
				}
			} else {
				// 校验短信验证码
				var validSMSCodeFormData = {
					service : 'notification.validateCheckCode',
					params : JSON.stringify({
						mobile : requestParams.mobile,
						check_code : requestParams.sms_code
					})
				};
				request.post(appConfig.notificationPath, {form: validSMSCodeFormData}, function(error, response, validBody) {
					try {
						if(!error && response.statusCode == 200) {
							var validResult = JSON.parse(validBody);
							if(validResult.rsp_code != 'succ') {	// 短信验证码校验未通过，直接返回校验信息
								console.log("validBody282: %s",validBody);
								res.send(validResult);
								return;
							}

							// 短信校验通过，消费者登录
							var consumerLoginFormData = {
								service : 'infrastructure.consumerLogin',
								params : JSON.stringify({
									mobile : requestParams.mobile,
									member_type_key : 'consumer',
									request_info : requestParams.request_info || ipware.get_ip(req).clientIp,
									device_model : requestParams.device_model || '',
									data_source : requestParams.data_source || 'meitianhui'
								})
							};
							request.post(appConfig.userPath, {form: consumerLoginFormData}, function(error, response, loginBody) {
								logger.info('登录返回结果：');
								logger.info(loginBody);
								console.log("loginBody301:%s",loginBody);
								try {
									if(!error && response.statusCode == 200) {	//请求成功
										var loginData = JSON.parse(loginBody);
										if(loginData.rsp_code == 'succ') {	// 登录成功
											res.send(loginData);
											return;
										} else if(loginData.rsp_code == 'fail' &&
											(loginData.error_code == 'user_not_exist' || loginData.error_code == 'member_not_exist')) {	// 因为用户未注册而登录失败
											// 帮助用户自动注册账户

											var randomPwd = parseInt(Math.random() * 100000000);	// 生成随机的6~8位的纯数字密码
											var randomPwdMD5 = md5(randomPwd);
											// 先调用兑换中心注册接口
											// var exchangeRegData = {
											// 	method : 'temporary.accountCreate',
											// 	app_token : appConfig.exchangeCenterRegAppTokenConstant,
											// 	user_account : requestParams.mobile,
											// 	user_password : randomPwdMD5
											// }
											// logger.info('准备在兑换中心注册用户，参数：');
											// logger.info(exchangeRegData);
											// request.post(appConfig.exchangeCenterRegPath, {form: exchangeRegData}, function(error, response, exchangeRegBody) {
												// logger.info('兑换中心注册返回结果：');
												// logger.info('error:' + error);
												// logger.info('body:' + exchangeRegBody);
												// if(!error && response.statusCode == 200) {
												// 	var exchangeRegResult = JSON.parse(exchangeRegBody);
												//
												// 	if(exchangeRegResult.errcode != 0 && exchangeRegResult.errcode != 40007) {	// 注册失败并且不是账号已注册错误时，则直接返回
												// 		logger.error('兑换中心用户注册失败：');
												// 		logger.error(exchangeRegResult);
												// 		res.send({
												// 			rsp_code: 'fail',
												// 			error_code: 'exchange_center_reg_error',
												// 			error_msg: exchangeRegResult.errmsg
												// 		});
												// 		res.end();
												// 		return;
												// 	}
												// 	var exchangeUserId = '';
												// 	if(exchangeRegResult.errcode == 40007) {	// 兑换中心账号已存在
												// 		logger.info('兑换中心账号已存在，继续再开放平台注册');
												// 		exchangeUserId += exchangeRegResult.user_id;
												// 	} else {
												// 		exchangeUserId += exchangeRegResult.data.user_id;
												// 	}
												//
												// 	// 兑换中心注册成功，再在开放平台注册

												var exchangeUserId = uuidv1().replace(/-/g,'');
													var openRegData = {
														service : 'infrastructure.consumerUserRegister',
														params : JSON.stringify({
															user_id : exchangeUserId,
															mobile : requestParams.mobile,
															password : randomPwdMD5
														})
													};
													logger.info('准备在开放平台注册用户，参数：');
													logger.info(openRegData);
													request.post(appConfig.userPath, {form: openRegData}, function(error, response, openRegbody) {
														logger.info('开放平台注册返回结果：');
														logger.info('error:' + error);
														logger.info('body:' + openRegbody);
														if(!error && response.statusCode == 200) {
															var openRegResult = JSON.parse(openRegbody);
															if(openRegResult.rsp_code == 'fail') {
																logger.info('用户在开放平台注册失败！');
																res.send(openRegResult);
																res.end();
																return;
															} else {
																logger.info('用户注册成功！');
												//
																// 新用户注册送50金币，调用余额支付接口，不影响业务主流程
																var giveBizParam = {
																	data_source: 'SJLY_01',
																	detail: '注册赠送',
																	amount: '200',
																	out_trade_no: bizUtils.generateTradeNo(),
																	buyer_id: '10000001',	// 每天惠账户
																	seller_id: exchangeUserId,
																	payment_way_key: 'ZFFS_08'
																};
																giveBizParam.out_trade_body = JSON.stringify(giveBizParam);
																var giveGoldFormData = {
																	service: 'finance.balancePay',
																	params: JSON.stringify(giveBizParam)
																};
																request.post(appConfig.financePath, {form: giveGoldFormData}, function(error, response, giveGoldBody) {
																	try {
																		logger.info('消费者新用户注册送50金币结果：');
																		logger.info(giveGoldBody);
																		logger.info('注册手机号：' + requestParams.mobile);
																	} catch(e) {
																		logger.info('消费者新用户注册送50金币调用接口失败：');
																		logger.error(e);
																	}
																});

																logger.info('准备自动登录，登录参数：');
																logger.info(JSON.stringify(consumerLoginFormData));
																request.post(appConfig.userPath, {form: consumerLoginFormData}, function(error, response, autoLoginBody) {
																	logger.info('自动登录结果：');
																	logger.info(autoLoginBody);
																	try {
																		if(!error && response.statusCode == 200) {	//请求成功
																			res.send(JSON.parse(autoLoginBody));
																			return;
																		} else {
																			res.send({
																				rsp_code: 'fail',
																				error_code: 'login_error',
																				error_msg: '登录失败'
																			});
																			res.end();
																			return;
																		}
																	} catch(e) {
																		errHandler.systemError(res, error);
																		return;
																	}
																});//
															}
														} else {
															errHandler.systemError(res, error);
															return;
														}
													});
												// } else {
												// 	errHandler.systemError(res, error);
												// 	return;
												// }
											// });
										} else {	// 其他原因登录失败
											res.send(loginData);
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
						} else {
							errHandler.systemError(res, error);
							return;
						}
					} catch(e) {
						errHandler.systemError(res, e);
						return;
					}
				});
			}
		});
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

/*
 * 校验用户凭证
 * @param user_token app令牌
 */
router.all('/user/valid_user_token', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(!requestParams.user_token) {
			errHandler.incompleteParams(res);
			return;
		}

		// 校验user_token
		bizUtils.validUserToken(req, res, requestParams.user_token, function(tokenValidData) {
			// 校验通过，直接返回校验数据
			res.send(tokenValidData);
		});
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

/*
 * 加盟店登录
 * @param app_token app令牌
 * @param mobile 账号
 * @param sms_code 短信验证码
 * @param request_info 设备唯一ID（非必填）
 * @param device_model 设备型号（非必填）
 * @param sign 签名
 */
router.all('/store/login', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.mobile == 'undefined'
			|| typeof requestParams.sms_code == 'undefined' || typeof requestParams.sign == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}

		// 校验app_token
		bizUtils.validAppToken(req, res, requestParams.app_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}

			if(typeof appConfig.testStoreAccount[requestParams.mobile] != 'undefined') {	// 特定测试账号，根据配置文件中的短信验证码进行校验
				if(appConfig.testStoreAccount[requestParams.mobile] == requestParams.sms_code) {	// 短信校验通过，加盟店登录
					var storeLoginFormData = {
						service : 'infrastructure.storeLogin',
						params : JSON.stringify({
							user_account: requestParams.mobile,
							request_info: requestParams.request_info || ipware.get_ip(req).clientIp,
							device_model: requestParams.device_model || '',
							data_source: requestParams.data_source || 'meitianhui'
						})
					};
					request.post(appConfig.userPath, {form: storeLoginFormData}, function(error, response, loginBody) {
						try {
							if(!error && response.statusCode == 200) {	//请求成功
								res.send(JSON.parse(loginBody));
							} else {
								errHandler.systemError(res, error);
								return;
							}
						} catch(e) {
							errHandler.systemError(res, e);
							return;
						}
					});
				} else {
					res.send({
						'error_code' : 'check_code_disabled',
						'error_msg' : '验证码失效或不存在',
						'rsp_code' : 'fail'
					});
				}
			} else {
				// 校验短信验证码
				var validSMSCodeFormData = {
					service : 'notification.validateCheckCode',
					params : JSON.stringify({
						mobile : requestParams.mobile,
						check_code : requestParams.sms_code
					})
				};
				request.post(appConfig.notificationPath, {form: validSMSCodeFormData}, function(error, response, validBody) {
					try {
						if(!error && response.statusCode == 200) {
							var validResult = JSON.parse(validBody);
							if(validResult.rsp_code != 'succ') {	// 短信验证码校验未通过，直接返回校验信息
								res.send(validResult);
								return;
							}

							// 短信校验通过，加盟店登录
							var storeLoginFormData = {
								service : 'infrastructure.storeLogin',
								params : JSON.stringify({
									user_account: requestParams.mobile,
									request_info: requestParams.request_info || ipware.get_ip(req).clientIp,
									device_model: requestParams.device_model || '',
									data_source: requestParams.data_source || 'meitianhui'
								})
							};
							request.post(appConfig.userPath, {form: storeLoginFormData}, function(error, response, loginBody) {
								try {
									if(!error && response.statusCode == 200) {	//请求成功
										res.send(JSON.parse(loginBody));
									} else {
										errHandler.systemError(res, error);
										return;
									}
								} catch(e) {
									errHandler.systemError(res, e);
									return;
								}
							});
						} else {
							errHandler.systemError(res, error);
							return;
						}
					} catch(e) {
						errHandler.systemError(res, e);
						return;
					}
				});
			}
		});
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

/*
 * 用户登录后更新日志
 * @param user_token 用户令牌
 * @param member_type_key 用户类型（consumer / stores / supplier / partner）
 * @param device_type 设备类别（iOS / Android）
 * @param device_model 设备型号
 * @param alias 别名
 * @param sign 签名
 */
router.all('/user/login_log_update', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(!requestParams.user_token || !requestParams.member_type_key || !requestParams.device_type
			 || !requestParams.device_model || !requestParams.alias || !requestParams.sign) {
			errHandler.incompleteParams(res);
			return;
		}

		// 校验user_token
		bizUtils.validUserToken(req, res, requestParams.user_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}

			// 更新登录日志
			var loginLogUpdateFormData = {
				service: 'infrastructure.memberLoginLogUpdate',
				params: JSON.stringify({
					member_id: tokenValidData.data.member_id,
					member_type_key: requestParams.member_type_key,
					device_type: requestParams.device_type,
					device_model: requestParams.device_model,
					alias: requestParams.alias
				})
			};
			request.post(appConfig.userPath, {form: loginLogUpdateFormData}, function(error, response, body) {
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
 * 用户登出
 * @param user_token app令牌
 * @param sign 签名
 */
router.all('/user/logout', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.user_token == 'undefined' || typeof requestParams.sign == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}

		// 验证用户的会员类型
		var logoutFormData = {
			service: 'infrastructure.userLogout',
			params: JSON.stringify({
				user_token: requestParams.user_token,
				request_info: ipware.get_ip(req).clientIp
			})
		};
		request.post(appConfig.userPath, {form: logoutFormData}, function(error, response, body) {
			if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
				res.send(JSON.parse(body));
			} else {
				errHandler.systemError(res, error);
				return;
			}
		});
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

/*
 * 验证用户的会员类型
 * @param app_token app令牌
 * @param user_account 用户账号
 * @param member_type_key 用户类型（consumer / stores / supplier / partner）
 * @param sign 签名
 */
router.all('/user/valid_type', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.user_account == 'undefined'
			|| typeof requestParams.member_type_key == 'undefined' || typeof requestParams.sign == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}

		// 校验app_token
		bizUtils.validAppToken(req, res, requestParams.app_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}

			// 验证用户的会员类型
			var validTypeFormData = {
				service: 'infrastructure.memberTypeValidate',
				params: JSON.stringify({
					user_account: requestParams.user_account,
					member_type_key: requestParams.member_type_key
				})
			};
			request.post(appConfig.userPath, {form: validTypeFormData}, function(error, response, body) {
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
 * 根据手机号验证用户的会员类型
 * @param app_token app令牌
 * @param mobile 手机号
 * @param member_type_key 用户类型（consumer / stores / supplier / partner）
 * @param sign 签名
 */
router.all('/user/valid_type_by_mobile', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.mobile == 'undefined'
			|| typeof requestParams.member_type_key == 'undefined' || typeof requestParams.sign == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}

		// 校验app_token
		bizUtils.validAppToken(req, res, requestParams.app_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}

			// 验证用户的会员类型
			var validTypeFormData = {
				service: 'infrastructure.memberTypeValidateByMobile',
				params: JSON.stringify({
					mobile: requestParams.mobile,
					member_type_key: requestParams.member_type_key
				})
			};
			request.post(appConfig.userPath, {form: validTypeFormData}, function(error, response, body) {
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
 * 消费者注册
 * @param app_token app令牌
 * @param mobile 手机号
 * @param sms_code 短信验证码
 * @param password  密码（MD5）
 * @param sign 签名
 */
router.all('/consumer/reg', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.mobile == 'undefined' || typeof requestParams.sms_code == 'undefined'
			|| typeof requestParams.password == 'undefined' || typeof requestParams.sign == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}

		// 校验app_token
		bizUtils.validAppToken(req, res, requestParams.app_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}

			// 校验短信验证码
			var validSMSCodeFormData = {
				service : 'notification.validateCheckCode',
				params : JSON.stringify({
					mobile : requestParams.mobile,
					check_code : requestParams.sms_code
				})
			};
			request.post(appConfig.notificationPath, {form: validSMSCodeFormData}, function(error, response, validBody) {
				try {
					if(!error && response.statusCode == 200) {
						var validResult = JSON.parse(validBody);
						if(validResult.rsp_code != 'succ') {	// 短信验证码校验未通过，直接返回校验信息
							res.send(validResult);
							return;
						}

						// 消费者注册
						// 先调用兑换中心注册接口
						var exchangeRegData = {
							method : 'temporary.accountCreate',
							app_token : appConfig.exchangeCenterRegAppTokenConstant,
							user_account : requestParams.mobile,
							user_password : requestParams.password
						}
						logger.info('准备在兑换中心注册用户，参数：');
						logger.info(exchangeRegData);
						request.post(appConfig.exchangeCenterRegPath, {form: exchangeRegData}, function(error, response, exchangeRegBody) {
							logger.info('兑换中心注册返回结果：');
							logger.info('error:' + error);
							logger.info('body:' + exchangeRegBody);
							if(!error && response.statusCode == 200) {
								var exchangeRegResult = JSON.parse(exchangeRegBody);
								if(exchangeRegResult.errcode != 0) {	// 注册失败
									logger.info('兑换中心用户注册失败：');
									logger.info(exchangeRegResult);
									res.send({
										rsp_code: 'fail',
										error_code: 'system error',
										error_msg: exchangeRegResult.errmsg || '注册失败'
									});
									res.end();
									return;
								}

								// 兑换中心注册成功，再在开放平台注册
								var formData = {
									service : 'infrastructure.consumerUserRegister',
									params : JSON.stringify({
										user_id : exchangeRegResult.data.user_id + '',
										mobile : requestParams.mobile,
										password : requestParams.password
									})
								};
								logger.info('准备在开放平台注册用户，参数：');
								logger.info(formData);
								request.post(appConfig.userPath, {form: formData}, function(error, response, body) {
									logger.info('开放平台注册返回结果：');
									logger.info('error:' + error);
									logger.info('body:' + body);
									if(!error && response.statusCode == 200) {
										var registerResult = JSON.parse(body);
										if(registerResult.rsp_code == 'succ') {
											// 新用户注册送50金币，调用余额支付接口，不影响业务主流程
											var giveBizParam = {
												data_source: 'SJLY_01',
												detail: '注册赠送',
												amount: '50',
												out_trade_no: bizUtils.generateTradeNo(),
												buyer_id: '10000001',	// 每天惠账户
												seller_id: exchangeRegResult.data.user_id,
												payment_way_key: 'ZFFS_08'
											};
											giveBizParam.out_trade_body = JSON.stringify(giveBizParam);
											var giveGoldFormData = {
												service: 'finance.balancePay',
												params: JSON.stringify(giveBizParam)
											};
											request.post(appConfig.financePath, {form: giveGoldFormData}, function(error, response, giveGoldBody) {
												try {
													logger.info('消费者新用户注册送50金币结果：');
													logger.info(giveGoldBody);
													logger.info('注册手机号：' + requestParams.mobile);
												} catch(e) {
													logger.info('消费者新用户注册送50金币调用接口失败：');
													logger.error(e);
												}
											});
										}
										res.send(registerResult);
									} else {
										errHandler.systemError(res, error);
										return;
									}
								});
							} else {
								errHandler.systemError(res, error);
								return;
							}
						});
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

/*
 * 店东端注册消费者账号
 * @param user_token 用户令牌
 * @param mobile 手机号
 * @param sms_code 短信验证码
 * @param sign 签名
 */
router.all('/consumer/reg_by_store', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.user_token == 'undefined' || typeof requestParams.mobile == 'undefined'
			|| typeof requestParams.sms_code == 'undefined' || typeof requestParams.sign == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}

		// 校验user_token
		bizUtils.validUserToken(req, res, requestParams.user_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}

			// 校验短信验证码
			var validSMSCodeFormData = {
				service : 'notification.validateCheckCode',
				params : JSON.stringify({
					mobile : requestParams.mobile,
					check_code : requestParams.sms_code
				})
			};
			request.post(appConfig.notificationPath, {form: validSMSCodeFormData}, function(error, response, validBody) {
				try {
					if(!error && response.statusCode == 200) {
						var validResult = JSON.parse(validBody);
						if(validResult.rsp_code != 'succ') {	// 短信验证码校验未通过，直接返回校验信息
							res.send(validResult);
							return;
						}
						// 注册消费者
						var randomPwd = parseInt(Math.random() * 100000000);	// 生成随机的6~8位的纯数字密码
						var randomPwdMD5 = md5(randomPwd);
						//
						// // 先调用兑换中心注册接口
						// var exchangeRegData = {
						// 	method : 'temporary.accountCreate',
						// 	app_token : appConfig.exchangeCenterRegAppTokenConstant,
						// 	user_account : requestParams.mobile,
						// 	user_password : randomPwdMD5
						// }
						// logger.info('准备在兑换中心注册用户，参数：');
						// logger.info(exchangeRegData);
						// request.post(appConfig.exchangeCenterRegPath, {form: exchangeRegData}, function(error, response, exchangeRegBody) {
						// 	logger.info('兑换中心注册返回结果：');
						// 	logger.info('error:' + error);
						// 	logger.info('body:' + exchangeRegBody);
						// 	if(!error && response.statusCode == 200) {
						// 		var exchangeRegResult = JSON.parse(exchangeRegBody);
						// 		if(exchangeRegResult.errcode != 0 && exchangeRegResult.errcode != 40007) {	// 注册失败并且不是账号已注册错误时，则直接返回
						// 			logger.error('兑换中心用户注册失败：');
						// 			logger.error(exchangeRegResult);
						// 			res.send({
						// 				rsp_code: 'fail',
						// 				error_code: 'system error',
						// 				error_msg: exchangeRegResult.errmsg || '注册失败'
						// 			});
						// 			res.end();
						// 			return;
						// 		}
						//
						// 		var exchangeUserId = '';
						// 		if(exchangeRegResult.errcode == 40007) {	// 兑换中心账号已存在
						// 			logger.info('兑换中心账号已存在，继续再开放平台注册');
						// 			exchangeUserId += exchangeRegResult.user_id;
						// 		} else {
						// 			exchangeUserId += exchangeRegResult.data.user_id;
						// 		}
								var exchangeUserId = uuidv1().replace(/-/g,'');
								// 兑换中心注册成功，再在开放平台注册
								var formData = {
									service : 'infrastructure.consumerUserRegister',
									params : JSON.stringify({
										user_id : exchangeUserId,
										mobile : requestParams.mobile,
										password : randomPwdMD5,
										reference_id : tokenValidData.data.member_id,
										reference_type_key : 'stores'
									})
								};
								logger.info('准备在开放平台注册用户，参数：');
								logger.info(formData);
								request.post(appConfig.userPath, {form: formData}, function(error, response, body) {
									logger.info('开放平台注册返回结果：');
									logger.info('error:' + error);
									logger.info('body:' + body);
									if(!error && response.statusCode == 200) {
										var registerResult = JSON.parse(body);
										if(registerResult.rsp_code == 'succ') {	// 注册成功
											logger.info('用户注册成功！');
											// 发送通知短信
											var smsNotifyFormData = {
												service : 'notification.SMSSend',
												params : JSON.stringify({
													sms_source : 'SJLY_O3',
													mobiles : requestParams.mobile,
													msg : '恭喜您已成功注册为每天惠会员！请您尽快到每天惠官网 http://www.meitianhui.com/ 通过忘记密码功能设置您的账户密码。'
												})
											};
											request.post(appConfig.notificationPath, {form: smsNotifyFormData}, function(error1, response, body) {
												if(error1 || response.statusCode != 200) {	// 短信发送失败
													logger.error('通知短信发送失败！' + '手机号：' + requestParams.mobile + '，初始密码：' + randomPwd);
												}
											});
											// 新用户注册送50金币，调用余额支付接口，不影响业务主流程
											var giveBizParam = {
												data_source: 'SJLY_02',
												detail: '注册赠送',
												amount: '50',
												out_trade_no: bizUtils.generateTradeNo(),
												buyer_id: '10000001',	// 每天惠账户
												seller_id: exchangeUserId,
												payment_way_key: 'ZFFS_08'
											};
											giveBizParam.out_trade_body = JSON.stringify(giveBizParam);
											var giveGoldFormData = {
												service: 'finance.balancePay',
												params: JSON.stringify(giveBizParam)
											};
											request.post(appConfig.financePath, {form: giveGoldFormData}, function(error, response, giveGoldBody) {
												try {
													logger.info('消费者新用户注册送50金币结果：');
													logger.info(giveGoldBody);
													logger.info('注册手机号：' + requestParams.mobile);
												} catch(e) {
													logger.info('消费者新用户注册送50金币调用接口失败：');
													logger.error(e);
												}
											});
										}
										res.send(registerResult);
									} else {
										errHandler.systemError(res, error);
										return;
									}
								});
							} else {
								errHandler.systemError(res, error);
								return;
							}
					// 	});
					// }
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

/*
 * 查询APP信息
 * @param app_token app令牌
 * @param sign 签名
 */
// router.all('/app/info', function(req, res, next) {
// 	try {

// 		// 请求参数
// 		var requestParams = bizUtils.extend(req.query, req.body);

// 		// 验证请求参数是否完整
// 		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.sign == 'undefined') {
// 			errHandler.incompleteParams(res);
// 			return;
// 		}

// 		// 校验app_token
// 		bizUtils.validAppToken(req, res, requestParams.app_token, function(tokenValidData) {
// 			// 根据返回的security_code校验签名
// 			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
// 				errHandler.invalidSign(res);
// 				return;
// 			}

// 			// 根据返回的app_id查询app信息
// 			var appInfoFormData = 'service=infrastructure.findAppInfo&params=' + JSON.stringify({app_id: tokenValidData.data.app_id});
// 			request.post(appConfig.infrastructurePath, {form: appInfoFormData}, function(error, response, body) {
// 				if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
// 					res.send(JSON.parse(body));
// 				} else {
// 					errHandler.systemError(res, error);
// 					return;
// 				}
// 			});
// 		});
// 	} catch(e) {
// 		errHandler.systemError(res, e);
// 		return;
// 	}
// });

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
 * 根据手机号查询用户id
 * @param app_token app令牌
 * @param mobile 手机号
 * @param sign 签名
 */
router.all('/user/get_user_id_by_mobile', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.mobile == 'undefined' || typeof requestParams.sign == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}

		// 校验app_token
		bizUtils.validAppToken(req, res, requestParams.app_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}

			// 根据手机号查询用户id
			var getUserIDFormData = {
				service: 'infrastructure.userFindByMobile',
				params: JSON.stringify({
					mobile: requestParams.mobile
				})
			};
			request.post(appConfig.infrastructurePath, {form: getUserIDFormData}, function(error, response, body) {
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
 * 根据手机号查询会员id
 * @param user_token 用户令牌
 * @param mobile 用户手机号
 * @param member_type_key 账户类型(consumer, stores, supplier, partner)
 * @param sign 签名
 */
router.all('/user/get_member_id_by_mobile', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.user_token == 'undefined' || typeof requestParams.mobile == 'undefined'
			|| typeof requestParams.member_type_key == 'undefined' || typeof requestParams.sign == 'undefined') {
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
			var memberIdFormData = {
				service : 'infrastructure.memberFindByMobile',
				params : JSON.stringify({
					mobile : requestParams.mobile,
					member_type_key : requestParams.member_type_key
				})
			};
			request.post(appConfig.userPath, {form: memberIdFormData}, function(error, response, body) {
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
 * 根据手机号查询会员id（校验app_token，给运营管理系统用的）
 * @param app_token App令牌
 * @param mobile 用户手机号
 * @param member_type_key 账户类型(consumer, stores, supplier, partner)
 * @param sign 签名
 */
router.all('/user/get_member_id_by_mobile_for_mgt', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.mobile == 'undefined'
			|| typeof requestParams.member_type_key == 'undefined' || typeof requestParams.sign == 'undefined') {
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

			// 根据返回的user_id查询用户信息
			var memberIdFormData = {
				service : 'infrastructure.memberFindByMobile',
				params : JSON.stringify({
					mobile : requestParams.mobile,
					member_type_key : requestParams.member_type_key
				})
			};
			request.post(appConfig.userPath, {form: memberIdFormData}, function(error, response, body) {
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
 * 重置用户密码
 * @param app_token 用户令牌
 * @param password 重置后的新密码
 * @param mobile 用户绑定的手机号
 * @param sms_code 短信验证码
 * @param sign 签名
 */
router.all('/user/reset_pwd', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.password == 'undefined' || typeof requestParams.mobile == 'undefined'
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
			var validSMSCodeFormData = {
				service : 'notification.validateCheckCode',
				params : JSON.stringify({
					mobile : requestParams.mobile,
					check_code : requestParams.sms_code
				})
			};
			request.post(appConfig.notificationPath, {form: validSMSCodeFormData}, function(error, response, validBody) {
				if(!error && response.statusCode == 200) {
					var validResult = JSON.parse(validBody);
					if(validResult.rsp_code != 'succ') {	// 短信验证码校验未通过，直接返回校验信息
						res.send(validResult);
						return;
					}

					// 重置密码
					var resetPwdFormData = {
						service : 'infrastructure.userPasswordReset',
						params : JSON.stringify({
							mobile : requestParams.mobile,
							password : requestParams.password
						})
					}
					request.post(appConfig.userPath, {form: resetPwdFormData}, function(error, response, body) {
						if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
							res.send(JSON.parse(body));
						} else {
							errHandler.systemError(res, error);
							return;
						}
					});
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
 * 修改用户密码 -- 暂不提供该接口
 * @params user_token 用户令牌
 * @param old_password 旧密码
 * @param new_password 新密码
 * @param sign 签名
 */
// router.all('/user/change_pwd', function(req, res, next) {
// 	try {
//
// 		// 暂不提供该接口
// 		errHandler.systemError(res);
// 		return;

// 		// 请求参数
// 		var requestParams = bizUtils.extend(req.query, req.body);

// 		// 验证请求参数是否完整
// 		if(typeof requestParams.user_token == 'undefined' || typeof requestParams.old_password == 'undefined'
// 			|| typeof requestParams.new_password == 'undefined' || typeof requestParams.sign == 'undefined') {
// 			errHandler.incompleteParams(res);
// 			return;
// 		}

// 		// 验证user_token
// 		bizUtils.validUserToken(req, res, requestParams.user_token, function(tokenValidData) {
// 			// 根据返回的security_code校验签名
// 			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
// 				errHandler.invalidSign(res);
// 				return;
// 			}

// 			// 修改密码
// 			var changePwdFormData = 'service=infrastructure.userPasswordChange&params=' + JSON.stringify({user_id: tokenValidData.data.user_id, old_password: requestParams.old_password, new_password: requestParams.new_password});
// 			request.post(appConfig.userPath, {form: changePwdFormData}, function(error, response, body) {
// 				if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
// 					res.send(JSON.parse(body));
// 				} else {
// 					errHandler.systemError(res, error);
// 					return;
// 				}
// 			});
// 		});
// 	} catch(e) {
// 		errHandler.systemError(res, e);
// 		return;
// 	}
// });

/*
 * 设置支付密码
 * @param user_token 用户令牌
 * @param sms_code 短信验证码
 * @param mobile 用户绑定的手机号
 * @param payment_password 支付密码（MD5）
 * @param sign 签名
 */
router.all('/user/set_pay_pwd', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.user_token == 'undefined' || typeof requestParams.sms_code == 'undefined' || typeof requestParams.mobile == 'undefined'
			|| typeof requestParams.payment_password == 'undefined'  || typeof requestParams.sign == 'undefined') {
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

			// 校验短信验证码
			var validSMSCodeFormData = 'service=notification.validateCheckCode&params=' + JSON.stringify({mobile: requestParams.mobile, check_code: requestParams.sms_code});
			request.post(appConfig.notificationPath, {form: validSMSCodeFormData}, function(error, response, validBody) {
				if(!error && response.statusCode == 200) {
					var validResult = JSON.parse(validBody);
					if(validResult.rsp_code != 'succ') {	// 短信验证码校验未通过，直接返回校验信息
						res.send(validResult);
						return;
					}
					// 设置支付密码
					var setPayPwdFormData = 'service=infrastructure.payPasswordSetting&params=' + JSON.stringify({user_id: tokenValidData.data.user_id, payment_password: requestParams.payment_password, login_password: requestParams.login_password});
					request.post(appConfig.userPath, {form: setPayPwdFormData}, function(error, response, body) {
						if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
							res.send(JSON.parse(body));
						} else {
							errHandler.systemError(res, error);
							return;
						}
					});
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
 * 设置支付选项
 * @param user_token 用户令牌
 * @param small_direct 小额免密，可选值Y（是），N（否）
 * @param sms_notify 短信通知，可选值Y（是），N（否）
 * @param sign 签名
 */
router.all('/user/set_pay_option', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		// 验证请求参数是否完整
		if(typeof requestParams.user_token == 'undefined' || typeof requestParams.small_direct == 'undefined'
			|| typeof requestParams.sms_notify == 'undefined' || typeof requestParams.sign == 'undefined') {
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

			// 业务参数集
			var paramsStr = JSON.stringify({
				user_id: tokenValidData.data.user_id,
				small_direct: requestParams.small_direct,
				sms_notify: requestParams.sms_notify
			});

			// 设置支付选项
			var setPayOptFormData = 'service=infrastructure.paySecurityOptions&params=' + paramsStr;
			request.post(appConfig.userPath, {form: setPayOptFormData}, function(error, response, body) {
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
 * 校验支付密码
 * @param user_token 用户令牌
 * @param payment_password 支付密码（MD5）
 * @param sign 签名
 */
router.all('/user/verify_pay_pwd', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.user_token == 'undefined' || typeof requestParams.payment_password == 'undefined' || typeof requestParams.sign == 'undefined') {
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

			// 重置支付密码
			var resetPayPwdFormData = 'service=infrastructure.payPasswordValidate&params=' + JSON.stringify({user_id: tokenValidData.data.user_id, payment_password: requestParams.payment_password});
			request.post(appConfig.userPath, {form: resetPayPwdFormData}, function(error, response, body) {
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
			var sendSMSCodeFormData = 'service=notification.sendCheckCode&params=' + JSON.stringify({sms_source: tokenValidData.data.app_id, mobile: requestParams.mobile,lock_value:requestParams.lock_value});
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
 * 交易注册（扫码支付）
 * @param app_token app令牌
 * @param sign 签名
 */
router.all('/trade/reg', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.sign == 'undefined') {
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

			// 交易注册
			var tradeRegFormData = {
				service : 'infrastructure.transactionRegister',
				params : '{}'
			};
			request.post(appConfig.infrastructurePath, {form: tradeRegFormData}, function(error, response, body) {
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
 * 交易验证（扫码支付）
 * @param app_token app令牌
 * @param flow_no 流水号
 * @param security_code 验证码
 * @param sign 签名
 */
router.all('/trade/verify', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.flow_no == 'undefined'
			|| typeof requestParams.security_code == 'undefined' || typeof requestParams.sign == 'undefined') {
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

			// 交易验证
			var tradeVerifyFormData = {
				service : 'infrastructure.transactionVerify',
				params : JSON.stringify({
					flow_no : requestParams.flow_no,
					security_code : requestParams.security_code
				})
			};
			request.post(appConfig.infrastructurePath, {form: tradeVerifyFormData}, function(error, response, body) {
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
 * 手机号登录注册
 * @param user_token app令牌
 * @param sign 签名
 */
router.all('/base/mobile_login_reg', function(req, res, next) {
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

			logger.info('【user_token校验返回数据：】');
			logger.info(JSON.stringify(tokenValidData));

			// 手机号登录注册
			var mobileRegFormData = {
				service : 'infrastructure.mobileLoginRegister',
				params : JSON.stringify({
					mobile : tokenValidData.data.mobile
				})
			};
			request.post(appConfig.infrastructurePath, {form: mobileRegFormData}, function(error, response, body) {
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
 * 用户意见反馈
 * @param user_token 	用户令牌
 * @param category	 	意见类型(产品缺陷、投诉、建议、其它)
 * @param contact 		联系方式(手机号或QQ号)
 * @param data_source	来源
 * @param desc1			描述
 * @param attachment	附件(图片) (非必传)
 # @param sign 			签名
 */
router.all('/user/feedback', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.user_token == 'undefined' || typeof requestParams.category == 'undefined'
			|| typeof requestParams.contact == 'undefined' || typeof requestParams.data_source == 'undefined'
			|| typeof requestParams.desc1 == 'undefined' || typeof requestParams.sign == 'undefined') {
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

			// 调用意见反馈接口
			var bizParams = {
				category : requestParams.category,
				contact : requestParams.contact,
				data_source : requestParams.data_source,
				desc1 : requestParams.desc1
			}
			if(typeof requestParams.attachment != 'undefined') {
				bizParams.attachment = requestParams.attachment;
			}
			var feedbackFormData = {
				service : 'infrastructure.feedback',
				params : JSON.stringify(bizParams)
			};
			request.post(appConfig.infrastructurePath, {form: feedbackFormData}, function(error, response, body) {
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
 * 查询用户意见反馈
 * @param app_token 	app令牌
 * @param contact		联系方式(手机号或QQ号)（非必填）
 * @param category 		意见类别（非必填）
 * @param data_source 	数据来源（非必填）
 * @param page 			分页参数（JSON格式字符串，包含page_no和page_size）
 * @param sign 			签名
 */
router.all('/user/feedback_list', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.page == 'undefined' || typeof requestParams.sign == 'undefined') {
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

			// 调用查询意见反馈接口
			var bizParams = {};
			if(typeof requestParams.contact != 'undefined') {
				bizParams.contact = requestParams.contact;
			}
			if(typeof requestParams.category != 'undefined') {
				bizParams.category = requestParams.category;
			}
			if(typeof requestParams.data_source != 'undefined') {
				bizParams.data_source = requestParams.data_source;
			}

			var feedbackFormData = {
				service : 'infrastructure.feedbackPageFind',
				params : JSON.stringify(bizParams),
				page : requestParams.page
			};
			request.post(appConfig.infrastructurePath, {form: feedbackFormData}, function(error, response, body) {
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
 * 反馈意见修改(运营平台调用)
 * @param app_token 	app令牌
 * @param feedback_id 	反馈id
 * @param remark		备注
 * @param sign 			签名
 */
router.all('/user/feedback_edit', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.feedback_id == 'undefined'
			|| typeof requestParams.remark == 'undefined' || typeof requestParams.sign == 'undefined') {
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

			// 反馈修改
			var feedbackEditFormData = {
				service : 'infrastructure.feedbackEdit',
				params : JSON.stringify({
					feedback_id : requestParams.feedback_id,
					remark : requestParams.remark
				})
			};
			request.post(appConfig.infrastructurePath, {form: feedbackEditFormData}, function(error, response, body) {
				if(!error && response.statusCode == 200) {
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
 * 登陆手机号变更
 * @param user_token 	用户令牌
 * @param user_id	 	用户id
 * @param mobile 		新手机号
 # @param sign 			签名
 */
router.all('/user/change_mobile', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.user_token == 'undefined' || typeof requestParams.user_id == 'undefined'
			|| typeof requestParams.mobile == 'undefined' || typeof requestParams.sign == 'undefined') {
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

			// 调用意见反馈接口
			var changeMobileFormData = {
				service : 'infrastructure.loginMobileChange',
				params : JSON.stringify({
					user_id : requestParams.user_id,
					mobile : requestParams.mobile
				})
			};
			request.post(appConfig.infrastructurePath, {form: changeMobileFormData}, function(error, response, body) {
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
 * 获取兑换中心app凭证
 * @param app_token 	app令牌
 * @param sign 			签名
 */
router.all('/app/access_token', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.sign == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}

		// 校验app_token
		bizUtils.validAppToken(req, res, requestParams.app_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}

			// 调用兑换中心接口获取app_token
			request.post(appConfig.exchangeCenterAppTokenPath, function(error, response, body) {
				logger.info('【兑换中心APP登录结果】' + JSON.stringify(body));
				try {
					if(!error && response.statusCode == 200) {
						var jsonBody = JSON.parse(body);
						if(jsonBody.errcode == 0) {
							res.send({
								rsp_code : 'succ',
								data : {
									access_token : jsonBody.data.access_token
								}
							});
							return;
						} else {
							errHandler.systemError(res, error);
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

/*
 * 获取兑换中心用户的access_token
 * @param user_token 	用户令牌
 * @param access_token 	app凭证
 * @param sign 			签名
 */
router.all('/user/access_token', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.user_token == 'undefined' || typeof requestParams.access_token == 'undefined' || typeof requestParams.sign == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}

		// 校验user_token
		bizUtils.validUserToken(req, res, requestParams.user_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}

			// 进行登录注册
			var regLoginFormData = {
				service : 'infrastructure.mobileLoginRegister',
				params : JSON.stringify({
					mobile : tokenValidData.data.mobile
				})
			}
			request.post(appConfig.infrastructurePath, {form: regLoginFormData}, function(error, response, regBody) {
				logger.info('【登录注册结果】' + JSON.stringify(regBody));
				try {
					if(!error && response.statusCode == 200) {
						var regData = JSON.parse(regBody);
						if(regData.rsp_code == 'fail') {
							errHandler.systemError(res, regData.error_msg);
							return;
						}

						// 调用兑换中心接口获取app_token
						var loginFromData = {
							method : 'user_passport.login',
							access_token : requestParams.access_token,
							login_account : tokenValidData.data.mobile,
							code : regData.data.code
						}
						request.post(appConfig.exchangeCenterHost, {form: loginFromData}, function(error, response, loginBody) {
							logger.info('【兑换中心登录结果】' + JSON.stringify(loginBody));
							try {
								if(!error && response.statusCode == 200) {
									var loginData = JSON.parse(loginBody);
									if(loginData.errcode == 0) {
										res.send({
											rsp_code : 'succ',
											data : {
												access_token : loginData.data.access_token
											}
										});
										return;
									} else {
										errHandler.systemError(res, error);
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

/*
 * 校验user_token是否有效
 * @param user_token 用户令牌
 * @param sign 签名
 */
router.all('/user/verify_user_token', function(req, res, next) {
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
			// user_token有效
			res.send({
				rsp_code: 'succ',
				data : {}
			});
			return;
		});
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

/*
 * 消费者APP社区地址重定向
 */
router.all('/consumer/bbs', function(req, res, next) {
	res.redirect(appConfig.bbsPath);
});

/*
 * 获取今日头条简讯和地址
 * @param app_token app令牌
 * @param sign 签名
 */
router.all('/get_dailynews', function(req, res, next) {
	try {

		res.send({
			rsp_code: 'fail',
			error_code: 'unavailable',
			error_msg: '此模块暂未开放'
		});
		return;

		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(!requestParams.app_token || !requestParams.sign) {
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

			res.send({
				rsp_code: 'succ',
				data: {
					url: '/openapi/h5/c/dailynews',
					news_list: [{
						news_title: '每天惠购物节·省钱攻略！'
					}, {
						news_title: '致店东家人的一封信'
					}]
				}
			});
			res.end();
		});
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

/*
 * 获取生活服务地址
 * @param app_token app令牌
 * @param sign 签名
 */
router.all('/get_lifeservice', function(req, res, next) {
	try {

		res.send({
			rsp_code: 'fail',
			error_code: 'unavailable',
			error_msg: '此模块暂未开放'
		});
		return;

		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(!requestParams.app_token || !requestParams.sign) {
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

			res.send({
				rsp_code: 'succ',
				data: {
					url: '/openapi/h5/c/lifeservice'
				}
			});
			res.end();
		});
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

/*******************熟么**********************/

/*
 * 熟么用户登录注册
 * @param app_token app令牌
 * @param params	业务参数集（mobile, device_id）
 * @param sign 		签名
 */
router.all('/shume/login_register', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(!requestParams.app_token || !requestParams.params || !requestParams.sign) {
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

			// 手机号登录注册
			var shumeLoginRegFormData = {
				service : 'infrastructure.shumeLoginRegister',
				params : requestParams.params
			};
			request.post(appConfig.infrastructurePath, {form: shumeLoginRegFormData}, function(error, response, body) {
				if(!error && response.statusCode == 200) {
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
 * 熟么用户登录
 * @param app_token 	app令牌
 * @param param 		业务参数集(mobile, code, device_id, device_name)
 * @param sign 			签名
 */
router.all('/shume/login', function(req, res, next) {
	// 请求参数
	var requestParams = bizUtils.extend(req.query, req.body);

	// 验证请求参数是否完整
	if(!requestParams.app_token || !requestParams.params || !requestParams.sign) {
		errHandler.incompleteParams(res);
		return;
	}
	// 请求业务参数
	var bizParams = JSON.parse(requestParams.params);
	// 校验业务参数是否完整
	if(!bizParams.mobile || !bizParams.code || !bizParams.device_id || !bizParams.device_name) {
		errHandler.incompleteParams(res);
		return;
	}

	// 校验app_token
	bizUtils.validAppToken(req, res, requestParams.app_token, function(tokenValidData) {
		// 根据返回的security_code校验签名
		if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
			errHandler.invalidSign(res);
			return;
		}

		// 开始验证登录是否有注册过
		var verifyFormData = {
			service : 'infrastructure.shumeLoginVerify',
			params : JSON.stringify({
				mobile : bizParams.mobile,
				code : bizParams.code,
				device_id : bizParams.device_id
			})
		};
		request.post(appConfig.infrastructurePath, {form: verifyFormData}, function(error, response, verifyBody) {
			try {
				if(!error && response.statusCode == 200) {
					var verifyResult = JSON.parse(verifyBody);
					if(verifyResult.rsp_code == 'succ') {	// 验证通过

						// 开始登录
						var shumeLoginFormData = {
							service : 'infrastructure.consumerLogin',
							params : JSON.stringify({
								mobile : bizParams.mobile,
								member_type_key : 'consumer',
								request_info : bizParams.device_name,
								device_model : bizParams.device_id,
								data_source : 'shume'
							})
						};
						request.post(appConfig.userPath, {form: shumeLoginFormData}, function(error, response, loginBody) {
							logger.info('【熟么】登录返回结果：');
							logger.info(loginBody);
							try {
								if(!error && response.statusCode == 200) {	//请求成功
									var loginResult = JSON.parse(loginBody);
									if(loginResult.rsp_code == 'succ') {	// 登录成功
										res.send(loginResult);
										return;
									} else if(loginResult.rsp_code == 'fail' &&
										(loginResult.error_code == 'user_not_exist' || loginResult.error_code == 'member_not_exist')) {	// 因为用户未注册而登录失败
										// 帮助用户自动注册账户
										var randomPwd = parseInt(Math.random() * 100000000);	// 生成随机的6~8位的纯数字密码
										var randomPwdMD5 = md5(randomPwd);
										// 先调用兑换中心注册接口
										var exchangeRegData = {
											method : 'temporary.accountCreate',
											app_token : appConfig.exchangeCenterRegAppTokenConstant,
											user_account : bizParams.mobile,
											user_password : randomPwdMD5
										}
										logger.info('【熟么】准备在兑换中心注册用户，参数：');
										logger.info(exchangeRegData);
										request.post(appConfig.exchangeCenterRegPath, {form: exchangeRegData}, function(error, response, exchangeRegBody) {
											logger.info('【熟么】兑换中心注册返回结果：');
											logger.info('error:' + error);
											logger.info('body:' + exchangeRegBody);
											if(!error && response.statusCode == 200) {
												var exchangeRegResult = JSON.parse(exchangeRegBody);

												if(exchangeRegResult.errcode != 0 && exchangeRegResult.errcode != 40007) {	// 注册失败并且不是账号已注册错误时，则直接返回
													logger.error('【熟么】兑换中心用户注册失败：');
													logger.error(exchangeRegResult);
													res.send({
														rsp_code: 'fail',
														error_code: 'exchange_center_reg_error',
														error_msg: exchangeRegResult.errmsg
													});
													res.end();
													return;
												}
												var exchangeUserId = '';
												if(exchangeRegResult.errcode == 40007) {	// 兑换中心账号已存在
													exchangeUserId += exchangeRegResult.user_id;
												} else {
													exchangeUserId += exchangeRegResult.data.user_id;
												}

												// 兑换中心注册成功，再在开放平台注册
												var openRegData = {
													service : 'infrastructure.consumerUserRegister',
													params : JSON.stringify({
														user_id : exchangeUserId,
														mobile : bizParams.mobile,
														password : randomPwdMD5
													})
												};
												logger.info('【熟么】准备在开放平台注册用户，参数：');
												logger.info(openRegData);
												request.post(appConfig.userPath, {form: openRegData}, function(error, response, openRegbody) {
													logger.info('【熟么】开放平台注册返回结果：');
													logger.info('error:' + error);
													logger.info('body:' + openRegbody);
													if(!error && response.statusCode == 200) {
														var openRegResult = JSON.parse(openRegbody);
														if(openRegResult.rsp_code == 'fail') {
															logger.info('【熟么】用户在开放平台注册失败！');
															res.send(openRegResult);
															res.end();
															return;
														} else {
															logger.info('【熟么】用户注册成功！');

															// 新用户注册送50金币，调用余额支付接口，不影响业务主流程
															var giveBizParam = {
																data_source: 'SJLY_01',
																detail: '注册赠送',
																amount: '50',
																out_trade_no: bizUtils.generateTradeNo(),
																buyer_id: '10000001',	// 每天惠账户
																seller_id: exchangeUserId,
																payment_way_key: 'ZFFS_08'
															};
															giveBizParam.out_trade_body = JSON.stringify(giveBizParam);
															var giveGoldFormData = {
																service: 'finance.balancePay',
																params: JSON.stringify(giveBizParam)
															};
															request.post(appConfig.financePath, {form: giveGoldFormData}, function(error, response, giveGoldBody) {
																try {
																	logger.info('消费者新用户注册送50金币结果：');
																	logger.info(giveGoldBody);
																	logger.info('注册手机号：' + bizParams.mobile);
																} catch(e) {
																	logger.info('消费者新用户注册送50金币调用接口失败：');
																	logger.error(e);
																}
															});

															logger.info('【熟么】准备自动登录，登录参数：');
															logger.info(JSON.stringify(shumeLoginFormData));
															request.post(appConfig.userPath, {form: shumeLoginFormData}, function(error, response, autoLoginBody) {
																logger.info('【熟么】自动登录结果：');
																logger.info(autoLoginBody);
																try {
																	if(!error && response.statusCode == 200) {	//请求成功
																		res.send(JSON.parse(autoLoginBody));
																		return;
																	} else {
																		errHandler.systemError(res, error);
																		return;
																	}
																} catch(e) {
																	errHandler.systemError(res, e);
																	return;
																}
															});
														}
													} else {
														errHandler.systemError(res, error);
														return;
													}
												});
											} else {
												errHandler.systemError(res, error);
												return;
											}
										});
									} else {	// 其他原因登录失败
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
					} else {
						res.send(verifyResult);
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
});

/*
 * 熟么商户登录
 * @param app_token 	app令牌
 * @param param 		业务参数集(mobile, code, device_id, device_name)
 * @param sign 			签名
 */
router.all('/shume/store_login', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(!requestParams.app_token || !requestParams.params || !requestParams.sign) {
			errHandler.incompleteParams(res);
			return;
		}
		// 请求业务参数
		var bizParams = JSON.parse(requestParams.params);
		// 校验业务参数是否完整
		if(!bizParams.mobile || !bizParams.code || !bizParams.device_id || !bizParams.device_name) {
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

			// 开始验证登录是否有注册过
			var verifyFormData = {
				service : 'infrastructure.shumeLoginVerify',
				params : JSON.stringify({
					mobile : bizParams.mobile,
					code : bizParams.code,
					device_id : bizParams.device_id
				})
			};
			request.post(appConfig.infrastructurePath, {form: verifyFormData}, function(error, response, verifyBody) {
				try {
					if(!error && response.statusCode == 200) {
						var verifyResult = JSON.parse(verifyBody);
						if(verifyResult.rsp_code == 'succ') {	// 验证通过
							// 加盟店登录
							var storeLoginFormData = {
								service : 'infrastructure.storeLogin',
								params : JSON.stringify({
									user_account: bizParams.mobile,
									request_info: bizParams.device_id,
									device_model: bizParams.device_name,
									data_source: 'shume'
								})
							};
							request.post(appConfig.userPath, {form: storeLoginFormData}, function(error, response, loginBody) {
								try {
									if(!error && response.statusCode == 200) {	//请求成功
										res.send(JSON.parse(loginBody));
									} else {
										errHandler.systemError(res, error);
										return;
									}
								} catch(e) {
									errHandler.systemError(res, e);
									return;
								}
							});
						} else {
							res.send(verifyResult);
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



/*#############################################################################*/
/*####                            惠易定3.0集成                            ####*/
/*#############################################################################*/

/*
 * 根据手机号验证是否为店东并且从惠易定3.0同步店东信息（店东助手APP登录发送验证码前调用此接口）
 * @param app_token app令牌
 * @param mobile 手机号
 * @param sign 签名
 */
router.all('/user/valid_store_exist', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		logger.info('【店东登录验证接口请求参数】');
		logger.info(requestParams);

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.mobile == 'undefined'
			|| typeof requestParams.sign == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}

		// 校验app_token
		bizUtils.validAppToken(req, res, requestParams.app_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}

			// STEP1：在开放平台校验账号是否存在
			var platformValidFormData = {
				service: 'infrastructure.memberTypeValidateByMobile',
				params: JSON.stringify({
					mobile: requestParams.mobile,
					member_type_key: 'stores'
				})
			};
			request.post(appConfig.userPath, {form: platformValidFormData}, function(error1, response1, platformValidBody) {
				logger.info('###店东助手APP登录开放平台账号验证结果：');
				logger.info(platformValidBody);
				if(!error1 && response1.statusCode == 200) {
					var platformValidResult = JSON.parse(platformValidBody);
					// 接口请求失败或者账号在开放平台存在，直接返回结果
					if(platformValidResult.rsp_code == 'fail' ||
						(platformValidResult.rsp_code == 'succ' && platformValidResult.data.member_type == 'exist')) {
						res.send(platformValidResult);
						return;
					}

					// STEP2：去惠易定3.0判断账号是否存在
					var hyd3ValidFormData = {
						mobile : requestParams.mobile
					};
					request.post(appConfig.hyd3ValidUserPath, {form: hyd3ValidFormData}, function(error2, response2, hyd3ValidBody) {
						logger.info('###店东助手APP惠易定3.0账号验证结果：');
						logger.info(hyd3ValidBody);
						if(!error2 && response2.statusCode == 200) {
							var hyd3ValidResult = JSON.parse(hyd3ValidBody);
							if(hyd3ValidResult.status != 200) {	// 惠易定3.0中账号不存在，直接返回开放平台中不存在的结果
								res.send(platformValidResult);
								return;
							}

							// STEP3：惠易定3.0中存在该账号信息，同步至开放平台
							var syncStoreInfoFormData = {
								service : 'member.storeSyncRegisterForHYD',
								params : JSON.stringify({
									stores_name : hyd3ValidResult.data.name,
									stores_no : bizUtils.generateStoreNo(),
									contact_person : hyd3ValidResult.data.businessUser,
									contact_tel : hyd3ValidResult.data.mobile,
									area_id : hyd3ValidResult.data.districtId,
									address : hyd3ValidResult.data.address,
									registered_date : hyd3ValidResult.data.createdAt
								})
							};
							logger.info('###同步惠易定3.0中的账号到开放平台：');
							logger.info(syncStoreInfoFormData);
							request.post(appConfig.memberPath, {form: syncStoreInfoFormData}, function(error3, response3, syncStoreInfoBody) {
								logger.info('###同步惠易定3.0账号同步结果：');
								logger.info(syncStoreInfoBody);
								if(!error3 && response3.statusCode == 200) {
									var syncStoreInfoResult = JSON.parse(syncStoreInfoBody);
									if(syncStoreInfoResult.rsp_code == 'succ') {	// 账号同步成功
										platformValidResult.data.user = 'exist';
										platformValidResult.data.member_type = 'exist';
										res.send(platformValidResult);
										return;
									} else {
										errHandler.systemError(res, syncStoreInfoResult.error_msg);
										return;
									}
								} else {
									errHandler.systemError(res, error3);
									return;
								}
							});
						} else {
							errHandler.systemError(res, error2);
							return;
						}
					});
				} else {
					errHandler.systemError(res, error1);
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
 * 获取惠易定登录版本（2.0 or 3.0）
 * @param user_token 用户令牌
 * @param sign 签名
 */
router.all('/hyd/get_version', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		// 验证请求参数是否完整
		if(!requestParams.user_token || !requestParams.sign) {
			errHandler.incompleteParams(res);
			return;
		}

		// 校验user_token
		bizUtils.validUserToken(req, res, requestParams.user_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}
			var areaId = tokenValidData.data.area_id;

			logger.info('###进入惠易定模块请求数据：');
			logger.info(tokenValidData.data);

			// 除深圳、新乡、南昌、株洲、赤峰、永州、汉中、泉州、临清外，其余地区直接进入2.0
			// !areaId.startsWith('4403') &&
			if(!areaId.startsWith('4107') && !areaId.startsWith('3601')
				&& !areaId.startsWith('4302') && !areaId.startsWith('1504') && !areaId.startsWith('4311')
				&& !areaId.startsWith('6107') && !areaId.startsWith('3505') && areaId != '371581') {
				logger.info('直接进入惠易定2.0');
				res.send({
					rsp_code : 'succ',
					data : {
						hyd_version : '2'
					}
				});
				return;
			}

			// STEP1: 判断是否为惠易定3.0用户
			var hyd3ValidFormData = {
				mobile : tokenValidData.data.mobile
			};
			request.post(appConfig.hyd3ValidUserPath, {form: hyd3ValidFormData}, function(error1, response2, hyd3ValidBody) {
				logger.info('###获取惠易定版本店东助手APP惠易定3.0账号验证结果：');
				logger.info(hyd3ValidBody);
				if(!error1 && response2.statusCode == 200) {
					var hyd3ValidResult = JSON.parse(hyd3ValidBody);
					if(hyd3ValidResult.status == 200) {
						// 用户存在，登录惠易定3.0
						// 调用登录接口
						var loginFormData = {
							mobile : tokenValidData.data.mobile
						};
						request.post(appConfig.hyd3LoginPath, {form: loginFormData}, function(error2, response, loginBody) {
							logger.info('###登录惠易定3.0返回结果：');
							logger.info(loginBody);
							if(!error2 && response.statusCode == 200) {
								var loginResult = JSON.parse(loginBody);
								if(loginResult.status == '200') {	// 登录成功
									res.send({
										rsp_code : 'succ',
										data : {
											hyd_version : '3',
											url : loginResult.data.url,
											cookieDomain : loginResult.data.cookieDomain,
											cookies : bizUtils.stringifyCookies(loginResult.data.cookies)
										}
									});
									return;
								} else if(loginResult.status == '404') {	// 账号未注册
									res.send({
										rsp_code : 'fail',
										error_code : 'not_registered',
										error_msg : '请联系当地分公司开通惠易定权限'
									});
									return;
								} else if(loginResult.status == '403') {	// 账号被锁定
									res.send({
										rsp_code : 'fail',
										error_code : 'account_locked',
										error_msg : '账号被锁定，请联系当地分公司'
									});
									return;
								} else {	// 惠易定3.0登录接口错误
									res.send({
										rsp_code : 'fail',
										error_code : 'hyd_system_error',
										error_msg : '惠易定系统错误'
									});
									return;
								}
							} else {
								errHandler.systemError(res, error2);
								return;
							}
						});
					} else if(hyd3ValidResult.status == 404) {
						// 用户不存在，判断所在地区是否已开通3.0
						fs.readFile('hyd3-inactivated-area.txt', function(err, txt) {
							if(err) {	// 配置文件读取失败
								errHandler.systemError(res, err);
								return;
							}
							// 未开放3.0地区码集合
							var inactivatedAreaStr = txt.toString();
							if(inactivatedAreaStr.indexOf('|' + areaId + '|') != -1) {	// 未开通，返回惠易定2.0
								res.send({
									rsp_code : 'succ',
									data : {
										hyd_version : '2'
									}
								});
								return;
							} else {	// 已开通，跳转到惠易定3.0注册页
								// 查询便利店基本信息
								var storeInfoFormData = {
									service: 'member.storeDetailFind',
									params: JSON.stringify({
										stores_id: tokenValidData.data.member_id
									})
								};
								request.post(appConfig.memberPath, {form: storeInfoFormData}, function(error, response, storeInfoBody) {
									try {
										if(!error && response.statusCode == 200) {
											var storeInfoResult = JSON.parse(storeInfoBody);
											var registerUrl = hyd3ValidResult.data.url;
											if(storeInfoResult.rsp_code == 'succ') {	// 将便利店信息以get方式传到注册页面
												var getParams =
													'storeAccount=' + tokenValidData.data.mobile
													+ '&storeName=' + storeInfoResult.data.detail.stores_name
													+ '&contact=' + storeInfoResult.data.detail.contact_person
													+ '&contactMobile=' + storeInfoResult.data.detail.contact_tel;
												if(registerUrl.indexOf('?') == '-1') {
													registerUrl += '?' + getParams;
												} else {
													registerUrl += '&' + getParams;
												}
											}
											registerUrl = encodeURI(registerUrl);
											res.send({
												rsp_code : 'succ',
												data : {
													hyd_version : '3',
													url : registerUrl,
													cookieDomain : '',
													cookies : []
												}
											});
											return;
										} else {
											errHandler.systemError(res, error);
											return;
										}
									} catch(e) {
										errHandler.systemError(res, e);
										return;
									}
								});
							}
						});
					} else {
						res.send({
							rsp_code : 'fail',
							error_code : 'hyd_system_error',
							error_msg : hyd3ValidResult.msg || '惠易定系统错误'
						});
						return;
					}
				} else {
					errHandler.systemError(res, error1);
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
 * 大汉手机充值回调转发
 */
router.post('/daHanNotify/mobileRechargeNotify', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = req.body;
		request.post(appConfig.daHanNotifyCallbackPath, {form: requestParams}, function(error, response, body) {
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

module.exports = router;
