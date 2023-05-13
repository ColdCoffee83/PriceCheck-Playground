<div class="container-fluid">
  <form class="form-horizontal">
    <div class="form-group row">
      <label class="col-sm-2 col-form-label" for="pocFirstNameSearch">First Name</label>
      <div class="col-sm-4">
        <input type="text" class="form-control personSearchField" id="pocFirstNameSearch" name="pocFirstNameSearch" placeholder="First Name">
      </div>
      <label class="col-sm-2 col-form-label" for="pocLastNameSearch">Last Name</label>
      <div class="col-sm-4">
        <input type="text" class="form-control personSearchField" id="pocLastNameSearch" name="pocLastNameSearch" placeholder="Last Name">
      </div>
    </div>
    <div id="searchBranchPOCsAndOfficersContainer">
      <div class="form-group" id="searchResultsContainer">
        <table id="searchBranchPOCsAndOfficersTbl" class="table table-bordered">
          <thead>
            <tr>
              <th>Name</th>
              <th>Office</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody></tbody>
        </table>
      </div>
      <div class="form-group" id="selectedBranchPOCsContainer">
        <label for="branchPOCsList">Selected Branch POCs</label>
        <ul id="branchPOCsList" class="list-group"></ul>
      </div>
      <div class="form-group" id="selectedOfficersContainer">
        <label for="officersList">Selected Branch Officers</label>
        <ul id="officersList" class="list-group"></ul>
      </div>
      <button id="saveBranchPOCsAndOfficersBtn" class="btn btn-primary float-right">Save</button>
    </div>
  </form>
</div>