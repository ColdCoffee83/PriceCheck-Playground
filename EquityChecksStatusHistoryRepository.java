import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import gov.cia.americano.domesticmodel.equityChecks.EquityChecksModel;
import gov.cia.americano.domesticmodel.equityChecks.EquityChecksStatusHistory;
 @Repository
public class EquityChecksStatusHistoryRepository {
     @PersistenceContext
    private EntityManager entityManager;
     @Transactional
    public void updateStatusHistory(EquityChecksModel equityCheck, String newStatus) {
        equityCheck.setStatus(newStatus);
         EquityChecksStatusHistory newStatusHistory = new EquityChecksStatusHistory();
        newStatusHistory.setEquityCheck(equityCheck);
        newStatusHistory.setStatus(newStatus);
        newStatusHistory.setTimestamp(new Date());
        entityManager.persist(newStatusHistory);
    }
     public List<EquityChecksStatusHistory> getStatusHistory(Long equityCheckId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EquityChecksStatusHistory> cq = cb.createQuery(EquityChecksStatusHistory.class);
        Root<EquityChecksStatusHistory> root = cq.from(EquityChecksStatusHistory.class);
        cq.select(root).where(cb.equal(root.get("equityCheck").get("id"), equityCheckId));
        TypedQuery<EquityChecksStatusHistory> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
     public void deleteByEquityCheckId(Long equityCheckId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<EquityChecksStatusHistory> delete = cb.createCriteriaDelete(EquityChecksStatusHistory.class);
        Root<EquityChecksStatusHistory> root = delete.from(EquityChecksStatusHistory.class);
        delete.where(cb.equal(root.get("equityCheck"), equityCheckId));
        entityManager.createQuery(delete).executeUpdate();
    }
}