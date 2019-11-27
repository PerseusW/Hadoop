# Phase 2 Hive

This part is also finished, see the [screenshots](screenshots) for the result.

I degraded component versions back to:

| Component | Version   |
| --------- | --------- |
| JDK       | 1.8       |
| Hadoop    | 2.10.0    |
| Hive      | 2.3.6     |
| Derby     | 10.14.2.0 |

```shell
sudo apt update
sudo apt install ssh
ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
chmod 0600 ~/.ssh/authorized_keys

tar -xzvf jdk-8u221-linux-x64.tar.gz
sudo mv jdk1.8.0_221 /usr/local/java

tar -xzvf hadoop-2.10.0.tar.gz
sudo mv hadoop-2.10.0 /usr/local/hadoop

tar -xzvf apache-hive-2.3.6-bin.tar.gz
sudo mv apache-hive-2.3.6-bin /usr/local/hive

tar -xzvf db-derby-10.14.2.0-bin.tar.gz
sudo mv db-derby-10.14.2.0-bin /usr/local/derby

export JAVA_HOME=/usr/local/java
export PATH=$PATH:$JAVA_HOME/bin
export HADOOP_HOME=/usr/local/hadoop
export HIVE_HOME=/usr/local/hive
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

```shell
$HADOOP_HOME/bin/hdfs namenode -format
$HADOOP_HOME/sbin/start-dfs.sh
$HADOOP_HOME/sbin/start-yarn.sh
$HADOOP_HOME/bin/hadoop fs -mkdir /tmp
$HADOOP_HOME/bin/hadoop fs -mkdir -p /user/hive/warehouse
$HADOOP_HOME/bin/hadoop fs -mkdir -p /user/percy/UserLog
$HADOOP_HOME/bin/hadoop fs -chmod g+x /tmp
$HADOOP_HOME/bin/hadoop fs -chmod g+x /user/hive/warehouse
$HADOOP_HOME/bin/hadoop fs -put /home/percy/Desktop/million_user_log /user/percy/UserLog
$HIVE_HOME/bin/schematool -initSchema -dbType derby
$HIVE_HOME/bin/hive
```

By now, I've entered `Hive SQL Shell`. And this phase is completed by writing SQL.

First, import the data into the database:

```sql
create external table UserLog(userID int,itemID int,catID int,merchantID int,brandID int,month int,day int,action int,ageRange int,gender int,province string) row format delimited fields terminated by ',' stored as textfile location '/user/percy/UserLog';
```

Second, I should delete the `userIDs` that are sometimes men and sometimes women. But it's really hard to delete tuples in `Hive` and the teacher said that it's ok if we don't wash the data. So instead of calculating the people who bought an item, I'm calculating the number of items bought by people:

```sql
select count(*) from UserLog where action = 2;
```

And instead of calculating the percentage of male and female buyers, I'm calculating the percentage of items bought by a male or female:

```sql
select sum(case when gender = 1 then 1 else 0 end) / count(*), sum(case when gender = 2 then 1 else 0 end) / count(*) from UserLog where action = 2;
```

This part doesn't have modifications since it doesn't have anything to do with gender:

```sql
select sum(case when action = 0 then 1 else 0 end) as viewed,brandID from UserLog group by brandID order by viewed desc limit 10;
```

