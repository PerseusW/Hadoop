${HADOOP}/bin/hadoop com.sun.tools.javac.Main code/RelationA.java code/Selection.java
cd code/
jar cf Selection.jar *.class
rm *.class
mv Selection.jar ~/Desktop/jar
cd ..
