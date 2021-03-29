package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import misc.BaseTest;
import misc.exceptions.EmptyContainerException;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import org.junit.Test;

public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    // Tests if the size is updated correctly.
    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
    }
    
    // Tries to remove min from an Empty container.
    // An empty container exception is thrown if the queue is empty.
    @Test(timeout = SECOND)
    public void removeMinEmptyContainer() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        assertTrue(heap.isEmpty());
        try {
            heap.removeMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
         // Do nothing: this is ok
            
        }
    }
    
    // RemoveMin with one thing in the queue.
    // Then do it another time (ensure that queue updates).
    @Test(timeout = SECOND)
    public void removeMinOneElement() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(5);
        assertEquals(1, heap.size());
        assertEquals(5, heap.removeMin());
        assertEquals(0, heap.size());
    }
    
    
    // Many inserts, many removes, tests removing from empty array.
    @Test(timeout = SECOND)
    public void removeMinEdgeCase() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(5);
        heap.insert(3);
        heap.insert(6);
        assertEquals(3, heap.size());
        assertEquals(3, heap.removeMin());
        assertEquals(5, heap.removeMin());
        assertEquals(6, heap.removeMin());
        assertEquals(0, heap.size());
        try {
            heap.removeMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
         // Do nothing: this is ok
            
        }
    }
    
    // Tests using peekMin on an empty container.
    @Test(timeout = SECOND)
    public void peekMinEmptyContainer() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        assertTrue(heap.isEmpty());
        try {
            heap.peekMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
         // Do nothing: this is ok
            
        }
    }
    
    // Tests using peekMin on one element.
    @Test(timeout = SECOND)
    public void peekMinOneElement() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(3, heap.peekMin());
    }
    
    // Tests doing multiple insertions and deletions, without checking
    // values match. Inserts from 0 to 999.
    @Test(timeout = SECOND)
    public void multipleInsertionsAndDeletions() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 1000; i++) {
            heap.insert(i);
            assertEquals(0, heap.peekMin());
        }
        
        for (int i = 0; i < 1000; i++) {
            assertEquals(i, heap.peekMin());
            heap.removeMin();
        }
        
    }
    
    // Tests doing multiple insertions and deletions, without checking
    // values match. Inserts from 1000 to 1.
    @Test(timeout = SECOND)
    public void multipeleInsertionsAndDeletionsReverse() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 1000; i++) {
            heap.insert(1000 - i);
            assertEquals(1000 - i, heap.peekMin());
        }
        
        for (int i = 0; i < 1000; i++) {
            assertEquals(i + 1, heap.peekMin());
            heap.removeMin();
        }
        
    }
    
    // Uses peekMin and remove min to check that it is not deleting the same 
    // element twice 'in one command'. 
    @Test(timeout = SECOND)
    public void peekMinDuplicates() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(0);
        heap.insert(2);
        heap.insert(3);
        heap.insert(2);
        heap.insert(0);
        assertEquals(0, heap.peekMin());
        assertEquals(0, heap.removeMin());
        assertEquals(0, heap.peekMin());
        assertEquals(0, heap.removeMin());
        assertEquals(2, heap.peekMin());
        assertEquals(2, heap.removeMin());
        assertEquals(2, heap.peekMin());
        assertEquals(2, heap.removeMin());
        assertEquals(3, heap.peekMin());
        assertEquals(3, heap.removeMin());
        try {
            heap.peekMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
         // Do nothing: this is ok
            
        }
    }
    
    // Tries to insert a null value.
    @Test(timeout = SECOND)
    public void insertNull() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        assertTrue(heap.isEmpty());
        try {
            heap.insert(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
         // Do nothing: this is ok
            
        }
    }
    
    // Testing variations on inserts by checking at each step.
    @Test(timeout = SECOND)
    public void insertSmallerAndLargerElements() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(1);
        assertEquals(1, heap.peekMin());
        assertEquals(1, heap.size());
        heap.insert(0);
        assertEquals(0, heap.peekMin());
        assertEquals(2, heap.size());
        heap.insert(3);
        assertEquals(0, heap.peekMin());
        assertEquals(3, heap.size());
    }
    
    // Checks to see if array resizes correctly.
    @Test(timeout = SECOND)
    public void insertResize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 39; i++) {
            heap.insert(i);
        }
        assertEquals(39, heap.size());
    }
    
    // Checking to see if array resizes, returns similar values correctly, and that
    // the size updates.
    @Test(timeout = SECOND)
    public void insertResizeRemove() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 5; i++) {
            heap.insert(10);
            assertEquals(i + 1, heap.size());
        }
        for (int i = 0; i < 30; i++) {
            heap.insert(i);
            assertEquals(6 + i, heap.size());
        }
        assertEquals(0, heap.peekMin());
        for (int i = 0; i < 11; i++) {
            heap.removeMin();
            assertEquals(34 - i, heap.size());
        }
        assertEquals(10, heap.peekMin());
        for (int i = 0; i < 24; i++) {
            assertEquals(24 - i, heap.size());
            heap.removeMin();
        }
        try {
            heap.peekMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
         // Do nothing: this is ok
            
        }
        
        try {
            heap.insert(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
         // Do nothing: this is ok
            
        }
    }
    
    // Tests inserting negative numbers.
    @Test(timeout=SECOND)
    public void insertNegative() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(-1);
        heap.insert(-2);
        assertEquals(-2, heap.peekMin());
    } 
    
}
