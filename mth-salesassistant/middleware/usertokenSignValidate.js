/**
 *  http请求签名验证中间件
 *
 */
var openapi = require('../service/openapi');
var error = require('../lib/error');
var cons = require('../lib/constant');
var tool = require('../lib/tool');

module.exports = function(req,res,next){
  var params = req.allParams;
  if(!params.user_token || !params.sign){
    throw new error.GenericError(cons.ERROR_NO.INCOMPLETEPARAMS);
  }
  openapi.requestJavaInterface(cons.service.USERVALIDATE,
    cons.url.INFRASTRUCTURE,{user_token:user_token}).then(body=>{
      if(body.rsp_code === 'fail'){
        throw new error.GenericError(-1,'无效签名');
      }
      if(!tool.validSign(params,body.data.security_code)){
        throw new error.GenericError(-1,'无效签名');
      }
        next();
    });
};
