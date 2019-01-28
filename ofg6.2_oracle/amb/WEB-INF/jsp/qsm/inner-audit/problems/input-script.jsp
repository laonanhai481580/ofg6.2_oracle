<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils" %>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">

function Audit(){
	$.colorbox({href:"${qsmctx}/inner-audit/auditor-library/list-view.htm",iframe:true,
		width:$(window).width()<1000?$(window).width()-100:1000,
		height:$(window).height()<600?$(window).height()-100:600,
		overlayClose:false,
		title:"选择内审员"
	});
}
function setAuditValue(objs){
	var a="";
	var b="";
	for(var i=0;i<objs.length;i++){
		var obj = objs[i];
		a=obj.name;
		if(i==0){
			b=a;
		}else{
			b=b+","+a;
		}
	}
	$("#auditor").val(b);
}
//模糊查询不符合条款
function searchSet(obj){
	var myId=obj.id;
	var a=myId.split("_")[0];
	$('#'+myId)
	.bind("blur",function(){
		if(!$(this).data("autocomplete").menu.element.is(":visible")){
			var hisSearch = $(this).attr("hisSearch");
			if(this.value !== hisSearch){
				$(this).attr("hisSearch",this.value); 
			}
		}
	})
	.autocomplete({
		minLength: 1,
		delay: 100,
		autoFocus: true, 
		source: function( request, response ) {
				response([{label:'数据加载中...',value:''}]);
				$.post("${qsmctx}/inner-audit/correct-measures/search-clause-name.htm",{searchParams:'{"clauseName":"'+request.term+'"}',label:'name'},function(result){
					response(result);
				},'json');
		},
		focus: function() {
			return false;
		},
		select: function( event, ui ) {
			if(ui.item.label){
				$('#'+myId).attr("hisValue",ui.item.label);
				$('#'+myId).val(ui.item.label);
				$('#'+a+"_violatePact").val(ui.item.value);
				this.value = ui.item.label; 
			}else{
				$('#'+myId).attr("hisValue","");
				$('#'+myId).val("");
				$('#'+a+"_violatePact").val("");
				this.value = ""; 
			}
			return true;
		},
		close : function(event,ui){
			var cust = $('#'+myId);
			var val=cust.val();
			if(val){//如果当前料号值非空
				var hisValue = cust.attr("hisValue");
				if(!hisValue||hisValue==null){//如果料号没有查询出值，且val不为空，则val为原始输入值，保留原始输入数据
					cust.val(val);
				}else{//如果料号查询出有值，且val非空，则val为查询出的值，保留查询出的数据
					cust.val(hisValue);
				}
			}else{ //如果当前料号值为空
				var hisValue = cust.attr("hisValue");
				if(!hisValue||hisValue==null){//如果料号没有查询出值，且val为空，则val为原始输入值就是空
					cust.val('');
				}else{//如果料号查询出有值，且val空，则val为查询出的值带出的空，保留查询出的数据
					cust.val(hisValue);
				}
			}
			cust.attr("hisValue",'').attr("hisSearch",'');
			$('#'+myId).attr("hisValue",'').attr("hisSearch",'');
		}
	});
}
</script>