// repository for the RefIdam table
public interface RefIdamRepository extends RevisionedRepository<RefIdam, Long, Integer>, JpaRepository<RefIdam, Long> {
  // query to lookup RefIdam by ain excluding deleted records
  @Query("SELECT r FROM RefIdam r WHERE r.ain IS NOT NULL AND r.deleted = false")
  List<RefIdam> findByAin(String ain);

  // query refIdam by emailGroupName excluding deleted records
  @Query("SELECT r FROM RefIdam r WHERE r.emailGroupName IS NOT NULL AND r.deleted = false")
  List<RefIdam> findByEmailGroupName(String emailGroupName);

  // query refIdam by id excluding deleted records
  @Query("SELECT r FROM RefIdam r WHERE r.id IS NOT NULL AND r.
  deleted = false")
  RefIdam findById(Long id);
  
  // query refidam where emailGroupName is in a list of groups excluding deleted records
  @Query("SELECT r FROM RefIdam r WHERE r.emailGroupName IN :emailGroupNames AND r.deleted = false")  
  List<RefIdam> findByEmailGroupNameIn(@Param("emailGroupNames") List<String>
                                       emailGroupNames);
  
  // query refidam joining with EquityChecksModel on RefIdam.id = EquityChecksModel.actionPOC sorting by refIdam.name exclduing deleted records
  @Query("SELECT r FROM RefIdam r JOIN EquityChecksModel e ON r.id = e.actionPOC WHERE r.deleted = false ORDER BY r.name")
  List<RefIdam> getAllActionPOCsSorted();

  // same for EquityChecksModel.officePOCs
  @Query("SELECT r FROM RefIdam r JOIN EquityChecksModel e ON r.id = e.officePOC WHERE r.deleted = false ORDER BY r.name")
  List<RefIdam> getAllOfficePOCsSorted();

  Optional<RefIdam> findById(long Id);
  

