<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<%@page import="java.util.*" %>
	<%@page import="data.*"%>
	<%@page import="java.lang.Math"%>
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
	  
	  <li class="listManage">
        <!-- This is the drop-down menu -->
	    <form name="list" id="listDropDown" method="POST" role="group" onsubmit="return manageList(this);">
        <!-- Button to add item to selected list, doesn't do anything if choice is empty -->
          <button class="btn btn-primary" type="submit" id="addToList">Manage List</button><br>
          <select name="listName" id="listName" class="dropDownBar">
       		    <option id="nOptionButton" disabled selected value> -- select an option -- </option>
     			<option id="fOptionButton" value ="f">Favorites</option>
    		    <option id="tOptionButton" value ="t">To Explore</option>
    			<option id="dOptionButton" value ="d">Do Not Show</option>
          </select>
        </form>
	  </li>
	  
	</ul>
	<div class="container mt-2">
		<!-- Row for collage and buttons -->
		<div class = "align-items-start mx-auto">
		 	<div id="collageDiv" style="background-color: rgba(245, 245, 245, 0.5); max-width: 60vw; min-width:40vw; max-height: 50vh;text-align: center; min-height: 35vh;" class="py-5 overflow-hidden mx-auto">
		 	<% for (int i = 0; i < 10; i++) {
		 		Random rand = new Random();
		 		int angle = rand.nextInt(90) -45;
		 	%>
			<img style ="vertical-align: middle; transform: rotate(<%=angle%>deg);" src="<%=imageUrlVec[i] %>" height="200px" width="200px" class="center-block">
		 	<% } %>
		 	<%--

					<% for (int i = 0; i < imageUrlVec.length; ++i) { %>
					<img src="<%=imageUrlVec[i] %>" height="100" width="100">
					<% } %>
		 	--%>

			</div>
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
	          	 <%
	          	   int currPage = restaurantIndex;
	          	   int lastPage = (int) Math.ceil(restaurantArr.length/5);
	          	 %>
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
				   <p class="text-center">Page <%=currPage+1%></p>
				   <nav aria-label="Page pagination" style="padding: 0px 30%;">
				     <ul class="pagination">
				     <%
				        // Disable page if true
				        String disabledPage = "";
				        // The current button is always in the third position, unless there are <= 5 pages total, or if it's below 3. or if it's less than 2 from the last possible page
				     	// Prev button, only if not first page
				     	if((currPage != 0)){%>
				     		<li class="page-item"><a class="page-link" onclick="paginationForm(<%=restaurantIndex-1%>, <%=currPage%>, 'restaurant')">Prev</a></li>
				     	<%}
				     %>
				       <% 
				       	  // Display all pages if they are less than 5 total or if the current page is less than or equal to 2
				       	  if(lastPage <= 4 || currPage <= 2){
				       	    for(int i = 0; i <= lastPage; ++i){
				       	      if(i == restaurantIndex){disabledPage = "disabled";}
				       	    %>
				              <li class="page-item"><a class="page-link <%=disabledPage %>" onclick="paginationForm(<%=i%>, <%=currPage%>, 'restaurant')"><%=i+1%></a></li>
				         <% }}
				          // Display only last 5 pages if the current page is less than two away from the last possible page
				       	  else if((lastPage - currPage) <= 2){
				       	    for(int i = lastPage - 4; i <= lastPage; ++i){
				       	      if(i == currPage){disabledPage = "disabled";}
				       	    %>
				              <li class="page-item"><a class="page-link <%=disabledPage %>" onclick="paginationForm(<%=i%>, <%=currPage%>, 'restaurant')"><%=i+1%></a></li>
				       	  <%}}
				          // Current page is in the middle of the pagination
				       	  else{
				       		for(int i = currPage - 2; i <= currPage + 2; ++i){
				       		  if(i == restaurantIndex){disabledPage = "disabled";}
				       		%>
				              <li class="page-item"><a class="page-link <%=disabledPage %>" onclick="paginationForm(<%=i%>, <%=currPage%>, 'restaurant')"><%=i+1%></a></li>  
				       	  <%}}
				         %>
				       
				     <%
				       // Next button, only if not last page
				       if(currPage != lastPage){%>
				     		<li class="page-item"><a class="page-link" onclick="paginationForm(<%=currPage+1%>, <%=currPage%>, 'restaurant')">Next</a></li>
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
		          	 <%
		          	   currPage = recipeIndex;
		          	   lastPage = (int) Math.ceil(recipeArr.length/5);
		          	 %>
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
					   
				   <p class="text-center">Page <%=currPage+1%></p>
				   <nav aria-label="Page pagination" style="padding: 0px 30%;">
				     <ul class="pagination">
				     <%
				        // The current button is always in the third position, unless there are <= 5 pages total, or if it's below 3. or if it's less than 2 from the last possible page
				     	// Prev button, only if not first page
				     	if((currPage != 0)){%>
				     		<li class="page-item"><a class="page-link" onclick="paginationForm(<%=currPage-1%>, <%=currPage%>, 'recipe')">Prev</a></li>
				     	<%}
				     %>
				       <% 
				       	  // Display all pages if they are less than 5 total or if the current page is less than or equal to 3
				       	  if(lastPage <= 4 || currPage <= 2){
				       	    for(int i = 0; i <= lastPage; ++i){
				       	      if(i == currPage){disabledPage = "disabled";}
				       	    %>
				              <li class="page-item"><a class="page-link <%=disabledPage %>" onclick="paginationForm(<%=i%>, <%=currPage%>, 'recipe')"><%=i+1%></a></li>
				         <% }}
				          // Display only last 5 pages if the current page is less than two away from the last possible page
				       	  else if((lastPage - currPage) <= 2){
				       	    for(int i = lastPage - 4; i <= lastPage; ++i){
				       	      if(i == currPage){disabledPage = "disabled";}
				       	    %>
				              <li class="page-item"><a class="page-link <%=disabledPage %>" onclick="paginationForm(<%=i%>, <%=currPage%>, 'recipe')"><%=i+1%></a></li>
				       	  <%}}
				          // Current page is in the middle of the pagination
				       	  else{
				       		for(int i = currPage - 2; i <= currPage + 2; ++i){
				       		  if(i == currPage){disabledPage = "disabled";}
				       		%>
				              <li class="page-item"><a class="page-link <%=disabledPage %>" onclick="paginationForm(<%=i%>, <%=currPage%>, 'recipe')"><%=i+1%></a></li>  
				       	  <%}}
				         %>
				       
				     <%
				       // Next button, only if not last page
				       if(currPage != lastPage){%>
				     		<li class="page-item"><a class="page-link" onclick="paginationForm(<%=currPage+1%>, <%=currPage%>, 'recipe')">Next</a></li>
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
<style>
.navbar {
    display:table;
    border-collapse:collapse;
    border-radius:.25em;
    box-shadow:#d0d0d0 0px 0px 0px 1px;
    background-color: rgba(108, 122, 137, .75);
    width: 100%;
}
.navbar ul {
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
  height: 63px;
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