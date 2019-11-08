rm -r ~/Desktop/output
${HADOOP}/bin/hdfs dfs -rm -r /user/percy/output
${HADOOP}/bin/hadoop jar ~/Desktop/jar/KMeans.jar Main /user/percy/input /user/percy/output 5 20
${HADOOP}/bin/hdfs dfs -get /user/percy/output ~/Desktop
echo
echo Initial Clusters:
cat ~/Desktop/output/0*/*