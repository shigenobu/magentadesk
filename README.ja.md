# magentadesk - Get MariaDB Table Difference, And Reflect.

### 概要

MariaDBの同一ホスト内の2つのデータベースの差分を取り、同期を行います。  

### システム要件

* JDK 1.8 以上
* MariaDB 10.3, 10.4, 10.5, 10.6

### コマンド

標準入力にて、所定フォーマットのJSON文字列を渡すと、  
標準出力にて、結果のJSONが取得できます。  
以下は最も単純なコマンド例となります。  

    echo '${json}' | java -jar magentadesk.jar --mode=${mode}

[引数]  

|名称|値|説明|
|---|---|---|
|--mode|（必須）diff, sync または maintenance|diffは差分取得、syncは取得した差分の反映、maintenanceは比較元・比較先の組み合わせのメンテナンス状態を制御します|
|--logPath|ログを出力するファイルパス|stdoutとすると標準出力へ、stderrとすると標準エラー出力に出力します|
|--addSeconds|ログに記載される時刻を補正する数字|デフォルトは60x60x9、つまり日本時間となります|

[環境変数]  

|名称|説明|
|---|---|
|MD_ENV|DEBUGを指定すると、詳細なログを出力します。|
|MD_OUTPUT|PRETTYを指定すると、標準出力の結果のJSONが整形されます。|
|MD_LIMIT_LENGTH|diffの実行結果の値において、このバイト数以上になると、ハッシュ化されます。デフォルト1000です。|
|MD_HOME|指定しないと、${HOME}/.magentadeskとなり、ここにsync時の任意コマンド実行時の一時ファイルが格納されます。|

引数、環境変数を考慮した形の完全なコマンド例は以下となります。  

    echo '${json}' \
      | [MD_ENV=${mdEnv}] [MD_OUTPUT=${mdOutput}] [MD_LIMIT_LENGTH=${mdLimitLength}] [MD_HOME=${mdHome}] \
        java -jar magentadesk.jar --mode=${mode} [--logPath=${logPath}] [--addSeconds=${addSeconds}]


### JSONフォーマット

[入力JSON]

__mode=diffのとき__

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

__mode=syncのとき__

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
          "timeout": 10
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
      ]
    }

__mode=maintenanceのとき__

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
      // （必須）onでメンテナンス状態、offでメンテナンス解除
      // 比較元・比較先の組み合わせで、メンテナンス状態を制御します
      "maintenance":"(on|off)"
    }

[出力JSON]

__mode=diffのとき__

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

__mode=syncのとき__

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
      // 入力で使われたサマリーID
      "summaryId": "XXXXX"
    }

（任意のコマンドが実行されるイメージ）  

    echo '{"run":true, "reflectedJsonPath":"path_to_reflected_records.json"}' | ${command}

'run'は、入力JSONの'run'となるので、本実行かどうかをコマンド内で区別するのに利用できます。  
'reflectedJsonPath'ファイルのJSONフォーマットは、出力JSONの'reflectedRecordTables'となります。  

__mode=maintenanceのとき__

    {
      // メンテナンス結果
      "maintenance":"(on|off)"
    }

### 処理概要

[mode=diffのとき]  

* （共通）magentadeskデータベースの作成および、関連テーブルの作成
* （共通）生成から3時間以上経過しているサマリーID関連レコードの削除
* （共通）比較元・比較先の組み合わせで同時実行制御をかける（FOR UPDATE NOWAIT）
* メンテナンス状態の判定
* 比較対象外とするテーブルの抽出
* 比較対象テーブルの差分抽出およびその時点の差分状態登録

[mode=syncのとき]  

* （共通）magentadeskデータベースの作成および、関連テーブルの作成
* （共通）生成から3時間以上経過しているサマリーID関連レコードの削除
* （共通）比較元・比較先の組み合わせで同時実行制御をかける（FOR UPDATE NOWAIT）
* メンテナンス状態の判定
* 抽出された差分の反映
* 任意コマンドの実行

[mode=maintenanceのとき]  

* （共通）magentadeskデータベースの作成および、関連テーブルの作成
* （共通）生成から3時間以上経過しているサマリーID関連レコードの削除
* （共通）比較元・比較先の組み合わせで同時実行制御をかける（FOR UPDATE NOWAIT）
* メンテナンス状態の設定

### 注意事項

* チェックサムを用いているため、レコード差分が多いほど、差分抽出に時間がかかります
* 大量レコード（100万以上）あると、差分抽出に時間がかかります
* 主にマスターデータといった類のものが対象となることを想定しています


