Ext.define('user.model.CountryProvinceCityFactory',{
	// 国家
	country:[],
	// 省
	province:new Ext.util.MixedCollection(),
	// 市
	city:new Ext.util.MixedCollection(),

	getCountry:function(){
		if(this.country.length > 0){
			return this.country;
		}else{
			this.country = ['中国'];
			return this.country;
		}
	},
	getProvince:function(countryValue){
		var key = countryValue
		if(this.province.containsKey(key)){
			return this.province.get(key);
		}else{
			this.province.add('中国',this.generateArrayStore(['北京','上海','天津','重庆','河北','山西','辽宁','吉林','黑龙江','江苏','浙江','安徽','福建','江西',
				'山东','河南','湖北','湖南','广东','海南','四川','贵州','云南','陕西','甘肃','青海','内蒙古','广西','西藏','宁夏','新疆','香港','澳门','台湾']));
			if(this.province.containsKey(key)){
				return this.province.get(key);
			}else{
				return [];
			}
		}
	},
	getCity:function(countryValue,provinceValue){
		var key = countryValue + '_' + provinceValue;
		if(this.city.containsKey(key)){
			return this.city.get(key);
		}else{
			this.city.add('中国_北京',this.generateArrayStore(['东城区','西城区','崇文区','宣武区','朝阳区','丰台区','石景山区','海淀区','门头沟区','房山区','通州区','顺义区','昌平区','大兴区','怀柔区','平谷区','密云县','延庆县']));
			this.city.add('中国_上海',this.generateArrayStore(['黄浦区','徐汇区','长宁区','静安区','普陀区','闸北区','虹口区','杨浦区','闵行区','宝山区','嘉定区','浦东新区','金山区','松江区','青浦区','奉贤区']));
			this.city.add('中国_天津',this.generateArrayStore(['和平区','河东区','河西区','南开区','河北区','红桥区','塘沽区','汉沽区','大港区','东丽区','西青区','津南区','北辰区','武清区','宝坻区','蓟县','宁河县','静海县']));
			this.city.add('中国_重庆',this.generateArrayStore(['万州区','涪陵区','渝中区','大渡口区','江北区','沙坪坝区','九龙坡区','南岸区','北碚区','綦江区','大足区','渝北区','巴南区','黔江区','长寿区','江津区','合川区','永川区','南川区','璧山区','铜梁区','潼南区','荣昌区','梁平县','城口县','丰都县','垫江县','武隆县','忠县','开县','云阳县','奉节县','巫山县','巫溪县','石柱土家族自治县','秀山土家族苗族自治县','酉阳土家族苗族自治县','彭水苗族土家族自治县']));
			this.city.add('中国_河北',this.generateArrayStore(['石家庄','唐山','秦皇岛','邯郸','邢台','保定','张家口','承德','沧州','廊坊','衡水']));
			this.city.add('中国_山西',this.generateArrayStore(['长治','大同','晋城','晋中','临汾','吕梁','朔州','太原','忻州','阳泉','运城']));
			this.city.add('中国_辽宁',this.generateArrayStore(['鞍山','本溪','朝阳','大连','丹东','抚顺','阜新','葫芦岛','锦州','辽阳','盘锦','沈阳','铁岭','营口']));
			this.city.add('中国_吉林',this.generateArrayStore(['白城','白山','长春','吉林','辽源','四平','松原','通化','延边朝鲜族自治州']));
			this.city.add('中国_黑龙江',this.generateArrayStore(['大庆','大兴安岭','哈尔滨','鹤岗','黑河','鸡西','佳木斯','牡丹江','七台河','齐齐哈尔','双鸭山','绥化','伊春']));
			this.city.add('中国_江苏',this.generateArrayStore(['常州','淮安','连云港','南京','南通','苏州','宿迁','泰州','无锡','徐州','盐城','扬州','镇江']));
			this.city.add('中国_浙江',this.generateArrayStore(['杭州','湖州','嘉兴','金华','丽水','宁波','绍兴','台州','温州','舟山','衢州']));
			this.city.add('中国_安徽',this.generateArrayStore(['安庆','蚌埠','巢湖','池州','滁州','阜阳','合肥','淮北','淮南','黄山','六安','马鞍山','宿州','铜陵','芜湖','宣城','亳州']));
			this.city.add('中国_福建',this.generateArrayStore(['福州','龙岩','南平','宁德','莆田','泉州','三明','厦门','漳州']));
			this.city.add('中国_江西',this.generateArrayStore(['抚州','赣州','吉安','景德镇','九江','南昌','萍乡','上饶','新余','宜春','鹰潭']));
			this.city.add('中国_山东',this.generateArrayStore(['滨州','德州','东营','菏泽','济南','济宁','莱芜','聊城','临沂','青岛','日照','泰安','威海','潍坊','烟台','枣庄','淄博']));
			this.city.add('中国_河南',this.generateArrayStore(['安阳','鹤壁','济源','焦作','开封','洛阳','南阳','平顶山','三门峡','商丘','新乡','信阳','许昌','郑州','周口','驻马店','漯河','濮阳']));
			this.city.add('中国_湖北',this.generateArrayStore(['鄂州','恩施土家族苗族自治州','黄冈','黄石','荆门','荆州','潜江','神农架林区','十堰','随州','天门','武汉','仙桃','咸宁','襄樊','孝感','宜昌']));
			this.city.add('中国_湖南',this.generateArrayStore(['常德','长沙','郴州','衡阳','怀化','娄底','邵阳','湘潭','湘西土家族苗族自治州','益阳','永州','岳阳','张家界','株洲']));
			this.city.add('中国_广东',this.generateArrayStore(['潮州','东莞','佛山','广州','河源','惠州','江门','揭阳','茂名','梅州','清远','汕头','汕尾','韶关','深圳','阳江','云浮','湛江','肇庆','中山','珠海']));
			this.city.add('中国_海南',this.generateArrayStore(['白沙黎族自治县','保亭黎族苗族自治县','昌江黎族自治县','澄迈县','定安县','东方','海口','乐东黎族自治县','临高县','陵水黎族自治县','琼海','琼中黎族苗族自治县','三亚','屯昌县','万宁','文昌','五指山','儋州']));
			this.city.add('中国_四川',this.generateArrayStore(['阿坝藏族羌族自治州','巴中','成都','达州','德阳','甘孜藏族自治州','广安','广元','乐山','凉山彝族自治州','眉山','绵阳','南充','内江','攀枝花','遂宁','雅安','宜宾','资阳','自贡','泸州']));
			this.city.add('中国_贵州',this.generateArrayStore(['安顺','毕节','贵阳','六盘水','黔东南苗族侗族自治州','黔南布依族苗族自治州','黔西南布依族苗族自治州','铜仁','遵义']));
			this.city.add('中国_云南',this.generateArrayStore(['保山','楚雄彝族自治州','大理白族自治州','德宏傣族景颇族自治州','迪庆藏族自治州','红河哈尼族彝族自治州','昆明','丽江','临沧','怒江傈傈族自治州','曲靖','思茅','文山壮族苗族自治州','西双版纳傣族自治州','玉溪','昭通']));
			this.city.add('中国_陕西',this.generateArrayStore(['安康','宝鸡','汉中','商洛','铜川','渭南','西安','咸阳','延安','榆林']));
			this.city.add('中国_甘肃',this.generateArrayStore(['白银','定西','甘南藏族自治州','嘉峪关','金昌','酒泉','兰州','临夏回族自治州','陇南','平凉','庆阳','天水','武威','张掖']));
			this.city.add('中国_青海',this.generateArrayStore(['果洛藏族自治州','海北藏族自治州','海东','海南藏族自治州','海西蒙古族藏族自治州','黄南藏族自治州','西宁','玉树藏族自治州']));
			this.city.add('中国_内蒙古',this.generateArrayStore(['阿拉善盟','巴彦淖尔盟','包头','赤峰','鄂尔多斯','呼和浩特','呼伦贝尔','通辽','乌海','乌兰察布盟','锡林郭勒盟','兴安盟']));
			this.city.add('中国_广西',this.generateArrayStore(['百色','北海','崇左','防城港','桂林','贵港','河池','贺州','来宾','柳州','南宁','钦州','梧州','玉林']));
			this.city.add('中国_西藏',this.generateArrayStore(['阿里','昌都','拉萨','林芝','那曲','日喀则','山南']));
			this.city.add('中国_宁夏',this.generateArrayStore(['固原','石嘴山','吴忠','银川']));
			this.city.add('中国_新疆',this.generateArrayStore(['阿克苏','阿拉尔','巴音郭楞蒙古自治州','博尔塔拉蒙古自治州','昌吉回族自治州','哈密','和田','喀什','克拉玛依','克孜勒苏柯尔克孜自治州','石河子','图木舒克','吐鲁番','乌鲁木齐','五家渠','伊犁哈萨克自治州']));
			this.city.add('中国_香港',this.generateArrayStore(['香港']));
			this.city.add('中国_澳门',this.generateArrayStore(['澳门']));
			this.city.add('中国_台湾',this.generateArrayStore(['台湾']));
			if(this.city.containsKey(key)){
				return this.city.get(key);
			}else{
				return [];
			}
		}
	},
	generateArrayStore : function(abnormalArr){
		var normalArrStore = [];
		if(abnormalArr != null && abnormalArr != ""){
			Ext.Array.each(abnormalArr,function(ele){
				normalArrStore.push({'field1':ele});
			})
		}
		return normalArrStore;
	}
});
var countryProvinceCityFactory = Ext.create('user.model.CountryProvinceCityFactory',{});







