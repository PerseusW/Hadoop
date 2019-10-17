rm -rf output/
hadoop/bin/hadoop jar jar/Union.jar Union input/RelationA output
echo Output:
cat output/*
echo Answer:
cat ans/Union
