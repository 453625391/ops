let articleList = new Vue({
    el: '#articleContainer',
    data: () => {
        return {
            page: {
                currentPageNo: 1,
                hasMore: true,
                loading: false
            },
            articleList: []
        }
    },
    filters: {
        handleName: function(name) {
            if(!name) {
                return '匿名';
            } else if(/^1(2|3|4|5|6|7|8|9)\d{9}$/.test(name)) {
                return name.substring(0, 3) + '****' + name.substring(7, 11);
            } else {
                return name;
            }
        }
    },
    methods: {
        loadArticles: function() {
            let self = this;
            if (self.page.hasMore && !self.page.loading) {
                self.page.loading = true;
                fetch('/getShareList', {
                    credentials: 'include',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    method: 'post',
                    body: JSON.stringify({
                        circle: _requestParams.circle,
                        page_no: self.page.currentPageNo
                    })
                })
                .then(res => {
                    if (res.ok) {
                        res.json().then(data => {
                            if (data.articleList.length > 0) {
                                self.articleList.push.apply(self.articleList, data.articleList);
                                self.page.currentPageNo++;
                            } else {
                                self.page.hasMore = false;
                            }
                            self.page.loading = false;
                        });
                    }
                    self.page.loading = false;
                })
                .catch(e => {
                    self.page.loading = false;
                    M.Tip.danger('系统繁忙！');
                });
            }
        }
    },
    mounted: function() {
        this.loadArticles();
        new AutoLoader().load(this.loadArticles);
    }
});