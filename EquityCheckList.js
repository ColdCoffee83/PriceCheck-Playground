$(document).ready(function() {
  try{
    document.title = "AMERICANO - Equitiest";
    UserPreferences.init();
    UserPreferences.completeInit();

    setLastViewPage();
    $("#exportEquityChecksBtn").unbind.click(function(){
      classification.clear();
      classification.setClassifiedBy($("#ain").val());
      launchAcgWindow(appContext + "/classification/");
    });

    let equityCheckList;

    if (location.href.includes("myrequests")) {
      equityCheckList = new EquityCheckListTable({
        url: appContext + "/ajax/americano/equityCheck/loadMyRequests",
        type: "equity/myrequests",
        viewFunc: setupViewFunc,
      }, "#equitiesMyRequestsList");
    } else { 
      equityCheckList = new EquityCheckListTable({
        url: appContext + "/ajax/americano/equityCheck/load",
        type: "equity",
        viewFunc: setupViewFunc,
      }, "#equitiesList");
    }

    $("#equityNumSearch").data("field", "equiNumber");
    $("#statusSearch").data("field", "status");
    $("#requestingOfficeSearch").data("field", "requestingOffice");
    $("#officePOCSearch").data("field", "officePOC");
    $("#actionPOCSearch").data("field", "actionPOC");
    $("#stationSearch").data("field", "fieldStation");
    $("#stationSearch").chosen({
      placeholder_text_single: "Select Station",
      width: "100%",
      search_contains: true,
      no_results_text: "No Stations Found",
      allow_single_deselect: true
    });
    $("#organizationNameSearch").data("field", "organizationName");

    function resetAndReinitializeChosen(selector) {
      $(selector).val("").trigger("chosen:updated");
      $(selector).chosen({
        placeholder_text_single: " ",
        width: "100%",
        search_contains: true,
        no_results_text: "No Stations Found",
        allow_single_deselect: true
      });
    }

    $("#resetFilters").unbind().click(function (e) {
      e.preventDefault();
      resetAndReinitializeChosen("#requestingOfficeSearch");
      resetAndReinitializeChosen("#officePOCSearch");
      resetAndReinitializeChosen("#actionPOCSearch");
      resetAndReinitializeChosen("#stationSearch");
      resetAndReinitializeChosen("#organizationNameSearch");
      equityCheckList.resetFilters();
      equityCheckList.reload();

      // Reset filters
      $("#equityNumSearch").val("");
      $("#statusSearch").val("");
      $("#requestingOfficeSearch").val("");
      $("#officePOCSearch").val("");
      $("#actionPOCSearch").val("");
      $("#stationSearch").val("");
      $("#organizationNameSearch").val("");
      $(".listTable").DataTable().ajax.reload();      
    });

    $(".hideFiltersBtn").unbind().click(function () {
      const filterSec = $("#filterSec");
      const tableSec = $("#tableSec");
      const resetFilterBtn = $("#resetFilters");

      if (filterSec.is(":hidden")) {
        filterSec.show();
        resetFilterBtn.show();
        tableSec.removeClass("col-12");
        $(this).text("Hide Filters");
      } else {
        filterSec.hide();
        resetFilterBtn.hide();
        tableSec.addClass("col-12");
        $(this).text("Show Filters");
      }          
    });

    LClassify.init(function (xml, favoriteName) {
      classification.setClassificationXML(xml);
      submitFORM(xml);
    });
  } catch (e) {
    console.error(e);
  }      
});  

function submitFORM(xml) {
  const block = classification.getBlock();
  let classBlock = {
    classificationXML: xml,
  };
  let params = {
    classBy: classification.getClassifiedBy(),
    id: $("#equityNumSearch").val(),
    status: $("#statusSearch").val(),
    requestingOffice: $("#requestingOfficeSearch").val(),
    officePOC: $("#officePOCSearch").val(),
    actionPOC: $("#actionPOCSearch").val(),      
  };
  if (block != null) {
    params.classFrom = block.getDrvFrom();
    params.classOn = block.getDeclOn();
  }
  if (location.href.includes("myrequests")) {
    downloadReportNew("/ajax/excelReport/equity/myEquityList", classBlock, params);
  } else {
    downloadReportNew("/ajax/excelReport/equity/equityList", classBlock, params);
  }
}

$('#toggleView').on('click', function() {
    $('#tableSec_EquityCheckView').toggle();
    $('#tableSec_OrganizationView').toggle();
  });
  