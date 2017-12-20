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
 * 进入大健康页面
 * @param area_id  	地区
 */
router.get('/', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		res.render('b/djk/index', {
			area_id : requestParams.area_id
		});
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

/*
 * 获取大健康商品列表
 * @param display_area	商品类别
 * @param area_id		地区id
 */
router.post('/getList', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		var formData = {
			service : 'goods.psGoodsFindForWeb',
			params : JSON.stringify({
				area_id : requestParams.area_id || '',
				display_area : requestParams.display_area || ''
			})
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var jsonData = JSON.parse(body);
					var goodsList = handleImgUrlAndArea(jsonData.data.doc_url, jsonData.data.list);
					res.send({
						goodsList : goodsList,
						page : jsonData.data.page
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
 * 进入大健康商品详情页面
 * @param goods_id  商品id
 */
router.get('/detail', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		var formData = {
			service : 'goods.psGoodsDetailFind',
			params : JSON.stringify({
				goods_id : requestParams.goods_id
			})
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var data = JSON.parse(body);
					var goods = {};
					if(data.data && data.data.list.length != 0) {
						var goodsList = handleImgUrlAndArea(data.data.doc_url, data.data.list);
						goods = goodsList[0];
					}
					res.render('b/djk/detail', {
						goods : goods
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
 * 进入大健康搜索结果页面
 * @param keyword	关键字
 * @param area_id	地区id
 */
router.get('/search', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		var formData = {
			service : 'goods.psGoodsFindForWeb',
			params : JSON.stringify({
				goods_like : requestParams.keyword,
				area_id : requestParams.area_id,
				display_area : '健康,滋补,计生'
			})
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var jsonData = JSON.parse(body);
					var goodsList = handleImgUrlAndArea(jsonData.data.doc_url, jsonData.data.list);
					res.render('b/djk/search', {
						goodsList : goodsList
					});
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
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

// 处理图片url和地区
function handleImgUrlAndArea(map, goodsList) {
	for(var i=0; i<goodsList.length; i++) {
		// 处理图片
		if(goodsList[i].pic_info) {
			var picUrlArr = [];
			var pic_info = JSON.parse(goodsList[i].pic_info);
			for(var j=0; j<pic_info.length; j++) {
				picUrlArr.push(map[pic_info[j].path_id]);
			}
			goodsList[i].picUrlArr = picUrlArr;
		}

		// 处理详情图片
		if(goodsList[i].pic_detail_info && goodsList[i].pic_detail_info.length != 0) {	// 如果有详情图片
			var picDetailArr = [];
			goodsList[i].pic_detail_info = JSON.parse(goodsList[i].pic_detail_info);
			for(var m=0; m<goodsList[i].pic_detail_info.length; m++) {
				picDetailArr.push({
					title : goodsList[i].pic_detail_info[m].title || '',
					url : map[goodsList[i].pic_detail_info[m].path_id]
				});
			}
			goodsList[i].picDetailArr = picDetailArr;
		}

		// 处理地区
		if(goodsList[i].delivery_desc) {
			var areaArr = goodsList[i].delivery_desc.split('|');
	        for(var k=0; k<areaArr.length; k++) {
	            areaArr[k] = areaArr[k].replace('中国,', '').replace(/,/g, '').replace('中国', '全国');
	        }
	        goodsList[i].delivery_desc = areaArr.join('，');
		}
	}
	return goodsList;
}

module.exports = router;