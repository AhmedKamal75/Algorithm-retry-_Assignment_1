import java.util.Arrays;
import java.util.Random;

// cd bin
// java SelectorBenchmark 10000000

public class SelectorBenchmark {
    public static void main(String[] args) {
        int maxArrayLength = 100000000; // Default value
    
        // Check if a command-line argument is provided
        if (args.length > 0) {
            try {
                // Parse the first command-line argument as an integer
                maxArrayLength = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("The first command-line argument must be an integer. Using default maxArrayLength: " + maxArrayLength);
            }
        } else {
            System.out.println("No maxArrayLength provided. Using default: " + maxArrayLength);
        }
    
        // Call the benchmark method with maxArrayLength
        double[][] data = benchmark(maxArrayLength);
        System.out.println("length|randSele(sec)|MOMSelect(sec)|sort(sec)|medianRandSelect|medianMOMSelect|medianSort");
        for (double[] row : data) {
            System.out.println(Arrays.toString(row));
        }
    }
        
    public static double[][] benchmark(int maxArrayLength) {
        double[][] data = new double[(int)Math.log10(maxArrayLength)+ 1][7];
        System.out.println("number of arrays to test: " + data.length);
        for (int i = 1; i <= maxArrayLength; i *= 10) {
            System.out.println("in array with length: " + i);
            int[] array = generateRandomArray(i);
            int indexOfMedian = (array.length % 2 == 0)?array.length / 2 - 1:(array.length - 1)/2;
    
            long startTime = System.nanoTime();
            int medianRandomSelect = RandomSelectors.randomSelect(Arrays.copyOf(array,array.length), 0, array.length - 1, indexOfMedian + 1);
            long endTime = System.nanoTime();
            double durationInSecRandomSelect = (endTime - startTime) / 1e9;
    
            startTime = System.nanoTime();
            int medianMOMSelect = MOMSelectors.medianOfMedianSelector(Arrays.copyOf(array, array.length), indexOfMedian + 1);
            endTime = System.nanoTime();
            double durationInSecMOMSelect = (endTime - startTime) / 1e9;
    
            startTime = System.nanoTime();
            int[] sortedArray = Arrays.copyOf(array, array.length);
            Arrays.sort(sortedArray);
            int medianSort = sortedArray[indexOfMedian];
            endTime = System.nanoTime();
            double durationInSecSort = (endTime - startTime) / 1e9;
    
            data[(int)Math.log10(i)] = new double[]{i, durationInSecRandomSelect, durationInSecMOMSelect, durationInSecSort, medianRandomSelect, medianMOMSelect, medianSort};
        }
        return data;
    }
    
    public static int[] generateRandomArray(int length) {
        Random rand = new Random();
        int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = rand.nextInt(length);  // Generate random numbers between 0-99
        }
        return array;
    }
}
