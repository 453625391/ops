var apk_url = 'http://mps-static.meitianhui.com/apps/hdsy/app-release-hdsy-latest.apk';

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
			window.location.href = apk_url
		}
	});
	$('.mask-layer').click(function() {
		$(this).hide();
	});
});