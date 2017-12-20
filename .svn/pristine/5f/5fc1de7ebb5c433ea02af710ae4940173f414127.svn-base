/**
 *  http请求签名验证中间件
 *
 */
var openapi = require('../service/openapi');
var error = require('../lib/error');
var cons = require('../lib/constant');
var tool = require('../lib/tool');
var debug = require('debug')('mth-salesassistant:middleware:apptokenSignValidate');

module.exports = function(req,res,next){
  var params = req.allParams;
  // app_token=6d5ee225c88d7dad838083e43b0459dfa84594be99a5d55b983657bf55d4120815dc1cefd837c6aac786f736cd3a24ee&
  // mobile=15918651135&
  // security_code=r1DwhEFo&
  // sign=A27762314E92DD4B51A0936BBCFCBBD8&
  // sms_source=54R02MASMF349
  debug("params:",params);
  if(!params.app_token || !params.sign){
    throw new error.GenericError(cons.ERROR_NO.INCOMPLETEPARAMS);
  }
  // return next();
  openapi.requestJavaInterface(cons.service.APPVALIDATE,
    cons.url.INFRASTRUCTURE,{app_token:params.app_token,request_info:req.ip}).then(body=>{
      if(body.rsp_code === 'fail'){
        // throw new error.GenericError(-1,'无效签名');
        res.send(body);
        return;
      }
      if(!tool.validSign(params,body.data.security_code)){
        throw new error.GenericError(-1,'无效签名');
      }
        next();
    }).catch(next);
};
