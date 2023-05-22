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
  
  export function addBtnClickHandler(
  checkEquiIdValue,
  selectedItems,
  savedItems,
  context
) {
  let link = appContext + "/dialog/setUsers";
  let infoBoxTitle;
  let dataTableFunction;
  let widthOfInfoBox = 1400;
    
  switch (context) {
    case "FieldStationPOCs":
      infoBoxTitle = "Select Field Station POCs";
      // TODO: adapt setUpDataTable for Field Station POCs
      dataTableFunction = setupDataTable;
      break;
    case "DeskOfficers":
      infoBoxTitle = "Select Desk Officers";
      // TODO: adapt setUpDataTable for Desk Officers
      dataTableFunction = setupDataTable;
      break;
    case "CollaborationStakeholders":
      infoBoxTitle = "Select Collaboration Stakeholders";
      dataTableFunction = setupDataTable;      
      break;
  }

  dialogUtil.showInfoNoForm(link, infoBoxTitle, function() {
    $(".personSearchField").on("keyup", function(e) {
      typeDelay(function() {
        $("#searchCollaborationStakeholdersTbl")
          .DataTable()
          .ajax.reload();
      }, 250);
    });
  });

  selectedItems = functions.populateSelectedListFromSavedList(selectedItems, savedItems);
  $("#collabStakeholdersList").on("click", "stakeholderRemoveBtn", (e) => {
    selectedItems = stakeholderRemoveBtnClickHandler(selectedItems, null);
  });

  selectedItems = functions.populateSelectedListFromSavedList(selectedItems, savedItems);
  functions.updateSelectedStakeholdersList(selectedItems);
  selectedItems = dataTableFunction(selectedItems);
  $("#saveStakeholdersBtn").on("click", (e) => {
    savedItems = saveStakeholdersBtnClickHandler(checkEquiIdValue, selectedItems, savedItems, widthOfInfoBox);
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

  function setupDataTable(selectedItems, context) {
  let tableName, addBtnTitle, removeBtnTitle;
  switch (context) {
    case "FieldStationPOCs":
      tableName = "#searchFieldStationPOCsTbl";
      addBtnTitle = "Select FieldStationPOC";
      removeBtnTitle = "Remove FieldStationPOC";
      break;
    case "DeskOfficers":
      tableName = "#searchDeskOfficersTbl";
      addBtnTitle = "Select Desk Officer";
      removeBtnTitle = "Remove Desk Officer";
      break;
    case "CollaborationStakeholders":
      tableName = "#searchCollaborationStakeholdersTbl";
      addBtnTitle = "Select Person";
      removeBtnTitle = "Remove Person";
      break;
  }

  $(tableName).DataTable({
      columns: [
        { data: "displayName" },
        { data: "divOffice" },
        data: function(row, type, val, meta) {
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
        " to Selection'>" +
        addBtnTitle +
        "</button>"
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
          const table = $(tableName).DataTable();
          table.rows().every(function(rowIds, tableLoop, rowLoop) {
          const rowData = this.data();
          const ain = parseInt(rowData["ain"]);
  
          const index = selectedItems.findIndex(item => item.ain === ain);
  
          const button = this.nodes().to$().find("button");
  
          if (index !== -1) {
            button.text(removeBtnTitle);
          } else {
            button.text(addBtnTitle);
          }
        });
      },
      paging: false,
      searching: false,
      });
  
      return selectedItems;
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
