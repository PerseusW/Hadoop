# 阶段四 实验报告

该报告是**171860611，王麦迪**的工作成果。

## 环境配置

由于老师允许Standalone模式的配置，后续Spark相关的实验配置都将使用最简Standalone配置。

```shell
tar -xzvf jdk-8u221-linux-x64.tar.gz
sudo mv jdk1.8.0_221 /usr/local/java

tar -xzvf scala-2.13.1.tgz
sudo mv scala-2.13.1 /usr/local/scala

tar -xzvf spark-2.4.4-bin-hadoop2.7.tgz
sudo mv spark-2.4.4-bin-hadoop2.7 /usr/local/spark
```

```shell
export JAVA_HOME=/usr/local/java
export PATH=$PATH:/usr/local/java/bin:/usr/local/scala/bin:/usr/local/spark/bin
```

```shell
java -version
scala -version
spark-shell
```

至此，我们已经进入了`Spark Shell`，剩下的实验工作都在该环境下完成。

## 实验过程

该实验主要使用Scala语言。

### 数据预处理

```scala
import scala.io.Source
import java.io._

val data = Source.fromFile("/home/percy/Desktop/input/train_after.csv").getLines().toList
val outputStream = new PrintWriter(new File("/home/percy/Desktop/train_after.svm"))
for (line <- data) {
    val tuple = line.split(",")
    outputStream.write(tuple(4) + " 1:" + tuple(0) + " 2:" + tuple(1) + " 3:" + tuple(2) + " 4:" + tuple(3) + "\n")
}
outputStream.close()

val data = Source.fromFile("/home/percy/Desktop/input/test_after.csv").getLines().toList
val outputStream = new PrintWriter(new File("/home/percy/Desktop/test_after.svm"))
for (line <- data) {
    val tuple = line.split(",")
    outputStream.write(tuple(4) + " 1:" + tuple(0) + " 2:" + tuple(1) + " 3:" + tuple(2) + " 4:" + tuple(3) + "\n")
}
outputStream.close()
```

### 编码实现

```scala
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification._
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorIndexer}
```

#### 决策树

```scala
val trainingData = spark.read.format("libsvm").load("/home/percy/Desktop/train_after.svm")
val testData = spark.read.format("libsvm").load("/home/percy/Desktop/test_after.svm")
val labelIndexer = new StringIndexer().setInputCol("label").setOutputCol("indexedLabel").fit(trainingData)
val featureIndexer = new VectorIndexer().setInputCol("features").setOutputCol("indexedFeatures").setMaxCategories(4).fit(trainingData)
val dt = new DecisionTreeClassifier().setLabelCol("indexedLabel").setFeaturesCol("indexedFeatures")
val labelConverter = new IndexToString().setInputCol("prediction").setOutputCol("predictedLabel").setLabels(labelIndexer.labels)
val pipeline = new Pipeline().setStages(Array(labelIndexer, featureIndexer, dt, labelConverter))
val model = pipeline.fit(trainingData)
val selfPredictions = model.transform(trainingData)
val testPredictions = model.transform(testData)
val evaluator = new MulticlassClassificationEvaluator().setLabelCol("indexedLabel").setPredictionCol("prediction").setMetricName("accuracy")
val selfAccuracy = evaluator.evaluate(selfPredictions)
println(s"Self-test accuracy = $selfAccuracy")
val treeModel = model.stages(2).asInstanceOf[DecisionTreeClassificationModel]
//println(s"Learned classification tree model:\n ${treeModel.toDebugString}")
```

#### 梯度加强树

```scala
val gbt = new GBTClassifier().setLabelCol("indexedLabel").setFeaturesCol("indexedFeatures").setMaxIter(10).setFeatureSubsetStrategy("auto")
val pipeline = new Pipeline().setStages(Array(labelIndexer, featureIndexer, gbt, labelConverter))
val model = pipeline.fit(trainingData)
val selfPredictions = model.transform(trainingData)
val testPredictions = model.transform(testData)
val evaluator = new MulticlassClassificationEvaluator().setLabelCol("indexedLabel").setPredictionCol("prediction").setMetricName("accuracy")
val selfAccuracy = evaluator.evaluate(selfPredictions)
println(s"Self-test accuracy = $selfAccuracy")
val gbtModel = model.stages(2).asInstanceOf[GBTClassificationModel]
//println(s"Learned classification GBT model:\n ${gbtModel.toDebugString}")
```

#### 多层感知器

```scala
val layers = Array[Int](4, 5, 4, 2)
val trainer = new MultilayerPerceptronClassifier().setLayers(layers).setBlockSize(128).setSeed(1234L).setMaxIter(100)
val model = trainer.fit(trainingData)
val selfPredictions = model.transform(trainingData)
val testPredictions = model.transform(testData)
val evaluator = new MulticlassClassificationEvaluator().setMetricName("accuracy")
val selfAccuracy = evaluator.evaluate(selfPredictions)
println(s"Self-test accuracy = $selfAccuracy")
```

#### 朴素贝叶斯

```scala
val model = new NaiveBayes().fit(trainingData)
val predictions = model.transform(testData)
val evaluator = new MulticlassClassificationEvaluator().setLabelCol("label").setPredictionCol("prediction").setMetricName("accuracy")
val accuracy = evaluator.evaluate(predictions)
println(s"Self-test accuracy = $accuracy")
```

#### OneVsRest

```scala
val classifier = new LogisticRegression().setMaxIter(10).setTol(1E-6).setFitIntercept(true)
val ovr = new OneVsRest().setClassifier(classifier)
val model = ovr.fit(trainingData)
val selfPredictions = model.transform(trainingData)
val testPredictions = model.transform(testData)
val evaluator = new MulticlassClassificationEvaluator().setMetricName("accuracy")
val selfAccuracy = evaluator.evaluate(selfPredictions)
println(s"Self-test accuracy = $selfAccuracy")
```

#### 随机森林

```scala
val rf = new RandomForestClassifier().setLabelCol("indexedLabel").setFeaturesCol("indexedFeatures").setNumTrees(10)
val pipeline = new Pipeline().setStages(Array(labelIndexer, featureIndexer, rf, labelConverter))
val model = pipeline.fit(trainingData)
val selfPredictions = model.transform(trainingData)
val testPredictions = model.transform(testData)
val evaluator = new MulticlassClassificationEvaluator().setLabelCol("indexedLabel").setPredictionCol("prediction").setMetricName("accuracy")
val selfAccuracy = evaluator.evaluate(selfPredictions)
println(s"Self-test accuracy = $selfAccuracy")
val rfModel = model.stages(2).asInstanceOf[RandomForestClassificationModel]
//println(s"Learned classification forest model:\n ${rfModel.toDebugString}")
```

## 实验结果

| 模型       | 训练集准确度 |
| ---------- | ------------ |
| 决策树     | 94.32%       |
| 梯度加强树 | 94.33%       |
| 多层感知器 | 94.32%       |
| 朴素贝叶斯 | 45.83%       |
| OneVsRest  | 94.32%       |
| 随机森林   | 94.32%       |

参考我的[操作视频](output)

