package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ChainedHashDictionary<K, V> implements IDictionary<K, V> { 
	private IDictionary<K, V>[] chains;
    private int numberOfKeys;
    private int arraySize;
    private static final int DEFAULT_SIZE = 30;

    public ChainedHashDictionary() {
        this(DEFAULT_SIZE);
    }
    
    public ChainedHashDictionary(int startingSize) {
    	chains = makeArrayOfChains(startingSize);
    	numberOfKeys = 0;
    	arraySize = startingSize;
    }

    // Returns a new, empty array of the given size that can contain IDictionary<K, V> objects.
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    // Returns the value corresponding to the given key.
    // @throws NoSuchKeyException if the dictionary does not contain the given key.
    @Override
    public V get(K key) {
    	int hashedKey = hashing(key);
    	
    	if (chains[hashedKey] != null) {
    		return chains[hashedKey].get(key);
    	} else {
    		throw new NoSuchKeyException();
    	}
    }
    
    // Adds the key-value pair to the dictionary. If the key already exists in the dictionary,
    // replace its value with the given one.
    @Override
    public void put(K key, V value) {
    	int hashedKey = hashing(key);  	
    	
        if (chains[hashedKey] == null) {
        	chains[hashedKey] = new ArrayDictionary<K, V>();
        }
        
        numberOfKeys -= chains[hashedKey].size();
        chains[hashedKey].put(key, value);
        numberOfKeys += chains[hashedKey].size();
        
    	if (((numberOfKeys / arraySize) >= 5)) {
    		newSizedArray();
    	}
    }
    
    // The pairs array is updated with double it's previous size. The array
    // size is also updated, and all the data from the previous array is copied
    // to the new array.
    private void newSizedArray() {
    	int prevArraySize = arraySize;
		arraySize *= 2;
		IDictionary<K, V>[] newArray = makeArrayOfChains(arraySize);
		
		for (int i = 0; i < prevArraySize; i++) {
			if (chains[i] != null) {
				for (KVPair<K, V> pairs : chains[i]) {
					
					K key = pairs.getKey();
					V value = pairs.getValue();
					
					int hashedKey = hashing(key);  	
			    	
			        if (newArray[hashedKey] == null) {
			        	newArray[hashedKey] = new ArrayDictionary<K, V>();
			        }
			        
			        newArray[hashedKey].put(key, value);
				}
			}
		}
		
		chains = newArray;
    }
    
    // Takes in a key as a parameter. Returns an integer hash value of that key. 
    // If the key == null, returns 0.
    private int hashing(K key) {
    	if (key == null) {
    		return 0;
    	}
    	
    	return Math.abs((key.hashCode() % arraySize));
    }

    // Remove the key-value pair corresponding to the given key from the dictionary.
    // @throws NoSuchKeyException if the dictionary does not contain the given key.
    @Override
    public V remove(K key) {
    	int hashedKey = hashing(key);
    	
    	if (chains[hashedKey] != null) {
    		numberOfKeys -= chains[hashedKey].size();
    		V value = chains[hashedKey].remove(key);
    		numberOfKeys += chains[hashedKey].size();
    		
    		return value;
    	} else {
    		throw new NoSuchKeyException();
    	}
    }


    // Returns 'true' if the dictionary contains the given key and 'false' otherwise.
    @Override
    public boolean containsKey(K key) {
    	int hashedKey = hashing(key);
    	
    	if (chains[hashedKey] == null) {
    		return false;
    	} else {
    		return chains[hashedKey].containsKey(key);
    	}
    }

    
    // Returns the number of key-value pairs stored in this dictionary.
    @Override
    public int size() {
        return this.numberOfKeys;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        return new ChainedIterator<>(this.chains, this.numberOfKeys, this.arraySize);
    }

    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;   
        private int numKeys;
        private int arraySize;
        private Iterator<KVPair<K, V>> itr;   
        private int counter;
        private int index;
             
        public ChainedIterator(IDictionary<K, V>[] chains, int numKeys, int arraySize) {
            this.chains = chains;
            this.numKeys = numKeys;
            this.arraySize = arraySize;
            itr = setIterator();
        }
        
        // Sets the iterator field to a not null index.
        private Iterator<KVPair<K, V>> setIterator() {
        	Iterator<KVPair<K, V>> newItr = null;
        	while ((chains[index] == null || chains[index].size() == 0) 
        	        && index < arraySize - 1) {
        		index++;
        	}
        	if (chains[index] != null) {
    			newItr = chains[index].iterator();
    		}	
        	return newItr;
        }
        
        @Override
        public boolean hasNext() {
            return counter < numKeys;
        }

        @Override
        public KVPair<K, V> next() {
        	if (this.hasNext()) {		    
        	    if (!itr.hasNext()) {
        			index++;
        			itr = setIterator();
        		}
        		counter++;
    			return itr.next();
        	} else {
        		throw new NoSuchElementException();
        	}
        }
    }
}
