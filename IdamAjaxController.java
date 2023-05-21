@RestController
@RequestMapping(value = "/ajax/peopleLookup")
public class IdamAjaxController extends BaseController {
  private IdamAjaxActionService actionService;

  @Autowired
  public IdamAjaxController(IdamAjaxActionService actionService) {
    this.actionService = actionService;
  }  
  
  // get method for searchPeople function in action service at /name
  @RequestMapping(value = "/name", method = RequestMethod.GET)
  public AjaxPersonData searchPeople(HttpServletRequest request, PersonSearch search) {
    return actionService.searchPeople(search, getLatteUser(request));
  }

  // get method for searchGroup
  @RequestMapping(value = "/group", method = RequestMethod.GET)
  public AjaxTableData searchGroup(HttpServletRequest request, GroupSearch search) {
    return actionService.searchGroup(search, getLatteUser(request));
  }
}