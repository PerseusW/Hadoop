${HADOOP}/bin/hadoop com.sun.tools.javac.Main ~/Desktop/code/*.java
cd ~/Desktop/code
jar cf KMeans.jar *.class
rm *.class
mv KMeans.jar ~/Desktop/jar
