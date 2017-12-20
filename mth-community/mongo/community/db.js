var mongoose = require('mongoose');
var appConfig = require('../../app-config');
var db = mongoose.connect(appConfig.communityMongoDB);

module.exports = db;