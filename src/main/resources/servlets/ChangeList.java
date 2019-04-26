package servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import data.*;

@WebServlet("/changeList")
public class ChangeList extends HttpServlet {
  private static final long serialVersionUID = 1L;
  
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession();
    @SuppressWarnings("unchecked")
    ArrayList<ListItem> list = (ArrayList<ListItem>) session.getAttribute("list");
    String srcIndex = request.getParameter("srcIndex");
    String dstIndex = request.getParameter("dstIndex");
    String listType = request.getParameter("listLetter");


    System.out.println("src: " + srcIndex + " dst: " + dstIndex);
    
    if(srcIndex != null && srcIndex != "" && dstIndex != null && dstIndex != "") {
    
      int src = Integer.parseInt(srcIndex);
      int dst = Integer.parseInt(dstIndex);
      ListItem temp = list.get(dst);
      list.set(dst, list.get(src));
      list.set(src, temp);
     
      System.out.println("src: " + src + " dst: " + dst);
      request.setAttribute("listName", listType); // Send the entire array of lists to session, so that we can access any item on front end       
      session.setAttribute("list", list); // Send the entire array of lists to session, so that we can access any item on front end 
    }
    RequestDispatcher dispatch = request.getRequestDispatcher("/listManagement");
    dispatch.forward(request,  response);
  }

}
