var express = require('express');
var router = express.Router();
var url = require('url');
var request = require('request');
var ipware = require('ipware')();

var logger = require('../lib/log4js').getLogger('finance-router');
var appConfig = require('../app-config');
var errHandler = require('../lib/err-handler');
var bizUtils = require('../lib/biz-utils');

/*
 * 财务类接口
 * @param app_token app令牌
 * @param service 服务名称
 * @param params 业务参数集，json格式的字符串
 * @param sign 签名
 */
router.all('/', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if((typeof requestParams.app_token == 'undefined' && typeof requestParams.user_token == 'undefined') 
			|| typeof requestParams.service == 'undefined' || typeof requestParams.params == 'undefined' 
			|| typeof requestParams.sign == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}

		if(typeof requestParams.app_token != 'undefined') {
			// 验证app_token
			bizUtils.validAppToken(req, res, requestParams.app_token, function(tokenValidData) {
				// 根据返回的security_code校验签名
				if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
					errHandler.invalidSign(res);
					return;
				}

				if('finance.voucherRewardByMobile' == requestParams.service) {	// 根据手机号赠送礼券
					var bizParams = JSON.parse(requestParams.params);	// 请求中的业务参数
					// 将买家手机号填入out_trade_body中
					var out_trade_body = JSON.parse(bizParams.out_trade_body);
					out_trade_body.mobile = bizParams.buyer_mobile;
					bizParams.out_trade_body = JSON.stringify(out_trade_body);
					// 进行礼券赠送
					bizParams.buyer_id = '10000001';	//将买家id直接指定为每天惠
					logger.info('############# reward voucher start, mobile: '+ out_trade_body.mobile +' #############');
					var voucherRewardFormData = {	// 礼券赠送请求表单数据
						service : 'finance.orderReward',
						params : JSON.stringify(bizParams)
					};
					request.post(appConfig.financePath, {form: voucherRewardFormData}, function(error, response, body) {
						logger.info('############# reward voucher result: '+ body +' #############');
						if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
							res.send(JSON.parse(body));
						} else {
							errHandler.systemError(res, error);
						}
					});
				} else if('finance.prepayCardPay' == requestParams.service) {	// 亲情卡支付
					var bizParams = JSON.parse(requestParams.params);
					// STEP1: 根据亲情卡号查询会员id（即买家id）
					var cardFormData = {
						service : 'finance.storesPrepayCardScan',
						params : JSON.stringify({
							card_no : bizParams.card_no
						})
					};
					request.post(appConfig.financePath, {form: cardFormData}, function(error, response, cardBody) {
						logger.info('cardBody' + cardBody);
						try {
							if(!error && response.statusCode == 200) {
								var cardResult = JSON.parse(cardBody);
								if(cardResult.rsp_code == 'fail') {	// 查询亲情卡失败直接返回错误信息
									res.send(cardResult);
									return;
								}
								// 设置买家id
								bizParams.buyer_id = cardResult.data.member_id;
								// 支付方式固定余额支付
								bizParams.payment_way_key = 'ZFFS_05';
								// STEP2: 进行订单支付
								var payFormData = {
									service : 'finance.balancePay',
									params : JSON.stringify(bizParams)
								};
								request.post(appConfig.financePath, {form: payFormData}, function(error, response, payBody) {
									logger.info('payBody' + payBody);
									try {
										if(!error && response.statusCode == 200) {
											res.send(JSON.parse(payBody));
										} else {
											errHandler.systemError(res, error);
										}
									} catch(e) {
										errHandler.systemError(res, e);
									}
								});
							} else {
								errHandler.systemError(res, error);
							}
						} catch(e) {
							errHandler.systemError(res, e);
						}
					});
				} else {
					// 其他交易相关业务
					var formData = {
						service : requestParams.service,
						params : requestParams.params
					}
					if(requestParams.page) {
						formData.page = requestParams.page;
					}
					request.post(appConfig.financePath, {form: formData}, function(error, response, body) {
						try {
							if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
								res.send(JSON.parse(body));
							} else {
								errHandler.systemError(res, error);
							}
						} catch(e) {
							errHandler.systemError(res, e);
						}
					});
				}
			});
		} else {
			bizUtils.validUserToken(req, res, requestParams.user_token, function(tokenValidData) {
				// 根据返回的security_code校验签名
				if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
					errHandler.invalidSign(res);
					return;
				}

				if('finance.voucherRewardByMobile' == requestParams.service) {	// 根据手机号赠送礼券
					var bizParams = JSON.parse(requestParams.params);	// 请求中的业务参数
					// 将买家手机号填入out_trade_body中
					var out_trade_body = JSON.parse(bizParams.out_trade_body);
					out_trade_body.mobile = bizParams.buyer_mobile;
					bizParams.out_trade_body = JSON.stringify(out_trade_body);
					// 进行礼券赠送
					bizParams.buyer_id = '10000001';	//将买家id直接指定为每天惠
					logger.info('############# reward voucher start, mobile: '+ out_trade_body.mobile +' #############');
					var voucherRewardFormData = {	// 礼券赠送请求表单数据
						service : 'finance.orderReward',
						params : JSON.stringify(bizParams)
					};
					request.post(appConfig.financePath, {form: voucherRewardFormData}, function(error, response, body) {
						logger.info('############# reward voucher result: '+ body +' #############');
						if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
							res.send(JSON.parse(body));
						} else {
							errHandler.systemError(res, error);
						}
					});
				} else if('finance.prepayCardPay' == requestParams.service) {	// 亲情卡支付
					var bizParams = JSON.parse(requestParams.params);
					// STEP1: 根据亲情卡号查询会员id（即买家id）
					var cardFormData = {
						service : 'finance.storesPrepayCardScan',
						params : JSON.stringify({
							card_no : bizParams.card_no
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
								// 设置买家id
								bizParams.buyer_id = cardResult.data.member_id;
								// STEP2: 进行订单支付
								var payFormData = {
									service : 'finance.balancePay',
									params : JSON.stringify(bizParams)
								};
								request.post(appConfig.financePath, {form: payFormData}, function(error, response, payBody) {
									try {
										if(!error && response.statusCode == 200) {
											res.send(JSON.parse(payBody));
										} else {
											errHandler.systemError(res, error);
										}
									} catch(e) {
										errHandler.systemError(res, e);
									}
								});
							} else {
								errHandler.systemError(res, error);
							}
						} catch(e) {
							errHandler.systemError(res, e);
						}
					});
				} else if('finance.storeCashierPromotion' == requestParams.service) {	// 店东收银促销
					logger.info('店东收银促销参数');
					logger.info(JSON.stringify(requestParams));
					var bizParams = JSON.parse(requestParams.params);
					// 保存门店关联会员关系
					var formData = {
						service: 'stores.app.storesMemberRelCreate',
						params: JSON.stringify({
							stores_id: bizParams.seller_id,
							consumer_id: bizParams.buyer_id
						})
					};
					request.post(appConfig.memberPath, {form: formData});
					res.send({
						rsp_code: 'succ',
						data: {}
					});
					return;
					// var bizParams = JSON.parse(requestParams.params);
					// bizParams.out_trade_body = requestParams.params;

					// logger.info('店东收银促销参数');
					// logger.info(JSON.stringify(bizParams));
					
					// var formData = {
					// 	service: 'finance.storeCashierPromotion',
					// 	params: JSON.stringify(bizParams)
					// };
					// request.post(appConfig.financePath, {form: formData}, function(error, response, body) {
					// 	try {
					// 		if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
					// 			res.send(JSON.parse(body));
					// 		} else {
					// 			errHandler.systemError(res, error);
					// 		}
					// 	} catch(e) {
					// 		errHandler.systemError(res, e);
					// 	}
					// });
				} else if('finance.balanceCashier' == requestParams.service) {	// 余额收银
					var bizParams = JSON.parse(requestParams.params);
					bizParams.order_type_key = 'DDLX_17';
					var formData = {
						service: 'finance.balancePay',
						params: JSON.stringify(bizParams)
					};
					request.post(appConfig.financePath, {form: formData}, function(error, response, body) {
						try {
							if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
								res.send(JSON.parse(body));
							} else {
								errHandler.systemError(res, error);
							}
						} catch(e) {
							errHandler.systemError(res, e);
						}
					});
					// var bizParams = JSON.parse(requestParams.params);
					// bizParams.seller_id = '10000001';	// 卖家id指定为每天惠平台账户
					// var formData = {
					// 	service: 'finance.balancePay',
					// 	params: JSON.stringify(bizParams)
					// };
					// request.post(appConfig.financePath, {form: formData}, function(error, response, body) {
					// 	try {
					// 		if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
					// 			res.send(JSON.parse(body));
					// 		} else {
					// 			errHandler.systemError(res, error);
					// 		}
					// 	} catch(e) {
					// 		errHandler.systemError(res, e);
					// 	}
					// });
				} else if('voucher.app.voucherExchange' == requestParams.service) {	// 礼券兑换
					var bizParams = JSON.parse(requestParams.params);
					// 验证短信验证码
					var validSMSCodeFormData = {
						service : 'notification.validateCheckCode',
						params : JSON.stringify({
							mobile : bizParams.mobile,
							check_code : bizParams.sms_code
						})
					};
					request.post(appConfig.notificationPath, {form: validSMSCodeFormData}, function(error, response, validBody) {
						try {
							if(!error && response.statusCode == 200) {
								var validResult = JSON.parse(validBody);
								if(validResult.rsp_code == 'fail') {	// 短信验证码校验未通过，直接返回校验信息
									res.send(validResult);
									return;
								}
								// 调用礼券兑换接口
								delete bizParams.sms_code;
								var voucherExchangeFormData = {
									service: 'voucher.app.voucherExchange',
									params: JSON.stringify(bizParams)
								}
								request.post(appConfig.financePath, {form: voucherExchangeFormData}, function(error, response, exchangeBody) {
									try {
										if(!error && response.statusCode == 200) {
											res.send(JSON.parse(exchangeBody));
										} else {
											errHandler.systemError(res, error);
										}
									} catch(e) {
										errHandler.systemError(res, e);
									}
								});
							} else {
								errHandler.systemError(res, error);
							}
						} catch(e) {
							errHandler.systemError(res, e);
						}
					});
				} else {
					// 其他交易相关业务
					var formData = {
						service : requestParams.service,
						params : requestParams.params
					}
					if(requestParams.page) {
						formData.page = requestParams.page;
					}
					request.post(appConfig.financePath, {form: formData}, function(error, response, body) {
						try {
							if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
								res.send(JSON.parse(body));
							} else {
								errHandler.systemError(res, error);
							}
						} catch(e) {
							errHandler.systemError(res, e);
						}
					});
				}
			});
		}
	} catch(e) {
		errHandler.systemError(res, e);
	}
});

module.exports = router;