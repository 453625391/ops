var express = require('express');
var router = express.Router();
var url = require('url');
var request = require('request');
var ipware = require('ipware')();
var parseString = require('xml2js').parseString;

var logger = require('../lib/log4js').getLogger('paynotify-router');
var appConfig = require('../app-config');
var errHandler = require('../lib/err-handler');
var bizUtils = require('../lib/biz-utils');

/*
 * 转发支付宝异步通知回调接口
 */
router.all('/alipay', function(req, res, next) {
	logger.info('支付宝支付notify开始！');
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		
		// 进行交易相关业务
		request.post(appConfig.alipayNotifyCallbackPath, {form: requestParams}, function(error, response, body) {
			if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
				res.send(body);
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
 * 转发微信异步通知回调接口
 */
router.all('/wechat', function(req, res, next) {
	logger.info('微信支付notify开始！');
	try {
		// 微信传递的xml
		var xml = '';
		req.on('data', function(chunk) {
			xml += chunk;
		});
		logger.info('微信传递的xml：');
		logger.info(xml);
		req.on('end', function() {
			// 将xml转换成json
			parseString(xml, { explicitArray : false, ignoreAttrs : true }, function (err, result) {
				var formData = 'wechat_notify_data=' + JSON.stringify(result.xml);
				// 进行交易相关业务
				request.post(appConfig.wechatNotifyCallbackPath, {form: formData}, function(error, response, body) {
					logger.info('接口返回结果：');
					logger.info('body:' + body);
					if(!error && response.statusCode == 200) {	// 请求成功后直接返回结果
						res.send(body);
					} else {
						logger.info('error:' + error);
						errHandler.systemError(res, error);
						return;
					}
				});
			});
		});
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

module.exports = router;