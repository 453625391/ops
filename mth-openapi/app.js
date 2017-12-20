var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var url = require('url');
var session = require('express-session');
var redisStore = require('connect-redis')(session);
var favicon = require('serve-favicon');
var log4js = require('./lib/log4js').getLogger('APP');

// OAuth Router
var oauthRouter = require('./routes/oauth-router');

// 基础服务
var baseRouter = require('./routes/base-router');

// 支付通知异步回调
var paynotifyRouter = require('./routes/paynotify-router');

// 惠易定
var hydRouter = require('./routes/hyd-router');

// 鸿商POS机
var posRouter = require('./routes/pos-router');

// 大汉手机充值
var dahanRouter = require('./routes/dahan-router');

// 商品服务
var goodsRouter = require('./routes/goods-router');

var appConfig = require('./app-config');

var app = express();

// 配置开发环境，端口等
process.env.ENV = appConfig.evn;
process.env.PORT = appConfig.port;
app.disable('x-powered-by');

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.engine('.html', require('ejs').__express);
app.set('view engine', 'html');

// set favicon.ico
app.use(favicon(path.join(__dirname, '/public/image/favicon.ico')));

app.use(logger('dev'));

// 中间件，处理支付宝notify请求，修改请求header，否则无法解析到请求参数
app.use(function(req, res, next) {
    if(req.url.indexOf('/openapi/paynotify/alipay') != -1) {
        req.headers['content-type'] = 'application/x-www-form-urlencoded';
    }
    next();
});

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: false
}));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

// 中间件，为所有请求记录日志
app.use(function(req, res, next) {
    log4js.info(req.url);
    next();
});

// OAuth
app.use('/oauth', oauthRouter);

// 基础服务
app.use('/openapi', baseRouter);

// 支付通知异步回调
app.use('/openapi/paynotify', paynotifyRouter);

// 惠易定
app.use('/openapi/hyd', hydRouter);

// 鸿商POS机
app.use('/openapi/pos', posRouter);

// 大汉手机充值
app.use('/openapi/dahan', dahanRouter);

// 商品服务
app.use('/openapi/goods', goodsRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
    var err = new Error('Not Found');
    err.status = 404;
    next(err);
});

// error handlers

// development error handler
// will print stacktrace
if (appConfig.evn === 'development') {
    app.use(function(err, req, res, next) {
        res.status(err.status || 500);
        res.send(err);
    });
}

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    res.send({
        rsp_code: 'fail',
        error_code: 'system error',
        error_msg: '系统错误'
    });
});

module.exports = app;