@Entity
@Audited
@Table (name = "equity_checks_search_help")
public class EquityChecksSearchHelp extends DomBaseModel {
     @JsonIgnore
    @OneToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "EQUITY CHECK NUMBER")
    private EquityChecksModel equityCheck;
     @Column (name = "KEYWORDS", columnDefinition = "text")
    private String keywords;
     @Column (name = "REQUESTING OFFICE", columnDefinition = "text")
    private String requestingOffice;
     public EquityChecksModel getEquityCheck() {
        return this.equityCheck;
    }
     public void setEquityCheck(EquityChecksModel equityCheck) {
        this.equityCheck = equityCheck;
    }
     public String getKeywords() {
        return keywords;
    }
     public void setKeywords (String keywords) {
        this.keywords = keywords;
    }
     public String getRequestingOffice() {
        return requestingOffice;
    }
     public void setRequestingOffice (String requestingOffice) {
        this.requestingOffice = requestingOffice;
    }
     @Override
    public String getAuditValue () {
        return id.toString();
    }
     @Override
    public String getLabel() {
        return "Requirement Search Help";
    }
 }