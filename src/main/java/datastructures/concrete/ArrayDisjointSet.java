package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;

/**
 * See IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    private int[] pointers;
    private IDictionary<T, Integer> refrenceDictionary;
    private int count;
    
    private static final int DEFAULT_SIZE = 20;

    public ArrayDisjointSet() {
        this(DEFAULT_SIZE);
    }
    
    public ArrayDisjointSet(int size) {
        pointers = new int[size];
        refrenceDictionary = new ChainedHashDictionary<T, Integer>();
        count = 0;
    }
    
    //Takes in an item, uses a ChainHashDictionary to store the
    //item to a reference index. Resizes the array if necessary.
    @Override
    public void makeSet(T item) {
        if (refrenceDictionary.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        
        refrenceDictionary.put(item, count);
        if (count == pointers.length) {
            this.pointers = resizeArray(this.pointers);
        }
        pointers[count] = -1;
        count++;
    }
    
    //Copies the array over to resize it.
    private int[] resizeArray(int[] pointer) {
        int[] temp = new int[pointer.length * 2];
        for (int i = 0; i < pointer.length; i++) {
            temp[i] = pointer[i];
        }
        return temp;
    }
    
    
    @Override
    public int findSet(T item) {
        if (!refrenceDictionary.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        
        int refrenceID = refrenceDictionary.get(item);
        return findReference(refrenceID);
        
    }
    
    //Returns the reference index. Path reduction as well.
    private int findReference(int refrenceID) {
        int rankReference = pointers[refrenceID];
        int baseIndex = refrenceID;
        if (rankReference >= 0) {
            baseIndex = findReference(rankReference);
            pointers[refrenceID] = baseIndex;
        }else {
            baseIndex = refrenceID;
        }
        return baseIndex;
    }

    @Override
    public void union(T item1, T item2) {
        if (!refrenceDictionary.containsKey(item1) || !refrenceDictionary.containsKey(item2)) {
            throw new IllegalArgumentException();
        }
        
        int refrenceItem1 = findSet(item1);
        int refrenceItem2 = findSet(item2);
        
        if (refrenceItem1 == refrenceItem2) {
            throw new IllegalArgumentException();
        }
        
        int rank1 = pointers[refrenceItem1];
        int rank2 = pointers[refrenceItem2];
        
        if (rank1 < rank2) {
            pointers[refrenceItem2] = refrenceItem1;
        } else if (rank2 < rank1) {
            pointers[refrenceItem1] = refrenceItem2;
        } else {
            pointers[refrenceItem2] = refrenceItem1;
            pointers[refrenceItem1] = rank1 - 1;
        }  
    }
}