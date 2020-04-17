$(function() {
	createDataGrid('goodsUpperId', cfg);
});

var toolbar = [];
var columns = [
		{
			title : '操作',
			field : 'pk',
			width : 100,
			formatter : function(value, row, index) {
				var str = "";
				if (row.isUpdown == 1) {
					str += ' <a class="save" href="javascript:;" style="text-decoration:none;"> <button type="button" style="display:none;" showname="MARKET_GOODSSTATISTIC_BTN_UPGOODS" class="btn btn-warning" onclick="javascript:editState(\''
							+ value
							+ '\',2);">上架</button></a>';
					str += ' <a class="save" href="javascript:;" style="text-decoration:none;"> <button type="button" style="display:none;" showname="MARKET_GOODSSTATISTIC_BTN_DOWNGOODS" class="btn btn-warning" onclick="javascript:editState(\''
							+ value
							+ '\',3);">下架</button></a>';
				} else if (row.isUpdown == 2) {
					str += ' <a class="save" href="javascript:;" style="text-decoration:none;"> <button type="button" style="display:none;" showname="MARKET_GOODSSTATISTIC_BTN_DOWNGOODS" class="btn btn-warning" onclick="javascript:editState(\''
							+ value
							+ '\',3);">下架</button></a>';
				} else if (row.isUpdown == 3) {
					str += ' <a class="save" href="javascript:;" style="text-decoration:none;"> <button type="button" style="display:none;" showname="MARKET_GOODSSTATISTIC_BTN_UPGOODS" class="btn btn-warning" onclick="javascript:editState(\''
							+ value
							+ '\',2);">上架</button></a>';
				}

				return str;
			}
		}, {
			title : '是否上架',
			field : 'isUpdown',
			width : 20,
			sortable : true,
			formatter : function(value, row, index) {
				if (value == 1) {
					return "待上架";
				} else if (value == 2) {
					return "上架";
				} else if (value == 3) {
					return "下架";
				}

			}

		},
		{
			title : '店铺',
			field : 'storeName',
			width : 20,
			sortable : true
		},

		{
			title : '挂牌价',
			field : 'tonPrice',
			width : 20,
			sortable : true
		}, {
			title : '公司名称',
			field : 'companyName',
			width : 20,
			sortable : true
		}, {
			title : '品牌名称',
			field : 'brandName',
			width : 20,
			sortable : true
		}, {
			title : '品名名称',
			field : 'cfgoods.productName',
			width : 20
		}, {
			title : '规格大类名称',
			field : 'cfgoods.specName',
			width : 20
		}, {
			title : '规格系列名称',
			field : 'cfgoods.seriesName',
			width : 20
		}, {
			title : '等级名称',
			field : 'cfgoods.gradeName',
			width : 20
		}, {
			title : '品种名称',
			field : 'cfgoods.varietiesName',
			width : 20
		}, {
			title : '子品种名称',
			field : 'cfgoods.seedvarietyName',
			width : 20
		}, {
			title : '创建时间',
			field : 'insertTime',
			width : 20,
			sortable : true
		}, {
			title : '更新时间',
			field : 'updateTime',
			width : 20,
			sortable : true
		}, {
			title : '上架时间',
			field : 'upTime',
			width : 20,
			sortable : true
		}, {
			title : '审核时间',
			field : 'auditTime',
			width : 20,
			sortable : true
		}, {
			title : '批号',
			field : 'cfgoods.batchNumber',
			width : 20
		}, {
			title : '箱重',
			field : 'tareWeight',
			width : 20,
			sortable : true
		}, {
			title : '可用箱数',
			field : 'totalBoxes',
			width : 20,
			sortable : true
		}, {
			title : '可用库存',
			field : 'totalWeight',
			width : 20,
			sortable : true
		}, {
			title : '厂区名称',
			field : 'cfgoods.plantName',
			width : 20
		}, {
			title : '仓库名称',
			field : 'cfgoods.wareName',
			width : 20
		}, {
			title : '描述',
			field : 'cfgoods.purposeDescribe',
			width : 20,
		} ];
var cfg = {
	url : './goodsM_data.do',
	columns : columns,
	uniqueId : 'pk',
	sortName : 'insertTime',
	sortOrder : 'desc',
	queryParams : function(params) {
		return {
			auditStatus : 2,
			limit : params.limit,
			start : params.offset,
			orderType : params.order,
			orderName : params.sort
		}
	},
	toolbar : toolbar,
	onLoadSuccess : btnList
};

/**
 * 商品上架、下架
 * @param pk
 * @param sta
 */
function editState(pk, sta) {
	var parm = {
		pk : pk,
		isUpdown : sta
	};

	$.ajax({
		type : 'POST',
		url : './updateGoods.do',
		data : parm,
		dataType : 'json',
		success : function(data) {
			new $.flavr({
				modal : false,
				content : data.msg
			});
			if (data.success) {
				$('#goodsUpperId').bootstrapTable('refresh');
			}

		}
	});

}

/**
 * 商品上下架状态Table切换
 * @param s
 */
function findMarri(s) {
	if (s == 0) {
		$("#isUpdown").val("");
	} else {
		$("#isUpdown").val(s);
	}
	var url = "./goodsM_data.do" + urlParams(1) + "&auditStatus=2";

	var cfg = {
		url : url,
		columns : columns,
		uniqueId : 'pk',
		sortName : 'insertTime',
		sortOrder : 'desc',
		toolbar : toolbar,
		onLoadSuccess : btnList
	};
	createDataGrid('goodsUpperId', cfg);
}

function SearchClean(type) {

	if (type == 2) {
        $("#seriesPk").empty();
        $("#seriesPk").append("<option value=''>---请选择---</option>");
        $("#seriesPk").selectpicker('refresh');//
        $("#seedvarietyPk").empty();
        $("#seedvarietyPk").append("<option value=''>---请选择---</option>");
        $("#seedvarietyPk").selectpicker('refresh');//
		cleanpar();
	}
	var url = "./goodsM_data.do" + urlParams(1) + "&auditStatus=2";

	var cfg = {
		url : url,
		columns : columns,
		uniqueId : 'pk',
		sortName : 'insertTime',
		sortOrder : 'desc',
		toolbar : toolbar,
		onLoadSuccess : btnList
	};
	createDataGrid('goodsUpperId', cfg);
}

/**
 * 品种改变时，级联子品种
 */
function changeVari() {
	$.ajax({
		type : 'POST',
		url : './getvarietiesByPid.do',
		data : {
			parentPk : $("#varietiesPk").val()
		},
		dataType : 'json',
		success : function(data) {
			$("#seedvarietyPk").empty();
			$("#seedvarietyPk").append("<option value=''>---全部---</option>");
			if (data) {
				for ( var i = 0; i < data.length; i++) {
					$("#seedvarietyPk").append(
							"<option value='" + data[i].pk + "'>"
									+ data[i].name + "</option>");
				}
			}
			$("#seedvarietyPk").selectpicker('refresh');//
		}
	});
}

/**
 * 修改规格大类时候，级联规格系列
 */
function changeSpec() {
	$.ajax({
		type : 'POST',
		url : './getspecByPid.do',
		data : {
			parentPk : $("#specPk").val()
		},
		dataType : 'json',
		success : function(data) {
			$("#seriesPk").empty();
			$("#seriesPk").append("<option value=''>---全部---</option>");
			if (data) {
				for ( var i = 0; i < data.length; i++) {
					$("#seriesPk").append(
							"<option value='" + data[i].pk + "'>"
									+ data[i].name + "</option>");
				}
			}
			$("#seriesPk").selectpicker('refresh');//
		}
	});
}

/**
 * 导出商品数据信息
 */
function excel() {

    $.ajax({
        type : 'POST',
        url : './exportGoodsIsUpDown.do' + urlParams(1)+"&isOperBlock=2",
        data : {},
        dataType : 'json',
        success : function(data) {
            if (data.success){
                new $.flavr({
                    modal : false,
                    content :data.msg
                });
            }
        }
    });
}