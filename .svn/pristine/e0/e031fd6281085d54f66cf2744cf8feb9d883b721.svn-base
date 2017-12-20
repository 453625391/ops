/*
 * AutoLoader 1.0
 *
 * ChangFeng
 * Copyright 2017, MIT License
 *
 */
var AutoLoader = (function() {
    var scrollToTopIcon = 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFAAAABQCAYAAACOEfKtAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA3JpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo4ZWNhZjBmOS04ZWUyLWU4NGUtYTBhZS0wNGMyZjBjNjY3MTEiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MkIzOTgyOTc4MjA0MTFFNUJEODNGQkY0RkMxMkUxODAiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6MkIzOTgyOTY4MjA0MTFFNUJEODNGQkY0RkMxMkUxODAiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoV2luZG93cykiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDowMTcxYzIxYS0xNDJmLTQ2YmMtYjI0Mi00NjY4MjY3MjExNWYiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6OGVjYWYwZjktOGVlMi1lODRlLWEwYWUtMDRjMmYwYzY2NzExIi8+IDwv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';
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
    var createScrollToTop = function() {
        var scrollToTopDom = document.createElement('span');
        scrollToTopDom.style.display = 'none';
        scrollToTopDom.style.width = '1.6rem';
        scrollToTopDom.style.height = '1.6rem';
        scrollToTopDom.style.position = 'fixed';
        scrollToTopDom.style.right = '0';
        scrollToTopDom.style.bottom = '1.6rem';
        scrollToTopDom.style.background = 'url(' + scrollToTopIcon + ') no-repeat center center';
        scrollToTopDom.style.backgroundSize = '1.066667rem 1.066667rem';
        document.body.appendChild(scrollToTopDom);
        scrollToTopDom.addEventListener('click', function() {
            window.scrollTo(0, 0);
        });
        return scrollToTopDom;
    };

    var AutoLoaderFn = function() {};

    AutoLoaderFn.prototype.load = function(options, callback) {
        if (arguments.length == 0) throw new Error('autoloader: parameters are needed!');
        if (arguments.length == 1) {
            callback = options;
            options = {};
        }
        if (typeof callback !== 'function') throw new Error('autoloader: callback must be a function!');
        // default options
        options.triggerHeight = options.triggerHeight || 600;
        typeof options.triggerCallbackImmediately == 'undefined' ? options.triggerCallbackImmediately = true : options.triggerCallbackImmediately = options.triggerCallbackImmediately;

        // show quick scroll to top icon
        var scrollToTopDom;
        var dpr = 1;
        if (document.getElementsByTagName('html')[0].attributes['data-dpr']) dpr = document.getElementsByTagName('html')[0].attributes['data-dpr'].nodeValue;
        if (options.scrollToTop) scrollToTopDom = createScrollToTop();

        document.addEventListener('scroll', function() {
            var bheight = getScrollHeight() - getScrollTop() - getClientHeight();
            if (bheight < options.triggerHeight) {
                callback();
            }
            if (options.scrollToTop)(getScrollTop() / dpr) > 800 ? scrollToTopDom.style.display = 'inline-block' : scrollToTopDom.style.display = 'none';
        });
        if (options.triggerCallbackImmediately) callback();
    }

    return AutoLoaderFn;
})();
