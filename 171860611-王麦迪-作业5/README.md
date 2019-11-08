# KMeans MapReduce Implementation

This is the intellectual work of **171860611, 王麦迪**.

## 1 Overview

[Input Data](input): There are 1000 points with the structure of <double, double>. These points obey even distribution on [0, 100].

[Source Code](code/MapReduce): Data structures and the KMeans algorithm is implemented here. Data visualization with Qt/QCharts is also here.

[Scripts](sbin): These scripts make life a lot easier for a person who doesn't use an IDE.

[Compiled Jars](jar): JVM executable jars.

[Output](output): Files along with data visualization results.

## 2 For People With Time

### 2.1 Data Structures

#### 2.1.1 [DescIntWritable](code/MapReduce/DescIntWritable.java)

This is logically identical to `IntWritable`, only that the `compareTo` function is reversed to let keys be sorted in a descending order at the beginning of the reduce phase:

```java
public int compareTo(DescIntWritable o) {
    if (value > o.get()) {
        return -1;
    }
    else if (value < o.get()) {
        return 1;
    }
    else {
        return 0;
    }
}
```

You might be wondering why this is needed. Well, it's a clever way of getting the number of points mapped out at the beginning of the reduce phase. Knowing this number is crucial for evenly selecting the initial clusters amongst the input points.

#### 2.1.2 [Point](code/MapReduce/Point.java)

There is nothing much to say about this.

#### 2.1.3 [Cluster](code/MapReduce/Cluster.java)

The variables stored within a `Cluster`:

```java
public class Cluster implements WritableComparable<Cluster>
{
    private int clusterId;
    private int pointNum;
    private Point center;
......
}
```

This is a minimum size implementation of a `Cluster`. Another ideal implementation would be to store all points within a cluster, instead of using `pointNum` and `center` to represent the average of these points:

```java
public class Cluster implements WritableComparable<Cluster>
{
    private int clusterId;
    private ArrayList<Point> points;
......
}
```

So, why am I using the former? This is to minimize communication costs between *Map* nodes and *Reduce* nodes. When you're doing everything on one computer, this isn't important and may go unnoticed. But in an actual **Hadoop Cluster**, the costs could be immense. So one principle is to minimize the output of *Map* nodes.

### 2.2 Algorithms

Now that we have these data structures, it is time to use them.

#### 2.2.1 Generating Initial Clusters

I have achieved this via a separate `MapReduce Job`. I will describe this process mainly by following how <Key, Value> pairs change.

##### 2.2.1.1 Mapper

|                 | Input<Key, Value>         | Output<Key, Value>     |
| --------------- | ------------------------- | ---------------------- |
| Data Type       | LongWritable, Text        | DescIntWritable, Point |
| Logical Meaning | Line offset, line content | Point count, point     |

We maintain a global `count` variable that keeps count of the number of lines read. Whenever we're handed a line, we encapsulate it into a `Point`, then we emit it with it's count as the key.