let app = new Vue({
    el: '#vm',
    data: () => {
        return {
            article: {
                _id: '',
                theme_type: '',
                member_id: '',
                title: '',
                content: '',
                location: '',
                latitude: '',
                longitude: '',
                work_circle: '',
                life_circle: '',
                home_circle: '',
                hobby_circle: '',
                nick_name: '',
                head_path: '',
                like: [],
                comment: [],
                create_date: '',
                view_count: 0,
                status: '',
                pic_path: [],
                cover_path: [],
                isAlreadyLiked: false
            },
            comment: ''
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
    	addComment: function() {
    		let self = this;
    		if(!self.comment) return;
    		fetch('/addComment', {
                credentials: 'include',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                method: 'post',
                body: JSON.stringify({
                	id: self.article._id,
                	comment_content: self.comment
                })
            })
            .then(res => {
                if(res.ok) {
                    res.json().then(data => {
                        if(data.rsp_code == 'succ') {
                        	data.data.commentTimeFormatted = '刚刚';
                            self.article.comment.push(data.data);
                            self.comment = '';
                        } else {
                        	M.Tip.danger('评论失败，请稍后重试！');
                        }
                    });
                }
            })
            .catch(e => {
                M.Tip.danger('系统繁忙！');
            });
    	},
    	like: function() {
    		let self = this;
    		fetch('/like', {
                credentials: 'include',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                method: 'post',
                body: JSON.stringify({
                	id: self.article._id,
                	m: Math.random()
                })
            })
            .then(res => {
                if(res.ok) {
                    res.json().then(data => {
                        if(data.rsp_code == 'succ') {
                        	self.article.isAlreadyLiked = data.data.isLike;
                        	self.article.like = data.data.like;
                        } else {
                            M.Tip.danger('操作失败，请稍后重试！');
                        }
                    });
                }
            })
    	}
    },
    computed: {
    	reversedComment: function() {
    		return this.article.comment.reverse();
    	},
    	handledContent: function() {
    		let content = decodeURIComponent(this.article.content);
    		return content;
    	}
    },
    beforeMount: function() {
    	this.article = _article;
    },
    mounted: function() {
    	
    }
});
