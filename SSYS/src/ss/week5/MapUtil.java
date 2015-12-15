package ss.week5;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MapUtil {
	
	//@ ensures \forall K k1;\forall K k2; (k1 != k2) ==> (map.get(k1) != map.get(k2));
    //@ pure
	public static <K, V> boolean isOneOnOne(Map<K, V> map) {
        boolean result = true;
    	Iterator<K> ik = map.keySet().iterator();
    	Iterator<K> jk;
    	Iterator<V> iv = map.values().iterator();
    	Iterator<V> jv;
    	K k;
    	V v;
    	while (ik.hasNext()) {
    		jk = ik;
    		jv = iv;
    		k = ik.next();
    		v = iv.next();
    		while (jk.hasNext()) {
    			if (k != jk.next() ? v == jv.next() : false) {
    				result = false;
    				break;
    			}
    		}
    	}
        return result;
    }
    
    //  comes from map but how to write that VVVVVVVVVVVVVVVVVVVVVVVVVVVV
    
	//@ ensures \result == \forall V vr; range.contains(v); map.values().contains(vr);
	//@ pure
    public static <K, V> boolean isSurjectiveOnRange(Map<K, V> map, Set<V> range) {
        Iterator<V> iv = range.iterator();
        boolean result = true;
        while (iv.hasNext()) {
        	if (!map.values().contains(iv.next())) {
        		result = false;
        		break;
        	}
        }
        return result;
    }
    

    //@ ensures \forall K k; inverse.map.get(map.get(k)) == k;
    public static <K, V> Map<V, Set<K>> inverse(Map<K, V> map) {
        Map<V, Set<K>> result = new HashMap<V, Set<K>>();
        Iterator<V> iv = map.values().iterator();
        Iterator<K> ik = map.keySet().iterator();
        V v;
        K k;
    	while (iv.hasNext()) {
    		Set<K> set = new HashSet<K>();
    		v = iv.next();
    		k = ik.next();
			Iterator<V> jv = map.values().iterator();
			Iterator<K> jk = map.keySet().iterator();
	        V vj;
	        K kj;
			while (jv.hasNext()) {
	    		vj = jv.next();
	    		kj = jk.next();
				if (vj == v) {
				set.add(kj);
				}
			}
			result.put(v, set);
    	}	
        return result;
	}
    
//    		if (result.values().contains(v)) { <---------------------werkt niet naar behoren
//    		} else {
//    			Set<K> set = new HashSet<K>();
//    			set.add(k);
//    			result.put(v, set);
//    		}
    
    //@ requires isSurjective(map)
    //@ requires oneOnOne(map)
    //@ ensures \forall K k; inverse.map.get(map.get(k)) == k;
	public static <K, V> Map<V, K> inverseBijection(Map<K, V> map) {
		Map<V, K> result = new HashMap<V, K>();
        Iterator<V> iv = map.values().iterator();
        Iterator<K> ik = map.keySet().iterator();
        Set<V> values = new HashSet<V>(map.values());
		if (isOneOnOne(map) && isSurjectiveOnRange(map , values)) {
        	while (iv.hasNext()) {
        		result.put(iv.next(), ik.next());
        	}
        } else {
        	result = null;
        }
        return result;
	}
	
	
	//@ ensures \forall K k1; g.containsValue(g.get(k1)) ;
	public static <K, V, W> boolean compatible(Map<K, V> f, Map<V, W> g) {
		Iterator<V> fi = f.values().iterator();
		boolean result = true;
		while (fi.hasNext()) {
			if (!g.keySet().contains(fi.next())) {
				result = false;
			}
		}
        return result;
	}
	
	//@
	//@ ensures \forall K k1; g.get((f.get(k1))).equals(\result.get(k1));
	public static <K, V, W> Map<K, W> compose(Map<K, V> f, Map<V, W> g) {
		Map<K, W> result = new HashMap<K, W>();
			if (compatible(f, g)) {
			Iterator<V> fvi = f.values().iterator();
			Iterator<K> fki = f.keySet().iterator();
			while (fvi.hasNext()) {
				V v = fvi.next();
				K k = fki.next();
				result.put(k, g.get(v));
			}
		} else {
			result = null;
		}
        return result;
	}
}
