package misc;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

public class Searcher {
    
    // Takes the input list and returns the top k elements in sorted order.
    // If the input list contains fewer then 'k' elements, returns a list containing all input.length
    // elements in sorted order. Throws IllegalArgumentException  if k < 0.
    public static <T extends Comparable<T>> IList<T> topKSort(int k, IList<T> input) {
        if (k < 0) {
            throw new IllegalArgumentException();
        }

        IPriorityQueue<T> heap = new ArrayHeap<>(k + 1);
        IList<T> list = new DoubleLinkedList<>();
        
        if (k != 0 && input.size() != 0) {
            int counter = 0;
            
            for (T element : input) {
                if (counter < k) {
                    heap.insert(element);
                } else if (heap.peekMin().compareTo(element) < 0) {
                    heap.removeMin();
                    heap.insert(element);
                }
                counter++;
            }
                
            while (!heap.isEmpty()) {
                list.add(heap.removeMin());
            }
        }

        return list;
    } 
}