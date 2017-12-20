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
var goldExchangeBrandList = require('../../lib/gold-exchange-brand-list');

/*
 * 进入特卖页面
 */
router.get('/onsale', function(req, res, next) {
	res.redirect('http://h.meitianhui.com');
});

/*
 * 进入代办页面
 */
router.get('/agency', function(req, res, next) {
	res.render('c/agency/index');
});

// 处理图片url和地区
function handleImgUrlAndArea(goodsList, map) {
	var picUrlArr,pic_info,i,j,picDetailArr,m;
	for(var i=0; i<goodsList.length; i++) {
		// if(goodsList[i].data_source ==='hsrj'){ //新版 取path_id
		// 	 picUrlArr = [];
		// 	 pic_info = JSON.parse(goodsList[i].pic_info);
		// 	for( j=0; j<pic_info.length; j++) {
		// 		picUrlArr.push(pic_info[j].path_id);
		// 	}
		// 	goodsList[i].picUrlArr = picUrlArr;
		//
		// 	 picDetailArr = [];
		// 	goodsList[i].pic_detail_info = JSON.parse(goodsList[i].pic_detail_info);
		// 	for( m=0; m<goodsList[i].pic_detail_info.length; m++) {
		// 		picDetailArr.push({
		// 			title : goodsList[i].pic_detail_info[m].title || '',
		// 			url : goodsList[i].pic_detail_info[m].path_id
		// 		});
		// 	}
		// 	goodsList[i].picDetailArr = picDetailArr;
		//
		// }else{
		// 处理图片
		if(map && goodsList[i].pic_info) {
			 picUrlArr = [];
			 pic_info = JSON.parse(goodsList[i].pic_info);
			for( j=0; j<pic_info.length; j++) {
				picUrlArr.push(map[pic_info[j].path_id]);
			}
			goodsList[i].picUrlArr = picUrlArr;
		}

		// 处理详情图片
		if(map && goodsList[i].pic_detail_info && goodsList[i].pic_detail_info.length != 0) {	// 如果有详情图片
			 picDetailArr = [];
			goodsList[i].pic_detail_info = JSON.parse(goodsList[i].pic_detail_info);
			for (m=0; m<goodsList[i].pic_detail_info.length; m++) {
				picDetailArr.push({
					title : goodsList[i].pic_detail_info[m].title || '',
					url : map[goodsList[i].pic_detail_info[m].path_id]
				});
			}
			goodsList[i].picDetailArr = picDetailArr;
		}

	// }// end  if(goodsList[i].data_source ==='hsrj'){ //新版 取path_id

		// 处理头像
		if(map && goodsList[i].neighbor_pic_path) {
			goodsList[i].neighbor_pic_path = map[goodsList[i].neighbor_pic_path];
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
        	goodsList[i].saleDate = parseInt(saleTimeStr.substr(5, 2))  + '月' + parseInt(saleTimeStr.substr(8, 2)) + '日';
        	goodsList[i].saleTime = saleTimeStr.substr(11, 5) || '00:00';
        	if(buyDate.getYear() == now.getYear() && buyDate.getMonth() == now.getMonth()) {
        		if(buyDate.getDate() == now.getDate()) goodsList[i].saleDate = '今天';
        		if(buyDate.getDate() - now.getDate() == 1) goodsList[i].saleDate = '明天';
        	}
        }

        // 处理距离
        if(goodsList[i].distance) {
        	if(goodsList[i].distance < 1000) {
				goodsList[i].distance = parseInt(goodsList[i].distance) + 'm';
			} else {
				goodsList[i].distance = (parseInt(goodsList[i].distance)/1000).toFixed(1) + 'km';
			}
        }

        // 处理标签
        if(goodsList[i].label) {
        	var labelArr = goodsList[i].label.split(',');
        	goodsList[i].labelArr = labelArr;
        }
	}
	return goodsList;
}

/*
 * 进入淘淘领主页
 */
router.get('/payback', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		var ep = new eventproxy();
		ep.all('banner', 'recommendList', function(banner, recommendList) {
			res.render('c/payback/index', {
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
 * 获取淘淘领商品列表
 * @param display_area	商品类别
 * @param area_id		地区id
 */
router.post('/payback/getList', function(req, res, next) {
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
 * 进入淘淘领新人专享页面
 * @param area_id	地区id
 */
router.get('/payback/freshman', function(req, res, next) {
	res.render('c/payback/freshman', {
		requestParams: req.query
	});
});

/*
 * 查询淘淘领新人专享商品列表
 * @param page	分页参数
 */
router.post('/payback/freshman/getGoodsList', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = req.body;
		var formData = {
			service : 'goods.freeGetGoodsByLabelListPageFind',
			params : JSON.stringify({
				label_promotion : 'freshman'
			}),
			page: requestParams.page
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var jsonData = JSON.parse(body);
					var goodsList = jsonData.rsp_code == 'succ' ? jsonData.data.list : [];
					goodsList = handleImgUrlAndArea(goodsList);
					res.send({
						goodsList : goodsList
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
 * 进入淘淘领商品详情页面
 * @param goods_id  商品id
 */
router.get('/payback/detail', function(req, res, next) {
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
					console.log('requestParams269:',requestParams);
					console.log('body270:',body);
					var goods = {};
					if(data.data && data.data.list.length != 0) {
						var goodsList = handleImgUrlAndArea(data.data.list, data.data.doc_url);
						goods = goodsList[0];
					}
					console.log('goods276:',JSON.stringify(goods) );
					if(requestParams.mobile) {	// 如果有传手机号则直接判断该用户是否能领取该商品
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

									res.render('c/payback/detail', {
										goods: goods,
										alreadyGot: validResult.data ? validResult.data.already_got : 'false',		// 默认没有领过
										inBlacklist: validResult.data ? validResult.data.in_blacklist : 'false',	// 默认不在黑名单
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
						res.render('c/payback/detail', {
							goods: goods,
							alreadyGot: 'false',
							inBlacklist: 'false',
							requestParams: requestParams
						});
					}
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
 * 领取商品前判断是否需要升级礼券账户
 * @param member_id  会员id
 */
router.post('/payback/checkVoucherAccount', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 根据member_id查询手机号
		var userFormData = {
			service: 'member.memberInfoFindByMemberId',
			params: JSON.stringify({
				member_id: requestParams.member_id,
				member_type_key: 'consumer'
			})
		};
		request.post(appConfig.memberPath, {form: userFormData}, function(error, response, userBody) {
			try {
				if(!error && response.statusCode == 200) {
					var userResult = JSON.parse(userBody);
					if(userResult.rsp_code == 'fail') {
						res.send(userResult);
						return;
					}

					// 查询礼券是否需要升级
					var voucherFormData = {
						service: 'voucher.app.voucherExchangeLogFind',
						params: JSON.stringify({
							consumer_id: requestParams.member_id,
							mobile: userResult.data.mobile
						})
					};
					request.post(appConfig.financePath, {form: voucherFormData}, function(error, response, voucherBody) {
						try {
							if(!error && response.statusCode == 200) {
								var voucherResult = JSON.parse(voucherBody);
								if(voucherResult.rsp_code == 'succ') voucherResult.data.mobile = userResult.data.mobile;
								res.send(voucherResult);
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
 * 升级礼券账户
 * @param consumer_id  会员id
 * @param mobile  手机号
 * @param voucher  剩余礼券
 * @param gold  剩余金币
 */
router.post('/payback/upgradeVoucherAccount', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 升级礼券账户
		var upgradeFormData = {
			service: 'voucher.app.voucherExchangeGold',
			params: JSON.stringify({
				data_source: 'SJLY_01',
				consumer_id: requestParams.consumer_id,
				mobile: requestParams.mobile,
				voucher: requestParams.voucher,
				gold: requestParams.gold
			})
		};
		request.post(appConfig.financePath, {form: upgradeFormData}, function(error, response, upgradeBody) {
			try {
				if(!error && response.statusCode == 200) {
					var upgradeResult = JSON.parse(upgradeBody);
					res.send(upgradeResult);
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

/**
 * 进入淘淘领物流详情页
 */
router.get('/payback/logistics', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		var order_id = requestParams.order_id || requestParams.order_no;
		// 跳转到公用模块物流查询功能
		res.redirect('/openapi/h5/common/order-logistics?order_type=LLM&order_id='+order_id);
	} catch(e) {
		errHandler.renderErrorPage(res, e);
	}
});

/**
 * 进入淘淘领抽奖页
 */
router.get('/payback/lottery', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		var activityFormData = {
			service: 'goods.plActivityListForConsumerPageFind',
			params: JSON.stringify({
				member_id: requestParams.member_id
			})
		};
		request.post(appConfig.goodsPath, {form: activityFormData}, function(error, response, activityBody) {
			try {
				if(!error && response.statusCode == 200) {
					var activityResult = JSON.parse(activityBody);
					activityResult = handleActivityListPics(activityResult);
					var numFormData = {
						service: 'goods.memberLotteryNumFind',
						params: JSON.stringify({
							member_id: requestParams.member_id,
							member_type_key: 'consumer'
						})
					};
					request.post(appConfig.goodsPath, {form: numFormData}, function(error, response, numBody) {
						try {
							if(!error && response.statusCode == 200) {
								var numResult = JSON.parse(numBody);
								if(numResult.rsp_code == 'fail') {
									errHandler.renderErrorPage(res, numResult.error_msg, numResult.error_msg);
								}

								// 查询中奖公告
								var luckyFormData = {
									service: 'goods.plLuckyDeliveryListForOpPageFind',
									params: JSON.stringify({}),
									page: JSON.stringify({
										page_size: '40'
									})
								};
								request.post(appConfig.goodsPath, {form: luckyFormData}, function(error, response, luckyBody) {
									try {
										if(!error && response.statusCode == 200) {
											var luckyResult = JSON.parse(luckyBody);
											res.render('c/payback/lottery', {
												member_id: requestParams.member_id,
												leftCounts: numResult.data.lottery_num,
												activityList: activityResult.data.list,
												luckyList: luckyResult.rsp_code == 'succ' ? luckyResult.data.list : []
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

function handleActivityListPics(activityResult) {
	if(activityResult.rsp_code == 'fail') {
		activityResult.data = {};
		activityResult.data.doc_url = {};
		activityResult.data.list = [];
		return activityResult;
	}
	for(var i=0; i<activityResult.data.list.length; i++) {
		var picArr = [];
		activityResult.data.list[i].json_data = JSON.parse(activityResult.data.list[i].json_data);
		for(var j=0; j<activityResult.data.list[i].json_data.length; j++) {
			picArr.push(activityResult.data.doc_url[activityResult.data.list[i].json_data[j].path_id]);
		}
		activityResult.data.list[i].picArr = picArr;
	}
	return activityResult;
}

/**
 * 进入淘淘领抽奖页活动详情
 */
router.get('/payback/lottery/detail', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 查询活动详情
		var activityFormData = {
			service: 'goods.plActivityListForConsumerDetailFind',
			params: JSON.stringify({
				activity_id: requestParams.activity_id
			})
		};
		request.post(appConfig.goodsPath, {form: activityFormData}, function(error, response, activityBody) {
			try {
				if(!error && response.statusCode == 200) {
					var activityResult = JSON.parse(activityBody);
					activityResult = handleActivityDetailPics(activityResult);

					// 查询中奖列表
					var luckFormData = {
						service: 'goods.plLuckyDeliveryListForConsumerFind',
						params: JSON.stringify({
							activity_id: requestParams.activity_id
						})
					};
					request.post(appConfig.goodsPath, {form: luckFormData}, function(error, response, luckyBody) {
						try {
							if(!error && response.statusCode == 200) {
								var luckyResult = JSON.parse(luckyBody);
								res.render('c/payback/lottery/detail', {
									member_id: requestParams.member_id,
									activityInfo: activityResult.data.detail || {},
									luckyList: luckyResult.rsp_code = 'succ' ? luckyResult.data.list : []
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

/**
 * 进入淘淘领抽奖页活动详情(刷新后)
 */
router.get('/payback/lottery/joinsucc', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 查询活动详情
		var activityFormData = {
			service: 'goods.plActivityListForConsumerDetailFind',
			params: JSON.stringify({
				activity_id: requestParams.activity_id
			})
		};
		request.post(appConfig.goodsPath, {form: activityFormData}, function(error, response, activityBody) {
			try {
				if(!error && response.statusCode == 200) {
					var activityResult = JSON.parse(activityBody);
					activityResult = handleActivityDetailPics(activityResult);

					// 查询中奖列表
					var luckFormData = {
						service: 'goods.plLuckyDeliveryListForConsumerFind',
						params: JSON.stringify({
							activity_id: requestParams.activity_id
						})
					};
					request.post(appConfig.goodsPath, {form: luckFormData}, function(error, response, luckyBody) {
						try {
							if(!error && response.statusCode == 200) {
								var luckyResult = JSON.parse(luckyBody);
								res.render('c/payback/lottery/detail', {
									member_id: requestParams.member_id,
									activityInfo: activityResult.data.detail || {},
									luckyList: luckyResult.rsp_code = 'succ' ? luckyResult.data.list : []
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

function handleActivityDetailPics(activityResult) {
	if(activityResult.rsp_code == 'fail') {
		activityResult.data = {};
		activityResult.data.detail = {};
		return activityResult;
	}
	if(activityResult.data.detail.json_data) {
		var picArr = [];
		activityResult.data.detail.json_data = JSON.parse(activityResult.data.detail.json_data);
		for(var i=0; i<activityResult.data.detail.json_data.length; i++) {
			picArr.push(activityResult.data.doc_url[activityResult.data.detail.json_data[i].path_id]);
		}
		activityResult.data.detail.picArr = picArr;

	}
	if(activityResult.data.detail.goods_pic_detail_info) {
		var detailPicArr = [];
		activityResult.data.detail.goods_pic_detail_info = JSON.parse(activityResult.data.detail.goods_pic_detail_info);
		for(var i=0; i<activityResult.data.detail.goods_pic_detail_info.length; i++) {
			detailPicArr.push(activityResult.data.doc_url[activityResult.data.detail.goods_pic_detail_info[i].path_id]);
		}
		activityResult.data.detail.detailPicArr = detailPicArr;
	}
	return activityResult;
}

/**
 * 参与淘淘领抽奖
 */
router.post('/payback/lottery/join', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		var joinFormData = {
			service: 'goods.plPartcipatorAdd',
			params: JSON.stringify({
				activity_id: requestParams.activity_id,
				member_id: requestParams.member_id,
				member_type_key: 'consumer'
			})
		};
		request.post(appConfig.goodsPath, {form: joinFormData}, function(error, response, joinBody) {
			try {
				if(!error && response.statusCode == 200) {
					var joinResult = JSON.parse(joinBody);
					res.send(joinResult);
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

/**
 * 进入我参与的抽奖列表页面
 */
router.get('/payback/lottery/joined', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		var joinedFormData = {
			service: 'goods.plPartcipatorListForConsumerPageFind',
			params: JSON.stringify({
				member_id: requestParams.member_id
			})
		};
		request.post(appConfig.goodsPath, {form: joinedFormData}, function(error, response, joinedBody) {
			try {
				if(!error && response.statusCode == 200) {
					var joinedResult = JSON.parse(joinedBody);
					joinedResult = handleActivityListPics(joinedResult);
					res.render('c/payback/lottery/joined', {
						member_id: requestParams.member_id,
						joinedList: joinedResult.rsp_code == 'succ' ? joinedResult.data.list : [],
						docUrl: joinedResult.rsp_code == 'succ' ? joinedResult.data.doc_url : {},
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
 * 根据消费者id查询其默认门店的环信id
 */
router.post('/getChatIdOfDefaultStoreByConsumerId', function(req, res, next) {
	try {
		var requestParams = req.body;
		var ep = new eventproxy();
		var chatData = {
			chatId: requestParams.chat_id,
			title: requestParams.title
		};

		// 查询默认门店id
		var defaultStoreIdFormData = {
			service: 'member.favoriteStoreList',
			params: JSON.stringify({
				consumer_id: requestParams.consumer_id,
				is_llm_stores: 'Y'
			})
		};
		request.post(appConfig.memberPath, {form: defaultStoreIdFormData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var jsonData = JSON.parse(body);
					if(jsonData.rsp_code == 'succ' && jsonData.data.list.length > 0) {
						ep.emit('getDefaultStoreId', jsonData.data.list[0].stores_id);
					} else {
						ep.emit('getChatId', chatData);
					}
				} else {
					ep.emit('getChatId', chatData);
				}
			} catch(e) {
				ep.emit('getChatId', chatData);
			}
		});

		ep.once('getDefaultStoreId', function(stores_id) {
			// 查询门店环信id
			var chatIdFormData = {
				service: 'chat.getIMUserAccount',
				params: JSON.stringify({
					member_type_key: 'stores',
					member_id: stores_id
				})
			};
			request.post(appConfig.communityPath, {form: chatIdFormData}, function(error, response, body) {
				try {
					if(!error && response.statusCode == 200) {
						var jsonData = JSON.parse(body);
						if(jsonData.rsp_code == 'succ') {
							chatData.chatId = jsonData.data.im_user_id;
							chatData.title = jsonData.data.nickname;
						}
						ep.emit('getChatId', chatData);
					} else {
						ep.emit('getChatId', chatData);
					}
				} catch(e) {
					ep.emit('getChatId', chatData);
				}
			});
		});

		ep.once('getChatId', function(data) {
			res.send({
				rsp_code: 'succ',
				data: data
			});
		});
	} catch(e) {
		errHandler.systemError(res, e);
	}
});

/*
 * 进入拼团领（新）页面
 */
router.get('/hpt', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		if(requestParams.v) {
			// 查询banner图
			var formData = {
				service: 'gdAppAdvert.app.gdAppAdvertFind',
				params: JSON.stringify({
					category: 'hpt_app'
				})
			};
			request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
				try {
					if(!error && response.statusCode == 200) {
						var jsonData = JSON.parse(body);
						res.render('c/hpt/index', {
							requestParams : requestParams,
							banner: jsonData.data || {doc_url:{}, list:[]}
						});
					} else {
						errHandler.renderErrorPage(res, error);
					}
				} catch(e) {
					errHandler.renderErrorPage(res, e);
				}
			});
		} else {	// 拼团领改版，兼容旧版本，旧版本的APP直接显示无活动
			res.render('c/hpt/index-no', {
				requestParams : requestParams
			});
		}
	} catch(e) {
		errHandler.renderErrorPage(res, e);
	}
});

/*
 * 分页查询拼团领商品列表
 */
router.post('/hpt/queryGoods', function(req, res, next) {
	try {
		var requestParams = req.body;
		if(!requestParams.area_id || requestParams.area_id.length != 6) {
			requestParams.area_id = '100000';
		}
		var formData = {
			service: 'goods.tsGoodsFindForH5',
			params: JSON.stringify({
				area_id: requestParams.area_id,
				label_promotion: requestParams.label_promotion || ''
			}),
			page: requestParams.page
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var jsonData = JSON.parse(body);
					var goodsList = [];
					if(jsonData.rsp_code == 'succ') {
						goodsList = handleImgUrlAndArea(jsonData.data.list, jsonData.data.doc_url)
					}
					res.send({
						goodsList: goodsList,
						page: jsonData.data.page
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
 * 进入拼团领商品收藏列表页面
 */
router.get('/hpt/collect', function(req, res, next) {
	try {
		var requestParams = req.query;

		// 查询收藏列表
		var formData = {
			service: 'psGoodsFavorites.app.tsGoodsFavoritesListPageFind',
			params: JSON.stringify({
				member_id: requestParams.member_id,
				member_type_key: 'consumer'
			})
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var jsonData = JSON.parse(body);
					var goodsList = [];
					if(jsonData.rsp_code == 'succ') {
						goodsList = handleImgUrlAndArea(jsonData.data.list, jsonData.data.doc_url)
					}
					res.render('c/hpt/collect', {
						requestParams: requestParams,
						goodsList: goodsList
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
 * 进入拼团领商品详情页面
 */
router.get('/hpt/detail', function(req, res, next) {
	try {
		var requestParams = req.query;

		var ep = new eventproxy();
		ep.all('queryGoodsDetail', 'queryGroupList', function(goods, group) {
			res.render('c/hpt/detail', {
				requestParams: requestParams,
				goods: goods,
				orderNum: group.orderNum || 0,
				groupList: group.groupList
			});
		});

		// 查询商品详情
		var goodsFormData = {
			service : 'goods.psGoodsDetailFind',
			params : JSON.stringify({
				goods_id : requestParams.goods_id
			})
		};
		request.post(appConfig.goodsPath, {form: goodsFormData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var data = JSON.parse(body);
					var goods = {};
					if(data.rsp_code == 'succ') {
						var goodsList = handleImgUrlAndArea(data.data.list, data.data.doc_url);
						goods = goodsList[0];
					}
					ep.emit('queryGoodsDetail', goods);
				} else {
					errHandler.renderErrorPage(res, error);
				}
			} catch(e) {
				errHandler.renderErrorPage(res, e);
			}
		});

		// 分享的拼团领商品详情页没有经纬度，所以指定为0，这样不会查到任何团购列表，但可以查出已售数量
		requestParams.longitude = requestParams.longitude || '0.00';
		requestParams.latitude = requestParams.latitude || '0.00';

		// 查询团购列表
		var groupFormData = {
			service: 'tsActivity.consumer.nearbyTsActivityListFind',
			params: JSON.stringify({
				goods_id: requestParams.goods_id,
				longitude: requestParams.longitude,
				latitude: requestParams.latitude
			})
		};
		request.post(appConfig.opsorderPath, {form: groupFormData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var data = JSON.parse(body);
					var group = {
						orderNum: 0,
						groupList: []
					};
					if(data.rsp_code == 'succ') {
						group.orderNum = data.data.order_num || 0;
						group.groupList = handleImgUrlAndArea(data.data.list, data.data.doc_url);
					}
					ep.emit('queryGroupList', group);
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
 * 校验拼团领开团权限
 */
router.post('/hpt/createGroupAuth', function(req, res, next) {
	try {
		var requestParams = req.body;
		var formData = {
			service: 'tsActivity.consumer.qualificationValidate',
			params: JSON.stringify({
				consumer_id: requestParams.consumer_id
			})
		};
		request.post(appConfig.opsorderPath, {form: formData}, function(error, response, body) {
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
 * 进入拼团领开团须知页面
 */
router.get('/hpt/help', function(req, res, next) {
	res.render('c/hpt/help');
});

/*
 * 进入拼团领开团须知页面
 */
router.get('/hpt/createGroupRules', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = req.query;
		res.render('c/hpt/createGroupRules', {
			requestParams : requestParams
		});
	} catch(e) {
		errHandler.renderErrorPage(res, e);
	}
});

/*
 * 进入拼团领我要参团页面
 */
router.get('/hpt/joinGroupRules', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = req.query;
		var formData = {
			service: 'tsActivity.consumer.tsActivityCountForH5',
			params: JSON.stringify({
				activity_id: requestParams.activity_id
			})
		};
		request.post(appConfig.opsorderPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var data = JSON.parse(body);
					var activity = {};
					if(data.rsp_code == 'succ') {
						activity = data.data;
						if(activity.doc_url_list) activity.doc_url_list = JSON.parse(activity.doc_url_list);
						res.render('c/hpt/joinGroupRules', {
							requestParams : requestParams,
							activity: activity
						});
					} else {
						errHandler.renderErrorPage(res, data.error_msg, data.error_msg);
					}
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
 * 校验拼团领参团权限
 */
router.post('/hpt/joinGroupAuth', function(req, res, next) {
	try {
		var requestParams = req.body;
		var formData = {
			service: 'tsOrder.consumer.ladderTsOrderValidate',
			params: JSON.stringify({
				member_id: requestParams.member_id,
				goods_id: requestParams.goods_id
			})
		};
		request.post(appConfig.opsorderPath, {form: formData}, function(error, response, body) {
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

/**
 * 进入拼团领物流详情页
 */
router.get('/hpt/logistics', function(req, res, next) {
	try {
		// 物流单号
		var logisticsNumber = req.query.logistics_number;
		// 跳转到快递100物流查询页面
		res.redirect('https://m.kuaidi100.com/index_all.html?postid=' + logisticsNumber.replace('No:', ''));
	} catch(e) {
		errHandler.renderErrorPage(res, e);
	}
});

/*
 * 进入团购预售商品详情页面
 */
router.get('/presell/detail', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		var formData = {
			service : 'order.psGroupGoodsDetailFind',
			params : JSON.stringify({
				invitation_code : requestParams.invitation_code
			})
		};
		request.post(appConfig.opsorderPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var data = JSON.parse(body);
					var goods = {};
					if(data.data && data.data.detail) {
						var list = [];
						list.push(data.data.detail);
						var goodsList = handleImgUrlAndArea(list, data.data.doc_url);
						goods = goodsList[0];
					}
					res.render('c/presell/detail', {
						goods: goods,
						requestParams: requestParams
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
 * 进入品牌领换主页
 */
router.get('/gold-exchange', function(req, res, next) {
	try {
		var requestParams = req.query;
		// 查询banner图
		var formData = {
			service: 'gdAppAdvert.app.gdAppAdvertFind',
			params: JSON.stringify({
				category: 'mph_app'
			})
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var jsonData = JSON.parse(body);
					res.render('c/gold-exchange/brand', {
						area_id: requestParams.area_id,
						banner: jsonData.data || {doc_url:{}, list:[]}
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
 * 分页获取品牌领品牌列表
 */
router.post('/gold-exchange/brandlist', function(req, res, next) {
	try {
		var pageNo = req.body.page_no || 1;
		var pageSize = req.body.page_size || 5;
		var brandList = goldExchangeBrandList.list.slice((pageNo-1)*pageSize, pageNo*pageSize);
		res.send({
			brandList: brandList
		});
	} catch(e) {
		errHandler.systemError(res, e);
	}
});

/*
 * 进入品牌领品牌商品列表页
 */
router.get('/gold-exchange/list', function(req, res, next) {
	try {
		var requestParams = bizUtils.extend(req.query, req.body);
		requestParams.area_id = requestParams.area_id || '-1';
		var formData = {
			service : 'goods.goldExchangeGoodsPageFind',
			params : JSON.stringify({
				area_id : requestParams.area_id,
				label_promotion : requestParams.brand
			})
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var jsonData = JSON.parse(body);
					var goodsList = [];
					if(jsonData.data.list && jsonData.data.list.length != 0) {
						goodsList = handleImgUrlAndArea(jsonData.data.list, jsonData.data.doc_url);
					}
					res.render('c/gold-exchange/list', {
						goodsList : goodsList,
						requestParams : requestParams
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
 * 进入品牌领商品详情页
 */
router.get('/gold-exchange/detail', function(req, res, next) {
	try {
		var requestParams = bizUtils.extend(req.query, req.body);
		var formData = {
			service : 'goods.goldExchangeGoodsDetailFind',
			params : JSON.stringify({
				goods_id : requestParams.goods_id
			})
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var jsonData = JSON.parse(body);
					var goodsList = handleImgUrlAndArea(jsonData.data.list, jsonData.data.doc_url);
					res.render('c/gold-exchange/detail', {
						goods: goodsList[0],
						requestParams: requestParams
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
 * 进入品牌领物流详情页
 */
router.get('/gold-exchange/logistics', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		var order_id = requestParams.order_id || requestParams.order_no;
		// 跳转到公用模块物流查询功能
		res.redirect('/openapi/h5/common/order-logistics?order_type=JBD&order_id='+order_id);
	} catch(e) {
		errHandler.renderErrorPage(res, e);
	}
});

/*
 * 进入权益首页
 */
router.get('/interests', function(req, res, next) {
	try {
		var requestParams = req.query;
		var formData = {
			service: 'consumer.consumer.consumerBaseInfoFind',
			params: JSON.stringify({
				consumer_id: requestParams.member_id
			})
		};
		request.post(appConfig.memberPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var jsonData = JSON.parse(body);
					// console.log("jsonData:",jsonData);
					res.render('c/interests/index', {
						requestParams: requestParams,
						grade: jsonData.data.grade
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
 * 进入权益商品详情页
 */
router.get('/interests/detail', function(req, res, next) {
	try {
		var requestParams = req.query;
		var formData = {
			// service: 'goods.psGoodsDetailFind',
			service: 'gdActivity.consumer.gdActivityDetailFind',
			params: JSON.stringify({
				goods_id: requestParams.goods_id,
				//
				activity_id:requestParams.activity_id
			})
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			// console.log("body:",body);
			try {
				if(!error && response.statusCode == 200) {
					var data = JSON.parse(body);
					var goods = {};
					if(data.data && data.data.list.length != 0) {
						var goodsList = handleImgUrlAndArea(data.data.list, data.data.doc_url);
						goods = goodsList[0];
					}
					// console.log('goods:',JSON.stringify(goods));
					res.render('c/interests/detail', {
						goods: goods,
						requestParams: requestParams
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
 * 权益领取商品选择券
 */
router.get('/interests/chooseTicket', function(req, res, next) {
	try {
		var requestParams = req.query;
		res.render('c/interests/chooseTicket', {
			requestParams: requestParams
		});
	} catch(e) {
		errHandler.renderErrorPage(res, e);
	}
});

/*
 * 领取权益商品
 */
router.post('/interests/getGoods', function(req, res, next) {
	try {
		var requestParams = req.body;
		var formData = {
			service: 'gdActivity.consumer.gdActivityGet',
			params: JSON.stringify({
				activity_id: requestParams.activity_id,
				goods_id: requestParams.goods_id,
				member_id: requestParams.member_id,
				member_type_key: 'consumer',
				member_grade: requestParams.member_grade,
				member_mobile: requestParams.member_mobile,
				benefit_id: requestParams.benefit_id
			})
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var data = JSON.parse(body);
					res.send(data);
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
 * 进入权益历史页面
 */
router.get('/interests/history', function(req, res, next) {
	try {
		var requestParams = req.query;
		var formData = {
			service: 'gdBenefit.consumer.gdBenefitLogListPageFind',
			params: JSON.stringify({
				member_id: requestParams.member_id
			})
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var data = JSON.parse(body);
					var historyList = [];
					if(data.rsp_code == 'succ') historyList = data.data.list;
					res.render('c/interests/history', {
						historyList: historyList,
						requestParams: requestParams
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
 * 查询权益券
 */
router.post('/interests/queryTickets', function(req, res, next) {
	try {
		var requestParams = req.body;
		var formData = {
			service: 'gdBenefit.consumer.gdBenefitListPageFind',
			params: JSON.stringify({
				member_id: requestParams.member_id
			})
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var jsonData = JSON.parse(body);
					res.send({
						ticketList: jsonData.data.list || []
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
 * 分页查询权益商品
 */
router.post('/interests/queryGoods', function(req, res, next) {
	try {
		var requestParams = req.body;
		var formData = {
			service: 'gdActivity.consumer.gdActivityListPageFind',
			params: JSON.stringify({
				limited_grade: requestParams.grade || '2'
			}),
			page: JSON.stringify({
				page_no: requestParams.page_no || '1',
				page_size: requestParams.page_size || '5'
			})
		};
		request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
			try {
				if(!error && response.statusCode == 200) {
					var jsonData = JSON.parse(body);
					var goodsList = handleInterestsGoodsImgUrlAndExpireDate(jsonData.data.doc_url, jsonData.data.list)
					res.send({
						goodsList: goodsList
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

// 处理权益商品图片url
function handleInterestsGoodsImgUrlAndExpireDate(map, goodsList) {
	for(var i=0; i<goodsList.length; i++) {
		// 处理图片
		if(goodsList[i].json_data) {
			var picUrlArr = [];
			var json_data = JSON.parse(goodsList[i].json_data);
			for(var j=0; j<json_data.length; j++) {
				picUrlArr.push(map[json_data[j].path_id]);
			}
			goodsList[i].picUrlArr = picUrlArr;
		}
	}
	return goodsList;
}

/*
 * 收藏商品(淘淘领/拼团领)
 * @param member_id			消费者id
 * @param favorites_type	收藏类别（淘淘领：llm，拼团领：hpt）
 * @param goods_id 			商品id
 * @param valid_thru		商品有效期
 */
router.post('/collectGoods', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		var formData = {
			service: 'psGoodsFavorites.app.psGoodsFavoritesCreate',
			params: JSON.stringify({
				member_type_key: 'consumer',
				favorites_type: requestParams.favorites_type,
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
 * 取消收藏商品(淘淘领/拼团领)
 * @param member_id			消费者id
 * @param goods_id 			商品id
 * @param valid_thru		商品有效期
 */
router.post('/unCollectGoods', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);
		var formData = {
			service: 'psGoodsFavorites.app.psGoodsFavoritesCancel',
			params: JSON.stringify({
				member_type_key: 'consumer',
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
 * 进入兑换页面
 */
router.get('/exchange', function(req, res, next) {
	res.redirect('http://h.meitianhui.com/wap/index_exchange');
});

/*
 * 消费者APP社区地址重定向
 */
router.get('/bbs', function(req, res, next) {
	res.redirect(appConfig.bbsPath);
});

/*
 * 分享地址
 */
router.get('/share', function(req, res, next) {
	res.redirect('http://www.meitianhui.com/app');
});

/*
 * 分享亲情卡
 */
router.get('/share/prepaycard', function(req, res, next) {
	var requestParams = req.query;
	res.render('c/share/prepaycard', {
		card_no : requestParams.card_no || ''
	});
});

/*
 * 朋友圈
 */
router.get('/community', function(req, res, next) {
	req.url = req.url.replace('community', '');
	res.redirect(appConfig.communityHost + req.url);
});

/*
 * 朋友圈--我的
 */
router.get('/community/mine', function(req, res, next) {
	req.url = req.url.replace('community/', '');
	res.redirect(appConfig.communityHost + req.url);
});

/*
 * 今日头条
 */
router.get('/dailynews', function(req, res, next) {
	var requestParams = req.query;
	res.render('c/dailynews/index');
});

/*
 * 生活服务
 */
router.get('/lifeservice', function(req, res, next) {
	var requestParams = req.query;
	res.render('c/lifeservice/index', {
		member_id: requestParams.member_id
	});
});

/*
 * 合作商家
 */
router.get('/businessPartner', function(req, res, next) {
	res.redirect('https://cms.meitianhui.com/?p=2329');
});

// 展示页面
// //根据商品标签查询商品列表
//1。从首页轮播banner图片点进去的，有图片的
//2.从新人攻略点进去的
// https://appportal.meitianhui.com/openapi/h5/c/tag?area_id=440304&mi=a661d6b6f5254c61bf1d251f1a490402&mobile=13682382057&stores_name=%E5%87%8C%E5%8D%97%E6%B5%8B%E8%AF%95%E5%BA%97&contact_person=%E5%87%8C%E5%8D%97&bind=1
router.get('/tag',function(req,res,next){
	// 请求参数
		var requestParams = Object.assign({},req.query, req.body);
		res.render('c/tag/list', {
			requestParams: requestParams
		});
});



module.exports = router;
