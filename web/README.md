# magentadesk for web

web binary is used for 'api' and 'html'.  
html is default access to `http://localhost:8710`.  
api message format is below.  

### Json Format

* [diff](../doc/json_diff.md)
* [sync](../doc/json_sync.md)
* [maintenance](../doc/json_maintenance.md)

### Outline diff api

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

* see `202`.
* store `executionId` and use next request `/api/diff/check.json`.

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

* see `204`.
* if returned `204`, intermittently send the same request at a certain interval.

RESPONSE(200)

    HTTP/1.1 200 OK
    Content-type: application/json; encoding=UTF8
    Connection: close
    
    {json_diff_output}

* see `200`.
* if returned `200`, store {json_diff_output} response, and used at sync.

### Outline sync api

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

* see `202`.
* store `executionId` and use next request `/api/sync/check.json`.

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

* see `204`.
* if returned `204`, intermittently send the same request at a certain interval.

RESPONSE(200)

    HTTP/1.1 200 OK
    Content-type: application/json; encoding=UTF8
    Connection: close
    
    {json_sync_output}

* see `200`.
* if returned `200`, use {json_sync_output} response.

### Outline maintenance api

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

* see `202`.
* store `executionId` and use next request `/api/maintenance/check.json`.

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

* see `204`.
* if returned `204`, intermittently send the same request at a certain interval.

RESPONSE(200)

    HTTP/1.1 200 OK
    Content-type: application/json; encoding=UTF8
    Connection: close
    
    {json_maintenance_output}

* see `200`.
* if returned `200`, use {json_maintenance_output} response.
