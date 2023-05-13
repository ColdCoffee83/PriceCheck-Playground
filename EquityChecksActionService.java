// Importing necessary packages 
import java.util.ArrayList; 
import java.util.List; 
import java.util.Optional; 
import java.util.stream.Collectors; 
import javax.persistence.NonUniqueResultException; 
import javax.servlet.http.HttpServletRequest; 
import javax.transaction.Transactional; 
import org.apache.logging.log4j.LogManager; 
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service; 
import org.springframework.web.servlet.mvc.support.RedirectAttributes; 
import gov.cia.americano.domestic.service.DomRefDataService; 
import gov.cia.americano.domestic.service.DomSALService; 
import gov.cia.americano.domestic.service.EquityChecksService; 
import gov.cia.americano.domesticmodel.ajaxresponse.DomAjaxResponse; 
import gov.cia.americano.domesticmodel.ajaxresponse.DomNotificationResponse; 
import gov.cia.americano.domesticmodel.equityChecks.EquityChecksModel; 
import gov.cia.americano.domesticmodel.form.EquityChecksForm; 
import gov.cia.americano.enumeration.EnumRefDataType; 
import gov.cia.americano.enumeration.PageName; 
import gov.cia.americano.enumeration.SALErrors; 
import gov.cia.americano.security.LatteUser; 
import gov.cia.americano.service.RefDataService; 
import gov.cia.americano.service.action.BaseActionService; 


// Service class for Equity Check Action
public class EquityCheckActionService extends BaseActionService {
     // Logger instance
    private static final Logger log = LogManager.getLogger(EquityCheckActionService.class);
     // Page constants
    private static final String PAGE_INDEX = "americano/pages/index";
    private static final String PAGE_LIST = "americano/pages/equityCheck/equityCheckList";
    private static final String PAGE_MY_REQUESTS = "americano/pages/equityCheck/equityCheckMyRequests";
    private static final String PAGE_CREATE = "americano/pages/equityCheck/equityCheckCreate";
    private static final String PAGE_VIEW = "americano/pages/equityCheck/equityCheckView";
     // Attribute constants
    private static final String ATTR_EQUITY_CHECK_FORM = "equityCheck";
    private static final String ATTR_EQUITY_CHECK_OPERATION = "equityCheckOperation";
    private static final String NOTIFICATION = "notification";
    private static final String NOT_AUTHORIZED = "/domesticNotAuthorized.html";
    private static final String ATTR_CAN_VIEW_INTERNAL_DUE_DATE = "canViewEquityCheckInternal DueDate";
    private static final String ATTR_CAN_EDIT = "canEditEquityCheck";
    private static final String ATTR_CAN_VIEW = "canViewEquityCheck";
    private static final String ATTR_CAN_CREATE = "canCreate EquityCheck";
    private static final String ATTR_CAN_DELETE = "canDeleteEquityCheck";
     // Services
    private EquityChecksService equityCheckService;
    private DomRefDataService domRefDataService;
    private EquityCheckRefAndTagService refAndTagService;
     /**
     * Constructor
     * @param equityCheckService
     * @param domRefDataService
     * @param refAndTagService
     */
    public EquityCheckActionService(EquityChecksService equityCheckService, DomRefDataService domRefDataService, EquityCheckRefAndTagService refAndTagService) {
        this.equityCheckService = equityCheckService;
        this.domRefDataService = domRefDataService;
        this.refAndTagService = refAndTagService;
    }
    @Override 
    protected void setPageNotification(HttpServletRequest request, boolean isSuccess, String title, String message) {
        // Set the notification attribute on the request object
        request.setAttribute(NOTIFICATION, new DomAjaxResponse(isSuccess, title, message, null));
    }
    @Override
    protected void setPageNotification(HttpServletRequest request, String title, String message, List<String> errors) {
        // Set the notification attribute on the request object
        request.setAttribute(NOTIFICATION, new DomAjaxResponse(false, title, message, errors));
    }
    @Override 
    protected void setPageNotification (RedirectAttributes request, boolean isSuccess, String title, String message) { 
        request.addFlashAttribute(NOTIFICATION, new DomNotificationResponse (isSuccess, title, message)); 
    }

    @Autowired 
    public EquityCheckActionService (EquityChecksService equityChecksService, DomRefDataService domRefDataService, 
    RefDataService refDataService, EquityCheckRefAndTagService refAndTagService) { 
        this.equityCheckService = equityChecksService; 
        this.domRefDataService = domRefDataService; 
        this.refAndTagService = refAndTagService; 
    } 

    public EquityCheckActionService () { } 

    @SuppressWarnings({ "unused" }) 
    public String index (HttpServletRequest request, LatteUser latteUser) { 
        return PAGE_INDEX; 
    } 
    
    @SuppressWarnings({ "unused" }) 
    public String noAccess (HttpServletRequest request, LatteUser latteUser) { 
        return NOT_AUTHORIZED; 
    }

    public String listPage(HttpServletRequest request, LatteUser latteUser) { 
        setPermissionAttributes(request, latteUser); 
        addReferenceDataTables(request); 
        searchTagsAndSetTagAttributes(request); 
        return PAGE_LIST; 
    }

    public String myRequestsPage(HttpServletRequest request, LatteUser latteUser) { 
        setPermissionAttributes(request, latteUser); 
        addReferenceDataTables(request); 
        searchTagsAndSetTagAttributes(request); 
        return PAGE_MY_REQUESTS; 
    } 

    public EquityChecksModel getLatestDraftForUser(String currentUser) { 
        try { 
            return equityCheckService.getLatestDraftForUser(currentUser); 
        } catch (NonUniqueResultException ex) { 
            throw new RuntimeException("Error retrieving latest draft for user: " + currentUser, ex); 
        } 
    }

    // Method to search for tags and set tag attributes
    public void searchTagsAndSetTagAttributes(HttpServletRequest request) {
    // TODO: Add code here to search for tags and set tag attributes
    }
    public void setPermissionAttributes (HttpServletRequest request, LatteUser latteUser) { 
        final String ATTR_CAN_VIEW_INTERNAL_DUE_DATE = "canViewInternalDueDate";
        final String ATTR_CAN_EDIT = "canEdit";
        final String ATTR_CAN_VIEW = "canView";
        final String ATTR_CAN_CREATE = "canCreate";
        final String ATTR_CAN_DELETE = "canDelete";
        request.setAttribute(ATTR_CAN_VIEW_INTERNAL_DUE_DATE, latteUser.canViewEquityCheckInternalDueDate());
        request.setAttribute(ATTR_CAN_EDIT, latteUser.canEditEquityCheck());
        request.setAttribute(ATTR_CAN_VIEW, latteUser.canViewEquityCheck());
        request.setAttribute(ATTR_CAN_CREATE, latteUser.canCreateEquityCheck());
        request.setAttribute(ATTR_CAN_DELETE, latteUser.canDeleteEquityCheck());
    }

    public EquityChecksModel saveAsDraft (HttpServletRequest request, EquityChecksForm equityChecksForm, LatteUser latteUser) { 
        // Create an EquityCheck object from the EquityChecks Form
        EquityChecksModel equityCheck = equityCheckService.createEquityCheck(equityChecksForm, latteUser, true); 
        // Save the EquityCheck object    
        // Log any errors that occur
        try { 
            return equityCheckService.saveEquityCheck(equityCheck, true); 
        } catch (Exception ex) { 
            log.error("Error saving draft", ex); 
            return null; 
        } 
    }

    public void submitNewCheck(HttpServletRequest request, EquityChecksForm equityChecksForm, LatteUser latteUser) { 
    try {
        EquityChecksModel equityCheck = 
        equityCheckService.createEquityCheck(equityChecksForm, latteUser, 
        "Submitted"); 
        equityCheckService.saveEquityCheck(equityCheck, false); 
    } catch (Exception ex) { 
        log.error("Error submitting new check", ex); 
    }
    }

    public void updateExistingCheck (HttpServletRequest request, EquityChecksForm equityChecksForm, LatteUser latteUser) { 
    try { 
        EquityChecksModel equityCheck = 
        equityCheckService.updateEquityCheck(equityChecksForm, latteUser); 
        equityCheckService.saveEquityCheck(equityCheck, false); 
    } catch (Exception ex) { 
        log.error("Error updating existing check", ex); 
    }
    }

    public void updateExistingCheck(HttpServletRequest request, EquityChecksForm equityChecksForm, LatteUser latteUser) {
        try {
            EquityChecksModel equityCheck = equityCheckService.buildEquityCheck(equityChecksForm,latteÜser,null);
            equityCheckService.saveEquityCheck(equityCheck, false);
        } catch (Exception ex) {
            log.error ("Error updating existing check: " + ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }
    // The common code for each save action was moved into a separate method

    private void handleSaveAction(HttpservietRequest request, EquitychecksForm equityChecksForm, LatteUser LatteUser, boolean isDraft) {
        List<String> errors = new ArrayList<>();
        Optional<EquityChecksModel> equityChecksOptional = equityCheckService.validateAndSaveEquityCheck(equityChecksForm, errors, LatteUser, isDraft);
        saveErrorHandler(request, equityChecksForm, errors);
        if (!equityChecksOptional.isPresent()) {
            log.error("could not save equity check");
            return;
        }
        EquityChecksMode1 equityCheckToSave = equityChecksOptional.get();
        internalDueDateCheck(equityCheckToSave, LatteUser);
        statusUpdate(equityCheckToSave, LatteUser, isDraft); 
        setupSaveSuccessNotification(equityCheckToSave, request);
    }
    public void internalDueDateCheck (EquityChecksModel equityCheck, LatteUser latteUser) {
        internalDueDateCheck(equityCheck, latteUser);
    }
    public void statusUpdate (EquityChecksModel equityCheck, LatteUser latteUser, Boolean isDraft) {
        equityCheckService.statusUpdate(equityCheck, latteÜser, isDraft);
    }
    public Optional<EquityChecksModel> getAnyEquityCheckById(Long id) {
        return equityCheckService.findEquityCheckIncludingDeleted(id);
    }

    public void setupSaveSuccessNotification (EquityChecksModel equityCheck,
    HttpServletRequest request) {
    // TODO: flesh out the save success notifications for creating/editing equity
    // checks
    }

    // TODO: add an admin check method for admin-oriented calls (like keywords) public void saveErrorlandler (HttpServletRequest request, EquityChecksForm eqForm, List<String> errors) I

    // TODO: flesh out/correct this handler
    // setPageNotification (request, AMERICANO EQUITY CHECK + " Save Error"
    // EQUITY_CHECK_NOT_SAVED.getValue (), errors);
    // request. setAttribute (EQUITY_CHECK_FORM_ATTRIBUTE, eqForm);
    // if (eqForm.getId() == null)
    // {
    // request. setAttribute (EQUITY_CHECK_OPERATION, "Create");
    // ' else

    // request.setAttribute (EQUITY CHECK OPERATION, "Update"):
    // }
    public Optional<EquityChecksModel> checkEquityChecksFormForErrors (EquityChecksForm eqForm, List<String> errors,
    LatteUser latteUser) {
    return equityCheckService.validateAndSaveEquityCheck (eqForm, errors,
    latteUser, false);
    }

    public String saveNoAccess(HttpServletRequest request, EquityChecksForm eqForm, LatteUser latteUser) {
        // Save the failed equity check and set page notification
        DomSALService.saveltemFailed(eqForm, "The user does not have access to create/update an " + AMERICANO_EQUITY_CHECK, latteUser);
        this.setPageNotification(request, false, "No Permissions", SALErrors.NO_PERMISSIONS_PAGE.getValue().replace("|", PageName.EQUITY_CHECK_EDIT.getValue()));
        return "/";
    }
    public String create(HttpServletRequest request, LatteUser latteUser) {
        // Add reference data tables to the request
        addReferenceDataTables(request);
        // Create a new equity check request
        EquityChecksModel newRequest = equityCheckService.createNewRequest(latteUser);
        // Set tags for the request
        refAndTagService.setTags(request, newRequest);
        // Set attributes for the request
        request.setAttribute(ATTR_EQUITY_CHECK_OPERATION, "Create");
        request.setAttribute(ATTR_EQUITY_CHECK_FORM, newRequest);
        request.setAttribute(ATTR_CAN_VIEW_INTERNAL_DUE_DATE, latteUser.canViewEquityCheckInternalDueDate());
        return PAGE_CREATE;
    }

    public String createPageNoAccess(LatteUser latteUser) {
        // Set page notification for no permissions
        DomSALService.noPermissionsForPage(PageName.EQUITY_CHECK_CREATE, latteUser);
        return "/";
    }

    public String edit(HttpServletRequest request, Long id, String searchld, LatteUser latteUser) {
        // Set permission attributes for the request
        setPermissionAttributes(request, latteUser);
        // Add reference data tables to the request
        addReferenceDataTables(request);
        // Get equity check by id
        Optional<EquityChecksModel> equity = getEquityCheckById(id);
        if (equity.isPresent()) {
            // Handle equity check edit
            editEquityCheckHandler(equity, request, id, searchld, latteUser);
            return PAGE_CREATE;
        } else {
            // Handle edit error
            return editErrorHandler(request, id, searchld, latteUser);
        }
    }

    public String editErrorHandler (HttpServletRequest request, Long id, String searchId, LatteUser latteUser) {
        // TODO: flesh out for error handling an edit
        EquityChecksModel c = new EquityChecksModel();
        c.setId(id);
        DomSALService.viewFailedItem(c, SALErrors.EQUITY_CHECK_NOT_FOUND, searchId, latteUser);
        setPageNotification (request, false, AMERICANO_EQUITY_CHECKS + " not Found",
        "Error. " + AMERICANO_EQUITY_CHECKS + " could not be found.");
        return listPage (request, latteUser);
    }
        
    // Method to handle editing an equity check
    public void editEquityCheckHandler(Optional<EquityChecksModel> equity, HttpServletRequest request, Long id, String searchId, LatteUser latteUser) {
        // Get the equity check model
        EquityChecksModel c = equity.get();
        // Set tags for the equity check
        setTags(request, c);
        // Set the attribute for the equity check form
        request.setAttribute(ATTR_EQUITY_CHECK_FORM, c);
        // Set the attribute for the equity check operation
        if (c.getStatus().contains("Draft")) {
            request.setAttribute(ATTR_EQUITY_CHECK_OPERATION, "Create");
        } else {
            request.setAttribute(ATTR_EQUITY_CHECK_OPERATION, "Update");
        }
        // Call DomSALService to view the item
        DomSALService.viewedItem(c, searchId, latteUser);
    }

    public Optional<EquityChecksModel> getEquityCheckById(Long id) {
        return equityCheckService.findEquityCheckById(id);
    }

    public String editPageNoAccess (HttpServletRequest request, LatteÜser latteÜser) {
        // Check if the user has permissions for the page
        if (!DomSALService.hasPermissionsForPage(PageName.EQUITY_CHECK_EDIT, latteÜser)) {
            // Set page notification
            this.setPageNotification(request, false, "No Permissions");
            // Set error code
            SALErrors.NO_PERMISSIONS.PAGE.getValue().replace(" |", PageName.EQUITY_CHECK_EDIT.getValue());
            // Return empty string
            return "/";
        }
    }

    // Get the request, id, searchId, and latteUser parameters
    public String view(HttpServletRequest request, Long id, String searchId, LatteÜser latteUser) {
        // Set the permission attributes for the request and latteUser
        setPermissionAttributes(request, latteÜser);
        // Return the page view
        return PAGE_VIEW;
    }

    public String viewEquityCheckEmptyHandler(Long id, String searchId, HttpServletRequest request, LatteUser latteUser) {
        // Handle empty equity checks
        String temp = "delete this, eventually";
        // Return the temp string
        return temp;
    }
        
    public void viewEquityCheckPresentHandler(Long id, Optional<EquityChecksModel> equity, HttpServletRequest request, String searchId, LatteUser latteUser) {
        // Check if the user has access to the page
        if (DomSALService.noPermissionsForPage(PageName.EQUITY_CHECK_VIEW, latteUser)) {
            // Set a notification for the user
            this.setPageNotification(request, false, "No Permissions" + SALErrors.NO_PERMISSIONS_PAGE.getValue().replace("|", PageName.EQUITY_CHECK_VIEW.getValue()));
            return "/";
        }        
    }

    public String getInlineEquityCheck(HttpServletRequest request, Long id, LatteUser latteUser) {
        // TODO: flesh out for view inline equity checks
        String temp = "delete this, eventually";
        return temp;
    }

    public void viewEquityCheckPresentHandler (Long id, Optional<EquityChecksModel>
    equity, HttpServletRequest request,
    String searchld, LatteUser latteUser) {
    // TODO: flesh out for handling equity checks with filled-out fields
    }

    public String viewPageNoAccess (HttpServletRequest request, LatteUser latteUser) {
        DomSALService.noPermissionsForPage(PageName.EQUITY_CHECK_VIEW, latteUser);
        this.setPageNotification(request, false, "No Permissions", 
        SALErrors.NO_PERMISSIONS_PAGE.getValue().replace('|',
        PageName.EQUITY_CHECK_VIEW.getValue()));
        return "/";
    }

    public String getInlineEquityCheckNoAccess (HttpServletRequest request, Long id, LatteUser latteUser) {
        DomSALService.noPermissionsAjaxAction("The user does not have permission to view the equity check with id: " + id, latteUser);
         // Set page notification to false and provide an error message
        this.setPageNotification(request, false, "No Permission Error", 
         SALErrors.NO_PERMISSIONSITEM.toString().replace("|", id.toString()));
        return "/";
    }

   public void addReferenceDataTables(HttpServletRequest request) {
    // Set the attribute for DOM Field Stations
    request.setAttribute("domFieldStations", domRefDataService.getReferenceDataByType(EnumRefDataType.DOM_FIELD_STATION));
     // Get the list of requesting offices, action POCs, office POCs and organization names
    List<String> requestingOffices = cleanList(equityCheckService.getRequestingOffices());
    List<String> actionPOCs = cleanList(equityCheckService.getActionPOCs());
    List<String> officePOCs = cleanList(equityCheckService.getOfficePOCs());
    List<String> organizationNames = cleanList(equityCheckService.getOrganizationNames());
     // Set the attributes for the lists
    request.setAttribute("requestingOffices", requestingOffices);
    request.setAttribute("actionPOCs", actionPOCs);
    request.setAttribute("officePOCs", officePOCs);
    request.setAttribute("organizationNames", organizationNames);
    }

    public List<String> cleanList(List<String> listToClean) {
        // Filter out null and empty strings from the list
        return listToClean.stream()
            .filter(s -> s != null && !s.trim().isEmpty())
            .sorted()
            .collect(Collectors.toList());
    }

    protected void setTags(HttpServletRequest request, EquityChecksModel equity) {
        // TODO: Implement logic to set tags
    }

    public void deleteDraft(Long equityCheckId, Boolean fromDraftList) {
        equityCheckService.deleteDraft(equityCheckId, fromDraftList);
    }

    public List<EquityChecksModel> findDraftsByUser(String username) {
        return equityCheckService.findDraftsByUser(username);
    }

    public boolean ownsRequest(LatteUser latteUser, Long id) {
        return equityCheckService.ownsRequest(latteUser, id);
    }
}
