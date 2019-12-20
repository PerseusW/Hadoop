# 阶段二 实验报告

该报告是**171860611，王麦迪**的工作成果。

## 环境配置

因为版本兼容性，各项组件的版本如下：

| 组件   | 版本      |
| ------ | --------- |
| JDK    | 1.8       |
| Hadoop | 2.10.0    |
| Hive   | 2.3.6     |
| Derby  | 10.14.2.0 |

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
```

```shell
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

至此，我们已经进入了`Hive SQL Shell`，剩下的实验工作都在该环境下完成。

## 实验过程

该实验主要使用SQL语言。

### 需求分析

原始需求：

> Hive操作：
>
> ​	把精简数据集导入到数据仓库Hive中，并对数据仓库Hive中的数据进行查询分析
>
> ​	查询双11那天有多少人购买了商品
>
> ​	查询双11那天男女买家购买商品的比例
>
> ​	查询双11那天浏览次数前十的品牌

开发需求：

- 任务一
  - 导入数据
- 任务二（有改动：人 -> 人次）
  - 筛选action = 2的元组
  - 查询元组数量
- 任务三（有改动：人 -> 人次）
  - 查询gender = 0的元组数量
  - 查询gender = 1的元组数量
  - 与任务二的结果进行比较
- 任务四
  - 筛选action = 0的元组
  - 统计元组数量
  - 按照数量降序排序
  - 选择前10

### 编码实现

从HDFS上导入数据：

```sql
create external table UserLog(userID int,itemID int,catID int,merchantID int,brandID int,month int,day int,action int,ageRange int,gender int,province string) row format delimited fields terminated by ',' stored as textfile location '/user/percy/UserLog';
```

查询购买商品的人次：

```sql
select count(*) from UserLog where action = 2;
```

查询购买商品的人次的男女比例：

```sql
select sum(case when gender = 1 then 1 else 0 end) / count(*), sum(case when gender = 2 then 1 else 0 end) / count(*) from UserLog where action = 2;
```

查询点击量前十的类别：

```sql
select sum(case when action = 0 then 1 else 0 end) as viewed,brandID from UserLog group by brandID order by viewed desc limit 10;
```

### 运行结果

[结果截图](output)

