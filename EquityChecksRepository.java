import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import gov.cia.americano.domesticmodel.equityChecks.EquityChecksModel;
 @Repository
public interface EquityChecksRepository extends RevisionRepository<EquityChecksModel, Long, Integer>, JpaRepository<EquityChecksModel, Long> {
     @Query("select r from EquityChecksModel r where r.id = :id and r.isDeleted = false")
    EquityChecksModel getEquityChegkById(@Param("id") Long id);
     @Query(value = "SELECT * FROM equity checks order by id DESC limit 1", nativeQuery = true)
    EquityChecksModel getNewestEquityCheck();
     @Query("select r from EquityChecksModel r where r.status = 'Draft' and r.editUserName = :currentUser order by r.id desc")
    Page<EquityChecksModel> getLatestDraftForCurrentUser(@Param("currentUser") String currentUser, Pageable pageable);
     @Query("select r from EquityChecksModel r where r.status = 'Draft' and r.editUserName = :currentUser")
    List<EquityChecksModel> getDraftsForUser(@Param("currentUser") String username);
     @Query("select count (r) from EquityChecksModel r where r.id = :id and r.editTimestamp = r.insertTimestamp")
    Integer getRemoveableStatus(@Param("id") Long id);
     @Query("select count (r) from EquityChecksModel r where r.id = :id and r.insertUserName = :username")
    Integer checkOwnership(@Param("username") String username, @Param("id") Long id);
     @Query(value = "select r.ain, r.display name, r.organization from ref idam r " +
            "JOIN join_equity_checks_to_collaboration_stakeholder a " +
            "ON r.id = j.CHILD_ID " +
            "WHERE j.PARENT ID = :parent_id", nativeQuery = true)
    List<String> getSelectedCollaborationStakeholders(@Param("parent id") Long equityCheckId);
  // query for getting selected fieldStationPOCs
     @Query(value = "select r.ain, r.display name, r.organization from ref idam r " +
            "JOIN join_equity_checks_to_field_station_POC a " +
            "ON r.id = j.CHILD_ID " +
            "WHERE j.PARENT ID = :parent_id", nativeQuery = true)
  List<String> getSelectedFieldStationPOCs(@Param("parent id") Long equityCheckId);
  // query for getting selected deskOfficers
  @Query(value = "select r.ain, r.display name, r.organization from ref idam r " +
            "JOIN join_equity_checks_to_desk_officer a " +
            "ON r.id = j.CHILD_ID " +
            "WHERE j.PARENT ID = :parent_id", nativeQuery = true)
  List<String> getSelectedDeskOfficers(@Param("parent id") Long equityCheckId);
}