/**
 * Created by like on 2015-04-07.
 */
Ext.define('user.controller.EmployeeLocusController',{
    extend : 'Ext.app.Controller',
    views:['employeeLocus.Map'] ,
    stores:['EmployeeLocusStore'],
    models : [ 'EmployeeLocusModel'],
    GridDoActionUtil:Ext.create("admin.util.GridDoActionUtil"),
    refs : [{
        ref : 'employeeLocusMap',
        selector : 'employeeLocusMap'
    }],
    init:function() {
        this.control({
            'employeeLocusMap' : {
                afterrender : function(component, eOpts) {
                    var store = this.getEmployeeLocusMap().store;
                    var dataObj = [];
                    Ext.Ajax.request({
                        url : store.getProxy().api['map'],
                        params : {},
                        method : 'POST',
                        timeout : 4000,
                        async : false,
                        success : function(response, opts) {
                            var rt = Ext.JSON.decode(response.responseText);
                            dataObj = rt.data;
                        },
                        failure : function(response, opts) {
                            var errorCode = "";
                            if (response.status) {
                                errorCode = 'error:' + response.status;
                            }
                            Ext.example.msg('提示', '操作失败！' + errorCode);
                            success = false;
                        }
                    });
                    this.getEmployeeLocusMap().initPathMap(component.el.dom, dataObj);
                }
            }
        });
    }
})