hadoop/bin/hadoop com.sun.tools.javac.Main code/NaturalJoin.java
cd code/
jar cf NaturalJoin.jar *.class
rm *.class
mv NaturalJoin.jar ~/Desktop/jar
cd ..
