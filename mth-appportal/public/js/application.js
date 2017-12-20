/**
 * 所有页面都用到的一些基础代码放在这里(与业务无关的)
 * @author Changfeng
 */

// 所有通用方法的前置命名空间
var M = {};

// iOS初始化
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

// 初始化banner图轮播
M.initSwipe = function(positionId, sliderId) {
    positionId = positionId ? positionId : 'position';
    sliderId = sliderId ? sliderId : 'slider';
	var bullets = document.getElementById(positionId).getElementsByTagName('li');
    if(bullets[0]) bullets[0].className = 'on';
    var slider = Swipe(document.getElementById(sliderId), {
        auto: 3000,
        continuous: true,
        callback: function(pos) {
            var i = bullets.length;
            while (i--) {
                bullets[i].className = '';
            }
            bullets[pos].className = 'on';
        }
    });
}

// 为banner图绑定广告点击事件
M.bindBannerAdClick = function() {
	$('.banner img').click(function() {
        var bannerUrl = $(this).attr('banner-url');
        if(!bannerUrl) return;
        if(window.jsObj) {
            window.jsObj.openAdLayer(JSON.stringify({url: bannerUrl}));
        } else {
            setupWebViewJavascriptBridge(function(bridge) {
                bridge.callHandler('openAdLayer', {url: bannerUrl});
            });
        }
    });
}

$(function() {
	if(FastClick) FastClick.attach(document.body);
});

// 通用banner组件
Vue.component('my-banner', {
    props: ['bannerData', 'extraBannerList'],
    template:
        '<div class="banner">' +
            '<div id="slider" class="swipe">' +
                '<div class="swipe-wrap">' +
                    '<div v-for="banner in bannerData.list">' +
                        '<img v-bind:src="bannerData.doc_url[banner.path_id]" v-on:click="callNativeToOpenAdLayer(banner.url)" />' +
                    '</div>' +
                    '<div v-for="extraBanner in extraBannerList">' +
                        '<a v-bind:href="extraBanner.url">' +
                            '<img v-bind:src="extraBanner.image" />' +
                        '</a>' +
                    '</div>' +
                '</div>' +
            '</div>' +
            '<nav>' +
                '<ul id="position">' +
                    '<li v-for="banner in bannerData.list"></li><li v-for="banner in extraBannerList"></li>' +
                '</ul>' +
            '</nav>' +
        '</div>',
    methods: {
        initSwipe: function() {
            var bullets = document.getElementById('position').getElementsByTagName('li');
            if(bullets[0]) bullets[0].className = 'on';
            var slider = Swipe(document.getElementById('slider'), {
                auto: 3000,
                continuous: true,
                callback: function(pos) {
                    var i = bullets.length;
                    while (i--) {
                        bullets[i].className = '';
                    }
                    bullets[pos].className = 'on';
                }
            });
        },
        callNativeToOpenAdLayer: function(url) {
            if(!url) return;
            url = url.replace(/\s+/g, '');
            if(window.jsObj) {
                window.jsObj.openAdLayer(JSON.stringify({url: url}));
            } else {
                setupWebViewJavascriptBridge(function(bridge) {
                    bridge.callHandler('openAdLayer', {url: url});
                });
            }
        }
    },
    mounted: function() {
        this.initSwipe();
    }
});