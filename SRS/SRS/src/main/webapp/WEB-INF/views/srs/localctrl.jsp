<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>

<!-- URL -->

<c:url value="/srs/localctrl/read" var="readUrl" />
<c:url value="/srs/localctrl/destroy" var="destroyUrl" />
<c:url value="/srs/localctrl/update" var="updateUrl" />
<c:url value="/srs/localctrl/create" var="createUrl" />
<c:url value="/srs/localctrl/readTypes" var="readTypeUrl" />
<c:url value="/srs/localctrl/spLimitUpdate" var="spLimitUpdateUrl" />
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/proj4js/1.1.0/proj4js-combined.min.js"></script>

<!-- Opening tags -->

<common:pageOpening />


<!-- Page title -->

<h4 class="pt-1 pb-3 mb-3">
	<span class="mr-1 fa fa-cog"></span> ${pageTitle}
</h4>

<hr class="border-light container-m--x mt-0 mb-4">

<!-- Page body -->


<!-- Java(optional)  -->

<%
String regDateTemplate = kr.co.bbmc.utils.Util.getSmartDate();
%>
<%
String editTemplate = "<button type='button' onclick='edit(#= id #)' class='btn icon-btn btn-sm btn-outline-success borderless'>"
		+ "<span class='fas fa-pencil-alt'></span></button>";
%>
<!-- Kendo grid  -->

<div class="mb-4">
	<kendo:grid name="grid" pageable="true" groupable="false"
		filterable="true" sortable="true" scrollable="false"
		reorderable="true" resizable="true"
		selectable="${value_gridSelectable}">
		<kendo:grid-excel fileName="${pageTitle}.xlsx" allPages="true"
			proxyURL="/proxySave" />
		<kendo:grid-pageable refresh="true" buttonCount="5" pageSize="10"
			pageSizes="${pageSizesNormal}" />
		<kendo:grid-toolbarTemplate>
			<div class="d-flex align-items-center">
				<span class="mr-1">
					<button id="add-btn" type="button" class="btn btn-outline-success">${cmd_add}</button>
				</span> <span class="d-none d-lg-inline">
					<button type="button" class="btn btn-default k-grid-excel">${cmd_excel}</button>
				</span> <span class="ml-auto">
					<button id="delete-btn" type="button" class="btn btn-danger">${cmd_delete}</button>
				</span>
			</div>
		</kendo:grid-toolbarTemplate>
		<kendo:grid-columns>
			<kendo:grid-column title="${cmd_edit}" width="50" filterable="false"
				sortable="false" template="<%= editTemplate %>" />
			<kendo:grid-column title="로컬제어기" field="lc_name" minScreenWidth="600" />
			<kendo:grid-column title="기기ID" field="id" />
			<kendo:grid-column title="주소" field="lc_addr1" />
			<kendo:grid-column title="유효시작일" field="lc_valid_start" />
			<kendo:grid-column title="유효만료일" field="lc_valid_end" />
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
</style>


<!-- / Kendo grid  -->



<!-- services와 clusterer, drawing 라이브러리 불러오기 -->
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=53df131855c9cf1dcab42c45961837e2&libraries=services"></script>
	
<script>
$(document).ready(function() {

	// Add
	$("#add-btn").click(function(e) {
		e.preventDefault();
		
		initForm1("추가");

		
		$('#form-modal-1 .modal-dialog').draggable({ handle: '.modal-header' });
		$("#form-modal-1").modal();
	});
	// / Add

	// Delete
	$("#delete-btn").click(function(e) {
		e.preventDefault();
			
		var grid = $("#grid").data("kendoGrid");
		var rows = grid.select();
	
		var delRows = [];
		var lcNames = [];
		var lcMacs = [];
		
		rows.each(function(index, row) {
			var selectedItem = grid.dataItem(row);
			delRows.push(selectedItem.id);
			lcNames.push(selectedItem.lc_name);
			lcMacs.push(selectedItem.lc_mac);
			
			
		});
			
		if (delRows.length > 0) {
			showDelConfirmModal(function(result) {
				if (result) {
					$.ajax({
						type: "POST",
						contentType: "application/json",
						dataType: "json",
						url: "${destroyUrl}",
						data: JSON.stringify({ items: delRows, lcName:lcNames, lcMac:lcMacs }),
						success: function (form) {
        					showDeleteSuccessMsg();
							grid.dataSource.read();
						},
						error: ajaxDeleteError
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
<div id="formRootMap"></div>


<!--  Forms -->

<!-- 추가 폼 -->
<script id="template-1" type="text/x-kendo-template">

<div class="modal fade" data-backdrop="static" id="form-modal-1">
	<div class="modal-dialog">
		<form class="modal-content" id="form-1" rowid="-1" url="${createUrl}">
      
			<!-- Modal Header -->
			<div class="modal-header move-cursor">
				<h5 class="modal-title">
					로컬제어기
					<span class="font-weight-light pl-1"><span name="subtitle"></span>
				</h5>
				<button type="button" class="close" data-dismiss="modal">×</button>
			</div>
        
			<!-- Modal body -->
			<div class="modal-body modal-bg-color py-3">
				<div id="title-container"></div>
				<div class="form-row">
					<div class="col-10 col-sm-10">
						<div class="form-group col">
							<label class="form-label">
								제어기 명
								<span class="text-danger">*</span>
							</label>
							<input name="lc_name" type="text" maxlength="30" class="form-control required">
						</div>
					</div>
				</div>
				
				<div class="nav-tabs-top mt-1 px-2">
					<ul class="nav nav-tabs px-1">
						<li class="nav-item">
							<a class="nav-link active" data-toggle="tab" id="lo-basic" href="\\\#lo-basic-ctnt">
								<span class="fas fa-check"></span><span class="pl-1">기본</span>
							</a>
						</li>
						<li class="nav-item">
							<a class="nav-link" data-toggle="tab" id="lo-option" href="\\\#lo-option-ctnt">
								<span class="fas fa-toggle-on"></span><span class="pl-1">옵션</span>
							</a>
						</li>
					</ul>
					<div class="tab-content mx-1">
						<div class="tab-pane p-2 active" id="lo-basic-ctnt">
							<div class="form-row mt-3">
								<div class="col-6">
									<div class="form-group col">
										<label class="form-label">
											기기 IP
										</label>
										<input name="lc_ip" type="text" maxlength="20" class="form-control">
									</div>
								</div>
								<div class="col-6">
									<div class="form-group col">
										<label class="form-label">
											MAC Address
											<span class="text-danger">*</span>
										</label>
										<input name="lc_mac" type="text" maxlength="20" class="form-control required">
									</div>
								</div>
							</div>
							<div class="form-row">
								<div class="col-6">
									<div class="form-group col">
										<label class="form-label">
											유효시작일
										</label>
										<input name="lc_valid_start" type="date" class="form-control">
									</div>
								</div>
								<div class="col-6">
									<div class="form-group col">
										<label class="form-label">
											유효만료일
										</label>
										<input name="lc_valid_end" type="date" class="form-control">
									</div>
								</div>
							</div>
						</div>
						<div class="tab-pane p-2 fade" id="lo-option-ctnt">
							<div class="form-row">
								<div class="col-6">
									<div class="form-group col">
										<label class="form-label">
											지역
										</label>
										<input name="lc_area1" type="text" maxlength="50" class="form-control">
									</div>
								</div>
								<div class="col-6">
									<div class="form-group col">
										<label class="form-label">
											주소
										</label>
										<input name="lc_addr1" type="text" maxlength="50" class="form-control">
									</div>
								</div>
							</div>
							<div class="form-row">
								<div class="col-6">
									<div class="form-group col">
										<label class="form-label">
											지도 위도
										</label>
										<input name="lc_gps_lat" type="text" maxlength="50" class="form-control">
									</div>
								</div>
								<div class="col-6">
									<div class="form-group col">
										<label class="form-label">
											지도 경도
										</label>
										<input name="lc_gps_long" type="text" maxlength="50" class="form-control">
									</div>
								</div>
							</div>
							<div class="form-row">
								<div class="col-6">
									<div class="form-group col">
										<label class="form-label">
											관리 연락처
										</label>
										<input name="lc_manager_tel" type="text" maxlength="50" class="form-control">
									</div>
								</div>
								<div class="col-6">
									<div class="form-group col">
										<label class="form-label">
											메모
										</label>
										<input name="lc_memo" type="text" maxlength="100" class="form-control">
									</div>
								</div>
							</div>
							<div class="form-row">
								<div class="col-12">
									<div class="form-group col">
										<label class="form-label">
											도로명
										</label>
										<input name="lc_road_name" type="text" maxlength="20"  class="form-control">
									</div>
								</div>
							</div>
							<div class="form-row">
								<div class="col-6">
									<div class="form-group col">
										<label class="form-label">
											제한속도
										</label>
										<input name="lc_sp_limit" type="text" maxlength="20" class="form-control">
									</div>
								</div>
								<div class="col-6">
									<div class="form-group col">
										<label class="form-label">
											총거리
										</label>
										<input name="lc_total_distance" type="text" maxlength="20" class="form-control">
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
        
			<!-- Modal footer -->
			<div class="modal-footer d-flex">
				<button type="button" onclick="getSpLimit()" class="btn btn-default" >제한속도</button>
				<button type="button" onclick="showMap()" class="btn btn-default" >지도</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">${form_cancel}</button>
				<button type="button" class="btn btn-primary" onclick='saveForm()'>${form_save}</button>
			</div>	
		</form>
	</div>
</div>



</script>
<!-- 지도 검색폼 -->
<script id="template-2" type="text/x-kendo-template">

<div class="modal fade" data-backdrop="static" id="form-modal-2">
	<div class="modal-dialog modal-lg">      
		<div id="form-2" class="modal-content">        
			<div class="map_wrap">
    			<div id="local_map" style="width:100%;height:100%;"></div>
    				<div id="menu_wrap" class="bg_white">
        				<div class="option">
            				<div>
                				<form onsubmit="makeMap(); return false;">
                   					검색 : <input type="text" name="keyword" value="" id="keyword" size="15"> 
                    				<button type="submit">검색하기</button> 
               					</form>
            				</div>
        				</div>
        				<ul id="placesList"></ul>
        				<div id="pagination"></div>
    				</div>
				</div>        
			<!-- Modal footer -->
			<div class="modal-footer">
				<p id="result"></p>
				<button type="button" class="btn btn-default" data-dismiss="modal">${form_cancel}</button>
				<button type="button" class="btn btn-primary" data-dismiss="modal">${form_save}</button>
			</div>
		</div>
	</div>
</div>


</script>

<!--  Scripts -->

<script>
//추가 변경 폼 띄우기
function initForm1(subtitle) {
	$("#formRoot").html(kendo.template($("#template-1").html()));
	if (subtitle == "변경") {
 		$("#form-1 input[name='lc_mac']").attr("readonly",true);
 	}
	$("#form-1 span[name='subtitle']").text(subtitle ? subtitle : "${form_add}");
	// set validation
	$("#form-1").validate();
}
//지도 폼 띄우기
function initForm2(latlon) {
	$("#formRootMap").html(kendo.template($("#template-2").html()));
	if($("#form-2 input[name='keyword']").val() != ""){
		$("#form-2 input[name='keyword']").val("");	
	}
	keyword = $("#form-1 input[name='lc_name']").val();
	$("#form-2 input[name='keyword']").val(keyword);
	setTimeout("makeMap(latlon);", 500);
}

//추가 변경 저장 이벤트
function saveForm() {	
	if ($("#form-1").valid()) {	
		var data = {
	    	id: Number($("#form-1").attr("rowid")),
    		lc_name: $.trim($("#form-1 input[name='lc_name']").val()),
    		lc_ip: $.trim($("#form-1 input[name='lc_ip']").val()),	
			lc_valid_start: $.trim($("#form-1 input[name='lc_valid_start']").val()),
			lc_valid_end: $.trim($("#form-1 input[name='lc_valid_end']").val()),
			lc_area1: $.trim($("#form-1 input[name='lc_area1']").val()),
			lc_addr1: $.trim($("#form-1 input[name='lc_addr1']").val()),
	   		lc_mac: $.trim($("#form-1 input[name='lc_mac']").val()),
			lc_gps_lat: $.trim($("#form-1 input[name='lc_gps_lat']").val()),
			lc_gps_long: $.trim($("#form-1 input[name='lc_gps_long']").val()),
			lc_manager_tel: $.trim($("#form-1 input[name='lc_manager_tel']").val()),
			lc_memo: $.trim($("#form-1 input[name='lc_memo']").val()),
			lc_road_name: $.trim($("#form-1 input[name='lc_road_name']").val()),
			lc_sp_limit: $.trim($("#form-1 input[name='lc_sp_limit']").val()),
			lc_total_distance: $.trim($("#form-1 input[name='lc_total_distance']").val())
		};
		
    	$.ajax({
    		type: "POST",
    		contentType: "application/json",
    		dataType: "json",
    		url: $("#form-1").attr("url"),
    		data: JSON.stringify(data),
    		success: function (data,status) {
				showSaveSuccessMsg();
				$("#form-modal-1").modal("hide");
				$("#grid").data("kendoGrid").dataSource.read();
    		},
			error: ajaxSaveError
    	});
	}
}
//지도 버튼 클릭이벤트
function showMap() {
	latlon = "n"
	if (typeof dataItem != 'undefined' && $("#form-1 span[name='subtitle']").text() == "변경"){
		if(dataItem.lc_gps_lat != null && dataItem.lc_gps_long){
			latlon = "y"
		}
		else{
			latlon = "n"	
		}		
	}
	initForm2(latlon);
	$('#form-modal-2 .modal-dialog').draggable({ handle: '.modal-header' });
	$("#form-modal-2").modal();
}
//수정 버튼 클릭이벤트
function edit(id) {
	initForm1("${form_edit}");
	dataItem = $("#grid").data("kendoGrid").dataSource.get(id);
	$("#form-1").attr("rowid", dataItem.id);
	$("#form-1").attr("url", "${updateUrl}");
	$("#form-1 input[name='lc_name']").val(dataItem.lc_name);
	$("#form-1 input[name='lc_ip']").val(dataItem.lc_ip);
	$("#form-1 input[name='lc_mac']").val(dataItem.lc_mac);
	$("#form-1 input[name='lc_valid_start']").val(dataItem.lc_valid_start);
	$("#form-1 input[name='lc_valid_end']").val(dataItem.lc_valid_end);
	$("#form-1 input[name='lc_area1']").val(dataItem.lc_area1);
	$("#form-1 input[name='lc_addr1']").val(dataItem.lc_addr1);
	$("#form-1 input[name='lc_gps_lat']").val(dataItem.lc_gps_lat);
	$("#form-1 input[name='lc_gps_long']").val(dataItem.lc_gps_long);
	$("#form-1 input[name='lc_manager_tel']").val(dataItem.lc_manager_tel);
	$("#form-1 input[name='lc_memo']").val(dataItem.lc_memo);
	$("#form-1 input[name='lc_road_name']").val(dataItem.lc_road_name);
	$("#form-1 input[name='lc_sp_limit']").val(dataItem.lc_sp_limit);
	$("#form-1 input[name='lc_total_distance']").val(dataItem.lc_total_distance);		
	$('#form-modal-1 .modal-dialog').draggable({ handle: '.modal-header' });
	$("#form-modal-1").modal();
}
//속도제한 불러오기 이벤트
function getSpLimit(){
		lc_mac = $.trim($("#form-1 input[name='lc_mac']").val()),
		lat = $.trim($("#form-1 input[name='lc_gps_lat']").val())
		lon = $.trim($("#form-1 input[name='lc_gps_long']").val())
		if(lat.indexOf(".") == -1 || lat.indexOf(".") == -1){
			showAlertModal("danger", "올바른 위도, 경도를 입력해 주세요.");			
		}
		else if(lat == "" || lat == ""){
			showAlertModal("danger", "위도, 경도를 입력해 주세요.");	
		}
		else{
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
					if(response==undefined){
						showAlertModal("danger", "올바른 위도, 경도를 입력해 주세요.");	
					}
					else{
						var resultHeader, resultlinkPoints;
						
						if(response.resultData.header){
							resultHeader = response.resultData.header;
							resultlinkPoints = response.resultData.linkPoints;
							
							var tDistance = resultHeader.totalDistance;
							var tTime = resultHeader.speed;	
							var rName = resultHeader.roadName;
							$("#form-1 input[name='lc_road_name']").val(rName);
							$("#form-1 input[name='lc_sp_limit']").val(tTime);
							$("#form-1 input[name='lc_total_distance']").val(tDistance);
							
						}else{
							$("#result").text("가까운 도로 검색 결과가 없습니다.");
						}						
					}

				},
				error:function(request,status,error){
					console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
				}
			});	
		}
		
}

</script>
<!--  / 카카오 맵스크립 -->
<script>
function relayout(map) {    
    // 지도를 표시하는 div 크기를 변경한 이후 지도가 정상적으로 표출되지 않을 수도 있습니다
    // 크기를 변경한 이후에는 반드시  map.relayout 함수를 호출해야 합니다 
    // window의 resize 이벤트에 의한 크기변경은 map.relayout 함수가 자동으로 호출됩니다
    map.relayout();
}
//주소 불러오기
function getAddr(marker){
	// 이동할 위도 경도 위치를 생성합니다
    var lng = marker.getPosition()["La"]
    var lat = marker.getPosition()["Ma"]

    var moveLatLon = new kakao.maps.LatLng(lat, lng);
    
    // 지도 중심을 부드럽게 이동시킵니다
    // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
    map.setLevel(4);
    map.panTo(moveLatLon);           
    var message = '클릭한 위치의 위도는 ' + lat + ' 이고, ';
    message += '경도는 ' + lng + ' 입니다';
    var resultDiv = document.getElementById('result'); 
    resultDiv.innerHTML = message;
    let geocoder = new kakao.maps.services.Geocoder();

    let coord = new kakao.maps.LatLng(lat, lng);
    let callback = function(result, status) {
        if (status === kakao.maps.services.Status.OK) {
        	var addr = result[0].address.address_name;
        	var area = result[0].address.region_1depth_name;
        	$("#form-1 input[name='lc_gps_long']").val(lng);
        	$("#form-1 input[name='lc_gps_lat']").val(lat);
        	$("#form-1 input[name='lc_addr1']").val(addr);
        	$("#form-1 input[name='lc_area1']").val(area);
        }
    };

    geocoder.coord2Address(coord.getLng(), coord.getLat(), callback);
}
//맵 생성
function makeMap(latlon_check){
// 마커를 담을 배열입니다
var markers = [];
keyword = $("#form-2 input[name='keyword']").val();

var mapContainer = document.getElementById('local_map'), // 지도를 표시할 div 
    mapOption = {
        center: new kakao.maps.LatLng(37.566826, 126.9786567), // 지도의 중심좌표
        level: 5 // 지도의 확대 레벨
    };  
// 지도를 생성합니다    
map = new kakao.maps.Map(mapContainer, mapOption); 
// 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다
var infowindow = new kakao.maps.InfoWindow({zIndex:1});
    if(latlon_check == "y"){
    	keyword = dataItem.lc_gps_lat + " " + dataItem.lc_gps_long;
    	$("#form-2 input[name='keyword']").val(keyword);
    }
    else if(latlon_check == "n"){
    	keyword = document.getElementById('keyword').value;	
    }
    if (keyword.includes(".")){
    	removeMarker();
    	keyword = keyword.split(" ")
    	var lat = keyword[0];
        var lng = keyword[1];
        position = {latlng: new kakao.maps.LatLng(lat,lng)}

        var marker = new kakao.maps.Marker({ 
            // 지도 중심좌표에 마커를 생성합니다 
            map: map,
            position: position["latlng"]
        });
        map.setCenter(new kakao.maps.LatLng(lat, lng));
        marker.setMap(map);
        // 마커가 드래그 가능하도록 설정합니다 
        marker.setDraggable(true); 
        //드래그 끝났을 때 액
        kakao.maps.event.addListener(marker, 'dragend', function() {
        	getAddr(marker);
        });
    	getAddr(marker);
        /*
        var message = '클릭한 위치의 위도는 ' + lat + ' 이고, ';
        message += '경도는 ' + lng + ' 입니다';
        var resultDiv = document.getElementById('result'); 
        resultDiv.innerHTML = message;
        getAddr(lat,lng);
        */
    }
    else{
    	searchPlaces();    	
    }


// 키워드 검색을 요청하는 함수입니다
function searchPlaces() {
	// 장소 검색 객체를 생성합니다
	var ps = new kakao.maps.services.Places();  
    var keyword = document.getElementById('keyword').value;

    if (!keyword.replace(/^\s+|\s+$/g, '')) {
        alert('키워드를 입력해주세요!');
        return false;
    }

    // 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
    ps.keywordSearch( keyword, placesSearchCB); 
}

// 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
function placesSearchCB(data, status, pagination) {
    if (status === kakao.maps.services.Status.OK) {

        // 정상적으로 검색이 완료됐으면
        // 검색 목록과 마커를 표출합니다
        displayPlaces(data);

        // 페이지 번호를 표출합니다
        displayPagination(pagination);

    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {

        alert('검색 결과가 존재하지 않습니다.');
        return;

    } else if (status === kakao.maps.services.Status.ERROR) {

        alert('검색 결과 중 오류가 발생했습니다.');
        return;

    }
}

// 검색 결과 목록과 마커를 표출하는 함수입니다
function displayPlaces(places) {

    var listEl = document.getElementById('placesList'), 
    menuEl = document.getElementById('menu_wrap'),
    fragment = document.createDocumentFragment(), 
    bounds = new kakao.maps.LatLngBounds(), 
    listStr = '';
    
    // 검색 결과 목록에 추가된 항목들을 제거합니다
    removeAllChildNods(listEl);

    // 지도에 표시되고 있는 마커를 제거합니다
    removeMarker();
    
    for ( var i=0; i<places.length; i++ ) {

        // 마커를 생성하고 지도에 표시합니다
        var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
            marker = addMarker(placePosition, i), 
            itemEl = getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다


        // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
        // LatLngBounds 객체에 좌표를 추가합니다
        bounds.extend(placePosition);

        // 마커와 검색결과 항목에 mouseover 했을때
        // 해당 장소에 인포윈도우에 장소명을 표시합니다
        // mouseout 했을 때는 인포윈도우를 닫습니다
        (function(marker, title) {
            // 마커가 드래그 가능하도록 설정합니다 
            marker.setDraggable(true); 
            //드래그 끝났을 때 액
            kakao.maps.event.addListener(marker, 'dragend', function() {
            	getAddr(marker);
  
            });
        	
        	/*기존 클릭이벤트
            kakao.maps.event.addListener(marker, 'click', function() {
                panTo(marker,places);

            });
        	*/
            itemEl.onclick =  function () {
                panTo(marker,places);
            }
        })(marker, places[i].place_name);

        fragment.appendChild(itemEl);
    }

    // 검색결과 항목들을 검색결과 목록 Elemnet에 추가합니다
    listEl.appendChild(fragment);
    menuEl.scrollTop = 0;

    // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
    map.setBounds(bounds);
    map.relayout();

}

// 검색결과 항목을 Element로 반환하는 함수입니다
function getListItem(index, places) {

    var el = document.createElement('li'),
    itemStr = '<span class="markerbg marker_' + (index+1) + '"></span>' +
                '<div class="info">' +
                '   <h5>' + places.place_name + '</h5>';

    if (places.road_address_name) {
        itemStr += '    <span>' + places.road_address_name + '</span>' +
                    '   <span class="jibun gray">' +  places.address_name  + '</span>';
    } else {
        itemStr += '    <span>' +  places.address_name  + '</span>'; 
    }
                 
      itemStr += '  <span class="tel">' + places.phone  + '</span>' +
                '</div>';           

    el.innerHTML = itemStr;
    el.className = 'item';

    return el;
}

// 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
function addMarker(position, idx, title) {
    var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
        imageSize = new kakao.maps.Size(36, 37),  // 마커 이미지의 크기
        imgOptions =  {
            spriteSize : new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
            spriteOrigin : new kakao.maps.Point(0, (idx*46)+10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
            offset: new kakao.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
        },
        markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
            marker = new kakao.maps.Marker({
            position: position, // 마커의 위치
            image: markerImage 
        });

    marker.setMap(map); // 지도 위에 마커를 표출합니다
    markers.push(marker);  // 배열에 생성된 마커를 추가합니다

    return marker;
}

// 지도 위에 표시되고 있는 마커를 모두 제거합니다
function removeMarker() {
    for ( var i = 0; i < markers.length; i++ ) {
        markers[i].setMap(null);
    }   
    markers = [];
}

// 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
function displayPagination(pagination) {
    var paginationEl = document.getElementById('pagination'),
        fragment = document.createDocumentFragment(),
        i; 

    // 기존에 추가된 페이지번호를 삭제합니다
    while (paginationEl.hasChildNodes()) {
        paginationEl.removeChild (paginationEl.lastChild);
    }

    for (i=1; i<=pagination.last; i++) {
        var el = document.createElement('a');
        el.href = "#";
        el.innerHTML = i;

        if (i===pagination.current) {
            el.className = 'on';
        } else {
            el.onclick = (function(i) {
                return function() {
                    pagination.gotoPage(i);
                }
            })(i);
        }

        fragment.appendChild(el);
    }
    paginationEl.appendChild(fragment);
}

// 검색결과 목록 또는 마커를 클릭했을 때 호출되는 함수입니다
// 인포윈도우에 장소명을 표시합니다
function displayInfowindow(marker, title) {
    var content = '<div style="padding:5px;z-index:1;">' + title + '</div>';

    infowindow.setContent(content);
    infowindow.open(map, marker);
}
//검색목록 클릭시 이동 및 메세지 생성
function panTo(marker,places) {
    // 이동할 위도 경도 위치를 생성합니다
    var lng = marker.getPosition()["La"]
    var lat = marker.getPosition()["Ma"]

    var moveLatLon = new kakao.maps.LatLng(lat, lng);
    
    // 지도 중심을 부드럽게 이동시킵니다
    // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
    map.setLevel(4);
    map.panTo(moveLatLon);           
    var message = '클릭한 위치의 위도는 ' + lat + ' 이고, ';
    message += '경도는 ' + lng + ' 입니다';
    var resultDiv = document.getElementById('result'); 
    resultDiv.innerHTML = message;    
    var addr_full = "";
    var addr_area = "";
    j = 0
    for (j;j<places.length;j++){
    	if (String(lng).substring(0,10) == places[j].x.substring(0,10)){
    		addr_full = places[j].address_name;
    	}
    }
    var a = [];
    a = addr_full.split(" ");
    addr_area = a[0];
	$("#form-1 input[name='lc_gps_long']").val(lng);
	$("#form-1 input[name='lc_gps_lat']").val(lat);
	$("#form-1 input[name='lc_addr1']").val(addr_full);
	$("#form-1 input[name='lc_area1']").val(addr_area);
	

}





 // 검색결과 목록의 자식 Element를 제거하는 함수입니다
function removeAllChildNods(el) {   
    while (el.hasChildNodes()) {
        el.removeChild (el.lastChild);
    }
}
 
}
 

</script>

<!--  / Scripts -->


<!-- / Page body -->





<!-- Functional tags -->

<func:cmmValidate />


<!-- Closing tags -->

<common:base />
<common:pageClosing />

