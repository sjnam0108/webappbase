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
<c:url value="/srs/gridview/readSr" var="readSrUrl" />
<c:url value="/srs/gridview/readSrGroup" var="readSrGroupUrl" />
<c:url value="/srs/recontrolauto/selectOpSr" var="selectOpSrUrl" />
<c:url value="/srs/gridview/update" var="updateUrl" />
<c:url value="/srs/gridview/stateTime" var="stateTimeUrl" />


<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/proj4js/1.1.0/proj4js-combined.min.js"></script>

<!-- Opening tags -->

<common:pageOpening />


<!-- Page title -->

<h4 class="pt-1 pb-3 mb-3">
	<i class="fa fa-calendar-alt mr-2"></i> ${pageTitle}
</h4>

<hr class="border-light container-m--x mt-0 mb-4">


<!-- Page body -->

<!-- Java(optional)  -->
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
String regDateTemplate = kr.co.bbmc.utils.Util.getSmartDate();
%>
<%
String detailTemplate = "<button type='button'  onclick='detail(#= id #)' class='btn icon-btn btn-sm btn-outline-success borderless'>"
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
	<span class="mr-1 pr-2" id="gray"></span> <span>실시간 (10초 주기)</span>
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
			<div class="d-flex align-items-center">
				<span class="d-none d-lg-inline">
					<button type="button" class="btn btn-default k-grid-excel">${cmd_excel}</button>
				</span>
			</div>
		</kendo:grid-toolbarTemplate>
		<kendo:grid-columns>
			<kendo:grid-column title="상세보기" width="50" filterable="false"
				sortable="false" template="<%=detailTemplate%>" />
			<kendo:grid-column title="상태" field="lc_state"
				template="<%=lc_stateTemplate%>" />
			<kendo:grid-column title="제어기 ID" field="id" />
			<kendo:grid-column title="로컬제어기" field="lc_name" />
			<kendo:grid-column title="도로명" field="lc_road_name" />
			<kendo:grid-column title="주소" field="lc_addr1" />
			<kendo:grid-column title="관리 연락처" field="lc_manager_tel" />
			<kendo:grid-column title="최근 보고" field="whoLastUpdateDate" />
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
</style>

<!-- / Kendo grid  -->


<!-- Grid button actions  -->

<script>



	$(document).ready(function() {
		//최초 로컬 기기 상태 불러오기
		get_count_state();
		//10초주기로 로컬 기기 상태 불러오기
		setInterval(get_count_state, 10000);
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


<!-- 상세보기 폼 -->
<!--  Forms -->
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
<!--  상세보기 그리드 테이블 생성 시 전원 이미지 설정 -->
<script id="testTemplate1" type="text/x-kendo-template">
	# if (sr_power == '0' ) { # <span class='pl-2'><img src='/resources/shared/images/logo/off.png'></span> # } 
	else if (sr_power == '1') { # <span class='pl-2'><img src='/resources/shared/images/logo/on.png'></span> #}
	else { # <span>-</span># } #
</script>


<script>
	//SR그룹별 상세보기
	function chageUnitSelect() {
		// select element에서 선택된 option의 value가 저장된다.  
		var srSelect = document.getElementById("sr_group");
		var selectText = srSelect.options[srSelect.selectedIndex].text;
		if (selectText.length == 1) {
			selectText = "SR0" + selectText;
		} else if (selectText.length == 2) {
			selectText = "SR" + selectText;
		}

		dataItemGrid["sr_group"] = selectText;
		reMakeSrGrid(dataItemGrid);
		var sr_group = $("#test").data("kendoGrid");
		sr_group.dataSource.read();

	}
	//sr셀렉트change event
	function chageSrSelect() {
		lc_mac = dataItemGrid.lc_mac;
		data = {
			"lc_mac" : lc_mac
		};
		$
				.ajax({
					type : "POST",
					contentType : "application/json",
					dataType : "json",
					data : JSON.stringify(data),
					url : "${selectOpSrUrl}",
					success : function(sr_data) {
						if (sr_data != "") {

							sr_group = [];
							for (i = 0; i < sr_data.length; i++) {
								sr_group.push(sr_data[i].sr_no.slice(2, 4));
							}
							sr_group_no_set = new Set(sr_group);
							sr_group_no = Array.from(sr_group_no_set);
							sr_group_select = "<select id='sr_group' name='sr_group' class='form-control' onchange='chageUnitSelect()'><option class='form-control' >전체</option>";
							if (sr_group_no != "") {
								for (i = 0; i < sr_group_no_set.size; i++) {
									sr_group_select += "</option><option class='form-control' >"
											+ sr_group_no[i] + "</option>";
								}
								sr_group_select += "</select>";
							}

							$("#form-1 select[name='sr_group']").html(
									sr_group_select);

						} else {
							sr_group_select = "<select id='sr_group' name='sr_group' class='form-control' ><option class='form-control'>NO DATA</option></select>";
							$("#form-2 select[name='sr_group']").html(
									sr_group_select);
						}
						console.log("성공");
					}
				});
	}
	//상세보기 SR정보 테이블 만들기
	function makeSrGrid(dataItem) {
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
	function detail(lc_id) {
		viewDetail(lc_id);
		$('#form-modal-1 .modal-dialog').draggable({
			handle : '.modal-header'
		});
		$("#form-modal-1").modal();
	}
	//상세보기 이벤트 
	function viewDetail(lc_id) {
		dataItemGrid = $("#grid").data("kendoGrid").dataSource.get(lc_id);
		title = dataItemGrid.lc_name;
		$("#formRoot").html(kendo.template($("#template-1").html()));
		$("#form-1 span[name='title']").text(title);
		chageSrSelect();
		today = new Date(+new Date() + 3240 * 10000).toISOString().split("T")[0];
		$("#form-1 input[name='state_date']").val(today);
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

<func:cmmValidate />


<!-- Closing tags -->

<common:base />
<common:pageClosing />

