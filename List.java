package poo.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public interface List<T> extends Iterable<T>,Serializable{
	
	default int size() {
		int cnt=0;
		for(@SuppressWarnings("unused") T e: this) cnt++;
		return cnt;
	}
	
	default void clear() {
		Iterator<T> it=this.iterator();
		while(it.hasNext()) {
			it.next(); it.remove();
		}
	}
	
	default boolean contains(T e) {
		if(isEmpty()) return false;
		Iterator<T> it= this.iterator();
		while(it.hasNext()) {
			T x=it.next();
			if(x.equals(e)) return true;
		}
		return false;
	}
	
	void add(T e);
	
	default boolean isEmpty() {
		Iterator <T> it=this.iterator();
		return !it.hasNext();
	}
	
	boolean isFull();
	
	default T getFirst() {
		if(isEmpty()) throw new NoSuchElementException();
		Iterator<T>it=this.iterator();
		return it.next();
	}
	
	default T getLast() {
		if(isEmpty()) throw new NoSuchElementException();
		ListIterator<T>lit= this.listIterator(size());
		return lit.previous();
	}
	
	default void remove(T e) {
		Iterator<T>it=this.iterator();
		while(it.hasNext()) {
			T x= it.next();
			if(x.equals(e)) {it.remove(); break;}
		}
	}
	
	default T removeFirst() {
		if(isEmpty()) return null;
		Iterator<T>it=this.iterator();
		T x=it.next(); it.remove();
		return x;
	}
	
	default T removeLast() {
		if(isEmpty()) return null;
		ListIterator<T> lit= this.listIterator(size());
		T x= lit.previous(); lit.remove();
		return x;
	}
	
	ListIterator<T> listIterator();
	ListIterator<T> listIterator(int pos);
	void addFirst(T e);
	void addLast(T e);
	
	static<T> void sort(List<T> l, Comparator<T> c) {
		int k=1;
		for(int i=0; i<l.size()-1; i++) {
			boolean swap=false;
			ListIterator<T> lit= l.listIterator();
			for(int j=0; j<l.size()-k; j++) {
				T e1=lit.next(); T e2= lit.next();
				if(c.compare(e1,e2)>0) {
					lit.remove();
					lit.remove();
					lit.add(e2);
					lit.add(e1);
					swap=true;
				}
				lit.previous();
			}
			if(!swap)break;
			k++;
		}
	}//bublesort

	
	void salva(String nomeFile) throws IOException;
	void ripristina(String nomeFile) throws IOException;

}
