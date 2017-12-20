/**
 * Created by Changfeng
 * 系统配置
 */
var javaServiceHost = 'http://121.43.59.219:8080';
module.exports = {
    // 环境（开发：development， 生产：production）
    evn: 'development',

    // 服务端口
    port: 30002,

    /**
     * 日志输出级别
     * @可选值：TRACE / DEBUG / INFO / WARN / ERROR / FATAL / MARK / OFF
     * @建议：开发环境建议为'INFO'，生产环境建议为'ERROR'
     */
    logLevel: 'INFO',

    // 日志输入路径
    logPath: 'logs/',

    // 是否验证签名（发布至开放或生产环境需修改为true）
    needValidSign: true,

    // redis IP
    redisIP: 'salt.meitianhui.com',

    // redis 端口
    redisPort: 15683,

    // redis 密钥
    redisSecret: 'meitianhui',

    // 基础服务接口地址
    infrastructurePath: javaServiceHost + '/ops-infrastructure/infrastructure',

    // 基础服务--用户接口地址
    userPath: javaServiceHost + '/ops-infrastructure/user',

    // 基础服务--通知接口地址
    notificationPath: javaServiceHost + '/ops-notification/notification',

    // 财务服务接口地址
    financePath: javaServiceHost + '/ops-finance/finance',

    // 会员服务接口地址
    memberPath: javaServiceHost + '/ops-member/member',

    // 商品服务接口地址
    goodsPath: javaServiceHost + '/ops-goods/goods',

    // 预售订单接口地址
    opsorderPath: javaServiceHost + '/ops-order/order',

    // 支付宝支付异步通知回调转发地址
    alipayNotifyCallbackPath: javaServiceHost + '/ops-finance/alipayNotify',

    // 微信支付通知异步回调转发地址
    wechatNotifyCallbackPath: javaServiceHost + '/ops-finance/wechatNotify',

    // 兑换中心用户注册app_token常量
    exchangeCenterRegAppTokenConstant: 'b6d6d2d5-1d78-4b85-8b94-2be370fb6a83',

    // 兑换中心注册用户入口地址
    exchangeCenterRegPath: 'http://121.40.148.40/topapi/temporary.html',

    // 大汉手机充值回调地址
    daHanNotifyCallbackPath: javaServiceHost + '/ops-order/daHanNotify/mobileRechargeNotify'
}