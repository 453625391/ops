var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var url = require('url');
var session = require('express-session');
// var RedisStore = require('connect-redis')(session);
var favicon = require('serve-favicon');
var request = require('request');
var log4js = require('./lib/log4js').getLogger('APP');
var appConfig = require('./app-config');
var errHandler = require('./lib/err-handler');

// 社区
var communityRouter = require('./routes/community-router');

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

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: false
}));

// set cookies
app.use(cookieParser());

// set session
// app.use(session({
//     store: new RedisStore({
//         host: appConfig.redisHost,
//         port: appConfig.redisPort
//     }),
//     resave: true,
//     saveUninitialized: true,
//     name: 'test',
//     secret: '123456'
// }));

app.use(express.static(path.join(__dirname, 'public')));

// 中间件，为所有请求记录日志
app.use(function(req, res, next) {
    log4js.info(req.url);
    next();
});

global.userInfoMap = {};

// 统一设置cookie和查询用户信息
app.use('/', function(req, res, next) {
    if(req.query.mi) {
        // 将用户id设置到cookie中
        res.setHeader('Set-Cookie', ['sid=' + req.query.mi + ';path=/;expires=0']);
    }
    var memberId = req.query.mi || req.cookies.sid;
    // 如果全局变量中无该用户信息，则重新查询
    if(!global.userInfoMap[memberId]) {
        var consumerInfoFormData = {
            service : 'member.consumerFind',
            params : JSON.stringify({
                member_id : req.query.mi
            })
        };
        request.post(appConfig.memberPath, {form: consumerInfoFormData}, function(error, response, consumerInfoBody) {
            try {
                var consumerInfoResult = JSON.parse(consumerInfoBody);
                if(!error && response.statusCode == 200 && consumerInfoResult.rsp_code == 'succ') {
                    consumerInfoResult.data.member_id = req.query.mi;
                    global.userInfoMap[req.query.mi] = consumerInfoResult.data;
                    next();
                } else {
                    errHandler.renderToErrPage(res, '请先登录');
                }
            } catch (e) {
                errHandler.renderToErrPage(res);
            }
        });
    } else {
        next();
    }
});

// 统一登录拦截器
app.use('/', function(req, res, next) {
    var memberId = req.query.mi || req.cookies.sid;
    if(!global.userInfoMap[memberId]) {
        errHandler.renderToErrPage(res, '请先登录');
    } else {
        next();
    }
});

// 社区
app.use('/', communityRouter);

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