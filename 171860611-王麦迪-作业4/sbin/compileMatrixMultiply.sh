${HADOOP}/bin/hadoop com.sun.tools.javac.Main code/MatrixMultiply.java
cd code/
jar cf MatrixMultiply.jar *.class
rm *.class
mv MatrixMultiply.jar ~/Desktop/jar
cd ..
