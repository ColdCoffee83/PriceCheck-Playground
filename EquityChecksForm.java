import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import gov.cia.americano.domesticmodel.DomBaseModel;
import gov.cia.americano.domesticmodel.DomReference;
import gov.cia.americano.domesticmodel.equityChecks.EquityChecksModel;
import gov.cia.americano.domesticmodel.equityChecks.EquityChecksSearchHelp;
import gov.cia.americano.model.ReflIdam;
import gov.cia.americano.security.LatteUser;
import gov.cia.americano.utils.CollectionUtils;

/**
 * Form used to save and update equity check data.
 */ 
public class EquityChecksForm extends DomBaseModel {
// Variables related to attachments
private Set<Long> attachmentsList = new HashSet<>();
 // Variables related to due dates
private Long internalDueDate;
private Long requestedDueDate;
private Long coordinationDueDate;
 // Variables related to organization
private String organizationState;
private Long officePOCId;
private Long actionPOCId;
private String organizationName;
private String requestingOffice;
private String organizationUSAddress;
private String organizationCity;
private String organizationPOC;
private String email;
private String phone;
 // Variables related to justification
private String justification;
private String justificationDescription;
private String requestType;
private String status;
private Boolean openSourceResearchConducted;
private String anticipatedHighestSecurityLevel;
private String comments;
private Boolean firstTimeContactByRequestor;
private Long proposedEngagementDate;
private String engagementDescription;
private String howWasContactMade;
private Boolean priorCompanyContact;
private String priorCompanyContactPOC;
private String equiNumber;
 // Variables related to field stations
private Set<Long> fieldStationList = new HashSet<>();
 // Variables related to keywords
private Set<String> keywords = new HashSet<>();
 // Variables related to draft status
private Boolean isDraft;
private Timestamp editTimestamp;
 // Variables related to references
private Boolean referencedCore;
private Boolean referencedCrossStream;
private Boolean referencedExternal;
private Boolean referencedInternal;
 // Variables related to creation and modification
private Long createdBy;
private Long modifiedBy;
private Timestamp createdOn;
private Timestamp modifiedOn;

public EquityChecksForm() {
    super ();
}

public void saveEquityCheck (EquityChecksModel equityCheck, LatteUser latteUser) {
    RefIdam officePOC = null;
    RefIdam actionPOC = null;
     equityCheck = new EquityChecksModel(internalDueDate, requestedDueDate, coordinationDueDate, organizationName, requestingOffice, organizationUSAddress, organizationCity, organizationState, organizationPOC, email, phone, justification, justificationDescription, requestType, status, openSourceResearchConducted, anticipatedHighestSecurityLevel, comments, firstTimeContactByRequestor, proposedEngagementDate, engagementDescription, howWasContactMade, priorCompanyContact, priorCompanyContactPOC, equiNumber, referencedCore, referencedCrossStream);
     checkSearchHelp (equityCheck, latteUser);
     if (officePOCId != null) {
        officePOC = new Refldam(officePOCId);
        equityCheck.setOfficePOC (officePOC) ;
    }
     if (actionPOCId != null) {
        actionPOC = new Refldam(actionPOCId);
        equityCheck.setActionPOC (actionPOC) ;
    }
     equityCheck.getFieldStationList().clear();
    for (int i = 0; i < fieldStationList.size(); i++) {
        equityCheck.getFieldStationList ().add (new DomReference (fieldStationList.get(i)));
    }
     equityCheckRepository.save (equityCheck) ;
}

public void checkSearchHelp (EquityChecksModel equityCheck, LatteUser latteUser) {
    EquityChecksSearchHelp searchHelp = equityCheck.getEquityChecksSearchHelp();
    if (searchHelp == null) {
        searchHelp = new EquityChecksSearchHelp();
        searchHelp.setCreatedInfo(latteUser);
        searchHelp.setDeleted (false);
        searchHelp.setEquityCheck (new EquityChecksModel (equityCheck.getId()));
        equityCheck.setEquityChecksSearchHelp(searchHelp);
    }
    if (CollectionUtils.isNotEmpty (keywords)) {
        String keys = String.join(",", keywords);
        searchHelp.setKeywords (keys) ;
    } else {
        searchHelp.setKeywords (null);
    }
}

@Override
public String getAuditValue() {
return getId().toString();
}

@Override
public String getLabel() {
return "Create new Equity Check (No fields selected yet)";
}

public Set<Long> getAttachmentsList() {
return this.attachmentsList;
}

public void setAttachmentsList (Set<Long> attachmentsList) {
this.attachmentsList = attachmentsList;

}

public Long getInternalDueDate() {
return internalDueDate;

}

public void setInternalDueDate (Long internalDueDate) {
this.internalDueDate = internalDueDate;
}

public Long getRequestedDueDate() {
return requestedDueDate;
}

public void setRequestedDueDate (Long requestedDueDate) {
this.requestedDueDate = requestedDueDate;
}

public Long getCoordinationDueDate() {
return coordinationDueDate;

}

public void setCoordinationDueDate (Long coordinationDueDate) {
this.coordinationDueDate = coordinationDueDate;
}

public String getOrganizationState() {
return organizationState;
}

public void setOrganizationState (String organizationState) {
this.organizationState = organizationState;

}

public Long getActionPOCId() {
return actionPOCId;

}

public void setActionPOCId (Long actionPOCId) {
this.actionPOCId = actionPOCId;
}

public String getOrganizationName() {
return organizationName;

}

public void setOrganizationName (String organizationName) {
this.organizationName = organizationName;

}

public String getRequestingOffice() {
return requestingOffice;
}

public void setRequestingOffice (String requestingOffice) {
this.requestingOffice = requestingOffice;
}

public Long getOfficePOCId() {
return officePOCId;
}

public void setOfficePOCId (Long officePOCId) {
this.officePOCId = officePOCId;
}
public String getOrganizationUSAddress() {
    return organizationUSAddress;
}
 public void setOrganizationUSAddress (String organizationUSAddress) {
    this.organizationUSAddress = organizationUSAddress;
}
 public String getOrganizationCity() {
    return organizationCity;
}
 public void setOrganizationCity (String organizationCity) {
    this.organizationCity = organizationCity;
}
 public String getOrganizationPOC() {
    return organizationPOC;
}
 public void setOrganizationPOC (String organizationPOC) {
    this.organizationPOC = organizationPOC;
}
 public String getEmail() {
    return email;
}
 public void setEmail (String email) {
    this.email = email;
}
 public String getPhone() {
    return phone;
}
 public void setPhone (String phone) {
    this.phone = phone;
}
 public String getJustification() {
    return justification;
}
 public void setJustification (String justification) {
    this.justification = justification;
}
 public String getJustificationDescription() {
    return justificationDescription;
}
 public void setJustificationDescription (String justificationDescription) {
    this.justificationDescription = justificationDescription;
}

public String getRequestType() {
return requestType;
}

public void setRequestType (String requestType) {
this.requestType = requestType;

}

public String getStatus() {
return status;

}

public void setStatus (String status) {
this.status = status;

}

public Boolean getOpenSourceResearchConducted() {
return openSourceResearchConducted;

}

public void setOpenSourceResearchConducted (Boolean openSourceResearchConducted) {
this.openSourceResearchConducted = openSourceResearchConducted;

}

public String getAnticipatedHighestSecurityLevel() {
return anticipatedHighestSecurityLevel;
}

public void setAnticipatedHighestSecurityLevel (String
anticipatedHighestSecurityLevel) {
this.anticipatedHighestSecurityLevel = anticipatedHighestSecurityLevel;
}

public String getComments() {
return comments;
}
 public void setComments (String comments) {
this.comments = comments;
}
 public Boolean getFirstTimeContactByRequestor() {
return firstTimeContactByRequestor;
}
 public void setFirstTimeContactByRequestor (Boolean firstTimeContactByRequestor) {
this.firstTimeContactByRequestor = firstTimeContactByRequestor;
}
 public Long getProposedEngagementDate() {
return proposedEngagementDate;
}
 public void setProposedEngagementDate (Long proposedEngagementDate) {
this.proposedEngagementDate = proposedEngagementDate;
}
 public String getEngagementDescription() {
return engagementDescription;
}
 public void setEngagementDescription (String engagementDescription) {
this.engagementDescription = engagementDescription;
}
 public String getHowWasContactMade() {
return howWasContactMade;
}
 public void setHowWasContactMade (String howWasContactMade) {
this.howWasContactMade = howWasContactMade;
}
 public Boolean getPriorCompanyContact() {
return priorCompanyContact;
}
 public void setPriorCompanyContact (Boolean priorCompanyContact) {
this.priorCompanyContact = priorCompanyContact;
}
 public String getPriorCompanyContactPOC() {
return priorCompanyContactPOC;
}
 public void setPriorCompanyContactPOC (String priorCompanyContactPOC) {
this.priorCompanyContactPOC = priorCompanyContactPOC;
}
 public String getEquiNumber () {
return equiNumber;
}
 public void setEquiNumber (String equiNumber) {
this.equiNumber = equiNumber;
}
 public Set<String> getKeywords() {
return keywords;
}
 public void setKeywords (Set<String> keywords) {
this.keywords = keywords;
}
public Boolean getIsDraft() {
    return isDraft;
}
public void setIsDraft (Boolean isDraft) {
    this.isDraft = isDraft;
}
public Timestamp getEditTimestamp() {
    return editTimestamp;
}
public void setEditTimestamp (Timestamp editTimestamp) {
    this.editTimestamp = editTimestamp;
}
public Set<Long> getFieldStationList() {
    return fieldStationList;
}
public void setFieldStationList (Set<Long> fieldStationList) {
    this.fieldStationList = fieldStationList;
}
public Boolean getReferencedCore() {
    return referencedCore;
}
public void setReferencedCore (Boolean referencedCore) {
    this.referencedCore = referencedCore;
}
public Boolean getReferencedCrossStream() {
    return referencedCrossStream;
}
public void setReferencedCrossStream (Boolean referencedCrossStream) {
    this.referencedCrossStream = referencedCrossStream;
}
}