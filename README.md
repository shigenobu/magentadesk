# magentadesk - Get MariaDB Table Difference, And Reflect.

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

### About

Get diff and sync between two databases in same host created by MariaDB.  

### System Required

* over JDK 1.8
* MariaDB 10.3, 10.4, 10.5, 10.6, 10.7, 10.8, 10.9, 10.10, 10.11, 11.0

### Usage by cli

When put json to stdin, get result from stdout by json.  
Below, it's simple usage.  
Cli document is [here](cli/README.md).  

    echo '${json}' | java -jar magentadesk-cli.jar --mode=${mode}

(args)  

|name|value|remarks|
|----|-----|-------|
|--mode|(required)diff, sync or maintenance|'diff' is get diff, 'sync' is reflected used by diff results, 'maintenance' is under maintenance concerned base and compare.|
|--logPath|log written path.|when 'stdout', write out to stdout, when 'stderr', write out to stderr.|
|--addSeconds|add seconds in log time|default is 60x60x9, it means 'ja', if 0, it means 'en'.|

(full)

    echo '${json}' \
      | [MD_ENV=${mdEnv}] \
        [MD_OUTPUT=${mdOutput}] \
        [MD_LIMIT_LENGTH=${mdLimitLength}] \
        [MD_LIMIT_MISMATCH_COUNT=${mdLimitMismatchCount}] \
        [MD_HOME=${mdHome}] \
          java -jar magentadesk-cli.jar --mode=${mode} [--logPath=${logPath}] [--addSeconds=${addSeconds}]

### Usage by web

No stdin, startup http server.
Below, it's simple usage.  
Web document is [here](web/README.md).

    java -jar magentadesk-web.jar

(args)  

|name|value|remarks|
|----|-----|-------|
|--logPath|log written path.|when 'stdout', write out to stdout, when 'stderr', write out to stderr.|
|--addSeconds|add seconds in log time|default is 60x60x9, it means 'ja', if 0, it means 'en'.|
|--webHost|listen host|default is 0.0.0.0.|
|--webPort|listen port|default is 8710.|

(full)  

    [MD_ENV=${mdEnv}] \
    [MD_OUTPUT=${mdOutput}] \
    [MD_LIMIT_LENGTH=${mdLimitLength}] \
    [MD_LIMIT_MISMATCH_COUNT=${mdLimitMismatchCount}] \
    [MD_HOME=${mdHome}] \
      java -jar magentadesk-web.jar [--logPath=${logPath}] [--addSeconds=${addSeconds}] \
        [--webHost=${webHost}] [--webPort=${webPort}]

### Environment values

|name| remarks                                                                                                                      |
|----|------------------------------------------------------------------------------------------------------------------------------|
|MD_ENV| if 'DEBUG', log in detail.                                                                                                   |
|MD_OUTPUT| if 'PRETTY', write out json result in pretty.                                                                                |
|MD_LIMIT_LENGTH| when diff and over this, returned value is to hash. default is 10000.                                                        |
|MD_LIMIT_MISMATCH_COUNT| when diff and over this, returned mismatch recods is convert empty. default is 10000.                                        |
|MD_HOME| default is ${HOME}/.magentadesk. There is a home directory, by commands which execute in sync and at local database storage. |

### Notice

* Use checksum, checksum is different, as a result to extract record diff takes more time.
* In much record, to extract diff may takes much time.
* Mainly, magentadesk is target to 'master data'.

