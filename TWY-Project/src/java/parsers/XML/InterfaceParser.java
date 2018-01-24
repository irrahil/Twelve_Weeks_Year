/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers.XML;

import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
/**
 *
 * @author irit
 */
public class InterfaceParser extends DefaultHandler {
    
    HashMap<String, Object> params;
    
    String currentElement;
    FormObject form;
    FieldObject field;
    ButtonObject button;
    MenuObject[] menus;
    NewsObject news;
    int menuIndex = 0;
    boolean isMenu;
    int textType = 0;
    
    public InterfaceParser(HashMap<String, Object> parameters) {
        params = parameters;
    }
    /*
    Открытие тега
    */
    @Override
    public void startElement(String uri, 
                             String localname, 
                             String qName,              //Имя текущего тега
                             Attributes attributes      //Аттрибуты тега
    ) throws SAXException {
        currentElement = qName;
        if (currentElement.contains("menu") ) {
            isMenu = true;
            menus = new MenuObject[Integer.parseInt(attributes.getValue("count") ) ];
            for (int i=0; i < menus.length; i++)
                menus[i] = new MenuObject();
            params.put("MainMenu", menus[0]);
            params.put("TaskMenu", menus[1]);
            params.put("JournalMenu", menus[2]);
            params.put("AdminMenu", menus[3]);
        }
        if (currentElement.contains("Menu") )
            menuIndex = Integer.parseInt(attributes.getValue("num") ) - 1;
        if (currentElement.contains("ABOUT") )
            textType = 1;
        if (currentElement.contains("MAIN") )
            textType = 2;
        if (currentElement.contains("forms") )
            isMenu = false;
        if (currentElement.contains("NEWS") ) {
            news = new NewsObject();
            params.put("news", news);
        }
        if (currentElement.contains("entry") ) {
            NewsEntryObject obj = new NewsEntryObject(attributes.getValue("date"), attributes.getValue("message") );
            news.addNewsEntry(obj);
        }
        if (currentElement.contains("button") && isMenu ) {
            ArrayList<String> CmdParams = new ArrayList();
            CmdParams.add("\'" + attributes.getValue("name") + "\'" );
            button = new ButtonObject(attributes.getValue("name"), 
                                      attributes.getValue("value"), 
                                      "takePage", CmdParams );
            menus[menuIndex].addButton(button);
        }
        if (currentElement.contentEquals("title") )
            params.put("title", attributes.getValue("value") );
        if (currentElement.contentEquals("ajax_path") )
            params.put("ajax_path", attributes.getValue("value") );
        if (currentElement.contentEquals("calendar_rule") )
            params.put("calendar_rule", attributes.getValue("value") );
        if (currentElement.contentEquals("css_path") )
            params.put("css_path", attributes.getValue("value") );
        if (currentElement.contentEquals("form") )
        {
            boolean method = false;
            if (attributes.getValue("method") != null && attributes.getValue("method").contentEquals("post"))
                method = true;
            form = new FormObject(attributes.getValue(0), 
                                  Integer.parseInt(attributes.getValue(1)),
                                  Integer.parseInt(attributes.getValue(2)),
                                  method );
            params.put("form_" + attributes.getValue(0), form);
        }
        if (currentElement.contentEquals("field") ) {
            field = new FieldObject
            (
                attributes.getValue("name"),
                attributes.getValue("type"),
                attributes.getValue("comment"),
                attributes.getValue("value")
            );
            form.addField(field);
        }
        super.startElement(uri, localname, qName, attributes);
    }
    
    
    
    /*
    Чтение данных в теге
    */
    @Override
    public void characters(char[] c, int start, int length) //Получает содержимое между тегами
            throws SAXException {
        if (textType == 1) {
            String text = String.valueOf(c, start, start+length);
            if (text.indexOf("</ABOUT>") > 0)
                text = text.substring(0, text.indexOf("</ABOUT>") );
            params.put("aboutText", text );
            textType = 0;
        }
        if (textType == 2) {
            String text = String.valueOf(c, start, start+length);
            if (text.indexOf("</MAIN>") > 0)
                text = text.substring(0, text.indexOf("</MAIN>") );
            params.put("mainText", text );
            textType = 0;
        }
        super.characters(c, start, length);
    }
    
    
    
    /*
    Закрытие тега
    */
    @Override
    public void endElement(String uri,
                           String localname,
                           String qName)
            throws SAXException {
        super.endElement(uri, localname, qName);
    }
    
    
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }
}
