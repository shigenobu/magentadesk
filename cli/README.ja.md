# magentadesk for cli

### Jsonフォーマット

* [diff](../doc/json_diff.ja.md)
* [sync](../doc/json_sync.ja.md)
* [maintenance](../doc/json_maintenance.ja.md)

### 処理概要

(mode=diffのとき)  

* （共通）magentadeskデータベースの作成および、関連テーブルの作成
* （共通）生成から3時間以上経過しているサマリーID関連レコードの削除
* （共通）比較元・比較先の組み合わせで同時実行制御をかける（FOR UPDATE NOWAIT）
* メンテナンス状態の判定
* 比較対象外とするテーブルの抽出
* 比較対象テーブルの差分抽出およびその時点の差分状態登録

(mode=syncのとき)  

* （共通）magentadeskデータベースの作成および、関連テーブルの作成
* （共通）生成から3時間以上経過しているサマリーID関連レコードの削除
* （共通）比較元・比較先の組み合わせで同時実行制御をかける（FOR UPDATE NOWAIT）
* メンテナンス状態の判定
* 抽出された差分の反映
* 任意コマンドの実行

(mode=maintenanceのとき) 

* （共通）magentadeskデータベースの作成および、関連テーブルの作成
* （共通）生成から3時間以上経過しているサマリーID関連レコードの削除
* （共通）比較元・比較先の組み合わせで同時実行制御をかける（FOR UPDATE NOWAIT）
* メンテナンス状態の設定


