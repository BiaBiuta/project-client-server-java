package org.example.repository;


import org.example.Sample;

public interface ISampleRepository extends IRepository<Integer, Sample>{
     Sample findOneByCategoryAndAge(String category, String age_category);

}
