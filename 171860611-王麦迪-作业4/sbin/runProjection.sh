rm -rf output/
hadoop/bin/hadoop jar jar/Projection.jar Projection input/RelationA/Ra output 1
echo Output:
cat output/*
echo Answer:
cat ans/Projection
