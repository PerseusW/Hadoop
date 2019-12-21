```shell
tar -xzvf hbase-1.4.11-bin.tar.gz
sudo mv hbase-1.4.11 /usr/local/hbase

gedit /usr/local/hbase/conf/hbase-env.sh
export JAVA_HOME=/usr/local/java
```

```xml
gedit /usr/local/hbase/conf/hbase-site.xml
<configuration>
    <property>
        <name>hbase.cluster.distributed</name>
        <value>true</value>
    </property>

    <property>
        <name>hbase.rootdir</name>
        <value>hdfs://localhost:9000/hbase</value>
    </property>
</configuration>
```

