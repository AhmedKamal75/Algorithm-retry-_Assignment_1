import java.util.Arrays;
import java.util.Random;

public class RandomSelectors {
    /**
     * A method that returns the i'th smallest element in the array,
     * based on randomized partition part from quick sort.
     * 
     * @param array is an array of integers.
     * @param p is the start index of the array.
     * @param q is the ending index of the array.
     * @param i is the i'th smallest of array[p..q].
     * 
     * @return the element at the i'th index of the array.
     */
    public static int select(int[] array, int p, int q, int i){
        return randomSelect(Arrays.copyOf(array, array.length), p, q, i);
    }

    public static int randomSelect(int[] array, int p, int q, int i) {
        // Base case: if there's only one element in the array
        if (p == q) {
            return array[p];
        }
        
        // Randomly partition the array and get the pivot index
        int r = randPartition(array, p, q);
        // Calculate rank of pivot element
        int k = r - p + 1;

        // If pivot's rank is i, return pivot
        if (i == k) {
            return array[r];
        } else if (i < k) {
            // If i is less than rank of pivot, find i'th smallest in left subarray
            return randomSelect(array, p, r - 1, i);
        } else {
            // If i is more than rank of pivot, find (i-k)'th smallest in right subarray
            return randomSelect(array, r + 1, q, i - k);
        }        
    }

    /**
     * Partitions the input array around a randomly chosen pivot.
     *
     * @param array The input array.
     * @param p The starting index.
     * @param q The ending index.
     *
     * @return The partition index after performing random partitioning.
     */
    private static int randPartition(int[] array, int p, int q) {
        Random rand = new Random();
        // Choose a random pivot element
        int randomIndex = rand.nextInt(q - p + 1) + p;

        // Swap pivot with last element
        int temp = array[randomIndex];
        array[randomIndex] = array[q];
        array[q] = temp;

        // Partition around the last element and return its final position
        return partition(array, p, q);
    }

    /**
     * Partitions the input array around a pivot (last element).
     *
     * @param array The input array.
     * @param p The starting index.
     * @param q The ending index.
     *
     * @return The partition index after performing partitioning.
     */
    private static int partition(int[] array, int p, int q) {
        // Pivot element is last element
        int x = array[q];
        // Index for elements smaller than pivot
        int i = p - 1;

        // Iterate over elements and partition around pivot
        for (int j = p; j <= q - 1; j++) {
            if (array[j] <= x) {
                i++;
                // Swap current element with element at next smaller-than-pivot position
                swap(array, i, j);
                // int temp = array[i];
                // array[i] = array[j];
                // array[j] = temp;
            }
        }

        // Swap pivot with first larger-than-pivot element
        swap(array, i + 1, q);
        // int temp = array[i+1];
        // array[i+1] = array[q];
        // array[q] = temp;

        // Return final position of pivot
        return i + 1;
    }

    public static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
