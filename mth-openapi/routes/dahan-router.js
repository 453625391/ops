var express = require('express');
var router = express.Router();
var url = require('url');
var request = require('request');

var logger = require('../lib/log4js').getLogger('dahan-router');
var appConfig = require('../app-config');
var errHandler = require('../lib/err-handler');

/*
 * 大汉手机充值回调转发
 */
router.post('/mobileRechargeNotify', function(req, res, next) {
	try {
		var dataFlow = '';
		req.on('data', function(chunk) {
			dataFlow += chunk;
		});
		req.on('end', function() {
			logger.info('大汉手机充值回调请求参数流：');
			logger.info(dataFlow);

			var formData = {
				dahan_notify_data: dataFlow
			};
			request.post(appConfig.daHanNotifyCallbackPath, {form: formData}, function(error, response, body) {
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

module.exports = router;