rm -rf /tmp/hadoop*
${HADOOP}/bin/hdfs namenode -format
${HADOOP}/sbin/start-dfs.sh
${HADOOP}/bin/hdfs dfs -mkdir -p /user/percy
${HADOOP}/bin/hdfs dfs -put ~/Desktop/input /user/percy/input
${HADOOP}/sbin/start-yarn.sh