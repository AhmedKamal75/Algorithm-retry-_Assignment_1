import java.io.*;
import java.util.*;

public class MaxSideLength {
    private static final int BASE_CASE_SIZE = 3;
    private static final int STRIP_SIZE = 7;
    public static void main(String[] args) {
        String inputFile = "content\\input.txt"; // replace with your relative file path
        System.out.println("The maximum side length of the square is: " + solve(inputFile)); // output the data
    }

    public static long solve(String inputFile){
        List<Point> points = read(inputFile); // read the data
        // sort on x and on y
        List<Point> sortedX = new ArrayList<>(points);
        List<Point> sortedY = new ArrayList<>(points);
        sortedX.sort((p1, p2) -> Double.compare(p1.getX(), p2.getX()));
        sortedY.sort((p1, p2) -> Double.compare(p1.getY(), p2.getY()));
        // Calculate and print the maximum side length
        return (long) maxSideLength(sortedX, sortedY);
    }

    /**
    * Calculates the maximum side length using a divide-and-conquer approach.
    */
    private static double maxSideLength(List<MaxSideLength.Point> sortedX, List<MaxSideLength.Point> sortedY) {
        // base case: if size of points <= 3 
        if (sortedX.size() <= BASE_CASE_SIZE) {
            return calculateBaseCase(sortedX);
        }

        // Divide:
        int mid = sortedX.size() / 2;   // we will divide at the point that have index mid in the sortedX list
        List<Point> leftX = sortedX.subList(0, mid);
        List<Point> rightX = sortedX.subList(mid, sortedX.size());

        // this approch with hashset will take O(n)
        Set<Point> leftSet = new HashSet<Point>(leftX);

        List<Point> leftY = new ArrayList<>();
        List<Point> rightY = new ArrayList<>();

        for (Point point : sortedY) {
            if (leftSet.contains(point)) {
                leftY.add(point);
            } else {
                rightY.add(point);
            }
        }
        
        // Conquer:
        double leftDelta = maxSideLength(leftX, leftY);
        double rightelta = maxSideLength(rightX, rightY);

        // Combine:
        double delta = Math.min(leftDelta, rightelta); 

        // build the strip
        List<Point> stripY = new ArrayList<>();
        for (Point point : sortedY) {
            if (Math.abs(point.getX() - sortedX.get(mid).getX()) < delta) {
                stripY.add(point);
            }
        }

        // compare the each point to the next 7 points in the strip
        double stripDelta = findStripDelta(stripY, delta);

        return Math.min(stripDelta, delta);
    }

    /**
     * 
     * Calculates the base case when the size of points is less than or equal to BASE_CASE_SIZE.
     */
    private static double calculateBaseCase(List<Point> points) {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                double distance = dist(points.get(i), points.get(j));
                if (distance < min) {
                    min = distance;
                }
            }
        }
        return min;
    }

    /**
     * Finds the minimum distance in stripY.
     */
    private static double findStripDelta(List<Point> stripY, double delta) {
        double min = delta;
        for (int i = 0; i < stripY.size(); i++) {
            // three constraints on j
            for (int j = i + 1; j < stripY.size() && (j <= i + STRIP_SIZE) && (stripY.get(j).getY() - stripY.get(i).getY() < min); j++) {
                double distance = dist(stripY.get(i), stripY.get(j));
                if (distance < min) {
                    min = distance;
                }
            }
        }
        return min;
    }

    
    /**
     * Function to calculate distance between two points,
     *  using the Chebyshev distance (as indicated by the formula max(|x1-x2|, |y1-y2|))
     * @param p1
     * @param p2
     * @return
     */
    private static double dist(Point p1, Point p2) {
        return Math.max(Math.abs(p1.getX() - p2.getX()), Math.abs(p1.getY() - p2.getY()));
    }

    /**
     * @param inputFile
     * @return
     */
    private static List<Point> read(String inputFile) {
        List<Point> points = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(inputFile)))) {
            
            int numPoints = Integer.parseInt(reader.readLine());
            for (int i = 0; i < numPoints; i++) {
                String[] line = reader.readLine().split(" ");
                Point point = (new MaxSideLength()).new Point((double) Double.parseDouble(line[0]), (double) Double.parseDouble(line[1]));
                points.add(point);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return points;
    }

    /**
     * Point implements Comparable<Point>   
     */
    private class Point{
        private double x;
        private double y;

        public Point(double x, double y){
            this.x = x;
            this.y = y;
        }
        public double getX() {return x;}
        public double getY() {return y;}       

        @Override
        public String toString() {return "[" + x + ", " + y + "]";}
    }
}
