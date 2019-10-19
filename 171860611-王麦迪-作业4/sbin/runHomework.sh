${HADOOP}/bin/hdfs dfs -rm -r /user/percy/output
${HADOOP}/bin/hadoop jar jar/MatrixMultiply.jar MatrixMultiply /user/percy/input/Matrix/M_3_4 /user/percy/input/Matrix/N_4_2 /user/percy/output
${HADOOP}/bin/hdfs dfs -get /user/percy/output/part* ~/Desktop/ans/MatrixMultiply
echo
echo Matrix Multiply:
${HADOOP}/bin/hdfs dfs -cat /user/percy/output/*
echo

${HADOOP}/bin/hdfs dfs -rm -r /user/percy/output
${HADOOP}/bin/hadoop jar jar/Selection.jar Selection /user/percy/input/RelationA/Ra /user/percy/output 2 18
${HADOOP}/bin/hdfs dfs -get /user/percy/output ~/Desktop/ans/Selection1
echo
echo Selection1:
${HADOOP}/bin/hdfs dfs -cat /user/percy/output/*
echo

${HADOOP}/bin/hdfs dfs -rm -r /user/percy/output
${HADOOP}/bin/hadoop jar jar/Selection.jar Selection /user/percy/input/RelationA/Ra /user/percy/output 2 s18
${HADOOP}/bin/hdfs dfs -get /user/percy/output ~/Desktop/ans/Selection2
echo
echo Selection2:
${HADOOP}/bin/hdfs dfs -cat /user/percy/output/*
echo

${HADOOP}/bin/hdfs dfs -rm -r /user/percy/output
${HADOOP}/bin/hadoop jar jar/Selection.jar Selection /user/percy/input/RelationA/Ra /user/percy/output 2 g18
${HADOOP}/bin/hdfs dfs -get /user/percy/output ~/Desktop/ans/Selection3
echo
echo Selection3:
${HADOOP}/bin/hdfs dfs -cat /user/percy/output/*
echo

${HADOOP}/bin/hdfs dfs -rm -r /user/percy/output
${HADOOP}/bin/hadoop jar jar/Projection.jar Projection /user/percy/input/RelationA/Ra /user/percy/output 1
${HADOOP}/bin/hdfs dfs -get /user/percy/output ~/Desktop/ans/Projection
echo
echo Projection:
${HADOOP}/bin/hdfs dfs -cat /user/percy/output/*
echo

${HADOOP}/bin/hdfs dfs -rm -r /user/percy/output
${HADOOP}/bin/hadoop jar jar/Union.jar Union /user/percy/input/RelationA /user/percy/output
${HADOOP}/bin/hdfs dfs -get /user/percy/output ~/Desktop/ans/Union
echo
echo Union:
${HADOOP}/bin/hdfs dfs -cat /user/percy/output/*
echo

${HADOOP}/bin/hdfs dfs -rm -r /user/percy/output
${HADOOP}/bin/hadoop jar jar/Intersection.jar Intersection /user/percy/input/RelationA /user/percy/output
${HADOOP}/bin/hdfs dfs -get /user/percy/output ~/Desktop/ans/Intersection
echo
echo Intersection:
${HADOOP}/bin/hdfs dfs -cat /user/percy/output/*
echo

${HADOOP}/bin/hdfs dfs -rm -r /user/percy/output
${HADOOP}/bin/hadoop jar jar/Difference.jar Difference /user/percy/input/RelationA /user/percy/output /user/percy/input/RelationA/Ra
${HADOOP}/bin/hdfs dfs -get /user/percy/output ~/Desktop/ans/Difference
echo
echo Difference:
${HADOOP}/bin/hdfs dfs -cat /user/percy/output/*
echo

${HADOOP}/bin/hdfs dfs -rm -r /user/percy/output
${HADOOP}/bin/hadoop jar jar/NaturalJoin.jar NaturalJoin /user/percy/input/RelationA/Ra /user/percy/input/RelationB/Rb /user/percy/output
${HADOOP}/bin/hdfs dfs -get /user/percy/output ~/Desktop/ans/NaturalJoin
echo
echo NaturalJoin:
${HADOOP}/bin/hdfs dfs -cat /user/percy/output/*
