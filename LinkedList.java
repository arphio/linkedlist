package poo.util;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;



public class LinkedList<T> extends AbstractList<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8606408388121954488L;

	private enum mov{FORWARD, BACKWARD, UNKNOWN};
	
	private static class Nodo<E>{
		E info;
		Nodo<E> pre,next;
		public String toString() {return info.toString();}
	}
	
	private Nodo<T> inizio= null, fine=null;
	private int size=0;
	private int cntMod=0;
	
	public int size() {return size;}
	public boolean isFull() {return false;}
	public void clear() {size=0; inizio=null; fine=null; cntMod=0;}

	@Override
	public void add(T e) {
		addLast(e);
	}//add
	

	@Override
	public ListIterator<T> listIterator() {
		return new ListIteratorImpl();
	}

	@Override
	public ListIterator<T> listIterator(int pos) {
		return new ListIteratorImpl(pos);
	}
	

	@Override
	public void addFirst(T e) {
		Nodo<T> nuovo=new Nodo<T>();
		nuovo.info=e;
		if(inizio==null) {
			inizio=nuovo;
			fine=nuovo;
		}
		else {
			inizio.pre=nuovo;
			nuovo.next=inizio;
			inizio=nuovo;
		}
		size++;
		cntMod++;
	}

	@Override
	public void addLast(T e) {
		Nodo<T> n= new Nodo<T>();
		n.info=e;
		if(fine==null) {
			fine=n;
			inizio=n;
		}
		else {
			fine.next=n;
			n.pre=fine;
			fine=n;
		}
		size++;
		cntMod++;
		
	}

	@Override
	public Iterator<T> iterator() {
		return this.listIterator();
	}
	
	private class ListIteratorImpl implements ListIterator<T>{
		private Nodo<T> previous, succ;
		private mov lastMov= mov.UNKNOWN;
		private int index, cntModIt;
		
		public ListIteratorImpl(int pos) {
			if(pos>size || pos<0) throw new IllegalArgumentException("Impossibile raggiungere elemento lista.");
			succ=inizio;
			index=pos-1;
			cntModIt=cntMod;
			for(int i=0; i<pos; i++) {
				previous=succ;
				succ=previous.next;
			}
		}
		public ListIteratorImpl() {
			this(0);
		}

		@Override
		public void add(T arg0) {
			if(cntMod!=cntModIt)throw new ConcurrentModificationException();
			Nodo<T> nuovo= new Nodo<>();
			nuovo.info=arg0;
			if(inizio==null) {
				inizio=fine=nuovo;
				previous=nuovo;
			}
			
			else if(succ==null) {
				previous.next=nuovo;
				nuovo.pre=previous;
				previous=nuovo;
				fine=previous;
			}
			else if(previous==null) {
				inizio=nuovo;
				previous=nuovo;
				succ.pre=previous;
				previous.next=succ;
			}
			else {
				previous.next=nuovo;
				nuovo.pre=previous;
				previous=nuovo;
				previous.next=succ;
				succ.pre=previous;
			}
			size++;
			index++;
			cntModIt++; 
			cntMod++;
		}

		@Override
		public boolean hasNext() {
			return succ!=null;
		}

		@Override
		public boolean hasPrevious() {
			return previous!=null;
		}

		@Override
		public T next() {
		 if(!hasNext()) throw new IllegalStateException();
		    previous=succ;
		    succ=succ.next;
		    lastMov = mov.FORWARD;
		    index++;
			return previous.info;
		}

		@Override
		public int nextIndex() {
			return index+1;
		}

		@Override
		public T previous() {
			if(!hasPrevious()) throw new IllegalStateException();
			succ=previous;
			previous=previous.pre;
			lastMov= mov.BACKWARD;
			index--;
			return succ.info;
		}

		@Override
		public int previousIndex() {
			return index;
		}

		@Override
		public void remove() {
			if(cntMod!=cntModIt)throw new ConcurrentModificationException();
			if(lastMov==mov.FORWARD) {
				if(previous==inizio) inizio=succ;
				if(succ!=null)
					succ.pre=previous.pre;
				previous=previous.pre;
				if(succ==null)
					fine=previous;
				if(previous!=null)
					previous.next=succ;
				index--;
			}
			else if(lastMov==mov.BACKWARD) {
				if(succ==inizio) inizio= succ.next;
				if(previous!=null) 
					previous.next=succ.next;
				succ=succ.next;
				if(succ!=null)
					succ.pre=previous;
				if(succ==null)
					fine=previous;
			}
			else throw new IllegalStateException();
			size--;
			cntMod++; cntModIt++;
		}

		@Override
		public void set(T e) {
			if(cntMod!=cntModIt)throw new ConcurrentModificationException();
			Nodo<T> n= new Nodo<T>();
			n.info=e;
			if(lastMov==mov.FORWARD) {
				if(previous.pre!=null)
					previous.pre.next=n;
				n.pre=previous.pre;
				n.next=succ;
				if(succ!=null)
					succ.pre=n;
				previous=n;
				if(succ==null)
					fine=previous;
				if(previous.pre==null)
					inizio=previous;
			}
			else if(lastMov==mov.BACKWARD) {
				n.pre=previous;
				if(previous!=null)
					previous.next=n;
				n.next=succ.next;
				if(succ.next!=null)
					succ.next.pre=n;
				succ=n;
				if(previous==null)
					inizio=succ;
				if(succ.next==null)
					fine=succ;
			}
			else throw new IllegalStateException();
			cntMod++; cntModIt++;
		}
		
	}
	
	public static void main(String...args) {
		List<Integer> l=new LinkedList<>();
		l.add(10); l.add(9);l.add(8); l.add(7);l.add(6);
		ListIterator<Integer> lit=l.listIterator();
		for(int i=0; i<5; i++) {
			
		}
		lit.next(); lit.next(); lit.previous();
		lit.add(0); System.out.println(lit.next());
		l.clear(); l.add(45); l.addFirst(9); l.addLast(-58);
		System.out.println(l);
		
	}


}
