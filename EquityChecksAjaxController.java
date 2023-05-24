import java.io.IOException;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
 import gov.cia.americano.domestic.service.EquityChecksService;
import gov.cia.americano.domestic.service.action.EquityChecksAjaxActionService;
import gov.cia.americano.domestic.service.useraccess.DomUserAccessService;
import gov.cia.americano.domesticmodel.ajaxrequest.EquityChecksSearch;
import gov.cia.americano.domesticmodel.ajaxresponse.DomAjaxListTableData;
import gov.cia.americano.domesticmodel.equityChecks.EquityChecksStatusHistory;
import gov.cia.americano.model.RefIdam;
import gov.cia.americano.model.ajaxresponse.AjaxResponse;
import gov.cia.americano.security.LatteUser;


@RestController
@RequestMapping("/ajax/americano/equityCheck")
public class EquityChecksAjaxController extends DomBaseController {
   private EquityChecksAjaxActionService actionService;
  private EquityChecksService equityChecksService;
   @Autowired
  public EquityChecksAjaxController(EquityChecksAjaxActionService actionService,
    EquityChecksService equityChecksService) {
    this.actionService = actionService;
    this.equityChecksService = equityChecksService;
  }
   @RequestMapping(value = "/load", method = RequestMethod.POST)
  @ResponseBody
  public DomAjaxListTableData load(HttpServletRequest request, EquityChecksSearch equityChecksSearch) {
    LatteUser latteUser = getLatteUser(request);
    if (!domUserAccessService.hasAnyRole(latteUser)) {
      return actionService.filterNoAccess(latteUser);
    }
    return actionService.filter(equityChecksSearch, latteUser, false);
  }
   @RequestMapping(value = "/loadMyRequests", method = RequestMethod.POST)
  @ResponseBody
  public DomAjaxListTableData loadMyRequests(HttpServletRequest request, EquityChecksSearch equityChecksSearch) {
    LatteUser latteUser = getLatteUser(request);
     if (!domUserAccessService.hasAnyRole(latteUser)) {
      return actionService.filterNoAccess(latteUser);
    }
    return actionService.filter(equityChecksSearch, latteUser, true);
  }
   @PostMapping(value = "/setOfficePOC/{equityCheckId}")
  public AjaxResponse setOfficePOC(HttpServletRequest request, Refldam officer,
    @PathVariable Long equityCheckId) {
    LatteUser latteUser = getLatteUser(request);
     if (!domUserAccessService.canCreateEquityCheck(latteUser)) {
      return actionService.setOfficePOCNoAccess(equityCheckId, latteUser);
    }
     return actionService.setOfficePOC(officer, latteUser);
  }
   @PostMapping(value = "/setActionPOC/{equityCheckId}")
  public AjaxResponse setActionPOC(HttpServletRequest request, Refldam officer,
    @PathVariable Long equityCheckId) {
    LatteUser latteUser = getLatteUser(request);
     if (!domUserAccessService.canEditEquityCheck(latteUser)) {
      return actionService.setActionPOCNoAccess(equityCheckId, latteUser);
    }
     return actionService.setActionPOC(officer, latteUser);
  }
   @GetMapping("/statusHistory")
  public List<EquityChecksStatusHistory> getStatusHistory(@RequestParam Long equityCheckId) {
    List<EquityChecksStatusHistory> statusHistory = equityChecksService.getStatusHistory(equityCheckId);
    return statusHistory;
  }
   @PostMapping("/saveCollaborationStakeholders/{equityCheckId}")
  public AjaxResponse saveCollaborationStakeholders(HttpServletRequest request,
    @PathVariable Long equityCheckId, @RequestBody List<RefIdam> stakeholders) {
    LatteUser latteUser = getLatteUser(request);
    return actionService.saveCollaborationStakeholders(equityCheckId, stakeholders, latteUser);
  }
   @GetMapping("/getSelectedCollaborationStakeholders")
  public List<String> getSelectedCollaborationStakeholders(HttpServletRequest request,
    @RequestParam Long equityCheckId) {
    List<String> selectedCollaborationStakeholders = equityChecksService.getSelectedCollaborationStakeholders(equityCheckId);
    return selectedCollaborationStakeholders;
  }
  // get FieldStationPOCs
  @GetMapping("/getFieldStationPOCs")
  public List<String> getFieldStationPOCs(@RequestParam Long equityCheckId) {
    List<String> fieldStationPOCs = equityChecksService.getFieldStationPOCs(equityCheckId);
    return fieldStationPOCs;
  }
  // get DeskOfficers
  @GetMapping("/getDeskOfficers")
  public List<String> getDeskOfficers(@RequestParam Long equityCheckId) {
    List<String> deskOfficers = equityChecksService.getDeskOfficers(equityCheckId);
    return deskOfficers;
  }
  
   @PostMapping("/sendEmailToCollaborationStakeholders/{equityCheckId}")
  public AjaxResponse sendEmailToCollaborationStakeholders(HttpServletRequest request,
    @PathVariable Long equityCheckId, @RequestBody List<String> stakeholders) throws AddressException, IOException, MessagingException {
    LatteUser latteUser = getLatteUser(request);
    return actionService.sendEmailToCollaborationStakeholders(stakeholders, equityChecklId, latteUser);
  }
   @GetMapping("/getCollaborationEmailStatus/{equityCheckId}")
  public Boolean getCollaborationEmailStatus(HttpServletRequest request,
    @PathVariable Long equityCheckId) {
    return equityChecksService.getCollaborationEmailStatus(equityCheckId);
  }
   @Autowired
  public void setDomUserAccessService(DomUserAccessService accessService) {
    this.domUserAccessService = accessService;
  }
   @Autowired
  public void setEquityChecksAjaxActionService(EquityChecksAjaxActionService actionService) {
    this.actionService = actionService;
  }
}
