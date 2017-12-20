var express = require('express');
var router = express.Router();
var url = require('url');
var request = require('request');
var ipware = require('ipware')();

var versionConfig = require('../version-config');
var errHandler = require('../lib/err-handler');
var bizUtils = require('../lib/biz-utils');

/*
 * 根据包名获取最新版本信息
 * @param package_name	包名
 */
router.all('/latest', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if(!requestParams.package_name) {
			errHandler.incompleteParams(res);
			return;
		}
		// 获取App版本信息
		var versionInfo = versionConfig[requestParams.package_name];

		if(!versionInfo) {
			errHandler.appNotFound(res);
			return;
		}

		res.send({
			rsp_code: 'succ',
			data: versionInfo
		});
		return;
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

module.exports = router;