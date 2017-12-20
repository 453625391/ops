var express = require('express');
var router = express.Router();
var url = require('url');
var request = require('request');
var ipware = require('ipware')();
var parseString = require('xml2js').parseString;

var logger = require('../../lib/log4js').getLogger('djk-router');
var appConfig = require('../../app-config');
var errHandler = require('../../lib/err-handler');
var bizUtils = require('../../lib/biz-utils');

/*
 * 进入手机充值页面
 * @param member_id	用户id(消费者/店东)
 */
router.get('/mobile-fee', function(req, res, next) {
	try {

		// 春节期间暂停服务
		res.render('common/mobile-fee/outofservice');
		return;

		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		// 查询充值记录
		var recordsFormData = {
			service: 'csOrder.app.phoneBillOrderPageListFind',
			params: JSON.stringify({
				member_id: requestParams.member_id,
				member_type_key: requestParams.member_type_key
			})
		};
		request.post(appConfig.opsorderPath, {form: recordsFormData}, function(error, response, recordsBody) {
			try {
				if(!error && response.statusCode == 200) {
					var recordsResult = JSON.parse(recordsBody);
					res.render('common/mobile-fee/index', {
						recordsList : recordsResult.rsp_code == 'succ' ? recordsResult.data.list : []
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
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

/*
 * 获取手机号归属地
 * @param mobile 手机号
 */
router.post('/mobile-fee/attribution', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 查询归属地
		var attributionFormData = {
			service: 'recharger.app.mobileAttribution',
			params: JSON.stringify({
				mobile: requestParams.mobile
			})
		};
		request.post(appConfig.opsorderPath, {form: attributionFormData}, function(error, response, attributionBody) {
			try {
				if(!error && response.statusCode == 200) {
					var attributionResult = JSON.parse(attributionBody);
					if(attributionResult.rsp_code == 'fail') {	// 查询归属地失败
						res.send({
							rsp_code: 'fail',
							error_code: '归属地查询失败'
						})
						return;
					}

					// 查询充值商品列表
					var goodsFormData = {
						service: 'recharger.app.mobileRechargeTypeListFind',
						params: JSON.stringify({
							label: attributionResult.data.yysType
						})
					};
					request.post(appConfig.goodsPath, {form: goodsFormData}, function(error, response, goodsBody) {
						try {
							if(!error && response.statusCode == 200) {
								var goodsResult = JSON.parse(goodsBody);
								if(goodsResult.rsp_code == 'fail') {	// 充值商品查询失败
									res.send({
										rsp_code: 'fail',
										error_code: '系统繁忙'
									});
									return;
								}
								attributionResult.data.list = goodsResult.data.list;
								res.send(attributionResult);
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
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

/*
 * 进入订单物流页面
 * @param order_id		订单id
 * @param order_type	订单类型(WYP, LLM, JBD)
 */
router.get('/order-logistics', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		var formData = {
			service: 'order.orderLogisticsFind',
			params: JSON.stringify({
				order_id: requestParams.order_id,
				order_type: requestParams.order_type
			})
		};
		request.post(appConfig.opsorderPath, {form: formData}, function(error, response, orderBody) {
			try {
				if(!error && response.statusCode == 200) {
					var orderResult = JSON.parse(orderBody);
					if(orderResult.rsp_code == 'fail') {
						res.send(orderResult.error_msg);
						return;
					}
					if(!orderResult.data.detail.logistics) {
						res.send('暂无物流信息');
						return;
					}
					var logistics = JSON.parse(orderResult.data.detail.logistics);

					// 跳转到快递100物流查询页面
					res.redirect('https://m.kuaidi100.com/index_all.html?postid=' + logistics.number.replace('No:', ''));
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