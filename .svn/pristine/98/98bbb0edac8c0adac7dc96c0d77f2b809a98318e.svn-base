/**
 * 工具类
 * @author Changefeng
 */
var request = require('request');
var ipware = require('ipware')();
var md5 = require('md5');
var appConfig = require('../app-config');
var errHandler = require('./err-handler');
var logger = require('./log4js').getLogger('biz-util');

module.exports = {
	/**
	 * 合并参数，将后一个参数合并到前一个参数中
	 */
	extend : function() {
		var src, copy, name, options, 
		target = arguments[0] || {},
		i = 1,
		length = arguments.length;

		// Handle case when target is a string or something (possible in deep copy)
		if ( typeof target !== "object" ) {
			target = {};
		}

		for ( ; i < length; i++ ) {
			// Only deal with non-null/undefined values
			if ( (options = arguments[ i ]) != null ) {
				// Extend the base object
				for ( name in options ) {
					src = target[ name ];
					copy = options[ name ];

					// Prevent never-ending loop
					if ( target === copy ) {
						continue;
					}

					if ( copy !== undefined ) {
						target[ name ] = copy;
					}
				}
			}
		}

		// Return the modified object
		return target;
	},

	/**
	 * 克隆对象
	 */
	clone : function(obj) {
		var objS = JSON.stringify(obj);
		return JSON.parse(objS);
	},

	/**
	 * 获取当前年月yyyy-MM
	 */
	getCurrentYearMonth : function() {
		var now = new Date();
		var month = now.getMonth() < 9 ? ('0'+(now.getMonth()+1)) : (now.getMonth()+1);
		return now.getFullYear() + '-' + month;
	},

	getCurrentDate : function() {
		var now = new Date();
		var month = now.getMonth() < 9 ? ('0'+(now.getMonth()+1)) : (now.getMonth()+1);
		var day = now.getDate() < 10 ? ('0'+now.getDate()) : now.getDate();
		return now.getFullYear() + '-' + month + '-' + day;
	}
}