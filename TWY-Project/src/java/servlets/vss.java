/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.*;

import java.util.HashMap;
import java.util.*;
import servlets.additions.*;
import builders.*;
import controllers.authority.*;
import controllers.tasks.*;
import controllers.journal.*;
import controllers.time.*;
import parsers.*;
import json.CalendarObjectJSON;

/**
 *
 * @author irit
 */
@WebServlet(name = "vss", urlPatterns = {"/vss"})
public class vss extends HttpServlet {

    HashMap<String, Object> servletParams;
    HTMLBuilder builder;
    String username;
    String password;
    String currentCMD, currentPage, referer;
    HashMap<String, UserConfig> users;
    UserConfig user;
    PGConnection db_connect;
    String Msg, page;
    HttpSession session;
    
    @Override
    /*
     * Переопределение инициализации сервлета для инициации внутренних параметров.
     */
    public void init() throws ServletException {
        super.init();
        users = new HashMap();
        XMLParser parser = new XMLParser("/opt/apache-tomcat-8.5.5/webapps/TWY/xml_configs/interface.xml");
        servletParams = parser.getParams();
        db_connect = new PGConnection("localhost", "5432", "tomcat");
        db_connect.Connect("tomcat", "tomcat");
        servletParams.put("db_connect", db_connect);
        builder = new HTMLBuilder(servletParams);
        currentPage = "main";
        currentCMD = "";
        
    }
    
    
    private String drawInterface() {
        currentPage = (String) session.getAttribute("currentPage");
        switch (currentPage) {
            case "loginPage": return builder.drawLoginPage(user);
            case "mainPage": return builder.drawMainPage(user);
            case "regPage": return builder.drawRegPage(user);
            case "aboutPage": return builder.drawAboutPage(user);
            case "exitPage": return builder.drawExitPage(user);
            case "taskPage": return builder.drawTaskPage(user);
            case "journalPage": return builder.drawJournalPage(user);
            case "timePage": return builder.drawTimePage(user);
            case "adminPage": return builder.drawAdminPage(user);
            case "newYear": return builder.drawNewYearPage(user);
            case "main": return builder.drawMain(user);
            default: return "";
        }
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response, boolean isGet)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        session = request.getSession(); //Получение сессии текущего запроса
        //Ищем пользователя. Если не находим - создаем нового
        user = users.getOrDefault(session.getId(), new UserConfig() );
        if (!users.containsKey(session.getId() ) )
            users.put(session.getId(), user );
        
        //Проверяем на наличие соединения
        if (!db_connect.checkConnection() )
            db_connect.Connect("tomcat", "tomcat");
        response.setContentType("text/html;charset=UTF-8");
        username = null;
        password = null;
        Msg = null;
        referer = request.getHeader("referer");
        if (!referer.contains("vss") ) {
            TaskUtils.updateTime();
            session.setAttribute("currentPage", "main");
        }
        ///Обработка POST-запросов к приложению\\\
        if (!isGet) {
            Enumeration<String> post_params = request.getParameterNames();
            if (post_params.hasMoreElements() ) {
                currentCMD = request.getParameter("cmd");
                switch (currentCMD) {
                    case "takePage": {
                        session.setAttribute("currentPage", request.getParameter("getPage") );
                        break;
                    }
                    case "takeTaskPage": {
                        ArrayList<String> params = new ArrayList();
                        params.add(request.getParameter("Week_id") );
                        params.add(request.getParameter("getPage") );
                        Msg = TaskCtrl.getEditPage(db_connect, user, params);
                        break;
                    }
                    case "takeJournalPage": {
                        ArrayList<String> params = new ArrayList();
                        params.add(request.getParameter("getPage") );
                        params.add(request.getParameter("Week_id") );
                        Msg = JournalCtrl.getAddPage(db_connect, user, params);
                        break;
                    }
                    case "login": {
                        username = request.getParameter("login");
                        password = request.getParameter("pass");
                        LoginCtrl.logIn(db_connect, username, password, user);
                        session.setAttribute("currentPage", "main");
                        if (!user.isLogin() )
                            Msg = "Ошибка авторизации!";
                        break;
                    }
                    case "exit": {
                        user.logout();
                        session.setAttribute("currentPage", "main");
                        break;
                    }
                    case "getYearTasks": {
                        int year = Integer.parseInt(request.getParameter("year") );
                        user.task().setYear(year, TaskUtils.getBeginDate(db_connect, year) );
                        Msg = TaskCtrl.getTaskList(db_connect, user);
                        session.setAttribute("currentPage", "");
                        break;
                    }
                    case "getYearJournal": {
                        int year = Integer.parseInt(request.getParameter("year") );
                        user.task().setYear(year, TaskUtils.getBeginDate(db_connect, year) );
                        Msg = JournalCtrl.getJournalList(db_connect, user);
                        session.setAttribute("currentPage", "");
                        break;
                    }
                    case "getYearCalendar": {
                        int year = Integer.parseInt(request.getParameter("year") );
                        user.task().setYear(year, TaskUtils.getBeginDate(db_connect, year) );
                        ArrayList<CalendarObjectJSON> list = TimeCtrl.getCalendar(db_connect, user);
                        Msg = "";
                        for (int i = 0; i < list.size(); i++) {
                            Msg = Msg + ": :" + list.get(i).json();
                        }
                        session.setAttribute("currentPage", "");
                        break;
                    }
                    case "new_year": {
                        ArrayList<String> params = new ArrayList();
                        params.add(request.getParameter("begin_date") );
                        params.add(request.getParameter("purpose_1") );
                        params.add(request.getParameter("purpose_2") );
                        params.add(request.getParameter("purpose_3") );
                        TaskCtrl.MakeNewYear(db_connect, user, params);
                        session.setAttribute("currentPage", "main");
                        break;
                    }
                    case "new_task": {
                        ArrayList<String> params = new ArrayList();
                        params.add(request.getParameter("week_id") );
                        params.add(request.getParameter("task_day"));
                        params.add(request.getParameter("task_name"));
                        params.add(request.getParameter("purpose") );
                        if (TaskCtrl.MakeNewTask(db_connect, user, params) )
                            Msg = TaskCtrl.getEditPage(db_connect, user, params);
                        else
                            Msg = "Ошибка запроса. Попробуйте перезайти в систему.";
                        break;
                    }
                    case "new_entry": {
                        ArrayList<String> params = new ArrayList();
                        params.add(request.getParameter("week_id") );
                        params.add(request.getParameter("entry") );
                        if (JournalCtrl.MakeNewEntry(db_connect, user, params) )
                            Msg = JournalCtrl.getJournalList(db_connect, user);
                        else
                            Msg = "Ошибка запроса. Попробуйте перезайти в систему.";
                        break;
                    }
                    case "edit_task": {
                        ArrayList<String> params = new ArrayList();
                        params.add(request.getParameter("week_id") );
                        params.add(request.getParameter("task_id") );
                        params.add(request.getParameter("finish") );
                        params.add(request.getParameter("task_name"));
                        TaskCtrl.EditTask(db_connect, user, params);
                        Msg = TaskCtrl.getEditPage(db_connect, user, params);
                        break;
                    }
                }
                
            }
        }
        ///GET-запросы\\\
        else {
            Enumeration<String> get_params = request.getParameterNames();
            while (get_params.hasMoreElements() )  {//Проверяем на наличие хоть какого-нибудь параметра
                
                switch(get_params.nextElement() ) {
                    case "login": { username = request.getParameter("login"); break; }
                    case "pass": { password = request.getParameter("pass"); break; }
                    default: break;
                }
                
            }
        }
        
        try (PrintWriter out = response.getWriter()) {
            if (Msg != null) {
                out.println(Msg);
            }
            page = drawInterface();
            //Если мы вдруг ничего не отрисовывали
            if (Msg == null && page == null ) {
                session.setAttribute("currentPage", "main");
                page = drawInterface();
            }
            out.println(page );
            out.flush();        //Закрываем вывод
        }
        catch (Exception e) {
            e.printStackTrace();
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
        processRequest(request, response, true);
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
        processRequest(request, response, false);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
