<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="n" tagdir="/WEB-INF/tags/format" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://latte.com/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="field" tagdir="/WEB-INF/tags/fields" %>
<%@ taglib prefix="roField" tagdir="/WEB-INF/tags/fields/RO" %>

<link href="${pageContext.request.contextPath}/css/chosen/chosen.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/pages/event.css" rel="stylesheet"> 
<link href="${pageContext.request.contextPath}/css/pages/listPages.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/pages/equityChecks/equityCheck.css" rel="stylesheet">

<m:DomesticMasterPage>  
    <jsp:body>
        <script src="${pageContext.request.contextPath}/js/report/report.js"></script>
        <script src="${pageContext.request.contextPath}/js/jq/chosen/chosen.jquery.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/americano/pages/equityCheck/equityCheckList.js"></script>
        <script src="${pageContext.request.contextPath}/js/pages/listPages/equityChecklistPage.js"></script>
        <script src="${pageContext.request.contextPath}/js/util/dateUtil.js"></script>

        <div class="row noMinHeight">  
            <div class="cSec col-12 graphHead">  
                <h2>Equity Check Filters</h2>  
            </div> 
        </div>  

        <div class="row">
            <button id="hideFilters" class="hideFiltersBtn btn btn-primary">  
                Hide Filters  
            </button>
            <button id="resetFilters" type="button" title="Reset Filters">  
                Reset Filters  
            </button>
        </div>  

        <div class="row">
            <div id="filterSec" width="180px" class="csec">
                <div class="container-fluid">
                    <div class="form sdFrm filters" data-hpos="." data-hsec="2">  
                        <field:formInput  
                            id="equityNumSearch" type="input"  
                            inputCls="searchField equityCheck noLen" title="EQUI Number:"  
                        />  
                        <field:formList  
                            id="statusSearch" selectCls="searchField equityCheck" name="statusSearch"  
                            title="Status:"  
                        >
                            <option></option>  
                            <option value="Draft">Draft</option>  
                            <option value="Submitted">Submitted</option>  
                            <option value="Received">Received</option>  
                            <option value="Processing">Processing</option>  
                            <option value="In Coordination">In Coordination</option>  
                            <option value="Completed">Completed</option>
                        </field:formList>  
                        <field:formList  
                            id="requestingOfficeSearch" selectCls="searchSelect equityCheck" name="requestingOfficeSearch" title="Requesting Office:"  
                            >  
                            <option value="">&nbsp;</option>  
                            <c:forEach items="${requestingOffices}" var="itm">  
                                <option value="${itm}">${itm}</option>  
                            </c:forEach>  
                        </field:formList>  
                        <field:formList  
                            id="officePOCSearch" selectCls="searchField equityCheck" name="officePOCSearch" title="Office POC:"  
                        >  
                            <option value="">&nbsp;</option>  
                            <c:forEach items="${officePOCs}" var="itm">  
                                <option value="${itm}">${itm}</option>  
                            </c:forEach>  
                        </field:formList>  
                        <field:formList  
                            id="actionPOCSearch" selectCls="searchField equityCheck" name="actionPOCSearch" title="Action POC:"  
                        >  
                            <option value="">&nbsp;</option>  
                            <c:forEach items="${actionPOCs}" var="itm">  
                                <option value="${itm} ">${itm}</option>  
                            </c:forEach>  
                        </field:formList>  
                        <field:formMultiSelect  
                            id="stationSearch" name="stationSearch" title="Field Station(s):"  
                            divCls="searchField equityCheck"  
                        >  
                            <c:forEach items="${domFieldStations}" var="station">  
                                <option value="${station.displayValue}">${station.displayValue}</option>  
                            </c:forEach>  
                        </field:formMultiSelect>  
                        <field:formList  
                            id="organizationNameSearch" selectCls="searchField equityCheck" name="organizationNameSearch" title="Organization Name:"  
                        >  
                            <option value="">&nbsp;</option>  
                            <c:forEach items="${organizationNames}" var="itm">  
                                <option value="${itm}">${itm}</option>  
                            </c:forEach>  
                        </field:formList>
                    </div>
                </div>
            </div>
            <div id="tableSec_EquityCheckView" width="3372px" class="col cSec btm">  
                <div class="container-fluid">  
                    <div class="row">  
                        <div class="col">
                            <div class="container-fluid">
                                <h1>Equities</h1>  
                                <button id="exportEquityChecksBtn" class="btnBig pull-right" type="button"  
                                    title="Export to CSV"> Export 
                                </button>  
                                <div class="hSec" data-hpos="b" data-hsec="3">
                                    <div class="hContent">
                                        <h3>Equities Table</h3>  
                                        <span class="spanFld"  
                                        >This table displays the requested equity checks.</span> 
                                    <h3 class="spanFld">
                                            <span class="fa fa-th-list baTMxt"></span>
                                            - View the Equity Check Request
                                        </h3>
                                        <h3 class="spanFld">  
                                            <span class="fa fa-edit bgîxt"></span> 
                                            - Edit The Equity Check Request  
                                        </h3>  
                                        <h3 class="spanFld">  
                                            <span class="fa fa-trash bgTMxt"></span> - Remove The Equity Check Request </h3>  
                                    </div>                                      
                                    <table class="table table-bordered table-bordered-filtered data-table listTable selRowTb1 hov  
                                    id="equitiesList" title="Equities table" aria-label="This table displays all the Equity Check requests in Americano.  
                                    <thead>  
                                        <tr class="tblHeader" data-edit="S(canEdit)" data-delete="${canDelete)"  
                                        >  
                                            <th data-column="O" data-val="id" id-"eqSearchId"»  
                                            EQUI Number:  
                                            </th>  
                                            <th class="tblHeaderName" data-column="1" data-val="status"> Status:  
                                            </th>  
                                            <th data-column="2" data-val="insertTimestamp">Created Date:</th>  
                                            <th data-column="g" data-val "officePOC",  
                                            Office POC:  
                                            </th>  
                                            <th data-column="g" data-val-"requestingoffice",  
                                            Requesting Office </th>  
                                            <th data-column="5" data-val="requestedDueDate",  
                                            Requested Due Date:  
                                            </th>  
                                            <c:if test="s (canViewEquityCheckInternalDueDate) "s  
                                                <th data-column="6" data-vals"internalDueDate",  
                                                Internal Due Date:  
                                                </th> 
                                            </c:if>  
                                            <th data-column="" data-val="proposedEngagementDate"»  
                                            Proposed Engagement Date:  
                                            </th>  
                                            <th data-column="g" data-vals"fieldStationList"s  
                                            Field Station (s):  
                                            </th>  
                                            <th data-column="9" data-vals"organizationName",  
                                            Organization Name:  
                                            </th>  
                                            <th data-column="10' data-val="justification",  
                                            Justification:  
                                            </th>  
                                            <th data-column="11" data-val="requestType",  
                                            Request Type:  
                                            </th>  
                                            <th data-column="12" data-val="actionPOC">  
                                            Action POC:  
                                            </th>  
                                            <th data-column="13">Actions</th>  
                                        </tr>  
                                    </thead>  
                                    <tbody></tbody>  
                                </table>  
                            </div>  
                        </div>  
                    </div>  
                </div>  
            </div>  
        <div id="tableSec_OrganizationView" width="3372"> 
            <h1>Organization Names</h1>
            <table id="organizationView">
                <thead>  
                <tr>  
                    <th>Organization Name</th> 
                    <th># of Equity Check Requests</th>
                    <th>Equity Check IDs</th>  
                    <th>Collaboration Stakeholders</th>
                    <th>Actions</th> 
                </tr>
                </thead>
                <tbody>
                <% 
                    // Loop through organizations
                    for (Organization org : organizations) { 
                %>
                    <tr>
                    <td><%= org.getName() %></td>
                    <td><%= org.getEquityCheckRequests().size() %></td>
                    <td>
                        <% 
                        // Loop through equity check requests for org
                        for (EquityCheckRequest req : org.getEquityCheckRequests()) { 
                        %>
                        <a href="viewEquityCheck.jsp?id=<%= req.getId() %>"><%= req.getId() %></a><br>
                        <% } %>
                    </td>  
                    <td>
                        <% 
                        // Loop through stakeholders for org
                        for (Stakeholder stakeholder : org.getStakeholders()) { 
                        %>
                        <%= stakeholder.getName() %><br>
                        <% } %>
                    </td>
                    <td>
                        <button type="button" aria-label="Email All Collaboration Stakeholders">
                          <i class="fas fa-envelope"></i>
                        </button>
                        <button type="button" aria-label="Add Collaboration Stakeholders to All Equity Check Requests">
                          <i class="fas fa-user-plus"></i> 
                        </button>
                      </td>
                    </tr>
                <% } %>
                </tbody>
            </table>
            </div>                   
        </div>  
    </jsp:body>
</m:DomesticMasterPage> 
          
