${HADOOP}/bin/hadoop com.sun.tools.javac.Main ~/Desktop/code/*.java
cd ~/Desktop/code/
jar cf MatrixMultiply.jar MatrixMultiply*.class
rm MatrixMultiply*.class
mv MatrixMultiply.jar ~/Desktop/jar
jar cf RelationAlgebra.jar *.class
rm *.class
mv RelationAlgebra.jar ~/Desktop/jar
cd ..
