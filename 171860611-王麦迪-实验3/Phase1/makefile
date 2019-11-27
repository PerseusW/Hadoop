HD = /usr/local/hadoop
JAR = TopTen.jar

all:
	${HD}/bin/hadoop com.sun.tools.javac.Main code/*.java -d ./
	jar cf jar/${JAR} *.class
	rm *.class
start:
	rm -rf /tmp/hadoop*
	${HD}/bin/hdfs namenode -format
	${HD}/sbin/start-dfs.sh
	${HD}/bin/hdfs dfs -mkdir -p /user/percy
	${HD}/bin/hdfs dfs -put input /user/percy/input
	${HD}/sbin/start-yarn.sh
stop:
	${HD}/sbin/stop-dfs.sh
	${HD}/sbin/stop-yarn.sh
run:
	rm -rf output
	${HD}/bin/hdfs dfs -rm -f -r /user/percy/output
	${HD}/bin/hadoop jar jar/${JAR} Main /user/percy/input /user/percy/output
	${HD}/bin/hdfs dfs -get /user/percy/output
