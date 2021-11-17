# magentadesk for web

webバイナリでは、'api'としても'html'としても機能します。  
htmlの場合、デフォルトで`http://localhost:8710`でアクセスできます。  
apiの場合のメッセージ仕様は以下となります。  

### Jsonフォーマット

* [diff](../doc/json_diff.ja.md)
* [sync](../doc/json_sync.ja.md)
* [maintenance](../doc/json_maintenance.ja.md)

### 差分API概要

##### /api/diff/reserve.json

REQUEST  

    POST /api/diff/reserve.json HTTP/1.1
    Host: {yourHost}
    User-Agent: {yourUA}
    
    {json_diff_input}

RESPONSE(202)  

    HTTP/1.1 202 Accepted
    Content-type: application/json; encoding=UTF8
    Connection: close
    Content-length: 35
    X-execution-id: {executionId}

* 受付が完了すると`202`が返ってきます。
* このとき`executionId`が返ってくるので、それを`/api/diff/check.json`のリクエストヘッダーに使ってください。

##### /api/diff/check.json

REQUEST

    POST /api/diff/check.json HTTP/1.1
    Host: {yourHost}
    User-Agent: {yourUA}
    X-execution-id: {executionId}

RESPONSE(204)

    HTTP/1.1 204 No Content
    Content-type: application/json; encoding=UTF8
    Connection: close
    X-execution-id: {executionId}

* まだ処理が完了していないときは、`204`が返ってきます。
* `204`が返ってきたときは、同一リクエストを一定間隔で継続して実行してください。

RESPONSE(200)

    HTTP/1.1 200 OK
    Content-type: application/json; encoding=UTF8
    Connection: close
    
    {json_diff_output}

* `200`が返ってきたときは、差分検出完了となります。
* このとき、{json_diff_output}のレスポンスを、次のsyncのときに使ってください。

### 同期API概要

##### /api/sync/reserve.json

REQUEST

    POST /api/sync/reserve.json HTTP/1.1
    Host: {yourHost}
    User-Agent: {yourUA}
    
    {json_sync_input}

RESPONSE(202)

    HTTP/1.1 202 Accepted
    Content-type: application/json; encoding=UTF8
    Connection: close
    Content-length: 35
    X-execution-id: {executionId}

* 受付が完了すると`202`が返ってきます。
* このとき`executionId`が返ってくるので、それを`/api/sync/check.json`のリクエストヘッダーに使ってください。

##### /api/sync/check.json

REQUEST

    POST /api/sync/check.json HTTP/1.1
    Host: {yourHost}
    User-Agent: {yourUA}
    X-execution-id: {executionId}

RESPONSE(204)

    HTTP/1.1 204 No Content
    Content-type: application/json; encoding=UTF8
    Connection: close
    X-execution-id: {executionId}

* まだ処理が完了していないときは、`204`が返ってきます。
* `204`が返ってきたときは、同一リクエストを一定間隔で継続して実行してください。

RESPONSE(200)

    HTTP/1.1 200 OK
    Content-type: application/json; encoding=UTF8
    Connection: close
    
    {json_sync_output}

* `200`が返ってきたときは、同期完了となります。
* このとき、{json_sync_output}のレスポンスを、必要に応じて使ってください。

### メンテナンスAPI概要

##### /api/maintenance/reserve.json

REQUEST

    POST /api/maintenance/reserve.json HTTP/1.1
    Host: {yourHost}
    User-Agent: {yourUA}
    
    {json_maintenance_input}

RESPONSE(202)

    HTTP/1.1 202 Accepted
    Content-type: application/json; encoding=UTF8
    Connection: close
    Content-length: 35
    X-execution-id: {executionId}

* 受付が完了すると`202`が返ってきます。
* このとき`executionId`が返ってくるので、それを`/api/maintenance/check.json`のリクエストヘッダーに使ってください。

##### /api/maintenance/check.json

REQUEST

    POST /api/maintenance/check.json HTTP/1.1
    Host: {yourHost}
    User-Agent: {yourUA}
    X-execution-id: {executionId}

RESPONSE(204)

    HTTP/1.1 204 No Content
    Content-type: application/json; encoding=UTF8
    Connection: close
    X-execution-id: {executionId}

* まだ処理が完了していないときは、`204`が返ってきます。
* `204`が返ってきたときは、同一リクエストを一定間隔で継続して実行してください。

RESPONSE(200)

    HTTP/1.1 200 OK
    Content-type: application/json; encoding=UTF8
    Connection: close
    
    {json_maintenance_output}

* `200`が返ってきたときは、メンテナンス処理完了となります。
* このとき、{json_maintenance_output}のレスポンスを、必要に応じて使ってください。
