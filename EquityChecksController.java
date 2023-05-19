import java.io.IOException; 
import java.security.Principal; 
import java.util.HashMap; 
import java.util.List; 
import java.util.Map; 
import java.util.Optional; 
 
import javax.persistence.NonUniqueResultException; 
import javax.servlet.http.HttpServlet; 
import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse; 
import javax.servlet.http.HttpSession; 
import javax.validation.Valid; 
 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity; 
import org.springframework.orm.ObjectOptimisticLockingFailureException; 
import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.DeleteMapping; 
import org.springframework.web.bind.annotation.ExceptionHandler; 
import org.springframework.web.bind.annotation.GetMapping; 
import org.springframework.web.bind.annotation.PathVariable; 
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.PutMapping; 
import org.springframework.web.bind.annotation.RequestBody; 
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RequestMethod; 
import org.springframework.web.bind.annotation.ResponseBody; 
 
import gov.cia.americano.domestic.service.action.EquityCheckActionService; 
import gov.cia.americano.domesticmodel.equityChecks.EquityChecksModel; 
import gov.cia.americano.domesticmodel.form.EquityChecksForm; 
import gov.cia.americano.security.LatteUser; 
 
@Controller
@RequestMapping("/equity") 
public class EquityChecksController extends DomBaseController { 
    private static final String NOTIFICATION = "notification";
    private static final String HAS_DRAFT = "hasDraft"; 
    private final EquityCheckActionService actionService; 
 
@Autowired 
public EquityChecksController(EquityCheckActionService actionService) {
    try {
        this.actionService = actionService; 
    } catch (Exception e) {
        // Handle any exceptions thrown by the constructor
        logger.error("Error occurred when initializing EquityCheckActionService", e);
        throw new RuntimeException("Error occurred when initializing EquityCheckActionService", e);
    }
}
 
    @RequestMapping("/") 
public String listPage(HttpServletRequest request) { 
    LatteUser latteUser = getCurrentUser(request); 
 
    // If this path was redirected to via a save, set the save success/failed notification as a request attribute 
    HttpSession session = request.getSession(); 
    if (session.getAttribute(NOTIFICATION) != null) { 
        request.setAttribute(NOTIFICATION, session.getAttribute(NOTIFICATION)); 
    } 
    session.removeAttribute(NOTIFICATION); 
 
    if (!donUserAccessService.hasAnyRole(latteUser)) { 
    try {
        return actionService.noAccess(request, latteUser); 
    } catch (Exception e) {
        logger.error("Error occurred in EquityCheckActionService.noAccess method", e);
        // Handle the exception appropriately, such as displaying an error message to the user.
        return "errorPage";
    }
}
 
    return actionService.listPage(request, latteUser); 
}
 
    @RequestMapping("/myrequests") 
    public String myRequestsPage(HttpServletRequest request) { 
        LatteUser latteUser = getCurrentUser(request); 
 
        // If this path was redirected to via a save, set the save success/failed notification as a request attribute 
        HttpSession session = request.getSession(); 
        if (!domUserAccessService.hasAnyRole(latteUser)) { 
    try {
        return actionService.noAccess(request, latteUser);
    } catch (Exception e) {
        logger.error("Error occurred in EquityCheckActionService.noAccess method", e);
        // Handle the exception appropriately, such as displaying an error message to the user.
        return "errorPage";
    }
}
        session.removeAttribute(NOTIFICATION); 
 
        // TODO: to update the permission check to equity users once that LDAP roles are created 
        if (!domUserAccessService.hasAnyRole(latteUser)) { 
            return actionService.noAccess(request, latteUser); 
        } 
 
        return actionService.myRequestsPage(request, latteUser); 
        } 
 
    @RequestMapping("/create") 
    public String createPage(HttpServletRequest request) { 
        LatteUser latteUser = getCurrentUser(request); 

       if (!domUserAccessService.canCreateEquityCheck(latteUser)) { 
    try {
        EquityChecksModel model = new EquityChecksModel();
        return actionService.createPageNoAccess(latteUser);
    } catch (Exception e) {
        logger.error("Error occurred when initializing EquityChecksModel", e);
        // Handle the exception appropriately, such as displaying an error message to the user.
        return "errorPage";
    }
}

        return actionService.create(request, latteUser); 
    } 

    @RequestMapping(value = { "/view/{id}", "/view/{id}/{searchId}" }) 
    public String viewEquityCheck(HttpServletRequest request, @PathVariable Long id, 
            @PathVariable(required = false) String searchId) { 
        LatteUser latteUser = getCurrentUser(request); 

        if (!domUserAccessService.canViewEquityCheck(latteUser)) { 
            logger.info("User {} denied access to view equity check {}", latteUser.getUsername(), id);
            return actionService.viewPageNoAccess(request, latteUser); 
        } 

        return actionService.view(request, id, searchId, latteUser); 
    } 

    @RequestMapping(value = { "/edit/{id}", "/edit/{id}/{searchId}", "/myrequests/edit"})
    public String editPage(HttpServletRequest request, @PathVariable Long id, @PathVariable(required = false) String searchId) { 
        LatteUser latteUser = getCurrentUser(request); 
        List<EquityChecksModel> checksList = actionService.findDraftsByUser(latteUser.getUsername()); 
        Optional<EquityChecksModel> isOwned = checksList.stream()
                                            .filter(check -> check.getId().equals(id))
                                            .findFirst(); 
        if (isOwned.isPresent() && !domUserAccessService.canEditEquityCheck(latteUser)) {
            logger.info("User {} denied access to edit equity check {}", latteUser.getUsername(), id);  
            return actionService.editPageNoAccess(request, latteUser); 
        } 
        return actionService.edit(request, id, searchId, latteUser); 
    } 
    
    @PostMapping("/save/draft") 
    public ResponseEntity<EquityChecksModel> saveAsDraft(@RequestBody @Valid EquityChecksForm equityChecksForm, HttpServletRequest request) { 
        LatteUser latteUser = getCurrentUser(request); 
        EquityChecksModel equityCheck = actionService.saveAsDraft(request, equityChecksForm, latteUser); 
        return new ResponseEntity<>(equityCheck, HttpStatus.CREATED); 
    } 

  @RequestMapping ("/save")
    public void saveEquityCheckRequest (HttpServletRequest request,
HttpServletResponse response, EquityChecksForm equityChecksForm) throws Exception 1
LatteUser latteUser = getLatteUser (request);
actionService. save (request, equityChecksForm, latteÜser);
HttpSession session = request.getSession ();
session.setAttribute (NOTIFICATION, "Equity check request saved successfully");
  
}

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        logger.error("General exception occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Internal server error. Please try again later.");
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class) 
    public ResponseEntity<String> handleOptimisticLockingFailure() { 
        return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("The data you were trying to update has been modified by another user. Please retry your action."); 
    } 

    @RequestMapping(value="/checkDraft", method=RequestMethod.GET) 
@ResponseBody 
public Map<String, Object> checkDraft(Principal principal) { 
    Map<String, Object> response = new HashMap<>();
    String currentUser = principal.getName(); 
    try { 
        EquityChecksModel latestDraft = actionService.getLatestDraftForUser(currentUser); 
        if (latestDraft != null) { 
            response.put(HAS_DRAFT, true); 
            response.put("latestDraftId", latestDraft.getId()); 
        } else { 
            response.put(HAS_DRAFT, false); 
        } 
        return response; 
    } catch (NonUniqueResultException ex) { 
        response.put(HAS_DRAFT, true); 
        return response; 
    } catch (IndexOutOfBoundsException ex) { 
        response.put(HAS_DRAFT, false); 
        return response; 
    } 
}
    @DeleteMapping("/deleteDraft/{equityCheckId}") 
public ResponseEntity<Void> deleteDraft(@PathVariable Long equityCheckId, @RequestBody Map<String, Object> request) { 
    boolean deleteFromList = (boolean) request.get("deleteFromList"); 
    try {
        actionService.deleteDraft(equityCheckId, deleteFromList); 
    } catch (ObjectOptimisticLockingFailureException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("The draft you were trying to delete has been modified by another user. Please refresh and retry the delete.");
    }
    return ResponseEntity.noContent().build(); 
}
    @GetMapping("/drafts") 
    @ResponseBody 
    public List<EquityChecksModel> getDrafts(HttpServletRequest request) {
        String username = getCurrentUser(request); 
        List<EquityChecksModel> test = actionService.findDraftsByUser(username); 
        return test; 
    }
    
    public static String getCurrentUser(HttpServletRequest request) { 
        Principal principal = request.getUserPrincipal(); 
        if (principal != null) { 
            return principal.getName(); 
        } else { 
            return null; 
        }
    } 
}