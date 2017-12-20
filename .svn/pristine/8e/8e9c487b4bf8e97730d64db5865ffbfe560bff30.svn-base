<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>基础服务</title>
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
        <h4>基础服务</h4>
        <table class="tab">
            <tr>
                <td align="right">服务地址 ：</td>
                <td><input type="text" style="width: 400px;" id="url" value="http://192.168.16.240:9090/ops-infrastructure/"></td>
            </tr>
            <tr>
                <td align="right">service：</td>
                <td>
                    <select id ="service" style="width: 400px;">
						<option value="infrastructure.userLogin">用户登录(infrastructure.userLogin)</option>
						<option value="infrastructure.userLogout">用户登出(infrastructure.userLogout)</option>
						<option value="infrastructure.userValidate">用户验证(infrastructure.userValidate)</option>
						<option value="infrastructure.userFind">用户查询(infrastructure.userFind)</option>
						<option value="infrastructure.userRegister">用户注册(infrastructure.userRegister)</option>
						<option value="infrastructure.userPasswordChange">密码更改(infrastructure.userPasswordChange)</option>
						<option value="infrastructure.userPasswordReset">密码重置(infrastructure.userPasswordReset)</option>
						<option value="infrastructure.userEdit">用户编辑(infrastructure.userEdit)</option>
						<option value="infrastructure.notificationEmail">邮件通知(infrastructure.notificationEmail)</option>
						<option value="infrastructure.notificationMessage">消息通知(infrastructure.notificationMessage)</option>
						<option value="infrastructure.eventLog">事件日志(infrastructure.eventLog)</option>
						<option value="infrastructure.appToken">app获取token(infrastructure.appToken)</option>
						<option value="infrastructure.appValidate">app验证授权(infrastructure.appValidate)</option>
						<option value="infrastructure.findAppInfo">app信息查找(infrastructure.findAppInfo)</option>
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

