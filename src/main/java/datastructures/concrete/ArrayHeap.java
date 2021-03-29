package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    private static final int DEFAULT_ARRAY_SIZE = 20;
    private static final int NUM_CHILDREN = 4;
    private T[] heap;
    private int arraySize;
    private int numElements;

    public ArrayHeap() {
        this(DEFAULT_ARRAY_SIZE);
    }
    
    public ArrayHeap(int initialSize) {
        heap = makeArrayOfT(initialSize);
        arraySize = initialSize;
        numElements = 0;
    }

    // Returns a new, empty array of the given size that can contain elements of type T.
    // Each element in the array will initially be null.
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        return (T[]) (new Comparable[size]);
    }  
    
    // Returns, but does not remove, the smallest element in the queue.
    // Throws an EmptyContainerException if the queue is empty.
    @Override
    public T peekMin() {
        if (numElements == 0) {
            throw new EmptyContainerException();
        }  
        return heap[0];
    }

    // Returns the number of elements contained within this queue.
    @Override
    public int size() {
        return this.numElements;
    }

    // Removes and return the smallest element in the queue. If two elements within the queue 
    // are considered "equal", breaks tie by choosing "first" element.
    // Throws EmptyContainerException if the queue is empty
    @Override
    public T removeMin() {
        if (numElements == 0) {
            throw new EmptyContainerException();
        }
            
        T oldRoot = heap[0];
        heap[0] = heap[numElements - 1];
        heap[numElements - 1] = null;
        
        if (numElements > 1) {
            heap = percolateDown(0, heap);    
        }
        
        numElements--;
        return oldRoot;
    }
    
    // Takes index value and arrayHeap as parameters, returns a heap that has been
    // percolated down from the given index.
    private T[] percolateDown(int index, T[] percDownHeap) {
        int lowest = (NUM_CHILDREN * (index) + 1);
        
        if (lowest < arraySize && percDownHeap[lowest] != null) {
            for (int i = lowest + 1; i < NUM_CHILDREN * index + NUM_CHILDREN + 1; i++) {
                if (i < arraySize && percDownHeap[i] != null) {
                    if (!compareElements(percDownHeap[lowest], percDownHeap[i])) {
                        lowest = i;
                    }    
                }  
            }
            
            if (!compareElements(percDownHeap[index], percDownHeap[lowest])) {
                T atIndex = percDownHeap[index];
                percDownHeap[index] = percDownHeap[lowest];
                percDownHeap[lowest] = atIndex;
                
                return (percolateDown(lowest, percDownHeap));
            }
        }
        
        return percDownHeap;         
    }

    // Inserts the given item into the queue.
    // Throws an IllegalArgumentException if the item is null.
    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
            
        if (arraySize == numElements + 1) {
           newSizeArray();       
        }
        
        heap[numElements] = item;
        numElements++;
        
        if (numElements > 1) {
            heap = percolateUp(numElements - 1, heap);    
        }
  
    }
    
    // Takes index value and arrayHeap as parameters, returns a heap that has been
    // percolated up from the given index.
    private T[] percolateUp(int index, T[] percDownHeap) {
        int parent = (index - 1)/NUM_CHILDREN;
        
        if (index != 0) {  
            if (!compareElements(percDownHeap[parent], percDownHeap[index])) {
                T atParent = percDownHeap[parent];
                percDownHeap[parent] = percDownHeap[index];
                percDownHeap[index] = atParent;
                
                return (percolateUp(parent, percDownHeap));
            }
        }
        
        return percDownHeap;  
    }
    
    // The array is updated with double it's previous size. The array size is also updated, 
    // and all the data from the previous array is copied to the new array.
    private void newSizeArray() {
        T[] newArray = makeArrayOfT(arraySize * 2);
        arraySize = arraySize * 2;
        
        for (int i = 0; i < numElements; i++) {
            newArray[i] = heap[i];
        }
        
        heap = newArray;
    }
    
    // If a is smaller than or equal to b, returns true. If a is larger than b, return false. 
    private boolean compareElements(T a, T b) {
        return a.compareTo(b) <= 0;
    } 
}
