/*grails {
    mongo {
        host = "test.uniproud.com"
        port = 27017
        username = "wcb"
        password = "654321"
        databaseName = "test"
    }
}*/

dataSource {
    pooled = true
    jmxExport = true
    // driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
    driverClassName = "com.mysql.jdbc.Driver"
    username = "root"
    password = "P@ssw0rd0411"
    logSql = false
//	dialect = org.hibernatespatial.mysql.MySQLSpatialDialect
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
//    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
    singleSession = true // configure OSIV singleSession mode
}

// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = "jdbc:mysql://test.uniproud.com:3307/wcb1?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false"
        }
    }
    test {
        dataSource {
            dbCreate = "create-drop"
            url = "jdbc:mysql://test.uniproud.com:3307/wcb1?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false"
        }
    }
    production {
        dataSource {
            dbCreate = "create-drop"
            url = "jdbc:mysql://test.uniproud.com:3307/wcb6?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false"
			logSql = false
            properties {
               // See http://grails.org/doc/latest/guide/conf.html#dataSource for documentation
               jmxEnabled = true
               initialSize = 5
               maxActive = 50
               minIdle = 5
               maxIdle = 25
               maxWait = 10000
               maxAge = 10 * 60000
               timeBetweenEvictionRunsMillis = 5000
               minEvictableIdleTimeMillis = 60000
               validationQuery = "SELECT 1"
               validationQueryTimeout = 3
               validationInterval = 15000
               testOnBorrow = true
               testWhileIdle = true
               testOnReturn = false
               jdbcInterceptors = "ConnectionState"
               defaultTransactionIsolation = java.sql.Connection.TRANSACTION_READ_COMMITTED
            }
        }
    }
}

// Added by the Hibernate Spatial Plugin. 
//dataSource {
//    
//}

// Added by the Hibernate Spatial Plugin. 
//dataSource {
////    driverClassName = "net.sourceforge.jtds.jdbc.Driver" //"com.microsoft.sqlserver.jdbc.SQLServerDriver"
//    dialect = org.hibernatespatial.sqlserver.SQLServerSpatialDialect
//}
