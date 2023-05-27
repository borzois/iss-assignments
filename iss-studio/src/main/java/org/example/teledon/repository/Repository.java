package org.example.teledon.repository;

public interface Repository<T, ID> {
    void add(T elem);
    void delete(T elem);
    void update(T elem, ID id);
    T findOne(ID id);
    Iterable<T> findAll();
}
