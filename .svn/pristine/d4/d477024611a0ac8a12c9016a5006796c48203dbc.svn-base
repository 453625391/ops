var express = require('express');
var router = express.Router();
var url = require('url');
var request = require('request');
var ipware = require('ipware')();

var logger = require('../lib/log4js').getLogger('exchange-router');
var appConfig = require('../app-config');
var errHandler = require('../lib/err-handler');
var bizUtils = require('../lib/biz-utils');

/*
 * 兑换中心转发接口
 * @param app_token app令牌
 * @param access_token 兑换中心接口需要的令牌
 * @param service 具体服务接口名称(即兑换中心接口文档中的method)
 * @param request_type 兑换中心接口的请求方式，get/post
 * @param params 业务参数集，json格式的字符串
 * @param sign 签名
 */
router.all('/', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(typeof requestParams.app_token == 'undefined' || typeof requestParams.access_token == 'undefined' || typeof requestParams.service == 'undefined' 
			|| typeof requestParams.request_type == 'undefined' || typeof requestParams.params == 'undefined' || typeof requestParams.sign == 'undefined') {
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

			var paramsJson = JSON.parse(requestParams.params);
			
			if(requestParams.request_type.toUpperCase() == 'GET') {	// 兑换中心get方式请求
				var exchangeCenterFullPath = appConfig.exchangeCenterHost + '?access_token=' + requestParams.access_token + '&method=' + requestParams.service;
				for(var k in paramsJson) {
					exchangeCenterFullPath += '&' + k + '=' + paramsJson[k];
				}
				request.get(exchangeCenterFullPath, function(error, response, body) {
					if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
						bizUtils.handleExchangeResult(res, body);
						return;
					} else {
						errHandler.systemError(res, error);
						return;
					}
				});
			} else if(requestParams.request_type.toUpperCase() == 'POST') {	// 兑换中心post方式请求
				paramsJson.access_token = requestParams.access_token;
				paramsJson.method = requestParams.service;

				request.post(appConfig.exchangeCenterHost, {form: paramsJson}, function(error, response, body) {
					if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
						bizUtils.handleExchangeResult(res, body);
						return;
					} else {
						errHandler.systemError(res, error);
						return;
					}
				});
			} else {
				errHandler.invalidParams(res);
				return;
			}
		});
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

module.exports = router;