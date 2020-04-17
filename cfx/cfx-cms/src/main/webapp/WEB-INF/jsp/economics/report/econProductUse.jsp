<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="/WEB-INF/jsp/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>金融产品使用情况</title>
<c:import url="/WEB-INF/jsp/include/htmlHead.jsp">
</c:import>
<script src="./pageJs/economics/report/econProductUse.js"></script>
<style>
@media screen and (max-width: 1920px) and (min-width: 1600px) {
	.page-container-bg-solid .frame {
		width: 100%;
		height: 900px !important;
		background: #fff;
	}
}
</style>
</head>
<body class="sticky-header">
	<div class="page-heading">
		<ul class="breadcrumb">
			<li><a href="#">金融中心</a></li>
			<li class="active">数据管理>金融产品使用情况</li>
		</ul>
	</div>
	<!-- 页面内容导航结束-->
	<!--主体内容开始-->
	<div class="wrapper">
		<div class="row">
			<div class="col-sm-12">
				<section class="panel"> <header class="panel-heading">
				<input type="hidden" id="bankPk" name="bankPk" value="${bankPk}" />
				<label class="col-lg-3 col-lg-2"><span>年份：</span> <input
					class="form-controlgoods input-append date form-dateNowyear"
					type="text" name="year" id="year" value="${year}"
					onkeydown="entersearch()" readonly /> </label>
					<label class="col-sm-6 col-md-4 col-lg-3"><span>银行名称：</span>
						<select class="form-controlgoods input-append" id="selectBank" style="width: 180px;">
							<c:forEach items="${bankList}" var="banks">
								<option value="${banks.pk}" ${banks.pk == bankPk?'selected':''}>${banks.bankName}</option>
							</c:forEach>
						</select></label>
				<button class="btn btn-primary" onclick="searchTabs()">搜索</button>
				<button style="display:none;" showname="EM_REPORT_PROUSE_BTN_EXPORT"
					class="btn btn-primary" onclick="exportData();"
					data-toggle="dropdown">
					<span id="loadingExportData">导出</span>
				</button>
				</header>
				<div class="panel-body">
					<section class="panel">
					<div class="panel-bodycommon">
						<div class="tab-content">
							<div class="bootstrap-table" style="margin-top: 0px;">
								<div class="fixed-table-container" style="padding-bottom: 0px;">
									<div class="fixed-table-body">
										<table id="fiberLoanOrderId"
											style="text-align: center; width: 100%"
											class="table table-hover table-striped">
											<tr>
												<td rowspan="4">时间</td>
												<td colspan="100">金融产品</td>
											</tr>
											<tr>
												<td colspan="12">新增提款</td>
												<td colspan="12">累计提款</td>
												<td colspan="12">新增还款</td>
												<td colspan="12">累计还款</td>
												<td colspan="12">当前余额</td>
											</tr>
											<tr>
												<td colspan="6">化纤贷</td>
												<td colspan="6">化纤白条</td>
												<td colspan="6">化纤贷</td>
												<td colspan="6">化纤白条</td>
												<td colspan="6">化纤贷</td>
												<td colspan="6">化纤白条</td>
												<td colspan="6">化纤贷</td>
												<td colspan="6">化纤白条</td>
												<td colspan="6">化纤贷</td>
												<td colspan="6">化纤白条</td>
											</tr>
											<tr>
												<td>盛虹</td>
												<td>新凤鸣</td>
												<td>营销</td>
												<td>平台</td>
												<td>其他</td>
												<td>合计</td>
												
												<td>盛虹</td>
												<td>新凤鸣</td>
												<td>营销</td>
												<td>平台</td>
												<td>其他</td>
												<td>合计</td>
												
												<td>盛虹</td>
												<td>新凤鸣</td>
												<td>营销</td>
												<td>平台</td>
												<td>其他</td>
												<td>合计</td>
												
												<td>盛虹</td>
												<td>新凤鸣</td>
												<td>营销</td>
												<td>平台</td>
												<td>其他</td>
												<td>合计</td>
												
												<td>盛虹</td>
												<td>新凤鸣</td>
												<td>营销</td>
												<td>平台</td>
												<td>其他</td>
												<td>合计</td>
												
												<td>盛虹</td>
												<td>新凤鸣</td>
												<td>营销</td>
												<td>平台</td>
												<td>其他</td>
												<td>合计</td>
												
												<td>盛虹</td>
												<td>新凤鸣</td>
												<td>营销</td>
												<td>平台</td>
												<td>其他</td>
												<td>合计</td>
												
												<td>盛虹</td>
												<td>新凤鸣</td>
												<td>营销</td>
												<td>平台</td>
												<td>其他</td>
												<td>合计</td>
												
												<td>盛虹</td>
												<td>新凤鸣</td>
												<td>营销</td>
												<td>平台</td>
												<td>其他</td>
												<td>合计</td>
												
												<td>盛虹</td>
												<td>新凤鸣</td>
												<td>营销</td>
												<td>平台</td>
												<td>其他</td>
												<td>合计</td>
											</tr>
											<c:forEach items="${econProductUseList}" var="dimen">
											<tr>
												<td>${dimen.title}</td>
												
												<td>${dimen.sDAmount}</td>
												<td>${dimen.xDAmount}</td>
												<td>${dimen.yDAmount}</td>
												<td>${dimen.pDAmount}</td>
												<td>${dimen.qDAmount}</td>
												<td>${dimen.dAmount}</td>
												
												<td>${dimen.sBTAmount}</td>
												<td>${dimen.xBTAmount}</td>
												<td>${dimen.yBTAmount}</td>
												<td>${dimen.pBTAmount}</td>
												<td>${dimen.qBTAmount}</td>
												<td>${dimen.bTAmount}</td>
												
												<td>${dimen.sDTotalAmount}</td>
												<td>${dimen.xDTotalAmount}</td>
												<td>${dimen.yDTotalAmount}</td>
												<td>${dimen.pDTotalAmount}</td>
												<td>${dimen.qDTotalAmount}</td>
												<td>${dimen.dTotalAmount}</td>
												
												<td>${dimen.sBTTotalAmount}</td>
												<td>${dimen.xBTTotalAmount}</td>
												<td>${dimen.yBTTotalAmount}</td>
												<td>${dimen.pBTTotalAmount}</td>
												<td>${dimen.qBTTotalAmount}</td>
												<td>${dimen.bTTotalAmount}</td>
												
												<td>${dimen.sPayDAmount}</td>
												<td>${dimen.xPayDAmount}</td>
												<td>${dimen.yPayDAmount}</td>
												<td>${dimen.pPayDAmount}</td>
												<td>${dimen.qPayDAmount}</td>
												<td>${dimen.payDAmount}</td>
												
												<td>${dimen.sPayBTAmount}</td>
												<td>${dimen.xPayBTAmount}</td>
												<td>${dimen.yPayBTAmount}</td>
												<td>${dimen.pPayBTAmount}</td>
												<td>${dimen.qPayBTAmount}</td>
												<td>${dimen.payBTAmount}</td>
												
												<td>${dimen.sPayDTotalAmount}</td>
												<td>${dimen.xPayDTotalAmount}</td>
												<td>${dimen.yPayDTotalAmount}</td>
												<td>${dimen.pPayDTotalAmount}</td>
												<td>${dimen.qPayDTotalAmount}</td>
												<td>${dimen.payDTotalAmount}</td>
												
												<td>${dimen.sPayBTTotalAmount}</td>
												<td>${dimen.xPayBTTotalAmount}</td>
												<td>${dimen.yPayBTTotalAmount}</td>
												<td>${dimen.pPayBTTotalAmount}</td>
												<td>${dimen.qPayBTTotalAmount}</td>
												<td>${dimen.payBTTotalAmount}</td>
												
												<td>${dimen.sNowDAmount}</td>
												<td>${dimen.xNowDAmount}</td>
												<td>${dimen.yNowDAmount}</td>
												<td>${dimen.pNowDAmount}</td>
												<td>${dimen.qNowDAmount}</td>
												<td>${dimen.nowDAmount}</td>
												
												<td>${dimen.sNowBTAmount}</td>
												<td>${dimen.xNowBTAmount}</td>
												<td>${dimen.yNowBTAmount}</td>
												<td>${dimen.pNowBTAmount}</td>
												<td>${dimen.qNowBTAmount}</td>
												<td>${dimen.nowBTAmount}</td>
												
												
											</tr>
										</c:forEach>
										</table>
									</div>
								</div>
							</div>

						</div>
					</div>
					</section>
				</div>
				</section>
			</div>
		</div>
	</div>
</body>
<script>
	function entersearch(){  
	    var event = window.event || arguments.callee.caller.arguments[0];  
	    if (event.keyCode == 13)  
	       {  
	    	searchTabs();  
	       }  
	} 	
	</script>
	 <script src="./pageJs/include/third-party/fancybox/jquery.fancybox-1.3.1.pack.js"></script>
	 <script src="./pageJs/include/form-dateday.js"></script>
</html>