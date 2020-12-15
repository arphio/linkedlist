package poo.util;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

public abstract class AbstractList<T> implements List<T> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3898877475936258143L;

	@Override
	public String toString() {
		StringBuilder sb= new StringBuilder(300);
		sb.append('[');
		Iterator<T> it=this.iterator();
		while(it.hasNext()) {
			sb.append(it.next());
			if(it.hasNext()) sb.append(", ");
		}
		sb.append(']');
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof List)) return false;
		if(o==this) return true;
		@SuppressWarnings("unchecked")
		List<T> x= (List<T>) o;
		if(x.size()!=this.size()) return false;
		Iterator<T> it1= this.iterator();
		Iterator<T> it2= x.iterator();
		while(it1.hasNext()) {
			if(!(it1.next().equals(it2.next()))) return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		int N=43;
		int h=0;
		for(T e: this) h=h*N+e.hashCode();
		return h;
	}
	
	public void salva(String nomeFile) throws IOException {
		ObjectOutputStream oos=new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(nomeFile)));
		for(T e: this) {
			oos.writeObject(e);
		}
		oos.close();
	}
	
	@SuppressWarnings("unchecked")
	public void ripristina(String nomeFile) throws IOException{
		ObjectInputStream ois=new ObjectInputStream(new BufferedInputStream(new FileInputStream(nomeFile)));
		LinkedList<T> tmp=new LinkedList<>();
		boolean copiaOK=true;
		T n=null;
		for(;;) {
			try {
				n=(T)ois.readObject();
			}catch(EOFException e1) { ois.close();break;}
			catch(ClassCastException e2) {ois.close(); copiaOK=false; break;}
			catch(ClassNotFoundException e3) {ois.close(); copiaOK=false; break;}
			tmp.add(n);
		}
		
		if(copiaOK) {
			this.clear();
			for(T e:tmp) {
				this.add(e);
			}
		}
		else throw new IOException();
	}

}
