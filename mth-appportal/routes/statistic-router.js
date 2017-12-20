var express = require('express');
var router = express.Router();
var url = require('url');
var request = require('request');
var ipware = require('ipware')();
var eventproxy = require('eventproxy');

var logger = require('../lib/log4js').getLogger('statistic-router');
var appConfig = require('../app-config');
var errHandler = require('../lib/err-handler');
var bizUtils = require('../lib/biz-utils');

/*
 * 便利店获取本月动态
 * @param user_token 用户令牌
 * @param sign 签名
 */
router.all('/store/monthly_dynamic', function(req, res, next) {
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

			var result = {
				rsp_code: 'succ',
				data: {
					voucher: '0',
					reg: '0',
					card: '0',
					trade: '--'
				}
			};

			// TODO 查询本月动态，包括本月送券人数、本月推荐注册人数、本月开卡人数

			// 查询开卡人数
			var cardFormData = {
				service : 'finance.prepayCardActivateCount',
				params : JSON.stringify({
					stores_id : tokenValidData.data.member_id
				})
			};
			request.post(appConfig.financePath, {form: cardFormData}, function(error, response, cardBody) {
				try {
					if(!error && response.statusCode == 200) {
						var cardData = JSON.parse(cardBody);
						if(cardData.rsp_code == 'succ') {	// 查询成功，将开卡人数计入返回结果中
							result.data.card = cardData.data.count_num;
						}

						// 查询本月送券人数
						var date = new Date();
						var voucherFormData = {
							service : 'finance.storeVoucherRewardAccountCount',
							params : JSON.stringify({
								member_id : tokenValidData.data.member_id,
								transaction_date_start : bizUtils.getCurrentYearMonth() + '-01',
								transaction_date_end : bizUtils.getCurrentDate()
							})
						};
						request.post(appConfig.financePath, {form: voucherFormData}, function(error, response, voucherBody) {
							try {
								if(!error && response.statusCode == 200) {
									var voucherData = JSON.parse(voucherBody);
									if(voucherData.rsp_code == 'succ') {	// 查询成功，将送券人数计入返回结果中
										result.data.voucher = voucherData.data.count_num;
									}

									// 查询本月推荐注册人数
									var regFormData = {
										service : 'member.memberRegisterRecommendCount',
										params : JSON.stringify({
											reference_type_key : 'stores',
											reference_id : tokenValidData.data.member_id
										})
									};
									request.post(appConfig.memberPath, {form: regFormData}, function(error, response, regBody) {
										try {
											if(!error && response.statusCode == 200) {
												var regData = JSON.parse(regBody);
												if(regData.rsp_code == 'succ') {
													result.data.reg = regData.data.count_num;
												}

												// 查询交易数量
												var tradeFormData = {
													service : 'finance.tradeConsumerListForStores',
													params : JSON.stringify({
														stores_id : tokenValidData.data.member_id
													})
												};
												request.post(appConfig.financePath, {form: tradeFormData}, function(error, response, tradeBody) {
													try {
														if(!error && response.statusCode == 200) {
															var tradeData = JSON.parse(tradeBody);
															if(tradeData.rsp_code == 'succ') {
																result.data.trade = tradeData.data.list.length || '0';
															}

															// 返回最终查询结果
															res.send(result);
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
 * 消费者查询亲情卡和优惠券数量
 * @param user_token 用户令牌
 * @param sign 签名
 */
router.all('/consumer/card_coupon', function(req, res, next) {
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

			// 返回结果集
			var result = {
				rsp_code: 'succ',
				data: {
					card_num: '--',
					coupon_num: '--',
					lucky_money_num: '--',
					benefit_num: '--'
				}
			};

			var ep = new eventproxy();
			ep.all('card_num', 'coupon_num', 'lucky_money_num', 'benefit_num', function() {
				res.send(result);
			});

			// 查询亲情卡数量
			var cardFormData = {
				service : 'finance.consumerPrepayCardCount',
				params : JSON.stringify({
					member_id : tokenValidData.data.member_id
				})
			};
			request.post(appConfig.financePath, {form: cardFormData}, function(error, response, cardBody) {
				try {
					if(!error && response.statusCode == 200) {
						var cardData = JSON.parse(cardBody);
						if(cardData.rsp_code == 'succ') {
							result.data.card_num = cardData.data.count_num;
						}
						ep.emit('card_num');
					} else {
						errHandler.systemError(res, error);
						return;
					}
				} catch(e) {
					errHandler.systemError(res, e);
					return;
				}
			});

			// 查询优惠券数量
			var couponFormData = {
				service : 'finance.memberCouponCount',
				params : JSON.stringify({
					member_id : tokenValidData.data.member_id,
					status : 'activated'
				})
			};
			request.post(appConfig.financePath, {form: couponFormData}, function(error, response, couponBody) {
				try {
					if(!error && response.statusCode == 200) {
						var couponData = JSON.parse(couponBody);
						if(couponData.rsp_code == 'succ') {
							result.data.coupon_num = couponData.data.count_num;
						}
						ep.emit('coupon_num');
					} else {
						errHandler.systemError(res, error);
						return;
					}
				} catch(e) {
					errHandler.systemError(res, e);
					return;
				}
			});

			// 查询红包数量
			var luckyFormData = {
				service : 'goods.gcActivityDetailCount',
				params : JSON.stringify({
					member_id : tokenValidData.data.member_id,
					member_type_key : 'consumer'
				})
			};
			request.post(appConfig.goodsPath, {form: luckyFormData}, function(error, response, luckyBody) {
				try {
					if(!error && response.statusCode == 200) {
						var luckyData = JSON.parse(luckyBody);
						if(luckyData.rsp_code == 'succ') {
							result.data.lucky_money_num = luckyData.data.count_num;
						}
						ep.emit('lucky_money_num');
					} else {
						errHandler.systemError(res, error);
						return;
					}
				} catch(e) {
					errHandler.systemError(res, e);
					return;
				}
			});

			// 查询权益券数量
			var benefitFormData = {
				// service : 'gdBenefit.consumer.usableGdBenefitCount',
				// service : 'gdActivity.consumer.gdActivityCount',
				service : 'gdActivity.consumer.gdActivityCountFind',
				params : JSON.stringify({
					member_id : tokenValidData.data.member_id
				})
			};
			request.post(appConfig.goodsPath, {form: benefitFormData}, function(error, response, benefitBody) {
				try {
					if(!error && response.statusCode == 200) {
						var benefitData = JSON.parse(benefitBody);
						if(benefitData.rsp_code == 'succ') {
							result.data.benefit_num = benefitData.data.count_num;
						}
						ep.emit('benefit_num');
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
 * 会员解冻信息统计
 * @param user_token 		用户令牌
 * @param member_id			会员id
 * @param member_type_key	会员类型
 * @param sign 				签名
 */
router.all('/member/unfreeze_info', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(!requestParams.user_token || !requestParams.member_id || !requestParams.member_type_key || !requestParams.sign) {
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

			var finalResult = {
				rsp_code: 'succ',
				data: {}
			};

			// STEP1:根据member_id查询会员资产
			var assetFormData = {
				service: 'finance.memberAssetFind',
				params: JSON.stringify({
					member_id : requestParams.member_id
				})
			};
			request.post(appConfig.financePath, {form: assetFormData}, function(error, response, assetBody) {
				try {
					if(!error && response.statusCode == 200) {
						var assetResult = JSON.parse(assetBody);
						if(assetResult.rsp_code == 'fail') {
							res.send(assetResult);
							return;
						}
						finalResult.data.cash_balance = assetResult.data.cash_balance;	// 将领取余额放入最终返回结果中
						finalResult.data.cash_froze = assetResult.data.cash_froze;	// 将领取冻结金额放入最终返回结果中
						finalResult.data.gold = assetResult.data.gold;	// 将金币数量放入最终返回结果中

						// STEP2:根据member_id和member_type查询领样数和违规数
						var numFormData = {
							service: 'order.fgOrderViolationCount',
							params: JSON.stringify({
								member_id: requestParams.member_id,
								member_type_key: requestParams.member_type_key
							})
						};
						request.post(appConfig.opsorderPath, {form: numFormData}, function(error, response, numBody) {
							try {
								if(!error && response.statusCode == 200) {
									var numResult = JSON.parse(numBody);
									if(numResult.rsp_code == 'fail') {
										res.send(numResult);
										return;
									}
									finalResult.data.total_num = numResult.data.total_num;	// 将领样记录数放入最终返回结果中
									finalResult.data.violation_num = numResult.data.violation_num;	// 将违规记录数放入最终返回结果中

									// STEP3:根据member_id和member_type查询会员手机号
									var mobileFormData = {
										service: 'member.memberInfoFindByMemberId',
										params: JSON.stringify({
											member_id: requestParams.member_id,
											member_type_key: requestParams.member_type_key
										})
									};
									request.post(appConfig.memberPath, {form: mobileFormData}, function(error, response, mobileBody) {
										try {
											if(!error && response.statusCode == 200) {
												var mobileResult = JSON.parse(mobileBody);
												if(mobileResult.rsp_code == 'fail') {
													res.send(mobileResult);
													return;
												}
												finalResult.data.mobile = mobileResult.data.mobile;	// 将会员手机号放入最终返回结果中

												// 返回最终查询结果
												res.send(finalResult);
												return;
											} else {
												errHandler.systemError(res, error);
												return;
											}
										} catch(e) {
											errHandler.systemError(res, e);
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
