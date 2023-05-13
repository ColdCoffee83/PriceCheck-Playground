import javax.persistence.EntityManager;

import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import gov.cia.americano.domesticmodel.equityChecks.EquityChecksSearchHelp;

@Repository
public interface EquityChecksSearchHelpRepository extends
JpaRepository<EquityChecksSearchHelp, Long>{

@Transactional
@Modifying
@Query ("DELETE FROM EquityChecksSearchHelp p WHERE p.equityCheck.id = :equityCheckId")

void deleteByEquityCheckId(@Param("equityCheckId") Long equityCheckId);

}