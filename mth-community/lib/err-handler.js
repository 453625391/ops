/**
 * 错误处理
 * @author Changefeng
 */
var logger = require('./log4js').getLogger('base-router');

module.exports = {

	/**
	 * 参数缺失
	 * res: 客户端响应
	 */
	incompleteParams : function(res) {
		logger.info('incomplete params');
		res.send({
			rsp_code: 'fail',
			error_code: 'incomplete params',
			error_msg: '参数缺失'
		});
		res.end();
	},

	/**
	 * 非法参数
	 * res: 客户端响应
	 */
	invalidParams : function(res) {
		logger.info('invalid params');
		res.send({
			rsp_code: 'fail',
			error_code: 'invalid params',
			error_msg: '非法参数'
		});
		res.end();
	},

	/**
	 * 非法签名
	 * res: 客户端响应
	 */
	invalidSign : function(res) {
		res.send({
			rsp_code: 'fail',
			error_code: 'invalid sign',
			error_msg: '非法签名'
		});
		res.end();
	},

	/**
	 * app_token错误
	 * res: 客户端响应
	 */
	appTokenError : function(res) {
		logger.info('invalid app_token');
		res.send({
			rsp_code: 'fail',
			error_code: 'invalid app_token',
			error_msg: '无效的app_token'
		});
		res.end();
	},

	/**
	 * user_token错误
	 * res: 客户端响应
	 */
	userTokenError : function(res) {
		logger.info('invalid user_token');
		res.send({
			rsp_code: 'fail',
			error_code: 'invalid user_token',
			error_msg: '无效的user_token'
		});
		res.end();
	},

	/**
	 * 系统错误
	 * res: 客户端响应
	 */
	systemError : function(res, error) {
		logger.error('SYSTEM ERROR:' + error);
		res.send({
			rsp_code: 'fail',
			error_code: 'system error',
			error_msg: '系统错误'
		});
		res.end();
		return;
	},

	/**
	 * 渲染到错误页面
	 * res: 客户端响应
	 */
	renderToErrPage : function(res, errMsg) {
		res.render('community/error', {
			errMsg : errMsg
		});
		res.end();
		return;
	}

}