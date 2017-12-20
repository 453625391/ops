<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>会员服务</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link type="text/css" href="css/page.css" rel="stylesheet" />
<script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/2.1.1/jquery.js"></script>
<script type="text/javascript">
	
	function execute() {
		var url= $("#url").val();
		var service= $("#service").val();
		var params= $("#params").val();
		if (url != "" && service != "" && params != "" ) {
			$.ajax({
				type : 'POST',
				url : 'opsTest/execute.do',
				data : 'url='+url+'&service='+service+'&params='+params+'&dataTemp='+ new Date().getTime(),
				success : function(json) {
					$("#resultInfo").text(json);
				}
			});
		}
	}
	
</script>
</head>
<body>
	<div class="main">
        <h4>会员服务</h4>
        <table class="tab">
            <tr>
                <td align="right">服务地址 ：</td>
                <td><input type="text" style="width: 400px;" id="url" value="http://192.168.16.240:9090/ops-membe/membe"></td>
            </tr>
            <tr>
                <td align="right">service：</td>
                <td>
                    <select id ="service" style="width: 400px;">
                    <option value="member.getMemberId">获取会员id(member.getMemberId)</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td align="right">params：</td>
                <td><textarea id="params" ></textarea></td>
            </tr>
            <tr>
                <td align="right" ></td>
                <td><input type="button" value="提交" onclick="execute();" ></td>
            </tr>
        </table>
        <h4>返回信息</h4>
        <textarea id="resultInfo" readonly="readonly" style="width: 100%"></textarea>
    </div>
</body>
</html>

