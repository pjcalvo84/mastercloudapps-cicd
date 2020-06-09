#!/bin/sh
echo $0
echo $1
echo $2
sed -i "s/<version>.*-.*<\/version>/<version>$0<\/version>/" $1