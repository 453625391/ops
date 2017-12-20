var cons = require('./constant');
function SystemError(msg){
  this.rsp_code = 'fail';
  this.error_code ='system error';
  this.error_msg = msg||'系统错误';
}
SystemError.prototype = Error.prototype;
//参数不完整
exports.systemError = SystemError;



function GenericError(err_no,message){
  if(!!~err_no){
    this.code =200; //状态码
  }
  this.rsp_code = 'fail';
  this.error_code = cons.ERROR_CODE[err_no]||'-1';
  this.message = message||cons.ERROR_MESSAGE[err_no];
  this.error_msg =cons.ERROR_MESSAGE[err_no]||message;
}
GenericError.prototype = Error.prototype;
//参数不完整
exports.GenericError = GenericError;
