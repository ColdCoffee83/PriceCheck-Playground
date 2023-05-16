@Table (name = "equity checks_status_history")
public class EquityChecksStatusHistory {
@Id
@GeneratedValue (strategy = GenerationType.IDENTITY)
private Long id;
@ManyToOne
@JoinColumn (name = "EQUITY_CHECK_ID")
private EquityChecksModel equityCheck;
@Column (name = "STATUS")
private String status;
@Column (name = "TIMESTAMP")
private Date timestamp;
public EquityChecksStatusHistory() {
}
 public void setId(Long id) {
    this.id = id;
}
 public EquityChecksModel getEquityCheck() {
    return equityCheck;
}
 public void setEquityCheck (EquityChecksModel equityCheck) {
    this.equityCheck = equityCheck;
}
 public String getStatus() {
    return status;
}
 public void setStatus (String status) {
    this.status = status;
}
 public Date getTimestamp() {
    return timestamp;
}
 public void setTimestamp (Date timestamp) {
    this.timestamp = timestamp;
}
}