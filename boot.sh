#!/bin/sh

javac -classpath "src/lib/*": -sourcepath src: src/Main_Client.java

cd src/

java -cp "lib/*": Main_Client
