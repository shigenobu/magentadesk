### Diff

(入力)  

cli: input mode=diff  
web: request /api/diff/reserve.json  

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
      // （必須）比較元となるデータベース名
      // ----- 独自 -----
      "baseDatabase": "base",
      // （必須）比較先となるデータベース名
      "compareDatabase": "compare",
      // オプション
      "option": {
        // 比較対象となるテーブルのLIKEパターン、複数指定でOR条件となります
        "includeTableLikePatterns": [
          "m\\_*"
        ],
        // 比較対象から外すテーブルのLIKEパターン、複数指定でAND条件となります
        "excludeTableLikePatterns": [
          "m\\_admin\\_%"
        ],
        // trueにすると、AUTO_INCREMENTの現在値を、テーブル構造比較項目の対象から外します
        "ignoreAutoIncrement": false,
        // trueにすると、テーブルコメント・カラムコメント・インデックスコメント・パーティションコメントを、テーブル構造比較項目の対象から外します
        "ignoreComment": false,
        // trueにすると、パーティション定義を、テーブル構造比較項目の対象から外します
        "ignorePartitions": false,
        // trueにすると、default値にsequenceのnextval、lastvalが定義されている場合、テーブル構造比較項目の対象から外します
        "ignoreDefaultForSequence": false
      },
      // 比較条件
      "conditions": [
        {
           // 対象テーブル名と一致したとき、比較条件として比較元／比較先で利用されます
           // 対象テーブル名と一致したとき、'checksum table'のクエリーは実行されず、チェックサム値は偽の値に置換されます
          "tableName":"t1",
          "expression":"upd_date > (now() - interval 10 day)"
        }
      ]
    }

(OUTPUT)  

cli: output mode=diff  
web: response 200 /api/diff/check.json  

    {
      // 比較元にのみ存在するテーブルで、データ差分比較対象外となる
      "existsOnlyBaseTables": [
        {
          "tableName": "t1",
          "tableComment": "",
          "baseDefinition": "CREATE ..."
        }
      ],
      // 比較先にのみ存在するテーブルで、データ差分比較対象外となる
      "existsOnlyCompareTables": [
        {
          "tableName": "t1",
          "tableComment": "",
          "compareDefinition": "CREATE ..."
        }
      ],
      // 外部キー参照をされているテーブル、ビュー、シーケンス、InnoDB以外のストレージエンジン、トリガー定義が異なるテーブルで、
      // データ差分比較対象外となる
      "forceExcludeTables": [
        {
          "tableName": "t1",
          "tableComment": "",
          "definition": "CREATE ...",
          "reason": "(referencedForeignKey|isView|isSequence|notInnoDB|mismatchTrigger)"
        }
      ],
      // 主キーがないテーブル、外部キーを持っているテーブル、有効でないキャラセットで、データ差分比較対象外となる
      "incorrectDefinitionTables": [
        {
          "tableName": "t1",
          "tableComment": "",
          "definition": "CREATE ...",
          "reason": "(noPrimaryKey|hasForeignKey|invalidCharset)"
        }
      ],
      // 比較先と比較元の構造が異なるテーブルで、データ差分比較対象外となる
      "mismatchDefinitionTables": [
        {
          "tableName": "t1",
          "tableComment": "",
          "baseDefinition": "CREATE ...",
          "compareDefinition": "CREATE ..."
        }
      ],
      // データ差分が検出されたテーブルのレコード詳細
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
              // mode=syncで使える差分シーケンスNO
              "diffSeq": XXX,
              "baseValues": [],
              "compareValues": []
            }
          ]
        }
      ],
      // データ差分なしと判定されたテーブル一覧
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
      // mode=syncで使うためのサマリーID
      "summaryId": "XXXXX"
    }
