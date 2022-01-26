package link.shortener.repository;

import java.util.NavigableSet;

import org.mapdb.DB;

public interface MapDBRepository<K, V> {
    DB beginTransaction();

    void save(DB tx, String key, String value);

    NavigableSet<String> find(DB tx);

    String find(DB tx, String key);
}