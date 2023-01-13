<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/srs/localctrl/countstate" var="cntStateUrl" />
<c:url value="/srs/recontrolauto/read" var="readUrl" />
<c:url value="/srs/noticesetting/selectOpLc" var="selectOpLcUrl" />
<c:url value="/srs/recontrolauto/selectOpSr" var="selectOpSrUrl" />
<c:url value="/srs/recontrolauto/selectLc" var="selectLcUrl" />
<c:url value="/srs/recontrolauto/create" var="createUrl" />
<c:url value="/srs/recontrolauto/update" var="updateUrl" />
<c:url value="/srs/recontrolauto/destroy" var="destroyUrl" />
<c:url value="/srs/recontrolauto/updateCtrlState"
	var="updateCtrlStateUrl" />



<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/proj4js/1.1.0/proj4js-combined.min.js"></script>
<!-- Opening tags -->

<common:pageOpening />


<!-- Page title -->

<h4 class="pt-1 pb-3 mb-3">
	<i class="fas fa-flag mr-2"></i> ${pageTitle}
</h4>

<hr class="border-light container-m--x mt-0 mb-4">





<!-- Page body -->


<!-- Java(optional)  -->

<%
String lc_stateTemplate = "# if (ctrl_condition == '0' ) { # <span class='pl-2'><img src='/resources/shared/images/logo/off.png'></span> # }"
		+ "else if (ctrl_condition == '1') { # <span class='pl-2'><img src='/resources/shared/images/logo/on.png'></span> #}"
		+ "else { # <span>-</span># } #";
%>

<%
String sr_powerTemplate = "# if (ctrl_power == '0' ) { # <span class='pl-2'>OFF</span> # }"
		+ "else if (ctrl_power == '1') { # <span class='pl-2'>ON</span> #}" + "else { # <span>-</span># } #";
%>

<%
String editTemplate = "<button type='button'  onclick='edit(#= id #)' class='btn icon-btn btn-sm btn-outline-success borderless'>"
		+ "<<span class='fas fa-pencil-alt'></span></button>";
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

<div class="mb-4">
	<kendo:grid name="grid" pageable="true" groupable="false"
		filterable="true" sortable="true" scrollable="false"
		reorderable="true" resizable="true"
		selectable="true">
		<kendo:grid-excel fileName="${pageTitle}.xlsx" allPages="true"
			proxyURL="/proxySave" />
		<kendo:grid-pageable refresh="true" buttonCount="5" pageSize="10"
			pageSizes="${pageSizesNormal}" />
		<kendo:grid-toolbarTemplate>
			<div class="clearfix">
				<span class="ml-2">
					<button id="add-btn" type="button" class="btn btn-outline-success">${cmd_add}</button>
				</span>
				<div class="float-left">
					<button type="button"
						class="btn btn-default d-none d-sm-inline k-grid-excel">${cmd_excel}</button>
				</div>
				<div class="float-right">
					<button id="delete-btn" type="button" class="btn btn-danger">${cmd_delete}</button>
				</div>
				<div class="float-right">
					<h3 class="mr-2 ml-2">|</h3>
				</div>
				<div class="float-right ml-2">
					<button type="button" class="btn btn-default"
						onclick="excAutoCtrl('중지')">중지</button>
				</div>
				<div class="float-right">
					<button type="button" class="btn btn-default"
						onclick="excAutoCtrl('진행')">자동제어</button>
				</div>
				<div class="float-right">
					<h5 class="mr-2 pt-2" style="margin-bottom:0;">※ 자동제어 주기  1 분</h5>
				</div>
			</div>
		</kendo:grid-toolbarTemplate>
		<kendo:grid-columns>
			<kendo:grid-column title="수정" width="50" filterable="false"
				sortable="false" template="<%=editTemplate%>" />
			<kendo:grid-column title="자동제어 상태" field="ctrl_condition"
				template="<%=lc_stateTemplate%>" />
			<kendo:grid-column title="로컬제어기" field="lc_name" />
			<kendo:grid-column title="시간" field="time" />
			<kendo:grid-column title="강우(mm/h 미만)" field="rain" />
			<kendo:grid-column title="시정거리(m 미만) " field="v_ability" />
			<kendo:grid-column title="제한속도(km/h)" field="speed" />
			<kendo:grid-column title="백색 적정광도(mcd)" field="ctrl_wLight" />
			<kendo:grid-column title="황색 적정광도(mcd)" field="ctrl_yLight" /> 
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
		checkPower = "0";
		// Add
		$("#add-btn").click(function(e) {
			e.preventDefault();
			get_select_option("add")

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
			if (delRows == "") {
				showAlertModal("danger", "로컬제어기를 선택해 주세요.");
			}
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
<!-- 추가/수정폼 -->
<script id="template-1" type="text/x-kendo-template">

<div class="modal fade" data-backdrop="static" id="form-modal-1">
	<div class="modal-dialog" style="max-width:110rem">
		<form class="modal-content" id="form-1" rowid="-1" url="${createUrl}">
      
			<!-- Modal Header -->
			<div class="modal-header move-cursor">
				<h5 class="modal-title">
					적정광도 제어<span class="font-weight-light pl-1"><span name="subtitle"></span>
				</h5>
			</div>
        
			<!-- Modal body -->
			<div class="modal-body modal-bg-color py-3">
				<div class="row">
					<div class='card col-md-9 col-sm-9 col-9 col-lg-9 col-xl-9'>
						<div class="row">
							<div class="card col-md-2 col-sm-2 col-2 col-lg-2 col-xl-2" >
								<ul class="list-group list-group-flush">
									<li class="d-flex align-items-center justify-content-center list-group-item " >
										제어기
									</li>
									<li class="list-group-item" style="padding-right:0; padding-left:0">
										<div>
										<select id ="lc_ctrl-1" name="lc_ctrl-1" class="form-control" onchange="chageLangSelect()">
										</select>
										</div>
									</li>
								</ul>
							</div>
							<div class="card col-md-2 col-sm-2 col-2 col-lg-2 col-xl-2" >
								<ul class=" d-flex align-items-center justify-content-center list-group list-group-flush">
									<li class="list-group-item">
										제한속도(km/h)
									</li>
								<li class="list-group-item">
										<input type="text" name="speed"readonly class="form-control">
									</li>
								</ul>
							</div>
							<div class="card col-md-2 col-sm-2 col-2 col-lg-2 col-xl-2" >
								<ul class="list-group list-group-flush">
									<li class="d-flex align-items-center justify-content-center list-group-item " >
										시간
									</li>
									<li class="list-group-item" style="padding-right:0; padding-left:0">
										<div>
										<select id ="time" name="time" class="form-control" >
											<option>
												주간
											</option>
											<option>
												야간
											</option>
										</select>
										</div>
									</li>
								</ul>
							</div>

							<div class="card col-md-3 col-sm-3 col-3 col-lg-3 col-xl-3" >
								<ul class="d-flex align-items-center justify-content-center list-group list-group-flush">
									<li class="list-group-item">
										강우(mm/h 미만)			
									</li>
									<li class="list-group-item">
										<div class = "row">
											<div class="col">
												<input type="number"  style= "width:65px;padding:5px" name="rain1" class="form-control">
											</div>
											<div class="col d-flex align-items-center">
												~
											</div>
											<div class="col">			
												<input type="number"  style= "width:65px;padding:5px"  name="rain2" class="form-control">
											</div>
										</div>
									</li>
								</ul>
							</div>
							<div class="card col-md-3 col-sm-3 col-3 col-lg-3 col-xl-3" >
								<ul class="d-flex align-items-center justify-content-center list-group list-group-flush">
									<li class="list-group-item">
										시정거리(m 미만)
									</li>
									<li class="list-group-item">
										<div class = "row">
											<div class="col">
												<input type="number"  style= "width:65px;padding:5px" name="v_ability1" class="form-control">
											</div>
											<div class="col d-flex align-items-center">
												~
											</div>
											<div class="col">			
												<input type="number"  style= "width:65px;padding:5px"  name="v_ability2" class="form-control">
											</div>
										</div>
									</li>
								</ul>
							</div>
							
						</div>
					</div>
					<div class='card col-md-3 col-sm-3 col-3 col-lg-3 col-xl-3'>
						<div class="row">
							<div class="card col-md-6 col-sm-6 col-6 col-lg-6 col-xl-6" >
								<ul class="list-group list-group-flush">
									<li class="d-flex align-items-center justify-content-center list-group-item">
										백색 적정광도(mcd)
									</li>
									<li class="list-group-item">
										<input name = "sr_wLight" type="number" class="form-control">
									</li>
								</ul>
							</div>
							<div class="card col-md-6 col-sm-6 col-6 col-lg-6 col-xl-6"
								<ul class="list-group list-group-flush">
									<li class="d-flex align-items-center justify-content-center list-group-item">
										황색 적정광도(mcd)
									</li>
									<li class="list-group-item">
										<input name = "sr_yLight" type="number" class="form-control">
									</li>
								</ul>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- Modal footer -->
			<div class="modal-footer">
				<div class="float-left pr-4">
					<span>※ 주간 (07:00~19:00) 야간 (19:00~07:00)
					</span>
				</div>
				<button type="button" class="btn btn-default" data-dismiss="modal">${form_cancel}</button>
				<button type="button" class="btn btn-primary" onclick='saveForm()'>${form_save}</button>
			</div>
			
		</form>
	</div>
</div>

</script>


<!--  Scripts -->

<script>
	//추가 폼 값 수정
	function addForm(subtitle, lc_ctrl_op) {
		lc_ctrl_select = "<select id='lc_ctrl-1' name='lc_ctrl' class='form-control' onchange='chageLangSelect()'>";
		if (lc_ctrl_op != "") {
			for (i = 0; i < lc_ctrl_op.length; i++) {
				lc_ctrl_select += "<option class='form-control' value='"+lc_ctrl_op[i].lc_mac +"'>"
						+ lc_ctrl_op[i].lc_name + "</option>";
			}
			lc_ctrl_select += "</select>";
		}
		$("#formRoot").html(kendo.template($("#template-1").html()));
		$("#form-1 select[name='lc_ctrl-1']").html(lc_ctrl_select);
		$("#form-1 input[name='speed']").val(lc_ctrl_op[0].lc_sp_limit);
		$("#form-1 span[name='subtitle']").text(
				subtitle ? subtitle : "${form_add}");
		$("#lc_ctrl-1").trigger("change");
		// set validation

	}
	//변경 버튼 클릭이벤트
	function edit(id) {
		get_select_option("edit", id);
	}
	//변경 폼 값 수정
	function editForm(subtitle, lc_ctrl_op, id) {
		var dataItem = $("#grid").data("kendoGrid").dataSource.get(id);
		time_select = "<select name='time' id = 'time' class='form-control'>";
		time_list = ["주간","야간"];
		if(time_list != ""){
			for (i=0;i<time_list.length;i++){
				if( time_list[i] == dataItem.time){
					time_select += "<option class='form-control' selected>" +time_list[i] +"</option>"	
				}
				else{
					time_select += "<option class='form-control'>" +time_list[i] +"</option>"	
				}
				
			}
			time_select += "</select>"
		}
		lc_ctrl_select = "<select id='lc_ctrl-1' name='lc_ctrl-1' class='form-control' onchange='chageLangSelect()'>"
				+ "<option class='form-control' value='" + dataItem.lc_mac + "'>"
				+ dataItem.lc_name + "</option></select>";
		$("#formRoot").html(kendo.template($("#template-1").html()));
		$("#form-1 select[name='time']").html(time_select);
		$("#form-1 select[name='lc_ctrl-1']").html(lc_ctrl_select);
		checkPower = dataItem.ctrl_power;
		$("#form-1").attr("rowid", dataItem.id);
		$("#form-1").attr("url", "${updateUrl}");
		rain = dataItem.rain.split(" ~ ");
		v_ability = dataItem.v_ability.split(" ~ ");
		$("#form-1 input[name='rain1']").val(rain[0]);
		$("#form-1 input[name='rain2']").val(rain[1]);
		$("#form-1 input[name='v_ability1']").val(v_ability[0]);
		$("#form-1 input[name='v_ability2']").val(v_ability[1]);
		$("#form-1 input[name='speed']").val(dataItem.speed);
		$("#form-1 input[name='sr_wLight']").val(dataItem.ctrl_wLight);
		$("#form-1 input[name='sr_yLight']").val(dataItem.ctrl_yLight);
		$("#lc_ctrl-1").trigger("change");
		$("#form-1 select[name='lc_ctrl-1']").attr("disabled", true);
		$("#form-1 span[name='subtitle']").text(
				subtitle ? subtitle : "${form_edit}");
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
	//진행 , 중지 제어 
	function excAutoCtrl(type) {
		ctrl_state = "";
		var grid = $("#grid").data("kendoGrid");
		key = Object.keys(grid._selectedIds);
		id = key[0];

		if (type == "중지" && id != undefined) {
			ctrl_state = '0';
		} else if (type == "진행" && id != undefined) {
			ctrl_state = '1';

		} else if (id == undefined) {
			showAlertModal("danger", "로컬제어기를 선택해 주세요.");
		}
		data = {
			ctrl_condition : ctrl_state,
			"id" : id
		};

		$.ajax({
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			data : JSON.stringify(data),
			url : "${updateCtrlStateUrl}",
			success : function(lc_data) {

				$("#grid").data("kendoGrid").dataSource.read();
				console.log("성공");
			}
		//error: ajaxReadError
		});

	}
	//로컬제어기 정보 호출
	function get_select_option(type, id) {
		var data = {
			id : id
		};
		$.ajax({
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			data : JSON.stringify(data),
			url : "${selectOpLcUrl}",
			success : function(lc_data) {
				if (type == "add") {
					addForm("추가", lc_data);
				} else if (type == "edit") {
					editForm("변경", lc_data, id);
				}
				$('#form-modal-1 .modal-dialog').draggable({
					handle : '.modal-header'
				});
				$("#form-modal-1").modal();

			}
		//error: ajaxReadError
		});
	}
	//추가,수정 값 전송
	function saveForm() {
		var lc_name_sel = document.getElementById("lc_ctrl-1");
		var lc_name_selected = document.getElementById("lc_ctrl-1").options.selectedIndex;
		var lc_name = lc_name_sel.options[lc_name_selected].text;
		var lc_mac_sel = document.getElementById("lc_ctrl-1");
		var lc_mac_selected = document.getElementById("lc_ctrl-1").options.selectedIndex;
		var lc_mac = lc_mac_sel.options[lc_mac_selected].value;
		var time_sel = document.getElementById("time");
		var time_selected = document.getElementById("time").options.selectedIndex;
		var time= time_sel.options[time_selected].text;
		
		rain1 = $.trim($("#form-1 input[name='rain1']").val());
		rain2 = $.trim($("#form-1 input[name='rain2']").val());
		v_ability1 = $.trim($("#form-1 input[name='v_ability1']").val());
		v_ability2 = $.trim($("#form-1 input[name='v_ability2']").val());
		if(rain1==""){
			rain1 = 0;
		}
		if(rain2==""){
			rain2 = 999;
		}
		if(v_ability1==""){
			v_ability1 = 0;
		}
		if(v_ability2==""){
			v_ability2 = 999;
		}
		var ctrl_data = {
			"id" : Number($("#form-1").attr("rowid")),
			"lc_mac" : lc_mac,
			"lc_name" : lc_name,
			"time" : time,
			rain : rain1 + " ~ " + rain2,
			v_ability : v_ability1 + " ~ " + v_ability2,
			wLight : $.trim($("#form-1 input[name='sr_wLight']").val()),
			yLight : $.trim($("#form-1 input[name='sr_yLight']").val()),
			speed : $.trim($("#form-1 input[name='speed']").val()),
		};
			$.ajax({
				type : "POST",
				contentType : "application/json",
				dataType : "json",
				url : $("#form-1").attr("url"),
				data : JSON.stringify(ctrl_data),
				success : function(data, status) {
					showSaveSuccessMsg();
					$("#form-modal-1").modal("hide");
					$("#grid").data("kendoGrid").dataSource.read();
				},
				error : ajaxSaveError
			});
		

	}
	//전원 값 수정 체크
	function chageCheck(sr_no) {
		var viewMode = $("input[name=sr_power]").is(":checked") ? "E" : "A";
		if (viewMode == "E") {
			checkPower = "1";
		}
		if (viewMode == "A") {
			checkPower = "0";
		}

	}
	//로컬제어기셀렉트 온채인지 이벤트 로컬제어기에 해당하는 제한속도 불러오기 위해서 
	function chageLangSelect() {
		var langSelect = document.getElementById("lc_ctrl-1");

		// select element에서 선택된 option의 value가 저장된다.  
		var selectValue = langSelect.options[langSelect.selectedIndex].value;
		var selectText = langSelect.options[langSelect.selectedIndex].text;
		data = {
			lc_mac : selectValue,
			lc_name : selectText
		};
		$.ajax({
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			data : JSON.stringify(data),
			url : "${selectLcUrl}",
			success : function(sp_data) {
				$("#form-1 input[name='speed']").val(sp_data["speed"]);
				console.log("성공");
			},
		    error: ajaxReadError
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
