if [ $# != 1 ] ; then
echo "usage: $0 <the parameters> (must be quoted as one string)"
exit 1;
fi

java -cp ~/sahad-implementations/harp-sahad/harp-sahad-master/target/harp-sahad-1.0-SNAPSHOT.jar:/N/u/mavram/hadoop-2.6.0/share/hadoop/common/lib/* fascia.Fascia $1
