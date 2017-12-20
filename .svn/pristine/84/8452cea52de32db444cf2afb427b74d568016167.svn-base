var express = require('express');
var router = express.Router();
var url = require('url');
var request = require('request');

var logger = require('../lib/log4js').getLogger('community-router');
var appConfig = require('../app-config');
var errHandler = require('../lib/err-handler');
var bizUtils = require('../lib/biz-utils');

var STATE_CHANGE_URL = appConfig.communityPath.split('ops-community')[0] + 'ops-community/liveroom/notify/stateChange';
var PLAYBACK_URL = appConfig.communityPath.split('ops-community')[0] + 'ops-community/liveroom/notify/playback';

/*
 * 直播社区类接口转发
 * @param {}
 */
router.all('/', function(req, res, next) {
	try {
		var requestParams = bizUtils.extend(req.query, req.body);

		// 验证请求参数是否完整
		if((!requestParams.app_token && !requestParams.user_token) || !requestParams.sign || !requestParams.service || !requestParams.params) {
			errHandler.incompleteParams(res);
			return;
		}

		if(requestParams.app_token) {
			// 校验app_token
			bizUtils.validAppToken(req, res, requestParams.app_token, function(tokenValidData) {
				// 根据返回的security_code校验签名
				if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
					errHandler.invalidSign(res);
					return;
				}
				// 转发接口
				var formData = {
					service : requestParams.service,
					params : requestParams.params
				};
				if(requestParams.page) formData.page = requestParams.page;	// 加上分页参数
				request.post(appConfig.communityPath, {form: formData}, function(error, response, body) {
					try {
						if(!error && response.statusCode == 200) {
							res.send(JSON.parse(body));
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
			});
		} else {
			// 校验user_token
			bizUtils.validUserToken(req, res, requestParams.user_token, function(tokenValidData) {
				// 根据返回的security_code校验签名
				if(!bizUtils.validSign(bizUtils.clone(requestParams), tokenValidData.data.security_code)) {
					errHandler.invalidSign(res);
					return;
				}

				if(requestParams.service == 'chat.getAddibleUsersForChatGroup') {	// 获取可添加至群聊的会员列表，params: stores_id + group_id
					var bizParams = JSON.parse(requestParams.params);
					// 查询店东相关的消费者
					var relatedConsumersFormData = {
						service: 'member.storesRelConsumerList',
						params: JSON.stringify({
							stores_id: bizParams.stores_id
						})
					};
					request.post(appConfig.memberPath, {form: relatedConsumersFormData}, function(error1, response1, relatedConsumersBody) {
						try {
							if(!error1 && response1.statusCode == 200) {
								var relatedConsumers = JSON.parse(relatedConsumersBody);
								if(relatedConsumers.rsp_code == 'fail') {
									res.send(relatedConsumers);
									return;
								}
								if(!relatedConsumers.data.list || relatedConsumers.data.list.length == 0) {	// 如果未查询到与店东相关的消费者，则直接返回空列表
									res.send({
										rsp_code: 'succ',
										data: {
											list: []
										}
									});
									return;
								}

								// 查询群组中已存在的用户
								var existUsersFormData = {
									service: 'chat.getChatGroupUsers',
									params: JSON.stringify({
										group_id: bizParams.group_id
									})
								};
								request.post(appConfig.communityPath, {form: existUsersFormData}, function(error2, response2, existUsersBody) {
									try {
										if(!error2 && response2.statusCode == 200) {
											var existUsers = JSON.parse(existUsersBody);
											if(existUsers.rsp_code == 'fail') {
												res.send(existUsers);
												return;
											}
											var addibleUserList = userFilterForAddible(relatedConsumers.data.list, existUsers.data);
											res.send({
												rsp_code: 'succ',
												data: {
													doc_url: addibleUserList.length > 0 ? relatedConsumers.data.doc_url : [],
													list: addibleUserList
												}
											});
											return;
										} else {
											errHandler.systemError(res, error2);
											return;
										}
									} catch(e) {
										errHandler.systemError(res, e);
										return;
									}
								});
							} else {
								errHandler.systemError(res, error1);
								return;
							}
						} catch(e) {
							errHandler.systemError(res, e);
							return;
						}
					});
				} else {
					// 转发接口
					var formData = {
						service : requestParams.service,
						params : requestParams.params
					};
					if(requestParams.page) formData.page = requestParams.page;	// 加上分页参数
					request.post(appConfig.communityPath, {form: formData}, function(error, response, body) {
						try {
							if(!error && response.statusCode == 200) {
								res.send(JSON.parse(body));
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
				}
			});
		}
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

/*
 * 直播状态变更回调转发
 * @param {}
 */
router.get('/notify/stateChange', function(req, res, next) {
	try {
		request.post(STATE_CHANGE_URL, {form: req.query}, function(error, response, body) {
			if(!error && response.statusCode == 200) {
				res.send(body);
			} else {
				errHandler.systemError(res, error);
				return;
			}
		});
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

/*
 * 回播地址通知回调转发
 * @param {}
 */
router.get('/notify/playback', function(req, res, next) {
	try {
		request.post(PLAYBACK_URL, {form: req.query}, function(error, response, body) {
			if(!error && response.statusCode == 200) {
				res.send(body);
			} else {
				errHandler.systemError(res, error);
				return;
			}
		});
	} catch(e) {
		errHandler.systemError(res, e);
		return;
	}
});

function userFilterForAddible(relatedUserList, existUserList) {
	if(!existUserList || existUserList.length == 0) {
		return relatedUserList || [];
	}
	var addibleUserList = [];
	var addibleFlag = true;
	for(var i=0; i<relatedUserList.length; i++) {
		addibleFlag = true;
		for(var j=0; j<existUserList.length; j++) {
			if(existUserList[j].member_type_key == 'consumer' && existUserList[j].member_id == relatedUserList[i].consumer_id) {
				addibleFlag = false;
				break;
			}
		}
		if(addibleFlag) {
			addibleUserList.push(relatedUserList[i]);
		}
	}
	return addibleUserList;
}

module.exports = router;