/**
 * 所有页面都用到的一些基础代码放在这里(与业务无关的)
 * @author Changfeng
 */

// 所有通用方法的前置命名空间
var M = {};

// IOS初始化
function setupWebViewJavascriptBridge(callback) {
    if (window.WebViewJavascriptBridge) {
        return callback(WebViewJavascriptBridge);
    }
    if (window.WVJBCallbacks) {
        return window.WVJBCallbacks.push(callback);
    }
    window.WVJBCallbacks = [callback];
    var WVJBIframe = document.createElement('iframe');
    WVJBIframe.style.display = 'none';
    WVJBIframe.src = 'wvjbscheme://__BRIDGE_LOADED__';
    document.documentElement.appendChild(WVJBIframe);
    setTimeout(function() {
        document.documentElement.removeChild(WVJBIframe)
    }, 0)
}

window.onload = function() {
    // 绑定Fastclick，解决点击延迟
    // if (FastClick) {
    //     FastClick.attach(document.body);
    // }

    // iOS系统下建立js与Native的连接桥
    if (!window.jsObj) {
        setupWebViewJavascriptBridge(function(bridge) {
            M.iOSBridge = bridge;
        });
    }
};
