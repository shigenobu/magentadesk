### Maintenance

(INPUT)

cli: input mode=maintenance  
web: request /api/maintenance/reserve.json  

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

(OUTPUT)

cli: output mode=maintenance  
web: response 200 /api/maintenance/check.json  

    {
      // メンテナンス結果
      "maintenance":"(on|off)"
    }