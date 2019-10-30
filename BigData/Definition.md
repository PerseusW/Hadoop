# Introduction

Here are several ways to view Big Data:

- *Engineer perspective*. Imagine yourself as a software engineer working at Google, and your boss suddenly hands you a magical disk. He claims that the disk holds 1PB(1024TB) of data, and he wants you to develop a system that can search through all that data within a second. The world's fastest disk can read data at around 800MB/s. How do you feel?
- *Data Mining Perspective.* Imagine yourself as a data analyst sitting in a Wall Street office. Your colleague just earned a million in the [futures market](https://www.investopedia.com/terms/f/futuresmarket.asp). He/She did it by mining through PBs of NASA data and market data and found out a correlation between constellations and oil prices. The algorithm he/she used was implemented within five lines. How do you feel?
- *Legal Perspective.* Imagine yourself working in a Big Data company, and you just wrote a Python crawler script to crawl credit data from banks. The next day, you find yourself in jail, and nobody cares what you feel.

In a very straightforward sense, `Big Data` is data that is big. Now, most people would think that this is rhetorically obvious and somewhat insulting if they are trying to seek a clear definition, yet it is not wrong, and you really don't need to go through the trouble of reciting an 'official' one. It doesn't help more than simply knowing that `Big Data` is big.

Now, back to the stories above. The traits of `Big Data` are:

1. Pain-in-the-a** type enormous.
2. Potential high value.
3. A ticket into jail when misused.

## What is Big Data?

- **Big data** is a field that treats ways to analyze, systematically extract information from, or otherwise deal with data sets that are too large or complex to be dealt with by traditional data-processing application software.
- **Big data** is a term that describes the **large** volume of **data** -- both structured and unstructured -- that inundates a business on a day-to-day basis.
- Extremely large data sets that may be analyzed computationally to reveal patterns, trends, and associations, especially relating to human behavior and interactions.
- Big Data are data sets with magnitudes so big, that it would be considered unbearable to to traverse on a single computer.

## Where Does Big Data Come From?



### 2. 大数据有哪几个特征？

Volume：数据体量巨大，可以从数百TB到数十数百PB，甚至EB的规模。

Variety：数据结构多样化，既有结构化数据，又有非结构化数据，大数据包括各种格式和形态的的数据。

Velocity：这里有两种理解；一，大数据需要在一定的时间限度下得到及时处理；二，大数据的增长速度非常快。

Veracity：大数据的处理结果需要保证一定的正确性。

Value：大数据包含很多深度的价值，大数据分析挖掘和利用将带来巨大的商业价值。

### 3. 请解释数据和信息的区别。

从信息论的观点来看，描述信源的数据是信息和数据冗余之和，即 数据=信息*数据冗余。数据是数据采集时提供的，信息是从采集的数据中获取的有用信息。

数据是信息的载体，信息是对数据处理加工后得到的结果。

### 4. 请分析scale up和scale out的区别。

Scale up，即纵向扩展，指在现有的系统上，通过不断增加存储容量来满足数据增长的需求。但这种方式只增加了容量，而带宽和计算能力并没有随之增加，所以系统很快会到达性能瓶颈，需要继续扩展。

Scale out，即横向扩展，这种架构的升级通常是以节点为单位，每个节点往往包含容量、处理能力和I/O带宽。一个节点被添加到系统中，系统中的三种资源将同时升级。

从更高的抽象级上讲，scale up是通过升级自己来提升系统性能，scale out是通过复制自己来提升集群系统的性能。

### 5. 请简述金融数据的特征。

金融数据一般具有“流数据”特征，需要在短时间内快速处理，同时具有逻辑关系紧密、处理实时性要求高、客展示性需求强等特征。

由于金融行业的特殊性，金融数据的输入审核更严格，所需的存储容量更大，网络传输更广泛，数据维护更频繁