var Tip = (function() {
	var createMsgHTML = function(msg, type) {
		var tipWrapDom = document.createElement('DIV');
		tipWrapDom.className = '__tip-wrap__ ' + type;
		var msgDom = document.createElement('P');
		msgDom.innerHTML = msg;
		msgDom.className = '__tip-msg__';
		tipWrapDom.appendChild(msgDom);
		document.body.appendChild(tipWrapDom);
		setTimeout(function() {
			document.body.removeChild(tipWrapDom);
		}, 3000);
	}

	var TipObj = function() {};
	TipObj.prototype.info = function(msg) {
		createMsgHTML(msg, 'info');
	}
	TipObj.prototype.success = function(msg) {
		createMsgHTML(msg, 'success');
	}
	TipObj.prototype.warning = function(msg) {
		createMsgHTML(msg, 'warning');
	}
	TipObj.prototype.danger = function(msg) {
		createMsgHTML(msg, 'danger');
	}

	return TipObj;
})();

// 绑定到全局对象M上
if(M) {
	var Tip = new Tip();
	M.Tip = Tip;
}