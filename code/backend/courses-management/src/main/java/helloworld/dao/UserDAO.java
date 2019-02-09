package helloworld.dao;

import helloworld.entity.*;
import helloworld.entity.secureentities.StudentRegistration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Repository
public class UserDAO implements IUserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public List<Professeur> getTeachers() {
        String queryProf = "from Professeur order by userName";
        List<Professeur> users = new ArrayList<>();
        users.addAll(entityManager.createQuery(queryProf).getResultList());
        return users;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public List<Eleve> getStudents() {
        String queryProf = "from Eleve order by userName";
        List<Eleve> users = new ArrayList<>();
        users.addAll(entityManager.createQuery(queryProf).getResultList());
        return users;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public void addUser(IUser user) {
        final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        switch (user.getRole()) {
            case Professeur.ROLE_TEACHER:
                Professeur professeur = new Professeur(user);
                professeur.setHeadTeacher(false);
                entityManager.persist(professeur);
                break;
            case Professeur.ROLE_HEADTEACHER:
                Professeur headTeacher = new Professeur(user);
                headTeacher.setHeadTeacher(true);
                entityManager.persist(headTeacher);
                break;
            case Eleve.ROLE:
                Eleve eleve = new Eleve(user);
                entityManager.persist(eleve);
                break;
            case Administrator.ROLE:
                Administrator admin = new Administrator(user);
                entityManager.persist(admin);
                break;
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public IUser getUserById(String userId) {
        IUser admin = entityManager.find(Administrator.class, userId);
        if (admin != null) {
            admin.setRole(Administrator.ROLE);
            return admin;
        }

        IUser prof = entityManager.find(Professeur.class, userId);
        if (prof != null) {
            if (((Professeur) prof).isHeadTeacher())
                prof.setRole(Professeur.ROLE_HEADTEACHER);
            else
                prof.setRole(Professeur.ROLE_TEACHER);
            return prof;
        }

        IUser eleve = entityManager.find(Eleve.class, userId);
        if (eleve != null) {
            eleve.setRole(Eleve.ROLE);
            return eleve;
        }

        return null;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public Map<String, List<StudentRegistration>> getRegistrations(String userName) {

        Query q = entityManager.createQuery("select new helloworld.entity.secureentities." +
                "StudentRegistration(inscription.priorite, cours) from Inscription as inscription join Cours as cours" +
                " with inscription.inscriptionComposite.fkCours  = cours.coursId " +
                "where inscription.inscriptionComposite.fkEleve = :userName ");
        q.setParameter("userName", userName);
        List<StudentRegistration> registrations = new ArrayList<>(q.getResultList());
        //registrations.forEach(i -> i.setFkEleve(userName));

        Map<String, List<StudentRegistration>> regs = new HashMap<>();
        regs.put(userName, registrations);
        return regs;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public void updateRegistrations(List<Inscription> inscriptions) {

        //first : clean up registrations of the student
        Query delete = entityManager.createQuery("delete from Inscription where fkEleve=:username");

        /*for(Inscription i : inscriptions)   {
            delete.setParameter("username", i.getInscriptionComposite().getFkEleve());
            delete.executeUpdate();
        }*/
        Query q = entityManager.createQuery("from Inscription where fkEleve=:username order by priorite");
        q.setParameter("username", inscriptions.get(0).getInscriptionComposite().getFkEleve());
        List<Inscription> currentInscriptions = q.getResultList();
        for (Inscription i : currentInscriptions) {
            //Inscription toremove = entityManager.find(Inscription.class, i.getInscriptionComposite());
            entityManager.remove(i);
        }
        entityManager.flush();
        //now add
        for (Inscription i : inscriptions) {
            entityManager.persist(i);
            entityManager.flush();
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public Summary getSummary() {
        Map<String, List<StudentRegistration>> regs = new HashMap<>();

        String queryEleve = "from Eleve order by userName";
        List<Eleve> users = new ArrayList<>(entityManager.createQuery(queryEleve).getResultList());

        String queryCourses = "from Cours order by titre";
        List<Cours> courses = new ArrayList<>(entityManager.createQuery(queryCourses).getResultList());

        /*String queryRegistrations = "from Inscription";
        List<Inscription> registrations = new ArrayList<>(entityManager.createQuery(queryRegistrations).getResultList());
        return new Summary(courses, users, registrations);*/
        for (Eleve e : users) {
            Query q = entityManager.createQuery("select new helloworld.entity.secureentities." +
                    "StudentRegistration(inscription.priorite, cours) from Inscription as inscription join Cours as cours" +
                    " with inscription.inscriptionComposite.fkCours  = cours.coursId " +
                    "where inscription.inscriptionComposite.fkEleve = :userName ");
            q.setParameter("userName", e.getUserName());
            List<StudentRegistration> registrations = new ArrayList<>(q.getResultList());
            //registrations.forEach(i -> i.setFkEleve(userName));

            regs.put(e.getUserName(), registrations);
        }

        return new Summary(courses, regs);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public List<Cours> getCoursesByTeacher(String username) {
        Query q = entityManager.createQuery("select cours from Implication as implication join Cours as cours" +
                " with implication.impliqueComposite.fkCours  = cours.coursId " +
                "where implication.impliqueComposite.fkProf = :userName ");
        q.setParameter("userName", username);
        List<Cours> registrations = new ArrayList<>(q.getResultList());
        //registrations.forEach(i -> i.setFkEleve(userName));

        return registrations;
    }

    // ------------------
    // Deprecated methods
    // ------------------

    @Deprecated
    @Override
    @SuppressWarnings("unchecked")
    public List<IUser> getUsers() {
        String query = "from Administrator order by userName";
        List<IUser> users = new ArrayList<>(entityManager.createQuery(query).getResultList());
        String queryEleve = "from Eleve order by userName";
        users.addAll(entityManager.createQuery(queryEleve).getResultList());
        String queryProf = "from Professeur order by userName";
        users.addAll(entityManager.createQuery(queryProf).getResultList());
        return users;
    }

    @Deprecated
    @Override
    public void updateUser(User user) {
        IUser oldUser = getUserById(user.getUserName());
        oldUser.setUserName(user.getUserName());
        entityManager.flush();
    }

    @Deprecated
    @Override
    public void deleteUser(String userId) {
        entityManager.remove(getUserById(userId));
    }

    @Deprecated
    @Override
    public IUser getUser(String username, String password) {
        // Test if user is admin
        Query queryAdmin = entityManager.createQuery("from Administrator WHERE nomUtilisateur = :userName AND motDePasse = :userPassword");
        queryAdmin.setParameter("userName", username);
        queryAdmin.setParameter("userPassword", password);
        List<IUser> result = (List<IUser>) queryAdmin.getResultList();

        // If he is not
        if (result.isEmpty()) {
            // Test if user is teacher
            Query queryProf = entityManager.createQuery("from Professeur WHERE nomUtilisateur = :userName AND motDePasse = :userPassword");
            queryProf.setParameter("userName", username);
            queryProf.setParameter("userPassword", password);
            result = (List<IUser>) queryProf.getResultList();

            // If he is not
            if (result.isEmpty()) {
                // Test if user is student
                Query queryEleve = entityManager.createQuery("from Eleve WHERE nomUtilisateur = :userName AND motDePasse = :userPassword");
                queryEleve.setParameter("userName", username);
                queryEleve.setParameter("userPassword", password);
                result = (List<IUser>) queryEleve.getResultList();
            }
        }
        return result.isEmpty() ? null : result.get(0);
    }
}
