import java.util.List;
import java.util.Map;

public class RandomNumberGenerator {

    /**
     * @param a
     * @param r  the random number
     * @param lo
     * @param hi
     * @return Find the index that a[index] >= r
     */
    public static int findCeil(int[] a, int r, int lo, int hi) {
        int mid;
        while (lo < hi) {
            mid = lo + (hi - lo) / 2;
            if (a[mid] < r) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return a[lo] >= r ? lo : -1;
    }

    public static int customizedRandom(int[] a, Map<String, Integer> expectedNumbers, List<String> neighbours) {
        // a - list of object ID's, freq - list of values of probabilities

        //Create and fill the prefix array
        int[] prefix = new int[a.length];
        prefix[0] = expectedNumbers.getOrDefault(neighbours.get(0), 0);

        for (int i = 1; i < a.length; i++) {
            prefix[i] = expectedNumbers.get(neighbours.get(i - 1)) + expectedNumbers.get(neighbours.get(i));
        }

        //Math.random() is [0, 1) => r [prefix[0], prefix[len - 1])
        int r = (int) (Math.random() * prefix[a.length - 1]) + prefix[0];
        //findCeil convert the random value to index of original array.
        int indexCeil = findCeil(prefix, r, 0, a.length - 1);
        if(indexCeil>=a.length)
            return a[a.length-1];
        else if(indexCeil<=0)
            return a[0];
        return a[indexCeil];
    }

    public static int normalRandom(int max) {
        int min = 0;
        int randomPick = (int) (Math.random() * (max - min + 1) + min);
        return randomPick;
    }


}


