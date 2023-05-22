<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 <!-- Tag libraries -->
<%@taglib prefix="m" tagdir="/WEB-INF/tags/americano"%>
<%@taglib prefix="n" tagdir="/WEB-INF/tags/format"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://latte.com/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="field" tagdir="/WEB- INF/tags/fields"%>
 <!-- CSS -->
<link href="${pageContext.request. contextPath}/css/chosen/chosen.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/pages/equityChecks/equityCheckCreate.css" rel="stylesheet">
 <!-- Master page -->
<m:DomesticMasterPage>
  <jsp:body>
 <!-- JavaScript -->
<script src="${pageContext.request.contextPath}/js/americano/pages/equityCheck/equityCheckCreate.js" type="module"></script>
<script src="${pageContext.request.contextPath}/js/parts/americanoComments.js"></script>
<script src="${pageContext.request.contextPath}/js/jq/chosen/chosen.jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/fields/multiFileUpload.js"></script>
<script src="${pageContext.request.contextPath}/js/util/dateUtil.js"></script>
<script src="$(pageContext.request.contextPath}/js/parts/linkList.js"></script>
<script src="${pageContext.request.contextPath}/js/parts/validation.js"></script>
 
<input type="hidden" id="americanoEquityCheck" class="pageType" value="${americanoEquityCheck}" />
<input type="hidden" id="userRole" data-is-admin="${canViewEquityCheckInternalDueDate}" />
<form id ="equityCheckCreateForm" action="${pageContext.request.contextPath}/equity/save” method="post" name="equityCheck">
  <input type="checkbox" class="hidden" id="isDraft" name="isDraft">
  <input type="hidden" id="isEmptyEquityCheck" value="${equityCheck.isEmptyEquityCheck}" />
  <button type="submit" class="hidden"></button>
  <div class="row">
    <div class="cSec btm col-12 fstSec">
      <input type="hidden" id="equiId" class="parentId" name="id" value="${equityCheck.id}" />
      <input type="hidden" id="equiNumber" class="parentId" name="equiNumber" value="${equityCheck.equiNumber}" />
       <div>
        <c:if test="${equityCheck.officePOC == null}">
          <n:sectionHeader title="Create <span class='grn'>S{equityCheck.equiNumber}</span>"/>
        </ecrit>
        <c:if test="${equityCheck.officePOC != null}">          
          <n:sectionHeader title="Edit <span class='grn'>${equityCheck.equiNumber}</span>"/>
        </ol>
        <div class="row">
          <div class="col-sm-12 text-right">
            <c:if test="${equityCheck.status == 'Draft'}">
              <button type="button" class="btn btn-default saveAsDraftBtn" title="SaveAsDraft">Save as Draft</button>
            <lesit>
            <button type="button" class="btn btn-default submitBtn" title="Create">${equityCheckOperation}</button>
            <button type="button" class="btn btn-default red cancelBtn">Cancel</button>
          </div>
        </div>
        <n:sectionHeader title="Office POC & Requesting Office"/>         
        <div class="row">
          <div class="col-3">
            <div class="container-fluid">
              <input type="hidden" id="officePOCId" name="officePOCId" value="${equityCheck.officePOC.id}"/>
              <span id="officePOCName" class="spanFld">${equityCheck.officePOC.displayName}</span>
            </div>
          </div>
          <div class="col-3">
            <div class="container-fluid">
              <input type="hidden" id="requestingOfficeId" name="requestingOfficeId" value="${equityCheck.requestingOffice.id}"/>
              <span id="requestingOfficeName" class="spanFld">${equityCheck.requestingOffice.displayName}</span>
            </div>
          </div>
        </div>
  
<div class="row">
  <div class="col-3">
    <div class="container-fluid">
      <span id="officePOCOrg" class="spanFld grn">${equityCheck.officePOC.organization}</span>
      <span id="officePOCSecurePhone" class="spanFld">Secure Phone: <span class="orange">${equityCheck.officePOC.securePhone}</span></span>
      <button id='setOfficePOC Self' type='button' class='btn btn-primary'>Assign Self as Office POC</button>
      <button id='setOfficePOC' type='button' class='btn btn-primary officePOCSearch'>Lookup Office POC</button>
    </div>
  </div>
  <div class="col-3">
    <div class="container-fluid">
      <field: formInput id="requestingOffice" type="input" name="requestingOffice" value="${equityCheck.requestingOffice}" title="Requesting Office:" maxlength="256" required="true"/>
      <button id='officeMyGroup' type='button' class='btn btn-primary'>Select My Group</button>
    </div>
  </div>
</div>
<n:sectionHeader title="Company/Academic Institution Information"/>
<div class="row">
  <div class="col-3">
    <div class="container-fluid">
      <field: formInput id="organizationName" type="input" name="organizationName" value="${equityCheck.organizationName}" title="Organization Name:" maxlength="256" required="true"/>
    </div>
  </div>
</div>
  
type="input" name="organizationName"
value="${equityCheck.organizationName}"
title="Organization Name:" maxlength="256"
required="true"/>
<div class="row">
    <div class="col-3">
        <div class="container-fluid">
            <field: formInput 
                id="organizationUSAddress"
                type="input" 
                name="organizationUSAddress"
                value="${equityCheck.organizationUSAddress}"
                title="US Address:" 
                maxlength="256"
                required="true"
            />
        </div>
    </div>
    <div class="col-3">
        <div class="container-fluid">
            <field: formInput 
                id="organizationCity"
                type="input" 
                name="organizationCity"
                value="${equityCheck.organizationCity}"
                title="City:" 
                maxlength="256" 
                required="true"
            />
        </div>
    </div>
    <div class="col-3">
        <div class="container-fluid">
            <field: formInput 
                id="organizationState"
                type="input" 
                name="organizationState"
                value="${equityCheck.organizationState}"
                title="State:" 
                maxlength="256" 
                required="true"
            />
        </div>
    </div>
    <div class="col-3">
        <div class="container-fluid">
            <field: formInput 
                id="organizationPOC"
                type="input" 
                name="organizationPOC"
                value="${equityCheck.organizationPOC}"
                title="Point of Contact:" 
                maxlength="256"
                required="true"
            />
        </div>
    </div>
</div>
  <field:formInput id="organizationPOC" type="input" name="organizationPOC" value="${equityCheck.organizationPOC}" title="Organization POC:" maxlength="256" required="true"/>
 <field:formInput id="email" type="email" name="email" value="${equityCheck.email}" title="Email:" maxlength="256" required="true"/>
 <field:formInput id="phone" type="input" name="phone" value="${equityCheck.phone}" title="Phone:" maxlength="12" pattern="\d{3}-\d{4}1\d{3}-\d{3}-\d{4}" placeholder="xxx-xxx-xxxx"/>
 <n:sectionHeader title="Request Details"/>
 <div class="row">
    <div class="col-3">
        <div class="container-fluid">
            <field:formList id="justification" name="justification” title="Justification:" required="true">
                <option></option>
                <option value="Operational" <c:if test="${equityCheck.justification == 'Operational'}">selected</c:if>>Operational</option>
                <option value="Outreach" <c:if test="${equityCheck.justification == 'Outreach'}">selected</c:if>>Outreach</option>
                <option value="Procurement" <c:if test="${equityCheck.justification == 'Procurement'}">selected</c:if>>Procurement</option>
            </field:formList>
        </div>
    </div>
    <div class="col-3"></div>
</div>
  
<field: formDate id="requestedDueDate" fieldId="requestedDueDateFld" name="requestedDueDate" pattern="\d{2}/\d{2}/\d{4}" value="${equityCheck.requestedDueDate}" calClass="dlDdF1ld" label="Requested Due Date:" required="true"/>
 <div class="justifyDesc container-fluid">
  <field: formTextArea id="justificationDescription" name="justificationDescription" value="${equityCheck.justificationDescription}" title="Justification Description:" maxlength="255" required="true"/>
  <span class="spanFld red">You must provide a justification description for any equity check request within the next seven business days.</span>
</div>
  <div class="container-fluid">
  <field:formList id="requestType" name="requestType" title="Request Type:" required="true">
    <option></option>
    <option value="Traditional" <c:if test="S${equityCheck.requestType == 'Traditional'}">selected</c:if>>Traditional</option>
    <option value="Executive" <c:if test="S${equityCheck.requestType == 'Executive'}">selected</c:if>>Executive</option>
  </field:formList>
  <!-- Anticipated Highest Security Level -->
  <field:formList id="anticipatedHighestSecurityLevel" name="anticipatedHighestSecurityLevel" title="Highest Classification:" required="true">
    <option></option>
    <option value="TS" <c:if test="${equityCheck.anticipatedHighestSecurityLevel == 'TS'}">selected</c:if>>TS</option>
    <option value="S" <c:if test="${equityCheck.anticipatedHighestSecurityLevel == 'S'}">selected</c:if>>S</option>
    <option value="C" <c:if test="${equityCheck.anticipatedHighestSecurityLevel == 'C'}">selected</c:if>>C</option>
    <option value="SC-0" <c:if test="${equityCheck.anticipatedHighestSecurityLevel == 'SC-0'}">selected</c:if>>SC-0</option>
    <option value="SC-1" <c:if test="${equityCheck.anticipatedHighestSecurityLevel == 'SC-1'}">selected</c:if>>SC-1</option>
    <option value="SC-2" <c:if test="${equityCheck.anticipatedHighestSecurityLevel == 'SC-2'}">selected</c:if>>SC-2</option>
  </field:formList>
  <!-- Proposed Engagement Date -->
  <field:formDate id="proposedEngagementDate" fieldId="proposedEngagementDateFld" name="proposedEngagementDate" pattern="\d{2}/\d{2}/\d{4}" calClass="dlDdFld" label="Proposed Engagement Date:" value="${equityCheck.proposedEngagementDate}" required="true"/>
   <!-- Internal Due Date -->
  <c:if test="${canViewEquityCheckInternalDueDate}">
    <div class="col-3">
      <div class="container-fluid">
        <field:formDate id="internalDueDate" fieldId="internalDueDateFld" name="internalDueDate" pattern="\d{2}/\d{2}/\d{4}" label="Internal Due Date:" value="${equityCheck.internalDueDate}" required="true"/>
        <input type="hidden" id="actionPOCId" name="actionPOCId" value="${equityCheck.actionPOC.id}"/>
        <label for="actionPOCName" style="font-weight: bold">Action actionPOCSearch' 2958 <button id='setActionPOC_Clear' type='button' class='btn btn-primary'>Clear</button> 9 </div> 60 </div>
    </div>
  </c:if>
</div>
  
<div class="col-3">
    <field: formList id="status" name="status" title="Status:" disabled="${!canViewEquityCheckInternalDueDate}">
        <option></option>
        <option value="Draft"<c:if test="S{equityCheck.status == null || equityCheck.status == 'Draft'}">selected</c:if>>Draft</option>
        <option value="Submitted"<c:if test="${equityCheck.status == 'Submitted'}">selected</c:if>>Submitted</option>
        <option value="Received"<c:if test="${equityCheck.status == 'Received'}">selected</c:if>>Received</option>
        <option value="Processing"<c:if test="${equityCheck.status == 'Processing'}">selected</c:if>>Processing</option>
        <option value="In Coordination"<c:if test="${equityCheck.status == 'In Coordination'}">selected</c:if>>In Coordination</option>
        <option value="Completed"<c:if test="${equityCheck.status == 'Completed'}">selected</c:if>>Completed</option>
    </field:formList>
    <button id="showStatusHistory" type="button" class="btn btn-primary">Status History</button>
</div>
 <div class="row">
    <div class="col-6">
        <div class="container-fluid">
            <field:formMultiSelect id="fieldStationList" name="fieldStationList" title="Field Station(s):" required="true">
                <option></option>
                <!-- Loop through the list of field stations -->
                <c:forEach items="${fieldStations}" var="fieldStation">
                    <option value="${fieldStation.id}"<c:if test="${fieldStation.id == equityCheck.fieldStationId}">selected</c:if>>${fieldStation.name}</option>
                </c:forEach>
            </field:formMultiSelect>
        </div>
    </div>
    <c:if test="${canViewEquityCheckInternalDueDate}">
        <div class="col-6">
            <div class="container-fluid">
                <div id="processingCheckboxes" style="display: none;">
                    <field: formCheckBox id="referencedCrossStream" name="referencedCrossStream" title="Referenced Cross Stream?" checked="${equityCheck.referencedCrossStream}"/>
                    <field: formCheckBox id="referencedCore" name="referencedCore" title="Referenced Core?" checked="${equityCheck.referencedCore}"/>
                </div>
            </div>
        </div>
    </c:if>
</div>
  <button 
    id="searchCollaborationStakeholdersButton" 
    type="button" 
    class="btn btn-primary sendCollaborationEmailBtnSearch" 
    style="display: none;">
    Select Collaboration Stakeholders
</button>
<div 
    id="collabartionStakeholdersList" 
    class="form-group" 
    style="display: none;">
    <table 
        id="selectedCollaborationStakeholdersTbl" 
        class="table table-bordered">
        <thead>
            <tr>
                <th>Name</th>
                <th>Divison/Office</th>
            </tr>
        </thead>
        <tbody></tbody>
    </table>
</div>
<div class="form-group">
    <button 
        id="sendCollaborationEmailBtn" 
        type="button" 
        class="btn btn-primary" 
        style="display: none;">
        Send Collaboration Email to These Recipients
    </button>
</div>
<div class="row">
    <div class="col-6">
        <div class="container-fluid">
            <field: formInput 
                id="priorCompanyContactk" 
                type="input" 
                name="priorCompanyContac pPoc" 
                value="${equityCheck.priorCompanyContactPOC}" 
                title="With Whom Did You Speak?:" 
                maxlength="256"/>
        </div>
    </div>
    <c:if test="${canViewEquityCheckInternalDueDate}">
        <button id="searchFieldStationPOCsButton" type="button" class="btn btn-primary">Select Field Station POCs</button>
        <button id="searchDeskOfficersButton" type="button" class="btn btn-primary">Select Desk Officers</button>

        <div id="fieldStationPOCsList" class="form-group" style="display: none;">
    <table id="selectedFieldStationPOCsTbl" class="table table-bordered">
        <thead>
            <tr>
                <th>Name</th>
                <th>Divison/Office</th>
            </tr>
        </thead>
        <tbody></tbody>
    </table>
</div>
<div id="deskOfficersList" class="form-group" style="display: none;">
    <table id="selectedDeskOfficersTbl" class="table table-bordered">
        <thead>
            <tr>
                <th>Name</th>
                <th>Divison/Office</th>
            </tr>
        </thead>
        <tbody></tbody>
    </table>
</div>

    </c:if>
</div>
<div class="row">
    <div class="col-6">
        <div class="container-fluid">
            <field: formInput 
                id="howWasContactMade" 
                type="input" 
                name="howWasContactMade" 
                value="${equityCheck.howWasContactMade}" 
                title="How was contact made?:" 
                placeholder="e.g.phone call, email, visit" 
                maxlength="256"/>
        </div>
    </div>
</div>
<div class="row">
    <div class="col-6">
        <div class="container-fluid">
            <field: formTextArea 
                id="engagementDescription" 
                name="engagementDescription" 
                maxlength="3000" 
                value="${equityCheck.engagementDescription}" 
                title="Brief Description of Engagement: ">
                <label>TESTING</label>
            </field: formTextArea>
        </div>
    </div>
</div>
<div class="row">
    <div class="col-6">
        <div class="container-fluid">
            <!-- Open Source Research Conducted? -->
            <field:formCheckBox 
                id="openSourceResearchConducted" 
                title="Open Source Research Conducted?" 
                name="openSourceResearchConducted" 
                checked="${equityCheck.openSourceResearchConducted}"
            />
            <!-- If yes, attach the research -->
            <div id="attachResearch" class="container-fluid margin-top-20 form-group">
                <label for="attachmentsUpload">Open Source Research Attachment (s):</label>
                <field:multiFileUpload 
                    parentId="${equityCheck.id}" 
                    type="equity" 
                    label="Attachment (s)" 
                    fieldName="equityOpenSourceAttch" 
                    bannerReminder="true" 
                    singleFile="false"
                >
                    <c:forEach items="${equityCheck.attachmentsList}" var="file">
                        <div>
                            <a href="${pageContext.request.contextPath}/ajax/attachment/equity/download/${equityCheck.id}/${file.id}" title="Download the file ${file.fileName}">${file.fileName}</a>
                            <a class="remove-attachment" href="${file.fileName}" data-id="${file.id}"><span class="fa fa-times" aria-hidden="true"></span></a>
                        </div>
                    </c:forEach>
                </field:multiFileUpload>
            </div>
        </div>
    </div>
</div>
<div class="row">
    <div class="col-12">
        <div class="container-fluid">
            <!-- First time contact by Requestor/Originating Office? -->
            <field:formCheckBox 
                id="firstTimeContactByRequestor" 
                title="First time contact by Requestor/Originating Office?" 
                name="firstTimeContactByRequestor" 
                checked="${equityCheck.firstTimeContactByRequestor}"
            />
        </div>
    </div>
</div>
<div class="row">
    <div class="col-12">
        <div class="container-fluid">
            <!-- Has there been contact with the company prior to this request? -->
            <field:formCheckBox 
                id="priorCompanyContact" 
                title="Has there been contact with the company prior to this request?" 
                name="priorCompanyContact" 
                checked="${equityCheck.priorCompanyContact}"
            />
        </div>
    </div>
</div>
  <div class="row">
    <div class="col-6">
        <div class="container-fluid">
            <!-- Text area field for comments -->
            <field: formTextArea id="comments"
            name="comments" title="Comments:"
            maxlength="3000" value="${equityCheck.comments}"
            required="false"/>
        </div>
    </div>
</div>
 <!-- Row containing buttons to save, submit and cancel -->
<div class="row">
    <div class="col-sm-12 text-right">
        <!-- Conditional statement to check if Easter wand LyChspkopeyation is 'Create' -->
        if Easter {wand LyChspkopeyation == 'Create'}">
            <button type="button" class="btn btn-default
            saveAsDraftBtn" title="SaveAsDraft">Save as
            Draft</button>
        </esies
        <!-- Button to submit -->
        <button type="button" class="btn btn-default
        submitBtn"
        title="Create">${equityCheckOperation}</button>
        <!-- Button to cancel -->
        <button type="button" class="btn btn-default red
        cancelBtn">Cancel</button>
    </div>
</div>

  