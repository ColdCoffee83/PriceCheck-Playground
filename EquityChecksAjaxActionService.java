import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gov.cia.americano.domestic.service.AmericanoService;
import gov.cia.americano.domestic.service.DomSALService;
import gov.cia.americano.domestic.service.EquityChecksService;
import gov.cia.americano.domesticmodel.ajaxrequest.EquityChecksSearch;
import gov.cia.americano.domesticmodel.ajaxresponse.DomAjaxListTableData;
import gov.cia.americano.domesticmodel.equityChecks.EquityChecksModel;
import gov.cia.americano.domesticview.EquityChecksListView;
import gov.cia.americano.model.RefIdam;
import gov.cia.americano.model.ajaxresponse.AjaxResponse;
import gov.cia.americano.security.LatteUser;
import gov.cia.americano.service.action.BaseActionService;
import gov.cia.americano.utils.CollectionUtils;

@Service
public class EquityChecksAjaxActionService extends BaseActionService {
    private static final Logger log = LogManager.getLogger(EquityChecksAjaxActionService.class);
    private static final String NO_ACCESS = " does not have access to add a handling officer to the resource with resource ID ";
     private EquityChecksService equityChecksService;
    private AmericanoService fieldService;
     @Autowired
    public EquityChecksAjaxActionService(EquityChecksService equityChecksService, AmericanoService fieldService) {
        this.equityChecksService = equityChecksService;
        this.fieldService = fieldService;
    }
     public void addHandlingOfficer(String resourceId, String userId) {
        if (!fieldService.hasAccess(userId, resourceId)) {
            log.error(userId + NO_ACCESS + resourceId);
            throw new AccessDeniedException(userId + NO_ACCESS + resourceId);
        }
        equityChecksService.addHandlingOfficer(resourceId, userId);
    }


public DomAjaxListTableData filter (EquityChecksSearch equityChecksSearch, LatteUser latteUser, Boolean viewingMyRequests) {
    SearchActions salSearch = DomSALService.startSearch (equityChecksSearch.getSearchParams(), "equityCheck", latteUser);
    salSearch.started();
    DomAjaxListTableData equityCheckData = new DomAjaxListTableData();
     List<EquityChecksListView> equityChecks;
    try {
        equityChecks = CollectionUtils.filter(equityChecksService.getFilterEquityChecks (equityChecksSearch, viewingMyRequests, latteUser), null);
        equityCheckData.setData (equityChecks) ;
        equityCheckData.setRecordsTotal (CollectionUtils.size(equityCheckData.getData()));
    } catch (Exception e) {
        e.printStackTrace();
    }
     equityChecksSearch.getSearchParams() ;
    equityCheckData.setRecordsFiltered (equityCheckData.getRecordsTotal());
     equityCheckData.setCanView(latteUser.hasPermission("viewRequirement"));
    equityCheckData.setCanEdit (latteUser.hasPermission("editEquityCheck"));
    equityCheckData.setCanRemove (latteUser.hasPermission("deleteEquityCheck")) ;
    DomSALService.logSearchResults (salSearch, equityCheckData.getData());
    return equityCheckData;
}

public DomAjaxListTableData filterNoAccess (LatteUser latteUser) {
log.error ("User does not have access to view the Equity Check List");
DomSALService.noPermissionsAjaxAction("User does not have access to the Equity Check List", latteUser);
return new DomAjaxListTableData (false);
}

public AjaxResponse setOfficePOCNoAccess (Long id, LatteUser latteUser) {
DomSALService.noPermissionsAjaxAction(latteUser.getDisplayName() + NO_ACCESS
+ id, latteUser);
return new AjaxResponse (false, "No Access to set office POC",
"You do not have access to set the office POC for the equity check.  This action will be logged.",
null);
}

public AjaxResponse setOfficePOC(ReflIdam officePOC, LatteUser latteUser) {
AjaxResponse response = new AjaxResponse();
response.setSuccess (true);
response.setMessage ("Office POC added successfully");
RefIdam savedOfficer = fieldService.saveOfficer (officePOC, latteUser);
if (savedOfficer == null) {
//this means the person is already added to the list
response.setSuccess (false);
response.setMessage (officePOC.getDisplayName() + " is already added as handling officer. Please choose a different person.");
}

response.setObj (savedOfficer);
return response;
}

public AjaxResponse setActionPOCNoAccess (Long equityCheckId, LatteUser
latteUser) {
DomSALService.noPermissionsAjaxAction (latteUser.getDisplayName() + NO_ACCESS
+ equityCheckId, latteUser);
return new AjaxResponse (false, "No Access to change action POC", "You do not have access to change the action POC for the equity check request. This action will be logged.", null);
}

public AjaxResponse setActionPOC(Refldam actionPOC, LatteUser latteUser) {
AjaxResponse response = new AjaxResponse();

response.setSuccess (true);
RefIdam savedActionPOC = fieldService.saveActionPOC(actionPOC, latteUser);

if (savedActionPOC == null) {
//this means the person is already added to the list

response.setSuccess (false);
response.setMessage (actionPOC.getDisplayName() + " is already added as a reporter. Please choose a different person.");
}
response.setObj (savedActionPOC) ;
return response;

}

public AjaxResponse saveCollaborationStakeholders (Long id, List<RefIdam> stakeholders, LatteUser latteUser) {
    AjaxResponse response = new AjaxResponse();
    Optional<EquityChecksModel> op = equityChecksService. findEquityCheckById (id);
    response.setSuccess (true);
    if (op.isEmpty()) {
        response.setSuccess (false);
        response.setMessage ("An error occurred.");
    }
    Set<RefIdam> collaborationStakeholders = fieldService.saveCollaborationStakeholders (stakeholders, latteUser);
    equityChecksService.setCollaborationStakeholders (op.get(), collaborationStakeholders, latteUser);
    return response;
}
 public AjaxResponse sendEmailToCollaborationStakeholders(List<String> stakeholders, Long equityCheckld, LatteUser latteUser) throws AddressException, IOException, MessagingException {
    AjaxResponse response = new AjaxResponse();
    Boolean emailSent = equityChecksService.sendEmailToCollaborationStakeholders (stakeholders, equityCheckId, latteUser) ;
    response.setSuccess (true);
    if (!emailSent) {
        response.setSuccess (false);
        response.setMessage ("Email failed to send.");
    }
    return response;
    }
}