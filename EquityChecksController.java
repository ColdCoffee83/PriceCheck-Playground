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
 
    private final EquityCheckActionService actionService; 
 
    @Autowired 
    public EquityChecksController(EquityCheckActionService actionService) { 
        this.actionService = actionService; 
    } 
 
    @RequestMapping("/") 
    public String listPage(HttpServletRequest request) { 
        LatteUser latteUser = getLatteUser(request); 
 
        // If this path was redirected to via a save, set the save success/failed notification as a request attribute 
        HttpSession session = request.getSession(); 
        if (session.getAttribute("notification") != null) { 
            request.setAttribute("notification", session.getAttribute("notification")); 
        } 
        session.removeAttribute("notification"); 
 
        if (!donUserAccessService.hasAnyRole(latteUser)) { 
            return actionService.noAccess(request, latteUser); 
        } 
 
        return actionService.listPage(request, latteUser); 
    } 
 
    @RequestMapping("/myrequests") 
    public String myRequestsPage(HttpServletRequest request) { 
        LatteUser latteUser = getLatteUser(request); 
 
        // If this path was redirected to via a save, set the save success/failed notification as a request attribute 
        HttpSession session = request.getSession(); 
        if (session.getAttribute("notification") != null) { 
            request.setAttribute("notification", session.getAttribute("notification")); 
        } 
        session.removeAttribute("notification"); 
 
        // TODO: to update the permission check to equity users once that LDAP roles are created 
        if (!domUserAccessService.hasAnyRole(latteUser)) { 
            return actionService.noAccess(request, latteUser); 
        } 
 
        return actionService.myRequestsPage(request, latteUser); 
        } 
 
    @RequestMapping("/create") 
    public String createPage(HttpServletRequest request) { 
        LatteUser latteUser = getLatteUser(request); 

        if (!domUserAccessService.canCreateEquityCheck(latteUser)) { 
            return actionService.createPageNoAccess(latteUser); 
        } 

        return actionService.create(request, latteUser); 
    } 

    @RequestMapping(value = { "/view/{id}", "/view/{id}/{searchId}" }) 
    public String viewEquityCheck(HttpServletRequest request, @PathVariable Long id, 
            @PathVariable(required = false) String searchId) { 
        LatteUser latteUser = getLatteUser(request); 

        if (!domUserAccessService.canViewEquityCheck(latteUser)) { 
            return actionService.viewPageNoAccess(request, latteUser); 
        } 

        return actionService.view(request, id, searchId, latteUser); 
    } 

    @RequestMapping(value = { "/edit/{id}", "/edit/{id}/{searchId}", "/myrequests/edit"})
    public String editPage(HttpServletRequest request, @PathVariable Long id, @PathVariable(required = false) String searchId) { 
        LatteUser latteUser = getLatteUser(request); 
        String username = getCurrentUser(request); 
        List<EquityChecksModel> checksList = actionService.findDraftsByUser(username); 
        Optional<EquityChecksModel> isOwned = checksList.stream()
                                            .filter(check -> check.getId().equals(id))
                                            .findFirst(); 
        if (isOwned.isPresent() && !domUserAccessService.canEditEquityCheck(latteUser)) { 
            return actionService.editPageNoAccess(request, latteUser); 
        } 
        return actionService.edit(request, id, searchId, latteUser); 
    } 
    @PostMapping("/save/draft") 
    public ResponseEntity<EquityChecksModel> saveAsDraft(@RequestBody @Valid EquityChecksForm equityChecksForm, HttpServletRequest request) { 
        LatteUser latteUser = getLatteUser(request); 
        EquityChecksModel equityCheck = actionService.saveAsDraft(request, equityChecksForm, latteUser); 
        return new ResponseEntity<>(equityCheck, HttpStatus.CREATED); 
    } 
    @PostMapping("/save/submit") 
    public ResponseEntity<EquityChecksModel> submitNewCheck(@RequestBody @Valid EquityChecksForm equityChecksForm, HttpServletRequest request) { 
        LatteUser latteUser = getLatteUser(request); 
        EquityChecksModel equityCheck = actionService.submitNewCheck(equityChecksForm, latteUser); 
        return new ResponseEntity<>(equityCheck, HttpStatus.CREATED); 
    } 
    @PutMapping("/save/update/{id}") 
    public ResponseEntity<EquityChecksModel> updateExistingCheck(@PathVariable Long id, @RequestBody @Valid EquityChecksForm equityChecksForm, HttpServletRequest request) { 
        LatteUser latteUser = getLatteUser(request); 
        EquityChecksModel equityCheck = actionService.updateExistingCheck(id, equityChecksForm, latteUser); 
        return new ResponseEntity<>(equityCheck, HttpStatus.OK); 
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
                response.put("hasDraft", true); 
                response.put("latestDraftId", latestDraft.getId()); 
            } else { 
                response.put("hasDraft", false); 
            } 
            return response; 
        } catch (NonUniqueResultException ex) { 
            response.put("hasDraft", true); 
            return response; 
        } catch (IndexOutOfBoundsException ex) { 
            response.put("hasDraft", false); 
            return response; 
        } 
    } 
    @DeleteMapping("/deleteDraft/{equityCheckId}") 
    public ResponseEntity<Void> deleteDraft(@PathVariable Long equityCheckId, @RequestBody Map<String, Object> request) { 
        boolean deleteFromList = (boolean) request.get("deleteFromList"); 
        actionService.deleteDraft(equityCheckId, deleteFromList); 
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
