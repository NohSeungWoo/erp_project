<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<style type="text/css">
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
	
	/* 이 위에는 하이차트에서 가져온거다. */
	
</style>

<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/wordcloud.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>
<script src="https://code.highcharts.com/modules/export-data.js"></script>
<script src="https://code.highcharts.com/modules/accessibility.js"></script>

<script type="text/javascript">
	$(document).ready(function(){
		
	//    const text = 'Lorem amet, elit amet. amet amet elit, amet quam.',	
		const text = '${requestScope.str_keyword}';
	    // 검색했던 기록들 가져오기(중복 허용)
		
		// , 또는 .이 나올 경우 구분자로 인식함 => 기존 하이차트 구분자에서 " "는 제거했다. 왜냐하면 "좋아하는 음식"이렇게 검색할 수도 있기 때문이다.
		lines = text.split(/[,\.]+/g),
	    data = lines.reduce((arr, word) => {
	        let obj = Highcharts.find(arr, obj => obj.name === word);
	        if (obj) {
	            obj.weight += 1;
	        } else {
	            obj = {
	                name: word,
	                weight: 1
	            };
	            arr.push(obj);
	        }
	        return arr;
	    }, []);

		Highcharts.chart('container', {
		    accessibility: {
		        screenReaderSection: {
		            beforeChartFormat: '<h5>{chartTitle}</h5>' +
		                '<div>{chartSubtitle}</div>' +
		                '<div>{chartLongdesc}</div>' +
		                '<div>{viewTableButton}</div>'
		        }
		    },
		    series: [{
		        type: 'wordcloud',
		        data,
		        name: 'Occurrences'
		    }],
		    title: {
		        text: '&lt;Board Search Frequency : 게시판 검색 빈도&gt;'
		    }
		});
		
		
		
	});// end of $(document).ready(function(){})------------------------
</script>


<figure class="highcharts-figure border">
    <div id="container"></div>
    <p class="highcharts-description">
    	위의 통계자료는 각각의 단어들이 얼마나 검색이 되었는지 빈도를 시각화해줍니다.<br>
    	* 글자의 크기가 클수록 검색 빈도가 높은 글자이며, 크기가 작을수록 검색 빈도가 낮은 글자입니다.
    </p>
</figure>

