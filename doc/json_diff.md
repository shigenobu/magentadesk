### Diff

(INPUT)  

cli: input mode=diff  
web: request /api/diff/reserve.json

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
      // (required) base database
      "baseDatabase":"base",
      // (required) compare database
      "compareDatabase":"compare",
      // optionally
      "option":{
        // list. default is '%'. OR condition.
        "includeTableLikePatterns": [
          "m\\_*"
        ],
        // list. default is ''. AND condition.
        "excludeTableLikePatterns": [
          "m\\_admin\\_%"
        ],
        // if true, ignore auto increment value for table structure diff. default is false.
        "ignoreAutoIncrement": false,
        // if true, ignore table, column, index and partition comment for table structure diff. default is false.
        "ignoreComment": false,
        // if true, ignore partition for table structure diff. default is false.
        "ignorePartitions": false,
        // if true, ignore column default sequence definition for table structure diff. default is false.
        "ignoreDefaultForSequence": false
      },
      // conditions
      "conditions": [
        {
          // if setting tableName is matched, expresion is used by 'where' condition.
          // if setting tableName is matched, 'checksum table' query is passed and replaced a fake value.
          "tableName":"t1",
          "expression":"upd_date > (now() - interval 10 day)"
        }
      ]
    }

(OUTPUT)  

cli: output mode=diff  
web: response 200 /api/diff/check.json  

    {
      "existsOnlyBaseTables": [
        {
          "tableName": "t1",
          "tableComment": "",
          "baseDefinition": "CREATE ..."
        }
      ],
      "existsOnlyCompareTables": [
        {
          "tableName": "t1",
          "tableComment": "",
          "compareDefinition": "CREATE ..."
        }
      ],
      "forceExcludeTables": [
        {
          "tableName": "t1",
          "tableComment": "",
          "definition": "CREATE ...",
          "reason": "(referencedForeignKey|isView|isSequence|notInnoDB|mismatchTrigger)"
        }
      ],
      "incorrectDefinitionTables": [
        {
          "tableName": "t1",
          "tableComment": "",
          "definition": "CREATE ...",
          "reason": "(noPrimaryKey|hasForeignKey|invalidCharset)"
        }
      ],
      "mismatchDefinitionTables": [
        {
          "tableName": "t1",
          "tableComment": "",
          "baseDefinition": "CREATE ...",
          "compareDefinition": "CREATE ..."
        }
      ],
      "mismatchRecordTables": [
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
      "matchTables": [
        {
          "tableName": "t1",
          "tableComment": "",
          "baseTableType": "",
          "compareTableType": "",
          "baseChecksum": "",
          "compareChecksum": ""
        }
      ],
      "summaryId": "XXXXX"
    }