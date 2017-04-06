if [ $# != 2 ] ; then
echo "USAGE: $0 cl <dataset:miami/web-google/gnp1m100e>"
echo " e.g.: $0 cl miami"
exit 1;
fi

for t in {1..2}
do
	for temp in u5-1.t #u3-1.t u5-1.t u7-1.t # u5-2.t u5-3.t u7-1.t #u10-1.t
	do
		i=4
		for d in 0 1 2 3 4 5 6 7 8 9
		do
			./runHarpSgcr.sh $HARP_SAHAD_HOME/experiments/template/$temp sahad-data/$2/$2-u-$1$d-$i sahad-output/$2/$2-u-$1$d-$i/40threads/$t $i 40
			hdfs dfs -cat sahad-output/$2/$2-u-$1$d-$i/40threads/$t/*
        		echo "[The $t times] Above experiment is: ./runHarpSgcr.sh $HARP_SAHAD_HOME/experiments/template/$temp sahad-data/$2/$2-u-$1$d-$i sahad-output/$2/$2-u-$1$d-$i/40threads/$t $i 40"
		done
	done
done

