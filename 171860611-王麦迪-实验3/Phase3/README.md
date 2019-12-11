# Phase 3

This is the intellectual work of **171860611, 王麦迪**

## Environment Setup

Following the **Keeping things simple** principle, I have minimized the number of components used in this phase. There is no `HDFS`, no `YARN`, no `HBase` ,nor `Hive`, but that does not mean I do not know how these components interact with each other.

Things are kept to a minimal by using `Spark` in **standalone mode**, and only using `Spark-Shell`.

### Installation

```shell
tar -xzvf jdk-8u221-linux-x64.tar.gz
sudo mv jdk1.8.0_221 /usr/local/java

tar -xzvf scala-2.13.1.tgz
sudo mv scala-2.13.1 /usr/local/scala

tar -xzvf spark-2.4.4-bin-hadoop2.7.tgz
sudo mv spark-2.4.4-bin-hadoop2.7 /usr/local/spark
```

### Configuration

Environment paths aren't written in any file, but directly typed into the terminal to keep environment variables clean. The consequences are that the following commands have to be retyped in every new terminal.

```shell
export JAVA_HOME=/usr/local/java
export PATH=$PATH:/usr/local/java/bin:/usr/local/scala/bin:/usr/local/spark/bin
```

### Confirmation

```shell
java -version
scala -version
spark-shell
```

## Homework

### Source Code

The following code should be directly typed into `Spark-Shell CLI`. This is to skip compile and run procedures and keep things simple.

```scala
import spark.implicits._
import scala.util.Try
import org.apache.spark.sql.expressions.Window

case class UserLog(val userID: Int, val itemID: Int, val catID: Int, val merchantID: Int, val brandID: Int, val month: Int, val day: Int, val action: Int, val ageRange: Int, val gender: Int, val province: String)

val table = spark.read.textFile("/home/percy/Desktop/input/million_user_log").map(line => line.split(",")).map(array => new UserLog(Try(array(0).toInt).getOrElse(0), Try(array(1).toInt).getOrElse(0), Try(array(2).toInt).getOrElse(0), Try(array(3).toInt).getOrElse(0), Try(array(4).toInt).getOrElse(0), Try(array(5).toInt).getOrElse(0), Try(array(6).toInt).getOrElse(0), Try(array(7).toInt).getOrElse(0), Try(array(8).toInt).getOrElse(0), Try(array(9).toInt).getOrElse(0), array(10)))

val window = Window.partitionBy($"_1").orderBy($"count".desc,$"_2")
val topTenCatByProvince = table.filter(userLog => userLog.action == 2).map(userLog => (userLog.province, userLog.catID)).groupBy($"_1", $"_2").count().withColumn("rn", row_number.over(window)).where($"rn" <= 10)
val topTenItemByProvince = table.filter(userLog => userLog.action == 2).map(userLog => (userLog.province, userLog.itemID)).groupBy($"_1", $"_2").count().withColumn("rn", row_number.over(window)).where($"rn" <= 10)
val topTenCatBrowsed = table.filter(userLog => userLog.action == 0).map(userLog => (userLog.brandID)).groupBy($"value").count().sort($"count".desc).limit(10)
```

I am not going to analyze my code, since it should all be basic functions.

### Result

```scala
topTenCatByProvince.show(1000)
topTenItemByProvince.show(1000)
topTenCatBrowsed.show(1000)
```

Requirement 1:

<center>
    <img src="images/1.png",width=100%>
</center>

Requirement 2:

<center>
    <img src="images/2.png",width=100%>
</center>

Requirement 3:

<center>
    <img src="images/3.png",width=100%>
</center>

As you can see, the results for requirement 2 and requirement 3 are the same as phase 1 and phase 2.