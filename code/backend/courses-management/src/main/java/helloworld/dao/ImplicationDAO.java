package helloworld.dao;

import helloworld.entity.Implication;
import helloworld.entity.JpaCompositePrimaryKeys.ImpliqueComposite;
import helloworld.entity.Professeur;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Transactional
@Repository
public class ImplicationDAO implements IImplicationDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public Implication getImplicationByKey(ImpliqueComposite i) {
        return entityManager.find(Implication.class, i);
    }

    //for the moment, do a null pointer exeption... fuck it (but works in workbench)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public List<Professeur> getImplicatedProf(int coursId) {
        Query queryAdmin = entityManager.createQuery("SELECT professeur FROM Professeur as professeur JOIN Implication as implication " +
                "with implication.impliqueComposite.fkProf = professeur.userName where implication.impliqueComposite.fkCours = :coursId");
        queryAdmin.setParameter("coursId", coursId);
        List<Professeur> profs;
        profs = (List<Professeur>) queryAdmin.getResultList();
        return profs;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public void addImplication(Implication implication) {
        entityManager.persist(implication);
    }

    // ------------------
    // Deprecated methods
    // ------------------

    @Deprecated
    @Override
    public List<Implication> getImplications() {
        return null;
    }

    @Deprecated
    @Override
    public void updateImplication(Implication implication) {
    }

    @Deprecated
    @Override
    public void deleteImplication(Implication implication) {
    }

    @Override
    public void removeImplications(int coursId) {
        Query q = entityManager.createQuery("from Implication as implication where implication.impliqueComposite.fkCours = :coursId");
        q.setParameter("coursId", coursId);
        List<Implication> currentImplications = q.getResultList();
        for (Implication i : currentImplications)
            entityManager.remove(i);
        entityManager.flush();
    }
}
