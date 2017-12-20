const express = require('express');
const router = express.Router();
const appportal = require('../service/appportal');
const error = require('../lib/error');
const cons = require('../lib/constant');
const apptokenSignValidate = require('../middleware/apptokenSignValidate');
const usertokenSignValidate = require('../middleware/usertokenSignValidate');
const notEmptyValidate = require('../middleware/notEmptyValidate');
const debug = require('debug')('mth-salesassistant:router:appprotal');

/**
 * 接口联系人：丁辉 Java运营平台
 * @type {[type]}
 */


router.all('/omp-mth-store/app/stores',
notEmptyValidate('app_token','sign','stores_name','business_type_key',
'contact_person','contact_tel','area_code','address','stores_type_key','business_developer'),
apptokenSignValidate,function(req,res,next){
  appportal.requestJavaInterface(cons.service.REGISTERSTORES,cons.url.STORE, req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

// 门店交易类型查询
router.all('/salesassistant/storesTransactionTypeFind',
notEmptyValidate('app_token','sign','stores_id'),
apptokenSignValidate,
function(req,res,next){
  return appportal.requestJavaInterface(cons.service.STORESTRANSACTIONTYPEFIND,cons.url.SALESASSISTANT,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

// 订单统计报表
// router.all('/salesassistant/statisticsOrderCountAndMoneyFind',
// notEmptyValidate('app_token','sign'),
// apptokenSignValidate,
// function(req,res,next){
//   return appportal.requestJavaInterface(cons.service.STATISTICSORDERCOUNTANDMONEYFIND,cons.url.SALESASSISTANT,req.allParams).then(body=>{
//     res.send(body);
//   }).catch(next);
// });

// 订单参与门店与参与客户统计
// router.all('/salesassistant/statisticsStoreCountAndConsumerCountFind',
// notEmptyValidate('app_token','sign'),
// apptokenSignValidate,
// function(req,res,next){
//   return appportal.requestJavaInterface(cons.service.STATISTICSSTORECOUNTANDCONSUMERCOUNTFIND,cons.url.SALESASSISTANT,req.allParams).then(body=>{
//     res.send(body);
//   }).catch(next);
// });

// 统计淘淘领本月、上月、全部的订单数量、参与门店、参与顾客
router.all('/salesassistant/statisticsOrderStoreConsumerCount',
notEmptyValidate('app_token','sign'),
apptokenSignValidate,
function(req,res,next){
  return appportal.requestJavaInterface(cons.service.STATISTICSORDERSTORECONSUMERCOUNT,cons.url.SALESASSISTANT,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

// 查询淘淘领本月每天得订单数
router.all('/salesassistant/statisticsCurrentMonthOrdersEveryday',
notEmptyValidate('app_token','sign'),
apptokenSignValidate,
function(req,res,next){
  return appportal.requestJavaInterface(cons.service.STATISTICSCURRENTMONTHORDERSEVERYDAY,cons.url.SALESASSISTANT,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

// 查询门店总数量和本月新增门店数量  联盟店数量 加盟店数量 自营店数量
router.all('/salesassistant/statisticsStoreAmountAndMonthNewStoreNum',
notEmptyValidate('app_token','sign'),
apptokenSignValidate,
function(req,res,next){
  return appportal.requestJavaInterface(cons.service.STATISTICSSTOREAMOUNTANDMONTHNEWSTORENUM,cons.url.SALESASSISTANT,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

// 按区域统计门店数量  按等级统计门店数量  按门店类型统计门店数量
router.all('/salesassistant/salesassistantAppRequireInterface',
notEmptyValidate('app_token','sign'),
apptokenSignValidate,
function(req,res,next){
  return appportal.requestJavaInterface(cons.service.SALESASSISTANTAPPREQUIREINTERFACE,cons.url.SALESASSISTANT,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

// salesassistant.statisticsStoreCountAndConsumerCountFind
// salesassistant.statisticsOrderCountAndMoneyFind
//接口二合一
router.all('/salesassistant/statisticsTodayOrderAmountParticipateStoreAndCustomers',
notEmptyValidate('app_token','sign'),
apptokenSignValidate,
function(req,res,next){
  return appportal.requestJavaInterface(cons.service.STATISTICSTODAYORDERAMOUNTPARTICIPATESTOREANDCUSTOMERS,cons.url.SALESASSISTANT,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

//新闻
//根据id查询文章详情
router.all('/article/queryArticleDetail',
notEmptyValidate('article_id'),
function(req,res,next){
  return appportal.requestJavaInterface(cons.service.QUERYARTICLEDETAIL,cons.url.ARTICLE,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

//根据所属平台查询文章列表
router.all('/article/queryArticleListPage',
notEmptyValidate('pageNum','pageSize','article_platform'),
function(req,res,next){
  return appportal.requestJavaInterface(cons.service.QUERYARTICLELISTPAGE,
    cons.url.ARTICLE,req.allParams).then(body=>{
    res.send(body);
  }).catch(next);
});

//资讯详情页面
router.get('/article/:id',function(req,res,next){
  return appportal.requestJavaInterface(cons.service.QUERYARTICLEDETAIL,cons.url.ARTICLE,
    {article_id:req.params.id}).then(body=>{
      var article = body.data;
      console.log("data:",article);
      if(article.article_source_address && article.article_pathway ==='url'){
        res.redirect(article.article_source_address);
        return;
      }
    res.render('news.html',article);
  }).catch(next);
});
module.exports = router;
