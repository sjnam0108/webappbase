<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/srs/freezpredic/create" var="createUrl" />
<c:url value="/srs/freezpredic/read" var="readUrl" />
<c:url value="/srs/freezpredic/destroy" var="destroyUrl" />
<c:url value="/srs/freezpredic/update" var="updateUrl" />
<c:url value="/srs/freezpredic/selectOp" var="selectOpUrl" />
<c:url value="/srs/freezpredic/selectOpLc" var="selectOpLcUrl" />

<!-- Opening tags -->

<common:pageOpening />


<!-- Page title -->

<h4 class="pt-1 pb-3 mb-3">
	<i class="fas fa-bell mr-2"></i> ${pageTitle}
</h4>

<hr class="border-light container-m--x mt-0 mb-4">





<!-- Page body -->


<!-- Java(optional)  -->

<%
String editTemplate = "<button type='button' onclick='edit(#= id #)' class='btn icon-btn btn-sm btn-outline-success borderless'>"
		+ "<span class='fas fa-pencil-alt'></span></button>";
%>

<!-- Kendo grid  -->

<div class="mb-4">
	<kendo:grid name="grid" pageable="true" groupable="false"
		filterable="true" sortable="true" scrollable="false"
		reorderable="true" resizable="true" selectable="multiple, row">
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
			<kendo:grid-column title="로컬제어기" field="lc_name" />
			<kendo:grid-column title="대기온도(℃ 이하)" field="air_temp" />
			<kendo:grid-column title="노면온도(℃ 이하)" field="road_temp" />
			<kendo:grid-column title="풍속(m/s 이하)" field="win_speed" />
			<kendo:grid-column title="대기습도(% 이상)" field="air_humid" />
		</kendo:grid-columns>
		<kendo:grid-filterable>
			<kendo:grid-filterable-messages
				selectedItemsFormat="${filter_selectedItems}" />
		</kendo:grid-filterable>
		<kendo:dataSource serverPaging="true" serverSorting="true"
			serverFiltering="true" error="kendoReadError">
			<kendo:dataSource-sort>
				<kendo:dataSource-sortItem field="id" dir="asc" />
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

/* Kendo 그리드에서 bootstrap dropdownmenu 모두 보이게 */
.k-grid td {
	overflow: visible;
}
</style>

<!-- / Kendo grid  -->


<!-- Grid button actions  -->

<script>
	$(document).ready(function() {
		// Add
		$("#add-btn").click(function(e) {
			e.preventDefault();
			//셀렉트 로컬제어기 목로고 불러오기
			get_select_option();
		});
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
<!-- 추가 폼 -->
<script id="template-1" type="text/x-kendo-template">

<div class="modal fade" data-backdrop="static" id="form-modal-1">
	<div class="modal-dialog" style="max-width:100rem">
		<form class="modal-content" id="form-1" rowid="-1" url="${createUrl}">
      
			<!-- Modal Header -->
			<div class="modal-header move-cursor">
				<h5 class="modal-title">
					결빙예측<span class="font-weight-light pl-1"><span name="subtitle"></span>
				</h5>
			</div>
        
			<!-- Modal body -->
			<div class="modal-body modal-bg-color py-3">
				<div class="form-row">

					<div class="card col-md-2 col-sm-2 col-2 col-lg-2 col-xl-2">
							<ul class=" d-flex align-items-center justify-content-center list-group list-group-flush">
								<li class="list-group-item">
									로컬제어기 
								</li>
								<li class="list-group-item">
									<select  id = "lc_ctrl" name="lc_ctrl"class="form-control">
									</select>
								</li>
							</ul>
					</div>
					<div class="card col-md-2 col-sm-2 col-2 col-lg-2 col-xl-2" >
						<ul class=" d-flex align-items-center justify-content-center list-group list-group-flush">
							<li class="list-group-item">
								대기온도(℃ 이하)
							</li>
							<li class="list-group-item">
								<input type="text" name="air_temp" class="form-control">
							</li>
						</ul>
					</div>
					<div class="card col-md-2 col-sm-2 col-2 col-lg-2 col-xl-2" >
						<ul class=" d-flex align-items-center justify-content-center list-group list-group-flush">
							<li class="list-group-item">
								노면온도(℃ 이하)
							</li>
							<li class="list-group-item">
								<input type="text" name="road_temp" class="form-control">
							</li>
						</ul>
					</div>
					<div class="card col-md-2 col-sm-2 col-2 col-lg-2 col-xl-2" >
						<ul class=" d-flex align-items-center justify-content-center list-group list-group-flush">
							<li class="list-group-item">
								풍속(m/s 이하)
							</li>
							<li class="list-group-item">
								<input type="text" name="winter_speed" class="form-control">
							</li>
						</ul>
					</div>
					<div class="card col-md-2 col-sm-2 col-2 col-lg-2 col-xl-2" >
						<ul class=" d-flex align-items-center justify-content-center list-group list-group-flush">
							<li class="list-group-item">
								대기습도(% 이상)
							</li>
							<li class="list-group-item">
								<input type="text" name="air_humid" class="form-control">
							</li>
						</ul>
					</div>
				</div>
			</div>
        
			<!-- Modal footer -->
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">${form_cancel}</button>
				<button type="button" class="btn btn-primary" onclick='saveForm()'>${form_save}</button>
			</div>
			
		</form>
	</div>
</div>

</script>
<!-- 수정 폼 -->
<script id="template-2" type="text/x-kendo-template">

<div class="modal fade" data-backdrop="static" id="form-modal-2">
	<div class="modal-dialog" style="max-width:100rem">
		<form class="modal-content" id="form-2" rowid="-1" url="${createUrl}">
      
			<!-- Modal Header -->
			<div class="modal-header move-cursor">
				<h5 class="modal-title">
					결빙예측<span class="font-weight-light pl-1"><span name="subtitle"></span>
				</h5>
			</div>
        
			<!-- Modal body -->
			<div class="modal-body modal-bg-color py-3">
				<div class="form-row">
					<div class="card col-md-2 col-sm-2 col-2 col-lg-2 col-xl-2">
							<ul class=" d-flex align-items-center justify-content-center list-group list-group-flush">
								<li class="list-group-item">
									로컬제어기 
								</li>
								<li class="list-group-item">
									<input readonly id="lc_name" name="lc_name" type="text"  maxlength="100" class="form-control required">
								</li>
							</ul>
						
					</div>
					<div class="card col-md-2 col-sm-2 col-2 col-lg-2 col-xl-2" >
						<ul class=" d-flex align-items-center justify-content-center list-group list-group-flush">
							<li class="list-group-item">
								대기온도(℃ 이하)
							</li>
							<li class="list-group-item">
								<input type="text" name="air_temp" class="form-control">
							</li>
						</ul>
					</div>
					<div class="card col-md-2 col-sm-2 col-2 col-lg-2 col-xl-2" >
						<ul class=" d-flex align-items-center justify-content-center list-group list-group-flush">
							<li class="list-group-item">
								노면온도(℃ 이하)
							</li>
							<li class="list-group-item">
								<input type="text" name="road_temp" class="form-control">
							</li>
						</ul>
					</div>
					<div class="card col-md-2 col-sm-2 col-2 col-lg-2 col-xl-2" >
						<ul class=" d-flex align-items-center justify-content-center list-group list-group-flush">
							<li class="list-group-item">
								풍속(m/s 이하)
							</li>
							<li class="list-group-item">
								<input type="text" name="winter_speed" class="form-control">
							</li>
						</ul>
					</div>
					<div class="card col-md-2 col-sm-2 col-2 col-lg-2 col-xl-2" >
						<ul class=" d-flex align-items-center justify-content-center list-group list-group-flush">
							<li class="list-group-item">
								대기습도(% 이상)
							</li>
							<li class="list-group-item">
								<input type="text" name="air_humid" class="form-control">
							</li>
						</ul>
					</div>
				</div>
			</div>        
			<!-- Modal footer -->
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">${form_cancel}</button>
				<button type="button" class="btn btn-primary" onclick='saveForm()'>${form_save}</button>
			</div>
			
		</form>
	</div>
</div>

</script>



<!--  Scripts -->

<script>
	//수정버튼 이벤트
	function edit(id) {
		editForm("변경", id);
		console.log("성공");
		$('#form-modal-2 .modal-dialog').draggable({
			handle : '.modal-header'
		});
		$("#form-modal-2").modal();
	}
	//추가 폼 내용 설정
	function addForm(subtitle, lc_op) {
		lc_select = "<select name='lc_ctrl' class='form-control'>";
		if (lc_op != "") {
			for (i = 0; i < lc_op.length; i++) {
				lc_select += "<option class='form-control' value='"+lc_op[i].lc_mac +"'>"
						+ lc_op[i].lc_name + "</option>";
			}
			lc_select += "</select>";
		}
		$("#formRoot").html(kendo.template($("#template-1").html()));
		$("#form-1 select[name='lc_ctrl']").html(lc_select);
		$("#form-1 span[name='subtitle']").text(
				subtitle ? subtitle : "${form_add}");
		// set validation
		$("#form-1").validate();
	}
	//로컬제어기 셀렉트 
	function get_select_option() {
		var data = {
			id : "111"
		};
		$.ajax({
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			data : JSON.stringify(data),
			url : "${selectOpUrl}",
			success : function(no_data) {
				$.ajax({
					type : "POST",
					contentType : "application/json",
					dataType : "json",
					data : JSON.stringify(data),
					url : "${selectOpLcUrl}",
					success : function(lc_data) {
						addForm("추가", lc_data);
						$('#form-modal-1 .modal-dialog').draggable({
							handle : '.modal-header'
						});
						$("#form-modal-1").modal();
					}
				//error: ajaxReadError
				});
				console.log("성공");
			}
		//error: ajaxReadError
		});
	}
	//수정 폼 내용 설정
	function editForm(subtitle, id) {
		var dataItem = $("#grid").data("kendoGrid").dataSource.get(id);
		freez_select = "<select name='freez_predic' id = 'freez_predic' class='form-control'>";
		freez_list = [ "결빙", "비결빙" ];
		if (freez_list != "") {
			for (i = 0; i < freez_list.length; i++) {
				if (freez_list[i] == dataItem.freez_predic) {
					freez_select += "<option class='form-control' selected>"
							+ freez_list[i] + "</option>"
				} else {
					freez_select += "<option class='form-control'>"
							+ freez_list[i] + "</option>"
				}
			}
			freez_select += "</select>"
		}
		$("#formRoot").html(kendo.template($("#template-2").html()));
		$("#form-2").attr("rowid", dataItem.id);
		$("#form-2").attr("url", "${updateUrl}");
		$("#form-2 select[name='freez_predic']").html(freez_select);
		$("#form-2 input[name='lc_name']").val(dataItem.lc_name);
		$("#form-2 input[name='air_temp']").val(dataItem.air_temp);
		$("#form-2 input[name='air_humid']").val(dataItem.air_humid);
		$("#form-2 input[name='rain']").val(dataItem.rain);
		$("#form-2 input[name='snow']").val(dataItem.snow);
		$("#form-2 input[name='winter_speed']").val(dataItem.win_speed);
		$("#form-2 input[name='road_temp']").val(dataItem.road_temp);
		$("#form-2 span[name='subtitle']").text(
				subtitle ? subtitle : "${form_edit}");
		// set validation
		$("#form-2").validate();
	}
	//저장
	function saveForm() {
		//추가저장
		if (document.getElementById("lc_ctrl") != null) {
			var lc_name_sel = document.getElementById("lc_ctrl");
			var lc_name_selected = document.getElementById("lc_ctrl").options.selectedIndex;
			var lc_name = lc_name_sel.options[lc_name_selected].text;
			var lc_mac_sel = document.getElementById("lc_ctrl");
			var lc_mac_selected = document.getElementById("lc_ctrl").options.selectedIndex;
			var lc_mac = lc_mac_sel.options[lc_mac_selected].value;
			var data = {
				"id" : Number($("#form-1").attr("rowid")),
				"lc_name" : lc_name,
				"lc_mac" : lc_mac,
				"air_temp" : $.trim($("#form-1 input[name='air_temp']").val()),
				"air_humid" : $
						.trim($("#form-1 input[name='air_humid']").val()),
				"winter_speed" : $.trim($("#form-1 input[name='winter_speed']")
						.val()),
				"road_temp" : $
						.trim($("#form-1 input[name='road_temp']").val()),
			};
			$.ajax({
				type : "POST",
				contentType : "application/json",
				dataType : "json",
				url : $("#form-1").attr("url"),
				data : JSON.stringify(data),
				success : function(data) {
					showSaveSuccessMsg();
					$("#form-modal-1").modal("hide");
					$("#grid").data("kendoGrid").dataSource.read();
				},
				error : ajaxSaveError
			});	
		}//수정 저장 
		else {
			var edit_data = {
				"id" : $("#form-2").attr("rowid"),
				"air_temp" : $.trim($("#form-2 input[name='air_temp']").val()),
				"air_humid" : $
						.trim($("#form-2 input[name='air_humid']").val()),
				"rain" : $.trim($("#form-2 input[name='rain']").val()),
				"snow" : $.trim($("#form-2 input[name='snow']").val()),
				"winter_speed" : $.trim($("#form-2 input[name='winter_speed']")
						.val()),
				"road_temp" : $
						.trim($("#form-2 input[name='road_temp']").val())
			};
			$.ajax({
				type : "POST",
				contentType : "application/json",
				dataType : "json",
				url : $("#form-2").attr("url"),
				data : JSON.stringify(edit_data),
				success : function(data) {
					showSaveSuccessMsg();
					$("#form-modal-2").modal("hide");
					$("#grid").data("kendoGrid").dataSource.read();
				},
				error : ajaxSaveError
			});
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
