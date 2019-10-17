rm -rf output/
hadoop/bin/hadoop jar jar/Difference.jar Difference input/RelationA output input/RelationA/Ra
echo Output:
cat output/*
mv output/part* ans/Difference
