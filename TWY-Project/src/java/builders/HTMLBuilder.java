/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package builders;
import patterns.HTML.*;
import java.util.HashMap;
import servlets.additions.*;
/**
 * Класс отрисовывает страницы
 * @author irit
 */
public class HTMLBuilder {
    
    HashMap<String, Object> params;
    HTMLFormBuilder formBuilder;
    
    public HTMLBuilder(HashMap<String, Object> params) {
        this.params = params;
        formBuilder = new HTMLFormBuilder(params);
    }
    
    /*
     * Отрисовывает главную страницу сервлета
     *
     */
    public String drawMain(UserConfig user) {
        return HTMLStandartConstructs.getMainHTML(params, user);
    }
    
    
    public String drawMainPage(UserConfig user) {
        return  HTMLStandartConstructs.getMainHTML(params, user)
                + HTMLStandartConstructs.getMainPage(params)
                + HTMLStandartConstructs.getEndHTML();
    }
    
    
    /*
     * Отрисовывает страницу входа на сайт
     */
    public String drawLoginPage(UserConfig user) {
        return  HTMLStandartConstructs.getMainHTML(params, user)
                + formBuilder.getLoginForm()
                + HTMLStandartConstructs.getEndHTML();
    }
    
    
    
    /*
     * Отрисовывает страницу регистрации на сайте
     */
    public String drawRegPage(UserConfig user) {
        return HTMLStandartConstructs.getMainHTML(params, user) 
                + formBuilder.getRegForm()
                + HTMLStandartConstructs.getEndHTML();
    }
    
    
    /*
     * Отрисовывает страницу с описанием системы
     */
    public String drawAboutPage(UserConfig user) {
        return  HTMLStandartConstructs.getMainHTML(params, user)
                + HTMLStandartConstructs.getAboutPage(params)
                + HTMLStandartConstructs.getEndHTML();
    }
    
    
    public String drawExitPage(UserConfig user) {
        return HTMLStandartConstructs.getMainHTML(params, user)
                + formBuilder.getExitForm()
                + HTMLStandartConstructs.getEndHTML();
    }
    
    
    public String drawTaskPage(UserConfig user) {
        return HTMLStandartConstructs.getMainHTML(params, user)
                + HTMLStandartConstructs.getTaskPage(params, user)
                + HTMLStandartConstructs.getEndHTML();
    }
    
    
    public String drawNewYearPage(UserConfig user) {
        return HTMLStandartConstructs.getMainHTML(params, user)
                + formBuilder.getNewYearForm()
                + HTMLStandartConstructs.getEndHTML();
    }
    
    
    public String drawJournalPage(UserConfig user) {
        return HTMLStandartConstructs.getMainHTML(params, user)
                + HTMLStandartConstructs.getJournalPage(params, user)
                + HTMLStandartConstructs.getEndHTML();
    }
    
    
    public String drawAdminPage(UserConfig user) {
        return HTMLStandartConstructs.getMainHTML(params, user)
                + HTMLStandartConstructs.getEndHTML();
    }
    
    
    public String drawTimePage(UserConfig user) {
            return HTMLStandartConstructs.getMainHTML(params, user)
                + HTMLStandartConstructs.getTimePage(params, user)
                + HTMLStandartConstructs.getEndHTML();
    }
    
    
    public String drawMsg(String Msg, UserConfig user) {
            return HTMLStandartConstructs.getMainHTML(params, user)
                    + Msg
                    + HTMLStandartConstructs.getEndHTML();
    }
}
