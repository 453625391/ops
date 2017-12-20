var express = require('express');
var router = express.Router();
var url = require('url');
var request = require('request');
var ipware = require('ipware')();

var logger = require('../lib/log4js').getLogger('goods-router');
var appConfig = require('../app-config');
var errHandler = require('../lib/err-handler');
var bizUtils = require('../lib/biz-utils');
var wypCategory = require('../lib/wyp-category');
var pplBrandList = require('../lib/ppl-brand-list');

/*
 * 商品类接口
 * @param app_token / user_token 令牌（运营管理系统调用传递app_token，APP调用传递user_token）
 * @param service 服务名称
 * @param params 业务参数集，json格式的字符串
 * @param page 分页参数集，json格式字符串（非必填）
 * @param sign 签名
 */
router.all('/', function(req, res, next) {
	try {
		// 请求参数
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if((!requestParams.app_token && !requestParams.user_token) 
			|| !requestParams.service || !requestParams.params || !requestParams.sign) {
			errHandler.incompleteParams(res);
			return;
		}

		if(requestParams.app_token) {
			// 验证app_token
			bizUtils.validAppToken(req, res, requestParams.app_token, function(tokenValidData) {
				// 根据返回的security_code校验签名
				if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
					errHandler.invalidSign(res);
					return;
				}
				if(requestParams.service == 'goods.pplBrandPageFind') {	// 分页查询品牌领品牌列表
					var page = JSON.parse(requestParams.page) || {};
					var pageNo = page.page_no || 1;
					var pageSize = page.page_size || 10;
					var brandList = pplBrandList.list.slice((pageNo-1)*pageSize, pageNo*pageSize);
					res.send({
						rsp_code: 'succ',
						data: {
							list: brandList,
							page: {
								page_no: pageNo,
								page_size: pageSize,
								total_count: pplBrandList.list.length + '',
								total_page: Math.ceil(pplBrandList.list.length / pageSize) + ''
							}
						}
					});
				} else {	// 进行商品相关业务
					var formData = {
						service : requestParams.service,
						params : requestParams.params
					};
					if(requestParams.page) {	// 若请求有传递分页参数
						formData.page = requestParams.page;
					}
					request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
						if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
							res.send(JSON.parse(body));
						} else {
							errHandler.systemError(res, error);
							return;
						}
					});
				}
			});
		} else {
			// 验证user_token
			bizUtils.validUserToken(req, res, requestParams.user_token, function(tokenValidData) {
				// 根据返回的security_code校验签名
				if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
					errHandler.invalidSign(res);
					return;
				}
				if(requestParams.service == 'goods.wypGoodsCategoryFind') {	// 查询我要批商品分类
					var categoryList = wypCategory.list;
					res.send({
						rsp_code: 'succ',
						data: {
							list: categoryList
						}
					});
				} else if(requestParams.service == 'goods.wypGoodsDetailFind') {	// 查询我要批商品详情和规格
					// 查询商品详情
					var goodsInfoFormData = {
						service : 'goods.wypGoodsDetailFindForH5',
						params : requestParams.params
					};
					request.post(appConfig.goodsPath, {form: goodsInfoFormData}, function(error, response, infoBody) {
						try {
							if(!error && response.statusCode == 200) {
								var infoResult = JSON.parse(infoBody);
								if(infoResult.rsp_code == 'fail') {
									res.send(infoResult);
									return;
								}
								// 查询商品规格
								var goodsStandardFormData = {
									service: 'goods.psGoodsSkuForStoresFind',
									params: requestParams.params
								}
								request.post(appConfig.goodsPath, {form: goodsStandardFormData}, function(error, response, standardBody) {
									try {
										if(!error && response.statusCode == 200) {
											var standardResult = JSON.parse(standardBody);
											if(standardResult.rsp_code == 'fail') {
												res.send(standardResult);
												return;
											}
											// 将规格信息加入到商品详情信息中并返回结果
											infoResult.data.standard_list = standardResult.data.list;
											res.send(infoResult);
										} else {
											errHandler.systemError(res, error);
											return;
										}
									} catch(e) {
										errHandler.systemError(res, e);
										return;
									}
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
				} else if(requestParams.service == 'goods.batchHandoutCoupon') {	// 批量发放优惠券
					var bizParams = JSON.parse(requestParams.params);
					var memberIdList = bizParams.member_id.split(',');
					for(var i=0; i<memberIdList.length; i++) {
						if(memberIdList[i]) {
							var formData = {
								service: 'goods.couponPresented',
								params: JSON.stringify({
									member_id: memberIdList[i],
									member_type_key: 'consumer',
									item_id: bizParams.item_id,
									num: '1'
								})
							};
							request.post(appConfig.goodsPath, {form: formData});
						}
					}
					// 不关注发放结果，直接返回成功
					res.send({
						rsp_code: 'succ',
						data: {}
					});
				} else {
					// 进行商品相关业务
					var formData = {
						service : requestParams.service,
						params : requestParams.params
					};
					if(requestParams.page) {	// 若请求有传递分页参数
						formData.page = requestParams.page;
					}
					request.post(appConfig.goodsPath, {form: formData}, function(error, response, body) {
						if(!error && response.statusCode == 200) {	// 请求成功后直接返回查询结果
							res.send(JSON.parse(body));
						} else {
							errHandler.systemError(res, error);
							return;
						}
					});
				}
			});
		}
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

module.exports = router;