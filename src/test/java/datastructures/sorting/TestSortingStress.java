package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

public class TestSortingStress extends BaseTest {
	
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
    
	//Stress test for top sort k.
	@Test(timeout=10*SECOND)
    public void topSortStressTest() {
    	IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 100000; i++) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(90000, list);
        assertEquals(90000, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(10000 + i, top.get(i));
        }
    }
	
	//Stress test ArrayHeap size
	@Test(timeout=10*SECOND)
    public void heapSizeStress() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 100000; i++) {
            heap.insert(i);
        }
        assertEquals(100000, heap.size());
        
    }
	
	//Stress test ArrayHeap insert and delete
	@Test(timeout=10*SECOND)
    public void heapStressInsertDelete() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 100000; i++) {
            heap.insert(i);
        }
        assertEquals(100000, heap.size());
        for (int i = 0; i < 100000; i++) {
        	assertEquals(i, heap.peekMin());
        	heap.removeMin();
        }
        assertTrue(heap.isEmpty());
    } 
    
}