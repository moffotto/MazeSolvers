package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

import datastructures.concrete.KVPair;

public class ArrayDictionary<K, V> implements IDictionary<K, V> {

    private Pair<K, V>[] pairs;
    private int numberOfKeys;
    private int arraySize;
    private static final int DEFAULT_SIZE = 20;
   

    public ArrayDictionary() {
        this(DEFAULT_SIZE);
    }
    
    public ArrayDictionary(int startingSize) {
    	pairs = makeArrayOfPairs(startingSize);
    	numberOfKeys = 0;
    	arraySize = startingSize;
    }
    
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int aSize) {
        return (Pair<K, V>[]) (new Pair[aSize]);
    }
    
    //Pre: A key is passed in.
    //Post: Returns a -1 if the key is not in the dictionary
    //	    and returns the value in the index if it is in the
    //      dictionary.
    @Override
    public V get(K key) {
        int keyIndex = findKeyIndex(key);
        if (keyIndex == -1) {
        	throw new NoSuchKeyException();
        }
        return pairs[keyIndex].value;
    }
    
    //Pre: a key and value is passed in to be placed in the array.
    //Post: If the array is full a new array of double size will be created,
    //		and the key value pairs in the previous array will be passed over.
    //		If the key is not in the dictionary, then the pair will be added to
    // 		the next null index in the dictionary.
    @Override
    public void put(K key, V value) {
    	int index = findKeyIndex(key);
    	if (numberOfKeys == arraySize) {
    		newSizeArray();
    	}
    	if (index != -1) {
        	pairs[index].value = value;
    	}else {
        	pairs[numberOfKeys] =  new Pair<K, V>(key, value);
        	numberOfKeys++;
    	}

    }
    
    //Post: The pairs array is updated with double it's previous size. The array
    //		size is also updated, and all the data from the previous array is copied
    //		to the new array.
    private void newSizeArray() {
		Pair<K, V>[] newArray = makeArrayOfPairs(arraySize * 2);
		arraySize = arraySize * 2;
		for (int i = 0; i < numberOfKeys; i++) {
			newArray[i] = pairs[i];
		}
		pairs = newArray;
    }
    
    
    //Pre: This method takes in a key.
    //Post: The array is searched for the key. If the key is
    //		found, the index value is returned, if not a -1
    //		is returned.
    private int findKeyIndex(K key) {
    	for (int i = 0; i < this.numberOfKeys; i++) {
    		if ((pairs[i].key == null && key == null) || (pairs[i].key != null && pairs[i].key.equals(key))) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    //Pre: A key is passed in to be removed.
    //Post: If the key is not in the dictionary, a no such key exception
    //		is thrown back to the user. If it is in the dictionary, the
    //		index to be removed is replaced by the last not null index
    //		of the dictionary. The value of the pair that was removed
    //		from the dictionary is returned and the number of keys in the
    //		dictionary is reduced by one.
    @Override
    public V remove(K key) {
    	int keyIndex = findKeyIndex(key);
    	V removedValue;
        if (keyIndex == -1) {
        	throw new NoSuchKeyException();
        }else {
        	removedValue = pairs[keyIndex].value;
        	pairs[keyIndex] = pairs[numberOfKeys - 1];
        	pairs[numberOfKeys - 1] = null;
        	numberOfKeys--;
        	
        }
        return removedValue;
    }
    
    //Pre: A key is passed in.
    //Post: If the index of the key is greater than -1, then the key is in
    //		the dictionary. Otherwise it is returned that the key is not in
    //		the dictionary.
    @Override
    public boolean containsKey(K key) {
        if (findKeyIndex(key) != -1) {       	
        	return true;
        }
        return false;
    }
    
    //Post: The size, A.K.A. the number of keys in the dictionary is returned.
    @Override
    public int size() {
        return this.numberOfKeys;
    }
    
    @Override
    public Iterator<KVPair<K, V>> iterator() {  	
    	return new ArrayDictionaryIterator<K, V>(pairs, numberOfKeys);
    }
    
    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
    	private Pair<K, V>[] pairs;
    	private int index; 
        private int size;
        
        public ArrayDictionaryIterator(Pair<K, V>[] pairs, int size) {
            this.pairs = pairs;
            this.size = size;
        }
        
        @Override
        public boolean hasNext() {
        	return index < size;
        }

        @Override
        public KVPair<K, V> next() {
        	if (this.hasNext()) {
        		KVPair<K, V> pair = new KVPair<>(pairs[index].key, pairs[index].value);
        		index++;
        		return pair;
        	} else {
        		throw new NoSuchElementException();
        	}
        }
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}