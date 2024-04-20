package org.example;

import java.util.List;

public interface ICompetitionServices {
    Child findChild(String name) throws CompetitionException;
    Organizing login(Organizing org,ICompetitionObserver onserver) throws CompetitionException;
    void logout(Organizing user,ICompetitionObserver observer) throws CompetitionException;
    Organizing findOrganizing(String username, String password) throws CompetitionException;

    int numberOfChildrenForSample(Sample sample) throws CompetitionException;

    Iterable<Sample> findAllSamples() throws CompetitionException;
    Child saveChild(String name,int age) throws CompetitionException;

    Sample findSample(String ageCategory, String desen) throws CompetitionException;

    Registration registerChild(Child child, Sample sample) throws CompetitionException;

    List<Child> listChildrenForSample(Sample sample) throws CompetitionException;
}
