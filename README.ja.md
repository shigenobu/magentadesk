# magentadesk - Get MariaDB Table Difference, And Reflect.

### 概要

MariaDBの同一ホスト内の2つのデータベースの差分を取り、同期を行います。  

### システム要件

* JDK 1.8 以上
* MariaDB 10.3, 10.4, 10.5, 10.6, 10.7, 10.8, 10.9, 10.10, 10.11

### コマンド

標準入力にて、所定フォーマットのJSON文字列を渡すと、  
標準出力にて、結果のJSONが取得できます。  
以下は最も単純なコマンド例となります。  
CLI版のドキュメントは[こちら](cli/README.ja.md)。  

    echo '${json}' | java -jar magentadesk-cli.jar --mode=${mode}

(引数)  

|名称|値|説明|
|---|---|---|
|--mode|（必須）diff, sync または maintenance|diffは差分取得、syncは取得した差分の反映、maintenanceは比較元・比較先の組み合わせのメンテナンス状態を制御します|
|--logPath|ログを出力するファイルパス|stdoutとすると標準出力へ、stderrとすると標準エラー出力に出力します|
|--addSeconds|ログに記載される時刻を補正する数字|デフォルトは60x60x9、つまり日本時間となります|

(フル)  

    echo '${json}' \
      | [MD_ENV=${mdEnv}] \
        [MD_OUTPUT=${mdOutput}] \
        [MD_LIMIT_LENGTH=${mdLimitLength}] \
        [MD_LIMIT_MISMATCH_COUNT=${mdLimitMismatchCount}] \
        [MD_HOME=${mdHome}] \
          java -jar magentadesk-cli.jar --mode=${mode} [--logPath=${logPath}] [--addSeconds=${addSeconds}]

### WEB

標準入力不要で、HTTPサーバが立ち上がります。  
以下は最も単純なコマンド例となります。  
WEB版のドキュメントは[こちら](web/README.ja.md)。

    java -jar magentadesk-web.jar

|名称|値|説明|
|---|---|---|
|--logPath|ログを出力するファイルパス|stdoutとすると標準出力へ、stderrとすると標準エラー出力に出力します。|
|--addSeconds|ログに記載される時刻を補正する数字|デフォルトは60x60x9、つまり日本時間となります。|
|--webHost|待受ホスト|デフォルは0.0.0.0。|
|--webPort|待受ポート|デフォルは8710。|

(フル)  

    [MD_ENV=${mdEnv}] \
    [MD_OUTPUT=${mdOutput}] \
    [MD_LIMIT_LENGTH=${mdLimitLength}] \
    [MD_LIMIT_MISMATCH_COUNT=${mdLimitMismatchCount}] \
    [MD_HOME=${mdHome}] \
      java -jar magentadesk-web.jar [--logPath=${logPath}] [--addSeconds=${addSeconds}] \
        [--webHost=${webHost}] [--webPort=${webPort}]

### 環境変数  

|名称| 説明                                                                       |
|---|--------------------------------------------------------------------------|
|MD_ENV| DEBUGを指定すると、詳細なログを出力します。                                                 |
|MD_OUTPUT| PRETTYを指定すると、標準出力の結果のJSONが整形されます。                                        |
|MD_LIMIT_LENGTH| diffの実行結果の値において、このバイト数を超えると、ハッシュ化されます。デフォルト10000です。                      |
|MD_LIMIT_MISMATCH_COUNT| diffの実行結果の'mismatchRecordTables.records'の件数がこれを超えると、空になります。デフォルト10000です。 |
|MD_HOME| 指定しないと、${HOME}/.magentadeskとなり、ここにsync時の任意コマンド実行時の一時ファイルが格納されます。         |

### 注意事項

* チェックサムを用いているため、レコード差分が多いほど、差分抽出に時間がかかります
* 大量レコード（100万以上）あると、差分抽出に時間がかかります
* 主にマスターデータといった類のものが対象となることを想定しています
