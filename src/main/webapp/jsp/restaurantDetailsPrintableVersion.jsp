<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
  	 <%@page import="java.util.*" %>
	<%@page import="data.*"%>
<%@ page import="data.Recipe"%>
<!DOCTYPE html>
<html lang="en" style="height: 100%;">
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">    
    <meta charset="ISO-8859-1">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <% 
    // To check if we came from results page or list page
    String resultsOrList = (String) request.getSession().getAttribute("resultsOrList");
	Restaurant restaurantVal = (Restaurant) request.getAttribute("restaurantVal");
    int arrNum = Integer.parseInt((String) request.getParameter("arrNum"));
    // Check to see what the previous page was
	if(resultsOrList.equals("list")){
		// Get data from session
		ArrayList<Restaurant> rest = (ArrayList<Restaurant>) request.getSession().getAttribute("restaurants");
		System.out.println(rest.size());
		// Put restaurant item into local variable
		restaurantVal = rest.get(arrNum);
	}
    %>
    <!-- Title -->
    <title>Restaurant Details</title>
  </head>


  <body class="text-center" style="background-color:whitesmoke; background-image: url('http://localhost:8080/FeedMe/images/knifeAndNutsBoard.jpg'); background-repeat: no-repeat; background-size: cover; background-position: center center;">
    <!-- Row -->
    <div class="row">
	    <div class="col-sm-10" style="display:inline-block;width:1000px; margin:10% auto; background-color: rgba(245, 245, 245, 0.5);">
	       <!-- Title -->
	       <h1 id="restaurantName"><%= restaurantVal.getName() %></h1>
	       <!-- Holds image, prep and cook time of recipe-->
	       <div id="details">
	         <a id="address" href="<%= "https://www.google.com/maps/dir/?api=1&origin=Tommy+Trojan%2C+Childs+Way%2CLos+Angeles+CA&origin_place_id=ChIJIfdecuPHwoARKagsKQF16io&destination=" + restaurantVal.getAddress()%>"><strong>Address:</strong> <%= restaurantVal.getAddress() %></a>
	         <p id="phoneNumber"><strong>Phone Number:</strong><%= restaurantVal.getPhoneNumber() %></p>
	         <a id="website" href="<%= restaurantVal.getWebsiteUrl() %>"><strong>Website Address: </strong><%= restaurantVal.getWebsiteUrl() %></a>
	       </div>
	    </div>
    </div>
    <!-- Homebrew JS -->
    <script>
    // Adds the item to the specified list, if the user specifies the proper list
    function addToList(form){
    	var userInput = document.getElementById('listType').value;
    	console.log(userInput);
    	if (userInput == null || userInput.length == 0){
    		return false;	
    	}
    	else{
    		form.action = "/FeedMe/restaurantDetails";
    	}
    }
    </script>
    <!-- Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
  </body>
</html>