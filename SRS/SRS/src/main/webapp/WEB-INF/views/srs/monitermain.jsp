<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>




<!-- URL -->
<c:url value="/srs/localctrl/countstate" var="cntStateUrl" />

<c:url value="/srs/monitermain/getStateDay" var="getStateDayUrl" />


<!-- Opening tags -->

<common:pageOpening />


<!-- Page title -->

<h4 class="pt-1 pb-3 mb-3">
	<span class="mr-1 fas fa-desktop"></span>
	${pageTitle}
</h4>

<hr class="border-light container-m--x mt-0 mb-4">


 <div class="mb-4">
<img src="/resources/shared/images/logo/blue.png">
<span class="mr-4 blue" id="blue"></span>
<img src="/resources/shared/images/logo/yellow.png">
<span class="mr-4 yellow" id="yellow"></span>
<img src="/resources/shared/images/logo/orange.png">
<span class="mr-4 orange" id="orange"></span>
<img src="/resources/shared/images/logo/red.png">
<span class="mr-4 red" id="red"></span>
<img src="/resources/shared/images/logo/gray.png">
<span class="mr-4 pr-2 gray" id="gray"></span>
<span>실시간 (10초 주기)</span>
</div>

<!-- Page body -->


<!-- Java(optional)  -->

<div class="mb-4">
	<div class="row">
		<div class='card col-md-2 col-sm-2 col-2 col-lg-2 col-xl-2'>
			<h4 class="card-title mt-1 " style="display:flex;align-items:center"><img src="/resources/shared/images/logo/blue.png" style="margin-right:5%">정상 : <span class=" blue" ></span></h4>
			<h6 mb-1>모든 로컬제어기의 상태가 “정상” 인 수량</h6>

		</div>
		<div class='card col-md-2 col-sm-2 col-2 col-lg-2 col-xl-2'>
			<h4 class="card-title mt-1" style="display:flex;align-items:center"><img src="/resources/shared/images/logo/yellow.png" style="margin-right:5%">일부꺼짐 : <span class="mr-1 yellow"></span></h4>
			<h6 mb-1>로컬 제어기의 상태가 “SR 일부꺼짐” 인 제어기 수량</h6>

		</div>
		<div class='card col-md-2 col-sm-2 col-2 col-lg-2 col-xl-2'>
			<h4 class="card-title mt-1" style="display:flex;align-items:center"><img src="/resources/shared/images/logo/orange.png"style="margin-right:5%">모두꺼짐 : <span class="mr-1 orange" ></span></h4>
			<h6 mb-1>로컬 제어기의 상태가 “SR 모두꺼짐” 인 제어기 수량</h6>

		</div>
		<div class='card col-md-2 col-sm-2 col-2 col-lg-2 col-xl-2'>
			<h4 class="card-title mt-1" style="display:flex;align-items:center"><img src="/resources/shared/images/logo/red.png" style="margin-right:5%">제어기꺼짐 : <span class="mr-1 red" id="red"></span></h4>
			<h6 mb-1>로컬제어기 상태가 “제어기 꺼짐” 인 제어기 수량</h6>
	
		</div>
		<div class='card col-md-2 col-sm-2 col-2 col-lg-2 col-xl-2'>
			<h4 class="card-title mt-1"  style="display:flex;align-items:center"><img src="/resources/shared/images/logo/gray.png" style="margin-right:5%">미확인 : <span class="mr-1 gray" id="gray"></span></h4>
			<h6 mb-1>로컬제어기 상태가 “미확인” 인 제어기 수량</h6>

		</div>
	<div class=" card col-md-6 col-sm-6 col-6 col-lg-6 col-xl-6" >
		<ul class="list-group list-group-flush">
			<li class="list-group-item">
				주요 관심 지표
			</li>
			<li class="list-group-item">
				<div id="example-1">
        			<div id="chart_info" class="small-chart">
        			</div>
    			</div>
			</li>
		</ul>
	</div>
	<div class=" card col-md-6 col-sm-6 col-6 col-lg-6 col-xl-6" >
		<ul class="list-group list-group-flush">
			<li class="list-group-item">
				주요 기기 상태
			</li>
			<li class="list-group-item">
				<div id="example">
        			<div id="chart_state" class="small-chart">
        			</div>
    			</div>
    		</li>
		</ul>
	</div>
	<div class=" card col-md-6 col-sm-6 col-6 col-lg-6 col-xl-6" >
		<ul class="list-group list-group-flush">
			<li class="list-group-item">
				총 운영기기
			</li>
			<li class="list-group-item">
				<div id="total">
        			<div id="total_device" class="small-chart">
        			</div>
    			</div>
			</li>
		</ul>
	</div>
	<div class=" card col-md-6 col-sm-6 col-6 col-lg-6 col-xl-6" >
		<ul class="list-group list-group-flush">
			<li class="list-group-item">
				평균 운행시간 
			</li>
			<li class="list-group-item">
				<div id="avg_time">
        			<div id="avg_use_time" class="small-chart">
        			</div>
    			</div>
    		</li>
		</ul>
	</div>
</div>
<style>
.k-chart.small-chart {
            display: inline-block;
            width: 100%;
            height: 200px;
        }
</style>

<!-- Grid button actions  -->

<script>
	$(document).ready(function() {
		//최초 로컬 기기 상태 불러오기
		get_count_state();
		//10초주기로 로컬 기기 상태 불러오기
		setInterval(get_count_state, 10000);
		get_state_day();

	});
	
//로컬제어기 상태별 갯수 불러오기 이벤트
function get_count_state(){
	data = {id : "111"};
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		data: JSON.stringify(data),
		url: "${cntStateUrl}",
		success: function (data) {
			setCntState(data);
			createChart(data);

			console.log( "성공");
		}
		//error: ajaxReadError
	});
}
//로컬제어기 상태별 갯수 카운트 이벤트
function setCntState(data){
	blue = ""
	yellow =""
	orange = ""
	red = ""
	gray = ""
	
	for(i=0;i<data.length;i++){
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
	
	if (blue == ""){
		blue = "0"	
	}
	if (yellow == ""){
		yellow = "0"	
	}
	if (orange == ""){
		orange = "0"	
	}
	if (red == ""){
		red = "0"	
	}
	if (gray == ""){
		gray = "0"	
	}
	$(".blue").html( blue );
	$(".yellow").html( yellow );
	$(".orange").html( orange );
	$(".red").html( red );
	$(".gray").html( gray );
}
//로컬제어기 시간별 상태정보 불러오기
function get_state_day(){
	const today = new Date();
	var i=1;
	// "2020. 1 1."
	const year = today.getFullYear(); // 년
	const month = today.getMonth();   // 월
	const day = today.getDate();      // 일
	// 어제 날짜 구하기
	var dayM = [];
	for (i;i<=7;i++){
		dayM.push(new Date(year, month, day - i).toLocaleDateString());
	}

	data = {
			"dayM0" : dayM[0],
			"dayM1" : dayM[1],
			"dayM2" : dayM[2],
			"dayM3" : dayM[3],
			"dayM4" : dayM[4],
			"dayM5" : dayM[5],
			"dayM6" : dayM[6]
	}
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		data: JSON.stringify(data),
		url: "${getStateDayUrl}",
		success: function (data) {
			var result = data["result"];
			createTotalDeviceChart(result[0]);
			createAvgUseTimeChart(result[1]);
			createChartInfo(result[2]);
			console.log( "성공");
		}
		//error: ajaxReadError
	});
	
}
//금일 운행 정보생성 이벤트
function createChartInfo(data){
	
now = data["now"];
today = data["today"];
$("#chart_info").kendoChart({
    legend: {
        visible: false
    },
    seriesDefaults: {
        type: "bar"
    },
    series: [{
        name: "현재운행중",
        data: [now],
        color: "#3B92DA"
    },{
        name: "금일운행",
        data: [today],
        color: "#F3C51D"
        
    }],

    
    valueAxis: {
        max: sum,
        line: {
            visible: false
        },
        minorGridLines: {
            visible: true
        },
        labels: {
            rotation: "auto"
        }
    },
    categoryAxis: {
        categories: [],
        majorGridLines: {
            visible: false
        }
    },
    tooltip: {
        visible: true,
        template: "#= series.name #: #= value #"
    }

});

}
//로컬제어기 상태 그래프 생성
function createChart(data) {
	blue = ""
	yellow =""
	orange = ""
	red = ""
	gray = ""
	sum  = 0;
	for(i=0;i<data.length;i++){
		sum = sum + data[i][1]
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
	if (blue == ""){
		blue = "0"	
	}
	if (yellow == ""){
		yellow = "0"	
	}
	if (orange == ""){
		orange = "0"	
	}
	if (red == ""){
		red = "0"	
	}
	if (gray == ""){
		gray = "0"	
	}
	
	
    $("#chart_state").kendoChart({
        legend: {
            visible: false
        },
        seriesDefaults: {
            type: "bar"
        },
        series: [{
            name: "정상",
            data: [blue],
            color: "#3B92DA"
        },{
            name: "일부꺼짐",
            data: [yellow],
            color: "#F3C51D"
        },{
            name: "모두꺼짐",
            data: [orange],
            color: "#F37C1D"
        },{
            name: "제어기꺼짐",
            data: [red],
            color: "#C70000"
        }, 
        {
            name: "미확인",
            data: [gray],
            color: "#959595"
        }],
        
        valueAxis: {
            max: sum,
            line: {
                visible: false
            },
            minorGridLines: {
                visible: true
            },
            labels: {
                rotation: "auto"
            }
        },
        categoryAxis: {
            categories: [],
            majorGridLines: {
                visible: false
            }
        },
        tooltip: {
            visible: true,
            template: "#= series.name #: #= value #"
        }
 
    });
}
//총운영기기 그래프 생성
function createTotalDeviceChart(data) {

	
	var total_device =[];
	for (key in data){
		key_ex = key
		key_list =key.split("-")
		if(key_list[2].length ==1){
			key_list[2] = "0" + key_list[2]
		}	
		key = key_list[0]+"-" + key_list[1]+ "-"+key_list[2]
		var v = data[key_ex];
		cntDate = {
			value :v, 
			date : key		
		}
		total_device.push(cntDate);
	}

	total_device.sort(function(a, b) { 
	    return  a.date < b.date ? -1 : a.date > b.date ? 1 : 0;
	});


    $("#total_device").kendoChart({
        dataSource: {
            data: total_device
            
        },
        series: [{
            type: "column",
            aggregate: "sum",
            field: "value",
            categoryField: "date",
            color: "#959595"
        }],
        categoryAxis: {
            baseUnit: "days",
            majorGridLines: {
                visible: false
            }
        },
        valueAxis: {
            line: {
                visible: true
            }
        },
        tooltip: {
            visible: true
        }
        
      
    });
}

//평균운행시간 그래프 생성
function createAvgUseTimeChart(data) {

	var use_time =[];
	for (key in data){
		key_ex = key
		key_list =key.split("-")
		if(key_list[2].length ==1){
			key_list[2] = "0" + key_list[2]
		}	
		key = key_list[0]+"-" + key_list[1]+ "-"+key_list[2]
		var v = data[key_ex];
		var v = data[key];
		avgDate = {
			value :v, 
			date : key		
		}
		use_time.push(avgDate);
	}

	use_time.sort(function(a, b) { 
	    return a.date < b.date ? -1 : a.date > b.date ? 1 : 0;
	});

    $("#avg_use_time").kendoChart({
        dataSource: {
            data: use_time
            
        },
        series: [{
            type: "column",
            aggregate: "avg",
            field: "value",
            categoryField: "date",
            color: "#959595"
        }],
        categoryAxis: {
            baseUnit: "weeks",
            majorGridLines: {
                visible: false
            }
        },
        valueAxis: {
            line: {
                visible: true
            }
        },
        tooltip: {
            visible: true
        }

    });
}


</script>

<!-- / Grid button actions  -->


<!-- / Page body -->





<!-- Functional tags -->

<func:cmmValidate />


<!-- Closing tags -->

<common:base />
<common:pageClosing />
