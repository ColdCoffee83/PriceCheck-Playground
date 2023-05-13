$(document).on('click', ".branchPOCsAndOfficersSearch", function(e) {
  // link variable for string to dialog controller
  var link = appContext + "/dialog/setBranchPOCsAndOfficers";
  dialogUtil.showInfo(link, "Select Branch POCs and Officers", function() {
    // .personSearchField keyup listener to refresh #searchBranchPOCsAndOfficersTbl datatable
    $(".personSearchField").keyup(function (e) {
      typeDelay(function () {
        $("#searchBranchPOCsAndOfficersTbl").DataTable().ajax.reload();
      }, 250);
    });

    // TODO: Implement the rest of the event listener logic
    // - define function to populate the selected POC list and selected Officer list from the saved POC list and saved Officer list, respectively
    // - define function to update the selected POC list and selected Officer list
    // - .personRemove event listener for when either the Remove Person is clicked or the "x" next to the person's name in either the POC list or the Officer list are clicked
    // - call the function to populate the selected POC list and selected Officer list
    // - call the function to update the selected POC list and selected Officer list
    // - define the searchBranchPOCsAndOfficersTbl Datatable with the specified requirements
    $("#searchBranchPOCsAndOfficersTbl").DataTable({
  // ... other DataTable settings ...
  createdRow: function(row, data, dataIndex) {
    var dropdownHtml = `
      <div class="dropdown">
        <button class="btn btn-primary btn-sm dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          Select
        </button>
        <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
          <a class="dropdown-item select-as-poc" href="#">Select as Branch POC</a>
          <a class="dropdown-item select-as-officer" href="#">Select as Branch Officer</a>
        </div>
      </div>
    `;
    $('td:eq(2)', row).html(dropdownHtml);
  }
});

    // - define an event handler for when the "Save" button is clicked with the specified requirements
  });
});
