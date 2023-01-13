<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>

<!-- URL -->
<c:url value="/srs/localctrl/countstate" var="cntStateUrl" />
<c:url value="/srs/datechart/read" var="readUrl" />
<c:url value="/srs/datechart/destroy" var="destroyUrl" />
<c:url value="/srs/datechart/getStateDay" var="getStateDayUrl" />
<c:url value="/srs/datechart/readTypes" var="readTypeUrl" />

<!-- Opening tags -->

<common:pageOpening />

<!-- Page title -->

<h4 class="mt-3 pt-1 pb-3 mb-3">
	<span class="mr-1 fas fa-chart-line"></span> ${pageTitle}
</h4>

<hr class="border-light container-m--x mt-0 mb-4">


<div class="mb-4">
	<img src="/resources/shared/images/logo/blue.png"> <span
		class="mr-4 blue" id="blue"></span> <img
		src="/resources/shared/images/logo/yellow.png"> <span
		class="mr-4 yellow" id="yellow"></span> <img
		src="/resources/shared/images/logo/orange.png"> <span
		class="mr-4 orange" id="orange"></span> <img
		src="/resources/shared/images/logo/red.png"> <span
		class="mr-4 red" id="red"></span> <img
		src="/resources/shared/images/logo/gray.png"> <span
		class="mr-1 gray" id="gray"></span> <span>실시간 (10초 주기)</span>
</div>

<!-- Page body -->


<div class="mb-4">
	<div class="row">
		<div class="card col-md-6 col-sm-6 col-6 col-lg-6 col-xl-6">
			<ul class="list-group list-group-flush">
				<li class="list-group-item" style="padding: 21px">시작일</li>
				<li class="list-group-item">
					<div id="example-1">
						<input id="start_date" name="start_date" value="" type="date"
							class="form-control">
					</div>
				</li>
			</ul>
		</div>
		<div class="card col-md-6 col-sm-6 col-6 col-lg-6 col-xl-6">
			<ul class="list-group list-group-flush">
				<li class="list-group-item">
					<div class="float-left"
						style="margin-top: 12px; margin-bottom: 10px;">
						<span>종료일</span>
					</div>
					<div class="float-right">
						<button type="button" onclick="reControl()" class="form-control">검색
						</button>
					</div>
					<div class="float-right mr-4">
						<select class="form-control" id="type">
							<option>DAY</option>
							<option selected>MONTH</option>
							<option>YEAR</option>
						</select>
					</div>
				</li>
				<li class="list-group-item">
					<div id="example-1">
						<input id="end_date" name="end_date" type="date" value=""
							class="form-control">
					</div>
				</li>
			</ul>
		</div>
		<div class='card col-md-4 col-sm-4 col-4 col-lg-4 col-xl-4'>
			<h3 class="mt-4">
				<span class="ml-2 mr-2 fas fa-chart-line"></span>
			</h3>
			<h4>
				<span name="deviceCnt"></span>
			</h4>
		</div>
		<div class='card col-md-4 col-sm-4 col-4 col-lg-4 col-xl-4'>
			<h3 class="mt-4">
				<span class="ml-2 mr-2 fas fa-clock"></span>
			</h3>
			<h4>
				<span name="avgLcNoraml"></span>
			</h4>
		</div>
		<div class='card col-md-4 col-sm-4 col-4 col-lg-4 col-xl-4'>
			<h3 class="mt-4">
				<span class="ml-2 mr-2 fas fa-desktop"></span>
			</h3>
			<h4>
				<span name="avgCntDvice"></span>
			</h4>
		</div>
		<div class="mt-2 card col-md-12 col-sm-12 col-12 col-lg-12 col-xl-12">
			<div>
				<div id="date_chart"></div>
			</div>
		</div>
	</div>


<!-- / Page body -->

<script>
	$(document).ready(function() {
		//최초 로컬 기기 상태 불러오기
		get_count_state();
		//10초주기로 로컬 기기 상태 불러오기
		setInterval(get_count_state, 10000);
		const today = new Date();
		var i = 0;
		// "2020. 1 1."
		const year = today.getFullYear(); // 년
		const month = today.getMonth(); // 월
		const day = today.getDate(); // 일
		// 어제 날짜 구하기
		var end = new Date(
				+new Date(year, month, day) + 3240 * 10000)
				.toISOString().split("T")[0];	
		var start = new Date(
				+new Date(year - 1, month, day) + 3240 * 10000)
				.toISOString().split("T")[0];
		var type = "MONTH";
		//차트 데이터 불러오기
		get_data_chart(start, end, type)
		$('#start_date').val(start);
		$('#end_date').val(end);
	});
	//검색 이벤트
	function reControl() {
		start = $('#start_date').val();
		end = $('#end_date').val();
		type = $('#type').val();
		const start_date = to_date(start); 
		const end_date = to_date(end);
		const elapsedMSec = end_date.getTime() - start_date.getTime(); // 172800000
		const elapsedDay = elapsedMSec / 1000 / 60 / 60 / 24; // 2
		if(elapsedDay > 31 && type =="DAY"){
			showAlertModal("danger", "DAY 타입으로 한달이상을 선택할 수 없습니다.");
		}
		else{
			//차트 데이터 불러오기		
			get_data_chart(start, end, type)	
		}
	}
	//string to date
	function to_date(date_str){
	    var yyyyMMdd = String(date_str);
	    var sYear = yyyyMMdd.substring(0,4);
	    var sMonth = yyyyMMdd.substring(5,7);
	    var sDate = yyyyMMdd.substring(8,10);

	    //alert("sYear :"+sYear +"   sMonth :"+sMonth + "   sDate :"+sDate);
	    return new Date(Number(sYear), Number(sMonth)-1, Number(sDate));
	}
	
	
	//차트 데이터 불러오기
	function get_data_chart(start, end, type) {
		data = {
			"type" : type,
			"start" : start,
			"end" : end
		}
		$.ajax({
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			data : JSON.stringify(data),
			url : "${getStateDayUrl}",
			success : function(data) {
				//차트 그리기
				createChart(data);
				todayData = data["todayData"];
				$("span[name='deviceCnt']").text(
						"총 기기 수 : " + todayData["deviceCnt"]);
				$("span[name='avgLcNoraml']").text(
						"평균 운행시간 (분) : " + todayData["avgLcNoraml"]);
				$("span[name='avgCntDvice']").text(
						"평균 운행제어기 : " + todayData["avgLcNoCnt"]);
				console.log("성공");
			}
			//error: ajaxReadError
		});
	}
	//로컬제어기 상태 불러오기
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
	//차트 그리기
	function createChart(data) {
		sumDic = data["sum"];
		avgDic = data["avg"];
		sumList = [];
		avgList = [];
		date = [];
		i = 0
		result = [];
		for (key in sumDic) {
			val = {
				"date" : key,
				"sum" : sumDic[key],
				"avg" : avgDic[key]
			}
			result.push(val)
		}
		//정렬
		result.sort(function(a, b) {
			return a.date < b.date ? -1 : a.date > b.date ? 1 : 0;
		});
		for (i = 0; i < result.length; i++) {
			avgList[i] = result[i]["avg"]
			sumList[i] = result[i]["sum"]
			date[i] = result[i]["date"]
		}
		$("#date_chart").kendoChart({
			title : {
				text : "Gross domestic"
			},
			legend : {
				position : "bottom"
			},
			series : [ {
				type : "area",
				name : "운행 제어기 수",
				data : sumList
			}, {
				type : "line",
				name : "평균 운행시간(분)",
				data : avgList,
				axis : "axis",
			} ],
			valueAxis : [ {
				title : {
					text : "운행 제어기 수"
				},
				line : {
					visible : false
				}
				}, {
				name : "axis",
				title : {
					text : "평균 운행시간(분)"
				},
				line : {
					visible : false
				}
			} ],
			categoryAxis : {
				categories : date,
				majorGridLines : {
					visible : false
				},
			     labels: {
			          rotation: "auto"
			        },
				axisCrossingValue : [ 0, 1000 ],
			},
			tooltip : {
				visible : true,
			}
		});
	}
	//로컬제어기 카운트 체크
	function setCntState(data) {
		blue = ""
		yellow = ""
		orange = ""
		red = ""
		gray = ""
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
		$(".blue").html(blue);
		$(".yellow").html(yellow);
		$(".orange").html(orange);
		$(".red").html(red);
		$(".gray").html(gray);
	}
</script>



<!-- Functional tags -->
<func:cmmValidate />
<!-- Closing tags -->
<common:base />
<common:pageClosing />