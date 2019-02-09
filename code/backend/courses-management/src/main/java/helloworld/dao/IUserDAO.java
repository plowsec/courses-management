package helloworld.dao;

import helloworld.entity.*;
import helloworld.entity.secureentities.StudentRegistration;

import java.util.List;
import java.util.Map;

public interface IUserDAO {

    // -----------------------------------------
    // READ
    // -----------------------------------------

    List<IUser> getUsers();

    IUser getUserById(String id);

    List<Professeur> getTeachers();

    List<Eleve> getStudents();

    // -----------------------------------------
    // CREATE
    // -----------------------------------------

    void addUser(IUser user);

    // -----------------------------------------
    // UPDATE
    // -----------------------------------------

    void updateUser(User user);

    // -----------------------------------------
    // DELETE
    // -----------------------------------------

    void deleteUser(String userId);

    IUser getUser(String username, String password);

    Map<String, List<StudentRegistration>> getRegistrations(String userName);

    void updateRegistrations(List<Inscription> inscriptions);

    Summary getSummary();

    List<Cours> getCoursesByTeacher(String username);
}