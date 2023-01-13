<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/srs/localctrl/read" var="readUrl" />
<c:url value="/srs/roadinfo/selectLc" var="selectLcUrl" />
<c:url value="/srs/localctrl/countstate" var="cntStateUrl" />
<c:url value="/srs/gridview/readSr" var="readSrUrl" />
<c:url value="/srs/localctrl/readId" var="readIdUrl" />
<c:url value="/srs/localctrl/readTypes" var="readTypeUrl" />
<c:url value="/srs/gridview/update" var="updateUrl" />
<c:url value="/srs/gridview/stateTime" var="stateTimeUrl" />
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/proj4js/1.1.0/proj4js-combined.min.js"></script>

<!-- Opening tags -->

<common:pageOpening />


<!-- Page title -->

<h4 class="pt-1 pb-3 mb-3">
	<i class="fas fa-exclamation-triangle mr-2"></i> ${pageTitle}
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
<div class="row" >
	<div class="col-6 mt-3">
		<div class=row style ="background-color: #ffffff;    margin-inline: auto;padding:1%;border-top:1px solid #E6E6E6;border-left:1px solid #E6E6E6;border-right:1px solid #E6E6E6">
			<div class="col-2">
				<button type="button" class="btn btn-default k-grid-excel mr-6">${cmd_excel}</button>
			</div>
			<div class="col-2">
				<span style="display: flex;align-items: center;justify-content: end;padding-top: 10;">
    			로컬제어기</span>
			</div>
			<div class="col-4">
				<select id="select_lc" name="select_lc" onchange='get_road_info()' class="form-control">
				</select>
			</div>
			<div class="col-4">
				<span name="today" style="display:flex;margin-top:10"></span>
			</div>
		</div>
		<div id="grid"></div>
	</div>

	<div class="col-6">
		<div id="map_div" style="width: 100%; height: 595px;margin-top:18px"></div>
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
.k-grid-content.k-auto-scrollable{
    max-height: 550px;
}
.info-s {
	line-height: 1.5rem;
	width: 325px;
	height: 80px;
}
.info-m {
	line-height: 1.5rem;
	width: 325px;
	height: 150px;
}
.info-l {
	line-height: 1.5rem;
	width: 325px;
	height: 180px;
}
.info-xl {
	line-height: 1.5rem;
	width: 325px;
	height: 210px;
}

.info-content {
	font-size: 14px;
	
}

.info-title {
	display: block;
	background: #DADADA;
	color: #fff;
	text-align: left;
	height: 25px;
	line-height: 10px;
	border-radius: 4px;
	padding: 0px 10px; 
	padding-top: 1%;
	font-size: 17px;
}
</style>


<!-- kakao map actions  -->
<script type="text/javascript"
	src="//dapi.kakao.com/v2/maps/sdk.js?appkey=515f641b68a957d80bf598cc7321c251">
</script>

<!-- Grid button actions  -->

<script>
	$(document).ready(function() {
		//로컬제어기 정보 호출
		get_select_lc();
		//make_grid();
		//최초 로컬 기기 상태 불러오기
		get_count_state();
		//10초주기로 로컬 기기 상태 불러오기
		setInterval(get_count_state, 10000);

	});
</script>

<!-- / Grid button actions  -->




<!--  Scripts -->

<script>
//테이블의 현장정보 클릭시 해당 현장정보로 지도이동
function grid_change(e) {
	
	var grid = $("#grid").data("kendoGrid");
		key = Object.keys(grid._selectedIds);
		uid = key[0];
		data_list= grid._data;
		lat = "";
		lon = "";
		for(i=0;i<data_list.length;i++){
			if (grid._data[i].uid == uid){
				lon = grid._data[i].coordX;
				lat = grid._data[i].coordY;
			}
		}
		map.setCenter(new kakao.maps.LatLng(lat, lon));
		map.setLevel(2);
		// selectedDataItems contains all selected data items

}
//최초 로컬제어기 정보 호출
function get_select_lc(){
	var data = {id : "111"};
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		data: JSON.stringify(data),
		url: "${selectLcUrl}",
		success: function (lc_data) {
			createSelect(lc_data);


		}
		});
}	//로컬제어기 정보로 셀렉트 생성
	function createSelect(lc_ctrl_op) {
		lc_ctrl_select = "<select id='select_lc' name='select_lc' class='form-control' onchange='get_road_info()'>";
		if(lc_ctrl_op != ""){
			for (i=0;i<lc_ctrl_op.length;i++){
				lc_ctrl_select += "<option class='form-control' value='"+lc_ctrl_op[i].lc_gps_lat + "-" + lc_ctrl_op[i].lc_gps_long  +"'>" +lc_ctrl_op[i].lc_name +"</option>";
			}
			lc_ctrl_select += "</select>";
		}
		today = new Date(+new Date() + 3240 * 10000).toISOString().split("T")[0];
		$("select[name='select_lc']").html(lc_ctrl_select);
		$("span[name='today']").html(today);

		$("#select_lc").trigger("change");
		// set validation

	}
//로컬제어기 셀렉트 될때 해당 로컬제어기의 현장정보 호출 
function get_road_info(){
	var langSelect = document.getElementById("select_lc");  
	// select element에서 선택된 option의 value가 저장된다.  
	var selectValue = langSelect.options[langSelect.selectedIndex].value;
	
	latlon= selectValue.split("-");
	if(latlon[0] == "" || latlon[1] == ""){
		showAlertModal("danger", "로컬제어기에 등록된 좌표가 없습니다.");
	}
	else{
	lat = latlon[0];
	lon = latlon[1];
	latN =  parseFloat(lat);
	lonN =  parseFloat(lon);
	latMax = latN + 0.1;
	latMin = latN - 0.1;
	lonMax = lonN + 0.1;
	lonMin = lonN - 0.1;
	//호출 url
	url = "https://openapi.its.go.kr:9443/eventInfo?apiKey=5e95b4996b864bc1bb82e68db14e66b9&type=all&eventType=all"
			+"&minX=" + lonMin + "&maxX=" + lonMax + "&minY=" + latMin + "&maxY="+ latMax +"&getType=json";
	$.ajax({
		type : "GET",
		contentType : "application/json",
		dataType : "json",
		url : url,
		success : function(data) {
			make_map(lat,lon,data);
			make_grid(data);
			var grid = $("#grid").data("kendoGrid");
			grid.bind("change", grid_change);
		},
		error :  function(){
			data = {
					body:{items:[{
						eventType: "불어오기 실패",
						message: "새로고침 해주세요.",
						coordY: "37.3757737206854",
					    coordX: "126.927918311855"
					}]
					}
			}
			make_map(lat,lon,data);
			make_grid(data);
		}
	});
	}
}
//호출된 현장정보로 맵생성
function make_map(lat,lon,data){
	var container = document.getElementById('map_div');
	var options = {
			center : new kakao.maps.LatLng(lat, lon),
			level : 7
		};
	map = new kakao.maps.Map(container, options);
	// 마커가 표시될 위치입니다 
	var markerPosition  = new kakao.maps.LatLng(lat, lon); 

	// 마커를 생성합니다
	var marker = new kakao.maps.Marker({
	    position: markerPosition
	});
	marker.setMap(map);
	for (i= 0; i<data.body.items.length;i++){
		var position = new kakao.maps.LatLng(data.body.items[i].coordY,data.body.items[i].coordX);
		addMarker(position, i,map,data.body.items[i]);	
		// 마커에 표시할 인포윈도우를 생성합니다

	}
}
// 인포윈도우를 표시하는 클로저를 만드는 함수입니다 
function clickListener(map, marker, infowindow) {
	return function() {
		infowindow.open(map, marker);
	};
}
function addMarker(position, idx,map,info) {
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
	iwRemoveable = true;
	//메세지 사이즈 별 윈도우 크기 조절
	if(info.message.length <= 20){
		var infowindow = new kakao.maps.InfoWindow({
			content : "<div class='info-s'><div class='info-title'>"
					+ "<h5 style='display:flex;align-items:center'>" + info.roadName + "</h5></div>"
					+ "<div class='info-content'> "
					+ "<h7 class='pl-1'>  작업명 : " + info.eventType + "</h5>"
					+ "<p> <h7 class='pl-1'> 작업내용 : "
					+ info.message + "</h7></div></div>", // 인포윈도우에 표시할 내용
			removable : iwRemoveable
		});
	}
	else if(info.message.length >= 23 && info.message.length <= 69){
		var infowindow = new kakao.maps.InfoWindow({
			content : "<div class='info-m'><div class='info-title'>"
					+ "<h5 style='display:flex;align-items:center'>" + info.roadName + "</h5></div>"
					+ "<div class='info-content'> "
					+ "<h7 class='pl-1'>  작업명 : " + info.eventType + "</h5>"
					+ "<p> <h7 class='pl-1'> 작업내용 : "
					+ info.message + "</h7></div></div>", // 인포윈도우에 표시할 내용
			removable : iwRemoveable
		});
	}
	else if(info.message.length >= 70 && info.message.length <= 100){
		var infowindow = new kakao.maps.InfoWindow({
			content : "<div class='info-l'><div class='info-title'>"
					+ "<h5 style='display:flex;align-items:center'>" + info.roadName + "</h5></div>"
					+ "<div class='info-content'> "
					+ "<h7 class='pl-1'>  작업명 : " + info.eventType + "</h5>"
					+ "<p> <h7 class='pl-1'> 작업내용 : "
					+ info.message + "</h7></div></div>", // 인포윈도우에 표시할 내용
			removable : iwRemoveable
		});
	}
	else if(info.message.length >= 101){
		var infowindow = new kakao.maps.InfoWindow({
			content : "<div class='info-xl'><div class='info-title'>"
					+ "<h5 style='display:flex;align-items:center'>" + info.roadName + "</h5></div>"
					+ "<div class='info-content'> "
					+ "<h7 class='pl-1'>  작업명 : " + info.eventType + "</h5>"
					+ "<p> <h7 class='pl-1'> 작업내용 : "
					+ info.message + "</h7></div></div>", // 인포윈도우에 표시할 내용
			removable : iwRemoveable
		});
	}


	// 마커에 mouseover 이벤트와 mouseout 이벤트를 등록합니다
	// 이벤트 리스너로는 클로저를 만들어 등록합니다 
	// for문에서 클로저를 만들어 주지 않으면 마지막 마커에만 이벤트가 등록됩니다
	kakao.maps.event.addListener(marker, 'click', clickListener(map,
			marker, infowindow));
    marker.setMap(map); // 지도 위에 마커를 표출합니다
    //markers.push(marker);  // 배열에 생성된 마커를 추가합니다

    return marker;
}
//호출된 현장정보로 테이블 생성
function make_grid(data){
	for (i=0;i<data.body.items.length;i++){
		data.body.items[i]["no"] = i+1;
	}
    $("#grid").kendoGrid({
        dataSource: {
            data: data.body.items,
            schema: {
                model: {
                	id : "uid",
                    fields: {
                        위치: { type: "string" },
                    }
                }
            },
            pageSize: 10
        },
        height: 550,
        scrollable: true,
        selectable:"multiple, row",
        sortable: true,
        groupable:false,
        filterable: true,
        reorderable:true,
        resizable:true,
        pageable: {
            input: true,
            numeric: true,
            refresh: true,
            
        },
        columns: [
        	{field:"no",title:"No",width:"80px"},
           	 {field:"roadName", title:"위치", width: "200px"},
            { field: "eventType", title: "돌발유형", width: "200px" },
            { field: "message", title: "돌발내용", width: "200px" }
        ]
    });	
}
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
			},
		error: ajaxReadError
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





<!--  / Scripts -->


<!-- / Page body -->





<!-- Functional tags -->

<func:cmmImgLightBox />
<func:cmmValidate />


<!-- Closing tags -->

<common:base />
<common:pageClosing />
