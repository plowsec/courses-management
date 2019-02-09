package helloworld.service;

import helloworld.dao.IUserDAO;
import helloworld.entity.*;
import helloworld.entity.secureentities.StudentRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserDAO userDAO;

    @Override
    public List<IUser> getUsers() {
        List<IUser> users = userDAO.getUsers();
        //passwords should not be able to escape the database
        users.forEach(i -> i.setPassword("****"));
        return users;
    }

    @Override
    public List<Professeur> getTeachers() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().toArray()[0].toString();
        boolean isProf = role.equals("ROLE_" + Professeur.ROLE_HEADTEACHER) || role.equals("ROLE_" + Professeur.ROLE_TEACHER);
        if (!isProf)
            throw new Exception("Error 403 : unauthorized access to the list of teachers");
        List<Professeur> users = userDAO.getTeachers();
        //passwords should not be able to escape the database
        users.forEach(i -> i.setPassword("****"));
        return users;
    }

    @Override
    public List<Eleve> getStudents() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().toArray()[0].toString();
        boolean isProf = role.equals("ROLE_" + Professeur.ROLE_HEADTEACHER) || role.equals("ROLE_" + Professeur.ROLE_TEACHER);
        if (!isProf)
            throw new Exception("Error 403 : unauthorized access to the list of students");
        List<Eleve> users = userDAO.getStudents();
        //passwords should not be able to escape the database
        users.forEach(i -> i.setPassword("****"));
        return users;
    }

    @Override
    public synchronized void updatePerson(User user) {
        userDAO.updateUser(user);
    }

    @Override
    public void deleteUserById(String id) {
        userDAO.deleteUser(id);
    }

    @Override
    public synchronized void addUser(IUser user) {
        userDAO.addUser(user);
    }

    @Override
    public IUser getUser(String username, String password) {
        return userDAO.getUser(username, password);
    }

    @Override
    public IUser getUserById(String id) {
        IUser user = userDAO.getUserById(id);
        //user.setPassword("****");
        return user;
    }

    @Override
    public Map<String, List<StudentRegistration>> getRegistrations(String userName) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().toArray()[0].toString();
        boolean isProf = role.equals("ROLE_" + Professeur.ROLE_HEADTEACHER) || role.equals("ROLE_" + Professeur.ROLE_TEACHER);
        if (!isProf && !isAuthorized(userName))
            throw new Exception("Error 403 : unauthorized");

        return userDAO.getRegistrations(userName);
    }

    private boolean isAuthorized(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName().equals(username);
    }

    @Override
    public void updateRegistrations(List<Inscription> inscriptions) throws Exception {

        //check : this method can only be called by a student that modifies its own registrations
        for (Inscription i : inscriptions) {
            if (!isAuthorized(i.getInscriptionComposite().getFkEleve()))
                throw new Exception("Error 403 : unauthorized");
        }

        List<Inscription> safeRegistrations = new ArrayList<>();

        //get only valid registrations priorities
        inscriptions
                .stream()
                .filter(i -> i.getPriorite() == 2 || i.getPriorite() == 1)
                .forEach(safeRegistrations::add);

        //count the '1' priorities
        long nbRegsPriorityOne = safeRegistrations
                .stream()
                .filter(i -> i.getPriorite() == 1)
                .count();
        //count the '2' priorities
        long nbRegsPriorityTwo = safeRegistrations
                .stream()
                .filter(i -> i.getPriorite() == 2)
                .count();

        if (nbRegsPriorityOne != Inscription.NB_REGS_PRIORITY_ONE || nbRegsPriorityTwo != Inscription.NB_REGS_PRIORITY_TWO)
            throw new Exception("Students have to choose four registrations with priority 1, and one registration with priority 2");

        userDAO.updateRegistrations(safeRegistrations);
    }

    @Override
    public Summary getSummary() {
        Summary summary = userDAO.getSummary();
        return userDAO.getSummary();
    }

    @Override
    public List<Cours> getCoursesByTeacher(String username) throws Exception {

        if (!isAuthorized(username))
            throw new Exception("Error 403 : someone tried to access the courses of another teacher");

        return userDAO.getCoursesByTeacher(username);
    }

}
