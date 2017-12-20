var express = require('express');
var router = express.Router();
var url = require('url');
var request = require('request');

var logger = require('../lib/log4js').getLogger('goods-router');
var appConfig = require('../app-config');
var bizUtils = require('../lib/biz-utils');
var errHandler = require('../lib/err-handler');
// appportal商品服务Java接口地址（临时使用，后期改为配置）
var appportalJavaGoodsServiceAPIHost = 'http://100.114.48.227/ops-goods/goods';

/*
 * 商品下架
 * @params goods_code 商品码
 */
router.all('/offshelf', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		if(!requestParams.app_token || !requestParams.goods_code || !requestParams.sign) {
			errHandler.incompleteParams(res);
			return;
		}

		bizUtils.validAppToken(req, res, requestParams.app_token, function(tokenValidData) {
			// 根据返回的security_code校验签名
			if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
				errHandler.invalidSign(res);
				return;
			}
			var formData = {
				service: 'goods.freeGetGoodsStatusEdit',
				params: JSON.stringify({
					goods_code: requestParams.goods_code,
					status: 'off_shelf'
				})
			};
			request.post(appportalJavaGoodsServiceAPIHost, {form: formData}, function(error, response, body) {
				try {
					if(!error && response.statusCode == 200) {
						res.send(JSON.parse(body));
					} else {
						errHandler.systemError(res, error);
					}
				} catch(e) {
					errHandler.systemError(res, e);
				}
			});
		});
	} catch(e) {
		errHandler.systemError(res, e);
	}
});

module.exports = router;