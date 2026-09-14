package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.persistence.entities.Adresse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Hans Stamm
 */
@Repository
public interface AdresseRepository extends JpaRepository<Adresse, Integer> {}
