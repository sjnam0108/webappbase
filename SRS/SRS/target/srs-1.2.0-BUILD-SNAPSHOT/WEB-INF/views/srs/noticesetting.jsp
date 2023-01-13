<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/srs/noticesetting/create" var="createUrl" />
<c:url value="/srs/noticesetting/read" var="readUrl" />
<c:url value="/srs/noticesetting/destroy" var="destroyUrl" />
<c:url value="/srs/noticesetting/update" var="updateUrl" />
<c:url value="/srs/noticesetting/selectOp" var="selectOpUrl" />
<c:url value="/srs/noticesetting/selectOpLc" var="selectOpLcUrl" />

<!-- Opening tags -->

<common:pageOpening />


<!-- Page title -->

<h4 class="pt-1 pb-3 mb-3">
			<i class="fas fa-bell mr-2"></i>${pageTitle}
</h4>

<hr class="border-light container-m--x mt-0 mb-4">





<!-- Page body -->


<!-- Java(optional)  -->

<%
	String editTemplate = 
			"<button type='button' onclick='edit(#= id #)' class='btn icon-btn btn-sm btn-outline-success borderless'>" + 
			"<span class='fas fa-pencil-alt'></span></button>";
%>


<!-- Kendo grid  -->

<div class="mb-4">
<kendo:grid name="grid" pageable="true" groupable="false" filterable="true" sortable="true" scrollable="false" reorderable="true" resizable="true" selectable="single" >
	<kendo:grid-excel fileName="${pageTitle}.xlsx" allPages="true" proxyURL="/proxySave"/>
    <kendo:grid-pageable refresh="true" buttonCount="5" pageSize="10" pageSizes="${pageSizesNormal}" />
    <kendo:grid-toolbarTemplate>
    	<div class="d-flex align-items-center">
    		<span class="mr-1">
	   			<button id="add-btn" type="button" class="btn btn-outline-success">${cmd_add}</button>
    		</span>
   			<span class="d-none d-lg-inline">
    			<button type="button" class="btn btn-default k-grid-excel">${cmd_excel}</button>
   			</span>
   			<span class="ml-auto">
    			<button id="delete-btn" type="button" class="btn btn-danger">${cmd_delete}</button>
   			</span>
    	</div>
   	</kendo:grid-toolbarTemplate>
	<kendo:grid-columns>
		<kendo:grid-column title="${cmd_edit}" width="50" filterable="false" sortable="false" template="<%= editTemplate %>" />
		<kendo:grid-column title="로컬제어기" field="lc_name" />
		<kendo:grid-column title="정보" field="notice_name" />
		<kendo:grid-column title="등록일시" field="whoCreationDate" />
	</kendo:grid-columns>
	<kendo:grid-filterable>
		<kendo:grid-filterable-messages selectedItemsFormat="${filter_selectedItems}"/>
	</kendo:grid-filterable>
	<kendo:dataSource serverPaging="true" serverSorting="true" serverFiltering="true" error="kendoReadError">
		<kendo:dataSource-sort>
			<kendo:dataSource-sortItem field="id" dir="asc"/>
		</kendo:dataSource-sort>
		<kendo:dataSource-transport>
			<kendo:dataSource-transport-read url="${readUrl}" dataType="json" type="POST" contentType="application/json"/>
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
		//유고상황 DB에서 로컬제어기값이 있는 유고상황 불러오기
		get_select_option();

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
						type: "POST",
						contentType: "application/json",
						dataType: "json",
						url: "${destroyUrl}",
						data: JSON.stringify({ items: delRows }),
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


<!--  Forms -->
<!--  추가 폼 -->
<script id="template-1" type="text/x-kendo-template">

<div class="modal fade" data-backdrop="static" id="form-modal-1">
	<div class="modal-dialog">
		<form class="modal-content" id="form-1" rowid="-1" url="${createUrl}">
      
			<!-- Modal Header -->
			<div class="modal-header move-cursor">
				<h5 class="modal-title">
					안내 설정<span class="font-weight-light pl-1"><span name="subtitle"></span>
				</h5>
			</div>
        
			<!-- Modal body -->
			<div class="modal-body modal-bg-color py-3">
				<div class="form-row">
					<div class="col-sm-12">
						<div class="form-group col">
							<label class="form-label">
								로컬제어기 
								<span class="text-danger">*</span>
							</label>
							<select  name="lc_ctrl"class="form-control">
							</select>
						</div>
					</div>
				</div>
				<div class="form-row">
					<div class="col-sm-12">
						<div class="form-group col">
							<label class="form-label">
								안내정보
								<span class="text-danger">*</span>
							</label>
							<select name="notice"class="form-control">
							</select>
						</div>
					</div>
				</div>
			</div>
        
			<!-- Modal footer -->
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">${form_cancel}</button>
				<button type="button" class="btn btn-primary" onclick='saveFormAdd()'>${form_save}</button>
			</div>
			
		</form>
	</div>
</div>

</script>
<!-- 수정 폼 -->
<script id="template-2" type="text/x-kendo-template">

<div class="modal fade" data-backdrop="static" id="form-modal-2">
	<div class="modal-dialog">
		<form class="modal-content" id="form-2" rowid="-1" url="${createUrl}">
      
			<!-- Modal Header -->
			<div class="modal-header move-cursor">
				<h5 class="modal-title">
					안내 설정<span class="font-weight-light pl-1"><span name="subtitle"></span>
				</h5>
			</div>
        
			<!-- Modal body -->
			<div class="modal-body modal-bg-color py-3">
				<div class="form-row">
					<div class="col-sm-12">
						<div class="form-group col">
							<label class="form-label">
								로컬제어기 
								<span class="text-danger">*</span>
							</label>
							<input readonly name="lc_name" type="text"  maxlength="100" class="form-control required">
						</div>
					</div>
				</div>
				<div class="form-row">
					<div class="col-sm-12">
						<div class="form-group col">
							<label class="form-label">
								안내정보
								<span class="text-danger">*</span>
							</label>
							<select name="notice"class="form-control">
							</select>
						</div>
					</div>
				</div>
			</div>
        
			<!-- Modal footer -->
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">${form_cancel}</button>
				<button type="button" class="btn btn-primary" onclick='saveFormEdit()'>${form_save}</button>
			</div>
			
		</form>
	</div>
</div>

</script>



<!--  Scripts -->

<script>
//변경 버튼 클릭 
function edit(id) {
	var type= "edit";
	get_select_option_edit(id);
}
//추가 폼 값 설정
function addForm(subtitle,notice_op,lc_op) {
	notice_select = "<select name='notice' class='form-control'>";
	if(notice_op != ""){
		for (i=0;i<notice_op.length;i++){
			notice_select += "<option class='form-control'>" +notice_op[i].notice_name +"</option>";
		}
		notice_select += "</select>";
	}
	lc_select = "<select name='lc_ctrl' class='form-control'>";
	if(lc_op != ""){
		for (i=0;i<lc_op.length;i++){
			lc_select += "<option class='form-control'>" +lc_op[i].lc_name +"</option>";
		}
		lc_select += "</select>";
	}
	$("#formRoot").html(kendo.template($("#template-1").html()));
	$("#form-1 select[name='notice']").html(notice_select);
	$("#form-1 select[name='lc_ctrl']").html(lc_select);
	$("#form-1 span[name='subtitle']").text(subtitle ? subtitle : "${form_add}");	
	// set validation
	$("#form-1").validate();
}
//변경 폼 값 수정
function editForm(subtitle,notice_op,id) {
	var dataItem = $("#grid").data("kendoGrid").dataSource.get(id);	
	notice_select = "<select name='notice' class='form-control'>";
	if(notice_op != ""){
		for (i=0;i<notice_op.length;i++){
			if(notice_op[i].notice_name == dataItem.notice_name){
				notice_select += "<option class='form-control' selected>" +notice_op[i].notice_name +"</option>"	
			}
			else{
				notice_select += "<option class='form-control'>" +notice_op[i].notice_name +"</option>"	
			}
			
		}
		notice_select += "</select>"
	}
	$("#formRoot").html(kendo.template($("#template-2").html()));	
	$("#form-2").attr("rowid", dataItem.id);
	$("#form-2").attr("url", "${updateUrl}");
	$("#form-2 input[name='lc_name']").val(dataItem.lc_name);
	$("#form-2 select[name='notice']").html(notice_select);
	$("#form-2 span[name='subtitle']").text(subtitle ? subtitle : "${form_edit}");
	// set validation
	$("#form-2").validate();
}
//추가 전송
function saveFormAdd() {
	if ($("#form-1").valid()) {		
		var data = {
    		id: Number($("#form-1").attr("rowid")),    		
	   		lc_name: $.trim($("#form-1 select[name='lc_ctrl']").val()),
	   		notice_name: $.trim($("#form-1 select[name='notice']").val()),
		};
    	$.ajax({
    		type: "POST",
    		contentType: "application/json",
    		dataType: "json",
    		url: $("#form-1").attr("url"),
    		data: JSON.stringify(data),
    		success: function (data, status) {
				showSaveSuccessMsg();
				$("#form-modal-1").modal("hide");
				$("#grid").data("kendoGrid").dataSource.read();
    		},
			error: ajaxSaveError
    	});
	}
}
//변경 전송
function saveFormEdit() {
	if ($("#form-2").valid()) {		
		var data = {
    		id: Number($("#form-2").attr("rowid")),    		
	   		notice_name: $.trim($("#form-2 select[name='notice']").val()),
		};
    	$.ajax({
    		type: "POST",
    		contentType: "application/json",
    		dataType: "json",
    		url: $("#form-2").attr("url"),
    		data: JSON.stringify(data),
    		success: function (data, status) {
				showSaveSuccessMsg();
				$("#form-modal-2").modal("hide");
				$("#grid").data("kendoGrid").dataSource.read();
    		},
			error: ajaxSaveError
    	});
	}
}
//추가 폼에서 로컬제어기, 유고상황 정보 불러오기
function get_select_option(){
	var data = {id : "111"};
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		data: JSON.stringify(data),
		url: "${selectOpUrl}",
		success: function (no_data) {
			$.ajax({
				type: "POST",
				contentType: "application/json",
				dataType: "json",
				data: JSON.stringify(data),
				url: "${selectOpLcUrl}",
				success: function (lc_data) {
					addForm("추가",no_data,lc_data);	
					$('#form-modal-1 .modal-dialog').draggable({ handle: '.modal-header' });
					$("#form-modal-1").modal();
				}
				//error: ajaxReadError
			});
			console.log( "성공");
		}
		//error: ajaxReadError
	});
}
//변경폼에서 유고상황 정보만 불러오기
function get_select_option_edit(id){
	data = {id : "111"};
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		data: JSON.stringify(data),
		url: "${selectOpUrl}",
		success: function (data) {
			editForm("변경", data,id);		
			console.log( "성공");
			$('#form-modal-2 .modal-dialog').draggable({ handle: '.modal-header' });
			$("#form-modal-2").modal();
		}
		//error: ajaxReadError
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
