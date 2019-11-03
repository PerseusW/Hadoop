rm -r ~/Desktop/output
${HADOOP}/bin/hdfs dfs -rm -r /user/percy/output
${HADOOP}/bin/hadoop jar ~/Desktop/jar/KMeans.jar Main /user/percy/input /user/percy/output 4 4
${HADOOP}/bin/hdfs dfs -get /user/percy/output ~/Desktop
echo
echo Initial Clusters:
cat ~/Desktop/output/0*/*
echo
echo Iteration 1:
cat ~/Desktop/output/1*/*
echo
echo Iteration 2:
cat ~/Desktop/output/2*/*
echo
echo Iteration 3:
cat ~/Desktop/output/3*/*
