## Quick Overview

Pseudo-Distributed Operation with YARN:

1. Install `SSH`
2. Configure Hadoop:

```xml
<!-- etc/hadoop/core-site.xml -->
<configuration>
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://localhost:9000</value>
    </property>
</configuration>

<!-- etc/hadoop/hdfs-site.xml -->
<configuration>
    <property>
        <name>dfs.replication</name>
        <value>1</value>
    </property>
</configuration>

<!-- etc/hadoop/mapred-site.xml -->
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

<!-- etc/hadoop/yarn-site.xml -->
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



But if you want to learn what Hadoop is actually doing and how it runs, I strongly recommend you to continue. We will be using examples within the Hadoop packet itself, so **no** coding will be required.

The example we will be using is within a `jar` packet that is under `share` directory. The example is called `grep`, a common string matcher that is used all over the computer industry and follows the well known **regular expression** rules.

Usage is as below:

```shell
$ ... grep <Input directory> <Output directory> <Regular expression>
```

In a more detailed manner, you can run the following commands:

```shell
$ cd /usr/local/hadoop
$ mkdir ~/input
$ cp etc/hadoop/*.xml ~/input
$ bin/hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-3.2.1.jar grep ~/input ~/output 'dfs[a-z.]+'
```

<left>
	<img src="images/E2-02.png">
</left>

```shell
$ cat ~/output/*
1	dfsadmin
1	dfs.replication
```

<left>
    <img src="images/E2-03.png">
</left>

Remove files:

```shell
$ rm -rf ~/input/ ~/output
```

## Pseudo-Distributed Operation

##### Installing and configuring SSH

Installing SSH:

```shell
$ sudo apt-get install ssh
```

Generating SSH key:

```shell
$ ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa
$ cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
$ chmod 0600 ~/.ssh/authorized_keys
```

Confirming success:

```shell
$ ssh localhost
Welcome to Ubuntu 18.04.3 LTS (GNU/Linux 5.0.0-29-generic x86_64)

 * Documentation:  https://help.ubuntu.com
 * Management:     https://landscape.canonical.com
 * Support:        https://ubuntu.com/advantage


 * Canonical Livepatch is available for installation.
   - Reduce system reboots and improve kernel security. Activate at:
     https://ubuntu.com/livepatch

82 packages can be updated.
9 updates are security updates.

Your Hardware Enablement Stack (HWE) is supported until April 2023.
$ exit
```

##### Configuring Hadoop

Please note that after editing the following configure files, Hadoop will no longer be able to run in standalone mode or fully-distributed mode. The following configurations are specifically for pseudo-distributed mode.

Enter Hadoop directory:

```shell
$ cd /usr/local/hadoop
```

Edit the following files:

```xml
<!-- etc/hadoop/core-site.xml -->
<configuration>
    <!-- This is telling Hadoop where to run NameNode -->
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://localhost:9000</value>
    </property>
</configuration>

<!-- etc/hadoop/hdfs-site.xml -->
<configuration>
    <property>
        <name>dfs.replication</name>
        <value>1</value>
    </property>
</configuration>
```

##### Running Hadoop Without YARN

Formatting HDFS:

```shell
$ bin/hdfs namenode -format
```

Starting HDFS:

```shell
$ sbin/start-dfs.sh
Starting namenodes on [localhost]
Starting datanodes
Starting secondary namenodes [ubuntu]
```

Making sure they are started:

```shell
$ jps
6595 Jps
5990 NameNode
6202 DataNode
6461 SecondaryNameNode
```

Browse NameNode's web interface via http://localhost:9870/

Running homework requirements:

```shell
$ bin/hdfs dfs -mkdir /user
$ bin/hdfs dfs -mkdir /user/percy
$ bin/hdfs dfs -mkdir input
$ bin/hdfs dfs -put etc/hadoop/*.xml input
$ bin/hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-3.2.1.jar grep input output 'dfs[a-z.]+'
$ bin/hdfs dfs -cat output/*
2019-10-** *** INFO sasl.SaslDataTransferClient: SASL encryption trust check: localHostTrusted = false, remoteHostTrusted = false
1	dfsadmin
1	dfs.replication
```

<img src="images\E2-04.png">

Stop HDFS and remove generated files:

```shell
$ sbin/stop-dfs.sh
$ rm -rf data/
```

Be sure to remove generated files or else HDFS will not be able to format correctly.

##### Running Hadoop With YARN

Format HDFS first:

```shell
$ cd /usr/local/hadoop
$ bin/hdfs namenode -format
```

If data directory was not removed, you will not be able to reach DataNodes via this New NameNode and Java will throw ConnectionError：

<img src="images\E2-07.png">

Note that etc/hadoop/core-site.xml does not need to be changed as we are not changing where NameNode runs.

Then start HDFS:

```shell
$ sbin/start-dfs.sh
```

Configure YARN:

```xml
<!-- etc/hadoop/mapred-site.xml -->
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

<!-- etc/hadoop/yarn-site.xml -->
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

Start YARN:

```shell
$ sbin/start-yarn.sh
Starting resourcemanager
Starting nodemanagers
$ jps
9508 NodeManager
10084 NameNode
10295 DataNode
10554 SecondaryNameNode
9322 ResourceManager
10683 Jps
```

Browse ResourceManager's Web interface via http://localhost:8088/

Make sure your virtual machine has at least 3GB of running memory before running the next step, or your virtual machine may get stuck.

Run homework requirements:

```shell
$ bin/hdfs dfs -mkdir /user
$ bin/hdfs dfs -mkdir /user/percy
$ bin/hdfs dfs -mkdir input
$ bin/hdfs dfs -put etc/hadoop/*.xml input
$ bin/hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-3.2.1.jar grep input output 'dfs[a-z.]+'
```

<img src="images\E2-08.png">

<img src="images\E2-09.png">

If your program gets stuck at map phase or reduce phase, reboot your machine and repeat the following steps until you succeed.

Stop all services and remove data:

```shell
$ sbin/stop-yarn.sh
$ sbin/stop-dfs.sh
$ rm -rf data/
```



### Fully-Distributed Operation

We first start with a single virtual machine, as the second one can be cloned and adjusted. The IP address can be set to be static with any IP you want, the rest of this guide will use the IPs given in this table for consistence.

##### Planning

To stand out from other tutorials and show that I have grasped what each configuration file does, I have chosen to use only two virtual machines to simulate Hadoop operating in fully-distributed mode. The two machines each has the following duties:

|                     |     host-0      |      host-1       |
| :-----------------: | :-------------: | :---------------: |
|     IP Address      | 192.168.149.129 |  192.168.149.138  |
| Storage Components  |    NameNode     | SecondaryNameNode |
|                     |    DataNode     |     DataNode      |
| Resource Components |                 |  ResourceManager  |
|                     |   NodeManager   |    NodeManager    |

##### Configuring Inter-Node Connection

First, change the host name of your machine:

```shell
$ sudo gedit /etc/hostname
# Delete the name before and add the name you planned
host-0
```

Restart virtual machine to let changes take affect.

Second, add the IPs of known hosts into hosts file so that they know where to find each other:

```shell
$ sudo gedit /etc/hosts
# Add the following lines to the file
192.168.149.129 host-0
192.168.149.138	host-1
```

##### Hadoop Configuration

Third, tell Hadoop who the workers are:

```shell
$ gedit /usr/local/hadoop/etc/hadoop/workers
# Add our workers
host-0
host-1
```

Workers are responsible for saving actual data (DataNode) and computing (NodeManager).

Configuring NameNode:

```xml
<!-- etc/hadoop/core-site.xml -->
<configuration>
    <!-- Telling Hadoop where to run NameNode -->
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://host-0:9000</value>
    </property>
    <property>
        <name>hadoop.tmp.dir</name>
        <value>/usr/local/hadoop/data/tmp</value>
    </property>
</configuration>
```

Configuring HDFS:

```xml
<!-- etc/hadoop/hdfs-site.xml -->
<configuration>
    <!-- Telling Hadoop where to run Secondary NameNode -->
    <property>
        <name>dfs.namenode.secondary.http-address</name>
        <value>host-1:50090</value>
    </property>
    <property>
        <name>dfs.replication</name>
        <value>2</value>
    </property>
    <property>
        <name>dfs.namenode.name.dir</name>
        <value>/usr/local/hadoop/data/tmp/name</value>
    </property>
    <property>
        <name>dfs.datanode.name.dir</name>
        <value>/usr/local/hadoop/data/tmp/data</value>
    </property>
</configuration>
```

Configuring YARN:

```xml
<!-- etc/hadoop/yarn-site.xml -->
<configuration>
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
    <property>
        <name>yarn.nodemanager.env-whitelist</name>
        <value>JAVA_HOME,HADOOP_COMMON_HOME,HADOOP_HDFS_HOME,HADOOP_CONF_DIR,CLASSPATH_PREPEND_DISTCACHE,HADOOP_YARN_HOME,HADOOP_MAPRED_HOME</value>
    </property>
    <!-- Telling Hadoop where to run ResourceManager -->
    <property>
        <name>yarn.resourcemanager.hostname</name>
        <value>host-1</value>
    </property>
</configuration>
```

After configuration, clone this virtual machine. Change the clone's hostname to `host-1`, reboot, and try the `ssh <hostname>` command between the two machines to check if they can communicate.

##### Running Hadoop

Running homework requirements:

```shell
#Be sure that you have deleted data/

#host-0
$ bin/hdfs namenode -format
$ sbin/start-dfs.sh

#host-1
$ sbin/start-yarn.sh

#host-0
$ bin/hdfs dfs -mkdir /user
$ bin/hdfs dfs -mkdir /user/percy
$ bin/hdfs dfs -mkdir input
$ bin/hdfs dfs -put ./etc/hadoop/*.xml input
$ bin/hadoop jar ./share/hadoop/mapreduce/hadoop-mapreduce-examples-3.2.1.jar wordcount input output
......
$ bin/hdfs dfs -cat output/*
......
```

Screenshot of host-0 running wordcount：

![](images\E2-10.png)

Screenshot of host-1 running nodes：

![](images\E2-11.png)

Screenshot of result on host-0：

![](images\E2-12.png)