// list.js
Page({

	/**
     * 页面的初始数据
     */
	data: {
		loadGoodsInterfaceUrl: 'https://appportal.meitianhui.com/openapi/wxapp/getGoodsListByType',
        loadRecommendGoodsInterfaceUrl: 'https://appportal.meitianhui.com/openapi/wxapp/getRecommendGoodsList',
        loadBannerInterfaceUrl: 'https://appportal.meitianhui.com/openapi/wxapp/getBanner',
        categoryPanelSwitch: false,
        currentRoute: 'all',
        recommendGoodsList: [],
        banner: {},
		all: {
            page: {
                page_no: '1',
                page_size: '10'
            },
            hasMore: true,
            nothing: false,
            isLoading: false,
            goodsList: []
		},
        new: {
            page: {
                page_no: '1',
                page_size: '10'
            },
            hasMore: true,
            nothing: false,
            isLoading: false,
            goodsList: []
        },
        category: {
            category: '',
            page: {
                page_no: '1',
                page_size: '10'
            },
            hasMore: true,
            nothing: false,
            isLoading: false,
            goodsList: []
        },
        forecast: {
            page: {
                page_no: '1',
                page_size: '10'
            },
            hasMore: true,
            nothing: false,
            isLoading: false,
            goodsList: []
        }
        // wish: {
        //     service: 'goods.fgGoodsFavoritesListPageFind',
        //     page: {
        //         page_no: '1',
        //         page_size: '10'
        //     },
        //     hasMore: true,
        //     nothing: false,
        //     isLoading: false,
        //     goodsList: []
        // }
	},

	/**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        this.loadBanner();
        this.loadRecommendGoodsList();
        this.loadGoodsList();
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function() {

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function() {

    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide: function() {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function() {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function() {

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function() {
        this.loadGoodsList();
    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function() {

    },

    /**
     * 点击tab栏
     */
    bindTabTap: function(event) {
        let targetRoute = event.target.dataset.route;
        if(!targetRoute) return;
        let targetCategory = event.target.dataset.category || '';
        console.log(targetRoute, targetCategory);
        // 重复点击无效
        if(this.data.currentRoute == targetRoute && this.data.category.category == targetCategory) {
            console.log('111111');
            return;
        }
        this.setData({
            currentRoute: targetRoute,
            categoryPanelSwitch: false,
            'category.category': targetCategory
        });
        if(targetRoute == 'category' || this.data[targetRoute].goodsList.length == 0) {
            if(targetRoute == 'category') {
                this.setData({
                    'category.goodsList': [],
                    'category.page.page_no': '1',
                    'category.hasMore': true,
                    'category.isLoading': false
                });
            }
            this.loadGoodsList();
        }
    },

    /**
     * 切换分类面板展示/隐藏
     */
    bindCategoryPanelSwitch: function(event) {
        let onoff = event.target.dataset.switch;
        this.setData({
            categoryPanelSwitch: onoff
        });
    },

    /**
     * 获取商品列表
     */
    loadGoodsList: function() {
        let route = this.data.currentRoute;
        if(this.data[route].isLoading || !this.data[route].hasMore) return;
        let that = this;
        let newData = {};
        newData[route+'.isLoading'] = true;
        that.setData(newData);
        if(that.data[route].page.page_no == 1) {
            wx.showLoading({
                title: '加载中',
            });
        }
        wx.request({
            url: that.data.loadGoodsInterfaceUrl,
            data: {
                type: route,
                page: JSON.stringify(that.data[route].page),
                category: route == 'category' ? that.data.category.category : ''
            },
            method: 'POST',
            success: function(res) {
                wx.hideLoading();
                if(res.statusCode == 500) {
                    wx.showToast({
                      title: '系统异常',
                      icon: 'loading',
                      duration: 2000
                    });
                    return;
                }
                let goodsList = that.data[route].goodsList;
                goodsList.push.apply(goodsList, that.calculateRefund(res.data.goodsList));
                newData[route+'.goodsList'] = goodsList;
                newData[route+'.page.page_no'] = parseInt(that.data[route].page.page_no) + 1 + '';
                if(res.data.page.total_page == that.data[route].page.page_no
                    || res.data.goodsList.length == 0) {
                    newData[route+'.hasMore'] = false;
                }
                that.setData(newData);
            },
            fail: function(errMsg) {
                wx.showToast({
                  title: '网络异常',
                  icon: 'loading',
                  duration: 2000
                });
            },
            complete: function() {
                newData = {};
                newData[route+'.isLoading'] = false;
                that.setData(newData);
            }
        });
    },

    /**
     * 获取推荐商品列表
     */
    loadRecommendGoodsList: function() {
        var that = this;
        wx.request({
            url: that.data.loadRecommendGoodsInterfaceUrl,
            data: {},
            method: 'POST',
            success: function(res) {
                if(res.statusCode != 500) {
                    that.setData({
                        recommendGoodsList: res.data.recommendList
                    });
                }
            }
        });
    },

    /**
     * 获取banner
     */
    loadBanner: function() {
        var that = this;
        wx.request({
            url: that.data.loadBannerInterfaceUrl,
            data: {},
            method: 'POST',
            success: function(res) {
                if(res.statusCode != 500) {
                    that.setData({
                        banner: res.data.banner
                    });
                }
            }
        });
    },

    /**
     * 计算商品返还价格
     */
    calculateRefund: (goodsList) => {
        goodsList.forEach(goods => { goods.refund = (goods.market_price - goods.discount_price).toFixed(2) });
        return goodsList;
    }
})