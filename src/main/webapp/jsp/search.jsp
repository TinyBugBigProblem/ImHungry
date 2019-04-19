<html>
<head>
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
	<link href="../css/search.css" rel="stylesheet" type="text/css">

	<!-- Javascript -->
	<script type="text/javascript" src="../javascript/searchEmoji.js"></script>
	<title id="searchTitle">Search Page</title>
</head>

<body style="background-color:whitesmoke; background-image: url('http://localhost:8080/FeedMe/images/searchPageBackground.jpg'); background-repeat: no-repeat; background-size: cover; background-position: center center;">
<div class="view container" style="position:relative; top:30%;">
  <div class="row">
    <div class="col">
    </div>
    <div class="col-6" style="background-color: rgba(245, 245, 245, 0.5);">
      <h1 class="text-center display-1">I'm Hungry</h1>
  
      <div class="col-md-12 text-center"> 
    	<form id="form" onsubmit= "return changeEmoji(this);"> <!-- Calls the js function that changes the emoji -->
  		 <input id="newSearch" type="hidden" name="newSearch" value="new">
  		 <input id="userInput" type="text" name="q" placeholder="Enter Food"><br>
  		 <input id="searchTermTest" title="Number of items to show in results" type="text" name="n" value="" placeholder="Enter number of items to show in results"><br>
  		 <input id="restaurantRadius" title="Radius in miles for restaurants" type="text" name="r" value="" placeholder="Enter radius, in miles, to show restaurant"><br>
  		 <img src="https://images.emojiterra.com/twitter/v11/512px/1f620.png" id ="emoji" height = 20 width = 20>
  		 <button class="text-center" type="submit" value="Feed Me" name ="feedMeButton"id="feedMeButton" style="color: red;">Feed Me!</button>
  		</form>
  	  </div>
		
    </div>
    <div class="col">

    </div>
  </div>
</div>
</body>
</html>
