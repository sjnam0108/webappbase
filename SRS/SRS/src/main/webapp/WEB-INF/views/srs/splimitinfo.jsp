<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/srs/localctrl/read" var="readUrl" />
<c:url value="/srs/localctrl/countstate" var="cntStateUrl" />
<c:url value="/srs/localctrl/readId" var="readIdUrl" />
<c:url value="/srs/gridview/update" var="updateUrl" />
<c:url value="/srs/gridview/stateTime" var="stateTimeUrl" />
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/proj4js/1.1.0/proj4js-combined.min.js"></script>

<!-- Opening tags -->

<common:pageOpening />


<!-- Page title -->

<h4 class="pt-1 pb-3 mb-3">
	<i class="fas fa-tachometer-alt mr-2"></i> ${pageTitle}
</h4>

<hr class="border-light container-m--x mt-0 mb-4">
<%
String lc_stateTemplate = "# if (lc_state == '0' ) { #"
		+ "<span class='pl-2'><img src='/resources/shared/images/logo/blue.png'></span>"
		+ "# } else if (lc_state == '1') { #"
		+ "<span class='pl-2'><img src='/resources/shared/images/logo/yellow.png'></span>"
		+ "# } else if (lc_state == '2') { #"
		+ "<span class='pl-2'><img src='/resources/shared/images/logo/orange.png'></span>"
		+ "# } else if (lc_state == '3') { #"
		+ "<span class='pl-2'><img src='/resources/shared/images/logo/red.png'></span>"
		+ "# } else if (lc_state == '4') { #"
		+ "<span class='pl-2'><img src='/resources/shared/images/logo/gray.png'></span>" + "# } else { #"
		+ "<span class='pl-2'><img src='/resources/shared/images/logo/gray.png'></span>" + "# } #";
%>

<%
String editTemplate = "<button type='button'  onclick='edit(#= id #)' class='btn icon-btn btn-sm btn-outline-success borderless'>"
		+ "<span class='fas fa-search'></span></button>";
%>

<!-- Kendo grid  -->
<div class="mb-4">
	<img src="/resources/shared/images/logo/blue.png"> <span
		class="mr-1" id="blue"></span> <img
		src="/resources/shared/images/logo/yellow.png"> <span
		class="mr-1" id="yellow"></span> <img
		src="/resources/shared/images/logo/orange.png"> <span
		class="mr-1" id="orange"></span> <img
		src="/resources/shared/images/logo/red.png"> <span class="mr-1"
		id="red"></span> <img src="/resources/shared/images/logo/gray.png">
		<span class="mr-1 pr-2" id="gray"></span>
	<span>실시간 (10초 주기)</span>
</div>


<!-- Kendo grid  -->
<div class="row">
<div class="col-6 mt-3">
	<kendo:grid name="grid" id="grid" pageable="true" groupable="false"
		filterable="true" sortable="true" scrollable="false"
		reorderable="true" resizable="true"
		selectable="true">
		<kendo:grid-excel fileName="${pageTitle}.xlsx" allPages="true"
			proxyURL="/proxySave" />
		<kendo:grid-pageable refresh="true" buttonCount="5" pageSize="10"
			pageSizes="${pageSizesNormal}" />
		<kendo:grid-toolbarTemplate>
			<div class="d-flex align-items-center">
				 <span class="d-none d-lg-inline">
					<button type="button" class="btn btn-default k-grid-excel">${cmd_excel}</button>
				</span> 
			</div>
		</kendo:grid-toolbarTemplate>
		<kendo:grid-columns>
			<kendo:grid-column title="상태" field="lc_state"
				template="<%=lc_stateTemplate%>" />
			<kendo:grid-column title="로컬제어기" field="lc_name" minScreenWidth="600" />
			<kendo:grid-column title="도로명" field="lc_road_name" />
			<kendo:grid-column title="제한속도(km/s)" field="lc_sp_limit" />
			<kendo:grid-column title="총거리(m)" field="lc_total_distance" />
		</kendo:grid-columns>
		<kendo:grid-filterable>
			<kendo:grid-filterable-messages
				selectedItemsFormat="${filter_selectedItems}" />
		</kendo:grid-filterable>
		<kendo:dataSource serverPaging="true" serverSorting="true"
			serverFiltering="true" serverGrouping="true" error="kendoReadError">
			<kendo:dataSource-sort>
				<kendo:dataSource-sortItem field="id" dir="desc" />
			</kendo:dataSource-sort>
			<kendo:dataSource-transport>
				<kendo:dataSource-transport-read url="${readUrl}" dataType="json"
					type="POST" contentType="application/json" />
				<kendo:dataSource-transport-parameterMap>
					<script>
					function parameterMap(options,type) {
						return JSON.stringify(options);	
					}
				</script>
				</kendo:dataSource-transport-parameterMap>
			</kendo:dataSource-transport>
			<kendo:dataSource-schema data="data" total="total" groups="data">
				<kendo:dataSource-schema-model id="id">
					<kendo:dataSource-schema-model-fields>
						<kendo:dataSource-schema-model-field name="whoCreationDate"
							type="date" />
					</kendo:dataSource-schema-model-fields>
				</kendo:dataSource-schema-model>
			</kendo:dataSource-schema>
		</kendo:dataSource>
	</kendo:grid>
</div>

<div class="col-6">
<div id="layout_wrap">
<!--  
 	<div class="float-left">
			<button class="form-control"id="btn_select">검색</button>
	</div>
	-->
	<p id="result"></p>
	<div id="map_wrap" class="map_wrap" style="position: relative;">
		<div id="map_div" class="wrap"></div>
	</div>
	<div class="map_act_btn_wrap clear_box"></div>
</div>
</div>
<style>

/* 선택 체크박스를 포함하는 필터 패널을 보기 좋게 */
.k-filter-selected-items {
	font-weight: 500;
	margin: 0.5em 0;
}

.k-filter-menu .k-button {
	width: 47%;
	margin: 0.5em 1% 0.25em;
}

.k-chart.small-chart {
	display: inline-block;
	width: 300px;
	height: 300px;
}

/* Kendo 그리드에서 bootstrap dropdownmenu 모두 보이게 */
.k-grid td {
	overflow: visible;
}

<style type="text/css">
.wrap { position:relative; /*감싸는 레이어에 포지션 속성을 잡아주는 게 필수!(relative, absolute, fixed 중 택1*/ text-align:center; line-height:100px; margin:0 auto; font-size:12px; }
.over { position:absolute; top:46%; left:49%;/*위에 올라가는 레이어의 포지션은 top, bottom 둘 중 하나, left, right 둘 중 하나의 속성을 선택하여 잡아준다.*/ width:10px; height:10px; text-align:center; transform:translate(-50%,-50%);}

</style>

<!-- / Kendo grid  -->


<!-- Grid button actions  -->
<script
	src="https://apis.openapi.sk.com/tmap/jsv2?version=1&appKey=l7xx13f33846187e4dd7bbb1a4df23dbdefd"></script>
<script>
	$(document).ready(function() {
		var lon = 126.927918311855;
		var lat = 37.3757737206854;
		//tmap 생성 시 위 gps 값으로 초기지도 생성
		initTmap(lon,lat);
		//최초 로컬 기기 상태 불러오기
		get_count_state();
		//10초주기로 로컬 기기 상태 불러오기
		setInterval(get_count_state, 10000);
		var grid = $("#grid").data("kendoGrid");
		grid.bind("change", grid_change);

		// Add
		$("#add-btn").click(function(e) {
			e.preventDefault();

			initForm1();

			$('#form-modal-1 .modal-dialog').draggable({
				handle : '.modal-header'
			});
			$("#form-modal-1").modal();
		});
		// / Add

		// Delete
		$("#delete-btn").click(function(e) {
			e.preventDefault();

			var grid = $("#grid").data("kendoGrid");
			var rows = grid.select();

			var delRows = [];
			rows.each(function(index, row) {
				var selectedItem = grid.dataItem(row);
				delRows.push(selectedItem.id);
			});

			if (delRows.length > 0) {
				showDelConfirmModal(function(result) {
					if (result) {
						$.ajax({
							type : "POST",
							contentType : "application/json",
							dataType : "json",
							url : "${destroyUrl}",
							data : JSON.stringify({
								items : delRows
							}),
							success : function(form) {
								showDeleteSuccessMsg();
								grid.dataSource.read();
							},
							error : ajaxDeleteError
						});
					}
				}, true, delRows.length);
			}
		});
		// / Delete

	});
</script>

<!-- / Grid button actions  -->


<!--  Root form container -->
<div id="formRoot"></div>


<!--  Forms -->


<script id="template-1" type="text/x-kendo-template">


<div class="modal fade" data-backdrop="static" id="form-modal-1">
	<div class="modal-dialog" style="max-width:43rem">
		<div class="modal-content" id="form-1">
      
			<!-- Modal Header -->
			<div class="modal-header move-cursor">
				<h5 class="modal-title">
					<span name="title"></span>
				</h5>
				<button type="button" class="close" data-dismiss="modal">×</button>
			</div>
        
			<!-- Modal body -->
			<div class="modal-body modal-bg-color py-3">
				<div id="title-container"></div>

				
				<div class="nav-tabs-top mt-1 px-2">
					<ul class="nav nav-tabs px-1">
						<li class="nav-item">
							<a class="nav-link active" data-toggle="tab" id="lo-basic" href="\\\#lo-basic-ctnt">
								<span class="fas fa-chart-pie"></span><span class="pl-1">운영</span>
							</a>
						</li>
						<li class="nav-item">
							<a class="nav-link" data-toggle="tab" id="lo-option" href="\\\#lo-option-ctnt">
								<span class="fa fa-plane"></span><span class="pl-1">상태</span>
							</a>
						</li>
					</ul>
					<div class="tab-content mx-1">
						<div class="tab-pane p-2 active" id="lo-basic-ctnt">
							<div class="mb-4">
								<div class="row">
									<div class='mt-3 card col-md-6 col-sm-6 col-6 col-lg-6 col-xl-6'>
										<div id="chart" class="small-chart" style="background: center no-repeat url('/resources/shared/images/logo/pi_chart_bg.png');"></div>
									</div>
									<div class="mt-3 card col-md-6 col-sm-6 col-6 col-lg-6 col-xl-6" >
										<ul class="list-group list-group-flush">
											<li class="list-group-item" style="padding-right:0; padding-left:0">
												<input type="date" name="state_date" value="" onchange="changeDate()"class="form-control ">
											</li>
											<li class="list-group-item">
												<img class="mr-1" src="/resources/shared/images/logo/blue.png">정상 : <span name="normal">0</span>
											</li>
											<li class="list-group-item">
												<img class="mr-1" src="/resources/shared/images/logo/yellow.png">SR 일부꺼짐 :	<span name="partOff">0</span>										</li>
											<li class="list-group-item">
												<img  class="mr-1" src="/resources/shared/images/logo/orange.png">SR 모두꺼짐 :<span name="allOff">0</span>
											</li>
											<li class="list-group-item">
												<img class="mr-1" src="/resources/shared/images/logo/red.png">로컬제어기 꺼짐 :<span name="lcOff">0</span>
											</li>
										</ul>
									</div>
								</div>
						</div>
					</div>	
						<div class="tab-pane p-2 fade" id="lo-option-ctnt">
							<div id="test"> </div>
						</div>
					</div>
				</div>
			</div>
        
			<!-- Modal footer -->
			<div class="modal-footer d-flex">

			</div>
			
		</div>
	</div>
</div>



</script>
<!--  Scripts -->

<script>
    //로컬제어기 상태별 갯수 불러오기 이벤트
	function get_count_state() {
		data = {
			id : "111"
		};
		$.ajax({
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			data : JSON.stringify(data),
			url : "${cntStateUrl}",
			success : function(data) {
				setCntState(data);
				console.log("성공");
			}
		//error: ajaxReadError
		});
	}
    //로컬제어기 상태별 갯수 카운트 이벤트
	function setCntState(data) {
		blue = ""
		yellow = ""
		orange = ""
		red = ""
		gray = ""
		k = 0
		for (i = 0; i < data.length; i++) {

			if (data[i][0] == "0") {
				blue = data[i][1];
			}
			if (data[i][0] == "1") {
				yellow = data[i][1];
			}
			if (data[i][0] == "2") {
				orange = data[i][1];
			}
			if (data[i][0] == "3") {
				red = data[i][1];
			}
			if (data[i][0] == "4") {
				gray = data[i][1];
			}
			if (data[i][0] == null) {
				k = k + 1;
				gray = k;
			}
		}

		if (blue == "") {
			blue = "0"
		}
		if (yellow == "") {
			yellow = "0"
		}
		if (orange == "") {
			orange = "0"
		}
		if (red == "") {
			red = "0"
		}
		if (gray == "") {
			gray = "0"
		}
		$("#blue").html(blue);
		$("#yellow").html(yellow);
		$("#orange").html(orange);
		$("#red").html(red);
		$("#gray").html(gray);
	}

	


</script>
<script id="testTemplate1" type="text/x-kendo-template">
	# if (sr_power == '0' ) { # <span class='pl-2'><img src='/resources/shared/images/logo/off.png'></span> # } 
	else if (sr_power == '1') { # <span class='pl-2'><img src='/resources/shared/images/logo/on.png'></span> #}
	else { # <span>-</span># } #
</script>



<script>
//테이블 클릭이벤트 해당 위치로 티맵 지도이
function grid_change(e) {
	var grid = $("#grid").data("kendoGrid");
	key = Object.keys(grid._selectedIds);
	id = key[0];
	data = {
			"id" : id
	}
	// selectedDataItems contains all selected data items
	
	$.ajax({
		type : "POST",
		contentType : "application/json",
		dataType : "json",
		url : "${readIdUrl}",
		data : JSON.stringify(data),
		success : function(data) {
			//지도이동
			editTmap(data["lat"],data["lon"]);
		},
		error : ajaxSaveError
	});
	

}




</script>


<!--  / Scripts -->

<script type="text/javascript">

var map, marker1;
var lineArr = [];
function editTmap(lon,lat){
	//기존 마커 삭제
	marker1.setMap(null);
	// 2. 요청 좌표 마커 찍기
	if (lon == "" || lat == "") {
		showAlertModal("danger", "로컬제어기에 등록된 좌표가 없습니다.");
	}
	
	var markerPosition = new Tmapv2.LatLng(lon, lat);
	//마커 올리기
 	marker1 = new Tmapv2.Marker({
 		position : markerPosition,
 		icon : "http://tmapapi.sktelecom.com/upload/tmap/marker/pin_b_m_a.png",
		iconSize : new Tmapv2.Size(24, 38),
		map:map
 	});
 	map.setZoom(3);
	map.setCenter(markerPosition);
	
}
function initTmap(lon,lat){
	
 	// 1. 지도 띄우기
	map = new Tmapv2.Map("map_div", {
		center: new Tmapv2.LatLng(37.570028, 126.986072),
		width : "100%",
		height : "600px",
		zoom : 15,
		zoomControl : true,
		scrollwheel : true
		
	});
	// 마커 초기화
 	marker1 = new Tmapv2.Marker({
 		icon : "http://tmapapi.sktelecom.com/upload/tmap/marker/pin_b_m_a.png",
		iconSize : new Tmapv2.Size(24, 38),
		map:map
 	});
	
	map.addListener("mouseup", function onMouseUp(evt){
		var centerLonLat = map.getCenter();
		var centerlon = centerLonLat._lng.toFixed(6);
		var centerlat = centerLonLat._lat.toFixed(6);
		
		//지도가 이동이 끝날때마다 지도의 중심좌표를 경도 위도 텍스트 란에 값을 넣어줍니다. 
		$('#longitude').val(centerlon);//중심좌표의 경도입니다.
		$('#latitude').val(centerlat);//중심좌표의 위도입니다.
		
		$("#lon2").text(centerlon);
		$("#lat2").text(centerlat);
		
		$(".over").show();
	});

	/*
	$("#btn_select").click(function(){
		$(".over").hide();
		

		// 2. 요청 좌표 마커 찍기
		//기존 마커 삭제
		marker1.setMap(null);
		
		var markerPosition = new Tmapv2.LatLng(lat, lon);
		//마커 올리기
	 	marker1 = new Tmapv2.Marker({
	 		position : markerPosition,
	 		icon : "http://tmapapi.sktelecom.com/upload/tmap/marker/pin_b_m_a.png",
			iconSize : new Tmapv2.Size(24, 38),
			map:map
	 	});
		map.setCenter(markerPosition);
		//map.setZoom(16);
		
		// 3. API 사용요청
		$.ajax({
			method:"GET",
			url:"https://apis.openapi.sk.com/tmap/road/nearToRoad?version=1",//가까운 도로 찾기 api 요청 url입니다.
			async:false,
			data:{
				"appKey" : "l7xx13f33846187e4dd7bbb1a4df23dbdefd",
				"lon" : lon,
				"lat" : lat
			},
			success:function(response){
				
				var resultHeader, resultlinkPoints;
				
				if(response.resultData.header){
					resultHeader = response.resultData.header;
					resultlinkPoints = response.resultData.linkPoints;
					
					var tDistance = "총 거리 : " + resultHeader.totalDistance + "m,";
					var tTime = " 제한 속도 : " + resultHeader.speed +"km/H,";	
					var rName = " 도로명 : " + resultHeader.roadName +", ";
					var linkId = " linkId : " + resultHeader.linkId + resultHeader.idxName +", ";
					var laneType = " laneType : " + resultHeader.laneType +", ";
					var lane = " lane : " + resultHeader.lane +", ";
					var tlinkId = " tlinkId : " + resultHeader.tlinkId;
					
					$("#result").text(tDistance+tTime+rName+linkId+laneType+lane+tlinkId);
					
					// 기존 라인 지우기
					if(lineArr.length > 0){
						for(var k=0; k<lineArr.length ; k++){
							lineArr[k].setMap(null);
						}
						//지운뒤 배열 초기화
						lineArr = [];
					}
					
					var drawArr = [];
					
					// Tmapv2.LatLng객체로 이루어진 배열을 만듭니다.
					for(var i in resultlinkPoints){
						var lineLatLng = new Tmapv2.LatLng(resultlinkPoints[i].location.latitude, resultlinkPoints[i].location.longitude);
						
						drawArr.push(lineLatLng);
					}
					
					//그리기
					var polyline_ = new Tmapv2.Polyline({
							path : drawArr,	//만든 배열을 넣습니다.
							strokeColor : "#FF0000",
							strokeWeight: 6,
							map : map
					});
					
					//라인 정보를 배열에 담습니다.
					lineArr.push(polyline_);
					
				}else{
					$("#result").text("가까운 도로 검색 결과가 없습니다.");
				}
			},
			error:function(request,status,error){
				console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		});
		 
	});
	*/
} 
</script>


<!-- / Page body -->





<!-- Functional tags -->

<func:cmmImgLightBox />
<func:cmmValidate />


<!-- Closing tags -->

<common:base />
<common:pageClosing />
