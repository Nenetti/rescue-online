#!/bin/sh

DIR=$1
USER=$2

cd $DIR
git clone -b $USER https://github.com/Ri--one/rescue-online.git sample-$USER
