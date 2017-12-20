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
 * 转发支付宝异步通知回调接口（消费者）
 */
router.all('/consumer_alipay', function(req, res, next) {
	logger.info('消费者支付宝支付notify开始！');
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		
		// 进行交易相关业务
		request.post(appConfig.consumerAlipayNotifyCallbackPath, {form: requestParams}, function(error, response, body) {
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
 * 转发支付宝异步通知回调接口（店东）
 */
router.all('/store_alipay', function(req, res, next) {
	logger.info('店东支付宝支付notify开始！');
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		
		// 进行交易相关业务
		request.post(appConfig.storeAlipayNotifyCallbackPath, {form: requestParams}, function(error, response, body) {
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
 * 转发支付宝异步通知回调接口（颂到家）
 */
router.all('/sdj_alipay', function(req, res, next) {
	logger.info('颂到家支付宝支付notify开始！');
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		
		// 进行交易相关业务
		request.post(appConfig.sdjAlipayNotifyCallbackPath, {form: requestParams}, function(error, response, body) {
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
 * 转发支付宝异步通知回调接口（惠易定）
 */
router.all('/hyd_alipay', function(req, res, next) {
	logger.info('惠易定支付宝支付notify开始！');
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		
		// 进行交易相关业务
		request.post(appConfig.hydAlipayNotifyCallbackPath, {form: requestParams}, function(error, response, body) {
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
 * 转发支付宝异步通知回调接口（熟么）
 */
router.all('/shume_alipay', function(req, res, next) {
	logger.info('熟么支付宝支付notify开始！');
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		
		// 进行交易相关业务
		request.post(appConfig.shumeAlipayNotifyCallbackPath, {form: requestParams}, function(error, response, body) {
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
 * 转发支付宝异步通知回调接口（惠点收银）
 */
router.all('/cashier_alipay', function(req, res, next) {
	logger.info('惠点收银支付宝支付notify开始！');
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		
		// 进行交易相关业务
		request.post(appConfig.cashierAlipayNotifyCallbackPath, {form: requestParams}, function(error, response, body) {
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
 * 转发支付宝异步通知回调接口（面对面支付）
 */
router.all('/face_alipay', function(req, res, next) {
	logger.info('面对面支付宝支付notify开始！');
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		
		// 进行交易相关业务
		request.post(appConfig.faceAlipayNotifyCallbackPath, {form: requestParams}, function(error, response, body) {
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
 * 转发微信异步通知回调接口（消费者）
 */
router.all('/consumer_wechat', function(req, res, next) {
	logger.info('消费者微信支付notify开始！');
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
				request.post(appConfig.consumerWechatNotifyCallbackPath, {form: formData}, function(error, response, body) {
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

/*
 * 转发微信异步通知回调接口（店东）
 */
router.all('/store_wechat', function(req, res, next) {
	logger.info('店东微信支付notify开始！');
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
				request.post(appConfig.storeWechatNotifyCallbackPath, {form: formData}, function(error, response, body) {
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

/*
 * 转发微信异步通知回调接口（颂到家）
 */
router.all('/sdj_wechat', function(req, res, next) {
	logger.info('颂到家微信支付notify开始！');
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
				request.post(appConfig.sdjWechatNotifyCallbackPath, {form: formData}, function(error, response, body) {
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

/*
 * 转发微信异步通知回调接口（惠易定）
 */
router.all('/hyd_wechat', function(req, res, next) {
	logger.info('惠易定微信支付notify开始！');
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
				request.post(appConfig.hydWechatNotifyCallbackPath, {form: formData}, function(error, response, body) {
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

/*
 * 转发微信异步通知回调接口（熟么）
 */
router.all('/shume_wechat', function(req, res, next) {
	logger.info('熟么微信支付notify开始！');
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
				request.post(appConfig.shumeWechatNotifyCallbackPath, {form: formData}, function(error, response, body) {
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

/*
 * 转发微信异步通知回调接口（惠点公众号）
 */
router.all('/huidian_wechat', function(req, res, next) {
	logger.info('惠点公众号支付notify开始！');
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
				request.post(appConfig.huidianWechatNotifyCallbackPath, {form: formData}, function(error, response, body) {
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

/*
 * 转发微信异步通知回调接口（惠点收银）
 */
router.all('/cashier_wechat', function(req, res, next) {
	logger.info('惠点收银支付notify开始！');
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
				request.post(appConfig.cashierWechatNotifyCallbackPath, {form: formData}, function(error, response, body) {
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

/*
 * 转发微信异步通知回调接口（惠驿哥）
 */
router.all('/hyg_wechat', function(req, res, next) {
	logger.info('惠驿哥支付notify开始！');
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
				request.post(appConfig.hygWechatNotifyCallbackPath, {form: formData}, function(error, response, body) {
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