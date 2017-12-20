// detail.js
Page({

    /**
    * 页面的初始数据
    */
    data: {
        loadGoodsInterfaceUrl: 'https://appportal.meitianhui.com/openapi/wxapp/getGoodsDetail',
        goods: {}
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        this.getGoodsDetail(options.goods_id);
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
        
    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function() {

    },

    /**
     * 查询商品详情
     */
    getGoodsDetail: function(goods_id) {
        var that = this;
        wx.request({
            url: that.data.loadGoodsInterfaceUrl,
            data: {
                goods_id: goods_id
            },
            method: 'POST',
            success: function(res) {
                if(res.statusCode != 500) {
                    console.log(res.data.goods);
                    that.setData({
                        goods: res.data.goods
                    });
                }
            }
        });
    }
})