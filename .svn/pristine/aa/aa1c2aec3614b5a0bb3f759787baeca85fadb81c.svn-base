var express = require('express');
var router = express.Router();
var url = require('url');
var request = require('request');
var ipware = require('ipware')();
var eventproxy = require('eventproxy');

var logger = require('../lib/log4js').getLogger('opsorder-router');
var appConfig = require('../app-config');
var errHandler = require('../lib/err-handler');
var bizUtils = require('../lib/biz-utils');

/*
 * 预售订单类接口
 * @param app_token / user_token 令牌（运营管理系统调用传递app_token，APP调用传递user_token）
 * @param service 服务名称
 * @param params 业务参数集，json格式的字符串
 * @param page 分页参数集，json格式字符串（非必填）
 * @param sign 签名
 */
router.all('/', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if((typeof requestParams.app_token == 'undefined' && typeof requestParams.user_token == 'undefined') 
			|| typeof requestParams.service == 'undefined' || typeof requestParams.params == 'undefined' || typeof requestParams.sign == 'undefined') {
			errHandler.incompleteParams(res);
			return;
		}

		if(typeof requestParams.app_token != 'undefined') {	// 只传递了app_token，给管理系统专用
			// 验证app_token
			bizUtils.validAppToken(req, res, requestParams.app_token, function(tokenValidData) {
				// 根据返回的security_code校验签名
				if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
					errHandler.invalidSign(res);
					return;
				}
				// 进行订单相关业务
				var formData = {
					service : requestParams.service,
					params : requestParams.params
				};
				if(typeof requestParams.page != 'undefined') {	// 若请求有传递分页参数
					formData.page = requestParams.page;
				}
				request.post(appConfig.opsorderPath, {form: formData}, function(error, response, body) {
					if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
						res.send(JSON.parse(body));
					} else {
						errHandler.systemError(res, error);
						return;
					}
				});
			});
		} else {	// 传递了user_token，给APP使用
			// 验证user_token
			bizUtils.validUserToken(req, res, requestParams.user_token, function(tokenValidData) {
				// 根据返回的security_code校验签名
				if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
					errHandler.invalidSign(res);
					return;
				}

				if(requestParams.service == 'order.psOrderAdd') {	// 预售订单新增，临时处理价格传错的问题

					logger.info('############# psOrderAdd start #############');
					logger.info(requestParams);

					var bizParams = JSON.parse(requestParams.params);
					if(parseFloat(bizParams.total_fee) > parseFloat(bizParams.sale_fee)) {	// 如果实际支付价格大于销售价格，则说明参数传错，进行替换

						logger.info('############# switch total_fee and sale_fee #############');

						var tmpFee1 = bizParams.total_fee;
						bizParams.total_fee = bizParams.sale_fee;
						bizParams.sale_fee = tmpFee1;

						var psGoodsJson = bizParams.ps_goods;
						if(typeof bizParams.ps_goods == 'string') {

							logger.info('---- parse ps_goods ----');

							psGoodsJson = JSON.parse(bizParams.ps_goods);
						}
						if(parseFloat(psGoodsJson.total_fee) > parseFloat(psGoodsJson.sale_price)) {	// 如果实际支付价格大于销售价格，则说明参数传错，进行替换
							
							logger.info('############# switch total_fee and sale_price #############');

							var tmpFee2 = psGoodsJson.total_fee;
							psGoodsJson.total_fee = psGoodsJson.sale_price;
							psGoodsJson.sale_price = tmpFee2;
						}

						bizParams.ps_goods = JSON.stringify(psGoodsJson);
						requestParams.params = JSON.stringify(bizParams);

						logger.info('############# all switch over #############');
						logger.info(requestParams);
					}

					// 进行订单相关业务
					var formData = {
						service : requestParams.service,
						params : requestParams.params
					};
					if(typeof requestParams.page != 'undefined') {	// 若请求有传递分页参数
						formData.page = requestParams.page;
					}
					request.post(appConfig.opsorderPath, {form: formData}, function(error, response, body) {
						if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
							res.send(JSON.parse(body));
						} else {
							errHandler.systemError(res, error);
							return;
						}
					});
				} else if(requestParams.service == 'order.consumerFreeGetRecordCreate') {	// 消费者超级返取商品记录创建
					var ReqBizParams = JSON.parse(requestParams.params);
					var ep = new eventproxy();

					// 查询金币余额
					var goldFormData = {
						service : 'finance.memberAssetFind',
						params : JSON.stringify({
							member_id : ReqBizParams.member_id
						})
					};
					request.post(appConfig.financePath, {form: goldFormData}, function(error, response, goldBody) {
						try {
							if(!error && response.statusCode == 200) {
								var goldData = JSON.parse(goldBody);
								if(goldData.rsp_code == 'fail') {	// 查询用户金币余额失败
									ep.unbind();
									res.send(goldData);
									return;
								}
								if(parseInt(goldData.data.gold) < 0) {	// 金币余额为负
									ep.unbind();
									res.send({
										rsp_code: 'fail',
										error_code: 'balance_not_enough',
										error_msg: '可用金币余额不足'
									});
									return;
								}
								ep.emit('validGold');
							} else {
								ep.unbind();
								errHandler.systemError(res, error);
							}
						} catch(e) {
							ep.unbind();
							errHandler.systemError(res, e);
						}
					});

					// 查询手机号码
					ep.once('validGold', function(data) {
						var userFormData = {
							service : 'member.memberInfoFindByMemberId',
							params : JSON.stringify({
								member_id : ReqBizParams.member_id,
								member_type_key : 'consumer'
							})
						};
						request.post(appConfig.memberPath, {form: userFormData}, function(error, response, userBody) {
							try {
								if(!error && response.statusCode == 200) {
									var userResult = JSON.parse(userBody);
									if(userResult.rsp_code == 'fail') {	// 手机号查询失败
										ep.unbind();
										res.send(userResult);
										return;
									}
									ep.emit('queryMobile', userResult.data);
								} else {
									ep.unbind();
									errHandler.systemError(res, error);
								}
							} catch(e) {
								ep.unbind();
								errHandler.systemError(res, e);
							}
						});
					});

					// 创建淘淘领订单
					ep.all('queryMobile', function(data) {
						var createFormData = {
							service : 'order.consumerFreeGetRecordCreate',
							params : JSON.stringify({
								goods_code : ReqBizParams.goods_code,
								consumer_id : ReqBizParams.member_id,
								recommend_stores_id : ReqBizParams.recommend_stores_id || tokenValidData.data.member_id,
								mobile : data.mobile
								// mobile: ReqBizParams.mobile
							})
						};
						request.post(appConfig.opsorderPath, {form: createFormData}, function(error, response, createBody) {
							if(!error && response.statusCode == 200) {
								var createResult = JSON.parse(createBody);
								if(createResult.rsp_code == 'succ') {	// 创建成功
									// 发送APP通知,不影响流程
									var storeName = ReqBizParams.stores_name ? ('【' + ReqBizParams.stores_name + '】') : '';
									var appNotifyFormData = {
										service : 'member.appMsgNotify',
										params : JSON.stringify({
											receiver : ReqBizParams.member_id,
											message : '感谢您参加'+ storeName +'超级返活动，请按如下指引完成订单：' + decodeURIComponent(ReqBizParams.goods_url)
										})
									};
									request.post(appConfig.memberPath, {form: appNotifyFormData});
								}
								res.send(createResult);
							} else {
								errHandler.systemError(res, error);
							}
						});
					});
				} else if(requestParams.service == 'order.autoSellCountForStoresSale') {	// 自动售货机统计
					// TODO
					res.send({
						rsp_code: 'succ',
						data: {
							yesterday_sell_amount: '0.00',
							month_sell_amount: '0.00',
							totle_amount: '0.00',
							device_num: '0'
						}
					});
				} else if(requestParams.service == 'order.wypCooperationStoresSaleCount') {	// 我要批统计
					// TODO
					res.send({
						rsp_code: 'succ',
						data: {
							cooperate_stores: '0',
							yesterday_sell_amount: '0.00',
							month_sell_amount: '0.00',
							totle_amount: '0.00'
						}
					});
				} else if(requestParams.service == 'order.saleAssistantSaleCount') {	// 社区导购统计
					// TODO
					res.send({
						rsp_code: 'succ',
						data: {
							cooperate_member: '0',
							month_sell_amount: '0.00',
							totle_amount: '0.00',
							totle_rebate: '0.00'
						}
					});
				} else {
					// 进行订单相关业务
					var formData = {
						service : requestParams.service,
						params : requestParams.params
					};
					if(typeof requestParams.page != 'undefined') {	// 若请求有传递分页参数
						formData.page = requestParams.page;
					}
					request.post(appConfig.opsorderPath, {form: formData}, function(error, response, body) {
						if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
							res.send(JSON.parse(body));
						} else {
							errHandler.systemError(res, error);
							return;
						}
					});
				}
			});
		}
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

module.exports = router;