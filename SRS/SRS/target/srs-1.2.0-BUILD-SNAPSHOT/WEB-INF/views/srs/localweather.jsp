<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/srs/localweather/read" var="readUrl" />
<c:url value="/srs/localweather/readWeather" var="readWeatherUrl" />
<c:url value="/srs/localctrl/countstate" var="cntStateUrl" />
<c:url value="/srs/gridview/stateTime" var="stateTimeUrl" />

<c:url value="/srs/gridview/readSr" var="readSrUrl" />
<c:url value="/srs/gridview/readSrGroup" var="readSrGroupUrl" />
<c:url value="/srs/recontrolauto/selectOpSr" var="selectOpSrUrl" />


<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/proj4js/1.1.0/proj4js-combined.min.js"></script>

<!-- Opening tags -->

<common:pageOpening />


<!-- Page title -->

<h4 class="pt-1 pb-3 mb-3">
	<i class="mr-1 fas fa-calendar-alt"> </i> ${pageTitle}
</h4>

<hr class="border-light container-m--x mt-0 mb-4">





<!-- Page body -->


<!-- Java(optional)  -->

<!-- Java(optional)  -->
<%
String lc_stateTemplate =
"# if (lc_state == '0' ) { #" + "<span class='pl-2'><img src='/resources/shared/images/logo/blue.png'></span>" +
"# } else if (lc_state == '1') { #" + "<span class='pl-2'><img src='/resources/shared/images/logo/yellow.png'></span>" +
"# } else if (lc_state == '2') { #" + "<span class='pl-2'><img src='/resources/shared/images/logo/orange.png'></span>" +
"# } else if (lc_state == '3') { #" + "<span class='pl-2'><img src='/resources/shared/images/logo/red.png'></span>" +
"# } else if (lc_state == '4') { #" + "<span class='pl-2'><img src='/resources/shared/images/logo/gray.png'></span>" +
"# } else { #" + "<span class='pl-2'><img src='/resources/shared/images/logo/gray.png'></span>" +
"# } #";

%>


<%
	String detailTemplate = 
	"<button type='button'  onclick='detail(#= id #)' class='btn icon-btn btn-sm btn-outline-success borderless'>" + 
	"<span class='fas fa-search'></span></button>";
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
			<div class="clearfix">
				<div class="float-left">
					<button type="button"
						class="btn btn-default d-none d-sm-inline k-grid-excel">${cmd_excel}</button>
				</div>
			</div>
		</kendo:grid-toolbarTemplate>
		<kendo:grid-columns>
			<kendo:grid-column title="상세보기" width="50" filterable="false"
				sortable="false" template="<%= detailTemplate %>" />
			<kendo:grid-column title="상태" field="lc_state"
				template="<%= lc_stateTemplate %>" />
			<kendo:grid-column title="로컬제어기" field="lc_name" />
			<kendo:grid-column title="지역" field="lc_area1" />
			<kendo:grid-column title="현재기온(℃)" field="lc_pu_temp" />
			<kendo:grid-column title="최고/최저" field="lc_pu_max_min" />
			<kendo:grid-column title="날씨" field="lc_pu_wCondition" />
			<kendo:grid-column title="습도(%)" field="lc_pu_humidity" />
			<kendo:grid-column title="풍향(°)" field="lc_pu_wDirection" />
			<kendo:grid-column title="풍속(m/s)" field="lc_pu_wSpeed" />
			<kendo:grid-column title="미세먼지(ug)" field="lc_pu_dust" />
			<kendo:grid-column title="초미세먼지(ug)" field="lc_pu_ultra_dust" />
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
						function parameterMap(options, type) {
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

.k-chart.small-chart {
	display: inline-block;
	width: 300px;
	height: 300px;
}

/* Kendo 그리드에서 bootstrap dropdownmenu 모두 보이게 */
.k-grid td {
	overflow: visible;
}
</style>

<!-- / Kendo grid  -->


<!-- Grid button actions  -->
<script>
	$(document).ready(function() {
		//최초 로컬 기기 상태 불러오기
		get_count_state();
		//10초주기로 로컬 기기 상태 불러오기
		setInterval(get_count_state, 10000);
		//지역날씨 정보 최신화
		reload_table();
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
		// 엑셀다운로드시 전체불러오기
		var exportFlag = false;
		$("#grid").data("kendoGrid").bind("excelExport", function (e) {
		    if (!exportFlag) {
		        e.sender.showColumn("lc_ip");
		        e.preventDefault();
		        exportFlag = true;
		        setTimeout(function () {
		            e.sender.saveAsExcel();
		        });
		    } else {
		        e.sender.hideColumn("lc_ip");
		        exportFlag = false;
		    }
		});

	});
</script>

<!-- / Grid button actions  -->


<!--  Root form container -->
<div id="formRoot"></div>


<!--  Forms -->

<!-- 상세보기 폼 -->
<script id="template-1" type="text/x-kendo-template">


<div class="modal fade" data-backdrop="static" id="form-modal-1">
	<div class="modal-dialog" style="max-width:45rem">
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
						<li class = "mr-2"style="margin-left:44%;margin-top:10px">
							<h5>SR그룹 선택</h5>
						</li>
						<li>
							<select  id="sr_group" name="sr_group" class="form-control" onchange="chageUnitSelect()">
								<option>----</option>
							</select>
						</li>

					</ul>
					<div class="tab-content mx-1">
						<div class="tab-pane p-2 active" id="lo-basic-ctnt">
							<div class="mb-4">
								<div class="row" style="margin:0">
									<div class='mt-3 card col-md-6 col-sm-6 col-6 col-lg-6 col-xl-6'>
										<div id="chart" class="small-chart" style="background: center no-repeat url('/resources/shared/images/logo/pi_chart_bg.png');"></div>
									</div>
									<div class="mt-3 card col-md-6 col-sm-6 col-6 col-lg-6 col-xl-6" >
										<ul class="list-group list-group-flush">
											<li class="list-group-item" style="padding-right:0; padding-left:0">
												<input type="date" name="state_date" value="" onchange="changeDate()" class="form-control ">
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
<!--  상세보기 그리드 테이블 생성 시 전원 이미지 설정 -->
<script id="testTemplate1" type="text/x-kendo-template">
	# if (sr_power == '0' ) { # <span class='pl-2'><img src='/resources/shared/images/logo/off.png'></span> # } 
	else if (sr_power == '1') { # <span class='pl-2'><img src='/resources/shared/images/logo/on.png'></span> #}
	else { # <span>-</span># } #
</script>

<script>
//SR그룹별 상세보기
function chageUnitSelect(){
	// select element에서 선택된 option의 value가 저장된다.  
	var srSelect = document.getElementById("sr_group"); 
	var selectText = srSelect.options[srSelect.selectedIndex].text;
	if (selectText.length == 1){
		selectText = "SR0" + selectText;	
	}
	else if (selectText.length == 2){
		selectText = "SR" + selectText;	
	}
	
	dataItemGrid["sr_group"] = selectText;
	reMakeSrGrid(dataItemGrid);
	var sr_group = $("#test").data("kendoGrid");
	sr_group.dataSource.read();
	
}
//sr셀렉트change event
function chageSrSelect(){  
 	lc_mac = dataItemGrid.lc_mac;
	data = {"lc_mac" : lc_mac
			};
 $.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		data: JSON.stringify(data),
		url: "${selectOpSrUrl}",
		success: function (sr_data) {
			if (sr_data != ""){

			    sr_group = [];
				for (i = 0;i<sr_data.length;i++){
					sr_group.push(sr_data[i].sr_no.slice(2,4));
				}
				sr_group_no_set = new Set(sr_group);
				sr_group_no = Array.from(sr_group_no_set);
				sr_group_select = "<select id='sr_group' name='sr_group' class='form-control' onchange='chageUnitSelect()'><option class='form-control' >전체</option>";
				if(sr_group_no != ""){
					for (i=0;i<sr_group_no_set.size;i++){
						sr_group_select += "</option><option class='form-control' >" +sr_group_no[i] +"</option>";
					}
					sr_group_select += "</select>";
				}

				$("#form-1 select[name='sr_group']").html(sr_group_select);
		

			}
			else {
				sr_group_select = "<select id='sr_group' name='sr_group' class='form-control' ><option class='form-control'>NO DATA</option></select>";
				$("#form-2 select[name='sr_group']").html(sr_group_select);
			}
			console.log( "성공");
		}
	});
 }
//상세보기 SR정보 테이블 만들기
function makeSrGrid(dataItem){
	$("#test").kendoGrid({
		dataSource : {
			transport : {
				read : {
					url : "${readSrUrl}",
					dataType : "json",
					data : dataItem,
					type : "POST",
					contentType : "application/json"
				},
				parameterMap : function parameterMap(options, type) {
					return JSON.stringify(options);
				}
			},
			schema : {
				data : "data",
				total : "total",
				groups : "data",
				model : {
					id : "id",
					fields : {
						whoCreationDate : {
							type : "date"
						}
					}
				},
				sort : {
					sortItem : {
						field : "id",
						dir : "desc"
					}
				}
			},
			serverPaging : true,
			serverFiltering : true,
			serverSorting : true,
			serverGrouping : true,
			error : kendoReadError,
		},
		pageable : {
			pageSize : 5
		},
		filterable : true,
		groupable : false,
		sortable : true,
		resizable : true,
		selectable : "${value_gridSelectable}",
		columns : [ {
			field : "sr_no",
			title : "구분",
		    width : "120"
		}, {
			field : "sr_power",
			title : "전원상태",
			template : $("#testTemplate1").html(),
			width : "120"
		}, {
			field : "sr_light",
			title : "광도값",
			width : "120"
		}, {
            field : "sr_battery",
            title : "배터리"
        }, {
            field : "sr_volt",
            title : "태양광"
		}

		]
	});	
} 
//상세보기에서 Select에 따른 SR정보 테이블 만들기
function reMakeSrGrid(sr_group) {
	$("#test").kendoGrid({
		dataSource : {
			transport : {
				read : {
					url : "${readSrGroupUrl}",
					dataType : "json",
					data : sr_group,
					type : "POST",
					contentType : "application/json"
				},
				parameterMap : function parameterMap(options, type) {
					return JSON.stringify(options);
				}
			},
			schema : {
				data : "data",
				total : "total",
				groups : "data",
				model : {
					id : "id",
					fields : {
						whoCreationDate : {
							type : "date"
						}
					}
				},
				sort : {
					sortItem : {
						field : "id",
						dir : "desc"
					}
				}
			},
			serverPaging : true,
			serverFiltering : true,
			serverSorting : true,
			serverGrouping : true,
			error : kendoReadError,
		},
		pageable : {
			pageSize : 5
		},
		filterable : true,
		groupable : false,
		sortable : true,
		resizable : true,
		selectable : "${value_gridSelectable}",
		columns : [ {
			field : "sr_no",
			title : "구분",
		    width : "120"
		}, {
			field : "sr_power",
			title : "전원상태",
			template : $("#testTemplate1").html(),
			width : "120"
		}, {
			field : "sr_light",
			title : "광도값",
			width : "120"
		}, {
            field : "sr_battery",
            title : "배터리"
        }, {
            field : "sr_volt",
            title : "태양광"
		}

		]
	});	

}
//상세보기 piChart에서 달력 날짜 변경이벤트
function changeDate() {
	date = $("#form-1 input[name='state_date']").val();
	lc_mac = dataItemGrid.lc_mac;
	data = {
		"date" : date,
		"lc_mac" : lc_mac
	};
	$.ajax({
		type : "POST",
		contentType : "application/json",
		dataType : "json",
		url : "${stateTimeUrl}",
		data : JSON.stringify(data),
		success : function(data) {
			createChart(data);
		},
		error : ajaxSaveError
	});
}
//지역날씨 최신화
function reload_table() {
		mydata = {
			"서울 종로구" : "11",
			"서울 중구" : "12",
			"서울 용산구" : "13",
			"서울 성동구" : "14",
			"서울 광진구" : "15",
			"서울 동대문구" : "16",
			"서울 중랑구" : "17",
			"서울 성북구" : "18",
			"서울 강북구" : "19",
			"서울 도봉구" : "1A",
			"서울 중구" : "12",
			"서울 용산구" : "13",
			"서울 성동구" : "14",
			"서울 광진구" : "15",
			"서울 동대문구" : "16",
			"서울 중랑구" : "17",
			"서울 성북구" : "18",
			"서울 강북구" : "19",
			"서울 도봉구" : "1A",
			"서울 종로구" : "11",
			"서울 중구" : "12",
			"서울 용산구" : "13",
			"서울 성동구" : "14",
			"서울 광진구" : "15",
			"서울 동대문구" : "16",
			"서울 중랑구" : "17",
			"서울 성북구" : "18",
			"서울 강북구" : "19",
			"서울 도봉구" : "1A",
			"서울 노원구" : "1B",
			"서울 은평구" : "1C",
			"서울 서대문구" : "1D",
			"서울 마포구" : "1E",
			"서울 양천구" : "1F",
			"서울 강서구" : "1G",
			"서울 구로구" : "1H",
			"서울 금천구" : "1I",
			"서울 영등포구" : "1J",
			"서울 동작구" : "1K",
			"서울 관악구" : "1L",
			"서울 서초구" : "1M",
			"서울 강남구" : "1N",
			"서울 송파구" : "1O",
			"서울 강동구" : "1P",
			"부산 중구" : "21",
			"부산 서구" : "22",
			"부산 동구" : "23",
			"부산 영도구" : "24",
			"부산 부산진구" : "25",
			"부산 동래구" : "26",
			"부산 남구" : "27",
			"부산 북구" : "28",
			"부산 해운대구" : "29",
			"부산 사하구" : "2A",
			"부산 금정구" : "2B",
			"부산 강서구" : "2C",
			"부산 연제구" : "2D",
			"부산 수영구" : "2E",
			"부산 사상구" : "2F",
			"부산 기장군" : "2G",
			"대구 중구" : "31",
			"대구 동구" : "32",
			"대구 서구" : "33",
			"대구 남구" : "34",
			"대구 북구" : "35",
			"대구 수성구" : "36",
			"대구 달서구" : "37",
			"대구 달성구" : "38",
			"인천 중구" : "41",
			"인천 동구" : "42",
			"인천 남구" : "43",
			"인천 연수구" : "44",
			"인천 남동구" : "45",
			"인천 부평구" : "46",
			"인천 계양구" : "47",
			"인천 서구" : "48",
			"인천 강화군" : "49",
			"인천 옹진군" : "4A",
			"대전 동구" : "61",
			"대전 중구" : "62",
			"대전 서구" : "63",
			"대전 유성구" : "64",
			"대전 대덕구" : "65",
			"울산 중구" : "71",
			"울산 남구" : "72",
			"울산 동구" : "73",
			"울산 북구" : "74",
			"울산 울주군" : "75",
			"세종특별자치시" : "81",
			"경기 수원시 장안구" : "A11",
			"경기 수원시 권선구" : "A12",
			"경기 수원시 팔달구" : "A13",
			"경기 수원시 영통구" : "A14",
			"경기 성남시 수정구" : "A21",
			"경기 성남시 중원구" : "A22",
			"경기 성남시 분당구" : "A23",
			"경기 의정부시" : "A3",
			"경기 안양시 만안구" : "A41",
			"경기 안양시 동안구" : "A42",
			"경기 부천시 원미구" : "A51",
			"경기 부천시 소사구" : "A52",
			"경기 부천시 오정구" : "A53",
			"경기 광명시" : "A6",
			"경기 평택시" : "A7",
			"경기 동두천시" : "A8",
			"경기 안산시 상록구" : "A91",
			"경기 안산시 단원구" : "A92",
			"경기 고양시 덕양구" : "AA1",
			"경기 고양시 일산동구" : "AA2",
			"경기 고양시 일산서구" : "AA3",
			"경기 과천시" : "AB",
			"경기 구리시" : "AC",
			"경기 남양주시" : "AD",
			"경기 오산시" : "AE",
			"경기 시흥시" : "AF",
			"경기 군포시" : "AG",
			"경기 의왕시" : "AH",
			"경기 하남시" : "AI",
			"경기 용인시 처인구" : "AJ1",
			"경기 용인시 기흥구" : "AJ2",
			"경기 용인시 수지구" : "AJ3",
			"경기 파주시" : "AK",
			"경기 이천시" : "AL",
			"경기 안성시" : "AM",
			"경기 김포시" : "AN",
			"경기 화성시" : "AO",
			"경기 광주시" : "AP",
			"경기 양주시" : "AQ",
			"경기 포천시" : "AR",
			"경기 여주시" : "AS",
			"경기 연천군" : "AT",
			"경기 가평군" : "AU",
			"경기 양평군" : "AV",
			"강원 춘천시" : "B1",
			"강원 원주시" : "B2",
			"강원 강릉시" : "B3",
			"강원 동해시" : "B4",
			"강원 태백시" : "B5",
			"강원 속초시" : "B6",
			"강원 삼척시" : "B7",
			"강원 홍천군" : "B8",
			"강원 횡성군" : "B9",
			"강원 영월군" : "BA",
			"강원 평창군" : "BB",
			"강원 정선군" : "BC",
			"강원 철원군" : "BD",
			"강원 화천군" : "BE",
			"강원 양구군" : "BF",
			"강원 인제군" : "BG",
			"강원 고성군" : "BH",
			"강원 양양군" : "BI",
			"충북 청주시 상당구" : "C11",
			"충북 청주시 서원구" : "C12",
			"충북 청주시 흥덕구" : "C13",
			"충북 청주시 청원구" : "C14",
			"충북 충주시" : "C2",
			"충북 제천시" : "C3",
			"충북 보은군" : "C4",
			"충북 옥천군" : "C5",
			"충북 영동군" : "C6",
			"충북 증평군" : "C7",
			"충북 진천군" : "C8",
			"충북 괴산군" : "C9",
			"충북 음성군" : "CA",
			"충북 단양군" : "CB",
			"충남 천안시 동남구" : "D11",
			"충남 천안시 서북구" : "D12",
			"충남 공주시" : "D2",
			"충남 보령시" : "D3",
			"충남 아산시" : "D4",
			"충남 서산시" : "D5",
			"충남 논산시" : "D6",
			"충남 계룡시" : "D7",
			"충남 당진시" : "D8",
			"충남 금산군" : "D9",
			"충남 부여군" : "DA",
			"충남 서천군" : "DB",
			"충남 청양군" : "DC",
			"충남 홍성군" : "DD",
			"충남 예산군" : "DE",
			"충남 태안군" : "DF",
			"전북 전주시 완산구" : "E11",
			"전북 전주시 덕진구" : "E12",
			"전북 군산시" : "E2",
			"전북 익산시" : "E3",
			"전북 정읍시" : "E4",
			"전북 남원시" : "E5",
			"전북 김제시" : "E6",
			"전북 완주군" : "E7",
			"전북 진안군" : "E8",
			"전북 무주군" : "E9",
			"전북 장수군" : "EA",
			"전북 임실군" : "EB",
			"전북 순창군" : "EC",
			"전북 고창군" : "ED",
			"전북 부안군" : "EE",
			"전남 목포시" : "F1",
			"전남 여수시" : "F2",
			"전남 순천시" : "F3",
			"전남 나주시" : "F4",
			"전남 광양시" : "F5",
			"전남 담양군" : "F6",
			"전남 곡성군" : "F7",
			"전남 구례군" : "F8",
			"전남 고흥군" : "F9",
			"전남 보성군" : "FA",
			"전남 화순군" : "FB",
			"전남 장흥군" : "FC",
			"전남 강진군" : "FD",
			"전남 해남군" : "FE",
			"전남 영암군" : "FF",
			"전남 무안군" : "FG",
			"전남 함평군" : "FH",
			"전남 영광군" : "FI",
			"전남 장성군" : "FJ",
			"전남 완도군" : "FK",
			"전남 진도군" : "FL",
			"전남 신안군" : "FM",
			"경북 포항시" : "G11",
			"경북 포항시" : "G12",
			"경북 경주시" : "G2",
			"경북 김천시" : "G3",
			"경북 안동시" : "G4",
			"경북 구미시" : "G5",
			"경북 영주시" : "G6",
			"경북 영천시" : "G7",
			"경북 상주시" : "G8",
			"경북 문경시" : "G9",
			"경북 경산시" : "GA",
			"경북 군위군" : "GB",
			"경북 의성군" : "GC",
			"경북 청송군" : "GD",
			"경북 영양군" : "GE",
			"경북 영덕군" : "GF",
			"경북 청도군" : "GG",
			"경북 고령군" : "GH",
			"경북 성주군" : "GI",
			"경북 칠곡군" : "GJ",
			"경북 예천군" : "GK",
			"경북 봉화군" : "GL",
			"경북 울진군" : "GM",
			"경북 울릉군" : "GN",
			"경남 창원시 의창구" : "H11",
			"경남 창원시 성산구" : "H12",
			"경남 창원시 마산합포구" : "H13",
			"경남 창원시 마산회원구" : "H14",
			"경남 창원시 진해구" : "H15",
			"경남 진주시" : "H2",
			"경남 통영시" : "H3",
			"경남 사천시" : "H4",
			"경남 김해시" : "H5",
			"경남 밀양시" : "H6",
			"경남 거제시" : "H7",
			"경남 양산시" : "H8",
			"경남 의령군" : "H9",
			"경남 함안군" : "HA",
			"경남 창녕군" : "HB",
			"경남 고성군" : "HC",
			"경남 남해군" : "HD",
			"경남 하동군" : "HE",
			"경남 산청군" : "HF",
			"경남 함양군" : "HG",
			"경남 거창군" : "HH",
			"경남 합천군" : "HI",
			"제주도 제주시" : "I1",
			"제주도 서귀포시" : "I2"
		};
		$.ajax({
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			data : JSON.stringify(mydata),
			url : "${readWeatherUrl}",
			success : function(data) {
				$("#grid").data("kendoGrid").dataSource.read();
			}
		//error: ajaxReadError
		});
	}
	//로컬제어기 상태별 갯수 불러오기 이벤트
	function get_count_state() {
		data = {
			"id" : "111"
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
	//상세보기 버튼 클릭이벤트
	function detail(lc_id) {
		viewDetail(lc_id);
		$('#form-modal-1 .modal-dialog').draggable({
			handle : '.modal-header'
		});
		$("#form-modal-1").modal();
	}
	//상세보기 내용 생성
	function viewDetail(lc_id) {
		dataItemGrid = $("#grid").data("kendoGrid").dataSource.get(lc_id);
		title = dataItemGrid.lc_name;
		$("#formRoot").html(kendo.template($("#template-1").html()));
		$("#form-1 span[name='title']").text(title);
		var today = new Date(+new Date() + 3240 * 10000).toISOString().split(
				"T")[0];
		$("#form-1 input[name='state_date']").val(today);
		date = $("#form-1 input[name='state_date']").val();
		chageSrSelect();
		lc_mac = dataItemGrid.lc_mac;
		data = {
			"date" : date,
			"lc_mac" : lc_mac
		};
		$.ajax({
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			url : "${stateTimeUrl}",
			data : JSON.stringify(data),
			success : function(data) {
				createChart(data);
			},
			error : ajaxSaveError
		});
		makeSrGrid(dataItemGrid);

	}
	//파이차트 생성 이벤트
	function createChart(data) {
		sums = data["sums"];
		sumList = sums.split(",");
		sumList[0] = sumList[0].slice(1);
		sumList[3] = sumList[3].slice(0, -1);
		$("#form-1 span[name='normal']").text(sumList[0] + "분");
		$("#form-1 span[name='partOff']").text(sumList[1] + "분");
		$("#form-1 span[name='allOff']").text(sumList[2] + "분");
		$("#form-1 span[name='lcOff']").text(sumList[3] + "분");
		$("#chart").kendoChart({
			title : {

			},
			legend : {
				visible : false
			},
			chartArea : {
				background : ""
			},
			seriesDefaults : {
			/*
			labels: {
			visible: true,
			background: "transparent",
			template: "#= category #: \n #= value#%"
			}
			 */
			},
			series : [ {
				type : "pie",
				startAngle : 90,
				data : [ {
					category : "1",
					value : 1,
					color : data["00"]
				}, {
					category : "2",
					value : 1,
					color : data["01"]
				}, {
					category : "3",
					value : 1,
					color : data["02"]
				}, {
					category : "4",
					value : 1,
					color : data["03"]
				}, {
					category : "5",
					value : 1,
					color : data["04"]
				}, {
					category : "6",
					value : 1,
					color : data["05"]
				}, {
					category : "7",
					value : 1,
					color : data["06"]
				}, {
					category : "8",
					value : 1,
					color : data["07"]
				}, {
					category : "9",
					value : 1,
					color : data["08"]
				}, {
					category : "10",
					value : 1,
					color : data["09"]
				}, {
					category : "11",
					value : 1,
					color : data["10"]
				}, {
					category : "12",
					value : 1,
					color : data["11"]
				}, {
					category : "13",
					value : 1,
					color : data["12"]
				}, {
					category : "14",
					value : 1,
					color : data["13"]
				}, {
					category : "15",
					value : 1,
					color : data["14"]
				}, {
					category : "16",
					value : 1,
					color : data["15"]
				}, {
					category : "17",
					value : 1,
					color : data["16"]
				}, {
					category : "18",
					value : 1,
					color : data["17"]
				}, {
					category : "19",
					value : 1,
					color : data["18"]
				}, {
					category : "20",
					value : 1,
					color : data["19"]
				}, {
					category : "21",
					value : 1,
					color : data["20"]
				}, {
					category : "22",
					value : 1,
					color : data["21"]
				}, {
					category : "23",
					value : 1,
					color : data["22"]
				}, {
					category : "24",
					value : 1,
					color : data["23"]
				}

				]
			} ],
			tooltip : {
				visible : true,
				format : "{0}%"
			}
		});
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
