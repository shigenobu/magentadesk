# magentadesk for cli

### Json Format

* [diff](../doc/json_diff.md)
* [sync](../doc/json_sync.md)
* [maintenance](../doc/json_maintenance.md)

### Exit code

* 0:  success
* 1:  error
* 11: invalid args
* 12: invalid stdin
* 13: invalid input
* 14: invalid version
* 21: disallow simultaneous execution
* 22: in maintenance
* 23: no exists base or compare
* 24: no exists diff seqs
* 31: not success code
* 32: not success status
* 41: error local database (web only)
* 99: unknown

### Outline

(mode=diff)

* (diff and sync) create magentadesk database, and table.
* (diff and sync) delete from diff recored passed over 3 hours since created.
* (diff and sync) base and compare, lock execute simultaneously. （FOR UPDATE NOWAIT）
* check maintenance state between base and compare.
* extract target to diff table.
* register table diff.

(mode=sync)

* (diff and sync) create magentadesk database, and table.
* (diff and sync) delete from diff recored passed over 3 hours since created.
* (diff and sync) base and compare, lock execute simultaneously. （FOR UPDATE NOWAIT）
* check maintenance state between base and compare.
* sync table diff used by diff.
* execute any commands.

(mode=maintenance)

* (diff and sync) create magentadesk database, and table.
* (diff and sync) delete from diff recored passed over 3 hours since created.
* (diff and sync) base and compare, lock execute simultaneously. （FOR UPDATE NOWAIT）
* set maintenance state.


