<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/srs/noticeenroll/create" var="createUrl" />
<c:url value="/srs/noticeenroll/read" var="readUrl" />
<c:url value="/srs/noticeenroll/destroy" var="destroyUrl" />
<c:url value="/srs/noticeenroll/update" var="updateUrl" />

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

	String regDateTemplate = kr.co.bbmc.utils.Util.getSmartDate();
%>
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
		<kendo:grid-column title="이름" field="notice_name" />
		<kendo:grid-column title="내용" field="notice_content" />
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
		initForm1();		
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
<!-- 추가/수정 폼 -->
<script id="template-1" type="text/x-kendo-template">

<div class="modal fade" data-backdrop="static" id="form-modal-1">
	<div class="modal-dialog">
		<form class="modal-content" id="form-1" rowid="-1" url="${createUrl}">
      
			<!-- Modal Header -->
			<div class="modal-header move-cursor">
				<h5 class="modal-title">
					유고상황 정보등록
				</h5>
			</div>
        
			<!-- Modal body -->
			<div class="modal-body modal-bg-color py-3">
				<div class="form-row">
					<div class="col-sm-12">
						<div class="form-group col">
							<label class="form-label">
								이름
								<span class="text-danger">*</span>
							</label>
							<input name="notice_name" type="text"  maxlength="100" class="form-control required">
						</div>
					</div>
				</div>
				<div class="form-row">
					<div class="col-sm-12">
						<div class="form-group col">
							<label class="form-label">
								내용
								<span class="text-danger">*</span>
							</label>
							<textarea name="notice_content" rows="3" class="form-control required" maxlength="1000"></textarea>
						</div>
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
//추가, 수정 폼 정보 적용 
function initForm1(subtitle) {
	$("#formRoot").html(kendo.template($("#template-1").html()));
	// set validation
	$("#form-1").validate();
}
//추가,수정 저장 
function saveForm() {	
	if ($("#form-1").valid()) {
		var data = {
    		id: Number($("#form-1").attr("rowid")),    		
    		notice_name: $.trim($("#form-1 input[name='notice_name']").val()),
	   		notice_content: $.trim($("#form-1 textarea[name='notice_content']").val()),
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
//수정버튼 클릭 이벤트
function edit(id) {
	initForm1("${form_edit}");
	var dataItem = $("#grid").data("kendoGrid").dataSource.get(id);
	$("#form-1").attr("rowid", dataItem.id);
	$("#form-1").attr("url", "${updateUrl}");
	$("#form-1 input[name='notice_name']").val(dataItem.notice_name);
	$("#form-1 textarea[name='notice_content']").val(dataItem.notice_content);	
	$('#form-modal-1 .modal-dialog').draggable({ handle: '.modal-header' });
	$("#form-modal-1").modal();
}






</script>

<!--  / Scripts -->


<!-- / Page body -->





<!-- Functional tags -->

<func:cmmValidate />


<!-- Closing tags -->

<common:base />
<common:pageClosing />
