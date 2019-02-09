package helloworld.dao.repository;

import helloworld.entity.Cours;
import helloworld.entity.JpaCompositePrimaryKeys.InscriptionComposite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Cours, InscriptionComposite> {


    //List<Cours> findByInscriptionCompositefkEleve(String fkEleve);
}
