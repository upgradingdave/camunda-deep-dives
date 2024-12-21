package org.camunda.community.utils;


import java.util.HashMap;
import java.util.Map;

public class SimpleCache<K, V> {
  private Map<K, V> cache;

  public SimpleCache() {
    cache = new HashMap<>();
  }

  public V get(K key) {
    return cache.get(key);
  }

  public void put(K key, V value) {
    cache.put(key, value);
  }

  public void remove(K key) {
    cache.remove(key);
  }

  public void clear() {
    cache.clear();
  }
}

