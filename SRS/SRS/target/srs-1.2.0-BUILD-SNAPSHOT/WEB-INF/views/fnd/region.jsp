<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/fnd/region/create" var="createUrl" />
<c:url value="/fnd/region/read" var="readUrl" />
<c:url value="/fnd/region/update" var="updateUrl" />
<c:url value="/fnd/region/destroy" var="destroyUrl" />

<c:url value="/dsg/common/readCountryCodes" var="readCmnCountryCodeUrl" />


<!-- Opening tags -->

<common:pageOpening />


<!-- Page title -->

<h4 class="pt-1 pb-3 mb-3">
	<span class="mr-1 ${sessionScope['loginUser'].icon}"></span>
	${pageTitle}
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
<kendo:grid name="grid" pageable="true" filterable="true" sortable="true" scrollable="false" reorderable="true" resizable="true" selectable="${value_gridSelectable}" >
	<kendo:grid-excel fileName="${pageTitle}.xlsx" allPages="true" proxyURL="/proxySave"/>
	<kendo:grid-pageable refresh="true" buttonCount="5" pageSize="10" pageSizes="${pageSizesNormal}" />
	<kendo:grid-toolbarTemplate>
    	<div class="clearfix">
    		<div class="float-left">
    			<button id="add-btn" type="button" class="btn btn-outline-success">${cmd_add}</button>
    			<button type="button" class="btn btn-default d-none d-sm-inline k-grid-excel">${cmd_excel}</button>
    		</div>
    		<div class="float-right">
    			<button id="delete-btn" type="button" class="btn btn-danger">${cmd_delete}</button>
    		</div>
    	</div>
	</kendo:grid-toolbarTemplate>
	<kendo:grid-filterable extra="false" />
	<kendo:grid-columns>
		<kendo:grid-column title="${cmd_edit}" width="50" filterable="false" sortable="false" template="<%= editTemplate %>" />
		<kendo:grid-column title="${title_regionName}" field="regionName" />
		<kendo:grid-column title="${title_regionCode}" field="regionCode" />
		<kendo:grid-column title="${title_coordX}" field="x" />
		<kendo:grid-column title="${title_coordY}" field="y" />
		<kendo:grid-column title="${title_countryCode}" field="countryCode" />
	</kendo:grid-columns>
	<kendo:grid-filterMenuInit>
		<script>
			function grid_filterMenuInit(e) {
				if (e.field == "countryCode") {
					e.container.find("div.k-filter-help-text").text("${grid_customFilterInfoSelector}");
					e.container.find("span.k-dropdown:first").css("display", "none");
	    				
					e.container.find(".k-textbox:first")
						.removeClass("k-textbox")
						.kendoDropDownList({
							dataSource: {
								transport: {
									read: {
										dataType: "json",
										url: "${readCmnCountryCodeUrl}",
										type: "POST",
										contentType: "application/json"
									},
									parameterMap: function (options) {
										return JSON.stringify({ });
									},
								},
								error: function(e) {
									showReadErrorMsg();
								}
							},
							optionLabel: {
								text: "${grid_selectValue}", value: "",
							},
							dataTextField: "text",
							dataValueField: "value",
						});
				}
			}
		</script>
	</kendo:grid-filterMenuInit>
	<kendo:dataSource serverPaging="true" serverSorting="true" serverFiltering="true" serverGrouping="true" error="kendoReadError">
		<kendo:dataSource-sort>
			<kendo:dataSource-sortItem field="countryCode" dir="asc"/>
			<kendo:dataSource-sortItem field="regionName" dir="asc"/>
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
			<kendo:dataSource-schema-model id="id" />
		</kendo:dataSource-schema>
	</kendo:dataSource>
</kendo:grid>
</div>

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

<script id="template-1" type="text/x-kendo-template">

<div class="modal fade" data-backdrop="static" id="form-modal-1">
	<div class="modal-dialog modal-sm">
		<form class="modal-content" id="form-1" rowid="-1" url="${createUrl}">
      
			<!-- Modal Header -->
			<div class="modal-header move-cursor">
				<h5 class="modal-title">
					${label_region}
					<span class="font-weight-light pl-1"><span name="subtitle"></span>
				</h5>
				<button type="button" class="close" data-dismiss="modal">×</button>
			</div>
        
			<!-- Modal body -->
			<div class="modal-body modal-bg-color">
				<div class="form-group col">
					<label class="form-label">
						${title_regionName}
						<span class="text-danger">*</span>
					</label>
					<input name="regionName" type="text" maxlength="50" class="form-control required">
				</div>
				<div class="form-group col">
					<label class="form-label">
						${title_regionCode}
						<span class="text-danger">*</span>
					</label>
					<input name="regionCode" type="text" maxlength="15" class="form-control required">
				</div>
				<div class="form-group col">
					<label class="form-label">
						${title_coordX}
					</label>
					<input name="x" type="text" maxlength="20" class="form-control">
				</div>
				<div class="form-group col">
					<label class="form-label">
						${title_coordY}
					</label>
					<input name="y" type="text" maxlength="20" class="form-control">
				</div>
				<div class="form-group col">
					<label class="form-label">
						${title_countryCode}
						<span class="text-danger">*</span>
					</label>
					<select name="countryCode" class="selectpicker bg-white" data-style="btn-default" data-none-selected-text="">
<c:forEach var="item" items="${CountryCodes}">
						<option value="${item.value}">${item.text}</option>
</c:forEach>
					</select>
				</div>
			</div>
        
			<!-- Modal footer -->
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">${form_cancel}</button>
				<button type="button" class="btn btn-primary" onclick='saveForm1()'>${form_save}</button>
			</div>
			
		</form>
	</div>
</div>

</script>

<!--  / Forms -->


<!--  Scripts -->

<script>

function initForm1(subtitle) {
	
	$("#formRoot").html(kendo.template($("#template-1").html()));
	
	$("#form-1 select[name='countryCode']").selectpicker('render');

	bootstrapSelectVal($("#form-1 select[name='countryCode']"), "${value_defaultCountryCode}");

	$("#form-1 span[name='subtitle']").text(subtitle ? subtitle : "${form_add}");
	
	$("#form-1").validate({
		rules: {
			regionName: {
				minlength: 2, maxlength: 50,
			},
			regionCode: {
				minlength: 3, maxlength: 15, alphanumeric: true,
			},
			x: {
				maxlength: 20, number: true,
			},
			y: {
				maxlength: 20, number: true,
			},
			countryCode: {
				required: true,
			}
		}
	});
}


function saveForm1() {

	if ($("#form-1").valid()) {
    	var data = {
    		id: Number($("#form-1").attr("rowid")),
    		regionName: $.trim($("#form-1 input[name='regionName']").val()),
    		regionCode: $.trim($("#form-1 input[name='regionCode']").val()),
    		x: $.trim($("#form-1 input[name='x']").val()),
    		y: $.trim($("#form-1 input[name='y']").val()),
    		countryCode: $("#form-1 select[name='countryCode']").val(),
    	};
    	
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: $("#form-1").attr("url"),
			data: JSON.stringify(data),
			success: function (form) {
				showSaveSuccessMsg();
				$("#form-modal-1").modal("hide");
				$("#grid").data("kendoGrid").dataSource.read();
			},
			error: ajaxSaveError
		});
	}
}


function edit(id) {
	
	initForm1("${form_edit}");

	var dataItem = $("#grid").data("kendoGrid").dataSource.get(id);
	
	$("#form-1").attr("rowid", dataItem.id);
	$("#form-1").attr("url", "${updateUrl}");
	
	$("#form-1 input[name='regionName']").val(dataItem.regionName);
	$("#form-1 input[name='regionCode']").val(dataItem.regionCode);
	$("#form-1 input[name='x']").val(dataItem.x);
	$("#form-1 input[name='y']").val(dataItem.y);

	bootstrapSelectVal($("#form-1 select[name='countryCode']"), dataItem.countryCode);

	
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
