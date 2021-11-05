### Sync

(INPUT)

cli: input mode=sync  
web: request /api/sync/reserve.json

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
      // (required) diff result id
      "summaryId":"XXXXX",
      // optionally, if listed, selected diffSeqs are only reflected.
      "diffSeqs": [],
      // optionally, if 'true', execute sync, 'false' is dryrun. default is false.
      "run": false,
      // optionally, if 'true', not exists in base, but exists in compare, force to delete from compare. default is false.
      "force": false,
      // optionally, if 'true', delete only summary id records in complete execution, 'false' is delete base and compare concerned records. default is false.
      "loose": false,
      // optionally, execute commands just before commit.
      "commandsBeforeCommit": [
        {
          // (required) command
          "command": "XXX",
          // default is 30.
          "timeout": 10,
          // default is [0]. if not contains command result code, rollback happens.
          "successCodeList": [0, 23]
        }
      ],
      // optionally, execute commands just after commit.
      "commandsAfterCommit": [
        {
          // (required) command
          "command": "XXX",
          // default is 30.
          "timeout": 10
        }
      ],
      // optionally, execute http callback just before commit.
      "httpCallbackBeforeCommit": [
        {
          // (required) callback url
          "url": "http://localhost:9000/before.php",
          // default is 30.
          "timeout": 10,
          // default is [200]. if not contains http status code, rollback happens.
          "successStatusList": [200, 201]
        }
      ],
      // optionally, execute http callback just after commit.
      "httpCallbackAfterCommit": [
        {
          // (required) callback url
          "url": "http://localhost:9000/after.php",
          // default is 30.
          "timeout": 10
        }
      ]
    }

(OUTPUT)

cli: output mode=sync  
web: response 200 /api/sync/check.json  

    {
      "reflectedRecordTables": [
        {
          "tableName": "t1",
          "tableComment": "",
          "columns": [
            {
              "columnName": "",
              "columnType": "",
              "columnCollation": "",
              "columnComment": "",
              "isPrimary": (true|false),
            }
          ],
          "records": [
            {
              "diffSeq": XXX,
              "values": [],
              "changes": []
            }
          ]
        }
      ],
      "notReflectedRecordTables": [
        {
          "tableName": "t1",
          "tableComment": "",
          "columns": [
            {
              "columnName": "",
              "columnType": "",
              "columnCollation": "",
              "columnComment": "",
              "isPrimary": (true|false),
            }
          ],
          "records": [
            {
              "diffSeq": XXX,
              "baseValues": [],
              "compareValues": []
            }
          ]
        }
      ],
      "commandResultsBeforeCommit": [
        {
          "command": "XXX",
          "code": XXX,
          "output": "XXX"
        }
      ],
      "commandResultsAfterCommit": [
        {
          "command": "XXX",
          "code": XXX,
          "output": "XXX"
        }
      ],
      "httpResultsBeforeCommit": [
        {
          "status": 200,
          "body": ""
        }
      ],
      "httpResultsAfterCommit": [
        {
          "status": 200,
          "body": ""
        }
      ],
      "summaryId": "XXXXX"
    }

---

##### called command in commandsBeforeCommit or commandsAfterCommit

(stdin)

    {
      // when mode=sync, input run value.
      "run": false,
      // when mode=sync, output reflectedRecordTables value written in file.
      "reflectedJsonPath": "${mdHome}/reflected_${summaryId}.json"
    }

(really execution)

    echo '{"run":true, "reflectedJsonPath":"path_to_reflected_records.json"}' | ${command}

##### called http callback in httpCallbackBeforeCommit or httpCallbackAfterCommit

(request)

    {
      // when mode=sync, input run value.
      "run": false,
      // when mode=sync, output reflectedRecordTables value.
      "reflectedRecordTables": []
    }

(really execution)

    POST {URL} HTTP/1.1
    Host: {HOST}
    User-Agent: magentadesk-http-client
    Content-type: application/json; charset=UTF8
    
    {"run":true, "reflectedRecordTables":[]}
