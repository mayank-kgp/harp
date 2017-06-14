if [ $# != 1 ] ; then
echo "USAGE: $0  <dataset:nyc/miami>"
echo " e.g.: $0 miami"
exit 1;
fi

for t in {1..10}
do
	for temp in u5-1.fascia #u3-1.t u5-1.t u7-1.t # u5-2.t u5-3.t u7-1.t #u10-1.t
	do
		for thread in 1 4 16 64 256 #16 18 20 22 24 26 28 30 32 34 36 38 40 42 44 46 48
		do
			export OMP_NUM_THREADS=$thread
			/N/u/mavram/sahad-implementations/fascia-cpp/fascia-psu-code/fascia -g /N/u/mavram/sahad-implementations/inputgraphs/$1 -t /N/u/mavram/sahad-implementations/templates/$temp -i 1 -r	
        		echo "[The $t times] Above experimet: /N/u/mavram/sahad-implementations/fascia-cpp/fascia-psu-code/fascia -g /N/u/mavram/sahad-implementations/inputgraphs/$1 -t /N/u/mavram/sahad-implementations/templates/$temp -r  -i 1 -thread $OMP_NUM_THREADS"
		done
	done
done

