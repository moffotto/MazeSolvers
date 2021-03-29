package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DoubleLinkedList<T> implements IList<T> {
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    
    //Pre: Takes in data input 'item' of type T as parameter.
    //Post: Adds the given item to the end of the IList.
    @Override
    public void add(T item) { 	  	
        if (this.isEmpty()) {
            front = new Node<T>(item); 
            back = front;
        } else {
        	back.next = new Node<T>(back, item);   
        	back = back.next;
        }
            
        size++;        
    }

    //Pre: Container must not be empty, otherwise throws EmptyContainerException.
    //Post: Removes and returns the item from the end of the list
    @Override
    public T remove() {
    	if (this.isEmpty()) {
    		throw new EmptyContainerException();
    	} 

    	T item = back.data;
    	
    	if (this.size() == 1) {
    		front = null;
    		back = null;
    	} else {    	
    		back = back.prev;
    		back.next = null;
    	}
    	
    	size--;
    	return item;   		    	
    }

    //Pre: Takes in the desired integer index value as a parameter. Index must correlate to an
    //	   element within the list, otherwise throws IndexOutOfBoundsException.
    //Post: Returns the item located at the given index.
    @Override
    public T get(int index) {
    	if (index < 0 || index >= this.size()) {
    		throw new IndexOutOfBoundsException();
    	}
    	
    	T item = getNodeAtIndex(index).data;    	    	
     	return item;
    }

    //Pre: Takes in the desired integer index value & a data input 'item' of type T as parameters.
    //	   Index must be >= 0 && < the size of the list, otherwise throws IndexOutOfBoundsException.
    //Post: Overwrites data in element at given index with given 'item' data.
    @Override
    public void set(int index, T item) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        
        if (this.size() == 1) {
        	front = new Node<T>(item);
        	back = front;
        } else if (index == 0) {
        	Node<T> temp = front;
            front = new Node<T>(item, temp.next);
            temp.next.prev = front;
            temp.next = null;           
        } else if (index == size - 1) {
        	Node<T> temp = back;
            back = new Node<T>(temp.prev, item);
            temp.prev.next = back;
            temp.prev = null;
        } else {
        	Node<T> temp = getNodeAtIndex(index);
            Node<T> temp2 = new Node<T>(temp.prev, item, temp.next);
            temp.prev.next = temp2;
            temp.next.prev = temp2;
            temp.next = null;
            temp.prev = null;
        }
        
       
    }

    //Pre: Takes in the desired integer index value & a data input 'item' of type T as parameters.
    //	   Index must be > 0 && <= the size of the list, otherwise throws IndexOutOfBoundsException.
    //Post: Inserts new element at given index. If there already exists an element at that index,
    // 		shifts over that element and any subsequent elements one index higher.
    @Override
    public void insert(int index, T item) {
    	if (index < 0 || index > this.size()) {
    		throw new IndexOutOfBoundsException();
    	}
    	
    	if (index == this.size()) {
    		this.add(item);
    		return;
    	} else if (index == 0){
   			front = new Node<T>(item, front);
   			front.next.prev = front;
    	} else {
    		Node<T> temp = getNodeAtIndex(index);
    		Node<T> temp2 = new Node<T>(temp.prev, item, temp);
    		
    		temp.prev.next = temp2;
    		temp.prev = temp2;   		
    	}    		

    	size++;    	
    }

    //Pre: Takes in the desired integer index value as a parameter. Index must be > 0 && < the size
    //	   of the list, otherwise throws IndexOutOfBoundsException.
    //Post: Deletes the item at the given index. If there are any elements located at a higher index,
    //	    shifts them all down by one.
    @Override
    public T delete(int index) {
    	if (index < 0 || index >= this.size()) {
    		throw new IndexOutOfBoundsException();
    	}
    	
    	Node<T> temp;
    	
    	if (index == (this.size() - 1)) {
    		return this.remove();
    	} else if (index == 0){
    		temp = front;
   			front = temp.next;
   			front.prev = null;
   			temp.next = null;			  			
    	} else {
    		temp = getNodeAtIndex(index);   		
    		temp.prev.next = temp.next;
    		temp.next.prev = temp.prev;
    		temp.prev = null;
    		temp.next = null;
    	}
    	
    	size--; 
    	return temp.data;
    }
    
    //Pre: Takes in data input 'item' of type T as a parameter.
    //Post: Returns an integer index corresponding to the first occurrence of the given item in the 
    //	    list. If the item does not exist in the list, returns -1.
    @Override
    public int indexOf(T item) {  	
        return findIndex(item);
    }

    //Post: Returns an integer as the 'size' of the list.
    @Override
    public int size() {
        return this.size;
    }

    //Pre: Takes in data input of type T as a parameter.
    //Post: Returns 'true' if this container contains the given element, and 'false' otherwise.
    @Override
    public boolean contains(T other) {
        return (findIndex(other) != -1);
    }
    
    //Pre: Takes in an the desired integer index value as a parameter.
    //Post: Returns the node at a given index.
    private Node<T> getNodeAtIndex(int index) {
    	Node<T> temp;
    	int counter;
    	
    	if (index <= (size / 2)) {
    		temp = front;
    		counter = 0;
    		
    		while (counter != index) {
        		temp = temp.next;
        		counter++;
        	}
    	} else {
    		temp = back;
    		counter = size - 1;
    		
    		while (counter != index) {
        		temp = temp.prev;
        		counter--;
        	}
    	}
    	
    	return temp;
    }
    
    //Pre: Takes in data input 'item' of type T as a parameter.
    //Post: Returns an integer index corresponding to the first occurrence of the given item in the 
    //	    list. If the item does not exist in the list, returns -1.
    private int findIndex(T item) {
        int count = 0;
        Node<T> temp = front;
        while (temp != null) {
        	if ((temp.data == null && item == null) || (temp.data != null && temp.data.equals(item))) {
        		return count;
        	}
            count++;
            temp = temp.next;
        }
        return -1;
    }

    @Override
    public Iterator<T> iterator() {
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {  	
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
        
        public Node(Node<E> prev, E data) {
        	this(prev, data, null);
        }
        
        public Node(E data, Node<E> next) {
        	this(null, data, next);
        }
        
        public Node(E data) {
            this(null, data, null);
        }

    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            this.current = current;
        }   
          
        //Post: Returns 'true' if the iterator still has elements to look at; returns 'false' otherwise.
        public boolean hasNext() {
        		return current != null;
        }
      
        //Pre: Must have more elements to look iterate to, otherwise throws NoSuchElementException.
        //Post: Returns the next item in the iteration and updates the iterator to advance one
        //	    element forward.
        public T next() {
            if (this.hasNext()) {
            	T data = current.data;
            	current = current.next;
                return data;    	
            } else {
            	throw new NoSuchElementException();
            }
        }
    }
}
