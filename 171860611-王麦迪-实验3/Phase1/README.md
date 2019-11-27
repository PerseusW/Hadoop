# Phase 1 MapReduce

First and foremost, this part is finished, see my results via this [link](output).

I do not plan on making comments on the algorithm, since that is the easy part. The hard part, as always of this course, is environment setup. I am recording the commands for setting up my environment here. There is no other point to this README.

```shell
sudo apt update
sudo apt install make
sudo apt install ssh
ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
chmod 0600 ~/.ssh/authorized_keys

tar -xzvf jdk-8u221-linux-x64.tar.gz
sudo mv jdk1.8.0_221 /usr/local/java

tar -xzvf hadoop-3.2.1.tar.gz
sudo mv hadoop-3.2.1 /usr/local/hadoop

export JAVA_HOME=/usr/local/java
export PATH=$PATH:$JAVA_HOME/bin
export HADOOP_HOME=/usr/local/hadoop
```

```shell
# $HADOOP_HOME/etc/hadoop/hadoop-env.sh
export JAVA_HOME=/usr/local/java
```

```xml
<!--$HADOOP_HOME/etc/hadoop/core-site.xml-->
<configuration>   
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://localhost:9000</value>
    </property>
</configuration>

<!--$HADOOP_HOME/etc/hadoop/hdfs-site.xml-->
<configuration>
    <property>
        <name>dfs.replication</name>
        <value>1</value>
    </property>
</configuration>

<!--$HADOOP_HOME/etc/hadoop/mapred-site.xml-->
<configuration>
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
    <property>
        <name>mapreduce.application.classpath</name>
        <value>$HADOOP_MAPRED_HOME/share/hadoop/mapreduce/*:$HADOOP_MAPRED_HOME/share/hadoop/mapreduce/lib/*</value>
    </property>
</configuration>

<!--$HADOOP_HOME/etc/hadoop/yarn-site.xml-->
<configuration>
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
    <property>
        <name>yarn.nodemanager.env-whitelist</name>
        <value>JAVA_HOME,HADOOP_COMMON_HOME,HADOOP_HDFS_HOME,HADOOP_CONF_DIR,CLASSPATH_PREPEND_DISTCACHE,HADOOP_YARN_HOME,HADOOP_MAPRED_HOME</value>
    </property>
</configuration>
```

