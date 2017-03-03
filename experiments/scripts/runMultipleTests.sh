if [ $# != 2 ] ; then
echo "USAGE: $0 e/m/r <dataset:miami/web-google/gnp1m100e>"
echo " e.g.: $0 e web-google"
exit 1;
fi

for t in {1..5}
do
	for temp in u5-1.t u5-2.t u5-3.t u7-1.t u10-1.t
	do
		for i in 1 2 4 6 8 10 12 14
		do
			./runHarpSgcr.sh $HARP_SAHAD_HOME/experiments/template/$temp $2-u-$1-$i out-scgr $i 40
			hdfs dfs -cat out-scgr/*
        		echo "[The $t times] Above experiment is: ./runHarpSgcr.sh $HARP_SAHAD_HOME/experiments/template/$temp $2-u-$1-$i out-scgr $i 40"
		done
	done
done

