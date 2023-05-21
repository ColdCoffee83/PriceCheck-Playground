<%@taglib prefix="c" url="http://java.sun.com/jsp/jstl/core"%>
<div class="container-fluid">
  <form class="form-horizontal">
    <div class="form-group row">
      <label for="collabFirstNameSearch" class="col-sm-2 col-form-label">FirstName</label>
      <div class="com-sm-4">
        <input type="text" class="form-control personSearchField" id="collabFirstNameSearch" placeholder="firstName">
      </div>
      <label for="collabLastNameSearch" class="col-sm-2 col-form-label">Last Name</label>
      <div class="col-sm-4">
        <input type="text" class="form-control personSearchField" id="collabLastNameSearch" placeholder="Last Name">
      </div>
    </div>
    <div id="collaborationStakeholdersContainer">
      <div class="form-group" id="searchResultsContainer">
        <table id="searchCollaborationStakeholdersTbl" class="table table-bordered">
          <thead>
            <tr>
              <th>Name</th> 
              <th>Division/Office</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody></tbody>
        </table>
      </div>
      <div class="form-group" id="selectedStakeholdersContainer">
        <label for="collabStakeholdersList">Selected Collaboration Stakeholders:</label>
        <ul id="collabStakeholdersList" class="list-group"></ul>
        <button id="saveStakeholdersBnt" class="btn btn-primary float-right">Save</button>
      </div>
    </div>
  </form>
</div>
          
      