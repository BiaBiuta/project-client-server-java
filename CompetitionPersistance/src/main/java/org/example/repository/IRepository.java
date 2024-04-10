package org.example.repository;


import org.example.Entity;

public interface IRepository <ID,E extends Entity<ID>>{
    //organizator are nevoie de gasire dupa user si parola
    //child are nevoie
    E findOne(ID id);
    Iterable<E> findAll();
    E save(E entityForAdd);



}
