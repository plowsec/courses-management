package helloworld.service;

import helloworld.entity.*;
import helloworld.entity.secureentities.StudentRegistration;

import java.util.List;
import java.util.Map;

public interface IUserService {

    // -----------------------------------------
    // READ
    // -----------------------------------------

    IUser getUser(String username, String password);

    IUser getUserById(String id);

    List<IUser> getUsers();

    List<Professeur> getTeachers() throws Exception;

    List<Eleve> getStudents() throws Exception;

    // -----------------------------------------
    // CREATE
    // -----------------------------------------

    void addUser(IUser user);

    // -----------------------------------------
    // UPDATE
    // -----------------------------------------

    void updatePerson(User user);

    // -----------------------------------------
    // DELETE
    // -----------------------------------------

    void deleteUserById(String id);

    Map<String, List<StudentRegistration>> getRegistrations(String userName) throws Exception;

    void updateRegistrations(List<Inscription> inscriptions) throws Exception;

    Summary getSummary();

    List<Cours> getCoursesByTeacher(String username) throws Exception;
}
