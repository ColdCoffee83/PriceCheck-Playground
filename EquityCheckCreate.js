import * as events from "./equityCheckCreateEvents.js";
import * as functions from "./equityCheckCreateFunctions.js";
 $(document).ready(function() {
  document.title = "AMERICANO - Equity Check";
  const checkEquildValue = parseInt($("#checkEquildValue").val());
  let isNavigationWarningEnabled = true;
  let collaborationEmailSent = false;
  let savedCollaborationStakeholders = [];
  let selectedCollaborationStakeholders = [];
  let savedFieldStationPOCs = [];
  let selectedFieldStationPOCs = [];
  let savedDeskOfficers = [];
  let selectedDeskOfficers = [];
  let defaultInternalDueDate = new Date();
  defaultInternalDueDate.setDate(defaultInternalDueDate.getDate() + 7);
  let internlDueDateRec = $("#internalDueDate");
  let requestedDueDateRec = $("#requestedDueDate");
  let initialState = $("#initialState").val();
  let status = $("#status").val();
   if (status === "Draft") {
    $("#saveAsDraftBtn").show();
  } else if (status === "Submitted") {
    $("#submitBtn").show();
  }
   // Validate inputs
  if (!checkEquildValue) {
    console.error("Error: checkEquildValue is not valid");
    return;
  }
   if (!internlDueDateRec || !requestedDueDateRec || !initialState || !status) {
    console.error("Error: one or more required fields are missing");
    return;
  }
   events.bindEvents();
  functions.initForm();
  setSecChange();
  functions.showHideElementsBasedOnStatus();
   // Fetch saved collaboration stakeholders
  functions.updateSelectedCollaborationStakeholdersTbl(checkEquiIdValue).then(function(data) {
    savedCollaborationStakeholders = data;
  });
   // Check collaboration email status
  async function checkCollaborationEmailStatus() {
    try {
      collaborationEmailSent = await functions.fetchCollaborationEmailStatus(checkEquiIdValue);
    } catch (error) {
      console.error("Error checking collaboration email status:", error);
    }
    return collaborationEmailSent;
  }
  checkCollaborationEmailStatus();
   // Handle navigation bar click
  $(".navbar a").on("click", (e) => {
    isNavigationWarningEnabled = events.navbarClickHandler(
      initialState,
      isNavigationWarningEnabled
    );
  });
   // Handle cancel button click
  $("button.cancelBtn").on("click", () => {
    isNavigationWarningEnabled = events.cancelBtnClickHandler(
      initialState,
      isNavigationWarningEnabled
    );
  });
   // Handle save as draft button click
  $("button.saveAsDraftBtn").unbind().click(() => {
    isNavigationWarningEnabled = events.saveAsDraftBtnClickHandler();
  });
   // Handle submit button click
  $(".submitBtn").unbind().click(() => {
    isNavigationWarningEnabled = events.submitBtnClickHandler();
  });
   // Handle internal due date change
  functions.internalDueDateHandler(internlDueDateRec);
   // Handle requested due date change
  functions.requestedDueDateHandler(requestedDueDateRec);
   // Handle proposed engagement date change
  functions.proposedEngagementDatehandler();
   // Handle office POC self assign
  functions.setupOfficePOC_SelfAssign();
   // Handle action POC self assign
  functions.setupActionPOC_SelfAssign();
   // Handle office POC assign
  functions.setupOfficePOC();
   // Handle action POC assign
  functions.setupActionPOC();
   // Show/hide elements based on status
  functions.showHideElementsBasedOnStatus();
   // Handle field station POC and desk officer search
  $(document).on("click", ".fielsStationPOC_DeskOfficer_Search", () => {
    events.addFieldStationPOCsAndDeskOfficersBtnClickHandler(
      checkEquiIdValue,
      selectedFieldStationPOCs,
      savedFieldStationPOCs,
      selectedDeskOfficers,
      savedDeskOfficers
    );
  });
   // Handle collaboration stakeholder search
  $(document).on("click", ".sendCollaborationEmailBtnSearch", () => {
    events.addCollaborationStakeholdersBtnClickHandler(
      checkEquiIdValue,
      selectedCollaborationStakeholders,
      savedCollaborationStakeholders
    );
  });
   // Handle send collaboration email button click
  $(".sendCollaborationEmailBtn").unbind().click(() => {
    collaborationEmailSent = events.sendCollaborationEmailBtnClickHandler(
      collaborationEmailSent
    );
  });
   // Handle status history click
  $(document).on("click", ".statusHistoryBtn", () => {
    events.showStatusHistoryClickHandler(checkEquiIdValue);
  });
   // Delete draft on page unload
  window.onbeforeunload = function() {
    functions.deleteDraft();
  };
});