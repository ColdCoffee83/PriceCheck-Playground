@Service
  public class IdamActionService extends BaseActionService {
    private IdamService idamService;

    @Autowired
    public IdamActionService(IdamService idamService) {
      this.idamService = idamService;
    }

    public AjaxTableData searchGroup(PersonSearch search, LatteUser user) {
      AjaxTableData response = new AjaxTableData();
      response.setRecords(idamService.searchGroup(search.getGroup(), user));
      return response;
  }

    public AjaxPersonData searchPeople(PersonSearch search, LatteUser user) {
      AjaxPersonData response = new AjaxPersonData();
      response.setData(idamService.searchPeopleByName(search, user));
      return response;
    }
      