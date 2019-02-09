package helloworld.dao;

import helloworld.entity.Periode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@Repository
public class PeriodDAO implements IPeriodDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public Periode getPeriod() {
        String query = "from Periode";
        Periode result = (Periode) entityManager.createQuery(query).getResultList().get(0);
        return result;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public void updatePeriod(Periode period) {
        entityManager.remove(getPeriod());
        entityManager.flush();
        entityManager.clear();
        entityManager.persist(period);
    }
}
