#!/bin/sh
mvn --also-make --projects web clean test -DfailIfNoTests=false -Dtest=com.walksocket.md.TestWebForever#testForever
