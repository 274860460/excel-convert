package quiet.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Group {

	public static <K, V> Map<K, List<V>> create(List<V> list, Key<K, V> key) {
		Map<K, List<V>> m = new TreeMap<K, List<V>>();
		create(m, list, key);
		return m;
	}
	
	public static <K, V> void create(Map<K, List<V>> m, List<V> list, Key<K, V> key) {
		for (V o : list) {
			K k = key.get(o);
			List<V> l = (List<V>) m.get(k);
			if (l == null) {
				l = new ArrayList<V>();
				m.put(k, l);
			}
			l.add(o);
		}
	}
}
