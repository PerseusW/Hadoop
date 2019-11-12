all:
	${HADOOP}/bin/hadoop com.sun.tools.javac.Main code/MapReduce/*.java -d ./
	jar cf jar/KMeans.jar *.class
	rm *.class
start:
	rm -rf /tmp/hadoop*
	${HADOOP}/bin/hdfs namenode -format
	${HADOOP}/sbin/start-dfs.sh
	${HADOOP}/bin/hdfs dfs -mkdir -p /user/percy
	${HADOOP}/bin/hdfs dfs -put input /user/percy/input
	${HADOOP}/sbin/start-yarn.sh
stop:
	${HADOOP}/sbin/stop-dfs.sh
	${HADOOP}/sbin/stop-yarn.sh
run:
	rm -rf output
	${HADOOP}/bin/hdfs dfs -rm -f -r /user/percy/output
	${HADOOP}/bin/hadoop jar jar/KMeans.jar Main /user/percy/input /user/percy/output 5 1
	${HADOOP}/bin/hdfs dfs -get /user/percy/output
