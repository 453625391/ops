const express = require('express');
const router = express.Router();
const hyd = require('../service/hyd');
const error = require('../lib/error');
const cons = require('../lib/constant');
const apptokenSignValidate = require('../middleware/apptokenSignValidate');
const usertokenSignValidate = require('../middleware/usertokenSignValidate');
const notEmptyValidate = require('../middleware/notEmptyValidate');
const debug = require('debug')('mth-salesassistant:router:hyd');

/**
 * 接口联系人 世燕 PHP 惠易定
 * @type {[type]}
 */

// 司机端查询运单列表
router.all('/transport/list',
notEmptyValidate('app_token','sign','tel_no','status'),
apptokenSignValidate,function(req,res,next){
  hyd.requestPHPInterface(cons.service.TRANSPORTLIST, req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

//APP查询运单详情
router.all('/transport/detail',
notEmptyValidate('app_token','sign','transport_id'),
apptokenSignValidate,function(req,res,next){
  hyd.requestPHPInterface(cons.service.TRANSPORTDETAIL,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

router.all('/order/detail',
notEmptyValidate('app_token','sign','tid'),
apptokenSignValidate,function(req,res,next){
  hyd.requestPHPInterface(cons.service.ORDERDETAIL,req.allParams).then(body=>{
    res.json(body);
  }).catch(next);
});


router.all('/transport/proc',
notEmptyValidate('app_token','sign','transport_id'),
apptokenSignValidate,function(req,res,next){
  hyd.requestPHPInterface(cons.service.TRANSPORTPROC,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

router.all('/order/set_seqnum',
notEmptyValidate('app_token','sign','transport_id','tids'),
apptokenSignValidate,function(req,res,next){
  hyd.requestPHPInterface(cons.service.ORDERSET_SEQNUM,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

router.all('/transport/loading',
notEmptyValidate('app_token','sign','transport_id','status','driver_name','tel_no'),
apptokenSignValidate,function(req,res,next){
  hyd.requestPHPInterface(cons.service.TRANSPORTLOADING,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

router.all('/transport/order_items/edit',
notEmptyValidate('app_token','sign','tid','item_list','payment'),
apptokenSignValidate,function(req,res,next){
  hyd.requestPHPInterface(cons.service.ORDER_ITEMS_EDIT,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

router.all('/transport/order/payment',
notEmptyValidate('app_token','sign','tid','payment','small_change_fee',
'pay_method','payed_fee'),
apptokenSignValidate,function(req,res,next){
  hyd.requestPHPInterface(cons.service.TRADEORDERPAYMENT,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

router.all('/transport/order/sign',
notEmptyValidate('app_token','sign','tid','sign_imgs','voucher_id','voucher_name','voucher_mobile','belongs'),
apptokenSignValidate,function(req,res,next){
  hyd.requestPHPInterface(cons.service.TRANSPORTORDERSIGN,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

router.all('/transport/order/cancel',
notEmptyValidate('app_token','sign','tid','driver_name','tel_no'),
apptokenSignValidate,function(req,res,next){
  hyd.requestPHPInterface(cons.service.TRANSPORTORDERCANCEL,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

router.all('/order/modified/items',
notEmptyValidate('app_token','sign','tid'),
apptokenSignValidate,function(req,res,next){
  hyd.requestPHPInterface(cons.service.ORDER_MODIFIED_ITEMS,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

module.exports = router;
