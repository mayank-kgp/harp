if [ $# != 1 ] ; then
    echo "USAGE: $0 <dataset>"
    echo "i.e. $0 miami.graph" 
    exit 1;
fi

echo "USING NODE:"
hostname

for template in u5-1.fascia 
do
    for thread in 1 4 16 64 256
    do
	for sample in {1..10}
	do
	    echo "Sample $sample of the experiment: fascia -g /N/u/mavram/sahad-implementations/inputgraphs/$1 -t /N/u/mavram/sahad-implementations/templates/$template -thread $thread -r"
	    ./runfasciaMihai.sh "fascia -g /N/u/mavram/sahad-implementations/inputgraphs/$1 -t /N/u/mavram/sahad-implementations/templates/$template -thread $thread -r"
	    #java -cp /N/u/mavram/sahad-implementations/harp-sahad/harp-sahad-master/target/harp-sahad-1.0-SNAPSHOT.jar:/N/u/mavram/hadoop-2.6.0/share/hadoop/common/lib/* fascia.Fascia "fascia -g /N/u/mavram/sahad-implementations/inputgraphs/$1 -t /N/u/mavram/sahad-implementations/templates/$template -thread $thread -r"
	done
    done
done
echo "finished"
