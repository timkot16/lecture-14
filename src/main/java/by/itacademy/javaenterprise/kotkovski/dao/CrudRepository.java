package by.itacademy.javaenterprise.kotkovski.dao;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<K, E> {

    void delete(K id);

    E save(E entity);

    void update(K id, E entity);

    List<E> findAll();

    Optional<E> findById(K id);
}
