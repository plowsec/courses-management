package helloworld.dao;

import helloworld.entity.Implication;
import helloworld.entity.JpaCompositePrimaryKeys.ImpliqueComposite;
import helloworld.entity.Professeur;

import java.util.List;

public interface IImplicationDAO {

    // -----------------------------------------
    // READ
    // -----------------------------------------

    List<Implication> getImplications();
    Implication getImplicationByKey(ImpliqueComposite i);
    List<Professeur> getImplicatedProf(int coursId);

    // -----------------------------------------
    // CREATE
    // -----------------------------------------

    void addImplication(Implication implication);

    // -----------------------------------------
    // UPDATE
    // -----------------------------------------

    void updateImplication(Implication implication);

    // -----------------------------------------
    // DELETE
    // -----------------------------------------

    void deleteImplication(Implication implication);

    void removeImplications(int coursId);
}