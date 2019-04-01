<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html lang="en">
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
    if(request.getAttribute("index") != null){
    	index = Integer.parseInt((String) request.getParameter("index"));
      System.out.println("not null index: " + index);
    }
    System.out.println("front end index: " + index);
    
  %>

    <!-- Title -->
    <title>List Management</title>
  </head>
  <body style="background-color:whitesmoke;">
    <div id="main" class="d-inline-flex p-1">
      <div class="p-2 ml-2">
      <!-- Restaurants and Recipes lists  -->
      	<h1><%=listName %> List</h1>
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
          	<div class="col-12" id="managementList<%=j%>">
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
    	      	
	<!-- Takes the user to the specified list -->
   	<div id="buttons" class="buttons align-middle p-1">
		<form name="list" onsubmit="return manageList(this);">
      	<select id="dropDownBar" name="listName" class="dropDownBar">
      		<option disabled selected value> -- select an option -- </option>
       		<option value ="f" >Favorites</option>
        	<option value ="t">To Explore</option>
        	<option value ="d">Do Not Show</option>
      	</select>
     	<!-- Button to add item to selected list, doesn't do anything if choice is empty -->
     	<button class="Button" id="manageListButton">Manage List</button> <br>

       </form>
	  <!-- Takes user to the search page -->
      <form action ="/FeedMe/jsp/search.jsp">
      	<button class="Button" id="returnToSearch" onclick="javascript:location.href = this.value;">Return to Search</button>
      </form>
      <!-- Takes the user to the results page -->
	   <form action ="/FeedMe/results">
      	<button class="Button" id="backToResults" onclick="javascript:location.href = this.value;">Return to Results</button>
      </form>
	
	  </div>
	 </div>
	 
	 <!-- This is where the pagination is -->
	 <div id="pagination" class="">
	   <!-- 
	       Need a current page, and size of list
	        - For this, can keep the original list on front end
	   -->
	   <form id="paginationForm" method="POST" action="/FeedMe/listManagement">
	     <input type="hidden" name="listName" value="<%=listName.toLowerCase().charAt(0)%>">
	     <input type="hidden" name="page" value="">
	   </form>
	   <p>Pagination</p>
	   <nav aria-label="Page pagination">
	     <ul class="pagination">
	       <% for(int i = 0; i <= managementList.size()/5; ++i){ %>
	         <li class="page-item"><a class="page-link" onclick="paginationForm(<%=i%>)"><%=i%></a></li>
	       <%} %>
	     </ul>
	   </nav>
	 </div>
	<!-- End of html/java code -->
  </body>
    <!-- Homebrew JS -->
  <script>
  function paginationForm(page){
	  var form = document.getElementById("paginationForm");
	  form.page = page;
	  console.log(page);
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
    <%@ include file="/css/buttons.css"%>
  </style>
</html>