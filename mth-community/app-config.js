/**
 * Created by Changfeng
 * 系统配置
 */
module.exports = {
    // 环境（开发：development， 生产：production）
    evn: 'development',

    // 服务端口
    port: 3006,

    /**
     * 日志输出级别
     * @可选值：TRACE / DEBUG / INFO / WARN / ERROR / FATAL / MARK / OFF
     * @建议：开发环境建议为'INFO'，生产环境建议为'ERROR'
     */
    logLevel: 'INFO',

    // 日志输入路径
    logPath: 'logs/',

    // 社区mongodb服务地址
    communityMongoDB : 'mongodb://changfeng:123456@121.43.59.219:27017/community',

    // 会员服务接口地址
    memberPath: 'http://121.43.59.219:8080/ops-member/member'
}