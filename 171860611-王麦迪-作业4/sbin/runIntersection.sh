rm -rf output/
hadoop/bin/hadoop jar jar/Intersection.jar Intersection input/RelationA output
echo Output:
cat output/*
mv output/part* ans/Intersection
