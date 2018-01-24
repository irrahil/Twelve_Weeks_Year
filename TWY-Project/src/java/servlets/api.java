/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import controllers.authority.APILoginCtrl;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.additions.*;
import json.*;

/**
 *
 * @author irit
 */
@WebServlet(name = "api", urlPatterns = {"/api"})
public class api extends HttpServlet {

    String json_msg;
    String currentCMD;
    String user_token;
    ArrayList<UserJSON> userList;
    UserJSON currentUser;
    PGConnection db_connect;
    
    public void init() throws ServletException {
        super.init();
        json_msg = "";
        currentCMD = "";
        user_token = "";
        userList = new ArrayList();
        db_connect = new PGConnection("localhost", "5432", "tomcat");
        db_connect.Connect("tomcat", "tomcat");
    }
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        Enumeration<String> post_params = request.getParameterNames();
        if (post_params.hasMoreElements() ) {
            
            currentCMD = request.getParameter("cmd");
            user_token = request.getParameter("token");
            System.out.println("токен: " + user_token);
            for (int i=0; i < userList.size(); i++) {
                if (userList.get(i).token().contains(user_token) )
                    currentUser = userList.get(i);
            }
            if (currentCMD != null) {
                
                switch (currentCMD) {
                    case "login": {
                        String userName = request.getParameter("user_login");
                        String pass = request.getParameter("user_pass");
                        String dev_id = request.getParameter("device_id");
                        currentUser = new UserJSON();
                        userList.add(currentUser);
                        APILoginCtrl.logIn(db_connect, userName, pass, currentUser, dev_id);
                        json_msg = currentUser.getUser();
                        System.out.println("JSON-объект после логина: " + json_msg);
                        break;
                    }
                    default: {
                        json_msg = "unexpected cmd";
                        break;
                    }
                }
                
            }
        }    
        
        //Вывод полученных данных
        try (PrintWriter out = response.getWriter()) {
            out.println(json_msg);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("Error! System doesn\'t use GET-messages!!!");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "API for changing with different clients";
    }// </editor-fold>

}
