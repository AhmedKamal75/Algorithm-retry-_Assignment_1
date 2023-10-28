import java.util.Arrays;

/**
 * This class implements the Median of Medians algorithm.
 */
public class MOMSelectors {

    /**
     * This method finds the i-th smallest element in an array using the Median of Medians algorithm.
     *
     * @param array The array from which to select.
     * @param i The rank of the element to select.
     * @return The i-th smallest element in the array.
     */
    public static int medianOfMedianSelector(int[] array, int i) {
        return select(Arrays.copyOf(array, array.length), 0, array.length - 1, i);
    }

    private static int select(int[] array, int left, int right, int i) {
        if (right - left + 1 <= 5) {
            Arrays.sort(array, left, right + 1); // this method sort in place, so the array is being changed 
            return array[left + i - 1];
        }


        // 1. Divide the n elements into groups of 5. Find the median of each 5-element group using any trivial way.
        int[] medians = new int[(right - left + 4) / 5];
        for (int j = 0; j < medians.length; j++) {
            int groupLeft = left + j * 5;
            int groupRight = Math.min(groupLeft + 4, right);
            Arrays.sort(array, groupLeft, groupRight + 1); // the array is being changed.
            medians[j] = array[groupLeft + (groupRight - groupLeft) / 2];
        }


        // 2. Recursively SELECT the median medianOfMedians of the ⌊n / 5⌋ group medians to be the pivot.
        int medianOfMedians = select(medians, 0, medians.length - 1, medians.length / 2 + 1); // x


        // 3. Partition around the pivot medianOfMedians. Let k = rank(medianOfMedians).
        int pivotIndex = partition(array, left, right, medianOfMedians);

        if (i == pivotIndex - left + 1)
            return medianOfMedians;
        else if (i < pivotIndex - left + 1)
            return select(array, left, pivotIndex - 1, i);
        else
            return select(array, pivotIndex + 1, right, i - (pivotIndex - left + 1));
    }

    private static int partition(int[] array, int left, int right, int pivot) {
        int i = left;
        for (int j = left; j <= right; j++) {
            if (array[j] <= pivot) {
                RandomSelectors.swap(array, i++, j);
            }
        }
        return i - 1;
    }
}
