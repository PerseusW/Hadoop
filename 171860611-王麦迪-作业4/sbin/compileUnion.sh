${HADOOP}/bin/hadoop com.sun.tools.javac.Main code/RelationA.java code/Union.java
cd code/
jar cf Union.jar *.class
rm *.class
mv Union.jar ~/Desktop/jar
cd ..
