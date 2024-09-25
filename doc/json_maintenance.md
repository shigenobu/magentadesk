### Maintenance

(INPUT)

cli: input mode=maintenance  
web: request /api/maintenance/reserve.json  

    {
      // (required) connect host
      "host":"127.0.0.1",
      // (required) connect port
      "port":13306,
      // (required) connect user, need 'base' database, 'compare' database, and 'magentadesk' database privileges
      // when connecting, use information_schema
      "user":"root",
      // (required) connect password
      "pass":"pass",
      // (required) connect charset, 'utf8', 'utf8mb3' or 'utf8mb4' is allowed
      "charset":"utf8mb4",
      // (required) db type, 'mariadb' or 'mysql' is allowed, 'mariadb' is selected when no input 
      "dbType": "mariadb"
      // (required) base database
      "baseDatabase":"base",
      // (required) compare database
      "compareDatabase":"compare",
      // (required) maintenance state, if true, in maintenance concerned base with compare
      "maintenance":"(on|off)"
    }

(OUTPUT)

cli: output mode=maintenance  
web: response 200 /api/maintenance/check.json  


    {
      // maintenance result
      "maintenance":"(on|off)"
    }
