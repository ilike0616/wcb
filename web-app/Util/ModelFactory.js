Ext.define("user.model.modelFactory",{
	//数据类模型的集合
	models:new Ext.util.MixedCollection(),
	//字段集合
	fields:new Ext.util.MixedCollection(),
	getModelByModuleId:function(moduleId){
		//1.声明类,返回类的ClassName
		if(this.models.containsKey(moduleId)){
			return moduleId;
		}else{
			//ajax拿到我们的字段集合
			var fields = [];
			if(this.fields.containsKey(moduleId)){
				fields = this.fields.containsKey(moduleId)
			}else{
				var localFields = localStorage.getItem('model-'+moduleId);
				if(localFields){
					fields = Ext.JSON.decode(localFields);
				}else {
					Ext.Ajax.request({
						url: 'model?moduleId=' + moduleId,
						method: 'POST',
						timeout: 4000,
						async: false,//跟关键 我不需要异步操作
						success: function (response, opts) {
							fields = eval(response.responseText);
						}
					});
				}
			}
			this.fields.add(moduleId,fields);
			
			var newModel = Ext.define(moduleId,{
				extend:'Ext.data.Model',
				fields:fields
			});
			this.models.add(moduleId,newModel);
			return moduleId;
		}
	}
});
var modelFactory = Ext.create('user.model.modelFactory',{});







