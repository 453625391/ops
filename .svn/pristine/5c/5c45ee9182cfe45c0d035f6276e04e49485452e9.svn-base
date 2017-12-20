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
var request = require('request');

// 基础服务
var baseRouter = require('./routes/base-router');

// 会员服务
var memberRouter = require('./routes/member-router');

// 财务服务
var financeRouter = require('./routes/finance-router');

// 兑换中心服务
var exchangeRouter = require('./routes/exchange-router');

// 商品服务
var goodsRouter = require('./routes/goods-router');

// 预售订单服务
var opsorderRouter = require('./routes/opsorder-router');

// 订单服务
var orderRouter = require('./routes/order-router');

// 直播社区服务
var communityRouter = require('./routes/community-router');

// 统计
var statisticRouter = require('./routes/statistic-router');

// 支付通知异步回调
var paynotifyRouter = require('./routes/paynotify-router');

// APP版本信息
var versionRouter = require('./routes/version-router');

// C端H5服务 领有惠
var h5CRouter = require('./routes/h5/h5-c-router');

// B端H5服务 店东助手
var h5BRouter = require('./routes/h5/h5-b-router');

// 通用H5服务
var h5CommonRouter = require('./routes/h5/h5-common-router');

// 大健康
var djkRouter = require('./routes/h5/djk-router');

// 超级返展示
var fanRouter = require('./routes/fan-router');

// 微信小程序
var wxappRouter = require('./routes/wxapp-router');

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

// 中间件
app.use(function(req, res, next) {
    // 处理支付宝notify请求，修改请求header，否则无法解析到请求参数
    if(req.url.indexOf('/openapi/paynotify/consumer_alipay') != -1 || req.url.indexOf('/openapi/paynotify/store_alipay') != -1
        || req.url.indexOf('/openapi/paynotify/sdj_alipay') != -1) {
        req.headers['content-type'] = 'application/x-www-form-urlencoded';
    }
    // 商品详情页，统一更新查看次数
    if(req.url.indexOf('goods_id=') != -1) {
        if(req.query.goods_id) {
            // 更新查看次数
            var formData = {
                service : 'goods.gdViewAdd',
                params : JSON.stringify({
                    goods_id : req.query.goods_id
                })
            };
            request.post(appConfig.goodsPath, {form: formData});
        }
    }
    next();
});

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: false
}));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public'), {
    // 设置缓存7天
    maxAge: 604800000
}));

// 中间件，为所有请求记录日志
app.use(function(req, res, next) {
    log4js.info(req.url);
    next();
});

// 基础服务
app.use('/openapi', baseRouter);

// 会员服务
app.use('/openapi/member', memberRouter);

// 财务服务
app.use('/openapi/finance', financeRouter);

// 兑换中心
app.use('/openapi/exchange', exchangeRouter);

// 商品服务
app.use('/openapi/goods', goodsRouter);

// 预售订单服务
app.use('/openapi/opsorder', opsorderRouter);

// 订单服务
app.use('/openapi/order', orderRouter);

// 直播社区服务
app.use('/openapi/community', communityRouter);

// 统计
app.use('/openapi/statistic', statisticRouter);

// 支付通知异步回调
app.use('/openapi/paynotify', paynotifyRouter);

// APP版本信息
app.use('/openapi/version', versionRouter);

// C端H5服务
app.use('/openapi/h5/c', h5CRouter);

// B端H5服务
app.use('/openapi/h5/b', h5BRouter);

// 通用H5服务
app.use('/openapi/h5/common', h5CommonRouter);

// 大健康
app.use('/openapi/h5/b/djk', djkRouter);

// 超级返展示
app.use('/fan', fanRouter);

// 微信小程序
app.use('/openapi/wxapp', wxappRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
    var err = new Error('Not Found');
    err.status = 404;
    next(err);
});

// error handlers

// development error handler
// will print stacktrace
// if (appConfig.evn === 'development') {
//     app.use(function(err, req, res, next) {
//         res.status(err.status || 500);
//         res.send(err);
//     });
// }

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
    log4js.error('APP SYSTEM ERROR:' + err);
    res.status(err.status || 500);
    res.send({
        rsp_code: 'fail',
        error_code: 'system error',
        error_msg: '系统错误'
    });
});
module.exports = app;
