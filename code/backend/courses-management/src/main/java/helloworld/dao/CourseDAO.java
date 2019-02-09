package helloworld.dao;

import helloworld.dao.dto.NbCourses;
import helloworld.dao.repository.CourseRepository;
import helloworld.entity.Cours;
import helloworld.entity.JpaCompositePrimaryKeys.InscriptionComposite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Repository
public class CourseDAO implements ICourseDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public Cours getCourseById(int id) {
        return entityManager.find(Cours.class, id);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public Cours getCourseByTitle(String title) {
        Query q = entityManager.createQuery("from Cours WHERE titre = :title");
        q.setParameter("title", title);
        return (Cours) q.getResultList().get(0);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public List<Cours> getAllCourses() {
        String query = "from Cours order by titre";
        List<Cours> courses = new ArrayList<>();
        courses.addAll(entityManager.createQuery(query).getResultList());
        return courses;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public List<Cours> getCourses() {
        String query = "from Cours order by coursId";
        List<Cours> courses = new ArrayList<>();
        courses.addAll(entityManager.createQuery(query).getResultList());
        return courses;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public void addCourse(Cours cours) {
        entityManager.persist(new Cours(cours));
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public void updateCourse(Cours cours) {
        Cours oldCours = getCourseById(cours.getCoursId());
        oldCours.setDescriptionXML(cours.getDescriptionXML());
        oldCours.setEtat(cours.getEtat());
        oldCours.setEtudMax(cours.getEtudMax());
        oldCours.setSemestrePref(cours.getSemestrePref());
        oldCours.setTitre(cours.getTitre());
        entityManager.flush();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public void setCourseDraft(int coursId) {
        Cours oldCours = getCourseById(coursId);
        oldCours.setEtat("Brouillon");
        entityManager.flush();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public void setCourseValid(int coursId) {
        Cours oldCours = getCourseById(coursId);
        oldCours.setEtat("Valide");
        entityManager.flush();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public void setCourseActive(int coursId) {
        Cours oldCours = getCourseById(coursId);
        oldCours.setEtat("Actif");
        entityManager.flush();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public Map<Integer, Integer> regsCounts() {
        String q = "select new helloworld.dao.dto.NbCourses(inscription.inscriptionComposite.fkCours,count(inscription.inscriptionComposite.fkCours)) from Inscription as inscription group by inscription.inscriptionComposite.fkCours";
        Map<Integer, Integer> result = new HashMap<>();
       /*
        try  {
            Session session = entityManager.unwrap(Session.class);
            Transaction transaction = session.beginTransaction();
       */
        Query qr = entityManager.createQuery(q); //instead of query
        List<NbCourses> coursesCount = qr.getResultList();
        for (NbCourses nb : coursesCount) {
            result.put(nb.getFkCours(), nb.getNbRegistrations());
        }
        /*
            transaction.commit();
            session.close();

        }
        catch(Exception e)  {
            e.printStackTrace();
        }
        */
        return result;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public void closeRegistrations() {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("clean_non_activated_courses");
        query.execute();
    }

    @Override
    public List<InscriptionComposite> getCourseRegistrationsById(int id) {
        Query q = entityManager.createQuery("select new helloworld.entity.JpaCompositePrimaryKeys.InscriptionComposite(inscription.inscriptionComposite.fkEleve, inscription.inscriptionComposite.fkCours) from Inscription as inscription where inscription.inscriptionComposite.fkCours = :id");
        q.setParameter("id", id);
        List<InscriptionComposite> inscriptions = new ArrayList<>();
        inscriptions.addAll(q.getResultList());
        return inscriptions;
    }

    // ------------------
    // Deprecated methods
    // ------------------

    @Deprecated
    @Override
    public void updateEtatCours(Cours cours) {
        Cours oldCours = getCourseById(cours.getCoursId());
        oldCours.setEtat(cours.getEtat());
        entityManager.flush();
    }
}
