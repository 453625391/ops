var express = require('express');
var routes = require('./router');
var bodyParser = require('body-parser');
var log4js = require('log4js');
var app = express();
var logger = log4js.getLogger('http');
var debug = require('debug')('mth-salesassistant:app');
var  cons = require('consolidate');
var Handlebars = require('handlebars');
var moment = require('moment');

Handlebars.registerHelper('publishAt', function(date,format) {
     format = format||'YYYY/MM/DD';
       if(arguments.length > 2){
           format = arguments[1];
       }
       if(date){
           return moment(date).format(format);
       } else　{
           return '';
       }
});

app.use(express.static('public'));

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));
app.use(log4js.connectLogger(logger, { level: 'auto', format: ':method :url', nolog: '\\.gif|\\.jpg$' }));

// Use handlebars as template engine
 app.engine("html", cons.handlebars);
 app.set("view engine", "html");
 app.set("views", __dirname + "/views");

// https://www.npmjs.com/package/consolidate
//
//合并参数
app.use(function(req,res,next){
  req.allParams = Object.assign({request_info:req.ip},req.query,req.body);
  debug("set req.allParams: %j",req.allParams);
  next();
});
app.use(routes);


// catch 404 and forward to error handler
app.use(function(req, res, next) {
    // var err = new Error('Not Found');
    // err.status = 404;
    // next(err);
    res.send('404 Not Found');
});

// Log the error
app.use(function (err, req, res, next) {
  debug(err);
  // 需要处理第三方api报的500 错误
  logger.error("%s %j err:%s",req.url,req.params,err.message||err.error_message);
  debug("%s %d err:%s",__filename,__line,err);
  //页面请求处理
  // res.status(500);
  // res.render('error', { error: err });
  if(!err.rsp_code){ // 非自定义异常
    //404

    res.send({
      rsp_code:'fail',
      name:err.name,
      error_code:err.name,
      error_msg:err.message
    });
    return;
  }
  res.send(err);
});

process.on('uncaughtException', (err) => {
  debug('callback error:',err);
  logger.error(err);
});

process.on('unhandledRejection', (reason, p) => {
  debug('promise error:',reason, p);
  logger.error(err);
});

module.exports = app;
