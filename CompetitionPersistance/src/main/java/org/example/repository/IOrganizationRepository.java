package org.example.repository;


import org.example.Organizing;

public interface IOrganizationRepository extends IRepository<Integer, Organizing>{
    Organizing findByName(String username, String password);
}
