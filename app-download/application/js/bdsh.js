var itunes_url = 'https://itunes.apple.com/cn/app/id1076505403';
var apk_url = 'http://mps-static.meitianhui.com/apps/consumer/ConvenitenBuy-release-latest.apk';

function getUA() {
	var UA = {
		isWechat: false,
		isiOS: false
	};
	var uastr = window.navigator.userAgent.toLowerCase();
	if(uastr.match(/MicroMessenger/i) == 'micromessenger') {
		UA.isWechat = true;
	}
	if(uastr.indexOf('iphone') != -1) {
		UA.isiOS = true;
	}
	return UA;
}

$(function() {
	new Swiper('.swiper-container', {slidesPerView: 'auto'});
	var UA = getUA();
	$('.download').click(function() {
		if(UA.isWechat) {
			if(UA.isiOS) {
				$('.tip-android').remove();
			} else {
				$('.tip-ios').remove();
			}
			$('.mask-layer').show();
		} else {
			if(UA.isiOS) {
				window.location.href = itunes_url;
			} else {
				window.location.href = apk_url
			}
		}
	});
	$('.mask-layer').click(function() {
		$(this).hide();
	});
});