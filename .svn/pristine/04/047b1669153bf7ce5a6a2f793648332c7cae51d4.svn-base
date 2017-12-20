var express = require('express');
var router = express.Router();
var url = require('url');
var request = require('request');
var ipware = require('ipware')();
var parseString = require('xml2js').parseString;
var eventproxy = require('eventproxy');

var logger = require('../../lib/log4js').getLogger('h5-b-router');
var appConfig = require('../../app-config');
var errHandler = require('../../lib/err-handler');
var bizUtils = require('../../lib/biz-utils');

/*
 * 进入我要批页面（每日一惠）
 * @param channel 	渠道(ios , android)
 * @param area_id  	地区
 */
router.get('/wholesale', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 暂时隐藏每日一惠，直接进入批发中心
		res.redirect('wholesale/index?area_id=' + requestParams.area_id)
		return;

		var formData = {
			service : 'goods.psGoodsActivityListFind',
			params : JSON.stringify({
				activity_type : 'HDMS_02'
			})
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error1, response, body) {
			try {
				if(!error1 && response.statusCode == 200) {
					var topData = JSON.parse(body);
					if(topData.data.list && topData.data.list.length != 0) {
						var detailFormData = {
							service : 'goods.psGoodsDetailFind',
							params : JSON.stringify({
								goods_id : topData.data.list[0].goods_id
							})
						};
						request.post(appConfig.goodsPath, {form: detailFormData}, function(error2, response, detailBody) {
							try {
								if(!error2 && response.statusCode == 200) {
									var detailData = JSON.parse(detailBody);
									var goodsList = handleImgUrlAndArea(detailData.data.list, detailData.data.doc_url);
									var goods = goodsList[0] || {};
									res.render('b/wholesale/today', {
										goods : goods,
										area_id : requestParams.area_id
									});
								} else {
									errHandler.renderErrorPage(res, error2);
									return;
								}
							} catch(e) {
								errHandler.renderErrorPage(res, e);
								return;
							}
						});
					} else {
						// 如果没查到每日一惠商品则直接进入批发中心
						res.redirect('wholesale/index?area_id=' + requestParams.area_id)
					}
				} else {
					errHandler.renderErrorPage(res, error1);
				}
			} catch(e) {
				errHandler.renderErrorPage(res, e);
			}
		});
	} catch(e) {
		errHandler.renderErrorPage(res, e);
	}
});

/*
 * 进入我要批首页（新版）
 * @param area_id  	地区
 */
router.get('/wholesale/index', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		var ep = new eventproxy();
		ep.all('activityGoodsList', 'adGoodsList', 'recommendBanner', 'dczgBanner', 'haxBanner', 'xqtBanner',
			function(activityGoodsList, adGoodsList, recommendBanner, dczgBanner, haxBanner, xqtBanner) {
			res.render('b/wholesale/index', {
				adGoodsList : adGoodsList,
				activityGoodsList : activityGoodsList,
				recommendBanner: recommendBanner,
				dczgBanner: dczgBanner,
				haxBanner: haxBanner,
				xqtBanner: xqtBanner,
				requestParams : requestParams
			});
		});

		// 查询推荐商品
		var recommendFormData = {
			service : 'goods.psGoodsActivityListFind',
			params : JSON.stringify({
				activity_type : 'HDMS_01'
			})
		};
		request.post(appConfig.goodsPath, {form: recommendFormData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var jsonData = JSON.parse(body);
					var activityGoodsList = jsonData.rsp_code == 'succ' ? jsonData.data.list : [];
					activityGoodsList = handleImgUrlAndArea(jsonData.data.list);
					ep.emit('activityGoodsList', activityGoodsList);
				} else {
					errHandler.renderErrorPage(res, error);
				}
			} catch(e) {
				errHandler.renderErrorPage(res, e);
			}
		});

		// 查询广告商品
		var adGoodsFormData = {
			service : 'goods.gdAdvertFind',
			params : JSON.stringify({
				placement : 'app_woyaopi',
				status : 'online'
			})
		};
		request.post(appConfig.goodsPath, {form: adGoodsFormData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var jsonData = JSON.parse(body);
					var adGoodsList = [];
					if(jsonData.rsp_code == 'succ') {
						adGoodsList = handleAdGoods(jsonData.data);
					}
					ep.emit('adGoodsList', adGoodsList);
				} else {
					errHandler.renderErrorPage(res, error);
				}
			} catch(e) {
				errHandler.renderErrorPage(res, error)
			}
		});

		// 查询我要批推荐banner图
		getBanner('wyp_recommend', function(banner) {
			ep.emit('recommendBanner', banner);
		});

		// 查询我要批大厂直供banner图
		getBanner('wyp_dczg', function(banner) {
			ep.emit('dczgBanner', banner);
		});

		// 查询我要批惠安心banner图
		getBanner('wyp_hax', function(banner) {
			ep.emit('haxBanner', banner);
		});

		// 查询我要批新奇特banner图
		getBanner('wyp_xqt', function(banner) {
			ep.emit('xqtBanner', banner);
		});
	} catch(e) {
		errHandler.renderErrorPage(res, e);
	}
});

/*
 * 获取banner图列表
 * @param category	banner图分类
 */
 function getBanner(category, callback) {
 	var banner = {doc_url:{}, list:[]};
	var formData = {
		service: 'gdAppAdvert.app.gdAppAdvertFind',
		params: JSON.stringify({
			category: category
		})
	};
	request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
		try {
			if(!error && response.statusCode == 200) {
				var jsonData = JSON.parse(body);
				if(jsonData.rsp_code == 'succ') {
					banner = jsonData.data
				}
				callback(banner);
			} else {
				callback(banner);
			}
		} catch(e) {
			callback(banner);
		}
	});
 }

/*
 * 进入我要批分类页面（新版）
 * @param area_id  	地区
 * @param category 	分类
 */
router.get('/wholesale/category', function(req, res, next) {
	res.render('b/wholesale/category', {
		area_id: req.query.area_id,
		category: req.query.category
	});
});

/*
 * 获取我要批商品列表
 * @param display_area	商品类别
 * @param area_id		地区id
 */
router.post('/wholesale/getList', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		var formData = {
			service : 'goods.wypGoodsFindForH5',
			params : JSON.stringify({
				area_id : requestParams.area_id,
				display_area : requestParams.display_area || '',
				label_promotion : requestParams.label_promotion || ''
			}),
			page : JSON.stringify({
				page_no : requestParams.page_no || '1',
				page_size : requestParams.page_size || '50'
			})
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var jsonData = JSON.parse(body);
					var goodsList = handleImgUrlAndArea(jsonData.data.list, jsonData.data.doc_url);
					res.send({
						goodsList : goodsList,
						page : jsonData.data.page
					});
				} else {
					errHandler.systemError(res, error);
				}
			} catch(e) {
				errHandler.systemError(res, e);
			}
		});
	} catch(e) {
		errHandler.systemError(res, e);
	}
});

/*
 * 进入我要批商品详情页面
 * @param channel 	渠道(ios , android)
 * @param goods_id  商品id
 */
router.get('/wholesale/detail', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		var formData = {
			service : 'goods.wypGoodsDetailFindForH5',
			params : JSON.stringify({
				goods_id : requestParams.goods_id
			})
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var data = JSON.parse(body);
					var goods = {};
					if(data.data && data.data.detail) {
						var tmpList = [];
						tmpList.push(data.data.detail);
						var goodsList = handleImgUrlAndArea(tmpList, data.data.doc_url);
						goods = goodsList[0];
					}
					res.render('b/wholesale/detail', {
						channel : requestParams.channel || 'ios',
						goods : goods
					});
				} else {
					errHandler.renderErrorPage(res, error);
				}
			} catch(e) {
				errHandler.renderErrorPage(res, e);
			}
		});
	} catch(e) {
		errHandler.renderErrorPage(res, e);
	}
});

/*
 * 进入我要批搜索结果页面
 * @param keyword	关键字
 * @param area_id	地区id
 */
router.get('/wholesale/search', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		if(!requestParams.area_id || requestParams.area_id.length != 6) {
			requestParams.area_id = '100000';
		}
		var formData = {
			service : 'goods.wypGoodsFindForH5',
			params : JSON.stringify({
				goods_like : requestParams.keyword,
				area_id : requestParams.area_id
			}),
			page : JSON.stringify({
				page_no : requestParams.page_no || '1',
				page_size : requestParams.page_size || '30'
			})
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var jsonData = JSON.parse(body);
					var goodsList = handleImgUrlAndArea(jsonData.data.list, jsonData.data.doc_url);
					res.render('b/wholesale/search', {
						goodsList : goodsList,
						page : jsonData.data.page,
						channel : requestParams.channel,
						area_id : requestParams.area_id
					});
				} else {
					errHandler.renderErrorPage(res, error);
				}
			} catch(e) {
				errHandler.renderErrorPage(res, e);
			}
		});
	} catch(e) {
		errHandler.renderErrorPage(res, e);
	}
});

/*
 * 进入我要批物流页面
 * @param order_id	订单id
 */
router.get('/wholesale/logistics', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		// 跳转到公用模块物流查询功能
		res.redirect('/openapi/h5/common/order-logistics?order_type=WYP&order_id='+requestParams.order_id);
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
	}
	return goodsList;
}

// 处理我要批广告图
function handleAdGoods(adResult) {
	if(adResult.list.length > 0) {
		var picInfoArr;
		for(var i=0; i<adResult.list.length; i++) {
			picInfoArr = JSON.parse(adResult.list[i].pic_info);
			if(picInfoArr.length > 0) {
				adResult.list[i].id = picInfoArr[0].forward_url;
				adResult.list[i].pic_path = adResult.doc_url[picInfoArr[0].path_id];
			} else {
				adResult.list.splice(i, 1);	// 没有图片信息时直接移除掉list中的这个对象
			}
		}
	}
	return adResult.list;
}

/*
 * 进入超级返页面
 */
router.get('/free', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		var ep = new eventproxy();
		ep.all('banner', 'recommendList', function(banner, recommendList) {
			res.render('b/free/index', {
				requestParams: requestParams,
				banner: banner,
				recommendList: recommendList
			});
		});

		// 查询banner图
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
					ep.emit('banner', jsonData.data || {doc_url:{}, list:[]});
				} else {
					errHandler.renderErrorPage(res, error);
				}
			} catch(e) {
				errHandler.renderErrorPage(res, e);
			}
		});

		// 查询推荐商品
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
					recommendList = handleImgUrlAndArea(recommendList);
					ep.emit('recommendList', recommendList);
				} else {
					errHandler.renderErrorPage(res, error);
				}
			} catch(e) {
				errHandler.renderErrorPage(res, e);
			}
		});
	} catch(e) {
		errHandler.renderErrorPage(res, e);
	}
});

/*
 * 获取超级返商品列表
 * @param display_area	商品类别
 * @param area_id		地区id
 */
router.post('/free/getList', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		var formData = {
			service: requestParams.service,
			params: requestParams.params,
			page: requestParams.page
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var jsonData = JSON.parse(body);
					var goodsList = jsonData.rsp_code == 'succ' ? jsonData.data.list : [];
					goodsList = handleImgUrlAndArea(goodsList);
					res.send({
						goodsList : goodsList,
						page : jsonData.data ? jsonData.data.page : {}
					});
				} else {
					errHandler.systemError(res, error);
				}
			} catch(e) {
				errHandler.systemError(res, e);
			}
		});
	} catch(e) {
		errHandler.systemError(res, e);
	}
});

/*
 * 进入超级返商品详情页面
 * @param goods_id  商品id
 */
router.get('/free/detail', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		var detailFormData = {
			service : 'goods.psGoodsDetailFind',
			params : JSON.stringify({
				goods_id : requestParams.goods_id
			})
		};
		request.post(appConfig.goodsPath, {form: detailFormData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var data = JSON.parse(body);
					var goods = {};
					if(data.data && data.data.list.length != 0) {
						var goodsList = handleImgUrlAndArea(data.data.list, data.data.doc_url);
						goods = goodsList[0];
					}

					var validFormData = {
						service: 'order.freeGetValidate',
						params: JSON.stringify({
							goods_code: goods.goods_code,
							mobile: requestParams.mobile
						})
					};
					request.post(appConfig.opsorderPath, {form: validFormData}, function(error, response, body) {
						try {
							if(!error && response.statusCode == 200) {
								var validResult = JSON.parse(body);
								res.render('b/free/detail', {
									goods : goods,
									alreadyGot: validResult.data ? validResult.data.already_got : 'false',		// 默认没有领过
									inBlacklist: validResult.data ? validResult.data.in_blacklist : 'false',	// 默认不在黑名单,
									requestParams: requestParams
								});
							} else {
								errHandler.renderErrorPage(res, error);
							}
						} catch(e) {
							errHandler.renderErrorPage(res, e);
						}
					});
				} else {
					errHandler.renderErrorPage(res, error);
				}
			} catch(e) {
				errHandler.renderErrorPage(res, e);
			}
		});
	} catch(e) {
		errHandler.renderErrorPage(res, e);
	}
});

/*
 * 收藏淘淘领商品
 * @param member_id		消费者id
 * @param goods_id 		商品id
 * @param valid_thru	商品有效期
 */
router.post('/free/collectGoods', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		var formData = {
			service: 'psGoodsFavorites.app.psGoodsFavoritesCreate',
			params: JSON.stringify({
				favorites_type: 'llm',
				member_type_key: 'stores',
				member_id: requestParams.member_id,
				goods_id: requestParams.goods_id
			})
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var jsonData = JSON.parse(body);
					res.send(jsonData);
				} else {
					errHandler.systemError(res, error);
				}
			} catch(e) {
				errHandler.systemError(res, e);
			}
		});
	} catch(e) {
		errHandler.systemError(res, e);
	}
});

/*
 * 取消收藏淘淘领商品
 * @param member_id			消费者id
 * @param goods_id 			商品id
 * @param valid_thru		商品有效期
 */
router.post('/free/unCollectGoods', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		var formData = {
			service: 'psGoodsFavorites.app.psGoodsFavoritesCancel',
			params: JSON.stringify({
				member_type_key: 'stores',
				member_id: requestParams.member_id,
				goods_id: requestParams.goods_id
			})
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var jsonData = JSON.parse(body);
					res.send(jsonData);
				} else {
					errHandler.systemError(res, error);
				}
			} catch(e) {
				errHandler.systemError(res, e);
			}
		});
	} catch(e) {
		errHandler.systemError(res, e);
	}
});

/*
 * 店东超级返发送短信
 * @param member_id  店东会员id
 */
router.post('/free/sendSMS', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// STEP1: 查询可用余额
		var usableFormData = {
			service : 'finance.usableCashBalanceFind',
			params : JSON.stringify({
				member_id : requestParams.member_id,
				member_type_key : 'stores'
			})
		};
		request.post(appConfig.financePath, {form: usableFormData}, function(error, response, usableBody) {
			try {
				if(!error && response.statusCode == 200) {
					var usableResult = JSON.parse(usableBody);
					if(usableResult.rsp_code == 'fail') {
						errHandler.systemError(res, usableResult.error_msg);
						return;
					}
					if(usableResult.data.usable_cash_balance < 0) {
						res.send({
							rsp_code: 'fail',
							error_code: 'not_allowed',
							error_msg: '可用余额不足'
						});
						return;
					}

					// STEP2: 创建预订单
					var orderFormData = {
						service : 'order.storesFreeGetRecordCreate',
						params : JSON.stringify({
							goods_code : requestParams.goods_code,
							stores_id : requestParams.member_id,
							stores_name : requestParams.stores_name,
							contact_person : requestParams.contact_person,
							amount : requestParams.amount,
							mobile : requestParams.mobile
						})
					};
					request.post(appConfig.opsorderPath, {form: orderFormData}, function(error, response, orderBody) {
						try {
							if(!error && response.statusCode == 200) {
								var orderResult = JSON.parse(orderBody);
								if(orderResult.rsp_code != 'succ') {	// 创建预订单失败
									logger.info('【店东超级返创建预订单失败】');
									logger.info(requestParams);
									logger.info(orderBody);
									orderResult.error_code = 'not_allowed';
									res.send(orderResult);
									return;
								}

								// STEP3: 发送APP通知,不影响流程
								var appNotifyFormData = {
									service : 'notification.appMsgNotify',
									params : JSON.stringify({
										receiver : requestParams.member_id,
										message : '感谢您参加超级返活动，请按如下指引完成订单：' + decodeURIComponent(requestParams.product_source)
									})
								};
								request.post(appConfig.notificationPath, {form: appNotifyFormData});

								res.send(orderResult);
							} else {
								errHandler.systemError(res, error);
							}
						} catch(e) {
							errHandler.systemError(res, e);
						}
					});
				} else {
					errHandler.systemError(res, error);
				}
			} catch(e) {
				errHandler.systemError(res, e);
			}
		});
	} catch(e) {
		errHandler.systemError(res, e);
	}
});

/*
 * 进入盘点页面
 */
router.get('/inventory', function(req, res, next) {
	res.render('b/inventory/index');
});

/*
 * 进入商品盘点页面
 */
router.get('/inventory/goodsInventory', function(req, res, next) {
	res.render('b/inventory/goodsInventory');
});

/*
 * 进入惠金币页面
 */
router.get('/goldcoin', function(req, res, next) {
	res.render('b/goldcoin/index');
});

/*
 * 进入推广页面
 */
router.get('/generalize', function(req, res, next) {
	res.render('b/generalize/index', {
		stores_id: req.query.stores_id
	});
});

/*
 * 进入颂到家推广页面
 */
router.get('/generalize/sdj', function(req, res, next) {
	res.render('b/generalize/sdj/index');
});

/*
 * 进入代办页面
 */
router.get('/agency', function(req, res, next) {
	res.render('b/agency/index');
});

/*
 * 进入商学院
 */
router.get('/course', function(req, res, next) {
	res.redirect('http://u.meitianhui.com/');
});

/*
 * 进入颂到家商学院
 */
router.get('/sdj/course', function(req, res, next) {
	res.redirect('https://cms.meitianhui.com/?cat=11');
});

/*
 * 进入优惠通知
 */
router.get('/benefit_notify', function(req, res, next) {
	res.redirect('https://cms.meitianhui.com/?cat=3');
});

/*
 * 进入注册新门店
 */
router.get('/reg', function(req, res, next) {
	res.redirect('https://mps.meitianhui.com/reg');
});

/*
 * 进入登录遇到疑问
 */
router.get('/problem', function(req, res, next) {
	res.render('b/problem/index');
});

/*
 * 进入店东助手更多模块
 */
router.get('/more', function(req, res, next) {
	var requestParams = req.query;
	if(!requestParams.stores_id) {
		res.send('stores_id参数缺失')
	} else {
		res.render('b/more/index', {
			stores_id: requestParams.stores_id
		});
	}
});

/*
 * 进入店东助手更多--新品秀首页
 */
router.get('/more/xpx', function(req, res, next) {
	try {
		var requestParams = req.query;
		var formData = {
			service: 'goods.ppActivityListForWebPageFind',
			params: JSON.stringify({
				stores_id: requestParams.stores_id,
				status: 'online'
			})
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var result = JSON.parse(body);
					result = handleImgPathForActivityList(result);
					res.render('b/more/xpx/index', {
						stores_id: requestParams.stores_id,
						docUrl: result.data.doc_url,
						list: result.data.list
					});
				} else {
					errHandler.renderErrorPage(res, error);
				}
			} catch(e) {
				errHandler.renderErrorPage(res, e);
			}
		});
	} catch(e) {
		errHandler.renderErrorPage(res, e);
	}
});

/*
 * 处理新品秀图片路径
 */
function handleImgPathForActivityList(result) {
	if(result.rsp_code == 'fail') {
		result.data = {};
		result.data.doc_url = {};
		result.data.list = [];
		return result;
	}
	for(var i=0; i<result.data.list.length; i++) {
		result.data.list[i].json_data = JSON.parse(result.data.list[i].json_data);
		result.data.list[i].picUrl = [];
		for(var j=0; j<result.data.list[i].json_data.length; j++) {
			result.data.list[i].picUrl.push(result.data.doc_url[result.data.list[i].json_data[j].path_id]);
		}
	}
	return result;
}

/*
 * 进入店东助手更多--新品秀报名页
 */
router.get('/more/xpx/signup', function(req, res, next) {
	try {
		var requestParams = req.query;
		var formData = {
			service: 'member.storeFind',
			params: JSON.stringify({
				member_id: requestParams.stores_id
			})
		};
		request.post(appConfig.memberPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var result = JSON.parse(body);
					if(result.rsp_code == 'fail') {
						res.send(result);
						return;
					}
					res.render('b/more/xpx/signup', {
						stores_id: requestParams.stores_id,
						activity_id: requestParams.activity_id,
						storeInfo: result.data
					});
				} else {
					errHandler.renderErrorPage(res, error);
				}
			} catch(e) {
				errHandler.renderErrorPage(res, e);
			}
		});
	} catch(e) {
		errHandler.renderErrorPage(res, e);
	}
});

/*
 * 新品秀报名
 */
router.post('/more/xpx/signup', function(req, res, next) {
	try {
		var requestParams = bizUtils.extend(req.query, req.body);

		// 组装图片
		if(requestParams.pic_ids) {
			var picArr = requestParams.pic_ids.split(',');
			requestParams.pic_json_data = [];
			for(s in picArr) {
				requestParams.pic_json_data.push({
					path_id: picArr[s]
				});
			}
			requestParams.pic_json_data = JSON.stringify(requestParams.pic_json_data);
			delete requestParams.pic_ids;
		}

		var formData = {
			service: 'goods.ppActivityDetailAdd',
			params: JSON.stringify(requestParams)
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var result = JSON.parse(body);
					res.send(result);
				} else {
					errHandler.systemError(res, error);
				}
			} catch(e) {
				errHandler.systemError(res, e);
			}
		});
	} catch(e) {
		errHandler.systemError(res, e);
	}
});

/*
 * 进入店东助手任务模块
 */
router.get('/task', function(req, res, next) {
	var requestParams = req.query;
	res.render('b/task/index', {
		stores_id: requestParams.stores_id
	});
});

module.exports = router;