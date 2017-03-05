
hdfs dfs -rm -r /sahad-output/test/
./runHarpSgcr.sh $HARP_SAHAD_HOME/experiments/template/u5-1.t /sahad-data/test /sahad-output/test 1 2
hdfs dfs -cat /sahad-output/test/*
