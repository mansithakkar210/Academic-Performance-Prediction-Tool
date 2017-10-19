
import java.util.ArrayList;
import java.util.List;
 
public class KMeans {
 
	//Number of Clusters. This metric should be related to the number of points
    private int NUM_CLUSTERS = 3;    
    //Number of Points
    private int NUM_POINTS = 15;
    //Min and Max X and Y
    private static final int MIN_COORDINATE = 0;
    private static final int MAX_COORDINATE = 100;
    
    private List points;
    private List clusters;
    
    public KMeans() {
    	this.points = new ArrayList();
    	this.clusters = new ArrayList();    	
    }
    
    public static void main(String[] args) {
    	
    	KMeans kmeans = new KMeans();
    	kmeans.init();
    	kmeans.calculate();
    }
    
    //Initializes the process
    public void init() {
    	//Create Points
    	points = Point.createRandomPoints(MIN_COORDINATE,MAX_COORDINATE,NUM_POINTS);
    	/*for(int l = 0; l<points.size(); l++){
    		Point p = (Point) points.get(l);
		    System.out.println(p);
    	}*/
    	
    	//Create Clusters
    	//Set Random Centroids
    	for (int i = 0; i < NUM_CLUSTERS; i++) {
    		Cluster cluster = new Cluster(i);
    		Point centroid = Point.createRandomPoint(MIN_COORDINATE,MAX_COORDINATE);
    		cluster.setCentroid(centroid);
    		clusters.add(cluster);
    	}
    	
    	//Print Initial state
    	plotClusters();
    }
 
	private void plotClusters() {
    	for (int i = 0; i < NUM_CLUSTERS; i++) {
    		Cluster c = (Cluster) clusters.get(i);
    		c.plotCluster();
    	}
    }
    
	//The process to calculate the K Means, with iterating method.
    public void calculate() {
        boolean finish = false;
        int iteration = 0;
        
        // Add in new data, one at a time, recalculating centroids with each new one. 
        while(!finish) {
        	//Clear cluster state
        	clearClusters();
        	
        	List lastCentroids = getCentroids();
        	
        	//Assign points to the closer cluster
        	assignCluster();
            
            //Calculate new centroids.
        	calculateCentroids();
        	
        	iteration++;
        	
        	List currentCentroids = getCentroids();
        	
        	//Calculates total distance between new and old Centroids
        	double distance = 0;
        	for(int i = 0; i < lastCentroids.size(); i++) {
        		distance += Point.distance((Point) lastCentroids.get(i),(Point) currentCentroids.get(i));
        	}
        	System.out.println("#################");
        	System.out.println("Iteration: " + iteration);
        	System.out.println("Centroid distances: " + distance);
        	plotClusters();
        	        	
        	if(distance == 0) {
        		finish = true;
        	}
        }
    }
    
    private void clearClusters() {
    	int i = 0;
    	while(i < clusters.size()) {
    		Cluster cluster = (Cluster) clusters.get(i);
    		cluster.clear();
    		i++;
    	}
    }
    
    private List getCentroids() {
    	List centroids = new ArrayList(NUM_CLUSTERS);
    	int i = 0;
    	while(i< clusters.size()) {
    		Cluster cluster = (Cluster) clusters.get(i);
    		Point aux = cluster.getCentroid();
    		Point point = new Point(aux.getX(),aux.getY());
    		centroids.add(point);
    		i++;
    	}
    	return centroids;
    }
    
    private void assignCluster() {
        double max = Double.MAX_VALUE;
        double min = max; 
        int cluster = 0;                 
        double distance = 0.0; 
        int j = 0;
        while(j < points.size()) {
        	Point point = (Point) points.get(j);
        	min = max;
            for(int i = 0; i < NUM_CLUSTERS; i++) {
            	Cluster c = (Cluster) clusters.get(i);
                distance = Point.distance(point, c.getCentroid());
                if(distance <  min){
                    min = distance;
                    cluster = i;
                }
            }
            point.setCluster(cluster);
            Cluster n = (Cluster) clusters.get(cluster);
            n.addPoint(point);
            j++;
        }
    }
    
    private void calculateCentroids() {
    	for(int i = 0; i < NUM_CLUSTERS; i++) {
        	Cluster cluster = (Cluster) clusters.get(i);
            double sumX = 0;
            double sumY = 0;
            List list = cluster.getPoints();
            int n_points = list.size();
            
            for(int j = 0; j < list.size(); j++) {
            	Point point = (Point) list.get(j);
            	sumX += point.getX();
                sumY += point.getY();
            }
            
            Point centroid = cluster.getCentroid();
            if(n_points > 0) {
            	double newX = sumX / n_points;
            	double newY = sumY / n_points;
                centroid.setX(newX);
                centroid.setY(newY);
            }
        }
    }
}