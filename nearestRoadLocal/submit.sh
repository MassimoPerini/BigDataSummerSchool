#!/bin/bash

JAR=target/kmeans-0.0.1-SNAPSHOT.jar
OUT=kmeans.out
IN=order201510-small.csv
OUT1=test1.out
OUT2=test2.out


hdfs dfs -rm -r $OUT
hdfs dfs -rm -r $OUT1
hdfs dfs -rm -r $OUT2
spark-submit --verbose --master yarn-cluster --class bigdata.ml.App $JAR $IN 10 100 $OUT $OUT1 $OUT2

