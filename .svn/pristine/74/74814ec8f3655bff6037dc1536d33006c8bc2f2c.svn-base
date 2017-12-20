var request = require('request');
var rp = require('request-promise');
var nconf = require('nconf');
var error = require('../lib/error');
var javaServiceHost= nconf.get('service:appprotalHost');
var debug = require('debug')('mth-salesassistant:service:appprotal');
var log4js = require('log4js');
var logger = log4js.getLogger('http');

//openapi
exports.requestJavaInterface = function(service,path,params){
  debug('appportal params:%j',params);
  logger.info('service:%s path:%s params:%j',service,path,params);
  var curr = Date.now();
    var formData = {
      service : service,
      // params : JSON.stringify(params,function(key,val){
      //   if(key=='page'){
      //     return JSON.parse(val);
      //   }
      //   return val;
      // })
    };
    if(params.page){
      formData.page = params.page;
      delete params.page;
    }
    formData.params = JSON.stringify(params);

    debug('appportal formData:%j',formData);
    return rp({
      method:'POST',
      timeout:10000,
      uri:javaServiceHost + path,
      form: formData,
      // The resolveWithFullResponse options allows to pass the full response:
      resolveWithFullResponse:true,
      json:true
    }).then(response=>{
       // Access response.statusCode, response.body etc.
       debug('response.statusCode:',response.statusCode);
      //  if(response.statusCode >= 500){ //服務器錯誤
      //    throw new error.GenericError(-2, `java服務器錯誤 service:${service} path:${path} params:${params}`);
      //  }
       return response.body;
    });
};
