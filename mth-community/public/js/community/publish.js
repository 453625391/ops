let vm = new Vue({
    el: '#publish',
    data: () => {
        return {
            publishFormData: {
                theme_type: 'share',
                member_id: _mi,
                title: '',
                content: '',
                cover_path: '',
                location: '',
                latitude: '',
                longitude: '',
                work_circle: false,
                life_circle: false,
                home_circle: false,
                hobby_circle: false
            }
        }
    },
    methods: {
        getLocation: function() {
            if (M.iOSBridge) {
                M.iOSBridge.callHandler('getLocation', (data) => { setLocation(data) });
            } else {
                window.jsObj.getLocation('setLocation');
            }
        },
        uploadImg: function(isCover) {
            if (M.iOSBridge) {
                if(isCover) {
                    M.iOSBridge.callHandler('uploadImg', (data) => { handleCover(data) });
                } else {
                    M.iOSBridge.callHandler('uploadImg', (data) => { handleIllustration(data) });
                }
            } else {
                let cbName = isCover ? 'handleCover' : 'handleIllustration'
                window.jsObj.uploadImg(cbName);
            }
        },
        closeViewAndRefreshArticleList: function() {
            if(M.iOSBridge) {
                M.iOSBridge.callHandler('closeAndRefresh');
            } else {
                window.jsObj.closeAndRefresh();
            }
        },
        publish: function() {
            let self = this;
            if(!self.publishFormData.cover_path) {
                M.Tip.info('请添加封面');
                return;
            }
            if(!self.publishFormData.work_circle 
                && !self.publishFormData.life_circle 
                && !self.publishFormData.home_circle 
                && !self.publishFormData.hobby_circle) {
                M.Tip.info('请选择要发布的圈子！');
                return;
            }
            self.publishFormData.content = document.getElementById('content').innerHTML;    // contenteditable元素Vue暂不支持model绑定
            fetch('/addArticle', {
                credentials: 'include',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                method: 'post',
                body: JSON.stringify(self.publishFormData)
            })
            .then(res => {
                if(res.ok) {
                    res.json().then(data => {
                        if(data.rsp_code == 'succ') {
                            self.closeViewAndRefreshArticleList();
                        } else {
                            M.Tip.danger('发布失败，请稍后重试！');
                        }
                    });
                }
            })
            .catch(e => {
                M.Tip.danger('系统繁忙！');
            });
        }
    },
    mounted: function() {
        setTimeout(() => {this.getLocation()}, 1000);
    }
});

// 设置所在位置
function setLocation(data) {
    if (typeof data === 'string') data = JSON.parse(data);
    vm.publishFormData.location = (data.city || '') + (data.district || '') + (data.streetName || '');
    vm.publishFormData.latitude = data.latitude;
    vm.publishFormData.longitude = data.longitude;
}

// 处理封面上传
function handleCover(data) {
    if (typeof data === 'string') data = JSON.parse(data);
    vm.publishFormData.cover_path = data.doc_path;
}

// 处理插图上传
function handleIllustration(data) {
    if (typeof data === 'string') data = JSON.parse(data);
    document.getElementById('content').innerHTML += '<div class="illustration-wrap"><img src="'+ data.doc_path +'" /></div><br>';
}