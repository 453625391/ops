const express = require('express');
const error = require('../lib/error');
const tool = require('../lib/tool');
const cons = require('../lib/constant');
const openapi = require('../service/openapi');
const apptokenSignValidate = require('../middleware/apptokenSignValidate');
const usertokenSignValidate = require('../middleware/usertokenSignValidate');
const notEmptyValidate = require('../middleware/notEmptyValidate');
const debug = require('debug')('mth-salesassistant:router:openapi');
const router = express.Router();

/**
 * 接口联系人 丁忍 java开放平台
 * @type {[type]}
 */

/**
 * APP登录
 * @param app_id app	app接入时分配的id
 * @param private_key 	app接入时分配的密钥
 */
router.all('/app/login',
notEmptyValidate('app_id','private_key'),
 function(req, res, next) {
  return openapi.requestJavaInterface(cons.service.APPTOKENAUTH,
    cons.url.INFRASTRUCTURE,req.allParams).then(function(body){
    res.send(body);
  }).catch(next);
});

router.all('/version/latest',function(req,res,next){
  var package_name = req.allParams.package_name||'com.meitianhui.saleassistant';
  var versionConfig = {
    'com.meitianhui.saleassistant': { //Android
          android:{
            version_code: 3,
            version_name: '1.0.3',
            download_url: 'http://mps-static.meitianhui.com/apps/gaodian/gaodianapp-release-latest.apk',
            force:true,
            changelog:'1.优化惠商头条\r\n 2.优化短信验证码'
          },
          ios:{
            version_code: 3,
            version_name: '1.0.3',
            download_url: 'https://itunes.apple.com/cn/app/id1267685403',
            force:true,
            changelog:'1.优化惠商头条\r\n 2.优化短信验证码'
          }
      }
  };
res.send({
  rsp_code: 'succ',
	data: versionConfig[package_name]||{}
});
});

/**
 * APP登录时获取手机验证码
 * @param  {[type]} mobile [description]
 * @param  {[type]} sms_source [description]
 */
router.all('/sms/code',
notEmptyValidate('app_token','sign','mobile','sms_source'),
apptokenSignValidate,
function(req,res, next){
  var params = req.allParams;
  //先判断用户是否存在
  //再发验证码
  return openapi.requestJavaInterface(cons.service.USERFINDBYMOBILE,cons.url.USER,
    {mobile:params.mobile}).then(body=>{
      if(!body.data || !body.data.user_id){ //用戶不存在
        throw new error.GenericError(-1,'用戶手机号未注册,获取验证码失败');
      }
      return openapi.requestJavaInterface(cons.service.SENDCHECKCODE,
        cons.url.NOTIFICATION,params).then(body=>{
          res.send(body);
      });
    }).catch(next);
});

/**
 * APP通过手机号验证码登录
 * @param  {[type]} mobile [description]
 * @param  {[type]} check_code [description]
 * @param {[type]}  data_source   [description]
 * @param {[type]}  device_model   [description]
 */
router.all('/sms/login',
notEmptyValidate('app_token','sign','mobile','check_code','data_source','device_model'),
apptokenSignValidate,
function(req,res, next){
  var params = req.allParams;
  //手机号为18676702002且跳过验证码为2002，跳过向服务器验证验证码
  if((params.mobile == '18676702002' && params.check_code == '2002')||
  (params.mobile == '13570806141' && params.check_code == '6666')){
    return openapi.requestJavaInterface(cons.service.SALESMANLOGIN,
      cons.url.USER,{
        mobile:params.mobile,
        request_info:params.request_info,
        data_source:params.data_source,
        device_model:params.device_model
      }).then(body=>{
        res.send(body);
      }).catch(next);
  }
  return openapi.requestJavaInterface(cons.service.VALIDATECHECKCODE,
    cons.url.NOTIFICATION,{
      mobile:params.mobile,
      check_code:params.check_code
    }).then(body=>{

      console.log("body69:",body);
      if(body.rsp_code === 'fail'){  //
        // throw new error.GenericError(-1,'获取验证码失败');
        res.send(body);
        return;
      }
      return openapi.requestJavaInterface(cons.service.SALESMANLOGIN,
        cons.url.USER,{
          mobile:params.mobile,
          request_info:req.ip,
          data_source:params.data_source,
          device_model:params.device_model
        }).then(body=>{
          res.send(body);
        });
    }).catch(next);

});


router.all('/user/salesmanLogin',
notEmptyValidate('app_token','sign','mobile','data_source','device_model'),
apptokenSignValidate,
function(req,res,next){
  var params = req.allParams;
  return openapi.requestJavaInterface(cons.service.SALESMANLOGIN,
    cons.url.USER,{
      mobile:params.mobile,
      request_info:req.ip,
      data_source:params.data_source,
      device_model:params.device_model
    }).then(body=>{
      res.send(body);
    }).catch(next);
});

router.all('/salesman/authApply',
notEmptyValidate('app_token','sign','id_card','id_card_pic_path','salesman_id'),
apptokenSignValidate,
function(req,res,next){
  return openapi.requestJavaInterface(cons.service.AUTHAPPLY,
    cons.url.MEMBER,req.allParams).then(body=>{
      res.send(body);
    }).catch(next);
});

router.all('/salesman/driverApply',
notEmptyValidate('app_token','sign','salesman_id','car_model','car_number','capacity',
'appearance_pic_path','driving_license_pic_path','driving_permit_pic_path'),
apptokenSignValidate,
function(req,res,next){
  return openapi.requestJavaInterface(cons.service.DRIVERAPPLY,
    cons.url.MEMBER, req.allParams).then(body=>{
      res.send(body);
    }).catch(next);

});

router.all('/salesman/driverAgainApply',
notEmptyValidate('app_token','sign','salesman_id','car_model','car_number','capacity',
'appearance_pic_path','driving_license_pic_path','driving_permit_pic_path'),
apptokenSignValidate,
function(req,res,next){
  return openapi.requestJavaInterface(cons.service.DRIVERAGAINAPPLY,
    cons.url.MEMBER,req.allParams).then(body=>{
      res.send(body);
    }).catch(next);
});

router.all('/salesman/salesmanDetailFind',
notEmptyValidate('app_token','sign','salesman_id'),
apptokenSignValidate,
function(req,res,next){
  return openapi.requestJavaInterface(cons.service.SALESMANDETAILFIND,cons.url.MEMBER, req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

/*
Deprected
 */
router.all('/salesman/messageHeadlines',
notEmptyValidate('app_token','sign'),
apptokenSignValidate,
function(req,res,next){
  return openapi.requestJavaInterface(cons.service.MESSAGEHEADLINES,cons.url.MEMBER,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

router.all('/salesman/specialistApply',
notEmptyValidate('app_token','sign','salesman_id'),
apptokenSignValidate,
function(req,res,next){
  return openapi.requestJavaInterface(cons.service.SPECIALISTAPPLY,cons.url.MEMBER,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

router.all('/salesman/specialistAgainApply',
notEmptyValidate('app_token','sign','salesman_id'),
apptokenSignValidate,
function(req,res,next){
  return openapi.requestJavaInterface(cons.service.SPECIALISTAGAINAPPLY,cons.url.MEMBER,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

router.all('/salesman/feedback',
notEmptyValidate('app_token','sign','contact','desc1'),
apptokenSignValidate,
function(req,res,next){
  return openapi.requestJavaInterface(cons.service.FEEDBACK,cons.url.MEMBER,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

router.all('/salesman/authApplyDetailFind',
notEmptyValidate('app_token','sign','salesman_id'),
apptokenSignValidate,
function(req,res,next){
  return openapi.requestJavaInterface(cons.service.AUTHAPPLYDETAILFIND,cons.url.MEMBER,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

router.all('/salesman/driverApplyDetailFind',
notEmptyValidate('app_token','sign','salesman_id'),
apptokenSignValidate,
function(req,res,next){
  return openapi.requestJavaInterface(cons.service.DRIVERAPPLYDETAILFIND,cons.url.MEMBER,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

router.all('/salesman/specialistApplyDetailFind',
notEmptyValidate('app_token','sign','salesman_id'),
apptokenSignValidate,
function(req,res,next){
  return openapi.requestJavaInterface(cons.service.SPECIALISTAPPLYDETAILFIND,cons.url.MEMBER,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});
router.all('/salesman/salesmanEdit',
notEmptyValidate('app_token','sign','salesman_id','head_pic_path'),
apptokenSignValidate,
function(req,res,next){
  return openapi.requestJavaInterface(cons.service.SALESMANEDIT,cons.url.MEMBER,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

//系统消息
router.all('/salesman/systemInform',
notEmptyValidate('app_token','sign','cre_by'),
apptokenSignValidate,
function(req,res,next){
  return openapi.requestJavaInterface(cons.service.SYSTEMINFORM,cons.url.MEMBER,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

//业务员助教申请添加
router.all('/salesman/assistantApplicationApply',
notEmptyValidate('app_token','sign','stores_id','assistant_id','remark'),
apptokenSignValidate,
function(req,res,next){
  return openapi.requestJavaInterface(cons.service.ASSISTANTAPPLICATIONAPPLY,cons.url.MEMBER,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

//查询附加门店接口（搞掂APP）
router.all('/stores/nearbyStoresTypeListPageFind',
notEmptyValidate('app_token','sign','find_type'),
apptokenSignValidate,
function(req,res,next){
  return openapi.requestJavaInterface(cons.service.NEARBYSTORESTYPELISTPAGEFIND,cons.url.MEMBER,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

//业务员对应的拓店，助教门店数量查询
router.all('/stores/salesmanStoresNumFind',
notEmptyValidate('app_token','sign','salesman_id','contact_tel'),
apptokenSignValidate,
function(req,res,next){
  return openapi.requestJavaInterface(cons.service.SALESMANSTORESNUMFIND,cons.url.MEMBER,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

//业务员数据
router.all('/salesman/salesmanDataFind',
notEmptyValidate('app_token','sign'),
apptokenSignValidate,
function(req,res,next){
  return openapi.requestJavaInterface(cons.service.SALESMANDATAFIND,cons.url.MEMBER,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

// 门店对应的会员资产列表分页查询
router.all('/stores/storesForConsumerAssetListPageFind',
notEmptyValidate('app_token','sign','stores_id'),
apptokenSignValidate,
function(req,res,next){
  return openapi.requestJavaInterface(cons.service.STORESFORCONSUMERASSETLISTPAGEFIND,cons.url.MEMBER,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

// 门店资产查询
router.all('/finance/memberAssetFind',
notEmptyValidate('app_token','sign','member_id'),
apptokenSignValidate,
function(req,res,next){
  return openapi.requestJavaInterface(cons.service.MEMBERASSETFIND,cons.url.FINANCE,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

// // 扫码支付（暂时支持支付宝） 接口关闭
// router.all('/trade/barCodePay',
// notEmptyValidate('app_token','sign','data_source','auth_code','payment_way_key','detail','amount','out_trade_body','out_trade_no','member_id'),
// apptokenSignValidate,
// function(req,res,next){
//   return openapi.requestJavaInterface(cons.service.BARCODEPAY,cons.url.FINANCE,req.allParams).then(body=>{
//     res.send(body);
//   }).catch(next);
// });

//获取短信验证码前调取验证信息接口
router.all('/infrastructure/securityVerify',
notEmptyValidate('app_token','sign'),
apptokenSignValidate,
function(req,res,next){
  return openapi.requestJavaInterface(cons.service.SECURITYVERIFY,cons.url.INFRASTRUCTURE,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

// 登陆 发短信
router.all('/sms/code/v2',
notEmptyValidate('app_token','sign','lock_value'),
apptokenSignValidate,
function(req,res,next){
  return openapi.requestJavaInterface(cons.service.SENDCHECKCODE,cons.url.NOTIFICATION,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});



module.exports = router;
