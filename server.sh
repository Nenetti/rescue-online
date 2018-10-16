#!/bin/sh

javac -classpath "src/lib/*": -sourcepath src: src/Main_Server.java

cd src/

java -cp "lib/*": Main_Server
