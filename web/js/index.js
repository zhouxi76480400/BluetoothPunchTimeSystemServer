$(function(){
	//导航鼠标移动事件
	$(".leftmenu>li").mouseenter(function(){
		$(this).addClass("current").siblings().removeClass("current");
	});
	//鼠标移出导航事件
	$(".leftmenu>li").mouseleave(function(){
		$(".leftmenu>li").removeClass("current");
	});
	//电子图书页签切换
	$(".ebookleft>div.title ul>li").mouseenter(function(){
		$(this).addClass("current").siblings().removeClass("current");
		var a = $(".ebookleft ul>li").index(this);
		$("#ebook .content>div.item").hide().eq(a).show();
	});
	
	//新书畅销效果
	$(".ebookright >ul>li").mouseenter(function(){
		$(this).addClass("current").siblings().removeClass("current");
	});
	
	//服装页签
	$(".productlist>div.title>ul>li").mouseenter(function(){
		//当li标签有current样式时，不执行动画 。没有时执行动画
		if(!$(this).hasClass("current")){
			$(this).addClass("current").siblings().removeClass("current");
//		var b = $(".productlist>div.title ul>li").index(this);
//		$(".productlist>.content>div.item").hide().eq(b).show();
//		
			var b = $(this).parent().find("li").index(this);
			$(this).parent().parent().parent().find(".item").hide().eq(b).stop(true,true).fadeIn();	
		}	
	});
	
	
	//推广商品
	$("#recommproduct>div.title>ul>li").mouseenter(function(){
		$(this).addClass("current").siblings().removeClass("current");
		var index = $("recommproduct>div.title>ul>li").index(this);
		$(".recommproductlist").hide().eq(index).stop(true,true).fadeIn();
	});
	
	//头部轮播
	var opts={
			boxh:430,//盒子的高度
			w:1000,//盒子的宽度
			h:390,//图片的高度
			isShow:true,//是否显示控制器
			isShowBtn:true,//是否显示左右按钮
			controltop:15,//控制按钮上下偏移的位置,要将按钮向下移动   首先保证boxh 高度>图片 h
			controlsW:20,//控制按钮宽度
			controlsH:10,//控制按钮高度
			radius:5//控制按钮圆角度数
	};
	$("#ppt01").tyslide(opts);
	
	//电子书轮播
		var opts={
			boxh:218,//盒子的高度
			w:332,//盒子的宽度
			h:218,//图片的高度
			isShow:true,//是否显示控制器
			isShowBtn:true,//是否显示左右按钮
			controltop:15,//控制按钮上下偏移的位置,要将按钮向下移动   首先保证boxh 高度>图片 h
			controlsW:20,//控制按钮宽度
			controlsH:10,//控制按钮高度
			radius:5//控制按钮圆角度数
	};	
	$(".ppt02").tyslide(opts);
	
//	
//			var opts={
//			boxh:424,//盒子的高度
//			w:338,//盒子的宽度
//			h:424,//图片的高度
//			isShow:true,//是否显示控制器
//			isShowBtn:true,//是否显示左右按钮
//			controltop:15,//控制按钮上下偏移的位置,要将按钮向下移动   首先保证boxh 高度>图片 h
//			controlsW:20,//控制按钮宽度
//			controlsH:10,//控制按钮高度
//			radius:5//控制按钮圆角度数
//	};
//	$(".pptbox").tyslide(opts);
//	

		//服装轮播
			var opts={
			boxh:338,//盒子的高度
			w:424,//盒子的宽度
			h:338,//图片的高度
			isShow:true,//是否显示控制器
			isShowBtn:true,//是否显示左右按钮
			controltop:15,//控制按钮上下偏移的位置,要将按钮向下移动   首先保证boxh 高度>图片 h
			controlsW:15,//控制按钮宽度
			controlsH:5,//控制按钮高度
			radius:5//控制按钮圆角度数
	};	
	$(".ppt03").tyslide(opts);
	
	
	//搜索框提示效果
	$("input[name=searchinput]").focus(function(){
		if($(this).val()==$(this).attr("data-title")){
			$(this).val("");
			$(this).css({"color":"#000"});
		}
	}).blur(function(){
		if(!($(this).val().trim().length>0)){
			$(this).val($(this).attr("data-title"));
			$(this).css({"color":"#ccc"});
		}
	});
	
	 getfixedwrapper();  //页面加载时调用搜索框固定在顶部de函数
	//搜索框固定在顶部效果
	$(window).scroll(function(){
      getfixedwrapper();
	})
	function getfixedwrapper(){
		if($(document).scrollTop()>100){
			$("#fixedwrapper").addClass("on");
		}else{
			$("#fixedwrapper").removeClass("on");
		}
	}
	
	
	//电梯效果
	$("#floors>li").mouseenter(function(){
		var txt=$(this).attr("data-text");
		var bgcolor=$(this).attr("data-color");
		$(this).find("span").text(txt).show();
		$(this).css({"background-color":bgcolor});
		$(this).stop(true,true).animate({"width":"80px"},300);
	});
	$("#floors>li").mouseleave(function(){
		$(this).find("span").hide();
		$(this).css({"background-color":"transparent"});
		$(this).stop(true,true).animate({"width":"40px"},300);
	})
	
	
	//滚动到指定位置
	$("#floors>li").click(function(){
		var id = $(this).attr("data-id");
		var topval = $("#"+id).offset().top;
		$("html,body").animate({
			scrollTop:(topval - 200)
		});
	
	});
	
	//鼠标滚动触发楼层变色

$(window).scroll(function() {
	var ebookoffset = parseInt($("#ebook").offset().top); //电子书坐标
	var clothesoffset = parseInt($("#clothes").offset().top); //服装坐标
	var outdooroffset = parseInt($("#sports").offset().top); //户外运动坐标
	var scrolldx = $(document).scrollTop(); //滚动的高度
	var floor = $("#lift>li").eq(0);
	if(scrolldx < clothesoffset && scrolldx >= ebookoffset) {
		floor.css({
			"background-color": floor.attr("data-color")
		});
		floor.siblings().css({
			"background-color": "transparent"
		});
	} else {
		floor.css({
			"background-color": "transparent"
		});
	}
	var floor2 = $("#lift>li").eq(1);
	if(scrolldx < outdooroffset && scrolldx >= clothesoffset) {
		floor2.css({
			"background-color": floor.attr("data-color")
		});
		floor2.siblings().css({
			"background-color": "transparent"
		});
	} else {
		floor2.css({
			"background-color": "transparent"
		});
	}
});

	
	//滚动到顶部效果

//		this.style.display="block";这是dom对象的用法
//正确的应该是$(this).css({display:"block"}); 这是jquery对象的用法

	$(window).scroll(function(){
		if($(window).scrollTop()>100){
			$("#gotoTop").show();
		}else{
			$("#gotoTop").hide();
		}
	})
	$("#gotoTop").click(function(){
	
		$("html,body").animate({
			scrollTop:0
		},500);
	});
		
		
	//二维码效果
	//#codeTwo>li指的是#codeTwo下的直接子元素中有li
	//但是你的html中#codeTwo下直接子元素中没有li，所以获取不到对象
	$("#codeTwo>ul li.codeer").mouseenter(function(){//可以这样写
		$(".ercode").fadeIn();
	}).mouseleave(function(){
		$(".ercode").fadeOut();
	});
		
		
		
		
	
	
	});