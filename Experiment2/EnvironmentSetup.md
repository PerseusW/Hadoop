# Hadoop Experiment Guide

Reference: [Hadoop Official Guide](https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-common/SingleCluster.html)

Environment: [Ubuntu 18.04](https://mirrors.tuna.tsinghua.edu.cn/ubuntu-releases/)

This guide is mainly about how to establish a Hadoop-runnable environment. No actual coding is needed, yet knowing [shell](https://bash.cyberciti.biz/guide/What_is_Linux_Shell) would help.

## Standalone Operation

### Installing JDK

Installing JDK is the first step to running Hadoop, since Hadoop is implemented in Java, although Hadoop does provide C++ API. There are two choices to choose from:

1. OpenJDK (Recommended for Linux first timers)
2. OracleJDK (Recommended if you want to customize paths)

#### OpenJDK

This couldn't get easier for you, simply type the following line in the terminal:

```shell
$ sudo apt-get install openjdk-8-jdk
```

Confirm that install is complete:

```shell
$ java -version
openjdk version "1.8.0_222"
OpenJDK Runtime Environment (build 1.8.0_222-8u222-b10-1ubuntu1~18.04.1-b10)
OpenJDK 64-Bit Server VM (build 25.222-b10, mixed mode)
```

#### OracleJDK

This is a bit trickier, but it offers you more freedom when moving JDK around.

First, visit [Oracle's Official Download Link](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) to get your JDK8 download. You may have to register an Oracle account to do so. The download package you got for Linux should end with tar.gz.

Now, assuming you already have this packet in Ubuntu under the directory `~/Downloads`, extract it:

```shell
$ tar -xzvf jdk-8u221-linux-x64.tar.gz
```

Move it and rename it while moving:

```shell
$ sudo mv jdk1.8.0_221 /usr/local/java
```

The next step is to configure `paths` so that Java programs know where your Java Virtual Machine is:

```shell
$ sudo gedit ~/.bashrc
# Then add the following lines in the end
export JAVA_HOME=/usr/local/java
export PATH=${JAVA_HOME}/bin:${PATH}
$ source ~/.bashrc
```

Confirm that install is complete:

```shell
$ java -version
java version "1.8.0_221"
Java(TM) SE Runtime Environment (build 1.8.0_221-b11)
Java HotSpot(TM) 64-Bit Server VM (build 25.221-b11, mixed mode)
```

### Installing Hadoop

Unfortunately, Hadoop doesn't have its own archive in Ubuntu's database, meaning that you can't download and install Hadoop via the command line directly. So we'll have to do it manually.

The steps are somewhat similar to installing Oracle's JDK:

```shell
$ cd ~/Downloads
$ wget http://mirrors.tuna.tsinghua.edu.cn/apache/hadoop/common/hadoop-3.2.1/hadoop-3.2.1.tar.gz
$ tar -xzvf hadoop-3.2.1.tar.gz
$ sudo mv hadoop-3.2.1 /usr/local/hadoop
```

### Configuring Hadoop

If you've finished the commands above, then Hadoop is already in your system. But, as mentioned above, Hadoop is written in Java, and therefore runs on the Java Virtual Machine. So, we need to tell Hadoop where JAVA_HOME is.

```shell
$ sudo gedit /usr/local/hadoop/etc/hadoop/hadoop-env.sh
```

If you installed OpenJDK, insert the following line anywhere in the file:

```
export JAVA_HOME=$(readlink -f /usr/bin/java | sed "s:bin/java::")
```

Otherwise, insert this line:

```
export JAVA_HOME=/usr/local/java
```

### Running Hadoop

Type in the commands below, and if you see something similar, it means your environment is intact.

```shell
$ cd /usr/local/hadoop
$ bin/hadoop
Usage: hadoop [OPTIONS] SUBCOMMAND [SUBCOMMAND OPTIONS]
 or    hadoop [OPTIONS] CLASSNAME [CLASSNAME OPTIONS]
  where CLASSNAME is a user-provided Java class
......
SUBCOMMAND may print help when invoked w/o parameters or with -h.
```

The rest of these procedures are **not required**, so you can stop here if you wish.
