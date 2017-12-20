var express = require('express');
var router = express.Router();
var session = require('express-session');
var url = require('url');
var Q = require('q');
var request = require('request');
var moment = require('moment');
moment.locale('zh-cn');

var logger = require('../lib/log4js').getLogger('community-router');
var appConfig = require('../app-config');
var errHandler = require('../lib/err-handler');
var bizUtils = require('../lib/biz-utils');
var communitySchema = require('../mongo/community/schema/articleSchema');

/*
 * 进入社区首页
 */
router.get('/', function(req, res, next) {
	try {
		var requestParams = req.query;
		switch(requestParams.cate) {
			case 'life':
				requestParams.circle = 'life_circle';
				break;
			case 'work':
				requestParams.circle = 'work_circle';
				break;
			case 'hometown':
				requestParams.circle = 'home_circle';
				break;
			case 'hobby':
				requestParams.circle = 'hobby_circle';
			default:
				requestParams.circle = null;
		}
		res.render('community/index', {
			requestParams : requestParams,
			memberInfo : global.userInfoMap[req.query.mi]
		});
	} catch(e) {
		errHandler.renderToErrPage(res);
	}
});

// 根据ID查询会员信息
var getMemberInfo = function(member_id) {
	var deferred = Q.defer();

	var consumerInfoFormData = {
		service : 'member.consumerFind',
		params : JSON.stringify({
			member_id : member_id
		})
	};
	request.post(appConfig.memberPath, {form: consumerInfoFormData}, function(error, response, consumerInfoBody) {
		try {
			var consumerInfoResult = JSON.parse(consumerInfoBody);
			if(!error && response.statusCode == 200 && consumerInfoResult.rsp_code == 'succ') {
				consumerInfoResult.data.member_id = member_id;
				deferred.resolve(consumerInfoResult.data);
			} else {
				deferred.reject('member_not_found');
			}
		} catch (e) {
			deferred.reject('system_error');
		}
	});

	return deferred.promise;
}

// 查询所有分享
var getAllShares = function(data) {
	var deferred = Q.defer();
	communitySchema.findArticleByCondition({theme_type : 'share'}, function(err, result) {
		if(!err) {
			deferred.resolve(result);
		} else {
			deferred.reject('find_themes_error');
		}
	});
	return deferred.promise;
}

/*
 * 分页获取分享列表
 */
router.post('/getShareList', function(req, res, next) {
	try {
		var requestParams = req.body;
		var condition = {
			theme_type: 'share'
		};
		if(requestParams.circle) condition[requestParams.circle] = true;
		communitySchema.findArticleByCondition(condition, {page_no : requestParams.page_no, page_size : 5}, function(err, result) {
			if(!err) {
				res.send({
					articleList : result,
				});
			} else {
				errHandler.renderToErrPage(res);
			}
		});
	} catch(e) {
		errHandler.renderToErrPage(res);
	}
});

/*
 * 进入社区分享详情
 */
router.get('/detail', function(req, res, next) {
	try {
		var requestParams = req.query;
		var mi = req.cookies.sid;
		communitySchema.findArticleById(requestParams.id, function(err, result) {
			try {
				if(!err) {
					// 判断当前用户是否已经点赞过该文章
					var handleLikeResult = handleLike(mi, result[0].like);
					var article = result[0];
					article.isAlreadyLiked = handleLikeResult.isAlreadyLiked || false;
					article.content = result[0].content.replace(/\n/g, '<br>');
					article.content = encodeURIComponent(result[0].content);
					res.render('community/detail', {
						article: article,
						mi: mi
					});
					return;
				} else {
					errHandler.renderToErrPage(res);
				}
			} catch(e) {
				errHandler.renderToErrPage(res);		
			}
		});
	} catch(e) {
		errHandler.renderToErrPage(res);
	}
});

/*
 * 进入社区我的页面
 */
router.get('/mine', function(req, res, next) {
	try {
		var requestParams = req.query;
		var mi = req.cookies.sid;
		// 查询我的分享数量
		var condition = {
			member_id : mi,
			theme_type : 'share'
		}
		communitySchema.findCountByCondition(condition, function(err, count) {
			if(!err) {
				res.render('community/mine', {
					shareCount : count,
					member_id : mi
				});
				return;
			} else {
				errHandler.renderToErrPage(res);
			}
		});
	} catch(e) {
		errHandler.renderToErrPage(res);
	}
});

/*
 * 进入社区发现页面
 */
router.get('/find', function(req, res, next) {
	try {
		var requestParams = req.query;
		res.render('community/find');
		return;
	} catch(e) {
		errHandler.renderToErrPage(res);
	}
});

/*
 * 进入社区我的主页
 */
router.get('/homepage', function(req, res, next) {
	try {
		var requestParams = req.query;

		getMemberInfo(requestParams.mi).then(function(data) {
			// 会员信息查询成功，查询该会员发布的所有分享内容
			communitySchema.findArticleByCondition({theme_type : 'share', member_id : requestParams.mi}, function(err, result) {
				if(!err) {
					res.render('community/homepage', {
						memberInfo : data,
						articleList : result
					});
				} else {
					errHandler.renderToErrPage(res);
				}
			});
		}, function(err) {
			errHandler.renderToErrPage(res);
		});
	} catch(e) {
		errHandler.renderToErrPage(res);
	}
});

/*
 * 进入社区发布页面
 */
router.get('/publish', function(req, res, next) {
	try {
		var requestParams = req.query;
		res.render('community/publish', {
			mi: req.cookies.sid
		});
		return;
	} catch(e) {
		errHandler.renderToErrPage(res);
	}
});

// 发布文章
router.post('/addArticle', function(req, res, next) {
	try {
		var article = req.body;
		var memberInfo = global.userInfoMap[req.cookies.sid];
		// 用户昵称
		article.nick_name = memberInfo.nick_name || memberInfo.full_name || '匿名';
		// 用户头像
		article.head_path = memberInfo.doc_url[memberInfo.head_pic_path];
		// 家乡圈
		if(article.home_circle == 'true') {
			article.home_circle = memberInfo.home_circle;
		}
		// 工作圈
		if(article.work_circle == 'true') {
			article.work_circle = memberInfo.work_circle;
		}
		// 生活圈
		if(article.life_circle == 'true') {
			article.life_circle = memberInfo.life_circle;
		}
		// 兴趣圈
		if(article.hobby_circle == 'true') {
			article.hobby_circle = memberInfo.hobby_circle;
		}
		// 保存文章
		communitySchema.save(article, function(err, result) {
			if(!err) {
				res.send({
					rsp_code : 'succ',
					data : {}
				});
				return;
			} else {
				errHandler.renderToErrPage(res);
			}
		});
	} catch(e) {
		errHandler.renderToErrPage(res);
	}
});

/*
 * 发表评论
 */
router.post('/addComment', function(req, res, next) {
	try {
		var requestParams = req.body;
		var memberInfo = global.userInfoMap[req.cookies.sid];
		var comment = {};
		comment.commenter_id = memberInfo.member_id;
		comment.commenter_nick_name = memberInfo.nick_name;
		comment.commenter_head_path = memberInfo.doc_url[memberInfo.head_pic_path];
		comment.comment_content = requestParams.comment_content;

		// 查询被评论人的信息
		communitySchema.findArticleById(requestParams.id, function(err, result) {
			try {
				if(!err) {
					comment.be_commented_id = result[0].member_id;
					comment.be_commented_nick_name = result[0].nick_name;
					result[0].comment.push(comment);
					// 保存评论
					communitySchema.update({_id : requestParams.id}, {$set: {comment : result[0].comment}}, function(err) {
						if(!err) {
							res.send({
								rsp_code : 'succ',
								data : comment
							});
							return;
						} else {
							errHandler.renderToErrPage(res);
						}
					});
				} else {
					errHandler.renderToErrPage(res);
				}
			} catch(e) {
				errHandler.renderToErrPage(res);
			}
		});
	} catch(e) {
		errHandler.renderToErrPage(res);
	}
});

/*
 * 点赞 / 取消点赞
 */
router.post('/like', function(req, res, next) {
	try {
		var requestParams = req.body;
		var memberInfo = global.userInfoMap[req.cookies.sid];
		var like = {};
		communitySchema.findArticleById(requestParams.id, function(err, result) {
			try {
				if(!err) {
					var handleLikeResult = handleLike(memberInfo.member_id, result[0].like);
					if(handleLikeResult.isAlreadyLiked) {	// 取消点赞
						result[0].like = handleLikeResult.modifiedLike;
					} else {	// 点赞
						like.liker_id = memberInfo.member_id;
						like.liker_nick_name = memberInfo.nick_name;
						like.liker_head_path = memberInfo.doc_url[memberInfo.head_pic_path];
						result[0].like.push(like);
					}
					communitySchema.update({_id : requestParams.id}, {$set: {like : result[0].like}}, function(err) {
						if(!err) {
							res.send({
								rsp_code : 'succ',
								data : {
									isLike : !handleLikeResult.isAlreadyLiked,
									like: result[0].like
								}
							});
							return;
						} else {
							errHandler.renderToErrPage(res);
						}
					});
				} else {
					errHandler.renderToErrPage(res);
				}
			} catch(e) {
				errHandler.renderToErrPage(res);
			}
		});
	} catch(e) {
		errHandler.renderToErrPage(res);
	}
});

// 判断是否已点赞
function handleLike(liker_id, likeArr) {
	var modifiedLike = bizUtils.clone(likeArr);
	var result = {
		isAlreadyLiked : false,
		modifiedLike : modifiedLike
	};
	for(var i=0; i<modifiedLike.length; i++) {
		if(modifiedLike[i].liker_id == liker_id) {
			result.isAlreadyLiked = true;
			modifiedLike.splice(i, 1);
			break;
		}
	}
	return result;
}

module.exports = router;