<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<%@page import="java.util.*" %>
	<%@page import="data.*"%>
	<%

	request.getSession().setAttribute("resultsOrList", "results");
	String[] imageUrlVec = (String[])request.getAttribute("imageUrlVec");
	String searchTerm =  (String) request.getAttribute("searchTerm");
	Integer resultCount =  (Integer) request.getAttribute("resultCount");
	Restaurant[] restaurantArr = (Restaurant[]) request.getAttribute("restaurantArr");
	Recipe[] recipeArr = (Recipe[]) request.getAttribute("recipeArr");
	
	%>
	  <!-- Bootstrap CSS  -->
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
	  <!-- Homebrew CSS  -->
    <link href="../css/buttons.css" rel="stylesheet" type="text/css">
    <link href="../css/details.css" rel="stylesheet" type="text/css">
    <link href="../css/list.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet"  href="/css/results.css"  type="text/css">
    <style>
    <%@ include file="/css/buttons.css"%>
    </style>
	  <!-- Javascript -->
	<script type="text/javascript" src="../javascript/manageListButton.js"></script>
	 <title><%=searchTerm %></title>
</head>

<body style="background-color:whitesmoke;">
	<div class="container mt-5">
		<!-- Row for collage and buttons -->
		<div class = "row align-items-start">
			<div class="col-sm-2 order-3">
		 		<div class="buttons">
		 			<form id="listDropDown" name="list" onsubmit="return manageList(this);">
      					<select id="listName" name="listName" class="dropDownBar">
      					<option id="nOptionButton" disabled selected value> -- select an option -- </option>
       				    <option id="fOptionButton" value ="f">Favorites</option>
        				<option id="tOptionButton" value ="t">To Explore</option>
        				<option id="dOptionButton" value ="d">Do Not Show</option>
      					</select> <br>
      					<!-- Button to add item to selected list, doesn't do anything if choice is empty -->
     					<button id="addToList" class="Button">Manage Lists</button> <br>

      				</form>


      				<form action ="/FeedMe/jsp/search.jsp">
      					<button id="returntoSearch" onclick="javascript:location.href = this.value;" class="Button">Return to Search</button>
      				</form>

		 		</div>
		 	</div>

		 	<div id="collageDiv" style=" max-width: 60vw; min-width:40vw; max-height: 50vh;text-align: center; min-height: 35vh; border: 2px solid black;" class="col-sm-6 order-2 pt-3 overflow-hidden">
		 	<% for (int i = 0; i < 10; i++) {
		 		Random rand = new Random();
		 		int angle = rand.nextInt(90) -45;
		 	%>
			<img style =" vertical-align: middle; transform: rotate(<%=angle%>deg);" src="<%=imageUrlVec[i] %>" height="100" width="100">
		 	<% } %>
		 	<%--

					<% for (int i = 0; i < imageUrlVec.length; ++i) { %>
					<img src="<%=imageUrlVec[i] %>" height="100" width="100">
					<% } %>
		 	--%>

			</div>
			<div class="col-sm-3 order-1"></div>
		</div>

		<!-- Search For xx  -->
		<div class="py-5 text-center">
   			<h2 id="titleHeader">Results For <%=searchTerm %></h2>
   		</div>

   		<!-- Restaurants and Recipes lists  -->
   		<div class="row md-2">
   			<div class="col-md-6">
      			<h2 id="restaurantTitle" class="text-center"> Restaurants</h2>
          		<%

          		for(int i = 0; i < resultCount; i++){
          			String colorStyle = "";
          			if (i%2 == 0){
          				colorStyle = "silver";
          			}
          			else{
          				colorStyle = "grey";
          			}
          		%>
          		<% if(restaurantArr[i] != null){ %>
          			<% System.out.println("Rest Arr: " + i + " " + (restaurantArr[i] == null));  %>
         			<div class="row no-gutters border rounded overflow-hidden flex-md-row md-4 shadow-md h-md-250 position-relative" id="Restaurant<%=i%>">
        			<div style="background-color:<%=colorStyle %>;"class="col p-4 d-flex flex-column position-static">
          			<div class="container">
  						<div class="row">
    					<div class="col-sm">
							<strong id="NameRestaurant">Name:</strong> <br><p><%=restaurantArr[i].getName() %> </p>
   						</div>

    					<div class="col-sm">
     	 						<strong id="starsRestaurant">Stars:</strong> <br> <p> <%=restaurantArr[i].getRating() %> </p>
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
      							<strong id="minutesRestaurant">Minutes:</strong> <br> <p><%=restaurantArr[i].getDrivingTime() %> </p>
   							</div>

    					<div class="col-sm">
     	 						<strong id="addressRestaurant">Address: </strong><br> <p><%=restaurantArr[i].getAddress() %></p>
    					</div>
    					<div class="col-sm text-right">
    							<%
        							String restaurantPrice = "";
        							int price = (int)restaurantArr[i].getPrice();
        							if (price == 1){
        							restaurantPrice = "$";
        							}
        							else if (price == 2){
        							restaurantPrice = "$$";
        							}
        							else{
        								restaurantPrice = "$$$";
        							}
        							%>
     	 						<strong>Price: <%=restaurantPrice%></strong>
    					</div>
  						</div>
					</div>

          			<a href="/FeedMe/restaurantDetails?arrNum=<%=i%>" class="stretched-link" title="restaurantDetailsLink<%=i%>"></a>
        			</div>
        			<div class="col-auto d-none d-lg-block">
        			
          			</div>
      				</div>
      				
          		<% }} %>
		
    	</div>
    	

    	<!-- Recipes -->
    		<div class="col-md-6">
      			<h2 id="recipeTitle" class= "text-center"> Recipes</h2>
          		<% for(int i = 0; i < resultCount; i++){ 
          			String colorStyle = "";
          			if (i%2 == 0){
          				colorStyle = "silver";
          			}
          			else{
          				colorStyle = "grey";
          			}
          		%>
          			<% if(recipeArr[i] != null){ %>
          			<% System.out.println("Rest Arr: " + i + " " + (restaurantArr[i] == null));  %>
         			<div class="row no-gutters border rounded overflow-hidden flex-md-row md-4 shadow-md h-md-250 position-relative" id="Recipe<%=i%>">
        			<div style="background-color:<%=colorStyle %>;" class="col p-4 d-flex flex-column position-static">
          			<div class="container">
  						<div class="row">
    						<div class="col-sm">
      							<strong id="NameRecipe">Name:</strong> <br><p><%=recipeArr[i].getName() %></p>
   							</div>

    					<div class="col-sm">
    						<% String recipeRating = String.format("%.1f",recipeArr[i].getRating()); %>
     	 						<strong id="starsRecipe">Stars:</strong> <br> <p> <%=recipeRating %> </p>
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
    							<%
    								double cookTime = recipeArr[i].getCookTime();
    								String renderCookTime = "";
    								if (cookTime < 0){
    									renderCookTime = "Not Available";
    								}
    								else{
    									renderCookTime = Double.toString(cookTime);
    								}
    								
    								double prepTime = recipeArr[i].getPrepTime();
    								String renderPrepTime = "";
    								if (prepTime < 0){
    									renderPrepTime = "Not Available";
    								}
    								else{
    									renderPrepTime = Double.toString(prepTime);
    								}			
    							%>
      							<strong id="cooktimeRecipe">Cook Time:</strong> <br> <p><%=renderCookTime %></p>
   							</div>

    					<div class="col-sm">
     	 						<strong id="preptimeRecipe">Prep Time: </strong><br> <p><%=renderPrepTime%></p>
    					</div>
    				
  						</div>
					</div>



          			<a href="/FeedMe/recipeDetails?arrNum=<%=i%>" class="stretched-link"></a>
        			</div>
        			<div class="col-auto d-none d-lg-block">
          			</div>
      				</div>
          		<% }} %>

    	</div>

   		</div>

	</div>
	
<script>

function manageList(form){
	var userInput = document.getElementById('listName').value;
	console.log(userInput);
	if (userInput == null || userInput.length == 0){
		return false;
	}
	else{
		form.action = "/FeedMe/listManagement";
		return true;
	}
} 
</script>
</body>