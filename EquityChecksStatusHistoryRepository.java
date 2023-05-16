import java.util.Optional;
import gov.cia.americano. domesticmodel.equityChecks. EquityChecksModel;
import gov.cia.americano. domesticmodel.equitychecks.EquityChecksStatusHistory;
*import org.springframework.data.repository.history. RevisionRepository;

public interface EquityChecksStatusHistoryRepository extends JpaRepository<EquityChecksStatusHistory, Integer>, RevisionRepository<EquityChecksStatusHistory, Integer, Integer> {
    Optional<EquityChecksStatusHistory> findFirstByEquityCheckOrderByTimestampDesc(EquityChecksModel equityChecksModel);
  
List<EquityChecksStatusHistory>
findAllByEquityCheck IdOrderByTimestampDesc(Long equityCheckId);

  
  void deleteByEquityCheck id(Long equityCheckId);

}