var request = require('request');
var rp = require('request-promise');
var nconf = require('nconf');
var error = require('../lib/error');
var hydHost=nconf.get('service:hydHost');
var debug = require('debug')('mth-salesassistant:service:hyd');

function getFormattedDate(){
  var now = new Date();
  var month = now.getMonth()+1;
  month = month>9?month:'0'+month;
  var day = now.getDate();
  day = day>9?day:'0'+day;
  var hour = now.getHours();
  hour = hour>9?hour:'0'+hour;
  var min = now.getMinutes();
  min = min>9?min:'0'+min;
  var sec = now.getSeconds();
  sec = sec>9?sec:'0'+sec;
  return  ''+now.getFullYear()+'/'+month+'/'+day+' '+hour+':'+min+':'+sec;
}
exports.requestPHPInterface = function(service,params){
  var formData = {
    req_serino:Date.now(),
    // req_time:Date.now(),
    req_time:getFormattedDate(),
    busitype : service,
    content : JSON.stringify(params)
  };
  debug('formData:',formData);
  console.time('php-request');
  return rp({
    method:'POST',
    timeout:10000,
    uri:hydHost+'/trabill',
    form: formData,
    // The resolveWithFullResponse options allows to pass the full response:
    resolveWithFullResponse:true,
    json:true
  }).then(response=>{
    console.timeEnd('php-request');
    // Access response.statusCode, response.body etc.
    debug('response.statusCode:',response.statusCode);
    if(response.statusCode >= 500){ //服務器錯誤
      throw new error.GenericError(-3,`php服務器錯誤 service:${service} path:${path} params:${params}`);
    }
    return response.body;
  });
};
