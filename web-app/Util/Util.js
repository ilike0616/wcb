Ext.define('Util.Util', {
    statics : {

        required: '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',

        decodeJSON : function (text) {

            var result = Ext.JSON.decode(text, true);

            if (!result){
                result = {};
                result.success = false;
                result.msg = result.msg;
            }

            return result;
        },

        showErrorMsg: function (text) {


        }
    }
});