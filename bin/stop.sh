#!/bin/sh

PID=$(ps -ef | grep coffeewx-admin-api-1.0.jar | grep -v grep | awk '{ print $2 }')
if [ -z "$PID" ]
then
    echo Application is already stopped
else
    echo kill $PID
    kill $PID
fi