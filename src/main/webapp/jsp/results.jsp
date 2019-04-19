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
	
    // Get pagination index start
    int restaurantIndex = 0;
    if(request.getAttribute("restaurantIndex") != null){
    	restaurantIndex = (Integer) request.getAttribute("restaurantIndex");
    }
    // Get pagination index start
    int recipeIndex = 0;
    if(request.getAttribute("recipeIndex") != null){
    	recipeIndex = (Integer) request.getAttribute("recipeIndex");
    }
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

<body style="background-color:whitesmoke; background-image: url('http://localhost:8080/FeedMe/images/knifeAndFoodBoard.jpg'); background-repeat: no-repeat; background-size: cover; background-position: center center;">
	<div class="container mt-2">
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
		<div style="background-color: rgba(245, 245, 245, 0.5);">
			<!-- Search For xx  -->
			<div class="py-5 text-center">
	   			<h2 id="titleHeader">Results For <%=searchTerm %></h2>
	   		</div>
	
	   		<!-- Restaurants and Recipes lists  -->
	   		<div class="row md-6">
	   			<div class="col-md-6">
	      			<h2 id="restaurantTitle" class="text-center"> Restaurants</h2>
	          		<%
					int j = 0;
	          		for(int i = restaurantIndex*5; (j < 5) && (i < restaurantArr.length); i++, j++){
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
	         			<div class="row no-gutters border rounded overflow-hidden flex-md-row md-4 shadow-md h-sm-250 position-relative" id="Restaurant<%=i%>" style="height:190px;">
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
          		 <!-- This is where the pagination is -->
				 <div id="restaurantPagination" class="">
				   <!-- 
				       Need a current page, and size of list
				        - For this, can keep the original list on front end
				   -->
				   <form id="restaurantPaginationForm" method="POST" action="/FeedMe/results">
				     <input id="paginationRestaurntListName" type="hidden" name="listName" value="restaurantList">
					 <input id="paginationRecipePage" type="hidden" name="restaurantIndex" value="<%=restaurantIndex%>">
					 <input id="paginationRecipePage" type="hidden" name="recipeIndex" value="<%=recipeIndex%>">
				   </form>
				   <p>Page <%=restaurantIndex%></p>
				   <nav aria-label="Page pagination">
				     <ul class="pagination">
				     <%
				        // Disable page if true
				        String disabledPage = "";
				        // The current button is always in the third position, unless there are <= 5 pages total, or if it's below 3. or if it's less than 2 from the last possible page
				     	// Prev button, only if not first page
				     	if((restaurantIndex != 0)){%>
				     		<li class="page-item"><a class="page-link" onclick="paginationForm(<%=i-1%>, <%=restaurantIndex%>, 'restaurant')">Prev</a></li>
				     	<%}
				     %>
				       <% 
				       	  // Display all pages if they are less than 5 total or if the current page is less than or equal to 3
				       	  if(((restaurantArr.length/5) <= 5) || (restaurantIndex <= 3)){
				       	    for(int i = 0; i < 5; ++i){
				       	      if(i == restaurantIndex){disabledPage = "disabled";}
				       	    %>
				              <li class="page-item"><a class="page-link <%=disabledPage %>" onclick="paginationForm(<%=i%>, <%=restaurantIndex%>, 'restaurant')"><%=i+1%></a></li>
				         <% }}
				          // Display only last 5 pages if the current page is less than two away from the last possible page
				       	  else if((restaurantArr.length - restaurantIndex) < 2){
				       	    for(int i = restaurantArr.length - 5; i < restaurantArr.length - 1; ++i){
				       	      if(i == restaurantIndex){disabledPage = "disabled";}
				       	    %>
				              <li class="page-item"><a class="page-link <%=disabledPage %>" onclick="paginationForm(<%=i%>, <%=restaurantIndex%>, 'restaurant')"><%=i+1%></a></li>
				       	  <%}}
				          // Current page is in the middle of the pagination
				       	  else{
				       		for(int i = restaurantIndex - 2; i < restaurantIndex + 5; ++i){
				       		  if(i == restaurantIndex){disabledPage = "disabled";}
				       		%>
				              <li class="page-item"><a class="page-link <%=disabledPage %>" onclick="paginationForm(<%=i%>, <%=restaurantIndex%>, 'restaurant')"><%=i+1%></a></li>  
				       	  <%}}
				         %>
				       
				     <%
				       // Next button, only if not last page
				       if(restaurantIndex != (restaurantArr.length/5)){%>
				     		<li class="page-item"><a class="page-link" onclick="paginationForm(<%=i+1%>, <%=restaurantIndex%>, 'restaurant')">Next</a></li>
				     	<%}
				       disabledPage = "";
				     %>
				     </ul>
				   </nav>
				 </div>	
	    	</div>
	    	
	
	    	<!-- Recipes -->
	    		<div class="col-md-6">
	      			<h2 id="recipeTitle" class= "text-center"> Recipes</h2>
	          		<% 
	          			j = 0;
	          			for(int i = recipeIndex*5; j < 5 && i < recipeArr.length ; i++,j++){ 
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
	         			<div class="row no-gutters border rounded overflow-hidden flex-md-row md-4 shadow-md h-sm-250 position-relative" id="Recipe<%=i%>" style="height:190px;">
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
					 <!-- This is where the pagination for recipes is -->
					 <div id="recipeRagination" class="">
					   <!-- 
					       Need a current page, and size of list
					        - For this, can keep the original list on front end
					   -->
					   <form id="recipePaginationForm" method="POST" action="/FeedMe/results">
					     <input id="paginationRecipeListName" type="hidden" name="listName" value="recipeList">
					     <input id="paginationRecipePage" type="hidden" name="restaurantIndex" value="<%=restaurantIndex%>">
					     <input id="paginationRecipePage" type="hidden" name="recipeIndex" value="<%=recipeIndex%>">
					   </form>
				   <p>Page <%=recipeIndex%></p>
				   <nav aria-label="Page pagination">
				     <ul class="pagination">
				     <%
				        // The current button is always in the third position, unless there are <= 5 pages total, or if it's below 3. or if it's less than 2 from the last possible page
				     	// Prev button, only if not first page
				     	if((recipeIndex != 0)){%>
				     		<li class="page-item"><a class="page-link" onclick="paginationForm(<%=i-1%>, <%=recipeIndex%>, 'recipe')">Prev</a></li>
				     	<%}
				     %>
				       <% 
				       	  // Display all pages if they are less than 5 total or if the current page is less than or equal to 3
				       	  if(((recipeArr.length/5) <= 5) || (recipeIndex <= 3)){
				       	    for(int i = 0; i < 5; ++i){
				       	      if(i == recipeIndex){disabledPage = "disabled";}
				       	    %>
				              <li class="page-item"><a class="page-link <%=disabledPage %>" onclick="paginationForm(<%=i%>, <%=recipeIndex%>, 'recipe')"><%=i+1%></a></li>
				         <% }}
				          // Display only last 5 pages if the current page is less than two away from the last possible page
				       	  else if((recipeArr.length - recipeIndex) < 2){
				       	    for(int i = recipeArr.length - 5; i < recipeArr.length - 1; ++i){
				       	      if(i == recipeIndex){disabledPage = "disabled";}
				       	    %>
				              <li class="page-item"><a class="page-link <%=disabledPage %>" onclick="paginationForm(<%=i%>, <%=recipeIndex%>, 'recipe')"><%=i+1%></a></li>
				       	  <%}}
				          // Current page is in the middle of the pagination
				       	  else{
				       		for(int i = recipeIndex - 2; i < recipeIndex + 5; ++i){
				       		  if(i == recipeIndex){disabledPage = "disabled";}
				       		%>
				              <li class="page-item"><a class="page-link <%=disabledPage %>" onclick="paginationForm(<%=i%>, <%=recipeIndex%>, 'recipe')"><%=i+1%></a></li>  
				       	  <%}}
				         %>
				       
				     <%
				       // Next button, only if not last page
				       if(recipeIndex != (recipeArr.length/5)){%>
				     		<li class="page-item"><a class="page-link" onclick="paginationForm(<%=i+1%>, <%=recipeIndex%>, 'recipe')">Next</a></li>
				     	<%}
				       disabledPage = "";
				     %>
					     </ul>
					   </nav>
					 </div>
	    		</div>
	   		</div>
	   	</div>
	</div>
	
</body>	

<script>
function paginationForm(page, currPage, type){
	var form;
	if(currPage == page){
		return;
	}
	else if(type == 'restaurant'){
		form = document.getElementById("restaurantPaginationForm");
		form.restaurantIndex.value = page;
	}
	else{
		form = document.getElementById("recipePaginationForm");
		form.recipeIndex.value = page;
	}
	console.log(form);
	form.submit();
}
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