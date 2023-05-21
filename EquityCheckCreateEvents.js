import * as functions from "./equityCheckCreateFunctions.¡s";
$("#isDraft").prop("checked", true);
if ($("#justification").val() === "") {
  $("#justification").val("None");
}
if ($("#requestType").val() === "") {
  $("#requestType").val("None");
}
export function submitBtnClickHandler() {
    const form = $("#equityCheckCreateForm");
    const inst = $("button.submitBtn");
    inst.prop("disabled", true);
    const invalidFields = form.find(":invalid");
  
    // custom validation for action POC input
    const actionPOCIsRequired = $("#actionPOCId").prop("required");
    const actionPOCIsEmpty = !$("#actionPOCId").val();
  
    if (invalidFields.length > 0 || (actionPOCIsRequired && actionPOCIsEmpty)) {
      const errorMessage = functions.generateErrorMessage(
        invalidFields,
        actionPOCIsRequired && actionPOCIsEmpty
      );
      dialogUtil.alert("Validation Error", errorMessage, function() {
        const invalidField = invalidFields.first();
        invalidField.focus();
        invalidField.scrollIntoView();
      });
      inst.prop("disabled", false);
      return false;
    }
  
    // proceed to save as usual
    form.find("button[type='submit']").removeAttr("disabled").click();
    return false;
  }
  export function showStatusHistoryClickHandler(checkEquiIdValue) {
      const newOrder = ["Draft", "Submitted", "Received", "Processing", "In Coordination", "Completed"];

  $.ajax({
    type: "GET",
    url: appContext + "/ajax/americano/equityCheck/statusHistory",
    data: { equityCheckId: checkEquiIdValue },
    dataType: "json",
    
    success: function(data) {
      // Filter the entries based on the order we want.
      const ordered = data.filter(row => newOrder.includes(row.status));
      
      // Find the latest status based on the status groups
      const groups = ordered.reduce((acc, cur) => {
        let status = cur.status;
        if (acc[status]) {
          if (new Date(cur.date) >= new Date(acc[status].date)) {
            acc[status] = cur;
          }
          return acc;
        }
        acc[status] = cur;
        return acc;
      }, {});

      // Create an array of objects with 'status' and latest 'date' fields arranged in the desired order.
      const result = newOrder.map(status => {
        let entry = { status }
        if (groups[status]) {          
          
          entry.date = groups[status].timestamp;
        } else {
          entry.date = ""
        }
        return entry;
      })

      // Prepare the HTML to render the table.
      const html = `
        <div class="table-responsive table-container">
          <table class="table">
            <thead>
              <tr>
                <th>Status</th>
                <th>Date</th>
              </tr>
            </thead>
            <tbody>
              ${result.map(({status, date}) => `<tr><td>${status}</td><td>${date}</td></tr>`).join('\n')}
            </tbody>
          </table>
        </div>
      `;
      
      // Render the table using 'dialogUtil.showInfoNoForm' method
      dialogUtil.showInfoNoForm( "Status History", html, function() {
        var statusHistoryTable = $("#statusHistoryTbl");
        statusHistoryTable.DataTable({"dom": "t"});
          }
        );
      }
    });
  }
  
  export function navbarClickHandler(initialState, isNavigationWarningEnabled, e) {
    if (functions.isFormInInitialState(initialState) && !$("#isDraft").prop("checked")) {
      return true;
    }
    if (!isNavigationWarningEnabled) {
      return false;
    }
  
    if (confirm("Are you sure you want to leave? Any unsaved changes will be lost.")) {
      if ($("#isDraft").prop("checked")) {
        functions.deleteDraft();
        return false;
      } else {
        e.preventDefault();
        return true;
      }
    }
  }
  export function cancelBtnClickHandler(initialState, isNavigationWarningEnabled) {
    if (functions.isFormInInitialState(initialState) && !$("#isDraft").prop("checked")) {
      functions.redirectToListLocation();
      return false;
    }
  
    if (isNavigationWarningEnabled) {
      if (confirm("Are you sure you want to leave? Any unsaved changes will be lost.")) {
        let shouldRedirect = false;
        if ($("#isDraft").prop("checked")) {
          shouldRedirect = functions.deleteDraft();
        } else {
          shouldRedirect = true;
        }
  
        if (shouldRedirect) {
          functions.redirectToListLocation();
          return false;
        }
      }
    }
  
    return true;
  }
  export function savesDraftBtnClickHandler() {
    // disable required attribute for all fields
    const form = $("#equityCheckCreateForm");
    form.find("input[required]").prop("required", false);
  
    $.ajax({
      type: "POST",
      url: appContext + "/ajax/americano/equityCheck/saveDraft",
      data: form.serialize(),
      dataType: "json",
      success: function(data) {
        if (data.success) {
          dialogUtil.showInfoNoForm("Draft saved successfully", function() {
            // TODO: Handle close event
          });
        } else {
          alert("Failed to save draft");
        }
      },
      error: function() {
        alert("Failed to save draft");
      }
    });
  }
  export function sendCollaborationEmailBtnClickHandler(collaborationEmailSent) {
    let confirmationMessage = "Are you sure you want to send the collaboration email to the listed recipients?";
    if (collaborationEmailSent) {
      confirmationMessage = "You have already sent a collaboration email. Are you sure you want to send it again?";
    }
  
    if (confirm(confirmationMessage)) {
      return functions.sendEmailToCollaborationStakeholders();
    } else {
      return false;
    }
  }
  export function addFieldStationPOCsAndDeskOfficersBtnClickHandler(
    selectedFieldStationPOCs,
    savedFieldStationPOCs,
    selectedDeskOfficers,
    savedDeskOfficers,
  ) {
    const link = appContext + "/dialog/setFieldStationPOCsAndDeskOfficers";
    dialogUtil.showInfoNoForm(
      link,
      "Select Field Station POCs and Desk Officers",
      function() {
        $(".personSearchField").on("keyup", function(e) {
          typeDelay(function() {
            $("#searchFieldStationPOCsAndDeskOfficersTbl")
              .DataTable()
              .ajax.reload();
          }, 250);
        });
      },
    );
    selectedFieldStationPOCs = functions.populateSelectedListFromSavedList(
      selectedFieldStationPOCs,
      savedFieldStationPOCs,
    );
    selectedDeskOfficers = functions.populateSelectedListFromSavedList(
      selectedDeskOfficers,
      savedDeskOfficers,
    );
  
    // TODO: anything else in the event listener
  }
  export function addCollaborationStakeholdersBtnClickHandler(
    checkEquiIdValue,
    selectedCollaborationStakeholders,
    savedCollaborationStakeholders,
  ) {
    const link = appContext + "/dialog/setCollaborationStakeholders";
    dialogUtil.showInfoNoForm(
      link,
      "Select Collaboration Stakeholders",
      function() {
        $(".personSearchField").on("keyup", function(e) {
          typeDelay(function() {
            $("#searchCollaborationStakeholdersTbl")
              .DataTable()
              .ajax.reload();
          }, 250);
        });
      },
    );
    selectedCollaborationStakeholders = functions.populateSelectedListFromSavedList(
      selectedCollaborationStakeholders,
      savedCollaborationStakeholders,
    );
    $("#collabStakeholdersList").on("click", "stakeholderRemoveBtn", (e) => {
      selectedCollaborationStakeholders = stakeholderRemoveBtnClickHandler(
        selectedCollaborationStakeholders,
        null,
      );
    });
    selectedCollaborationStakeholders = functions.populateSelectedListFromSavedList(
      selectedCollaborationStakeholders,
      savedCollaborationStakeholders,
    );
    functions.updateSelectedStakeholdersList(selectedCollaborationStakeholders);
    selectedCollaborationStakeholders = setupSearchCollaborationStakeholdersDataTable(
      selectedCollaborationStakeholders,
    );
    $("#saveStakeholdersBtn").on("click", (e) => {
      savedCollaborationStakeholders = saveStakeholdersBtnClickHandler(
        checkEquiIdValue,
        selectedCollaborationStakeholders,
        savedCollaborationStakeholders,
        1400, // width of the info box
      );
    });
  }
  
  export function stakeholderRemoveBtnClickHandler(
    selectedCollaborationStakeholders,
    stakeholderId,
    e,
  ) {
    e.preventDefault();
  
    const ain = stakeholderId == null ? stakeholderId : $(this).data("id");
    const index = selectedCollaborationStakeholders.findIndex(
      item => item.ain === ain,
    );
  
    if (index !== -1) {
      selectedCollaborationStakeholders.splice(index, 1);
      functions.updateSelectedStakeholdersList();
  
      // update button text in DataTable
      const table = $("#searchCollaborationStakeholdersTbl").DataTable();
      table.rows().every(function (row, dx, tableLoop, rowLoop) {
        const rowData = this.data();
  
        if (parseInt(rowData["ain"]) === ain) {
          const button = this.nodes().to$().find("button");
          button.text("Select Person");
          return false; // stop loop once we've found it
        }
      });
    }
  
    return selectedCollaborationStakeholders;
  }
  
  function setupSearchCollaborationStakeholdersDataTable(
    selectedCollaborationStakeholders,
  ) {
    $("#searchCollaborationStakeholdersTbl").DataTable({
      columns: [
        { data: "displayName" },
        { data: "divOffice" },
        data: function (row, type, val, meta) {
          return (
            "<button type='button' data-id='" +
            row.ain +
            "' data-sphone='" +
            row.securePhone +
            "' data-displayname='" +
            row.displayName +
            "' data-org='" +
            row.divOffice +
            "' class='selPersonBtns btn btn-primary btn-small' title='Add " +
            row.displayName +
            " to Selected Stakeholders'>Select Person</button>"
          );
        },
        ajax: {
          url: appContext + "/ajax/peopleLookup/name",
          type: "GET",
          data: function (d) {
            d.firstName = $("#collabFirstNameSearch").val();
            d.lastName = $("#collabLastNameSearch").val();
          },
        },
        drawCallback: function () {
          // updates button text based on presence of stakeholder in selected list
          const table = $("#searchCollaborationStakeholdersTbl").DataTable();
          table.rows().every(function (rowIds, tableLoop, rowLoop) {
            const rowData = this.data();
            const ain = parseInt(rowData["ain"]);
  
            const index = selectedCollaborationStakeholders.findIndex(
              item => item.ain === ain,
            );
  
            const button = this.nodes().to$().find("button");
  
            if (index !== -1) {
              button.text("Remove Button");
            } else {
              button.text("Select Button");
            }
          });
        },
        paging: false,
        searching: false,
      });
  
      return selectedCollaborationStakeholders;
    }
  
  function saveStakeholdersBtnClickHandler(
    checkEquiIdValue,
    selectedCollaborationStakeholders,
    savedCollaborationStakeholders,
    e,
  ) {
    e.preventDefault();
  
    $.ajax({
      url: appContext + "/ajax/americano/equityCheck/saveCollaborationStakeholders/" + checkEquiIdValue,
      method: "POST",
      contentType: "application/json",
      data: JSON.stringify(selectedCollaborationStakeholders),
      success: function (response) {
        dialogUtil.closeDialog();
        functions.updateSelectedCollaborationStakeholders(checkEquiIdValue).then(
          function (data) {
            savedCollaborationStakeholders = data;
          },
        );
      },
      error: function (xhr, status, error) {
        console.error("Error while saving stakeholders: " + status + " " + error);
      },
    });
  
    return savedCollaborationStakeholders;
  }

// Event listener for "Select Field Station POCs" button
$('#searchFieldStationPOCsBtn').click(function() {
    // Open the Field Station POCs modal
    $('#fieldStationPOCsModal').modal('show');

    // Populate the modal with a list of people
    functions.populateModalWithPeople('fieldStationPOCsModal', 'selectedFieldStationPOCsTbl');
});

// Event listener for "Select Desk Officers" button
$('#searchDeskOfficersBtn').click(function() {
    // Open the Desk Officers modal
    $('#deskOfficersModal').modal('show');

    // Populate the modal with a list of people
    functions.populateModalWithPeople('deskOfficersModal', 'selectedDeskOfficersTbl');
});
  