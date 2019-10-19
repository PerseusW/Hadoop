rm -r ~/Desktop/output
${HADOOP}/bin/hdfs dfs -rm -r /user/percy/output
${HADOOP}/bin/hadoop jar jar/Selection.jar Selection /user/percy/input/RelationA/Ra /user/percy/output 2 18
echo Output:
${HADOOP}/bin/hdfs dfs -cat /user/percy/output/*
${HADOOP}/bin/hdfs dfs -get /user/percy/output ~/Desktop/output
mv ~/Desktop/output/part* ans/Selection1
