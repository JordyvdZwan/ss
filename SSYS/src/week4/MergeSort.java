package week4;

import java.util.*;

public class MergeSort {
    public static <E extends Comparable<E>>
    
    void mergesort(List<E> list) {
    	if (list.size() > 1) {
	    	int i = list.size() / 2;
	    	List<E> fst;
	    	List<E> snd;
	    	if (i >= 2) {
	    		fst = list.subList(0, i - 1);
	        	snd = list.subList(i, list.size());
	        	mergesort(fst);
	        	mergesort(snd);
	    	} else {
	    		fst = list.subList(0, 0);
	        	snd = list.subList(1, 1);
	    	}
	    	
	    	int fi = 0;
	    	int si = 0;
	    	List<E> res = new ArrayList();
	    	
	    	if (list.size() > 1) {
				while (fi < fst.size() && si < snd.size()) {
					if (fst.get(fi).compareTo(snd.get(si)) < 0) {
						res.add(fst.get(fi));	
						fi = fi + 1;
					} else {
						res.add(snd.get(si));
						si = si + 1;
					}
	//				if (fi < fst.size()) {
	//					res.add(fst.get(fi));
	//				} 
	//				if (si < snd.size()) {
	//					res.add(snd.get(si));
	//				
	//				}
				}
			}
	    	list.clear();
	    	for (int j = 0; j < res.size(); j++) {
	    		list.(res.get(j));
	    	}
    	}
    }
}