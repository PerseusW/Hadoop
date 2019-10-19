${HADOOP}/bin/hadoop com.sun.tools.javac.Main code/RelationA.java code/Intersection.java
cd code/
jar cf Intersection.jar *.class
rm *.class
mv Intersection.jar ~/Desktop/jar
cd ..
