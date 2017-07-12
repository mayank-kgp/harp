#!/bin/bash

# running java-fascia on single haswell node

LIBJARS=/N/u/lc37/Project/Harp-Graph-Counting/harp-sahad/target/harp-sahad-1.0-SNAPSHOT.jar:${HOME}/Lib/HJlib/habanero-java-lib-0.1.4-SNAPSHOT.jar:${HADOOP_HOME}/share/hadoop/common/lib/log4j-1.2.17.jar:${HADOOP_HOME}/share/hadoop/common/lib/commons-cli-1.2.jar

echo $LIBJARS

DataDir=/scratch/lc37/Fascia-Data
ResDir=/N/u/lc37/Project/Harp-Graph-Counting/harp-sahad/Results/Fascia-Java

# if [ $# < 3 ] ; then
# echo "usage: <graph> <template> <threadnum>"
# exit 1;
# fi

graph=miami.graph
# graph=gnp.1.20.graph
template=u5-1.fascia
# template=u3-1.fascia
thd=24
paral=HJLIB
# paral=JThd
Itr=10
# Itr=100
# Itr=1000
# Itr=10000

echo "Start Exp on: $graph with template: $template and thread num: $thd itr: $Itr parallel imple: $paral"
java -cp ${LIBJARS} fascia.Fascia fascia -g ${DataDir}/graphs/$graph -t ${DataDir}/templates/$template -thread $thd -r -i $Itr > $ResDir/Fascia-Java-$graph-$template-Thd$thd-Itr$Itr-Paral-$paral.log 
echo "End Exp on: $graph with template: $template and thread num: $thd itr: $Itr parallel imple: $paral"
