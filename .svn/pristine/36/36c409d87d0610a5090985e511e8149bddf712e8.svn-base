/**
 * Created by Changfeng on 2016/07/12
 * 帮助路由
 */
var express = require('express');
var router = express.Router();
var helpConfig = require('../help-config');

/*
 * 接收帮助页面请求
 */
router.get('/', function(req, res, next) {
	try {
		var pagePath = helpConfig[req.query.id];
		if(!pagePath) pagePath = '404';
		res.render(pagePath);
		return;
	} catch(e) {
		res.render('404');
		return;
	}
});

module.exports = router;