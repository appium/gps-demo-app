#!/bin/bash

pushd app
android update project --subprojects --target android-19 --path . --name gpsDemo
ant clean debug
popd
cp app/bin/gpsDemo-debug.apk assets/

