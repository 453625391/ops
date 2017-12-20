var express = require('express');
var router = express.Router();
var request = require('request');

var appConfig = require('../app-config');
var errHandler = require('../lib/err-handler');
var bizUtils = require('../lib/biz-utils');

/*
 * 获取淘淘领商品列表
 * @param type		商品类型(all, new, category, forecast)
 * @param page		分页参数
 * @param category	分类名称(可选,当type为category时传)
 */
router.post('/getGoodsListByType', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = req.body;
		var service = '', params = {};

		switch(requestParams.type) {
			case 'all':
				service = 'goods.freeGetGoodsListPageFind';
				break;
			case 'new':
				service = 'goods.freeGetGoodsNewestListPageFind';
				break;
			case 'forecast':
				service = 'goods.freeGetGoodsPreSaleListPageFind';
				break;
			case 'category':
				service = 'goods.freeGetGoodsByLabelListPageFind';
				params.label_promotion = requestParams.category;
				break;
			default:
				sendError(res, 'invalid params');
				return;
		}

		var formData = {
			service: service,
			params: JSON.stringify(params),
			page: requestParams.page || JSON.stringify({page_no: '1', page_size: '10'})
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var jsonData = JSON.parse(body);
					var goodsList = jsonData.rsp_code == 'succ' ? jsonData.data.list : [];
					res.send({
						goodsList : goodsList,
						page : jsonData.data ? jsonData.data.page : {}
					});
				} else {
					sendError(res);
				}
			} catch(e) {
				sendError(res);
			}
		});
	} catch(e) {
		sendError(res);
	}
});

/*
 * 获取淘淘领推荐商品
 */
router.post('/getRecommendGoodsList', function(req, res, next) {
	try {
		var formData = {
			service: 'goods.psGoodsActivityListFind',
			params: JSON.stringify({
				activity_type: 'HDMS_04'
			})
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var jsonData = JSON.parse(body);
					var recommendList = jsonData.rsp_code == 'succ' ? jsonData.data.list : [];
					res.send({
						recommendList: recommendList
					});
				} else {
					errHandler.renderErrorPage(res, error);
				}
			} catch(e) {
				errHandler.renderErrorPage(res, e);
			}
		});
	} catch(e) {
		sendError(res);
	}
});

/*
 * 获取淘淘领banner
 */
router.post('/getBanner', function(req, res, next) {
	try {
		var formData = {
			service: 'gdAppAdvert.app.gdAppAdvertFind',
			params: JSON.stringify({
				category: 'llm_app'
			})
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var jsonData = JSON.parse(body);
					res.send({
						banner: jsonData.data || {doc_url:{}, list:[]}
					});
				} else {
					sendError(res);
				}
			} catch(e) {
				sendError(res);
			}
		});
	} catch(e) {
		sendError(res);
	}
});


/*
 * 查询淘淘领商品详情
 * @param goods_id	商品id
 */
router.post('/getGoodsDetail', function(req, res, next) {
	try {
		var requestParams = req.body;
		var formData = {
			service : 'goods.psGoodsDetailFind',
			params : JSON.stringify({
				goods_id : requestParams.goods_id
			})
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var jsonData = JSON.parse(body);
					var goods = {};
					if(jsonData.data && jsonData.data.list.length != 0) {
						var goodsList = handleGoodsData(jsonData.data.list, jsonData.data.doc_url);
						goods = goodsList[0];
					}
					res.send({
						goods: goods
					});
				} else {
					sendError(res);
				}
			} catch(e) {
				sendError(res);
			}
		});
	} catch(e) {
		sendError(res);
	}
});

// 处理商品数据
function handleGoodsData(goodsList, docUrl) {
	for(var i=0; i<goodsList.length; i++) {
		// 处理图片
		if(docUrl && goodsList[i].pic_info) {
			var picUrlArr = [];
			var pic_info = JSON.parse(goodsList[i].pic_info);
			for(var j=0; j<pic_info.length; j++) {
				picUrlArr.push(docUrl[pic_info[j].path_id]);
			}
			goodsList[i].picUrlArr = picUrlArr;
		}

		// 处理详情图片
		if(docUrl && goodsList[i].pic_detail_info && goodsList[i].pic_detail_info.length != 0) {	// 如果有详情图片
			var picDetailArr = [];
			goodsList[i].pic_detail_info = JSON.parse(goodsList[i].pic_detail_info);
			for(var m=0; m<goodsList[i].pic_detail_info.length; m++) {
				picDetailArr.push({
					title : goodsList[i].pic_detail_info[m].title || '',
					url : docUrl[goodsList[i].pic_detail_info[m].path_id]
				});
			}
			goodsList[i].picDetailArr = picDetailArr;
		}

        // 处理开卖时间
        if(goodsList[i].valid_thru) {
        	var buyDate = new Date(goodsList[i].valid_thru);
        	var now = new Date();
        	var leftMilliseconds = buyDate - now;
        	if(!isNaN(leftMilliseconds)) {
        		goodsList[i].leftSeconds = parseInt(leftMilliseconds / 1000);	// 距离开卖时间还剩多少秒
        	} else {
        		goodsList[i].leftSeconds = -1;	// 若没有取到距离开卖时间则默认可以直接购买
        	}
        	var saleTimeStr = goodsList[i].valid_thru;
        	goodsList[i].saleDate = saleTimeStr.substr(5, 2) + '月' + saleTimeStr.substr(8, 2) + '日';
        	goodsList[i].saleTime = saleTimeStr.substr(11, 5) || '00:00';
        	if(buyDate.getYear() == now.getYear() && buyDate.getMonth() == now.getMonth()) {
        		if(buyDate.getDate() == now.getDate()) goodsList[i].saleDate = '今天';
        		if(buyDate.getDate() - now.getDate() == 1) goodsList[i].saleDate = '明天';
        	}
        }

        // 计算返还价格
        goodsList[i].refund = (goodsList[i].market_price - goodsList[i].discount_price).toFixed(2);
	}
	return goodsList;
}

function sendError(res, msg) {
	res.status(500);
	res.send({
		error_msg: msg || '系统异常'
	});
}

module.exports = router;