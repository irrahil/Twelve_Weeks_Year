/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package patterns.HTML;

import java.util.HashMap;
import parsers.XML.*;
import servlets.additions.*;
import controllers.tasks.*;
import controllers.journal.*;
import controllers.time.*;
/**
 *
 * @author irit
 */
public class HTMLStandartConstructs {
    
    public static void HTMLStandartConstructs() {}
    
    public static String getMainHTML(HashMap params, UserConfig user) {
        String header = 
        "<!DOCTYPE html><html><head>"
        + "<title>" + params.get("title") + "</title>\n"
        + "<script src=\""
        + params.get("ajax_path") + "\"></script>"
        + "<script src=\""
        + params.get("calendar_rule") + "\"></script>"
        + "<link href=\""
        + params.get("css_path") + "\" rel=stylesheet type=text/css>"
        + "</head>"
        + "<body>"
        + getMenu(params, user);
        return header;
    }
    
    
    public static String getEndHTML() {
        return "</body></html>";
    }
    
    
    private static String getMenu(HashMap params, UserConfig user) {
        StringBuilder menu = new StringBuilder();
        menu.append("<table border=0 width=100%><tr>\n");
        MenuObject menuObj = (MenuObject)params.get("MainMenu");
        for (int i=0; i < menuObj.size(); i++) {
            if (user.isLogin() ) {
                if (menuObj.button(i).contains("loginPage") )
                    continue;
                if (menuObj.button(i).contains("regPage") )
                    continue;
                if (menuObj.button(i).contains("exitPage") ) {
                    menu.append("<td class=\"main_menu\">");
                    menu.append(user.userName() );
                    menu.append("</td>");
                }
                    
            }
            else {
                if (menuObj.button(i).contains("exitPage") )
                    continue;
            }
            menu.append("<td class=\"main_menu\">");
            menu.append(menuObj.button(i) );
            menu.append("</td>\n");
        }
        menu.append("</tr></table>\n");
        return menu.toString();
    }
    
    
    public static String getAboutPage(HashMap params) {
        
        StringBuilder aboutText = new StringBuilder();
        aboutText.append("<div align=center width=100%>");
        aboutText.append("<table>");
        aboutText.append("<tr><td><pre>");
        aboutText.append( (String)params.get("aboutText"));
        aboutText.append("</pre></td></tr></table></div>");
        return aboutText.toString();
    }
    
    
    public static String getMainPage(HashMap params) {
        StringBuilder aboutText = new StringBuilder();
        aboutText.append("<div align=center width=100%>");
        aboutText.append("<table>");
        aboutText.append("<tr><td><pre>");
        aboutText.append( (String)params.get("mainText"));
        aboutText.append("</pre></td></tr><tr><td>");
        aboutText.append("<div><h2>Новости</h2></div>");
        aboutText.append( ( (NewsObject)params.get("news") ).getNewsHtml() );
        aboutText.append("</td></tr></table></div>");
        return aboutText.toString();
    }
    
    
    public static String getTaskPage(HashMap params, UserConfig user) {
        if (!user.isLogin() )
            return "Вход не выполнен";
        MenuObject menu = (MenuObject)params.get("TaskMenu");
        StringBuilder taskPage = new StringBuilder();
        taskPage.append("<table class=\"task_page\">");
        taskPage.append("<tr>");
        taskPage.append("<td class=\"task_page_panel\" rowspan=2>");
        String years = TaskCtrl.getListOfYears( (PGConnection)params.get("db_connect"), user );
        taskPage.append(years);
        taskPage.append(menu.button("newYear") );
        taskPage.append("</td><td>");
        taskPage.append("<div id=\"tasks\"></div>");
        taskPage.append("</td></tr></table>");
        return taskPage.toString();
    }
    
    
    public static String getJournalPage(HashMap params, UserConfig user) {
        if (!user.isLogin() )
            return "Вход не выполнен";
        StringBuilder taskPage = new StringBuilder();
        taskPage.append("<table class=\"journal_page\">");
        taskPage.append("<tr>");
        taskPage.append("<td class=\"journal_page_panel\" rowspan=2>");
        String years = JournalCtrl.getListOfYears( (PGConnection)params.get("db_connect"), user );
        taskPage.append(years);
        taskPage.append("</td><td>");
        taskPage.append("<div id=\"journal\"></div>");
        taskPage.append("</td></tr></table>");
        return taskPage.toString();
    }
    
    
    public static String getAddJournalPage(String week) {
        StringBuilder page = new StringBuilder();
        page.append("<table width=100%>");
        page.append("<tr><td>");
        page.append("<textarea id=\"new_entry\" style=\"width:600px; height: 300px;\">");
        page.append("</textarea><br>");
        page.append("<button onClick=\"addNewEntry(");
        page.append(week);
        page.append("); return false\">Добавить</button>");
        page.append("</td></tr></table>");
        return page.toString();
    }
    
    
    public static String getTimePage(HashMap params, UserConfig user) {
        if (!user.isLogin() )
            return "Вход не выполнен";
        StringBuilder taskPage = new StringBuilder();
        taskPage.append("<table class=\"time_page\">");
        taskPage.append("<tr>");
        taskPage.append("<td class=\"time_page_panel\" rowspan=2>");
        String years = TimeCtrl.getListOfYears( (PGConnection)params.get("db_connect"), user );
        taskPage.append(years);
        taskPage.append("</td><td>");
        taskPage.append("<div id=\"calendar\"></div>");
        taskPage.append("</td></tr></table>");
        return taskPage.toString();
    }
}
