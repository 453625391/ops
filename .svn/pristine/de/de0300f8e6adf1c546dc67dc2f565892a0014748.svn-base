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

$(function() {
    setupWebViewJavascriptBridge(function(bridge) {

        bridge.registerHandler('notify', function(data, responseCallback) {
            // alert('接收到OC的请求，请求参数是：' + data);
            var responseData = {
                'Javascript Says': '小叮当我收到你的请求了！'
            };
            responseCallback(responseData);
        });

        $('#buyNow').tap(function(e) {
            e.preventDefault()
            bridge.callHandler('buyGoods', {
                'goods_id': $(this).attr('data-id')
            }, function(response) {
                // alert('回调成功！');
            });
        });
    });
});