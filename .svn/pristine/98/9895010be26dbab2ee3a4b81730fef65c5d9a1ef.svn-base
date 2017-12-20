var express = require('express');
var router = express.Router();
var url = require('url');
var request = require('request');
var ipware = require('ipware')();
var EventProxy = require('eventproxy');

var logger = require('../lib/log4js').getLogger('order-router');
var appConfig = require('../app-config');
var errHandler = require('../lib/err-handler');
var bizUtils = require('../lib/biz-utils');

/*
 * 订单类接口
 * @param user_token 用户令牌
 * @param service 服务名称
 * @param params 业务参数集，json格式的字符串
 * @param sign 签名
 */
router.all('/', function(req, res, next) {
	try {

		// ----暂时停用礼券兑换功能----
		res.send({
			rsp_code: 'fail',
			error_code: 'system error',
			error_msg: '抱歉，应用正在测试阶段，暂不支持该功能！'	// 返回创建订单失败的原因
		});
		res.end();
		return;
		// ----------------------------


		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证公共参数是否完整
		if(typeof requestParams.user_token == 'undefined' || typeof requestParams.service == 'undefined'
			|| typeof requestParams.params == 'undefined' || typeof requestParams.sign == 'undefined') {
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

			// 创建订单
			try {
				if(requestParams.service == 'order.createOrder') {
					// 业务参数
					var bizParams = JSON.parse(requestParams.params);

					// 判断业务参数是否完整
					if(typeof bizParams.member_id == 'undefined' || typeof bizParams.addr_id == 'undefined' || typeof bizParams.source_from == 'undefined' 
						|| typeof bizParams.is_fast_buy == 'undefined' || typeof bizParams.shop_id == 'undefined' || typeof bizParams.logistics_no == 'undefined') {
						errHandler.incompleteParams(res);
						return;
					}

					// 验证订单来源参数
					if(bizParams.source_from != 'app' && bizParams.source_from != 'pc') {
						errHandler.invalidParams(res);
						return;
					}

					// 验证是否立即购买参数
					if(bizParams.is_fast_buy != '0' && bizParams.is_fast_buy != '1') {
						errHandler.invalidParams(res);
						return;
					}

					// 非立即购买必须传购物车id
					if(bizParams.is_fast_buy == '0') {
						if(typeof bizParams.cart_id == 'undefined') {
							errHandler.incompleteParams(res);
							return;
						}
					}

					// 店铺id数组
					var shopIdArr = bizParams.shop_id.split(',');
					// 物流编号数组
					var logisticsNoArr = bizParams.logistics_no.split(',');

					// 店铺id数组和物流编号数组长度必须一致，保证一一对应
					if(shopIdArr.length != logisticsNoArr.length) {
						errHandler.invalidParams(res);
						return;
					}

					// 订单生成请求URL
					var creatOrderUrl = appConfig.exchangeCenterHost 
						+ '?access_token=' + requestParams.user_token + '&method=user_order.generate'
						+ '&user_id=' + tokenValidData.data.user_id
						+ '&addr_id=' + bizParams.addr_id
						+ '&source_from=' + bizParams.source_from;
					if(bizParams.is_fast_buy == '1') {	// 立即购买则加上mode
						creatOrderUrl += '&mode=fastbuy';
					} else {							// 否则加上购物车id
						creatOrderUrl += '&cart_id=' + bizParams.cart_id;
					}

					// 拼装物流信息参数
					for(var i=0; i<shopIdArr.length; i++) {
						creatOrderUrl += '&shipping['+ shopIdArr[i] +'][template_id]=' + logisticsNoArr[i];
					}

					request.get(creatOrderUrl, function(error, response, body) {
						if(!error && response.statusCode == 200) {
							var createOrderResult = JSON.parse(body);
							if(createOrderResult.errcode != 0) {	// 创建订单失败
								logger.error('创建订单失败！失败原因：' + createOrderResult.errmsg);
								res.send({
									rsp_code: 'fail',
									error_code: 'system error',
									error_msg: createOrderResult.errmsg || '系统错误'	// 返回创建订单失败的原因
								});
								res.end();
								return;
							}

							// 多个订单循环构建扣减礼券请求参数
							var formDataArr = [];
							var totalAmount = 0;
							for(var orderNo in createOrderResult.data) {
								var formData = {
									service : 'finance.orderPay',
									params : JSON.stringify({
										data_source : 'SJLY_01',
										detail : '礼券兑换',
										amount : createOrderResult.data[orderNo].payment,
										out_trade_no : orderNo,
										payment_way_key : 'ZFFS_06',
										buyer_id : bizParams.member_id,
										seller_id : '10000001'	// 卖家固定为每天惠
										// seller_id : createOrderResult.data[orderNo].shop_id
									})
								};
								formDataArr.push(formData);
								totalAmount += parseFloat(createOrderResult.data[orderNo].payment);
							}

							// TODO 判断账户礼券是否足够
							var queryVoucherData = {
								service : 'finance.memberAssetFind',
								params : JSON.stringify({
									member_id : bizParams.member_id
								})
							};

							// 查询会员账户礼券余额
							request.post(appConfig.financePath, {form: queryVoucherData}, function(error, response, body) {
								if(!error && response.statusCode == 200) {
									var queryVoucherResult = JSON.parse(body);
									if(queryVoucherResult.rsp_code == 'succ') {	// 查询礼券余额成功
										if(parseFloat(queryVoucherResult.data.voucher_balance) < totalAmount) {
											res.send({
												rsp_code : 'fail',
												error_code : 'not enough voucher',
												error_msg : '会员账户礼券余额不足!'
											});
										} else {	// 礼券账户余额足够
											var ep = new EventProxy();

											// 调用扣减礼券接口
											for(var i=0; i<formDataArr.length; i++) {
												request.post(appConfig.financePath, {form: formDataArr[i]}, function(error, response, body) {
													if(!error && response.statusCode == 200) {
														ep.emit('pay_voucher', JSON.parse(body));
													} else {
														logger.error('pay voucher error:' + error);
														ep.emit('pay_voucher', '-1');
													}
												});
											}

											// 调用扣减礼券完成
											ep.after('pay_voucher', formDataArr.length, function(list) {
												// 确认订单状态
												var payedOrderArr = [];
												for(var i=0; i<list.length; i++) {
													if(list[i] != '-1' && list[i].rsp_code == 'succ') {	// 订单礼券扣减成功
														payedOrderArr.push(list[i].data.out_trade_no);
													}
												}
												if(payedOrderArr.length != 0) {	// 至少有一个订单支持成功，则调用确认订单状态接口
													// 确认订单状态请求URL
													var confirmOrderUrl = appConfig.exchangeCenterHost
														+ '?access_token=' + requestParams.user_token + '&method=user_order.pay'
														+ '&tid=' + payedOrderArr.join(',');

													// 不关注确认订单状态的结果
													request.get(confirmOrderUrl, function(error, response, body) {
														if(error) {
															logger.error('confirm order status error:' + error);
														}
													});

													// 礼券扣减完成后直接返回成功，不关注订单状态是否修改成功
													res.send({
														rsp_code: 'succ',
														data: {}
													});
													return;
												} else {	// 一个订单都没支付成功
													logger.error('all orders confirm error');
													res.send({
														rsp_code : 'fail',
														error_code : 'pay order fail',
														error_msg : '订单支付失败！'
													});
												}
											});
										}
									} else {
										logger.error('query consumer voucher balance error');
										res.send({
											rsp_code : 'fail',
											error_code : 'query member voucher fail',
											error_msg : queryVoucherResult.data.error_msg || '查询会员余额失败！'
										});
										return;
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