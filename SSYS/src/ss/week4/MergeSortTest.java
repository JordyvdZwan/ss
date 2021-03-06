package week4;

import org.junit.Test;
import week4.MergeSort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MergeSortTest {
//    @Test
//    public void testMergesortEmptyList() {
//        List<Integer> sequence = new ArrayList<>(Collections.emptyList());
//        MergeSort.mergesort(sequence);
//        assertEquals(sequence, Collections.emptyList());
//    }

    @Test
    public void testMergesortSingleItem() {
        List<Integer> sequence = new ArrayList<>(Arrays.asList(1));
        sequence = MergeSort.mergesort(sequence);
        assertEquals(sequence, Arrays.asList(1));
    }

    @Test
    public void testMergesortSortedList() {
        List<Integer> sequence = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        sequence = MergeSort.mergesort(sequence);
        assertEquals(sequence, Arrays.asList(1, 2, 3, 4, 5));

        sequence = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
        sequence = MergeSort.mergesort(sequence);
        assertEquals(sequence, Arrays.asList(1, 2, 3, 4, 5, 6));
    }

    @Test
    public void testMergesortUnsortedList() {
        List<Integer> sequence = new ArrayList<>(Arrays.asList(3, 2, 1, 5, 4));
        sequence = MergeSort.mergesort(sequence);
        assertEquals(sequence, Arrays.asList(1, 2, 3, 4, 5));

        sequence = new ArrayList<>(Arrays.asList(3, 2, 1, 6, 5, 4));
        sequence = MergeSort.mergesort(sequence);
        assertEquals(sequence, Arrays.asList(1, 2, 3, 4, 5, 6));
    }
}
