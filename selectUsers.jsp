<%@taglib prefix="c" url="http://java.sun.com/jsp/jstl/core"%>
<div class="container-fluid">
  <form class="form-horizontal">
    <div class="form-group row">
      <label for="firstNameSearch" class="col-sm-2 col-form-label">FirstName</label>
      <div class="com-sm-4">
        <input type="text" class="form-control personSearchField" id="firstNameSearch" placeholder="firstName">
      </div>
      <label for="lastNameSearch" class="col-sm-2 col-form-label">Last Name</label>
      <div class="col-sm-4">
        <input type="text" class="form-control personSearchField" id="lastNameSearch" placeholder="Last Name">
      </div>
      
      <div id="usersContainer">
      <div class="form-group" id="searchResultsContainer">
        <table id="searchUsersTbl" class="table table-bordered">
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
        label for="usersList">Selected Users:</label>
  <ul id="usersList" class="list-group"></ul>
  <button id="saveUsersBtn" class="btn btn-primary float-right">Save</button>
      </div>
    </div>
  </form>
</div>
          
      