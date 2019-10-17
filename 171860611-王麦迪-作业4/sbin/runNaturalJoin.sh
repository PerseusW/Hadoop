rm -rf output/
hadoop/bin/hadoop jar jar/NaturalJoin.jar NaturalJoin input/RelationA/Ra input/RelationB/Rb output
echo Output:
cat output/*
mv output/part* ans/NaturalJoin
