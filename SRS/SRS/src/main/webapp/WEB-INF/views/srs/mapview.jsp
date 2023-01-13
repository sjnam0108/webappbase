<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->
<c:url value="/srs/localctrl/countstate" var="cntStateUrl" />
<c:url value="/srs/localctrl/read" var="lcreadUrl" />
<c:url value="/srs/mapview/read" var="readUrl" />
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
	<span class="mr-1 fas fa-map"></span> ${pageTitle}
</h4>

<hr class="border-light container-m--x mt-0 mb-4">





<!-- Page body -->


<!-- Java(optional)  -->

<%
String regDateTemplate = kr.co.bbmc.utils.Util.getSmartDate();
%>
<%
String detailTemplate = "<button type='button' onclick='detail(#= id #)' class='btn icon-btn btn-sm btn-outline-success borderless'>"
		+ "<span class='fas fa-pencil-alt'></span></button>";
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

	<button id="refresh" type="button" onclick='refresh()'
		class="btn btn-default ml-3">지도 새로고침</button>
	<button id="roadState" type="button" onclick='roadState()'
		class="btn btn-default">교통정보</button>
</div>
<!-- 로컬제어기 별 상세보기 기능을 위해 none 디스플레이 상태로 필요 -->
<!-- Kendo grid  -->
<div class="mb-4" style="display: none">
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
				sortable="false" template="<%= detailTemplate %>" />
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
				<kendo:dataSource-transport-read url="${lcreadUrl}" dataType="json"
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

<!-- 지도 DIV -->
<div class="mb-4">
	<div id="map" style="width: 100%; height: 800px;"></div>
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

.info {
	line-height: 1.5rem;
	width: 320px;
	height: 100px;
}

.info-content {
	font-size: 14px;
	margin-bottom: 5px;
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
	margin-bottom: 1%;
	font-size: 17px;
}
</style>




<!-- kakao map actions  -->
<script type="text/javascript"
	src="//dapi.kakao.com/v2/maps/sdk.js?appkey=515f641b68a957d80bf598cc7321c251"></script>
<script>
	
</script>
<!-- Grid button actions  -->
<script>
	$(document).ready(function() {
		//최초 로컬 기기 상태 불러오기
		get_count_state();
		//10초주기로 로컬 기기 상태 불러오기
		setInterval(get_count_state, 10000);
		//숨어있는 로컬제어기 테이블생성
		get_lc_ctrl_list();
		z = 0;

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
	lc_mac = dataItem.lc_mac;
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
	//로컬제어기 정보 불러오기
	function get_lc_ctrl_list() {
		data = {
			id : "111"
		};
		$.ajax({
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			data : JSON.stringify(data),
			url : "${readUrl}",
			success : function(data) {
				setTimeout(function() {
					//맵에 로컬제어기 별 마크업 생성
					make_markup(data);
				}, 300);
				console.log(data);
			}
		//error: ajaxReadError
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
	//상세보기 클릭이벤트
	function detail(id) {
		viewDetail(id);
		//$("#form-1").attr("rowid", dataItem.id);
		//	$("#form-1").attr("url", "${updateUrl}");
		$('#form-modal-1 .modal-dialog').draggable({
			handle : '.modal-header'
		});
		$("#form-modal-1").modal();
	}
	//상세보기 이벤트 
	function viewDetail(lc_id) {

		dataItem = $("#grid").data("kendoGrid").dataSource.get(lc_id);
		title = dataItem.lc_name;
		$("#formRoot").html(kendo.template($("#template-1").html()));

		//apply the activate event, which is thrown only after the anima
		$("#form-1 span[name='title']").text(title);
		//상세보기 파이차트 내용 불러오
		dataItemGrid = $("#grid").data("kendoGrid").dataSource.get(lc_id);
		chageSrSelect();
		var today = new Date(+new Date() + 3240 * 10000).toISOString().split(
				"T")[0];
		$("#form-1 input[name='state_date']").val(today);
		date = $("#form-1 input[name='state_date']").val();
		lc_mac = dataItem.lc_mac;
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
		////////////////모달기본 파이차트 

		makeSrGrid(dataItem);
	}
	//파이차트 생성 이벤트
	function createChart(data) {
		sums = data["sums"];
		sumList = sums.split(",");
		sumList[0] = sumList[0].slice(1);
		sumList[3] = sumList[3].slice(0,-1);
		$("#form-1 span[name='normal']").text(sumList[0]+"분");
		$("#form-1 span[name='partOff']").text(sumList[1]+"분");
		$("#form-1 span[name='allOff']").text(sumList[2]+"분");
		$("#form-1 span[name='lcOff']").text(sumList[3]+"분");
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
	//지도 새로고침 이벤트
	function refresh() {
		map.relayout();
	}
	//교통정보 추가 버튼클릭이벤트	
	function roadState() {

		// 아래 코드는 위에서 추가한 교통정보 지도타입을 제거합니다

		if (z % 2 == 0) {
			map.addOverlayMapTypeId(kakao.maps.MapTypeId.TRAFFIC);
			z++;
		}

		else {
			map.removeOverlayMapTypeId(kakao.maps.MapTypeId.TRAFFIC);
			z++;
		}

	}
    //지도에 로컬제어기 별 마크업 생성 이벤트
	function make_markup(data) {
		var container = document.getElementById('map');
		var positions = [];

		// 마커를 표시할 위치와 내용을 가지고 있는 객체 배열입니다 
		var sumlat = 0;
		var sumlng = 0;
		var cnt = 0;
		for (i = 0; i < data.length; i++) {

			lc_state = data[i]['lc_state'];
			state_val = "";
			if (lc_state == "0") {
				state_val = "blue";
			}
			if (lc_state == "1") {
				state_val = "yellow";
			}
			if (lc_state == "2") {
				state_val = "orange";
			}
			if (lc_state == "3") {
				state_val = "red";
			}
			if (lc_state == "4") {
				state_val = "gray";
			}
			positions[i] = {

				content : "<div class='info'><div class='info-title'><h5 style='display:flex;align-items:center'><img class='mr-1' src='/resources/shared/images/logo/" + state_val + "_small.png'>"
						+ data[i]['lc_name']
						+ "<button type='button'  onclick= detail("
						+ data[i]['id']
						+ ") style='margin-top:3px' class='ml-1 btn icon-btn btn-outline-primary borderless'>"
						+ "<i class='fa fa-search' ></i></button></h5></div>"
						+ "<div class='info-content'> "
						+ "<i class='ml-1 fa fa-check'></i>주소 :"
						+ data[i]['lc_addr1']
						+ "<p><p><i class='ml-1 fas fa-check'></i>최근보고 : "
						+ data[i]['whoLastUpdateDate'] + "</div></div>",
				latlng : new kakao.maps.LatLng(data[i]["lc_gps_lat"],
						data[i]["lc_gps_long"])
			}
			if (data[i]["lc_gps_lat"] != '' && data[i]["lc_gps_lat"] != null) {
				sumlat += parseInt(data[i]["lc_gps_lat"]);
				sumlng += parseInt(data[i]["lc_gps_long"]);
				cnt += 1;
			}
		}
		sumlat = sumlat / cnt;
		sumlng = sumlng / cnt;
		var options = {
			center : new kakao.maps.LatLng(sumlat, sumlng),
			level : 12
		};
		map = new kakao.maps.Map(container, options);
		// 지도를 표시하는 div 크기를 변경한 이후 지도가 정상적으로 표출되지 않을 수도 있습니다
		// 크기를 변경한 이후에는 반드시  map.relayout 함수를 호출해야 합니다 
		// window의 resize 이벤트에 의한 크기변경은 map.relayout 함수가 자동으로 호출됩니다

		for (var i = 0; i < positions.length; i++) {
			// 마커를 생성합니다
			var marker = new kakao.maps.Marker({
				map : map, // 마커를 표시할 지도
				position : positions[i].latlng
			// 마커의 위치
			});
			iwRemoveable = true;

			// 마커에 표시할 인포윈도우를 생성합니다 
			var infowindow = new kakao.maps.InfoWindow({
				content : positions[i].content, // 인포윈도우에 표시할 내용
				removable : iwRemoveable
			});

			// 마커에 mouseover 이벤트와 mouseout 이벤트를 등록합니다
			// 이벤트 리스너로는 클로저를 만들어 등록합니다 
			// for문에서 클로저를 만들어 주지 않으면 마지막 마커에만 이벤트가 등록됩니다
			kakao.maps.event.addListener(marker, 'click', clickListener(map,
					marker, infowindow));
			map.relayout();
		}

		function panTo(marker) {
			// 이동할 위도 경도 위치를 생성합니다
			var lng = marker.getPosition()["La"]
			var lat = marker.getPosition()["Ma"]

			var moveLatLon = new kakao.maps.LatLng(lat, lng);

			// 지도 중심을 부드럽게 이동시킵니다
			// 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
			map.setLevel(8);
			map.panTo(moveLatLon);

		}

		// 인포윈도우를 표시하는 클로저를 만드는 함수입니다 
		function clickListener(map, marker, infowindow) {
			return function() {
				infowindow.open(map, marker);
				panTo(marker);
			};
		}
		var infoTitle = document.querySelectorAll('.info-title');
		infoTitle.forEach(function(e) {
			var w = e.offsetWidth + 10;
			var ml = w / 2;
			e.parentElement.style.top = "82px";
			e.parentElement.style.left = "50%";
			e.parentElement.style.marginLeft = -ml + "px";
			e.parentElement.style.width = w + "px";
			e.parentElement.previousSibling.style.display = "none";
			e.parentElement.parentElement.style.border = "0px";
			e.parentElement.parentElement.style.background = "unset";
		});

	}


</script>

<!--  / Scripts -->


<!-- / Page body -->





<!-- Functional tags -->

<func:cmmValidate />


<!-- Closing tags -->

<common:base />
<common:pageClosing />

