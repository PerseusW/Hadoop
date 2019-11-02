import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public final class ClusterGenerator {
    private FileSystem fileSystem;
    private FileStatus[] fileList;
    private int clusterNum;
	
	public ClusterGenerator(Configuration configuration,String filePath) throws IOException {
		fileSystem = FileSystem.get(URI.create(filePath),configuration);
        fileList = fileSystem.listStatus((new Path(filePath)));
        clusterNum = configuration.getInt("clusterNum", 0);
	}
	
	public Path generateInitialCluster() throws IOException {
        ArrayList<Point> points = getPoints();
        ArrayList<Cluster> clusters = getClusters(points);
        return writeToFile(clusters);
    }

    private ArrayList<Point> getPoints() throws IOException {
        ArrayList<Point> points = new ArrayList<>();
		for (int i = 0;i < fileList.length;i++) {
			FSDataInputStream inputStream = fileSystem.open(fileList[i].getPath());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = new String();
			while ((line = reader.readLine()) != null) {
                Point point = new Point(line);
                points.add(point);
            }
            reader.close();
	    	inputStream.close();
        }
        return points;
    }

    private ArrayList<Cluster> getClusters(ArrayList<Point> points) throws IOException {
        ArrayList<Cluster> clusters = new ArrayList<>();
        int count = 1;
        for (int i = 0; i < points.size(); i += points.size() / clusterNum) {
            Cluster cluster = new Cluster();
            cluster.setClusterId(count); count++;
            cluster.setPointNum(1);
            cluster.setCenter(points.get(i));
            clusters.add(cluster);
        }
        return clusters;
    }

    private Path writeToFile(ArrayList<Cluster> clusters) throws IOException {
        Path path = fileSystem.getHomeDirectory();
        String pathString = path.toString();
        pathString = pathString += "/clusters";
        path = new Path(pathString);
        FSDataOutputStream outputStream = fileSystem.create(path);
        for (Cluster cluster: clusters) {
            outputStream.write((cluster.toString() + "\n").getBytes());
        }
        outputStream.close();
        return path;
    }
}
