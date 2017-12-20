/*
 * 惠易定专用
 * @author Changfeng
 */
var express = require('express');
var router = express.Router();
var url = require('url');
var request = require('request');
var ipware = require('ipware')();
var md5 = require('md5');

var logger = require('../lib/log4js').getLogger('hyd-router');
var appConfig = require('../app-config');
var errHandler = require('../lib/err-handler');
var bizUtils = require('../lib/biz-utils');

// 惠易定账号ID
var HYD_ACCOUNT_ID = '10000002';
// 数据来源--惠易定
var DATA_SOURCE_HYD = 'SJLY_06';
// 支付方式--惠金币
var PAY_TYPE_GOLD = 'ZFFS_08';

/*
 * 加盟店会员信息同步
 * @param app_token	app令牌
 * @param params	业务参数集（JSON格式字符串）
 * @param sign 		签名
 */
router.all('/store_reg_sync', function(req, res, next) {
	try {
		logger.info('【加盟店会员信息同步】');
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		logger.info('【请求参数】' + JSON.stringify(requestParams));

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.params == 'undefined' || typeof requestParams.sign == 'undefined') {
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

			// 调用同步接口
			var syncFormData = {
				service : 'member.storeSyncRegisterForHYD',
				params : requestParams.params
			};
			request.post(appConfig.memberPath, {form: syncFormData}, function(error, response, body) {
				if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
					logger.info('【返回结果】' + body);
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
 * 供应商会员信息同步
 * @param app_token	app令牌
 * @param params	业务参数集（JSON格式字符串）
 * @param sign 		签名
 */
router.all('/supplier_reg_sync', function(req, res, next) {
	try {
		logger.info('【供应商会员信息同步】');
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		logger.info('【请求参数】' + JSON.stringify(requestParams));

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.params == 'undefined' || typeof requestParams.sign == 'undefined') {
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

			// 调用同步接口
			var syncFormData = {
				service : 'member.supplierSyncRegisterForHYD',
				params : requestParams.params
			};
			request.post(appConfig.memberPath, {form: syncFormData}, function(error, response, body) {
				if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
					logger.info('【返回结果】' + body);
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
 * 系统赠送金币给店东
 * @param app_token	app令牌
 * @param params	业务参数集(JSON格式字符串。包括：detail, amount, mobile)
 * @param sign 		签名
 */
router.all('/system_gold_reward', function(req, res, next) {
	try {
		logger.info('【系统赠送金币给店东】');
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		logger.info('【请求参数】' + JSON.stringify(requestParams));

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.params == 'undefined' || typeof requestParams.sign == 'undefined') {
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

			var bizParams = JSON.parse(requestParams.params);

			// 校验手机号是否缺失
			if(typeof bizParams.mobile == 'undefined') {
				errHandler.incompleteParams(res);
				return;
			}
			// 根据手机号查询店东id
			var memberIdFormData = {
				service : 'infrastructure.memberFindByMobile',
				params : JSON.stringify({
					mobile : bizParams.mobile,
					member_type_key : 'stores'
				})
			}
			request.post(appConfig.userPath, {form: memberIdFormData}, function(error, response, memberBody) {
				if(!error && response.statusCode == 200) {
					var memberData = JSON.parse(memberBody);
					if(memberData.rsp_code == 'fail') {	// 店东不存在
						res.send(memberData);
						return;
					}

					bizParams.buyer_id = HYD_ACCOUNT_ID;					// 惠易定id
					bizParams.seller_id = memberData.data.member_id;		// 店东id
					bizParams.data_source = DATA_SOURCE_HYD;				// 数据来源
					bizParams.out_trade_no = bizUtils.getTradeNo();			// 交易号
					bizParams.payment_way_key = PAY_TYPE_GOLD;				// 支付方式--惠金币
					bizParams.out_trade_body = JSON.stringify(bizParams);

					// 调用余额支付接口
					var balancePayFormData = {
						service : 'finance.balancePay',
						params : JSON.stringify(bizParams)
					};
					request.post(appConfig.financePath, {form: balancePayFormData}, function(error, response, body) {
						if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
							logger.info('【返回结果】' + body);
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
 * 抽奖扣减
 * @param app_token	app令牌
 * @param params	业务参数集(JSON格式字符串。包括：detail, payment_way_key, amount, mobile)
 * @param sign 		签名
 */
router.all('/lottery_reduce', function(req, res, next) {
	try {
		logger.info('【抽奖扣减】');
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		logger.info('【请求参数】' + JSON.stringify(requestParams));

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.params == 'undefined' || typeof requestParams.sign == 'undefined') {
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

			var bizParams = JSON.parse(requestParams.params);

			// 校验手机号是否缺失
			if(typeof bizParams.mobile == 'undefined') {
				errHandler.incompleteParams(res);
				return;
			}
			// 根据手机号查询店东id
			var memberIdFormData = {
				service : 'infrastructure.memberFindByMobile',
				params : JSON.stringify({
					mobile : bizParams.mobile,
					member_type_key : 'stores'
				})
			}
			request.post(appConfig.userPath, {form: memberIdFormData}, function(error, response, memberBody) {
				if(!error && response.statusCode == 200) {
					var memberData = JSON.parse(memberBody);
					if(memberData.rsp_code == 'fail') {	// 店东不存在
						res.send(memberData);
						return;
					}

					bizParams.buyer_id = memberData.data.member_id;			// 店东id
					bizParams.seller_id = HYD_ACCOUNT_ID;					// 惠易定id
					bizParams.data_source = DATA_SOURCE_HYD;				// 数据来源
					bizParams.out_trade_body = JSON.stringify(bizParams);

					// 调用抽奖扣减接口
					var lotteryReduceFormData = {
						service : 'finance.lotteryReduce',
						params : JSON.stringify(bizParams)
					};
					request.post(appConfig.financePath, {form: lotteryReduceFormData}, function(error, response, body) {
						if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
							logger.info('【返回结果】' + body);
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
 * 抽奖赠送
 * @param app_token	app令牌
 * @param params	业务参数集（JSON格式字符串。包括：detail, payment_way_key, amount, mobile)
 * @param sign 		签名
 */
router.all('/lottery_reward', function(req, res, next) {
	try {
		logger.info('【抽奖赠送】');
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		logger.info('【请求参数】' + JSON.stringify(requestParams));

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.params == 'undefined' || typeof requestParams.sign == 'undefined') {
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

			var bizParams = JSON.parse(requestParams.params);

			// 校验手机号是否缺失
			if(typeof bizParams.mobile == 'undefined') {
				errHandler.incompleteParams(res);
				return;
			}
			// 根据手机号查询店东id
			var memberIdFormData = {
				service : 'infrastructure.memberFindByMobile',
				params : JSON.stringify({
					mobile : bizParams.mobile,
					member_type_key : 'stores'
				})
			}
			request.post(appConfig.userPath, {form: memberIdFormData}, function(error, response, memberBody) {
				if(!error && response.statusCode == 200) {
					var memberData = JSON.parse(memberBody);
					if(memberData.rsp_code == 'fail') {	// 店东不存在
						res.send(memberData);
						return;
					}

					bizParams.buyer_id = memberData.data.member_id;			// 店东id
					bizParams.seller_id = HYD_ACCOUNT_ID;					// 惠易定id
					bizParams.data_source = DATA_SOURCE_HYD;				// 数据来源
					bizParams.out_trade_body = JSON.stringify(bizParams);

					// 调用抽奖扣减接口
					var lotteryRewardFormData = {
						service : 'finance.lotteryReward',
						params : JSON.stringify(bizParams)
					};
					request.post(appConfig.financePath, {form: lotteryRewardFormData}, function(error, response, body) {
						if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
							logger.info('【返回结果】' + body);
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
 * 会员资产查询
 * @param app_token	app令牌
 * @param params	业务参数集(JSON格式字符串。包括：mobile)
 * @param sign 		签名
 */
router.all('/asset_query', function(req, res, next) {
	try {
		logger.info('【会员资产查询】');
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		logger.info('【请求参数】' + JSON.stringify(requestParams));

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.params == 'undefined' || typeof requestParams.sign == 'undefined') {
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

			var bizParams = JSON.parse(requestParams.params);

			// 校验手机号是否缺失
			if(typeof bizParams.mobile == 'undefined') {
				errHandler.incompleteParams(res);
				return;
			}
			// 根据手机号查询店东id
			var memberIdFormData = {
				service : 'infrastructure.memberFindByMobile',
				params : JSON.stringify({
					mobile : bizParams.mobile,
					member_type_key : 'stores'
				})
			}
			request.post(appConfig.userPath, {form: memberIdFormData}, function(error, response, memberBody) {
				if(!error && response.statusCode == 200) {
					var memberData = JSON.parse(memberBody);
					if(memberData.rsp_code == 'fail') {	// 店东不存在
						res.send(memberData);
						return;
					}

					// 调用会员资产查询接口
					var assetQueryFormData = {
						service : 'finance.memberAssetQuery',
						params : JSON.stringify({
							member_id : memberData.data.member_id
						})
					};
					request.post(appConfig.financePath, {form: assetQueryFormData}, function(error, response, body) {
						if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
							logger.info('【返回结果】' + body);
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
 * 根据店东手机号查询店东信息
 * @param app_token	app令牌
 * @param params	业务参数集(JSON格式字符串。包括：mobile)
 * @param sign 		签名
 */
router.all('/store_find', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.params == 'undefined' || typeof requestParams.sign == 'undefined') {
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

			var bizParams = JSON.parse(requestParams.params);

			// 校验手机号是否缺失
			if(typeof bizParams.mobile == 'undefined') {
				errHandler.incompleteParams(res);
				return;
			}
			// 根据手机号查询店东详情
			var storeFindFormData = {
				service : 'member.storeFindByMobile',
				params : JSON.stringify({
					mobile : bizParams.mobile
				})
			}
			request.post(appConfig.memberPath, {form: storeFindFormData}, function(error, response, body) {
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

module.exports = router;