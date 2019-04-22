<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html lang="en" style="height:100%;">
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">    
    <meta charset="ISO-8859-1">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <!-- Homebrew CSS" -->
    
    <!-- Import java data structures -->
	<%@page import="java.util.*" %>
	<%@page import="data.*"%>
  	<%
  	// Set the session attribute to indicate that this page was last seen
    request.getSession().setAttribute("resultsOrList", "list");
  	// Name of the list
    String listName = "";
  	// List name value pull from servlet
    String listID = (String) request.getAttribute("listName");
    // If value doesn't exist, we shouldn't be here on this page
    if(listID == null){
    	listName = "error";
    }
    else{
    	listName = listID;
    }
	// Get the List that is needed from servlet
    UserList lists = (UserList) request.getAttribute("listVal");
    ArrayList<Restaurant> restaurantArr = null;
    ArrayList<Recipe> recipeArr = null;
    ArrayList<ListItem> managementList = null;
    // Check if the list exists
    if(lists != null){ // If it does, get the two different lists from it
    	  restaurantArr = lists.getRestaurants();
        recipeArr = lists.getRecipes();
        managementList = lists.getLists();
    }
    else{
    	managementList = new ArrayList<ListItem>();
    }
    
    // Get pagination index start
    int index = 0;
    if(request.getParameter("page") != null){
    	index = Integer.parseInt((String) request.getParameter("page"));
    }
  %>

    <!-- Title -->
    <title>List Management</title>
  </head>
  <body style="height:100%; background-color:whitesmoke; background-image: url('http://localhost:8080/FeedMe/images/breadBoard.jpg'); background-repeat: no-repeat; background-size: cover; background-position: center center;">

	<!-- Holds all the buttons -->
	<ul class="navbar mb-5">	  
	  <li>
	    <img alt="navPic" class="linkToSite" src="http://localhost:8080/FeedMe/images/navPic.png">
	  </li>
	  <li>
	    <!-- Takes user to the search page -->
        <form action ="/FeedMe/jsp/search.jsp">
      	  <button class="btn btn-primary linkToSite" id="returnToSearch" onclick="javascript:location.href = this.value;">Return to Search</button>
        </form>
	  </li>
	
	  <li>
	    <!-- Takes the user to the results page -->
	    <form action ="/FeedMe/results">
          <button class="btn btn-primary linkToSite" id="backToResults" onclick="javascript:location.href = this.value;">Return to Results</button>
        </form>
	  </li>
	  
	  <li class="listManage">
        <!-- This is the drop-down menu -->
	    <form name="list" id="addForm" method="POST" role="group" onsubmit="return manageList(this);">
        <!-- Button to add item to selected list, doesn't do anything if choice is empty -->
          <button class="btn btn-primary" type="submit" id="manageListButton">Manage List</button><br>
          <select name="listName" id="dropDownBar" class="dropDownBar">
         	    <option disabled selected id="defaultOption"> -- select an option -- </option>
       		    <option value ="f" >Favorites</option>
          	    <option value ="t">To Explore</option>
        	    <option value ="d">Do Not Show</option>
          </select>
        </form>
	  </li>
	  
	</ul>
    <div id="main" class="mx-auto">
      <div class="">
      <!-- Restaurants and Recipes lists  -->
      	<h1 style="text-align:center;" class="center-text"><%=listName %> List</h1>
      		<% 
      		// Used to alternate colors
   			int j = index;
      	int k = 0;
          	while(j < managementList.size() && k < 5){ 
          	String colorStyle = "";
        	if (j%2 == 0){
          		colorStyle = "silver";
        	}
          	else{
          		colorStyle = "grey";
          	}
          	%>
    <!-- This is the restaurant div -->
    
          	<div class="col-4 mx-auto" id="managementList<%=j%>">
          	<%
          		String type = managementList.get(j).getType();
          		String name = "";
          		String stars = "";
          		String minutesOrCookTime = "";
          		String addressOrPrepTime = "";
          		String price = "";
          		String link = "";
          		
          		if(type.equals("rec")){
          			// Get array num of item in original list
					      int num = lists.getArrayNum(lists.getLists().get(j).getRecipe());
          			
          			Recipe temp = managementList.get(j).getRecipe();
          			name = temp.getName();
          			stars = String.format("%.1f",temp.getRating());
          			if(temp.getCookTime() == -1){
              			minutesOrCookTime = "<strong>Cook Time:</strong></br> <p>0</p>";
          			}
          			else{
              			minutesOrCookTime = "<strong>Cook Time:</strong></br> <p>" + temp.getCookTime() + "</p>";
          			}
          			if(temp.getPrepTime() == -1){
          				addressOrPrepTime = "<strong>Prep Time:</strong></br> <p>0</p>";
          			}
          			else{
          				addressOrPrepTime = "<strong>Prep Time:</strong></br> <p>" + temp.getPrepTime() + "</p>";
          			}
          			link = "/FeedMe/recipeDetails?arrNum=" + num;
          		}
          		else{
          		  int num = lists.getArrayNum(lists.getLists().get(j).getRestaurant());
					
          			Restaurant temp = managementList.get(j).getRestaurant();
          			name = temp.getName();
          			stars = String.format("%.1f",temp.getRating());
          			minutesOrCookTime = "<strong>Minutes:</strong></br> <p>" + temp.getDrivingTime() + "</p>";
          			addressOrPrepTime = "<strong>Address:</strong></br> <p>" + temp.getAddress() + "</p>";
          			
          			int priceToString = (int)managementList.get(j).getRestaurant().getPrice();
								if (priceToString == 1){
									price = "<strong>Price: <p>$</p></strong>";
								}
								else if (priceToString == 2){
									price = "<strong>Price: <p>$$</p></strong>";
								}
								else if (priceToString == 3) {
									price = "<strong>Price: <p>$$$</p></strong>";
								}
								else {
									price = "<strong>Price: <p>$$$$</p></strong>";
								}

          			link = "/FeedMe/restaurantDetails?arrNum=" + num;
          		}
          	%>
         			<div class="row no-gutters border rounded overflow-hidden flex-md-row mb-8 shadow-sm h-md-250 position-relative">
        			<div style="background-color:<%=colorStyle %>;"class="col p-4 d-flex flex-column position-static">
          			<div class="container">
  						<div class="row">
    					<div class="col-sm">
							<strong>Name:</strong> <br><p> <%=name%> </p>
   						</div>

    					<div class="col-sm">
     	 						<strong>Stars:</strong> <br> <p> <%=stars%> </p>
    					</div>
    					<div class="col-sm">
     	 						
    					</div>
  						</div>
  						<div class="row">
    						<div class="col-sm">

   							</div>

    					<div class="col-sm">

    					</div>
  						</div>
  						<div class="row">
    						<div class="col-sm">
      							<%=minutesOrCookTime%>
   							</div>

    					<div class="col-sm">
     	 						<%=addressOrPrepTime%>
    					</div>
    					<div class="col-sm text-right">
     	 						<%=price%>
    					</div>
  						</div>
					</div>

          			<a href="<%=link%>" class="stretched-link"></a>
        			</div>
      				</div>
      				<!-- This form takes the item to the specified list page -->
      				<form style="display:flex;flex-direction:column;justify-content:center;" method="POST" action="/FeedMe/listManagement">
             			<input type="hidden" name="listName" value="<%=listName.toLowerCase().charAt(0)%>">
	 	            	<input type="hidden" name="fromList" value="<%=listName.toLowerCase().charAt(0)%>">
    	            	<input type="hidden" name="recOrRest" value=<%=type%>>
        	        	<input type="hidden" name="arrNum" value="<%=j%>">
                    <input id="paginationPage1" type="hidden" name="page" value="<%=index%>">
                		<select id="moveDropDown" class="form-control" name="opType">
                			<option value="" disabled>---- Reorder Item ----</option>
                			<option value="up">Move Up</option>
                			<option value="down">Move Down</option>
                			<option value="" disabled>---- Move Item to list ----</option>
	                		<option value="f">Favorites</option>
    	            		<option value="t">To Explore</option>
        	        		<option value="d">Do Not Show</option>
                			<option value="" disabled>---- Remove Item from list ----</option>
            	    		<option value="r">Trash</option>
                		</select>
	                	<button id="moveButton" class="form-control" type="submit">Move</button>
					</form>
    		</div>
         <% ++j;++k;} %>
	</div>
	 <br>
	 <!-- This is where the pagination is -->
	 <div id="pagination" class="text-center">
	   <!-- 
	       Need a current page, and size of list
	        - For this, can keep the original list on front end
	   -->
	   <form id="paginationForm" method="POST" action="/FeedMe/listManagement">
	     <input id="paginationListName" type="hidden" name="listName" value="<%=listName.toLowerCase().charAt(0)%>">
	     <input id="paginationPage2" type="hidden" name="page" value="">
	   </form>
	   <p>Page <%=index+1%></p>
	   <nav aria-label="Page pagination">
	     <ul class="pagination mx-auto mb-3">
	       <% for(int i = 0; i <= managementList.size()/5; ++i){%>
	         <li class="page-item"><a class="page-link" onclick="paginationForm(<%=i%>)"><%=i+1%></a></li>
	       <%} %>
	     </ul>
	   </nav>
	 </div>

	 </div>
	<!-- End of html/java code -->
  </body>
    <!-- Homebrew JS -->
  <script>
  function paginationForm(page){
	  var form = document.getElementById("paginationForm");
	  form.page.value = page;
	  console.log(form);
	  form.submit();
  }
	function restaurantRedirect(form){
		form.submit();
	}
	function recipeRedirect(form){
		form.submit();
	}
	// Makes sure that the page does nothing if the default value for which list to manage is still there
	function manageList(form){
		var userInput = document.getElementById('dropDownBar').value;
		if (userInput == null || userInput.length == 0){
			return false;	
		}
		else{
			form.action = "/FeedMe/listManagement";
		}
	}
</script>
<style>
.navbar {
    display:table;
    border-collapse:collapse;
    border-radius:.25em;
    box-shadow:#d0d0d0 0px 0px 0px 1px;
    background-color: rgba(108, 122, 137, .75);
    float: left;
    width: 100%;
}
ul {
  width: 25%;
  list-style-type: none;
  margin: 0;
} 

li {
  display: block;
  float:left;
}
button{
    width:100%;
}
.listManage{
  position: relative;
  float: right;
}
.linkToSite{
  height:63px;
  border-right: 1px solid #bbb;
}
li img{
  width: 50px;
}  
</style>
</html>