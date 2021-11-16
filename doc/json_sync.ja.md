### Sync

(INPUT)

cli: input mode=sync  
web: request /api/sync/reserve.json  

    {
      // ----- 共通 -----
      // （必須）接続先ホスト
      "host": "127.0.0.1",
      // （必須）接続先ポート
      "port": 13306,
      // （必須）接続ユーザ、 'base'データベース、 'compare'データベース、 'magentadesk'データベースの操作権限が必要です
      // 接続時はinformation_schemaを利用します
      "user": "root",
      // （必須）接続ユーザパスワード
      "pass": "pass",
      // （必須）文字コード、utf8, utf8mb3 または utf8mb4 のみ
      "charset": "utf8mb4",
      // ----- 独自 -----
      // （必須）mode=diffの出力結果に記載されているサマリーID
      "summaryId": "XXXXX",
      // mode=diffの出力結果に記載されている差分シーケンスNOで、指定することでそれだけを判定対象とすることが可能
      "diffSeqs": [],
      // デフォルトfalseであり、trueで本実行となる
      "run": false,
      // デフォルトfalseであり、trueとすると、比較元に無く比較先に存在するレコードを強制的に削除する
      "force": false,
      // デフォルトfalseであり、trueとすると、指定したサマリーIDに関連する差分結果のみをクリアします
      // falseのままだと、比較元・比較先の組み合わせで生成済みの差分結果もクリアします
      // 反映対象が完全に分離できる場合のみ、trueが活用できると思います
      "loose": false,
      // コミット直前に実行したいコマンド一覧
      "commandsBeforeCommit": [
        {
          // （必須）コマンド
          "command": "XXX",
          // デフォルト30秒
          "timeout": 10,
          // デフォルト[0]。コマンドの結果コードがここに含まれない場合、ロールバックが発生します。
          "successCodeList": [0, 23]
        }
      ],
      // コミット直後に実行したいコマンド一覧
      "commandsAfterCommit": [
        {
          // （必須）コマンド
          "command": "XXX",
          // デフォルト30秒
          "timeout": 10
        }
      ],
      // コミット直前に呼び出したいコールバックAPI一覧
      "httpCallbackBeforeCommit": [
        {
          // (必須) コールバックURL
          "url": "http://localhost:9000/before.php",
          // デフォルト30秒
          "timeout": 10,
          // デフォルト[200]. コールバックAPIのHTTPステータスコードがここに含まれない場合、ロールバックが発生します。
          "successStatusList": [200, 201]
        }
      ],
      // コミット直後に呼び出したいコールバックAPI一覧
      "httpCallbackAfterCommit": [
        {
          // (必須) コールバックURL
          "url": "http://localhost:9000/after.php",
          // デフォルト30秒
          "timeout": 10
        }
      ]
    }

(OUTPUT)

cli: output mode=sync  
web: response 200 /api/sync/check.json  


    {
      // 反映されたレコード一覧
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
      // 反映されなかったレコード一覧（比較元に無く比較先に存在する場合で、比較先を削除しなかった場合のみ）
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
      // コミット直前に実行されたコマンド結果
      "commandResultsBeforeCommit": [
        {
          "command": "XXX",
          "code": XXX,
          "output": "XXX"
        }
      ],
      // コミット直後に実行されたコマンド結果
      "commandResultsAfterCommit": [
        {
          "command": "XXX",
          "code": XXX,
          "output": "XXX"
        }
      ],
      // コミット直前に実行されたAPI結果
      "httpResultsBeforeCommit": [
        {
          "url: "XXX",
          "status": 200,
          "body": ""
        }
      ],
      // コミット直後に実行されたAPI結果
      "httpResultsAfterCommit": [
        {
          "url: "XXX",
          "status": 200,
          "body": ""
        }
      ],
      // 入力で使われたサマリーID
      "summaryId": "XXXXX"
    }

---

##### 実行されるコマンド

    echo '{"run":true, "reflectedJsonPath":"path_to_reflected_records.json"}' | ${command}

'run'は、入力JSONの'run'となるので、本実行かどうかをコマンド内で区別するのに利用できます。  
'reflectedJsonPath'ファイルのJSONフォーマットは、出力JSONの'reflectedRecordTables'となります。

##### コールバックAPI実行時に送信されるリクエスト

    POST {URL} HTTP/1.1
    Host: {HOST}
    User-Agent: magentadesk-http-client
    Content-type: application/json; charset=UTF8
    
    {"run":true, "reflectedRecordTables":[]}

'run'は、入力JSONの'run'となるので、本実行かどうかをコマンド内で区別するのに利用できます。  
'reflectedRecordTables'は、出力JSONの'reflectedRecordTables'となります。

