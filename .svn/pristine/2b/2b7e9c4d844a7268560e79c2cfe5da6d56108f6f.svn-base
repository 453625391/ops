/**
 * 必填参数验证
 */
var error = require('../lib/error');
var debug = require('debug')('mth-salesassistant:lib:notEmptyValidate');
module.exports = function(){
  var args = Array.prototype.slice.call(arguments,0);
  // debug('args:',args);
  var emptyParams =[];
  return function(req,res,next){
    var params = req.allParams;
    emptyParams = args.filter(arg=>!params[arg]);
    if(emptyParams.length){
      throw new error.GenericError(-1,emptyParams+'参数不能为空');
    }
    next();
  };
};
