<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String ctxPath = request.getContextPath();
%>

<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/highcharts-more.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>
<script src="https://code.highcharts.com/modules/export-data.js"></script>
<script src="https://code.highcharts.com/modules/accessibility.js"></script>


<style type="text/css">

	#container {
	    height: 400px;
	}
	
	.highcharts-figure,
	.highcharts-data-table table {
	    min-width: 320px;
	    max-width: 800px;
	    margin: 1em auto;
	}
	
	.highcharts-data-table table {
	    font-family: Verdana, sans-serif;
	    border-collapse: collapse;
	    border: 1px solid #ebebeb;
	    margin: 10px auto;
	    text-align: center;
	    width: 100%;
	    max-width: 500px;
	}
	
	.highcharts-data-table caption {
	    padding: 1em 0;
	    font-size: 1.2em;
	    color: #555;
	}
	
	.highcharts-data-table th {
	    font-weight: 600;
	    padding: 0.5em;
	}
	
	.highcharts-data-table td,
	.highcharts-data-table th,
	.highcharts-data-table caption {
	    padding: 0.5em;
	}
	
	.highcharts-data-table thead tr,
	.highcharts-data-table tr:nth-child(even) {
	    background: #f8f8f8;
	}
	
	.highcharts-data-table tr:hover {
	    background: #f1f7ff;
	}

</style>

<script type="text/javascript">

	$(document).ready(function() {
		// 부서정보 가져오기
		var department = "${requestScope.departToString}";
		
		// 부서명 split 해서 배열에 담아주기 
		var departArr = department.split(",");
		
		// 부서별 인원 수 가져오기
		var departEmpCnt = "${requestScope.departEmpCntToString}";
		
		// 부서별 인원수 split 해서 배열에 담아주기
		var departCntArr = departEmpCnt.split(",");
		
		var departCntNumArr = [];
		
		for (var i = 0; i < departCntArr.length; i++) {
			departCntNumArr.push(Number(departCntArr[i]));
		}
		
		const chart = Highcharts.chart('container', {
		    title: {
		        text: '부서별 인원통계'
		    },
		    
		    xAxis: {
		        categories: departArr
		    },
		    series: [{
		        type: 'column',
		        colorByPoint: true,
		        data: departCntNumArr,
		        showInLegend: false
		    }]
		});

		document.getElementById('plain').addEventListener('click', () => {
		    chart.update({
		        chart: {
		            inverted: false,
		            polar: false
		        },
		    });
		});

		document.getElementById('inverted').addEventListener('click', () => {
		    chart.update({
		        chart: {
		            inverted: true,
		            polar: false
		        },
		    });
		});

		document.getElementById('polar').addEventListener('click', () => {
		    chart.update({
		        chart: {
		            inverted: false,
		            polar: true
		        },
		    });
		});

		
	});// end of $(document).ready(function() {})

</script>

<figure class="highcharts-figure" style="margin-top: 70px;">
    <div id="container"></div>
	
	<div id=buttons class="mt-3">
	    <button class="btn" id="plain" style="background-color: #adebeb;">기본</button>
	    <button class="btn" id="inverted" style="background-color: #e6f2ff">수평</button>
	    <button class="btn" id="polar"style="background-color: #ffd9b3">파이차트</button>
	</div>
</figure>