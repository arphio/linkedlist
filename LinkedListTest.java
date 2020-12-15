package poo.util;

import java.util.Comparator;
import java.util.ListIterator;
import java.util.Random;

public class LinkedListTest {
	
	public static void main(String...args) {
		LinkedList<Integer> l=new LinkedList<>();
		for(int i=0; i<30; i++) {
			Random r=new Random();
			l.add(r.nextInt(300));
		}
		
		System.out.println(l);
		MioComparatore c=new MioComparatore();
		List.sort(l,c);
		System.out.println(l);
		l.clear();
		l.add(89); l.addFirst(12); l.addFirst(8); l.addFirst(-187);
		l.addLast(-78);
		System.out.println(l);
		ListIterator<Integer> lit=l.listIterator();
		lit.next(); lit.next(); lit.previous(); lit.remove(); //canc -187,12,89,-78
		System.out.println(l);
		lit.next(); lit.add(1); //-187,12,1,89,-78
		System.out.println(l);
		System.out.println(lit.nextIndex());//3
		System.out.println(lit.previousIndex());//2
		while(lit.hasNext()) lit.next();
		System.out.println(lit.nextIndex());//5
		while(lit.hasPrevious())lit.previous();
		System.out.println(lit.previousIndex());//-1
	}
	
	static class MioComparatore implements Comparator<Integer>{

		@Override
		public int compare(Integer o1, Integer o2) {
			if(o1<o2)return -1;
			else if(o1>o2) return 1;
			return 0;
		}
		
	}
	

}
