grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
grails.project.war.file = "target/ROOT.war"

grails.project.fork = [
    // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
    //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

    // configure settings for the test-app JVM, uses the daemon by default
    test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
    // configure settings for the run-app JVM
    run: false,
    // configure settings for the run-war JVM
    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the Console UI JVM
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
//        inherits true // Whether to inherit repository definitions from plugins

        grailsPlugins()
        grailsHome()
        mavenLocal()
        grailsCentral()
        mavenCentral()

        mavenRepo 'http://116.239.5.250:8082/nexus/content/repositories/releases/'
        mavenRepo 'http://116.239.5.250:8082/nexus/content/repositories/wcb/'
        //mavenRepo 'http://192.168.0.122:8081/nexus/content/repositories/public'
        // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"



//        mavenRepo 'http://192.168.0.122:8081/nexus/content/groups/public/'
//        mavenRepo "http://repo.springsource.org/libs-milestone/"
//        mavenRepo 'http://repo.spring.io/milestone'
//        mavenRepo "http://192.168.0.224:8081/"
//        mavenRepo "http://192.168.0.224:8081/nexus/content/repositories/wcb/"
		  mavenRepo 'http://download.osgeo.org/webdav/geotools'
		  mavenRepo 'http://www.hibernatespatial.org/repository'
		  mavenRepo 'http://central.maven.org/maven2'
    }
 
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.
//		 compile ("org.hibernatespatial:hibernate-spatial-sqlserver:1.1.1"){
//            excludes 'hibernate-core'
//         }
        provided 'org.postgresql:postgresql:9.4.1211.jre7'
        build "org.fusesource.jansi:jansi:1.11"
    }

    plugins {
        // plugins for the build system only
        runtime ":hibernate4:4.3.5.3" // or ":hibernate:3.6.10.15"
        build ":tomcat:7.0.53"
        compile ":rest-client-builder:2.1.1"
        // plugins for the compile step
        compile ":scaffolding:2.1.0"
        compile ':cache:1.1.6'
        compile ":asset-pipeline:1.8.7"

        compile "com.uniproud.xswy:xswy-core:0.1.3.13.17.12"

        // plugins needed at runtime but not for compilation

        runtime ":database-migration:1.4.0"
        runtime ":cors:1.1.6"
        compile ':quartz:1.0.1'
        compile ":platform-core:1.0.0"

        compile ":mail:1.0.7"
        compile ":simple-captcha:1.0.0"
//        compile ":cookie-session:2.0.18"
//		compile ":plugin-config:0.2.0"
//		compile ":hibernate-spatial:0.0.4"
//		compile ":hibernate-spatial-mysql:0.0.4"
//		compile ":hibernate-spatial-sqlserver:0.0.4"
	
//        compile ":mongodb:3.0.1"
//        runtime ":jquery:1.11.1"
//        compile 'com.uniproud.wcb.wcb:plugin3:0.1'
        // Uncomment these to enable additional asset-pipeline capabilities
        //compile ":sass-asset-pipeline:1.7.4"
        //compile ":less-asset-pipeline:1.7.0"
        //compile ":coffee-asset-pipeline:1.7.0"
        //compile ":handlebars-asset-pipeline:1.3.0.3" 
        // compile ':cookie-session:2.0.16'
        // compile ":console:1.4.2"
    }
}
