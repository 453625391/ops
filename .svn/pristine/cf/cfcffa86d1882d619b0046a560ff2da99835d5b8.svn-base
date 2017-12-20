var md5 = require('md5');
var debug = require('debug')('mth-salesassistant:lib:tool');
//验证appToken签名
exports.validSign = function(reqParams,securityCode){
  var params = Object.assign({},reqParams);
  delete params.request_info;  // 刪除多余的自定义参数
  delete params.platform;  // 刪除多余的自定义参数 android
  delete params.version;  // 刪除多余的自定义参数 android

  debug('params: %j  securityCode: %s',params,securityCode);
  var singStr = params.sign;
  delete params.sign;
  debug("after remove sign's params:",params);
  var keyArr  = Object.keys(params);
  // 将所有key按字母a~z排序
  keyArr.sort();
  var tmpArray = [];
  for(var i=0; i<keyArr.length; i++) {
    var val = params[keyArr[i]];
    if(typeof val != 'string') {
      val = JSON.stringify(val);
    }
    tmpArray.push(keyArr[i] + '=' + val);
  }

  // 待加密字符串
  var stringToBeMd5 = tmpArray.join('&') + securityCode;
  debug('stringToBeMd5: %s',stringToBeMd5);
  // MD5加密
  var thisSign = md5(stringToBeMd5);
  if(thisSign.toUpperCase() != singStr.toUpperCase()) {	// 签名错误，记录日志
    debug('invalid sign');
    debug('sign from request:' + singStr);
    debug('correct sign:' + thisSign);
  }

  return thisSign.toUpperCase() === singStr.toUpperCase();
};
