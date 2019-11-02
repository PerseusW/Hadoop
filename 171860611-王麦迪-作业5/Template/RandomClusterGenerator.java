import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Random;

import org.apache.hadoop.configuration.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.LineReader;

public final class RandomClusterGenerator {
	private int k;
	
	private FileStatus[] fileList;
	private FileSystem fileSystem;
	private ArrayList<Cluster> kClusters;
	private Configuration configuration;
	
	public RandomClusterGenerator(Configuration configuration,String filePath,int k) throws IOException {
		this.k = k;
		fileSystem = FileSystem.get(URI.create(filePath),configuration);
		fileList = fileSystem.listStatus((new Path(filePath)));
		kClusters = new ArrayList<Cluster>(k);
		this.configuration = configuration;
	}
	
	public void generateInitialCluster(String destinationPath) throws IOException {
		Text line = new Text();
		FSDataInputStream inputStream = null;
		for(int i = 0;i < fileList.length;i++){
			inputStream = fileSystem.open(fileList[i].getPath());
			LineReader lineReader = new LineReader(inputStream,configuration);
			while(lineReader.readLine(line) > 0){
				System.out.println("read a line:" + line);
				Instance instance = new Instance(line.toString());
				makeDecision(instance);
			}
		}
		inputStream.close();
		writeBackToFile(destinationPath);
	}
	
	public void makeDecision(Instance instance){
		if(kClusters.size() < k){
			Cluster cluster = new Cluster(kClusters.size() + 1, instance);
			kClusters.add(cluster);
		}else{
			int choice = randomChoose(k);
			if(!(choice == -1)){
				int id = kClusters.get(choice).getClusterID();
				kClusters.remove(choice);
				Cluster cluster = new Cluster(id, instance);
				kClusters.add(cluster);
			}
		}
	}
	
	public int randomChoose(int k){
		Random random = new Random();
		if(random.nextInt(k + 1) == 0){
			return new Random().nextInt(k);
		}else
			return -1;
	}
	
	public void writeBackToFile(String destinationPath) throws IOException {
		Path path = new Path(destinationPath + "cluster-0/clusters");
		FSDataOutputStream outputStream = null;
		outputStream = fileSystem.create(path);
		for(Cluster cluster : kClusters){
			outputStream.write((cluster.toString() + "\n").getBytes());
		}
		outputStream.close();
	}
}
