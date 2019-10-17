hadoop/bin/hadoop com.sun.tools.javac.Main code/RelationA.java code/Difference.java
cd code/
jar cf Difference.jar *.class
rm *.class
mv Difference.jar ~/Desktop/jar
cd ..
