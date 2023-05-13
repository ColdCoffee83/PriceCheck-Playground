import * as events from "./equityCheckCreateEvents.is";
// Function to add weekdays to a given date
export function addWeekdays (current, days) {
    // Create a new Date object from the current date
    var businessDay = new Date (current);
    // Get the day of the week
    var day_of_week = businessDay.getDay ();
    // If the day of the week is Saturday
    if (day_of_week == 6) {
        // Initialize a counter
        var count = 0;
        // If the number of days to add is greater than 0
        if (days > 0) {
            // Loop until the counter is equal to the number of days to add
            while (count == days) {
                // Add one day to the date
                businessDay.setDate (businessDay.getDate () + 1);
                // Get the day of the week
                day_of_week = businessDay.getDay ();
                // If the day of the week is not Sunday or Saturday
                if (day_of_week  !== 0 && day_of_week  !== 6)
                    // Increment the counter
                    count++;
            }
        }
        // Return the new date
        return businessDay;
    }
}

export function selectOfficePOC(data) {
    const checkEquiIdValue = parseInt($("#equild").val());
    $.post(
      appContext + "/ajax/americano/equityCheck/setOfficePOC/" + checkEquildValue,
      data,
      function(result) {
        if (result.success === true) {
          const poc = result.obj;
          $("#officePOCName").text(data.displayName);
          $("#officePOCOrg").text(data.organization);
          $("#officePOCSecurePhone span.orange").text(data.securePhone);
          $("#officePOCId").val(poc.id);
          dialogUtil.closeDialog();
        } else {
          dialogUtil.alert("Error adding Officer", result.message);
        }
      }
    );
  }

  export function selectActionPOC(data) {
  const checkEquiIdValue = parseInt($("#equityCheckId").val());
   $.ajax({
    url: appContext + "/ajax/americano/equityCheck/setActionPOC/" + checkEquildValue,
    type: "POST",
    data: data,
    success: function(result) {
      if (result.success === true) {
        const poc = result.obj;
        $("#personName").val(poc.displayName);
        $("#personId").val(poc.id);
        $("#personOrg").val(poc.organization);
        $("#personSecurePhone").val(poc.securePhone);
        dialogUtil.closeDialog();
      } else {
        dialogUtil.alert("Error adding Officer", result.message);
      }
    }
  });
   $("#peopleLookupTable").DataTable({
    columns: [
      { data: "displayName" },
      { data: "divOffice" },
      {
        data: function(row, type, val, meta) {
          return (
            '<button type="button" data-id="' + row.id +
            '" data-sphone="' + row.securePhone +
            '" data-displayname="' + row.displayName +
            '" data-org="' + row.divOffice +
            '" class="selPersonBtns btn btn-primary btn-small" title="Select ' +
            row.displayName + '">Select Person</button>'
          );
        }
      }
    ],
    ajax: {
      url: appContext + "/ajax/peopleLookup/name",
      type: "GET",
      data: function(d) {
        d.firstName = $("#firstName").val();
        d.lastName = $("#lastName").val();
      },
      dataType: "json"
    },
    drawCallback: function() {
      $(".selPersonBtns").unbind().click(function(e) {
        var inst = $(this);
        var id = inst.data("id");
        var org = inst.data("org");
        var name = inst.data("displayName");
        var sPhone = inst.data("sphone");
        var data = {
          displayName: name,
          id: id,
          organization: org,
          securePhone: sPhone
        };
        selectOfficePOC(data);
      });
    },
    dom: "pt",
    retrieve: true,
    pagelength: 5,
    paging: true,
    ordering: false
  });
}
  
  export function setupActionPOC() {
    const $actionPOCSearch = $(".actionPOCSearch");
    const $personSearchField = $(".personSearchField");
    $actionPOCSearch.on('click', function(e) {
    const link = appContext + "/dialog/setPOC";
    dialogUtil.showInfoDialog(link, "Set Action POC", function() {
        typeDelay(function() {
          $("#personTable").DataTable({
            columns: [
              { data: "displayName" },
              { data: "divOffice" },
              {
                data: function(row, type, val, meta) {
                  return (
                    '<button type="button" data-id="' + row.id +
                    '" data-sphone="' + row.securePhone +
                    '" data-displayname="' + row.displayName +
                    '" data-org="' + row.divOffice +
                    '" class="selPersonBtns btn btn-primary btn-small" title="Select ' +
                    row.displayName + '">Select Person</button>'
                  );
                }
              },
            ],
            ajax: {
              url: appContext + "/ajax/peopleLookup/name",
              type: "GET",
              data: function(d) {
                d.firstName = $personSearchField.val();
                d.lastName = $personSearchField.val();
              }
            },
            drawCallback: function() {
              $(".selPersonBtns").on('click', function(e) {
                const inst = $(this);
                const id = inst.data("id");
                const org = inst.data("org");
                const name = inst.data("displayName");
                const sPhone = inst.data("sphone");
                const data = {
                  displayName: name,
                  id: id,
                  organization: org,
                  securePhone: sPhone
                };
                selectActionPOC(data);
              });
            }
          },
          dom: "pt",
          retrieve: true,
          pagelength: 5,
          paging: true,
          ordering: false
        });
      });
    });
  }

  export function setupOfficePOC_SelfAssign() {
    $("#setOfficePOCSelf").unbind().click(function() {
      var name = $("#luName").val();
      var nameArr = name.split(" ");
      var firstName = nameArr[0];
      var lastName = nameArr[nameArr.length - 1].replace(/-y-/g, "'"); // remove "-y-" bit
      var data = {
        firstName: firstName,
        lastName: lastName
      };
      $.ajax({
        url: appContext + "/ajax/peopleLookup/name",
        type: "GET",
        data: data,
        success: function(result) {
          var toSend = {
            displayName: result.data[0].displayName,
            ain: result.data[0].ain,
            organization: result.data[0].divOffice,
            securePhone: result.data[0].securePhone
          };
          selectOfficePOC(toSend);
        },
        error: function(xhr, status, error) {
          console.error("Failed to set office poc:", status, error);
        }
      });
    });
  }

  export function setupActionPOC_SelfAssign() {
  $("#setActionPOC_Self").unbind().click(function() {
    var name = $("#luName").val();
    var nameArr = name.split(" ");
    var firstName = nameArr[0];
    var lastName = nameArr[nameArr.length - 1].replace(/-y-/g, ""); // remove "-y-" bit
    var data = {
      firstName: firstName,
      lastName: lastName
    };
    $.ajax({
      url: appContext + "/ajax/peopleLookup/name",
      type: "GET",
      data: data,
      success: function(result) {
        var toSend = {
          displayName: result.data[0].displayName,
          ain: result.data[0].ain,
          organization: result.data[0].divOffice,
          securePhone: result.data[0].securePhone
        };
        selectActionPOC(toSend);
      },
      error: function(xhr, status, error) {
        console.error("Failed to set action poc:", status, error);
      }
    });
  });
}

export function generateErrorMessage(invalidFields, actionPOCRequired) {
    var message = "Please fill out all required fields:<br>";
    invalidFields.forEach(function() {
      var fieldTitle = $(this).attr("title");
      if (fieldTitle) {
        fieldTitle = fieldTitle.replace(/:/g, "");
        message += "<br>" + fieldTitle;
      }
    });
    if (actionPOCRequired) {
      message += "<br>Action POC";
    }
    return message;
  }
  
  export function redirectToListLocation() {
    const basePath = $("#pageContext").val();
    const isAdmin = $("#userRole").data("is-admin") === true;
  
    if (isAdmin) {
      location.href = basePath + "/equity/";
    } else {
      location.href = basePath + "/equity/myrequests/";
    }
  }

  export function deleteDraft() {
    const draftId = $("#equityId").val();
    const isEmptyDraft = $("#isEmptyEquityCheck").val();
  
    if (isEmptyDraft) {
      const deleteFromList = false;
      $.ajax({
        type: "DELETE",
        url: appContext + "/equity/deleteDraft/" + draftId,
        data: JSON.stringify({ deleteFromList }),
        contentType: "application/json",
        success: function() {
          console.log("empty draft delete complete");
        },
        error: function(jqXHR, textStatus, errorThrown) {
          console.error("Failed to delete draft:", textStatus, errorThrown);
          alert("Failed to delete draft.");
          return false;
        }
      });
    }
  }
  export function showHideElementsBasedOnStatus() {
    const status = $("#status").val();
    const actionPOCInput = $("#actionPOCId");
  
    if (
      status === "Received" ||
      status === "Processing" ||
      status === "In Coordination" ||
      status === "Completed"
    ) {
      actionPOCInput.prop("required", true);
    } else {
      actionPOCInput.prop("required", false);
    }
  
    if (
      status === "Processing" ||
      status === "In Coordination" ||
      status === "Completed"
    ) {
      $("#searchCollaborationStakeholdersButton").show();
      $("#processingCheckboxes").show();
      $("#collabartionStakeholdersList").show();
      $("#sendCollaborationEmailBtn").show();
      $("#searchFieldStationPOCsAndDeskOfficersBtn").show();
      $("#fieldStationPOCsAndDeskOfficersList").show();
    } else {
      $("#searchCollaborationStakeholdersButton").hide();
      $("#processingCheckboxes").hide();
      $("#collabartionStakeholdersList").hide();
      $("#sendCollaborationEmailBtn").hide();
      $("#searchFieldStationPOCsAndDeskOfficersBtn").hide();
      $("#fieldStationPOCsAndDeskOfficersList").hide();
    }
  }
  export function updateSelectedCollaborationStakeholdersTable(equityCheckId) {
    return new Promise((resolve, reject) => {
      $.ajax({
        url: appContext + "/ajax/americano/equityCheck/getSelectedCollaborationStakeholders",
        type: "GET",
        data: { equityCheckId },
        dataType: "json",
        success: function(data) {
          const tableBody = $("#selectedCollaborationStakeholdersTable tbody");
          tableBody.empty();
          data.forEach((stakeholder) => {
            const [_, name, office] = stakeholder.split(",");
            const row = $("<tr>");
            const nameCell = $("<td>").text(name);
            const officeCell = $("<td>").text(office);
            row.append(nameCell, officeCell);
            tableBody.append(row);
          });
          resolve(data);
        },
        error: function(jqXHR, textStatus, errorThrown) {
          console.error(textStatus, errorThrown);
          reject(new Error("Failed to fetch collaboration stakeholders"));
        }
      });
    });
  }
  
export function isFormInInitialState (initialState) {
return initialState === $ ("#equityCheckCreateForm") .serialize ();
}

export function fetchCollaborationEmailStatus(checkEquiIdValue) {
    return new Promise((resolve, reject) => {
      $.ajax({
        url: appContext + "/ajax/americano/equityCheck/getCollaborationEmailStatus/" + checkEquiIdValue,
        type: "GET",
        dataType: "json",
        success: function(data) {
          if (data !== null) {
            resolve(data.status === "Sent");
          } else {
            console.warn("received null response when fetching collaboration email status");
            resolve(false);
          }
        },
        error: function(errorThrown) {
          console.error("Failed to fetch collaboration email status due to the following:", errorThrown);
          reject(errorThrown);
        }
      });
    });
  }
  
  export function populateSelectedListFromSavedList(selected, saved) {
    for (let s of saved) {
      const [ain, name, office] = s.split(",");
      const exists = selected.find((poc) => poc.ain === Number(ain));
      if (!exists) {
        selected.push({
          ain: Number(ain),
          displayName: name,
          org: office,
          email: `${s[0]}@cia.ic.gov`,
        });
      }
    }
    return selected;
  }
  
  export function updateSelectedStakeholdersList(selectedCollaborationStakeholders) {
    let list = $("#collabStakeholdersList");
    list.empty();
    for (const stakeholder of selectedCollaborationStakeholders) {
      const listItem = $("<li>", {
        class: "list-group-item d-flex align-items-center stakeholder-item",
      });
      listItem.text(stakeholder.displayName);
      listItem.attr("data-id", stakeholder.ain);
      const btRemove = $("<button>", {
        class: "btn btn-sm btn-danger ml-auto stakeholderRemoveBtn",
      });
      btRemove.text("x");
      btRemove.data("id", stakeholder.ain);
      btRemove.attr("title", `Remove ${stakeholder.displayName} from Selected Stakeholders`);
      btRemove.click((e) => {
        let stakeholderId = stakeholder.ain;
        selectedCollaborationStakeholders = events.stakeholderRemoveBtnClickHandler(
          selectedCollaborationStakeholders,
          stakeholderId,
        );
      });
      listItem.append(btRemove);
      list.append(listItem);
    }
  }
  
  export function sendEmailToCollaborationStakeholders(checkEquiIdValue, savedCollaborationStakeholders) {
    const stakeholderAINs = savedCollaborationStakeholders.map((stakeholder) => stakeholder.split(", ")[0]);
    $.ajax({
      type: "POST",
      url: appContext + "/ajax/americano/equityCheck/sendEmailToCollaborationStakeholders/" + checkEquiIdValue,
      contentType: "application/json",
      data: JSON.stringify(stakeholderAINs),
      success: function() {
        alert("Email sent successfully.");
        return true;
      },
      error: function(jqXHR, textStatus, errorThrown) {
        console.error("Error sending email:", textStatus, errorThrown);
        alert("An error occurred while sending the email. Please try again.");
        return false;
      }
    });
  }

  export function internalDueDateHandler(internalDueDateRec) {
    if (internalDueDateRec !== null) {
      if (internalDueDateRec !== "") {
        $("#internalDueDateFld").val(
          dateUtil.formatDateOnly(Number(internalDueDateRec))
        );
        $("#internalDueDateFld").datepicker({
          onSelect: function() {
            $("#ui-datepicker-div").css({
              top: $("#internalDueDateFld").offset().top + 30,
            });
          },
        });
        $("#fieldStationList").chosen({
          placeholder_text: "Select one or more field stations",
          width: "100%",
          search_contains: true,
        });
        $("#internalDueDateFld").change(function() {
          internalDueDateRec = $(this).val();
          if (internalDueDateRec !== "") {
            var date = new Date(internalDueDateRec).getTime();
            $("#internalDueDate").val(date);
          }
        });
      }
    }
  }

  export function requestedDueDateHandler(requestedDueDateRec) {
  if (requestedDueDateRec !== "") {
    $("#requestedDueDateFld").val(
      dateUtil.formatDateOnly(Number(requestedDueDateRec))
    );
    $("#requestedDueDateFld").datepicker({
      onSelect: function() {
        $("#ui-datepicker-div").css({
          top: $("#requestedDueDateFld").offset().top + 30,
        });
      },
    });
    $("#requestedDueDateFld").change(function() {
      const adjRequestedDueDateRec = $("#requestedDueDateFld").val();
      const sevenBusinessDays = addWeekdays(new Date(), 7);
      const date = new Date(adjRequestedDueDateRec).getTime();
      if (adjRequestedDueDateRec !== "") {
        $("#requestedDueDateFld").val(
          dateUtil.formatDateOnly(Number(date))
        );
        date < sevenBusinessDays ? $(".justifyDesc").show() : $(".justifyDesc").hide();
        $("#requestedDueDate").val(date);
      }
      if (!adjRequestedDueDateRec || date > sevenBusinessDays) {
        $(".justifyDesc").hide();
        document.getElementById("justificationDescription").required = false;
        document.getElementById("justificationDescription").value = "";
      } else {
        $(".justifyDesc").show();
        document.getElementById("justificationDescription").required = true;
      }
    });
    // call change event handler once when page loads to set correct state
    $("#requestedDueDateFld").trigger("change");
  }
}
export function proposedEngagementDateHandler() {
  var proposedEngagementDateRec = $("#proposedEngagementDate").val();
  if (proposedEngagementDateRec !== "") {
    $("#proposedEngagementDateFld").val(
      dateUtil.formatDateOnly(Number(proposedEngagementDateRec))
    );
    $("#proposedEngagementDateFld").datepicker({
      minDate: 0,
    });
    $("#proposedEngagementDateFld").on("click", function() {
      $("#ui-datepicker-div").css({
        top: $("#proposedEngagementDateFld").offset().top + 30,
      });
    });
    $("#proposedEngagementDateFld").change(function() {
      proposedEngagementDateRec = $(this).val();
      if (proposedEngagementDateRec !== "") {
        var date = new Date(proposedEngagementDateRec).getTime();
        $("#proposedEngagementDate").val(date);
      } else {
        var defaultDueDate = addWeekdays(new Date(), 10).getTime();
        $("#proposedEngagementDate").val(dateUtil.formatDateOnly(defaultDueDate));
      }
    });
  } else {
    var defaultDueDate = addWeekdays(new Date(), 10).getTime();
    $("#proposedEngagementDate").val(dateUtil.formatDateOnly(defaultDueDate));
  }
}
