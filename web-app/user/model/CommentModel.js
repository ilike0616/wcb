Ext.define('user.model.CommentModel', {
	extend : 'Ext.data.Model',
	fields : [ {
		"name" : "employee.name",
		"type" : "string"
	}, {
		"name" : "content",
		"type" : "string"
	}, {
		"name" : "dateCreated",
		"type" : "date"
	}, {
		"name" : "photos"
	} ]
})