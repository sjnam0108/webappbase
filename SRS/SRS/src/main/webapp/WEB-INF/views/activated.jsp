<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/login" var="loginUrl" />
<c:url value="/loginkey" var="loginKeyUrl" />


<!-- Opening tags -->

<!DOCTYPE html>
<html lang="${html_lang}" class="default-style">
<head>
	<title>${pageTitle}</title>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="IE=edge,chrome=1">
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">

    <link href="/favicon.ico" rel="icon" type="image/x-icon">

	<link href="<c:url value='/resources/css/kendo.bootstrap.twitlight.all.css'/>" rel="stylesheet" />

    <link rel="stylesheet" href="/resources/vendor/css/bootstrap.css">
    <link rel="stylesheet" href="/resources/vendor/css/appwork.css">
    <link rel="stylesheet" href="/resources/vendor/css/theme-bbmc-twitlight-blue.css">
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
	-->
	<script src="https://kit.fontawesome.com/344b8987c7.js" crossorigin="anonymous"></script>
	
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
</head>
	<body>
		<div class="page-loader">
			<div class="bg-primary"></div>
		</div>
        

<!-- Content -->

<div class="m-3">
	<h3 class="text-blue">[SRS]</h3>
	<p>
	${msg}
	</p>
</div>

<!-- / Content -->


<!-- Base modules -->
<common:base />


		<!-- Core scripts -->
		<script src="/resources/vendor/lib/popper/popper.js"></script>
		<script src="/resources/vendor/js/bootstrap.js"></script>
		<script src="/resources/vendor/js/sidenav.js"></script>

		<!-- Libs -->
		<script src="/resources/vendor/lib/perfect-scrollbar/perfect-scrollbar.js"></script>
		<script src="/resources/vendor/lib/toastr/toastr.js"></script>
		<script src="/resources/vendor/lib/bootbox/bootbox.js"></script>
		<script src="/resources/vendor/lib/bootstrap-select/bootstrap-select.js"></script>
	</body>
</html>