<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->


<!-- Opening tags -->

<common:pageOpening />


<!-- Page title -->

<h4 class="pt-1 pb-3 mb-3">
	<i class="mr-1 fa fa-home"></i>
	${pageTitle}
</h4>

<hr class="border-light container-m--x mt-0 mb-4">





<!-- Page body -->


<ul class="nav nav-tabs tabs-alt container-p-x container-m--x mb-4">
	<li class="nav-item"><a class="nav-link active" data-toggle="tab"
		href="#shortcuts"> <i class="mr-1 fa fa-link"></i> 빠른 링크
	</a></li>
</ul>
<div class="tab-content">
	<div class="tab-pane active" id="shortcuts">
		<div class="row">
			<div class='card col-md-4 col-sm-4 col-4 col-lg-4 col-xl-4'>
				<div class="card-body">
					<div style="float:left">
						<h3>
							<i class="fa fa-desktop mr-2"></i>모니터링현황판
						</h3>
						<p class="card-text">모니터링</p>
					</div>
					<div class="float-right">
						<button id="move-btn" type="button"
							class="btn btn-outline-success" onclick="moveUrl('monitermain')">이동</button>
					</div>
				</div>
			</div>
			<div class='card col-md-4 col-sm-4 col-4 col-lg-4 col-xl-4'>
				<div class="card-body">
					<div style="float:left">
						<h3 >
							<i class="fa fa-calendar-alt mr-2"></i>그리드 뷰
						</h3>
						<p class="card-text">모니터링</p>
					</div>
					<div class="float-right">
						<button id="move-btn" type="button"
							class="btn btn-outline-success" onclick="moveUrl('gridview')">이동</button>
					</div>
				</div>
			</div>
			<div class='card col-md-4 col-sm-4 col-4 col-lg-4 col-xl-4'>
				<div class="card-body">
					<div style="float:left">
						<h3 >
							<i class="fa fa-map mr-2"></i>지도 뷰
						</h3>
						<p class="card-text">모니터링</p>
					</div>
					<div class="float-right">
						<button id="move-btn" type="button"
							class="btn btn-outline-success"onclick="moveUrl('mapview')">이동</button>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class='card col-md-4 col-sm-4 col-4 col-lg-4 col-xl-4'>
				<div class="card-body">
					<div style="float:left">
						<h3 >
							<i class="fa fa-chart-line mr-2"></i>기간 차트
						</h3>
						<p class="card-text">모니터링</p>
					</div>
					<div class="float-right">
						<button id="move-btn" type="button"
							class="btn btn-outline-success"onclick="moveUrl('datechart')">이동</button>
					</div>
				</div>
			</div>
			<div class='card col-md-4 col-sm-4 col-4 col-lg-4 col-xl-4'>
				<div class="card-body">
					<div style="float:left">
						<h3 >
							<i class="fa fa-calendar-alt mr-2"></i>지역날씨
						</h3>
						<p class="card-text">모니터링</p>
					</div>
					<div class="float-right">
						<button id="move-btn" type="button"
							class="btn btn-outline-success" onclick="moveUrl('localweather')">이동</button>
					</div>
				</div>
			</div>
			<div class='card col-md-4 col-sm-4 col-4 col-lg-4 col-xl-4'>
				<div class="card-body">
					<div style="float:left">
						<h3 >
							<i class="fa fa-calendar-alt mr-2"></i>현장정보
						</h3>
						<p class="card-text">모니터링</p>
					</div>
					<div class="float-right">
						<button id="move-btn" type="button"
							class="btn btn-outline-success" onclick="moveUrl('onsite')">이동</button>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class='card col-md-4 col-sm-4 col-4 col-lg-4 col-xl-4'>
				<div class="card-body">
					<div style="float:left">
						<h3 >
							<i class="fas fa-exclamation-triangle mr-2"></i>돌발상황 정보
						</h3>
						<p class="card-text">모니터링</p>
					</div>
					<div class="float-right">
						<button id="move-btn" type="button"
							class="btn btn-outline-success" onclick="moveUrl('roadinfo')">이동</button>
					</div>
				</div>
			</div>
			<div class='card col-md-4 col-sm-4 col-4 col-lg-4 col-xl-4'>
				<div class="card-body">
					<div style="float:left">
						<h3 >
							<i class="fas fa-tachometer-alt mr-2"></i>제한속도 정보
						</h3>
						<p class="card-text">모니터링</p>
					</div>
					<div class="float-right">
						<button id="move-btn" type="button"
							class="btn btn-outline-success" onclick="moveUrl('splimitinfo')">이동</button>
					</div>
				</div>
			</div>
			<div class='card col-md-4 col-sm-4 col-4 col-lg-4 col-xl-4'>
				<div class="card-body">
					<div style="float:left">
						<h3 >
							<i class="fa fa-wifi mr-2"></i>적정광도 제어
						</h3>
						<p class="card-text">원격제어</p>
					</div>
					<div class="float-right">
						<button id="move-btn" type="button"
							class="btn btn-outline-success" onclick="moveUrl('recontrolauto')">이동</button>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class='card col-md-4 col-sm-4 col-4 col-lg-4 col-xl-4'>
				<div class="card-body">
					<div style="float:left">
						<h3 >
							<i class="fa fa-wifi mr-2"></i>수동제어
						</h3>
						<p class="card-text">원격제어</p>
					</div>
					<div class="float-right">
						<button id="move-btn" type="button"
							class="btn btn-outline-success" onclick="moveUrl('recontrolpassive')">이동</button>
					</div>
				</div>
			</div>
			<div class='card col-md-4 col-sm-4 col-4 col-lg-4 col-xl-4'>
				<div class="card-body">
					<div style="float:left">
						<h3 >
							<i class="fa fa-bell mr-2"></i>안내설정
						</h3>
						<p class="card-text">유고사항</p>
					</div>
					<div class="float-right">
						<button id="move-btn" type="button"
							class="btn btn-outline-success" onclick="moveUrl('noticesetting')">이동</button>
					</div>
				</div>
			</div>
			<div class='card col-md-4 col-sm-4 col-4 col-lg-4 col-xl-4'>
				<div class="card-body">
					<div style="float:left">
						<h3 >
							<i class="fa fa-bell mr-2"></i>정보등록
						</h3>
						<p class="card-text">유고사항</p>
					</div>
					<div class="float-right">
						<button id="move-btn" type="button"
							class="btn btn-outline-success" onclick="moveUrl('noticeenroll')">이동</button>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class='card col-md-4 col-sm-4 col-4 col-lg-4 col-xl-4'>
				<div class="card-body">
					<div style="float:left">
						<h3 >
							<i class="fa fa-bell mr-2"></i>적정속도 관리
						</h3>
						<p class="card-text">유고사항</p>
					</div>
					<div class="float-right">
						<button id="move-btn" type="button"
							class="btn btn-outline-success" onclick="moveUrl('appspeedmgr')">이동</button>
					</div>
				</div>
			</div>
			<div class='card col-md-4 col-sm-4 col-4 col-lg-4 col-xl-4'>
				<div class="card-body">
					<div style="float:left">
						<h3 >
							<i class="fa fa-bell mr-2"></i>결빙예측
						</h3>
						<p class="card-text">유고사항</p>
					</div>
					<div class="float-right">
						<button id="move-btn" type="button"
							class="btn btn-outline-success" onclick="moveUrl('freezpredic')">이동</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>



<!-- Grid button actions  -->

<script>
	$(document).ready(function() {

		

	});
//이동버튼 클릭시 해당 url 로 이동
function moveUrl(val) {
	location.href = 'https://srs.bbmc.co.kr/srs/'+ val;
}
	
	
</script>

<!-- / Grid button actions  -->


<!-- / Page body -->





<!-- Functional tags -->

<func:cmmValidate />


<!-- Closing tags -->

<common:base />
<common:pageClosing />
