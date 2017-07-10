#!/bin/sh

if [ $# != 3 ] ; then
    echo "USAGE: $0 <graph> <template> <num_threads>"
    echo "i.e. $0 miami.graph" 
    exit 1;
fi

INPUT_FASCIA_JAVA="fascia -g /N/u/mavram/sahad-implementations/inputgraphs/$1 -t /N/u/mavram/sahad-implementations/templates/$2 -thread $3 -r -i 1"

echo "Starting"
java -cp ~/sahad-implementations/harp-sahad/harp-sahad-master/target/harp-sahad-1.0-SNAPSHOT.jar:/N/u/mavram/hadoop-2.6.0/share/hadoop/common/lib/* fascia.Fascia $INPUT_FASCIA_JAVA
echo "Finished"
