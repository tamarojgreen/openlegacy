package org.openlegacy.utils;

import org.hibernate.collection.PersistentBag;
import org.hibernate.collection.PersistentList;
import org.hibernate.collection.PersistentMap;
import org.hibernate.collection.PersistentSet;
import org.hibernate.collection.PersistentSortedSet;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Dehibernator {

	private IdentityHashMap<Object, Object> processed;

	@SuppressWarnings("unchecked")
	public <T> T clean(T object) {
		processed = new IdentityHashMap<Object, Object>();

		return (T)doClean(object);
	}

	@SuppressWarnings("unchecked")
	private Object doClean(Object dirty) {

		if (dirty == null) {
			return null;
		}

		if (processed.containsKey(dirty)) {

			return processed.get(dirty);
		}

		if (isPrimitive(dirty)) {

			return dirty;
		}

		if (dirty instanceof PersistentList) {

			PersistentList dirtyList = (PersistentList)dirty;
			List<Object> cleanList = new ArrayList<Object>();
			processed.put(dirtyList, cleanList);
			if (dirtyList.wasInitialized()) {
				for (Object value : dirtyList) {
					cleanList.add(doClean(value));
				}
			}
			return cleanList;
		}

		if (dirty instanceof PersistentBag) {

			PersistentBag dirtyList = (PersistentBag)dirty;
			List<Object> cleanList = new ArrayList<Object>();
			processed.put(dirtyList, cleanList);
			if (dirtyList.wasInitialized()) {
				for (Object value : dirtyList) {
					cleanList.add(doClean(value));
				}
			}
			return cleanList;
		}

		if (dirty instanceof PersistentSortedSet) {

			PersistentSortedSet dirtySet = (PersistentSortedSet)dirty;
			Set<Object> cleanSet = new TreeSet<Object>();
			processed.put(dirtySet, cleanSet);
			if (dirtySet.wasInitialized()) {
				for (Object value : dirtySet) {
					cleanSet.add(doClean(value));
				}
			}
			return cleanSet;
		}

		if (dirty instanceof PersistentSet) {

			PersistentSet dirtySet = (PersistentSet)dirty;
			Set<Object> cleanSet = new HashSet<Object>();
			processed.put(dirtySet, cleanSet);
			if (dirtySet.wasInitialized()) {
				for (Object value : dirtySet) {
					cleanSet.add(doClean(value));
				}
			}
			return cleanSet;
		}

		if (dirty instanceof PersistentMap) {

			PersistentMap dirtyMap = (PersistentMap)dirty;
			Map<Object, Object> cleanMap = new LinkedHashMap<Object, Object>();
			processed.put(dirtyMap, cleanMap);
			if (dirtyMap.wasInitialized()) {
				for (Object key : dirtyMap.keySet()) {
					Object value = dirtyMap.get(key);
					cleanMap.put(doClean(key), doClean(value));
				}
			}
			return cleanMap;
		}

		if (dirty instanceof List) {

			List<Object> dirtyList = (List<Object>)dirty;
			List<Object> cleanList = new ArrayList<Object>();
			processed.put(dirtyList, cleanList);
			for (Object value : dirtyList) {
				cleanList.add(doClean(value));
			}
			return cleanList;
		}

		if (dirty instanceof LinkedHashMap) {

			Map<Object, Object> dirtyMap = (Map<Object, Object>)dirty;
			Map<Object, Object> cleanMap = new LinkedHashMap<Object, Object>();
			processed.put(dirtyMap, cleanMap);
			for (Object key : dirtyMap.keySet()) {
				Object value = dirtyMap.get(key);
				cleanMap.put(doClean(key), doClean(value));
			}
			return cleanMap;
		}

		if (dirty instanceof HashMap) {

			Map<Object, Object> dirtyMap = (Map<Object, Object>)dirty;
			Map<Object, Object> cleanMap = new HashMap<Object, Object>();
			processed.put(dirtyMap, cleanMap);
			for (Object key : dirtyMap.keySet()) {
				Object value = dirtyMap.get(key);
				cleanMap.put(doClean(key), doClean(value));
			}
			return cleanMap;
		}

		if (dirty instanceof LinkedHashSet<?>) {

			Set<Object> dirtySet = (LinkedHashSet<Object>)dirty;
			Set<Object> cleanSet = new LinkedHashSet<Object>();
			processed.put(dirtySet, cleanSet);
			for (Object value : dirtySet) {
				cleanSet.add(doClean(value));
			}
			return cleanSet;
		}

		if (dirty instanceof HashSet<?>) {

			Set<Object> dirtySet = (HashSet<Object>)dirty;
			Set<Object> cleanSet = new HashSet<Object>();
			processed.put(dirtySet, cleanSet);
			for (Object value : dirtySet) {
				cleanSet.add(doClean(value));
			}
			return cleanSet;
		}

		if (dirty instanceof TreeSet<?>) {

			Set<Object> dirtySet = (TreeSet<Object>)dirty;
			Set<Object> cleanSet = new TreeSet<Object>();
			processed.put(dirtySet, cleanSet);
			for (Object value : dirtySet) {
				cleanSet.add(doClean(value));
			}
			return cleanSet;
		}

		if (dirty instanceof HibernateProxy) {

			HibernateProxy proxy = (HibernateProxy)dirty;
			LazyInitializer lazyInitializer = proxy.getHibernateLazyInitializer();
			if (lazyInitializer.isUninitialized()) {

				processed.put(dirty, null);
				return null;
			} else {

				dirty = lazyInitializer.getImplementation();
			}
		}

		processed.put(dirty, dirty);
		// for (String property : ReflectionUtils.getProperties(dirty)) {
		//
		// Object value = ReflectionUtils.get(dirty, property);
		// ReflectionUtils.setIfPossible(dirty, property, doClean(value));
		// }
		return dirty;
	}

	private static boolean isPrimitive(Object object) {
		if (object instanceof String) {
			return true;
		}

		if (object instanceof Date) {
			return true;
		}

		if (object instanceof Enum) {
			return true;
		}

		Class<? extends Object> xClass = object.getClass();
		if (xClass.isPrimitive()) {
			return true;
		}

		return false;
	}
}
