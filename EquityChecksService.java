// Import necessary packages
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Zoneld;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gov.cia.americano.domestic.repository.EquityChecksFilterRepository;
import gov.cia.americano.domestic.repository.EquityChecksRepository;
import gov.cia.americano.domestic.repository.EquityChecksStatusHistoryRepository;
import gov.cia.americano.domestic.repository.EquityChecksSearchHelpRepository;

import gov.cia.americano.domestic.service.equityChecks.EquityChecksFieldSaveService;
import gov.cia.americano.domesticmodel.ajaxrequest.EquityChecksSearch;
import gov.cia.americano.domesticmodel.email.EmailNote;
import gov.cia.americano.domesticmodel.equityChecks.EquityChecksModel;
import gov.cia.americano.domesticmodel.equityChecks.EquityChecksSearchHelp;
import gov.cia.americano.domesticmodel.equityChecks.EquityChecksStatusHistory;
import gov.cia.americano.domesticmodel.form.EquityChecksForm;

import gov.cia.americano.domesticview.EquityChecksListView;

import gov.cia.americano.enumeration.SALErrors;

import gov.cia.americano.model.RefIdam;

import gov.cia.americano.repository.RefIdamRepository;

import gov.cia.americano.security.LatteUser;

import gov.cia.americano.service.SALService;

@Service
public class EquityChecksService {
    static final Logger log = LogManager.getLogger(EquityChecksService.class);

    @Value("${APP_BASE_URL}")
    private String appBaseUrl;

    @Autowired
    private EquityChecksRepository equityChecksRepository;

    @Autowired
    private EquityChecksStatusHistoryRepository statusHistoryRepository;

    @Autowired
    private EquityChecksSearchHelpRepository searchHelpRepository;

    @Autowired
    private EquityChecksFilterRepository filterRepo;

    @Autowired
    private EquityChecksFieldSaveService fieldService;

    @Autowired
    private EmailService emailService;

@Autowired
    private RefIdamRepository refldamRepository;
  
    private static final String EQ_PREFIX = "EQUI-";
  
    

    // This method returns a list of EquityChecksListView objects based on the given parameters
    public List<EquityChecksListView> getFilterEquityChecks(EquityChecksSearch search, Boolean viewingMyRequests,
            LatteUser latteUser) throws Exception {
        // Attempt to filter the EquityChecks based on the given parameters
        return attemptFilterEquityChecks(search, viewingMyRequests, latteUser);
    }

    public List<EquityChecksListView> attemptFilterEquityChecks(EquityChecksSearch search, Boolean viewingMyRequests,
            LatteUser latteUser) throws Exception {
        try {
            return attemptFilter(search, viewingMyRequests, latteUser);
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    private List<EquityChecksListView> attemptFilter(EquityChecksSearch search, Boolean viewingMyRequests,
            LatteUser latteUser) {
        return filterRepo.filterEquityChecks(search, viewingMyRequests, latteUser);
    }

    private EquityChecksModel createOrUpdateEquityCheck(EquityChecksForm eqForm, boolean isDraft,
            LatteUser latteUser) throws Exception {
        Optional<EquityChecksModel> dbEquityCheckRec = findEquityCheck(eqForm.getId());
        EquityChecksModel equityCheck;
        if (!dbEquityCheckRec.isPresent()) {
            equityCheck = new EquityChecksModel();
        } else {
            equityCheck = dbEquityCheckRec.get();
        }
        eqForm.saveEquityCheck(equityCheck, latteUser);
        eqForm.setAudit(equityCheck);
        equityCheck.setDraft(isDraft);
        // Here, we call the helper methods to save or update the EquityCheck
        saveEquityChecksSearchHelp(equityCheck, latteUser);
        equityCheck = equityChecksRepository.save(equityCheck);
        createOrUpdateEquityCheckStatusHistory(equityCheck, eqForm.getStatus()); 
    return equityCheck;
}
   
    public Optional<EquityChecksModel> validateAndSaveEquityCheck (EquityChecksForm eqForm, List<String> errors, LatteUser latteUser, boolean isDraft) {
        try {
            return Optional.of(createOrUpdateEquityCheck(egForm, latteUser, isDraft));
        } catch (IOException ex) {
            errors.add(SALErrors.INTERNAL_SAVE_ERROR.getValue());
            DomSALService.saveltemFailed(eqgForm,errors.get(0),latteUser);
            return Optional.empty();
        } catch (SQLException ex) {
            errors.add(SALErrors.INTERNAL_SAVE_ERROR.getValue());
            DomSALService.saveltemFailed(eqgForm,errors.get(0),latteUser);
            return Optional.empty();
        } finally {
        // Close any resources used in the try block
        }
    }

    private EquityChecksModel saveEquityCheck (EquityChecksForm egForm, LatteUser latteUser, boolean isDraft) throws Exception {
        Optional<EquityChecksModel> dbEquityCheckRec = findEquityCheckById(eqForm.getId());
        EquityChecksModel equityCheck;
        if (!dbEquityCheckRec.isPresent()) {
            equityCheck = new EquityChecksModel();
            egForm.saveEquityCheck(equityCheck, latteUser);
            egForm.setAudit(equityCheck);
            equityCheck = equityChecksRepository.save(equityCheck);
        } else {
            equityCheck = dbEquityCheckRec.get();
        }
        egForm.saveEquityCheck(equityCheck, latteUser);
        eqForm.setAudit(equityCheck);
        equityCheck = equityChecksRepository.save(equityCheck);
        EquityChecksModel updated = insertOrUpdateEquityCheck(equityCheck, latteUser, isDraft);
        DomSALService.saveltem(updated, isDraft, latteUser);
        egForm.setId(updated.getlId());
        return equityCheck;
    }

// This method saves or updates an EquityChecksSearchHelp for the given EquityChecksModel
private void saveEquityChecksSearchHelp(EquityChecksModel equityCheck, LatteUser latteUser) throws Exception {
    // If the equity check already has a search help, update it
    if (equityCheck.getEquityChecksSearchHelp() == null) {
        // If the equity check does not have a search help, create a new one and save it
        EquityChecksSearchHelp ecsh = new EquityChecksSearchHelp();
        ecsh.setCreatedInfo(latteUser);
        equityCheck.setEquityChecksSearchHelp(ecsh);
    }
}

// This method creates or updates an equity check status history entry for the given equity check and new status
private void createOrUpdateEquityCheckStatusHistory(EquityChecksModel equityCheck, String newStatus) {
    // Get the latest equity check status history for this equity check
    Optional<EquityChecksStatusHistory> latestHistoryOptional = statusHistoryRepository.findFirstByEquityCheckOrderByTimestampDesc(equityCheck);

    // If there is no latest history, create a new one for the new status
    if (!latestHistoryOptional.isPresent()) {
        EquityChecksStatusHistory newHistory = new EquityChecksStatusHistory();
        newHistory.setStatus(newStatus);
        newHistory.setTimestamp (new Date ());
        newHistory.setEquityCheck(equityCheck);
        statusHistoryRepository.save(newHistory);
    } else {
        EquityChecksStatusHistory latestHistory = latestHistoryOptional.get();
        String oldStatus = latestHistory.getStatus();
        if (!newStatus.equals(oldStatus)) {
            // If the old status is not equal to the new status, create a new history entry for the new status
            EquityChecksStatusHistory newHistory = new EquityChecksStatusHistory();
            newHistory.setStatus(newStatus);
            newHistory.setTimestamp(new Date());
            newHistory.setEquityCheck(equityCheck);
            statusHistoryRepository.save(newHistory);
        } else {
            // If the old status is equal to the new status, update the status date of the latest history entry
            latestHistory.setTimestamp(new Date());
            statusHistoryRepository.save(latestHistory);
        }
    }
}
   
    public Optional<EquityChecksModel> findEquityCheckById (Long id) {
        return findEquityCheck (id, false);
    }
    public Optional<EquityChecksModel> findEquityCheckIncludingDeleted (Long id) {
        return findEquityCheck (id, true);
    }

    private Optional<EquityChecksModel> findEquityCheck(Long id, boolean includeDeleted) {
    Optional<EquityChecksModel> equityCheck;
    try {
        equityCheck = equityChecksRepository.findById(id);
    } catch (Exception ex) {
        ex.printStackTrace();
        log.error(ex);
        return Optional.empty();
    }
    
    if (!equityCheck.isPresent()) {
        String error = "There was an error retrieving an equity check request: " + "The equity check with id '" + id + "' does not exist.";
        log.error(error);
        return Optional.empty();
    }
    
    if (!includeDeleted && equityCheck.get().getDeleted()) {
        String error = "There was an error retrieving an equity check request: " + "The equity check with id '" + id + "' was deleted.";
        log.error(error);
        return Optional.empty();
    }
    
    return equityCheck;
}

    public EquityChecksModel getNewestRequest() {
        return equityChecksRepository.getNewestEquityCheck();
    }

    @Transactional
    public EquityChecksModel createNewRequest(LatteUser latteUser) {       
        EquityChecksModel saved = savedInit(latteUser);    
        saved = insertOrUpdateEquityCheck(saved, latteUser, false);
        EquityChecksStatusHistory newstatus = new EquityChecksStatusHistoryl);
newStatus.setStatus ("Draft");
newStatus.setEquityCheck (saved);
newStatus.setTimestamp(new Date());
statusHistoryRepository.save(newStatus);

        saved.setEquiNumber(EQ_PREFIX + saved.getId());
        return setSavedEquityCheckNumbers(saved);
    }

    public EquityChecksModel savedInit(LatteUser latteUser) {
        EquityChecksModel saved = new EquityChecksModel ();
        EquityChecksSearchHelp ecsh = new EquityChecksSearchHelp();
        ecsh.setCreatedInfo(latteUser);
        saved.setEquityChecksSearchHelp(ecsh);
        RefIdam currentUser = fieldService.saveOfficer(new RefIdam(latteUser), latteUser);
        saved.setOfficePOC(currentUser);
        return saved;
    }

    public EquityChecksModel insertOrUpdateEquityCheck (EquityChecksMode1 equityCheck, LatteUser latteUser, boolean isDeleted) {
        if (equityCheck.getInsertUserName() == null) { 
            equityCheck.setCreatedInfo(latteUser);
        } else {
            equityCheck.setModifiedInfo(latteUser, isDeleted);
        }
        return equityChecksRepository.save (equityCheck);
    }

    public EquityChecksModel setSavedEquityCheckNumbers(EquityChecksModel saved) {
        // TODO: i "think" this should be enough, but we may need to revisit this
        saved.setEquiNumber(EQ_PREFIX + saved.getId());
        return saved;
    }
    
    public void statusUpdate (EquityChecksModel equityCheck, LatteUser latteUser, Boolean isDraft) {
        Boolean draftCheck = isDraft != null && isDraft;
        if (draftCheck) {
            updateStatusAndSave(equityCheck, latteUser, "Draft");
        } else {
            String newStatus = determineNewStatus(equityCheck);
            if (newStatus != null) {
                updateStatusAndNotifyRequester(equityCheck, latteUser, newStatus);
            }
        }
    }

    private String determineNewStatus (EquityChecksModel equityCheck) {
        String currentStatus = equityCheck.getStatus();
        if (currentStatus == null)
            return "Submitted";
        switch (currentStatus) {
            case "Draft":
                return "Submitted";
            case "Submitted":
                return equityCheck.getActionPOC() != null ? "Received" : null;
            case "Received":
                return "Received";
            case "Processing":
            case "In Coordination":
                return null;
            case "Completed":
                return "Completed";
            default:
                return null;
        }
    }


    public void updateStatusAndSave (EquityChecksModel equityCheck, LatteUser latteUser, String newStatus) {
        equityCheck.setStatus(newStatus);
        insertOrUpdateEquityCheck(equityCheck, latteUser, false);
    }
    
    public void updateStatusAndNotifyRequester (EquityChecksModel equityCheck, LatteUser latteUser, String status) {
        equityCheck.setStatus(status);
        statusHistoryRepository.updateStatusHistory(equityCheck, status);
        try {
if (isNotificationRequired(equitycheck,status))(
sendNotificationBasedOnStatus(equityCheck, latteÜser, status);
}
) catch (IOException | MessagingException e) I
log.error ("Failed attempt to notify requester of received requested"); log.error (e.getMessage ()); e.printStackTrace () ;
EquityChecksStatusHistory newStatus = new EquityChecksStatusHistory ();
newStatus.setStatus (status);
newStatus.setTimestamp (new Date ()); newStatus. setEquityCheck (equityCheck); statusHistoryRepository.save (newStatus);
if (isNotificationRequired(equityCheck, status)) |
sendNotificationBasedOnStatus(equityCheck,latteUser,status);

    }

    private boolean isNotificationRequired(EquityChecksModel equityCheck, String status) {
        switch (status) {
        case "Submitted":
        return equityCheck.getNotifiedRequesterOfSubmitted() == null
        || !equityCheck.getNotifiedRequesterOfSubmitted();
        case "Received":
        return equityCheck.getNotifiedRequesterOfReceived() == null
        || !equityCheck.getNotifiedRequesterOfReceived();
        case "Completed":
        return equityCheck.getNotifiedRequesterOfCompleted() == null
        || !equityCheck.getNotifiedRequesterOfCompleted();
        default:
        return false;
        }
    }

    private void sendNotificationBasedOnStatus (EquityChecksModel equityCheck, LatteUser latteUser, String status) throws IOException, MessagingException {
        switch (status) {
        case "Submitted":
        case "Received":
        case "Completed":
        notifyRequester (equityCheck, latteUser, status);
        break;
        default:
        throw new IllegalArgumentException ("Invalid status provided");
        }
    }

    private void updateNotifiedRequesterFlag(EquityChecksModel equityCheck, String status, boolean b) {
        switch (status) {
        case "Submitted":
        equityCheck.setNotifiedRequesterOfSubmitted(b);
        break;
        case "Received":
        equityCheck.setNotifiedRequesterOfReceived(b);
        break;
        case "Completed":
        equityCheck.setNotifiedRequesterOfCompleted (b);
        break;
        }
    }

    public void notifyRequester (EquityChecksModel equityCheck, LatteUser latteUser,
String status) throws AddressException, IOException, MessagingException {
      boolean shouldNotify = false;
    switch (status) {
        case "Submitted":
            shouldNotify = equityCheck.getNotifiedOfSubmitted() == null || !equityCheck.getNotifiedOfSubmitted();
            break;
        case "Received":
            shouldNotify = equityCheck.getNotifiedOfReceived() == null || !equityCheck.getNotifiedOfReceived();
            break;
        case "Completed":
            shouldNotify = equityCheck.getNotifiedOfCompleted() == null || !equityCheck.getNotifiedOfCompleted();
            break;
        default:
            throw new IllegalArgumentException("Invalid status provided");
    }

      if (shouldNotify) {
        try {
          Optional<RefIdam> person = refIdamRepository.findById(equityCheck.getOfficePOC().getId());
        String ain = person.orElseThrow(() -> new IllegalStateException("Person not found")).getAin();
        String recipient = ain + "@cia.ic.gov";
        String subject;
        String classification;
        String equityCheckId = equityCheck.getId().toString();
        String viewLink = appBaseUrl + "/equity/view/" + equityCheckId;
        String fieldStationPOC; // for completed only
        String htmlBody;
        switch (status) {
            case "Submitted":
                subject = "Your Equtiy Check Request Has Been Submitted";
                classification = "UNCLASSIFIED";
                htmlBody = "div style='font-family: Arial, sans-serif; font-size: 14px; '>" + "h2 style= font-size: 18px; '›REQUEST IS SUBMITTED</h2>"
                            + "<p>The Corporate Vetting Cell (CC) has received your equity check request and will begin working on your request shortly. The normal turnaround time for equity checks is approximately 5 business days. The Task ID for your request is <a href='"
                            + viewLink + "'›" + equityCheckId + "</a>.</p>"
                            + "Sp>If you have any questions please send an email to <a href='mailto:T2MC EQUITY CELL WMA@cia.ic.gov'>T2MC_EQUITY_CEL [</a></p>"
                            + "<p>Thank you, </p>" + "<p>CVC</p>" + "</div>";
                break;
            case "Received":
                subject = "Your Egutiy Check Request Has Been Received";
                classification = "UNCLASSIFIED";
                htmlBody = "‹div style='font-family: Arial, sans-serif; font-size: 14px; '>"
                            + "<h2 style='font-size: 18px; '›REQUEST IS RECEIVED</h2>"
                            + "<p>The Corporate Vetting Cell (CVC) has assigned an action point of contact to your request.  Processing will begin soon. The normal turnaround time for equity checks is approximately 5 business days. The Task ID for your request is <a href='"
                            + viewLink + "'›" + equityCheckId + "</a>.</p>" + "<p>If you have any questions please send an email to <a href='mailto:T2MC EQUITY CELL WMA@cia.ic.gov'>T2MC EQUITY CEL [</a></p>"
                            + "<p>Thank you, </p>" + "‹p›VC</p>" + "</div>";
                break;
            case "Completed":
                subject = "Your Egutiy Check Request Has Been Completed";
                classification = "UNCLASSIFIED";
                // TODO: assigne fieldStationPOC here
                htmlBody = "‹div style='font-family: Arial, sans-serif; font-size: 14px; '>" 
                            + "‹h2 style= font-size: 18px; '>REQUEST IS COMPLETED: </h2>"
                            + "<p>The Corporate Vetting Cell (CVC) has completed your equity check for Request "
                            + equityCheckId + " and you can review the results <a href='"
                            + viewLink + "'›" + "by clicking here‹/a>.‹/p>"
                            + "<p>STATION and OD, please respond to all on this email within five business days if you have concerns about sensitive equities within Request + equityCheckId "
                            + ".</p>" + "‹p>If you have any questions about the results or the equity check process, please send an email to <a href='mailto:T2MC EQUITY CELL WA@cia.ic.gov'>T2MC_EQUITY_CELL</a>.</p>" 
                            + "<p›Thank you, </p>" + "‹p›cvc</p›" + "</div›";
                break;
            default:
                throw new IllegalArgumentException ("Invalid status provided");
        }
        EmailNote emailToSend = new EmailNote(recipient, subject, classification);
        emailToSend.setHtml(htmlBody);
        emailService.sendEmail(emailToSend, latteUser);
          switch (status) {
                case "Submitted":
                    equityCheck.setNotifiedRequesterOfSubmitted(true);
                    break;
                case "Received":
                    equityCheck.setNotifiedRequesterOfReceived(true);
                    break;
                case "Completed":
                    equityCheck.setNotifiedRequesterOfCompleted(true);
                    break;
            }
        } catch (IOException, MessagingException e) {
          log.error("Failed attempt to notify requester");
          log.error(e.getMessage());
        }          
        
    }

    public EquityChecksFilterRepository getFilterRepository() {
        return filterRepo;
    }

    public void internalDueDateCheck (EquityChecksModel equityCheck, LatteUser latteUser) {
        if (equityCheck.getInternalDueDate() == null) {
            int daysToAdd = (equityCheck.getRequestType() == "Executive") ? 5 : 10;
            LocalDate currentDate = LocalDate.now();
            LocalDate internalDueDate = currentDate.plusDays (daysToAdd) ;
            long ms = internalDueDate.atStartOfDay (Zoneld.systemDefault()).toInstant ().toEpochMilli ();
            equityCheck.setInternalDueDate (ms) ;
            insertOrUpdateEquityCheck (equityCheck, latteUser, false);
        }
    }

    public List<EquityChecksStatusHistory> getStatusHistory (Long equityCheckId) {
        return statusHistoryRepository.findAllByEquityCheck_IdOrderByTimestampDesc(equityCheckId);
    }

    public EquityChecksModel getLatestDraftForUser (String currentUser) {
        try {
            PageRequest pageRequest = PageRequest.of (0, 1);
            Page<EquityChecksModel> drafts = equityChecksRepository.getLatestDraftForCurrentUser(currentUser, pageRequest) ;
            return drafts.getContent().get(0);
        } catch (NonUniqueResultException ex) {
            throw ex;
        }
    }

    @Transactional
    public void deleteDraft(Long equityCheckId, Boolean fromDraftList) {
        if (equityChecksRepository.getRemoveableStatus(equityCheckId) >= 1 ||
        fromDraftList) {
            // delete the equity check draft status history
            statusHistoryRepository.deleteByEquityCheck_Id (equityCheckId);
            // delete the search help (wont be needed)
            searchHelpRepository.deleteByEquityCheckId(equityCheckId) ;
            // delete the equity check draft itself
            equityChecksRepository.deleteById(equityCheckId);
        }
    }

    public List<EquityChecksModel> findDraftsByUser (String username) {
        return equityChecksRepository.getDraftsForUser (username);
    }

    public boolean ownsRequest (LatteUser latteUser, Long id) {
        String username = latteUser.getUserId();
        Integer count = equityChecksRepository.checkOwnership(username, id);
        return count == 1;
    }

    public List<String> getRequestingOffices() {
        return filterRepo.getRequestingOffices();    
    }

    public List<String> getOfficePOCs() {
        return filterRepo.getOfficePOCs();
    }

    public List<String> getActionPOCs() {
        return filterRepo.getActionPOCs();
    }

    public List<String> getOrganizationNames() {
        return filterRepo.getOrganizationNames();
    }

    public void setCollaborationStakeholders (EquityChecksModel equityCheck, Set<RefIdam> collaborationStakeholders, LatteUser latteUser) {
        equityCheck.setCollaborationStakeholders (collaborationStakeholders);
        insertOrUpdateEquityCheck (equityCheck, latteUser, false);
    }

    public void setFieldStationPOCs (EquityChecksModel equityCheck, Set<RefIdam> fieldStationPOCs, LatteUser latteUser) {
      equityCheck.setFieldStationPOCs (fieldStationPOCs);
      insertOrUpdateEquityCheck (equityCheck, latteUser, false);
    }

    public void setDeskOfficers (EquityChecksModel equityCheck, Set<RefIdam> deskOfficers, LatteUser latteUser) {
      equityCheck.setDeskOfficers (deskOfficers);
      insertOrUpdateEquityCheck (equityCheck, latteUser, false);
    }

    public List<String> getSelectedCollaborationStakeholders (Long equityCheckId) {
        List<String> stakeholders =
        equityChecksRepository.getSelectedCollaborationStakeholders (equityCheckId);
        return stakeholders;
    }

  public List<String> getSelectedFieldStationPOCs (Long equityCheckId) {
    List<String> pocs =
    equityChecksRepository.getSelectedFieldStationPOCs (equityCheckId);
    return pocs;
  }

  public List<String> getSelectedDeskOfficers (Long equityCheckId) {
    List<String> officers =
    equityChecksRepository.getSelectedDeskOfficers (equityCheckId);
    return officers;
  }
  
    public Boolean sendEmailToCollaborationStakeholders (List<String> stakeholders, Long equityCheckId,
        LatteUser latteUser) throws AddressException, IOException, MessagingException {
        List<String> emailAddresses = stakeholders.stream().map(stakeholder -> stakeholder + "@cia.ic.gov").collect(Collectors.toList());

        String to = String.join(",", emailAddresses);
        String subject = "Requesting Collaboration for Equity Check";
        String classification = "UNCLASSIFIED//AIUO";
        String collaborationURL = appBaseUrl + "/equity/view/" + equityCheckId;
        EmailNote emailToSend = new EmailNote (to, subject, classification);
        String htmlBody = "<body style='font-family: Arial, sans-serlf; font-size: 14px; '><div style='padding: 20px; background-color: #F5F5F5;'>"
            + "<hl style='font-size: px;'>Equity Check Collaboration</h1>"
            + "<p>The Corporate Vetting Cell (CVC) is requesting your  collaboration with the equity check linked below. Please review the details within. If you have any questions about equity check process, please send an email to T2MC_EQUITY CELL. Thank you.<br><br>CVC</p>"
            + "<p><a href='" + collaborationURL + "'>" + collaborationURL + "</a></p>" + "</div>";
           
        emailToSend.setHtml (htmlBody);

        try {
            emailService.sendEmail (emailToSend, latteUser);
            EquityChecksModel equityCheck =
            equityChecksRepository.getById(equityCheckId);
            equityCheck.setSentCollaborationEmail (true);
            insertOrUpdateEquityCheck (equityCheck, latteUser, false);
            return true;
        } catch (Exception e) {
            log.error ("Failed to send email to collaboration stakeholders for Equity Check Id: {}", equityCheckId, e);
            return false;
        }
    }

    public Boolean getCollaborationEmailStatus (Long equityCheckId) {
    EquityChecksModel equityCheck = equityChecksRepository.getById(equityCheckId);
    return equityCheck.getSentCollaborationEmail();
    }

    @Transactional
    public EquityChecksModel createEquityCheck(EquityChecksForm eqForm, LatteUsecr latteUser, boolean isDraft) {
        EquityChecksModel equityCheck = new EquityChecksModel();
        eqForm.saveEquityCheck(equityCheck, latteUser);
        eqForm.setAudit(equityCheck);
        equityCheck = equityChecksRepository.save(equityCheck);        
        EquityChecksStatusHistory statusHistory = new EquityChecksStatusHistory ();
statusHistory. setEquityCheck (equityCheck);
statusHistory. setStatus (isDraft ? "Draft" : "Submitted"); statusHistory.setTimestamp(new Date ());
statusHistoryRepository.save (statusHistory);

        return equityCheck;
    }

    @Transactional
    public EquityChecksModel updateEquityCheck (EquityChecksForm eqForm, LatteUser latteUser, boolean isDraft) {
        Optional<EquityChecksModel> dbEquityCheckRec = equityChecksRepository.findById(eqForm.getId());
        if (!dbEquityCheckRec.isPresent()) {
            throw new EntityNotFoundException ("Equity check not found");
        }
        EquityChecksModel equityCheck = dbEquityCheckRec.get();
        eqForm.saveEquityCheck (equityCheck, latteUser);
        eqForm.setAudit (equityCheck) ;
        equityCheck = equityChecksRepository.save (equityCheck);
        EquityChecksStatusHistory statusHittory= new EquityChecksStatusHistory);
statusHistory.setEquityCheck (equitycheck);
statusHistory.setStatus (isDraft ? "Draft" : "Submitted"); statusHistory.setTimestamp (new Date ());
statusHistoryRepository.save (statusHistory);

        return equityCheck;
    }
}