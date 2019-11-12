HP = /usr/local/hadoop

all:
	${HP}/bin/hadoop com.sun.tools.javac.Main code/MapReduce/*.java -d ./
	jar cf jar/KMeans.jar *.class
	rm *.class
start:
	rm -rf /tmp/hadoop*
	${HP}/bin/hdfs namenode -format
	${HP}/sbin/start-dfs.sh
	${HP}/bin/hdfs dfs -mkdir -p /user/percy
	${HP}/bin/hdfs dfs -put input /user/percy/input
	${HP}/sbin/start-yarn.sh
stop:
	${HP}/sbin/stop-dfs.sh
	${HP}/sbin/stop-yarn.sh
run:
	rm -rf output
	${HP}/bin/hdfs dfs -rm -f -r /user/percy/output
	${HP}/bin/hadoop jar jar/KMeans.jar Main /user/percy/input /user/percy/output 5 1
	${HP}/bin/hdfs dfs -get /user/percy/output
