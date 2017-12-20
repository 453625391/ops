$(function() {
	$('#buyNow').tap(function(e) {
		var id = $(this).attr('data-id');
		window.jsObj.buyGoods(id);
	});
});