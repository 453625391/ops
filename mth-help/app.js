var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var url = require('url');
var favicon = require('serve-favicon');
var appConfig = require('./app-config');

// 帮助页面路由
var helpRouter = require('./routes/help-router');

var app = express();

// 配置开发环境，端口等
process.env.ENV = appConfig.evn;
process.env.PORT = appConfig.port;

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
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

// 帮助页面
app.use('/', helpRouter);

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
        res.send({
            status: 0,
            message: '系统错误',
            error: err
        });
    });
}

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    res.send({
        status: 0,
        message: '系统错误',
        error: err
    });
});


module.exports = app;
