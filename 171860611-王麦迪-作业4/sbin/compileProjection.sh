hadoop/bin/hadoop com.sun.tools.javac.Main code/RelationA.java code/Projection.java
cd code/
jar cf Projection.jar *.class
rm *.class
mv Projection.jar ~/Desktop/jar
cd ..
