package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;

import static org.junit.Assert.fail;

import org.junit.Test;

public class TestTopKSortFunctionality extends BaseTest {
    
    // Basic functionality test
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }
    
    // Input list is of size 19
    // K is equal to 19
    @Test(timeout=SECOND)
    public void testInputListSameSize() {
        IList<Integer> list = new DoubleLinkedList<>();
        
        for (int i = 0; i < 19; i++) {
            list.add(i);
        }
        
        IList<Integer> notEmpty = Searcher.topKSort(19, list); 
        assertEquals(19, notEmpty.size());
    }
    
    // Input list is of size 0
    // K is equal to 0
    @Test(timeout=SECOND)
    public void testInputListSize0() {
        IList<Integer> list = new DoubleLinkedList<>();
        IList<Integer> empty = Searcher.topKSort(0, list);
        assertEquals(0, empty.size());
    }
    
    // K is less than 0
    @Test(timeout=SECOND)
    public void testInputListKnegitive() {
        IList<Integer> list = new DoubleLinkedList<>();
        try {
            Searcher.topKSort(-1, list);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
         // Do nothing: this is ok
            
        }
    }
    
    // Input List contains null
    @Test(timeout=SECOND)
    public void testInputListNullValue() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(null);
        try {
            Searcher.topKSort(1, list);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
         // Do nothing: this is ok
            
        }

    }
    
    // A list with similar values
    @Test(timeout=SECOND)
    public void testInputSameValues() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(10);
        list.add(10);
        list.add(9);
        list.add(11);
        IList<Integer> sorList = Searcher.topKSort(3, list);
        assertEquals(10, sorList.get(0));
        assertEquals(10, sorList.get(1));
        assertEquals(11, sorList.get(2));
    }
    
    // List with only similar values
    @Test(timeout=SECOND)
    public void testInputListWithOnlySameValues() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(10);
        list.add(10);
        list.add(10);
        list.add(10);
        IList<Integer> sorList = Searcher.topKSort(3, list);
        assertEquals(10, sorList.get(0));
        assertEquals(10, sorList.get(1));
        assertEquals(10, sorList.get(2));
    }
    
    // K is larger than the size of the list
    @Test(timeout=SECOND)
    public void testInputListWithLargerrK() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(3);
        list.add(5);
        IList<Integer> sorList = Searcher.topKSort(3, list);
        assertEquals(3, sorList.get(0));
        assertEquals(5, sorList.get(1));
        assertEquals(2, sorList.size());
    }
    
    // Sorting a list with negative values
    @Test(timeout=SECOND)
    public void testInputListWithNegValues() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(-13);
        list.add(0);
        list.add(-5);
        list.add(-57);
        list.add(0);
        list.add(1);
        IList<Integer> sorList = Searcher.topKSort(6, list);
        assertEquals(-57, sorList.get(0));
        assertEquals(-13, sorList.get(1));
        assertEquals(-5, sorList.get(2));
        assertEquals(0, sorList.get(3));
        assertEquals(0, sorList.get(4));
        assertEquals(1, sorList.get(5));
    }
    
}
