package helloworld.service;

import helloworld.entity.Cours;
import helloworld.entity.JpaCompositePrimaryKeys.InscriptionComposite;
import helloworld.entity.secureentities.CoursImpProf;

import java.util.List;
import java.util.Map;

public interface ICourseService {

    // -----------------------------------------
    // READ
    // -----------------------------------------

    Cours getCourseById(int id) throws Exception;

    List<Cours> getAllCourses();

    List<Cours> getCourses();

    // -----------------------------------------
    // CREATE
    // -----------------------------------------

    void addCourse(CoursImpProf coursAndProfs)  throws Exception;

    // -----------------------------------------
    // UPDATE
    // -----------------------------------------

    void updateCourse(CoursImpProf cours)  throws Exception;

    void setCourseDraft(int coursId) throws Exception;

    void setCourseActive(int coursId) throws Exception;
    void setCourseValid(int coursId) throws Exception;

    /**
     * access : everyone
     * @return a mapping between the courses id and their number of registrations
     */
    Map<Integer, Integer> regsCounts();

    @Deprecated
    void updateEtatCourse(Cours cours);

    /**
     * used by headteacher only to close the registrations
     */
    void closeRegistrations() throws Exception;

    List<InscriptionComposite> getCourseRegistrationsById(int id);
}
