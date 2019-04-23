<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html style="height:100%;">
<head>
<link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<meta charset="ISO-8859-1">
<title>Login</title>
</head>
<body>
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
  <div class="container">
    <div>
    	<!-- Login form default -->
		<div id="loginDiv" class="card mx-auto">
			<div class="card-body">
				<h1 class="text-center" style="color:white;">Login</h1>
				<form method="POST" action="/FeedMe/login">
				    <input name="loginOrRegister" value="login" type="hidden" value="login">
					<div class="input-group form-group">
						<div class="input-group-prepend">
							<span class="input-group-text"><i class="fas fa-user"></i></span>
						</div>
						<input name="username" type="text" class="form-control" placeholder="username">
						
					</div>
					<div class="input-group form-group">
						<div class="input-group-prepend">
							<span class="input-group-text"><i class="fas fa-key"></i></span>
						</div>
						<input name="password" type="password" class="form-control" placeholder="password">
					</div>
					<div class="form-group">
						<input type="submit" value="Login" class="btn float-right login_btn">
					</div>
				</form>
			</div>
			<div class="card-footer">
				<div class="d-flex justify-content-center links">
					Don't have an account?<a href="#" onclick="return openRegisterForm()">Sign Up</a>
				</div>
			</div>
		</div>
		<!-- Register form, default is 0 width and transparent -->
		<div id="registerDiv" class="card mx-auto" style="width:0px;height:0px;opacity:0;">
			<div class="card-body">
				<h1 class="text-center" style="color:white;">Sign Up</h1>
				<form method="POST" action="/FeedMe/login">
				    <input name="loginOrRegister" value="register" type="hidden" value="register">
					<div class="input-group form-group">
						<div class="input-group-prepend">
							<span class="input-group-text"><i class="fas fa-user"></i></span>
						</div>
						<input name="username" type="text" class="form-control" placeholder="Enter username">
						
					</div>
					<div class="input-group form-group">
						<div class="input-group-prepend">
							<span class="input-group-text"><i class="fas fa-key"></i></span>
						</div>
						<input name="password" type="password" class="form-control" placeholder="Enter password">
					</div>
					<div class="form-group">
						<input type="submit" value="Register" class="btn float-right login_btn">
					</div>
				</form>
			</div>
			<div class="card-footer">
				<div class="d-flex justify-content-center links">
					Already have an account?<a href="#" onclick="return openLoginForm()">Sign In</a>
				</div>
			</div>
		</div>
	</div>
  </div>
</body>

<script>
function openRegisterForm(){
	var loginForm = document.getElementById("loginDiv");
	loginForm.style = "width:0px;height:0px;opacity:0;";
	var registerForm = document.getElementById("registerDiv");
	registerForm.style = "";
}
function openLoginForm(){
	var loginForm = document.getElementById("loginDiv");
	loginForm.style = "";
	var registerForm = document.getElementById("registerDiv");
	registerForm.style = "width:0px;height:0px;opacity:0;";
}


</script>

<style>
.login{
    display:table;
    border-collapse:collapse;
    border-radius:.25em;
    box-shadow:#d0d0d0 0px 0px 0px 1px;
    background-color: rgba(108, 122, 137, .75);
    width: 50%;
    margin-left:25%;
    margin-right:25%;
}
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

@import url('https://fonts.googleapis.com/css?family=Numans');

html,body{
background-image: url('http://localhost:8080/FeedMe/images/searchPageBackground.jpg');
background-size: cover;
background-repeat: no-repeat;
height: 100%;
font-family: 'Numans', sans-serif;
}


.card{
margin-top: auto;
margin-bottom: auto;
width: 400px;
background-color: rgba(0,0,0,0.5) !important;
}

.social_icon span{
font-size: 60px;
margin-left: 10px;
color: #FFC312;
}

.social_icon span:hover{
color: white;
cursor: pointer;
}

.card-header h3{
color: white;
}

.social_icon{
position: absolute;
right: 20px;
top: -45px;
}

.input-group-prepend span{
width: 50px;
background-color: #FFC312;
color: black;
border:0 !important;
}

input:focus{
outline: 0 0 0 0  !important;
box-shadow: 0 0 0 0 !important;

}

.remember{
color: white;
}

.remember input
{
width: 20px;
height: 20px;
margin-left: 15px;
margin-right: 5px;
}

.login_btn{
color: black;
background-color: #FFC312;
width: 100px;
}

.login_btn:hover{
color: black;
background-color: white;
}

.links{
color: white;
}

.links a{
margin-left: 4px;
}
</style>

</html>