Ext.define("user.model.ConfigParamsFactory",{
	//字段集合
	configParams:new Ext.util.MixedCollection(),
	getParamValueByParamName:function(paramName){
		if(this.configParams.length <= 0 || !this.configParams.containsKey(paramName)){
			var me = this;
			Ext.Ajax.request({
				url:'base/getConfigParams',
				method:'POST',
				timeout:4000,
				async:false,// 我不需要异步操作
				success:function(response,opts){
					var result = Ext.JSON.decode(response.responseText,true);
					if(result != null){
						Ext.Array.each(result,function(o){
							me.configParams.add(o.paramName,o.paramValue);
						})
					}
				}
			});
		}
		var paramValue = "";
		if(this.configParams.containsKey(paramName)){
			paramValue = this.configParams.get(paramName);
		}
		return paramValue;
	}
});
var configParamsFactory = Ext.create('user.model.ConfigParamsFactory',{});







