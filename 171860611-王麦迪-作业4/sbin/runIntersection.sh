rm -r ~/Desktop/output
${HADOOP}/bin/hdfs dfs -rm -r /user/percy/output
${HADOOP}/bin/hadoop jar jar/Intersection.jar Intersection /user/percy/input/RelationA /user/percy/output
echo Output:
${HADOOP}/bin/hdfs dfs -cat /user/percy/output/*
${HADOOP}/bin/hdfs dfs -get /user/percy/output ~/Desktop/output
mv ~/Desktop/output/part* ans/Intersection
