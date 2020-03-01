package com.example.moviesearcher.model.services.responses;

import com.example.moviesearcher.model.data.Person;

import java.util.List;

public interface PersonListAsyncResponse {
    void processFinished(List<Person> castList, List<Person> crewList);
}
