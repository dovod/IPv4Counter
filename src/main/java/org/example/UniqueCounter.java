package org.example;

public interface UniqueCounter<E> {

    void add(E element);

    int getUniqueCount();
}
