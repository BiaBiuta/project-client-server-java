package org.example.repository;


import org.example.Child;
import org.example.Registration;
import org.example.Sample;

import java.util.List;

public interface IRegistration extends IRepository<Integer, Registration>{
    List<Child> listChildrenForSample(Sample sample);
    int numberOfChildrenForSample(Sample sample);
    Registration findOneByChildAndSample(Integer child, Integer sample);
}
