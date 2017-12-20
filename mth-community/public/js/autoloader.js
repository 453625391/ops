/*
 * AutoLoader 1.0
 *
 * ChangFeng
 * Copyright 2017, MIT License
 *
 */
var AutoLoader = (function() {
    var getClientHeight = function() {
        var clientHeight = 0;
        if (document.body.clientHeight && document.documentElement.clientHeight) {
            var clientHeight = (document.body.clientHeight < document.documentElement.clientHeight) ? document.body.clientHeight : document.documentElement.clientHeight;
        } else {
            var clientHeight = (document.body.clientHeight > document.documentElement.clientHeight) ? document.body.clientHeight : document.documentElement.clientHeight;
        }
        return clientHeight;
    };
    var getScrollTop = function() {
        var scrollTop = 0;
        if (document.documentElement && document.documentElement.scrollTop) {
            scrollTop = document.documentElement.scrollTop;
        } else if (document.body) {
            scrollTop = document.body.scrollTop;
        }
        return scrollTop;
    };
    var getScrollHeight = function() {
        return Math.max(document.body.scrollHeight, document.documentElement.scrollHeight);
    };

    var AutoLoaderFn = function(triggerHeight, callback) {};

    AutoLoaderFn.prototype.load = function(triggerHeight, callback) {
        if (arguments.length == 0) throw new Error('autoloader: parameters are needed!');
        if (arguments.length == 1) {
            callback = triggerHeight;
            triggerHeight = 300; // default 300px
        }
        if (typeof callback !== 'function') throw new Error('autoloader: callback must be a function!');
        document.addEventListener('scroll', function() {
            var bheight = getScrollHeight() - getScrollTop() - getClientHeight();
            if (bheight < triggerHeight) {
                callback();
            }
        });
    }

    return AutoLoaderFn;
})();
