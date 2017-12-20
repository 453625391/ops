var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var moment = require('moment');
moment.locale('zh-cn');
var db = require('../db');

// 评论
var commentSchema = new Schema({
	// 评论人ID
	commenter_id : String,
	// 评论人昵称
	commenter_nick_name : String,
	// 评论人头像路径
	commenter_head_path : String,
	// 被评论人ID
	be_commented_id : String,
	// 被评论人昵称
	be_commented_nick_name : String,
	// 评论内容
	comment_content : String,
	// 评论时间
	comment_time : {type : Date, default : Date.now}
}, {
	toJSON: {virtuals: true},
	toObject: {virtuals: true}
});

// 格式化的评论时间
commentSchema.virtual('commentTimeFormatted').get(function() {
	var from = moment(this.comment_time).fromNow();
	if(from.indexOf('月') != -1 || from.indexOf('年') != -1 || (from.indexOf('天') != -1 && parseInt(from) > 3)) {	// 3天以上的直接显示日期
		return moment(this.comment_time).format('YYYY-MM-DD');
	} else {
		return from;
	}
});

// 点赞
var likeSchema = new Schema({
	// 点赞人id
	liker_id : String,
	// 点赞人昵称
	liker_nick_name : String,
	// 点赞人头像路径
	liker_head_path : String,
	// 点赞时间
	like_time : {type : Date, default : Date.now}
});

// 社区文章
var articleSchema = new Schema({
	// 主题类型（share , topic, sign）
	theme_type : String,
	// 作者ID
	member_id : String,
	// 作者昵称
	nick_name : String,
	// 头像路径
	head_path : String,
	// 标题
	title : String,
	// 主题正文内容
	content : String,
	// 封面图路径
	cover_path : Array,
	// 内容插图路径
	pic_path : Array,
	// 主题状态（1-有效 , 0-无效）
	status : {type : String, default : '1'},
	// 地址
	location : String,
	// 经度
	longitude : String,
	// 纬度
	latitude : String,
	// 查看次数
	view_count : {type : Number, default : 0},
	// 家乡圈
	home_circle : String,
	// 工作圈
	work_circle : String,
	// 生活圈
	life_circle : String,
	// 兴趣圈
	hobby_circle : String,
	// 创建时间
	create_date : {type : Date, default : Date.now},
	// 备注
	remark : String,
	// 评论
	comment : [commentSchema],
	// 赞
	like : [likeSchema]
}, {
	toJSON: {virtuals: true},
	toObject: {virtuals: true}
});
// articleSchema.set('toJSON', {virtuals: true});
// articleSchema.set('toObject', {virtuals: true});

// articleSchema.add({
// 	isAlreadyLiked: 'string'
// });

// 格式化的创建时间
articleSchema.virtual('createDateFormatted').get(function() {
	var from = moment(this.create_date).fromNow();
	if(from.indexOf('月') != -1 || from.indexOf('年') != -1 || (from.indexOf('天') != -1 && parseInt(from) > 3)) {	// 3天以上的直接显示日期
		return moment(this.create_date).format('YYYY-MM-DD');
	} else {
		return from;
	}
});

// 创建ArticleModel
var ArticleModel = db.model('ArticleModel', articleSchema);

module.exports = {
	// 保存article
	save : function(doc, callback) {
		var article = new ArticleModel(doc);
		article.save(callback);
	},

	// 根据条件更新article
	update : function(conditions, update, callback) {
		ArticleModel.update(conditions, update, {upsert:false}, function(err) {
			callback(err);
		});
	},

	// 根据ID删除article
	deleteById : function(id, callback) {
		ArticleModel.remove({_id : id}, callback);
	},

	// 查询所有article
	findAll : function(callback) {
		ArticleModel.find(callback);
	},

	// 根据类型查询
	findArticleByCondition : function(condition, page, callback) {
		if(typeof page == 'function') {
			callback = page;
			page = {
				page_no : 1,
				page_size : 100
			};
		}
		ArticleModel.find(condition, function(err, result) {
			callback(err, result);
		}).sort({
			create_date : -1
		}).skip(parseInt(page.page_no-1)*parseInt(page.page_size)).limit(parseInt(page.page_size));
	},

	// 根据id查询
	findArticleById : function(id, callback) {
		ArticleModel.find({_id: id}, function(err, result) {
			if(!err && result) {
				// 更新查看次数
				result[0].view_count++;
				ArticleModel.update({_id:result[0]._id}, result[0], function(err){});	// 此处回调不做任何处理，不影响主流业务
			}
			callback(err, JSON.parse(JSON.stringify(result)));
		});
	},

	// 根据条件查询数量
	findCountByCondition(condition, callback) {
		ArticleModel.count(condition, function(err, result) {
			callback(err, result);
		});
	}
}