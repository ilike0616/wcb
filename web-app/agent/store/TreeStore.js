Ext.define('agent.store.TreeStore', {
    extend: 'Ext.data.TreeStore',
    fields:['text','name','ctrl','view'],
    defaultRootId : 'root',
    proxy:{
        type:'ajax',
        url:'menu/agentTree',
        reader:'json'
    }
});
