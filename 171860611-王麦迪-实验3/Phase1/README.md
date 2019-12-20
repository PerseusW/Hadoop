# 阶段一 实验报告

该报告是**171860611，王麦迪**的工作成果。

## 环境配置

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

## 实验过程

该实验主要使用Java语言。

### 需求分析

原始需求：

> 基于精简数据集完成MapReduce作业：
>
> ​	统计各省的双十一前十热门关注产品（“点击+添加购物车+购买+关注”总量最多前10的产品）
>
> ​	统计各省的双十一前十热门销售产品（购买最多前10的产品）

开发需求：

- 任务一
  - 按照province分组
  - 统计itemID出现的次数
  - 按照次数降序排序
  - 选择前10名写入文件系统
- 任务二
  - 筛选出action = 2的元组
  - 按照province分组
  - 统计itemID出现的次数
  - 按照次数降序排序
  - 选择前10

### 模块划分

- 主模块
  - 程序入口
  - 指定各个Job实例的输入输出路径
  - 指定各个Job实例所使用的Mapper和Reducer
  - 统筹各个Job实例的运行顺序
- 数据模型
  - 定义元组字段
- 数据操作
  - 实现算法

### 编码实现

[源码](code)

### 运行结果

[编译脚本](makefile)

[编译结果](jar)

[输入文件](input)

[输出结果](output)

