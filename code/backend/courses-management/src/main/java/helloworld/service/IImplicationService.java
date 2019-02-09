package helloworld.service;

import helloworld.entity.Implication;
import helloworld.entity.Professeur;

import java.util.List;

public interface IImplicationService {

    // -----------------------------------------
    // READ
    // -----------------------------------------

    /**
     * allowed : a teacher implicated in the course
     * @param coursId id of the course to modify
     * @return a list of teachers implicated in the course
     */
    List<Professeur> getImplicatedProf(int coursId);


    // -----------------------------------------
    // CREATE
    // -----------------------------------------

    /**
     * todo : access validation
     * @param implication
     */
    void addImplication(Implication implication);

    // -----------------------------------------
    // UPDATE
    // -----------------------------------------

    /**
     * todo : access validation
     * @param implication
     */
    void updateImplication(Implication implication);

}
