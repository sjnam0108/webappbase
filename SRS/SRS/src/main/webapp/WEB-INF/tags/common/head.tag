<%@ tag pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
	<title>${pageTitle}</title>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="IE=edge,chrome=1">
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
	<meta id ="_csrf" name = "_csrf" content ="${_csrf.token}"/>
	<meta id ="_csrf_header" name="_csrf_header" content="${_csrf.headerNmae} " />
    <link href="/favicon.ico" rel="icon" type="image/x-icon">

	<link href="<c:url value='/resources/css/kendo.bootstrap.cotton.all.css'/>" rel="stylesheet" />

    <link rel="stylesheet" href="/resources/vendor/css/bootstrap.css">
    <link rel="stylesheet" href="/resources/vendor/css/appwork.css">
    <link rel="stylesheet" href="/resources/vendor/css/theme-bbmc-cotton-red.css">
    <link rel="stylesheet" href="/resources/vendor/css/colors.css">
    <link rel="stylesheet" href="/resources/vendor/css/uikit.css">
    
	<link rel="stylesheet" href="/resources/vendor/lib/perfect-scrollbar/perfect-scrollbar.css">
	<link rel="stylesheet" href="/resources/vendor/lib/toastr/toastr.css">
	<link rel="stylesheet" href="/resources/vendor/lib/bootstrap-select/bootstrap-select.css">
	<link rel="stylesheet" href="/resources/vendor/lib/spinkit/spinkit.css">
	<link rel="stylesheet" href="/resources/vendor/lib/bootstrap-slider/bootstrap-slider.css">
	<link rel="stylesheet" href="/resources/vendor/lib/nouislider/nouislider.css">
	
    <link rel="stylesheet" href="/resources/base/base.css">

    <script src="/resources/vendor/js/layout-helpers.js"></script>
    <script src="/resources/vendor/js/pace.js"></script>

	<!--
	<script src="<c:url value='/resources/vendor/js/fa57.all.min.js' />"></script>
		
	<script src="https://kit.fontawesome.com/344b8987c7.js" crossorigin="anonymous"></script>
	-->
	
	<script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.14.0/js/all.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.14.0/js/all.min.js"></script>
		
	<script src="<c:url value='/resources/js/jquery.min.js' />"></script>
	<script src="<c:url value='/resources/js/jszip.min.js' />"></script>
	<script src="<c:url value='/resources/js/kendo.all.min.js' />"></script>
	
	<script src="<c:url value='/resources/vendor/lib/jquery-ui/jquery-ui.min.js' />"></script>

	<script src="<c:url value='/resources/shared/js/prettify.js'/>"></script>
	<script src="<c:url value='/resources/shared/js/sockjs.min.js'/>"></script>
	<script src="<c:url value='/resources/shared/js/jsbn.js' />"></script>
	<script src="<c:url value='/resources/shared/js/prng4.js' />"></script>
	<script src="<c:url value='/resources/shared/js/rng.js' />"></script>
	<script src="<c:url value='/resources/shared/js/rsa.js' />"></script>
	
	<script src="<c:url value='/resources/js/cultures/kendo.culture.${kendoLangCountryCode}.min.js' />"></script>
	<script src="<c:url value='/resources/js/messages/kendo.messages.${kendoLangCountryCode}.min.js' />"></script>
	<script> kendo.culture("${kendoLangCountryCode}"); </script>
	
	
	<style>
.map_wrap, .map_wrap * {margin:0;padding:0;font-family:'Malgun Gothic',dotum,'돋움',sans-serif;font-size:12px;}
.map_wrap a, .map_wrap a:hover, .map_wrap a:active{color:#000;text-decoration: none;}
.map_wrap {position:relative;width:100%;height:500px;}
#menu_wrap {position:absolute;top:0;left:0;bottom:0;width:250px;margin:10px 0 30px 10px;padding:5px;overflow-y:auto;background:rgba(255, 255, 255, 0.7);z-index: 1;font-size:12px;border-radius: 10px;}
.bg_white {background:#fff;}
#menu_wrap hr {display: block; height: 1px;border: 0; border-top: 2px solid #5F5F5F;margin:3px 0;}
#menu_wrap .option{text-align: center;}
#menu_wrap .option p {margin:10px 0;}  
#menu_wrap .option button {margin-left:5px;}
#placesList li {list-style: none;}
#placesList .item {position:relative;border-bottom:1px solid #888;overflow: hidden;cursor: pointer;min-height: 65px;}
#placesList .item span {display: block;margin-top:4px;}
#placesList .item h5, #placesList .item .info {text-overflow: ellipsis;overflow: hidden;white-space: nowrap;}
#placesList .item .info{padding:10px 0 10px 55px;}
#placesList .info .gray {color:#8a8a8a;}
#placesList .info .jibun {padding-left:26px;background:url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_jibun.png) no-repeat;}
#placesList .info .tel {color:#009900;}
#placesList .item .markerbg {float:left;position:absolute;width:36px; height:37px;margin:10px 0 0 10px;background:url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png) no-repeat;}
#placesList .item .marker_1 {background-position: 0 -10px;}
#placesList .item .marker_2 {background-position: 0 -56px;}
#placesList .item .marker_3 {background-position: 0 -102px}
#placesList .item .marker_4 {background-position: 0 -148px;}
#placesList .item .marker_5 {background-position: 0 -194px;}
#placesList .item .marker_6 {background-position: 0 -240px;}
#placesList .item .marker_7 {background-position: 0 -286px;}
#placesList .item .marker_8 {background-position: 0 -332px;}
#placesList .item .marker_9 {background-position: 0 -378px;}
#placesList .item .marker_10 {background-position: 0 -423px;}
#placesList .item .marker_11 {background-position: 0 -470px;}
#placesList .item .marker_12 {background-position: 0 -516px;}
#placesList .item .marker_13 {background-position: 0 -562px;}
#placesList .item .marker_14 {background-position: 0 -608px;}
#placesList .item .marker_15 {background-position: 0 -654px;}
#pagination {margin:10px auto;text-align: center;}
#pagination a {display:inline-block;margin-right:10px;}
#pagination .on {font-weight: bold; cursor: default;color:#777;}

</style>
	
	
	
	
	
	
</head>
