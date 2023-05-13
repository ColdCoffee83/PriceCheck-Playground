import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import gov.cia.americano.domesticmodel.ajaxrequest.EquityChecksSearch;
import gov.cia.americano.domesticview.EquityChecksListView;
import gov.cia.americano.security.LatteUser;
import gov.cia.americano.utils.CollectionUtils;
import gov.cia.americano.utils.StringUtils;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
 @Repository
public class EquityChecksFilterRepository extends BaseDomesticFilterRepo {
 public List<EquityChecksListView> filterEquityChecks (EquityChecksSearch search,
  Boolean viewingMyRequests, LatteUser latteUser) {
  Session session = getSession();
  CriteriaBuilder cb = session.getCriteriaBuilder();
  CriteriaQuery<EquityChecksListView> cq = cb.createQuery(EquityChecksListView.class);
  Root<EquityChecksListView> ecClass = cq.from(EquityChecksListView.class);
   List<Predicate> predicates = new ArrayList<Predicate>();
  Predicate hasEquityNumber = cb.isNotNull (ecClass.get ("equiNumber"));
  Predicate isNotDeleted = cb.isFalse(ecClass.get ("isDeleted"));
   predicates.add (hasEquityNumber) ;
  predicates.add(isNotDeleted);
   String equiNumber = search.getEquiNumber ();
   if (StringUtils.isPopulated(equiNumber)) {
   Predicate equiNumberWithPrefix = cb.like(ecClass.get ("equiNumber"), "EQUI-"+search.getEquiNumber () + "%");
   Predicate equiNumberWithoutPrefix = cb.like(ecClass.get ("equiNumber"), "§" + search.getEquiNumber() + "%");
    Predicate equiNumberPredicate = cb.or (equiNumberWithPrefix, equiNumberWithoutPrefix);
    predicates.add (equiNumberPredicate) ;
  }
  if (StringUtils.isPopulated(search.getActionPOC())) {
   predicates.add(cb.equal (ecClass.get ("actionPOC"), search.getActionPOC()));
  }
  if (search.getCoordinationDueDate() != null) {
   predicates.add(cb.equal (ecClass.get ("coordinationDueDate"), search.getCoordinationDueDate()));
  }
  if (search.getFieldStation() != null && !search.getFieldStation().isEmpty()) {
   List<String> fieldStationList = Arrays.asList (search.getFieldStation().split(","));
   if (!fieldStationList.isEmpty()) {
    Expression<String> fieldStationField = ecClass.get ("fieldStationList");
     List<Predicate> fieldStationPredicates = new ArrayList<>();
    for (String fieldStation : fieldStationList) {
     fieldStationPredicates.add(cb.like (cb.lower (fieldStationField), "gm" + fieldStation.toLowerCase().trim() + "%"));
    }
    predicates.add(cb.or (fieldStationPredicates.toArray (new Predicate[predicates.size()])));
   }
  }
  if (StringUtils.isPopulated(search.getOfficePOC())) {
   predicates.add(cb.equal (ecClass.get ("officePOC"), search.getOfficePOC()));
  }
  if (StringUtils.isPopulated(search.getRequestingOffice())) {
   predicates.add(cb.equal (ecClass.get ("requestingOffice"), search.getRequestingOffice()));
  }
  if (StringUtils.isPopulated(search.getOrganizationName())) {
   predicates.add(cb.equal (ecClass.get ("organizationName"), search.getOrganizationName()));
  }
  if (StringUtils.isPopulated(search.getStatus())) {
   predicates.add(cb.equal (ecClass.get ("status"), search.getStatus()));
  }
  if (viewingMyRequests) {
   predicates.add(cb.equal (ecClass.get ("insertUserName"), latteUser.getUserId()));
  }
   Predicate finalQuery = cb.and(predicates.toArray (new Predicate [predicates.size()]));
  cq.where (finalQuery);
   TypedQuery<EquityChecksListView> q = session.createQuery(cq);
  List<EquityChecksListView> equityChecks = g.getResultList();
   session.close();
   if (CollectionUtils.isEmpty(equityChecks)) {
   return new ArrayList<>();
  }
  return equityChecks;
 }
  public List<String> getRequestingOffices() {
  Session session = getSession();
  CriteriaBuilder cb = session.getCriteriaBuilder();
  CriteriaQuery<String> cq = cb.createQuery(String.class);
  Root<EquityChecksListView> ecClass = cq.from(EquityChecksListView.class);
   cq.select (ecClass.get ("requestingOffice")).distinct (true);
  List <String> requestingOffices = session.createQuery(cq).getResultList();
  session.close();
  return requestingOffices;
 }
  public List<String> getOfficePOCs() {
  Session session = getSession();
  CriteriaBuilder cb = session.getCriteriaBuilder();
  CriteriaQuery<String> cq = cb.createQuery(String.class);
  Root<EquityChecksListView> ecClass = cq.from(EquityChecksListView.class);
   cq.select (ecClass.get ("officePOC")).distinct (true);
  List <String> officePOCs = session.createQuery(cq).getResultList();
  session.close();
  return officePOCs;
 }
 public List<String> getOrganizationNames() {
    Session session = getSession();
    CriteriaBuilder cb = session.getCriteriaBuilder();
    riteriaQuery<String> cq = cb.createQuery(String.class);
    Root<EquityChecksListView> ecClass = cq.from(EquityChecksListView.class);   
    cq.select (ecClass.get ("organizationName")) .distinct (true);    
    List <String> organizationNames = session.createQuery(cq).getResultList();
    session.close();    
    return organizationNames;
 }
}