${HADOOP}/bin/hadoop com.sun.tools.javac.Main ~/Desktop/code/MatrixMultiply/*.java
cd ~/Desktop/code/MatrixMultiply
jar cf MatrixMultiply.jar *.class
rm *.class
mv MatrixMultiply.jar ~/Desktop/jar

${HADOOP}/bin/hadoop com.sun.tools.javac.Main ~/Desktop/code/RelationAlgebra/*.java
cd ~/Desktop/code/RelationAlgebra
jar cf RelationAlgebra.jar *.class
rm *.class
mv RelationAlgebra.jar ~/Desktop/jar

${HADOOP}/bin/hadoop com.sun.tools.javac.Main ~/Desktop/code/InvertedIndexer/*.java
cd ~/Desktop/code/InvertedIndexer
jar cf InvertedIndexer.jar *.class
rm *.class
mv InvertedIndexer.jar ~/Desktop/jar
