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
   
   
functions.updateSelectedTable(checkEquiIdValue, "CollaborationStakeholders").then(function(data) {
    savedCollaborationStakeholders = data;
});
functions.updateSelectedTable(checkEquiIdValue, "FieldStationPOCs").then(function(data) {
    savedFieldStationPOCs = data;  
});
functions.updateSelectedTable(checkEquiIdValue, "DeskOfficers").then(function(data) {
     savedDeskOfficers = data;
});

   
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

  functions.internalDueDateHandler(internlDueDateRec);
  functions.requestedDueDateHandler(requestedDueDateRec);
  functions.proposedEngagementDateHandler();
  functions.setupOfficePOC_SelfAssign();
  functions.setupActionPOC_SelfAssign();
  functions.setupActionPOC();
  functions.showHideElementsBasedOnStatus();
     
// handle clicking on add field station POC button
$(document).on("click", ".addFieldStationPOCBtn", () => {
  events.addBtnClickHandler(
    checkEquiIdValue,
    selectedFieldStationPOCs,
    savedFieldStationPOCs,
    'FieldStationPOCs'
  );
});

// handle clicking on add desk officers button
$(document).on("click", ".addDeskOfficerBtn", () => {
  events.addBtnClickHandler(
    checkEquiIdValue,
    selectedDeskOfficers,
    savedDeskOfficers,
    'DeskOfficers'
  );
});

// Handle collaboration stakeholder search
$(document).on("click", ".sendCollaborationEmailBtnSearch", () => {
  events.addBtnClickHandler(
    checkEquiIdValue,
    selectedCollaborationStakeholders,
    savedCollaborationStakeholders,
    'CollaborationStakeholders'
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