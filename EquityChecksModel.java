import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column; 
import javax.persistence.Entity;
import javax.persistence.FetchType; 
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints. Pattern;
import com.fasterxmI.jackson.annotation.JsonProperty;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne; 
import javax.persistence.OneToMany;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation. DateTimeFormat;
import gov.cia.americano.model.RefIdam;
import gov.cia.americano.utils.DateTimeUtils;
import gov.cia.americano.domesticmodel.DomBaseModel; 
import gov.cia.americano.domesticmodel.DomReference;
import gov.cia.americano.model.Attachment;

@Entity
@Audited
@Table (name = "equity_checks")
@SuppressWarnings ("unused")
public class EquityChecksModel extends DomBaseModel {
    @Column (name = "EQUITY_CHECK_NUMBER")
    private String equiNumber;
    @Version
    private Long version;
    @Column (name = "INTERNAL_DUE_DATE")
    private Long internalDueDate;
    @Column (name = "REQUESTED_DUE_DATE")
    private Long requestedDueDate;
    @Column (name = "COORDINATION_DUE_DATE")
    private Long coordinationDueDate;
    @Column (name = "PROPOSED_ENGAGEMENT_DATE")
    private Long proposedEngagementDate;
    @Column (name = "ENGAGEMENT_DECRIPTION")
    private String engagementDescription;

    @OneToMany
    @JoinTable(name = "join_equity_checks_to_field_station", 
                joinColumns = @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID"), 
                inverseJoinColumns = @JoinColumn(name = "CHILD_ID", referencedColumnName = "ID"))
    // Set of DomReference objects to store the field station list
    private Set<DomReference> fieldStationList = new HashSet<>();

    @ManyToOne
    @JoinColumn (name = "ACTION_POC_IDAM_ID")
    private RefIdam actionPOC;
    @Column (name = "HOW_WAS_CONTACT_MADE")
    private String howWasContactMade;
   
    @Column (name = "PRIOR_COMPANY_CONTACT_POC")
    private String priorCompanyContactPOC;

    @Column (name = "ORGANIZATION_NAME")
    private String organizationName;

    @Column (name = "ORGANIZATION_US_ADDRESS")
    private String organizationUSAddress;

    @Column (name = "ORGANIZATION_CITY")
    private String organizationcIty;

    @Column (name = "ORGANIZATION_STATE")
    private String organizationstate;

    @Column (name = "ORGANIZATION_POC")
    private String organizationPOC;

    @Column (name = "EMAIL")
    private String email;

    @Column (name = "PHONE")
    private String phone;

    // Enum for justification field
    @Column(name = "JUSTIFICATION", columnDefinition = "ENUM ('Outreach', 'Procurement', 'Operational', 'None') NOT NULL DEFAULT 'None'")
    private String justification;

    @Column (name = "JUSTIFICATION_DESCRIPTION" )
    private String justificationDescription;

    @Column(name = "REQUEST_TYPE", 
    columnDefinition = "ENUM('Traditional', 'Executive', 'None') NOT NULL DEFAULT 'None'") 
    private String requestType; 

    @Column (name = "REQUESTING_OFFICE")
    private String requestingOffice;

    @ManyToOne
    @JoinColumn(name = "OFFICE_POC_IDAM_ID")
    private RefIdam officePOC;

    @Column (name = "FIRST_TIME_CONTACT_BY_REQUESTOR", nullable = false,
    columnDefinition = "TINYINT")
    private Boolean firstTimeContactByRequestor;

    @Column (name = "PRIOR_COMPANY_CONTACT", nullable = false, columnDefinition = "TINYINT" )
    private Boolean priorCompanyContact;

    @Column (name = "OPEN_SOURCE_RESEARCH_CONDUCTED", nullable = false,
    columnDefinition = "TINYINT")
    private Boolean openSourceResearchConducted;

    @Column (name = "ANTICIPATED_HIGHEST_SECURITY_LEVEL")
    private String anticipatedhighestSecurityLevel;

    @Column (name = "STATUS")
    private String status;

    @Column (name = "COMMENTS")
    private String comments;

    @Column (name = "IS_DRAFT", columnDefinition = "TINYINT")
    private Boolean isDraft;
    
    @Column (name = "NOTIFY_REQUESTER_OF_SUBMITTED", columnDefinition = "TINYINT")
    private Boolean notifiedRequester0fSubmitted;

    @Column (name = "NOTIFY_REQUESTER_OF_RECEIVED", columnDefinition = "TINYINT")
    private Boolean notifiedRequesterOfReceivedStatus;

    @Column (name = "NOTIFY_REQUESTER_OF_COMPLETED", columnDefinition = "TINYINT")
    private Boolean notifiedRequesterOfCompleted;

    @Column (name = "REFERENCED_CORE")
    private Boolean referencedCore;

    @Column (name = "REFERENCED_CROSS_STREAM")
    private Boolean referencedCrossStream;

    @Column (name = "SENT_COLLABORATION_EMAIL")
    private Boolean sentCollaborationEmail;

    @OneToMany(fetch = FetchType.EAGER) 
    @JoinTable(
        joinColumns = @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID"), 
        inverseJoinColumns = @JoinColumn(name = "CHILD_ID", referencedColumnName = "ID") 
    )
    private Set<Attachment> attachmentsList; 

    @ManyToMany(cascade = CascadeType.ALL) 
    @JoinTable(name = "join_equity_checks_to_collaboration_stakeholder", 
        joinColumns = @JoinColumn(name = "PARENT.ID", referencedColumnName = "ID"), 
        inverseJoinColumns = @JoinColumn(name = "CHILD ID", referencedColumnName = "ID")) 
    private Set<RefIdam> collaborationStakeholders; 

    @OneToMany
@JoinTable(name = "join_equity_checks_to_field_station_poc", 
            joinColumns = @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID"), 
            inverseJoinColumns = @JoinColumn(name = "CHILD_ID", referencedColumnName = "ID"))
// Set of RefIdam objects to store the field station POC list
private Set<RefIdam> fieldStationPOCList = new HashSet<>();

@OneToMany
@JoinTable(name = "join_equity_checks_to_desk_officer", 
            joinColumns = @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID"), 
            inverseJoinColumns = @JoinColumn(name = "CHILD_ID", referencedColumnName = "ID"))
// Set of RefIdam objects to store the desk officer list
private Set<RefIdam> deskOfficerList = new HashSet<>();


    @OneToOne(mappedBy = "equityCheck", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EquityChecksSearchHelp equityChecksSearchHelp; 
    
    @Transient
    private Boolean isEmptyEquityCheck = true;
    
    @Transient
    private Boolean hasEverBeenSaved = false;

    public EquityChecksMode1 () {
        super();
        // TODO: determine what default sets are required
        this.setStatus("Draft");
        setDeleted(false);
        this.attachmentsList = new HashSet<>();
    }

    public EquityChecksModel (Long id){
        this();
        setId(id);
    }

    @PostLoad
    public void postload () {
        isEmptyEquityCheck = getEditTimestamp() == getInsertTimestamp();
    }
    
    public Boolean getIsEmptyEquityCheck () {
        return isEmptyEquityCheck;
    }

    public void setIsEmptyEquityCheck (Boolean isEmptyEquityCheck) {
        this.isEmptyEquityCheck = isEmptyEquityCheck;
    }

    public Boolean getHasEverBeenSaved() {
        return hasEverBeenSaved;
    }
    
    public void setHasEverBeenSaved (Boolean hasEverBeenSaved) {
        this.hasEverBeenSaved = hasEverBeenSaved;
    }

    public String getLabel () {
        return "Create/Edit Equity Check";
    }

    public String getItenType () {
        return "Equity Check Form";
    }
    
    public String getAuditValue () {
        return getId().toString();
    }

    public Long getInternalDueDate () {
        return internalDueDate;
    }

    public void setInternalDueDate (Long date) {
    this.internalDueDate = date;
    }

    public Long getRequestedDueDate () {
        return requestedDueDate;
    }

    public void setRequestedDueDate(Long requestedDueDate) {
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

    public RefIdam getActionPOC() {
        return actionPOC;
    }

    public void setActionPOC(RefIdam actionPOC) {
        this.actionPOC = actionPOC;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
    this.organizationName = organizationName;
    }
            
    public String getRequestingOffice() {
        return requestingOffice;
    }

    public void setRequestingOffice(String requestingOffice) {
        this.requestingOffice = requestingOffice;
    }

    public RefIdam getOfficePOC() {
        return officePOC;
    }

    public void setOfficePOC(RefIdam officePOC) {
        this.officePOC = officePOC;
    }

    public String getOrganizationUSAddress() {
        return organizationUSAddress;
    }

    public void setOrganizationUSAddress(String organizationUSAddress) {
        this.organizationUSAddress = organizationUSAddress;
    }

    public String geOrganizationCity() {
        return organizationCity;
    }

    public void setOrganizationCity(String organizationCity) {
        this.organizationCity = organizationCity;
    }

    public String getOrganizationPOC() {
        return organizationPOC;
    }

    public void setOrganizationPOC(String organizationPOC) {
        this.organizationPOC = organizationPOC;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }
    
    public String getJustificationDescription ()
    {
    return justificationDescription;
    }
    public void setJustificationDescription (String justificationDescription) {
    this.justificationDescription = justificationDescription;
}
    public String getRequestType () {return requestType;
    }
    public void setRequestType (String requestType) {
    this.requestType = requestType;
    }
    public String getStatus () {
    return status;
    }

    public void setStatus (String status) {
    this.status = status;
    }

    public Boolean getIsDraft() {
        return isDraft;
    }
    public Boolean getOpenSourceResearchConducted() {
        return openSourceResearchConducted;
    }
    public void setOpenSourceResearchConducted(Boolean openSourceResearchConducted) {
        this.openSourceResearchConducted = openSourceResearchConducted;
    }
    public String getAnticipatedHighestSecurityLevel() {
        return anticipatedHighestSecurityLevel;
    }
    public void setAnticipatedHighestSecurityLevel(String anticipatedHighestSecurityLevel) {
        this.anticipatedHighestSecurityLevel = anticipatedHighestSecurityLevel;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public void setIsDraft(Boolean isDraft) {
        this.isDraft = isDraft;
    }
    public Boolean getFirstTimeContactByRequestor() {
        return firstTimeContactByRequestor;
    }
    public void setFirstTimeContactByRequestor(Boolean firstTimeContactByRequestor) {
        this.firstTimeContactByRequestor = firstTimeContactByRequestor;
    }
    public Long getProposedEngagementDate() {
        return proposedEngagementDate;
    }
public void setProposedEngagementDate(Long proposedEngagementDate) {
    this.proposedEngagementDate = proposedEngagementDate;
}
    public String getEngagementDescription () {
    return engagementDescription;
    }

    public void setEngagementDescription (String engagementDescription) {
    this.engagementDescription = engagementDescription;
    }
    public String getHowWasContactMade ( ) {
    return howWasContactMade;
    }
    public void setHowWasContactMade (String howWasContactMade) {
    this. howWasContactMade = howWasContactMade;
    }
        public Boolean getPriorCompanyContact () {
        return priorCompanyContact;
    };
    public void setPriorCompanyContact (Boolean priorCompanyContact) {
        this.priorCompanyContact = priorCompanyContact;
    }
    public String getPriorCompanyContactPOC () { return priorCompanyContactPOC;}
    
        public void setPriorCompanyContactPOC(String priorCompanyContactPOC) {
        this.priorCompanyContactPOC = priorCompanyContactPOC;
    }
    public String getEquiNumber() {
        return equiNumber;
    }
    public void setEquiNumber(String equiNumber) {
        this.equiNumber = equiNumber;
    }
 
 public void setEquityChecksSearchHelp(EquityChecksSearchHelp equityChecksSearchHelp) {
    if (equityChecksSearchHelp == null) {
        return;
    }
    this.equityChecksSearchHelp = equityChecksSearchHelp;
    populateEquityCheckInSearchHelp(); // New line added
}

private void populateEquityCheckInSearchHelp() {
    if (this.equityChecksSearchHelp != null) {
        this.equityChecksSearchHelp.setEquityCheck(this);
    }
}

  
    public EquityChecksSearchHelp getEquityChecksSearchHelp() {
        return equityChecksSearchHelp;
    }

    public Set<Attachment> getAttachmentsList () {
        return attachmentsList;
    }
    
    public void setAttachmentsList (Set<Attachment> attachmentsList) {
    this.attachmentsList = attachmentsList;
}

public Set<DomReference> getFieldStationList() {
    return fieldStationList;
}
public void setFieldStationList(Set<DomReference> fieldStationList) {
    this.fieldStationList = fieldStationList;
}
public Boolean getnotifiedRequesterOfReceivedStatus() {
    return notifiedRequesterOfReceivedStatus;
}
public void setnotifiedRequesterOfReceivedStatus(Boolean notifiedRequesterOfReceivedStatus) {
    this.notifiedRequesterOfReceivedStatus = notifiedRequesterOfReceivedStatus;
}
public Set<RefIdam> getCollaborationStakeholders() {
    return collaborationStakeholders;
}
public void setCollaborationStakeholders(Set<RefIdam> collaborationStakeholders) {
    this.collaborationStakeholders = collaborationStakeholders;
}
public Boolean getReferencedCore() {
    return referencedCore;
}
public void setReferencedCore(Boolean referencedCore) {
    this.referencedCore = referencedCore;
}
public Boolean getReferencedCrossStream() {
    return referencedCrossStream;
}
public void setReferencedCrossStream(Boolean referencedCrossStream) {
    this.referencedCrossStream = referencedCrossStream;
}
public Boolean getSentCollaborationEmail() {
    return sentCollaborationEmail;
}
public void setSentCollaborationEmail(Boolean sentCollaborationEmail) {
    this.sentCollaborationEmail = sentCollaborationEmail;
}
public Boolean getNotifiedRequesterOfSubmitted() {
    return notifiedRequesterOfSubmitted;
}
public void setNotifiedRequesterOfSubmitted(Boolean notifiedRequesterOfSubmitted) {
    this.notifiedRequesterOfSubmitted = notifiedRequesterOfSubmitted;
}
public Boolean getNotifiedRequester0fCompleted() {
    return notifiedRequester0fCompleted;
}
public void setNotifiedRequester0fCompleted(Boolean notifiedRequester0fCompleted) {
    this.notifiedRequester0fCompleted = notifiedRequester0fCompleted;
}

       public Long getVersion () {
    return version;
    }
    public void setVersion (Long version) {
    this.version = version;
   }
}