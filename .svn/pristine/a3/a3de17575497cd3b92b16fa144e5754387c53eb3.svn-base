const express = require('express');
const router = express.Router();
const ipos = require('../service/ipos');
const error = require('../lib/error');
const cons = require('../lib/constant');
const apptokenSignValidate = require('../middleware/apptokenSignValidate');
const usertokenSignValidate = require('../middleware/usertokenSignValidate');
const notEmptyValidate = require('../middleware/notEmptyValidate');
const debug = require('debug')('mth-salesassistant:router:ipos');

/**
 * 接口联系人 世颠 PHP ipos
 * @type {[type]}
 */

//1.查询订单详情
router.all('/order/detail',
notEmptyValidate('app_token','sign','order_code'),
// apptokenSignValidate,
function(req,res,next){
  ipos.requestPHPInterface(cons.service.ORDERDETAIL,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

//2.订单支付
router.all('/order/pay',
notEmptyValidate('app_token','sign','auth_code','order_code','amount','payment_way_key'),
apptokenSignValidate,function(req,res,next){
  ipos.requestPHPInterface(cons.service.ORDERPAY,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

//3.订单通知
router.all('/order/pay/notify',
notEmptyValidate('app_token','sign','order_code','amount','payment_way_key','transaction_no','payment_time'),
apptokenSignValidate,function(req,res,next){
  ipos.requestPHPInterface(cons.service.ORDERPAYNOTIFY,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

//4.金额校验接口
router.all('/order/pay/check',
notEmptyValidate('app_token','sign','order_code','amount'),
apptokenSignValidate,function(req,res,next){
  ipos.requestPHPInterface(cons.service.ORDERPAYCHECK,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});


module.exports = router;
