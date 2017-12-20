var express = require('express');
var router = express.Router();
var url = require('url');
var request = require('request');

var appConfig = require('../app-config');
var errHandler = require('../lib/err-handler');

/*
 * PC端展示
 * @param area_id 当前所在地区id
 */
router.get('/', function(req, res, next) {
	try {
		var requestParams = req.query;
		var formData = {
			service : 'goods.psGoodsActivityListFind',
			params : JSON.stringify({
				activity_type : 'HDMS_04'
			})
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var jsonData = JSON.parse(body);
					var goodsList = jsonData.rsp_code == 'succ' ? jsonData.data.list : [];
					goodsList = handleImgUrlAndArea(jsonData.data.list);
					res.render('fan', {
						goodsList : goodsList
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
 * 移动端淘淘领详情分享页面
 * @param goods_id 商品id
 */
router.get('/wap/detail', function(req, res, next) {
	try {
		if(!req.query.goods_id) {
			errHandler.incompleteParams(res);
			return;
		}
		var formData = {
			service : 'goods.psGoodsDetailFind',
			params : JSON.stringify({
				goods_id : req.query.goods_id
			})
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var data = JSON.parse(body);
					var goods = {};
					if(data.data && data.data.list.length != 0) {
						var goodsList = handleImgUrlAndArea(data.data.list, data.data.doc_url);
						goods = goodsList[0];
					}
					res.render('fan-detail-wap', {
						goods : goods,
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

// 处理图片url和地区
function handleImgUrlAndArea(goodsList, map) {
	for(var i=0; i<goodsList.length; i++) {
		// 处理图片
		if(map && goodsList[i].pic_info) {
			var picUrlArr = [];
			var pic_info = JSON.parse(goodsList[i].pic_info);
			for(var j=0; j<pic_info.length; j++) {
				picUrlArr.push(map[pic_info[j].path_id]);
			}
			goodsList[i].picUrlArr = picUrlArr;
		}

		// 处理详情图片
		if(map && goodsList[i].pic_detail_info && goodsList[i].pic_detail_info.length != 0) {	// 如果有详情图片
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