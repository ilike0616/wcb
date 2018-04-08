class UrlMappings {

	static mappings = {
        "/admin" (redirect: '/admin.html')
        "/ag" (redirect: '/ag.html')
        "/app/new/$platform/$edition"(controller: 'appVersion',action: 'newApp')
        "/confirm/$id?" {
            controller = 'employee'
            action = "emailConfirmation"
        }
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
        "/"(redirect: '/login1.html')
        "/"(view:"/index")
        "500"(view:'error')
        "403"(view:'error')
        "404"(view:'error')
	}
}