//切换登录

$(".loginwrapper>span").mouseenter(function(){
	$(this).addClass("current").siblings().removeClass("current");
	var index = $(this).index();
	$(".loginw > div").hide().eq(index).stop(true,true).fadeIn();
});

$("input[name=userinput]").focus(function() {
	$(this).val("");
}).blur(function() {
	$(this).val("请输入用户名");
	$(this).css({
		"color": "#ccc"
	});
});