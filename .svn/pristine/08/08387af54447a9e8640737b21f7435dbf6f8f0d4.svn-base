/*
 * 鸿商POS机专用
 * @author Changfeng
 */
var express = require('express');
var router = express.Router();
var url = require('url');
var request = require('request');
var ipware = require('ipware')();
var md5 = require('md5');

var logger = require('../lib/log4js').getLogger('pos-router');
var appConfig = require('../app-config');
var errHandler = require('../lib/err-handler');
var bizUtils = require('../lib/biz-utils');

/*
 * 加盟店登录
 * @param app_token app令牌
 * @param mobile 账号
 * @param sms_code 短信验证码
 * @param sign 签名
 */
router.all('/login', function(req, res, next) {
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
								request_info: ipware.get_ip(req).clientIp,
								device_model: 'pos',
								data_source: 'hongshang'
							})
						};
						request.post(appConfig.userPath, {form: storeLoginFormData}, function(error, response, loginBody) {
							try {
								if(!error && response.statusCode == 200) {	//请求成功
									var loginData = JSON.parse(loginBody);
									if(loginData.rsp_code == 'succ') {
										res.send({
											rsp_code : 'succ',
											data : {
												user_token : loginData.data.user_token,
												security_code : loginData.data.security_code,
												store_id : loginData.data.member_id,
												mobile : loginData.data.mobile
											}
										});
									} else {
										res.send(loginData);
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
 * 查询加盟店礼券余额
 * @param user_token	user令牌
 * @param sign 			签名
 */
router.all('/voucher_balance', function(req, res, next) {
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

			// 查询会员资产
			var assetFormData = {
				service : 'finance.memberAssetQuery',
				params : JSON.stringify({
					member_id : tokenValidData.data.member_id
				})
			};
			request.post(appConfig.financePath, {form: assetFormData}, function(error, response, body) {
				if(!error && response.statusCode == 200) {
					var asset = JSON.parse(body);
					if(asset.rsp_code == 'succ') {	// 查询成功后取出资产中的礼券余额进行返回
						res.send({
							rsp_code : 'succ',
							data : {
								voucher_balance : asset.data.voucher_balance
							}
						});
						return;
					} else {
						res.send(asset);
						return;
					}
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
 * 查询加盟店资产
 * @param user_token	user令牌
 * @param sign 			签名
 */
router.all('/store_asset', function(req, res, next) {
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

			// 查询会员资产
			var assetFormData = {
				service : 'finance.memberAssetQuery',
				params : JSON.stringify({
					member_id : tokenValidData.data.member_id
				})
			};
			request.post(appConfig.financePath, {form: assetFormData}, function(error, response, body) {
				if(!error && response.statusCode == 200) {
					res.send(JSON.parse(body));
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
 * 查询加盟店零钱交易流水
 * @param user_token	user令牌
 * @param page_size		每页记录数
 * @param page_no		页号
 * @param sign 			签名
 */
router.all('/store_cash_log', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(!requestParams.user_token || !requestParams.page_size || !requestParams.page_no || !requestParams.sign) {
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

			// 查询零钱交易流水
			var cashLogFormData = {
				service: 'finance.memberCashLogPageFind',
				params: JSON.stringify({
					member_id: tokenValidData.data.member_id,
					member_type_key: 'stores'
				}),
				page: JSON.stringify({
					page_size: requestParams.page_size,
					page_no: requestParams.page_no
				})
			};
			request.post(appConfig.financePath, {form: cashLogFormData}, function(error, response, body) {
				if(!error && response.statusCode == 200) {
					res.send(JSON.parse(body));
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
 * 赠送礼券
 * @param user_token	user令牌
 * @param mobile		用户手机号
 * @param amount		赠送礼券数量(上限200)
 * @param trade_no		交易流水号
 * @param sign 			签名
 */
router.all('/voucher_reward', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.user_token == 'undefined' || typeof requestParams.mobile == 'undefined' || typeof requestParams.amount == 'undefined' 
			|| typeof requestParams.trade_no == 'undefined' || typeof requestParams.sign == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}

		// 判断礼券必须为数字，并且不能小于0且不超过200
		if(isNaN(requestParams.amount) || parseInt(requestParams.amount) <=0 || parseInt(requestParams.amount) > 200) {
			res.send({
				rsp_code : 'fail',
				error_code : 'invalid amount',
				error_msg : '礼券数量非法'
			});
			return;
		}

		// 验证user_token
		bizUtils.validUserToken(req, res, requestParams.user_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}

			// 进行礼券赠送
			var bizParam = {
				data_source : 'SJLY_04',
				detail : '店东赠送礼券',
				amount : parseInt(requestParams.amount) + '',
				out_trade_no : requestParams.trade_no,
				payment_way_key : 'ZFFS_06',
				buyer_id : '10000003',			// 直接将买家指定为匿名用户
				seller_id : tokenValidData.data.member_id,
				mobile : requestParams.mobile
			};
			bizParam.out_trade_body = JSON.stringify(bizParam);
			var voucherRewardFormData = {	// 礼券赠送请求表单数据
				service : 'finance.orderReward',
				params : JSON.stringify(bizParam)
			};
			request.post(appConfig.financePath, {form: voucherRewardFormData}, function(error, response, body) {
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
 * 店东信息反馈
 * @param app_token 				app令牌
 * @param stores_name 				店东名称
 * @param contact_person			联系人
 * @param mobile 					手机号
 * @param old_account 				原店东助手账号
 * @param sign 						签名
 */
router.all('/feedback', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.stores_name == 'undefined' || typeof requestParams.contact_person == 'undefined'
			|| typeof requestParams.mobile == 'undefined' || typeof requestParams.old_account == 'undefined' || typeof requestParams.sign == 'undefined') {
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

			delete requestParams.app_token;
			delete requestParams.sign;
			requestParams.device_type = 'pos';

			// 调用店信息反馈接口
			var storefeedbackFormData = {
				service : 'member.storeFeedback',
				params : JSON.stringify(requestParams)
			};
			request.post(appConfig.memberPath, {form: storefeedbackFormData}, function(error, response, loginBody) {
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
		});
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

/*
 * 扫码支付
 * @param user_token		user令牌
 * @param detail			交易详情
 * @param amount			金额
 * @param trade_no			交易流水号
 * @param payment_way_key	支付方式(支付宝：ZFFS_10 , 微信：ZFFS_11 , 翼支付：ZFFS_14)
 * @param auth_code			支付条码信息
 * @param sign 				签名
 */
router.all('/scan_code_pay', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.user_token == 'undefined' || typeof requestParams.detail == 'undefined' || typeof requestParams.amount == 'undefined' 
			|| typeof requestParams.trade_no == 'undefined' || typeof requestParams.payment_way_key == 'undefined' 
			|| typeof requestParams.auth_code == 'undefined' || typeof requestParams.sign == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}

		// 校验金额，暂定不超过5万
		if(isNaN(requestParams.amount) || parseFloat(requestParams.amount) <=0 || parseFloat(requestParams.amount) > 50000) {
			res.send({
				rsp_code : 'fail',
				error_code : 'invalid amount',
				error_msg : '金额非法'
			});
			return;
		}

		// 校验支付方式
		if(requestParams.payment_way_key != 'ZFFS_10' && requestParams.payment_way_key != 'ZFFS_11' && requestParams.payment_way_key != 'ZFFS_14') {
			res.send({
				rsp_code : 'fail',
				error_code : 'invalid payment_way_key',
				error_msg : '支付方式非法'
			});
			return;
		}

		// 验证user_token
		bizUtils.validUserToken(req, res, requestParams.user_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}

			// 进行余额支付
			var bizParam = {
				data_source : 'SJLY_04',
				detail : requestParams.detail,
				amount : requestParams.amount + '',
				out_trade_no : requestParams.trade_no,
				payment_way_key : requestParams.payment_way_key,
				buyer_id : '10000001',			// 直接将买家指定每天惠
				seller_id : tokenValidData.data.member_id,
				auth_code : requestParams.auth_code
			};
			bizParam.out_trade_body = JSON.stringify(bizParam);

			var balancePayFormData = {
				service : 'finance.balancePay',
				params : JSON.stringify(bizParam)
			};
			request.post(appConfig.financePath, {form: balancePayFormData}, function(error, response, body) {
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
 * 亲情卡支付
 * @param user_token		user令牌
 * @param detail			交易详情
 * @param amount			金额
 * @param trade_no			交易流水号
 * @param card_no			亲情卡号
 * @param sign 				签名
 */
router.all('/prepay_card_pay', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(!requestParams.user_token || !requestParams.detail || !requestParams.amount 
			|| !requestParams.trade_no || !requestParams.card_no || !requestParams.sign) {
			errHandler.incompleteParams(res);
			return;
		}

		// 校验金额，暂定不超过5万
		if(isNaN(requestParams.amount) || parseFloat(requestParams.amount) <=0 || parseFloat(requestParams.amount) > 50000) {
			res.send({
				rsp_code : 'fail',
				error_code : 'invalid amount',
				error_msg : '金额非法'
			});
			return;
		}

		// 验证user_token
		bizUtils.validUserToken(req, res, requestParams.user_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}

			// STEP1: 根据亲情卡号查询会员id（即买家id）
			var cardFormData = {
				service : 'finance.prepayCardScan',
				params : JSON.stringify({
					card_no : requestParams.card_no
				})
			};
			request.post(appConfig.financePath, {form: cardFormData}, function(error, response, cardBody) {
				try {
					if(!error && response.statusCode == 200) {
						var cardResult = JSON.parse(cardBody);
						if(cardResult.rsp_code == 'fail') {	// 查询亲情卡失败直接返回错误信息
							res.send(cardResult);
							return;
						}

						// SETP2: 进行余额支付
						var bizParam = {
							data_source : 'SJLY_04',
							detail : requestParams.detail,
							amount : requestParams.amount + '',
							out_trade_no : requestParams.trade_no,
							payment_way_key : 'ZFFS_05',
							buyer_id : cardResult.data.member_id,
							seller_id : tokenValidData.data.member_id
						};
						bizParam.out_trade_body = JSON.stringify(bizParam);

						var balancePayFormData = {
							service : 'finance.balancePay',
							params : JSON.stringify(bizParam)
						};
						request.post(appConfig.financePath, {form: balancePayFormData}, function(error, response, body) {
							if(!error && response.statusCode == 200) {
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
 * 付款码支付
 * @param user_token		user令牌
 * @param detail			交易详情
 * @param amount			金额
 * @param trade_no			交易流水号
 * @param qrcode			二维码
 * @param sign 				签名
 */
router.all('/qrcode_pay', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(!requestParams.user_token || !requestParams.detail || !requestParams.amount 
			|| !requestParams.trade_no || !requestParams.qrcode || !requestParams.sign) {
			errHandler.incompleteParams(res);
			return;
		}

		// 校验金额，暂定不超过5万
		if(isNaN(requestParams.amount) || parseFloat(requestParams.amount) <=0 || parseFloat(requestParams.amount) > 50000) {
			res.send({
				rsp_code : 'fail',
				error_code : 'invalid amount',
				error_msg : '金额非法'
			});
			return;
		}

		logger.info('qrCode:' + requestParams.qrcode);

		// 二维码内容
		var qrCode = JSON.parse(requestParams.qrcode);

		// 验证user_token
		bizUtils.validUserToken(req, res, requestParams.user_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}

			if(typeof qrCode == 'object') {		// 扫描的是旧的带金额的二维码
				// 校验付款码内容
				if(qrCode.member_type_key != 'consumer' || qrCode.type != 'P') {
					res.send({
						rsp_code : 'fail',
						error_code : 'qrcode error',
						error_msg : '付款码错误'
					});
					return;
				}

				// 校验收银金额与付款码金额是否一致
				if(parseFloat(requestParams.amount) != parseFloat(qrCode.amount)) {
					res.send({
						rsp_code : 'fail',
						error_code : 'amount error',
						error_msg : '收银金额与付款码金额不一致'
					});
					return;
				}

				// STEP1: 校验流水号
				var tradeVerifyFormData = {
					service: 'infrastructure.transactionVerify',
					params: JSON.stringify({
						flow_no: qrCode.flow_no,
						security_code: qrCode.security_code
					})
				};
				request.post(appConfig.infrastructurePath, {form: tradeVerifyFormData}, function(error, response, verifyBody) {
					try {
						if(!error && response.statusCode == 200) {
							var verifyResult = JSON.parse(verifyBody);
							if(verifyResult.rsp_code == 'fail') {	// 流水号校验失败直接返回结果
								res.send(verifyResult);
								return;
							}

							// SETP2: 进行余额支付
							var bizParam = {
								data_source : 'SJLY_04',
								detail : requestParams.detail,
								amount : requestParams.amount + '',
								out_trade_no : requestParams.trade_no,
								payment_way_key : 'ZFFS_05',
								buyer_id : qrCode.member_id,
								seller_id : tokenValidData.data.member_id
							};
							bizParam.out_trade_body = JSON.stringify({
								buyer_id : qrCode.member_id,
								seller_id : tokenValidData.data.member_id,
								security_code : qrCode.security_code,
								security_type : 'P'
							});

							var balancePayFormData = {
								service : 'finance.balancePay',
								params : JSON.stringify(bizParam)
							};
							request.post(appConfig.financePath, {form: balancePayFormData}, function(error, response, body) {
								if(!error && response.statusCode == 200) {
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
					} catch(e) {
						errHandler.systemError(res, e);
						return;
					}
				});
			} else {	// 扫描的是新的不带金额的二维码或条形码
				logger.info('新型条形码');
				// 校验付款码内容
				if(typeof qrCode != 'number') {
					res.send({
						rsp_code : 'fail',
						error_code : 'qrcode error',
						error_msg : '付款码错误'
					});
					return;
				}

				// STEP1: 校验付款码
				var verifyCodeFormData = {
					service: 'trade.tradeCodeVerify',
					params: JSON.stringify({
						security_code: requestParams.qrcode
					})
				};
				logger.info(JSON.stringify(verifyCodeFormData));
				request.post(appConfig.financePath, {form: verifyCodeFormData}, function(error, response, verifyBody) {
					logger.info(verifyBody);
					if(!error && response.statusCode == 200) {
						var verifyResult = JSON.parse(verifyBody);
						if(verifyResult.rsp_code == 'fail') {	// 付款码无效
							res.send(verifyResult);
							return;
						}

						// SETP2: 进行余额支付
						var bizParam = {
							data_source : 'SJLY_04',
							detail : requestParams.detail,
							amount : requestParams.amount + '',
							out_trade_no : requestParams.trade_no,
							payment_way_key : 'ZFFS_05',
							buyer_id : verifyResult.data.member_id,
							seller_id : tokenValidData.data.member_id
						};
						bizParam.out_trade_body = JSON.stringify({
							buyer_id : verifyResult.data.member_id,
							seller_id : tokenValidData.data.member_id,
							security_code : verifyResult.data.security_code,
							security_type : 'P'
						});
						var balancePayFormData = {
							service : 'finance.balancePay',
							params : JSON.stringify(bizParam)
						};
						request.post(appConfig.financePath, {form: balancePayFormData}, function(error, response, body) {
							if(!error && response.statusCode == 200) {
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
			}
		});
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

/*
 * 交易状态查询
 * @param user_token		user令牌
 * @param trade_no			交易流水号
 * @param sign 				签名
 */
router.all('/trade_status', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.user_token == 'undefined' || typeof requestParams.trade_no == 'undefined' || typeof requestParams.sign == 'undefined') {
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

			// 查询交易状态
			var statusFormData = {
				service : 'finance.transactionStatusFind',
				params : JSON.stringify({
					out_trade_no : requestParams.trade_no
				})
			};
			request.post(appConfig.financePath, {form: statusFormData}, function(error, response, body) {
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
 * 礼券交易流水查询
 * @param user_token				user令牌
 * @param transaction_date_start	交易开始时间（非必填）
 * @param transaction_date_end		交易结束时间（非必填）
 * @param sign 						签名
 */
router.all('/voucher_bill', function(req, res, next) {
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

			// 查询交易流水
			var billFormData = {
				service : 'finance.storeVoucherBill',
				params : JSON.stringify({
					member_id : tokenValidData.data.member_id,
					transaction_date_start : requestParams.transaction_date_start || null,
					transaction_date_end : requestParams.transaction_date_end || null
				})
			};
			request.post(appConfig.financePath, {form: billFormData}, function(error, response, body) {
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
 * 根据手机号查询是否为会员
 * @param user_token	user令牌
 * @param mobile		手机号
 * @param sign 			签名
 */
router.all('/valid_member_by_mobile', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.user_token == 'undefined' || typeof requestParams.mobile == 'undefined' 
			|| typeof requestParams.sign == 'undefined') {
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

			// 根据手机号查询会员信息
			var memberFormData = {
				service : 'member.memberInfoFindByMobile',
				params : JSON.stringify({
					mobile : requestParams.mobile,
					member_type_key : 'consumer'
				})
			};
			request.post(appConfig.memberPath, {form: memberFormData}, function(error, response, body) {
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
 * 会员充值
 * @param user_token		user令牌
 * @param detail			交易详情
 * @param amount			金额
 * @param trade_no			交易流水号
 * @param mobile			充值手机号
 * @param sign 				签名
 */
router.all('/member_recharge', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(!requestParams.user_token || !requestParams.detail || !requestParams.amount 
			|| !requestParams.trade_no || !requestParams.mobile || !requestParams.sign) {
			errHandler.incompleteParams(res);
			return;
		}

		// 校验金额，暂定不超过5万
		if(isNaN(requestParams.amount) || parseFloat(requestParams.amount) <=0 || parseFloat(requestParams.amount) > 50000) {
			res.send({
				rsp_code : 'fail',
				error_code : 'invalid amount',
				error_msg : '金额非法'
			});
			return;
		}

		// 验证user_token
		bizUtils.validUserToken(req, res, requestParams.user_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}

			// STEP1: 根据手机号查询会员id（即买家id）
			var consumerFormData = {
				service : 'member.memberInfoFindByMobile',
				params : JSON.stringify({
					mobile : requestParams.mobile,
					member_type_key : 'consumer'
				})
			};
			request.post(appConfig.memberPath, {form: consumerFormData}, function(error, response, consumerBody) {
				try {
					if(!error && response.statusCode == 200) {
						var consumerResult = JSON.parse(consumerBody);
						if(consumerResult.rsp_code == 'fail') {
							res.send(consumerResult);
							return;
						}

						// SETP2: 进行余额支付
						var bizParam = {
							data_source : 'SJLY_04',
							detail : requestParams.detail,
							amount : requestParams.amount + '',
							out_trade_no : requestParams.trade_no,
							payment_way_key : 'ZFFS_05',
							seller_id : consumerResult.data.consumer_id,
							buyer_id : tokenValidData.data.member_id
						};
						bizParam.out_trade_body = JSON.stringify(bizParam);

						var balancePayFormData = {
							service : 'finance.balancePay',
							params : JSON.stringify(bizParam)
						};
						request.post(appConfig.financePath, {form: balancePayFormData}, function(error, response, body) {
							if(!error && response.statusCode == 200) {
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
 * 会员余额查询
 * @param user_token		user令牌
 * @param mobile			充值手机号
 * @param sign 				签名
 */
router.all('/member_asset_find', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(!requestParams.user_token || !requestParams.mobile || !requestParams.sign) {
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

			// STEP1: 根据手机号查询会员id
			var consumerFormData = {
				service : 'member.memberInfoFindByMobile',
				params : JSON.stringify({
					mobile : requestParams.mobile,
					member_type_key : 'consumer'
				})
			};
			request.post(appConfig.memberPath, {form: consumerFormData}, function(error, response, consumerBody) {
				try {
					if(!error && response.statusCode == 200) {
						var consumerResult = JSON.parse(consumerBody);
						if(consumerResult.rsp_code == 'fail') {
							res.send(consumerResult);
							return;
						}

						// SETP2: 进行资产查询
						var assetFormData = {
							service : 'finance.memberAssetFind',
							params : JSON.stringify({
								member_id : consumerResult.data.consumer_id
							})
						};
						request.post(appConfig.financePath, {form: assetFormData}, function(error, response, body) {
							if(!error && response.statusCode == 200) {
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
 * 本店会员查询
 * @param user_token		user令牌
 * @param sign 				签名
 */
router.all('/member_list_find', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(!requestParams.user_token || !requestParams.sign) {
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

			// 查询本店会员
			var memberListFormData = {
				service : 'member.storesRelConsumerAssetList',
				params : JSON.stringify({
					stores_id : tokenValidData.data.member_id
				})
			};
			request.post(appConfig.memberPath, {form: memberListFormData}, function(error, response, body) {
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
 * 激活亲情卡
 * @param user_token		user令牌
 * @param card_no			亲情卡号
 * @param sign 				签名
 */
router.all('/prepay_card_activate', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(!requestParams.user_token || !requestParams.card_no || !requestParams.sign) {
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

			// 激活亲情卡
			var cardActivateFormData = {
				service : 'finance.prepayCardActivate',
				params : JSON.stringify({
					card_no : requestParams.card_no,
					member_id : tokenValidData.data.member_id
				})
			};
			request.post(appConfig.financePath, {form: cardActivateFormData}, function(error, response, body) {
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
 * 店东通过手机号和短信验证码帮助用户注册账号
 * @param user_token 用户令牌
 * @param mobile 手机号
 * @param sms_code 短信验证码
 * @param sign 签名
 */
router.all('/member_reg', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(!requestParams.user_token || !requestParams.mobile
			|| !requestParams.sms_code || !requestParams.sign) {
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

						// 先调用兑换中心注册接口
						var exchangeRegData = {
							method : 'temporary.accountCreate',
							app_token : appConfig.exchangeCenterRegAppTokenConstant,
							user_account : requestParams.mobile,
							user_password : randomPwdMD5
						}
						logger.info('鸿商准备在兑换中心注册用户，参数：');
						logger.info(exchangeRegData);
						request.post(appConfig.exchangeCenterRegPath, {form: exchangeRegData}, function(error, response, exchangeRegBody) {
							logger.info('兑换中心注册返回结果：');
							logger.info('error:' + error);
							logger.info('body:' + exchangeRegBody);
							if(!error && response.statusCode == 200) {
								var exchangeRegResult = JSON.parse(exchangeRegBody);
								if(exchangeRegResult.errcode != 0 && exchangeRegResult.errcode != 40007) {	// 注册失败并且不是账号已注册错误时，则直接返回
									logger.error('兑换中心用户注册失败：');
									logger.error(exchangeRegResult);
									res.send({
										rsp_code: 'fail',
										error_code: 'system error',
										error_msg: exchangeRegResult.errmsg || '注册失败'
									});
									res.end();
									return;
								}

								var exchangeUserId = '';
								if(exchangeRegResult.errcode == 40007) {	// 兑换中心账号已存在
									logger.info('兑换中心账号已存在，继续再开放平台注册');
									exchangeUserId += exchangeRegResult.user_id;
								} else {
									exchangeUserId += exchangeRegResult.data.user_id;
								}

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
													sms_source : tokenValidData.data.member_id,
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
						});
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