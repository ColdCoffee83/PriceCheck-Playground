@Service
  public class IdamService {
    private static Logger log = LoggerFactory.getLogger(IdamService.class);
    private static final String NOTES_ID = "notesId";

    @Autowired
    private Idam idam;

    @Autowired
    private RefIdamRepository idamRepo;

    @Autowired
    private HttpServletRequest request;

    public RefIdam getIdamByAin(String ain) {
      return idamRepo.queryRefIdamByAin(ain);
    }

    public RefIdam getByGroup(String group) {
      return idamRepo.queryRefIdamByGroup(group);
    }

    public RefIdam save(RefIdam refIdam) {
      log.info("Saving refIdam: {}", refIdam);
      if (refIdam.getFirstVisit() == null) {
        refIdam.setFirstVisit(true);        
      }
      try {
        idamRepo.save(refIdam);      
      } catch (DataIntegrityViolationException e) {
        // this means there was already one in the db
        log.info("RefIdam already exists in db, updating it");
        refIdam = idamRepo.queryRefIdamByAin(refIdam.getAin());
      }
      return refIdam;
    }

    public RefIdam addIdamGroup(String groupName) {
      Optional<IdamGroup> aGroup = getGroup(groupName);
      RefIdam newGroup = null;
      if (!aGroup.isPresent()) {
        createAndSaveGroup(aGroup, newGroup);
      }
      return newGroup;
    }

    // createAndSaveGroup function
    private void createAndSaveGroup(Optional<IdamGroup> aGroup, RefIdam newGroup) {
      newGroup = new RefIdam(aGroup.get());
      newGroup.setModifiedInfo((LatteUser) request.getSession().getAttribute('latteUser'), false);
      save(newGroup);
    }

    public PersonaForUI searchPersonByUserId(String userName, LatteUser latteUser) {
      // if unsername not populated return new personaforui
      if (userName == null || userName.isEmpty()) {
        return new PersonaForUI();
      }

      // if username is populated, try to find the user in idam
      try {
        List<SearchTerm> searchTerms = new ArrayList<>();
        searchTerms.add(new SearchTerm(NOTES_ID, userName));
        Optional<List<Persona>> opPeopleList = getOpPeopleList(searchTerms, 0);
        if (opPeopleList.isPresent()) {
          List<Persona> peopleList = opPeopleList.get();
          if (peopleList.size() > 0) {
            Persona persona = peopleList.get(0);
            return new PersonaForUI(persona);
          }
        }
      } catch (Exception e) {
        log.error("Error while searching for user: {}", e.getMessage());
      }
      return new PersonaForUI();
    }

    // getOpPeopleList function
    private Optional<List<Persona>> getOpPeopleList(List<SearchTerm> searchTerms, int limit) throws Exception {
      if (limit > 0) {
        return idam.people().limit(limit).findBy(searchTerms);
      } else {
        return idam.people().findBy(searchTerms);
      }
    }

    // searchPersonByName function
    public List<PersonaForUI> searchPersonByName(PersonSearch  personSearch, LatteUser latteUser) {
      if (personSearch.isBlankSearch()) {
        return new ArrayList<>();
      }

      try {
        List<SearchTerm> searchTerms = new ArrayList<>();
        // if last name is populated
        if (personSearch.getLastName()!= null &&!personSearch.getLastName().isEmpty()) {
          searchTerms.add(new SearchTerm("lastName", personSearch.getLastName() + "*"));
        }
        // if first name is populated
        if (personSearch.getFirstName()!= null &&!personSearch.getFirstName().isEmpty()) {
          searchTerms.add(new SearchTerm("firstName", personSearch.getFirstName() + "*"));
        }
        Optional<List<Persona>> opIdamList = getOpPeopleList(searchTerms, 10);
        if (opIdamList.isPresent()) {
          List<Persona> idamList = opIdamList.get();
          List<PersonaForUI> personaForUIList = new ArrayList<>();
          for (Persona persona : idamList) {
            personaForUIList.add(new PersonaForUI(persona));
          }
          return personaForUIList;
        } catch (Exception e) {
          log.error("Error while searching for user: {}", e.getMessage());
        }
        return new ArrayList<>();        
      }   

    //searchGroup function
    public List<IdamGroup> searchGroup(String groupName, LatteUser latteUser) {
      // if group name is blank return empty list
      if (groupName == null || groupName.isEmpty()) {
        return new ArrayList<>();
      }
      // if group name is populated, try to find the group in idam
      try {
        List<Group> groups = new ArrayList<>();
        groups = addToGroup(groups, groupName, IdamCollection.DEFAULT);
        groups = addToGroup(groups, groupName, IdamCollection.AD);

        if (!groups.isEmpty()) {
          return groups.stream().map(IdamGroup::new).collect(Collectors.toList());
        }
        idam.setCollection(IdamCollection.DEFAULT);
      } catch (Exception e) {
        log.error("Error while searching for group: {}", e.getMessage());
      }
      return new ArrayList<>();
    }

    private List<Group> addToGroup(List<Group> groups, String groupName, IdamCollection collection) throws IdamException {
      idam.setCollection(collection);
      Optional<List<Group>> opGroups = idam.groups().findByCn(groupName + "*");
      if (opGroups.isPresent()) {
        List<Group> idamGroups = opGroups.get();
        groups.addAll(idamGroups);
      }
      return groups;
    }
    
    // getGroup function
    public List<IdamGroup> getGroup(String groupName) {
      // if group name is blank return empty list
      if (groupName == null || groupName.isEmpty()) {
        return new ArrayList<>();
      }
      try {
        IdamCollection[] collections = {IdamCollection.DEFAULT, IdamCollection.AD};
        Optional<IdamGroup> opGroup;
        for (IdamCollection collection : collections) {
          opGroup = searchIdamGroups(collection, groupName);
          if (opGroup.isPresent()) {
            if (collection == IdamCollection.AD) {
              idam.setCollection(IdamCollection.DEFAULT);
            }
            return opGroup;
          }
        }
      } catch (Exception e) {
        log.error("Error while searching for group: {}", e.getMessage());
      }
      return Optional.empty();
    }

    private Optional<IdamGroup> searchIdamGroups(IdamCollection collection, String groupName) throws IdamException {
      idam.setCollection(collection);
      Optional<List<IdamGroup>> opGroups = idam.groups().findByCn(groupName + "*");
      if (opGroups.isPresent()) {
        List<Group> groups = opGroups.get();
        return Optional.of(new IdamGroup(groups.get(0)));
      }
      return null
    }

    // getByAin function
    public Optional<PersonaForUI> getByAin(String ain) {
      try {
        Optional<Persona> opPersona = idam.people().findOneByAin(ain);
        if (opPersona.isPresent()) {
          return Optional.of(new PersonaForUI(opPersona.get()));
        }
      } catch (Exception e) {
        log.error("Error while searching for user: {}", e.getMessage());
      }
      return Optional.empty();
    }
  }