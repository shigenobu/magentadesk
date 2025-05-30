#!/bin/sh
mvn --also-make --projects web clean test -Dsurefire.failIfNoSpecifiedTests=false -Dtest=com.walksocket.md.TestWebForever#testForever
