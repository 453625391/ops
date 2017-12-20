<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>财务服务</title>
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
        <h4>财务服务</h4>
        <table class="tab">
            <tr>
                <td align="right">服务地址 ：</td>
                <td><input type="text" style="width: 400px;" id="url" value="http://192.168.16.240:9090/ops-finance/finance"></td>
            </tr>
            <tr>
                <td align="right">service：</td>
                <td >
                    <select id ="service" style="width: 400px;">
			            <option value="finance.initMemberAsset">余额充值(finance.initMemberAsset)</option>
						<option value="finance.balanceRecharge">余额充值(finance.balanceRecharge)</option>
						<option value="finance.couponRecharge">礼券充值(finance.couponRecharge)</option>
						<option value="finance.balancePay">余额支付(finance.balancePay)</option>
						<option value="finance.couponReward">抽奖赠送(finance.couponReward)</option>
						<option value="finance.couponReduce">奖金扣减(finance.couponReduce)</option>
						<option value="finance.orderPay">订单支付(finance.orderPay)</option>
						<option value="finance.orderReward">订单赠送(finance.orderReward)</option>
						<option value="finance.cashPay">现金消费(finance.cashPay)</option>
						<option value="finance.balanceWithdraw">余额体现(finance.balanceWithdraw)</option>
						<option value="finance.orderRefund">订单退款(finance.orderRefund)</option>
						<option value="finance.transactionConfirmed">交易结果通知(finance.transactionConfirmed)</option>
						<option value="finance.memberAssetQuery">会员资产查询(finance.memberAssetQuery)</option>
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

