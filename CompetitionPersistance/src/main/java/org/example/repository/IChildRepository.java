package org.example.repository;


import org.example.Child;

public interface IChildRepository extends IRepository<Integer, Child>{
    Child update(Child entityForUpdate);

    Child findByName(String name);
}
