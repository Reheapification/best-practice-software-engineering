package at.ac.tuwien.ifs.bpse.designpatterns.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class IteratorExample {


	public static void main (String args[]) {
		
		List<String> movies = new ArrayList<String>();
		Queue<String> steps = new LinkedList<String>();
		
		movies.add("Spiderman");
		movies.add("Superman");
		movies.add("X-Men");
		
		steps.add("Open Washing Machine");
		steps.add("Put Clothes in");
		steps.add("Close Washing Maching");
		steps.add("Add Washing Powder");
		steps.add("Start Machine");
		
		System.out.println("Movies:");
		Iterator<String> it = movies.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}

		System.out.println("Movies:");
		for (String s : movies) {
			System.out.println(s);
		}

		System.out.println("Steps:");
		it = steps.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}

		System.out.println("Steps:");
		for (String s : steps) {
			System.out.println(s);
		}
	}

}