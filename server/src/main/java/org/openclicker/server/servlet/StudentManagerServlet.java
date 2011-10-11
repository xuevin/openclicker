package org.openclicker.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openclicker.server.util.HibernateUtil;

public class StudentManagerServlet extends HttpServlet {
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    
    try {
      // Begin unit of work
      HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
      
      PrintWriter out = response.getWriter();
      out.println("HELLO SERVLET and Yellow");
      out.flush();
      out.close();
      
      // End unit of work
      HibernateUtil.getSessionFactory().getCurrentSession().getTransaction()
          .commit();
      
    } catch (Exception ex) {
      HibernateUtil.getSessionFactory().getCurrentSession().getTransaction()
          .rollback();
      
      if (ServletException.class.isInstance(ex)) {
        throw (ServletException) ex;
      } else {
        throw new ServletException(ex);
      }
    }
  }
}
