#!/bin/bash
# Advances the last number of the given version string by one.
function advance_version () {
    local v=$1
    # Get the last number. First remove any suffixes (such as '-SNAPSHOT').
    local cleaned;
    local last_num;
    cleaned=$(echo $v | sed -e 's/[^0-9][^0-9]*$//')
    last_num=$(echo $cleaned | sed -e 's/[0-9]*\.//g')
    local next_num=$(($last_num+1))
    # Finally replace the last number in version string with the new one.
    echo $v | sed -e "s/[0-9][0-9]*\([^0-9]*\)$/$next_num/"
}

new_version="$(advance_version "$(mvn org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.version -q -DforceStdout)")"
mvn versions:set -DnewVersion="$new_version" -DgenerateBackupPoms=false