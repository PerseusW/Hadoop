rm -r ~/Desktop/output
${HADOOP}/bin/hdfs dfs -rm -r /user/percy/output
${HADOOP}/bin/hadoop jar jar/MatrixMultiply.jar MatrixMultiply /user/percy/input/Matrix/M_3_4 /user/percy/input/Matrix/N_4_2 /user/percy/output
echo Output:
${HADOOP}/bin/hdfs dfs -cat /user/percy/output/*
${HADOOP}/bin/hdfs dfs -get /user/percy/output ~/Desktop/output
mv ~/Desktop/output/part* ans/MatrixMultiply
