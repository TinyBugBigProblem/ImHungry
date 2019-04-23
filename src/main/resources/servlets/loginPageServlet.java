package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.*;

/*
 * Back-end code for generating the Results Page
 */
@WebServlet("/login")
public class loginPageServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  /*
   * service method is invoked whenever user attempts to 
   */
  @SuppressWarnings("unchecked")
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Database database = new Database();
    /*
     * Determine if user is trying to login or register
     */
    String type = request.getParameter("loginOrRegister");
    boolean status = false;
    // If null then something wrong happened just redirect back to login page
    if(type == null) {
      RequestDispatcher dispatch = request.getRequestDispatcher("/jsp/login.jsp");
      dispatch.forward(request,  response);     
    }
    if(type.equals("login")) { // Perform login
      
    }
    else { // Perform register
      database.signUpUser(request.getParameter("username"), request.getParameter("password"));
    }
    
    /* 
     * Once login check is done, forward back to login page with status code 
     */
    RequestDispatcher dispatch = request.getRequestDispatcher("/jsp/login.jsp");
    dispatch.forward(request,  response);     
  }
}