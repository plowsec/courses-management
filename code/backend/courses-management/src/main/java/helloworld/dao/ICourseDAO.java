package helloworld.dao;

import helloworld.entity.Cours;
import helloworld.entity.JpaCompositePrimaryKeys.InscriptionComposite;

import java.util.List;
import java.util.Map;

public interface ICourseDAO {

    // -----------------------------------------
    // READ
    // -----------------------------------------

    Cours getCourseById(int id);

    Cours getCourseByTitle(String title);

    List<Cours> getAllCourses();

    List<Cours> getCourses();

    // -----------------------------------------
    // CREATE
    // -----------------------------------------

    void addCourse(Cours cours);

    // -----------------------------------------
    // UPDATE
    // -----------------------------------------

    void updateCourse(Cours cours);



    void setCourseDraft(int coursId);

    void setCourseValid(int coursId);

    void setCourseActive(int coursId);

    Map<Integer, Integer> regsCounts();

    void updateEtatCours(Cours cours);

    void closeRegistrations();

    List<InscriptionComposite> getCourseRegistrationsById(int id);
}
