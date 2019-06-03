# magentadesk - Get MariaDB Table Difference, And Reflect.

### About

Get diff and sync between two databases in same host created by MariaDB.  

### System Required

* over JDK 1.8
* over MariaDB 10.3

### Usage

When put json to stdin, get result from stdout by json.  
Below, it's simple usage.  

    echo '${json}' | java -jar magentadesk.jar --mode=${mode}

[args]  

|name|value|remarks|
|:--|:-|:---|
|--mode|(required)diff or sync| 'diff' is get diff, 'sync' is reflected used by diff results.|
|--logPath|log written path.|when 'stdout', write out to stdout, when 'stderr', write out to stderr.|
|--addSeconds|add seconds in log time|default is 60x60x9, it means 'ja', if 0, it means 'en'.|

[env]  

|name|remarks|
|:--|:---|
|MD_ENV|if 'DEBUG', log in detail.|
|MD_OUTPUT|if 'PRETTY', write out json result in pretty.|
|MD_HOME|default is ${HOME}/.magentadesk. There is a temporary directory, by commands which execute in sync.|

Complete sample, containis args and env.  

    echo '${json}' \
      | [MD_ENV=${mdEnv}] [MD_OUTPUT=${mdOutput}] [MD_HOME=${mdHome}] \
        java -jar magentadesk.jar --mode=${mode} [--logPath=${logPath}] [--addSeconds=${addSeconds}]


### JSON FORMAT

[INPUT JSON]

__mode=diff__

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
      // (required) connect charset, 'utf8' or 'utf8mb4' is allowed
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
        // if true, ignore system versioned column and table info for table structure diff. default is false.
        "ignoreSystemVersioned": false
      }
    }

__mode=sync__

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
      // (required) connect charset, 'utf8' or 'utf8mb4' is allowed
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
          "timeout": 10
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
      ]
    }

[OUTPUT JSON]

__mode=diff__

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
      "mismatchDefinitionTables": [
        {
          "tableName": "t1",
          "tableComment": "",
          "baseDefinition": "CREATE ...",
          "compareDefinition": "CREATE ..."
        }
      ],
      "incorrectDefinitionTables": [
        {
          "tableName": "t1",
          "tableComment": "",
          "definition": "CREATE ...",
          "reason": "(noPrimaryKey|hasForeignKey)"
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
      "mismatchRecordTables": [
        {
          "tableName": "t1",
          "tableComment": "",
          "columns": [
            {
              "columnName": "",
              "columnType": "",
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

__mode=sync__

    {
      "reflectedRecordTables": [
        {
          "tableName": "t1",
          "tableComment": "",
          "columns": [
            {
              "columnName": "",
              "columnType": "",
              "columnComment": "",
              "isPrimary": (true|false),
            }
          ],
          "records": [
            {
              "diffSeq": XXX,
              "values": []
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
      "summaryId": "XXXXX"
    }

(called command in commandsBeforeCommit or commandsAfterCommit)

  (stdin)

    {
      // when mode=sync, input run value.
      "run": false,
      // when mode=sync, output reflectedRecordTables value.
      "reflectedJsonPath": "${mdHome}/reflected_${summaryId}.json"
    }

### Outline

[mode=diff]  

* (diff and sync) create magentadesk database, and table.
* (diff and sync) delete from diff recored passed over 3 hours since created.
* (diff and sync) base and compare, lock execute simultaneously. （FOR UPDATE NOWAIT）
* extract target to diff table.
* register table diff.

[mode=sync]  

* (diff and sync) create magentadesk database, and table.
* (diff and sync) delete from diff recored passed over 3 hours since created.
* (diff and sync) base and compare, lock execute simultaneously. （FOR UPDATE NOWAIT）
* sync table diff used by diff.
* execute any commands.

### Notice

* Use checksum, checksum is different, as a result to extract record diff takes more time.
* In much record, to extract diff may takes much time.
* Mainly, magentadesk is target to 'master data'.

