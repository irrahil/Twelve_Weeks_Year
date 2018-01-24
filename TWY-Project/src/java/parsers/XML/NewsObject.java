/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers.XML;
import java.util.*;
/**
 *
 * @author irit
 */
public class NewsObject {
    ArrayList<NewsEntryObject> news;
    
    public NewsObject() {
        news = new ArrayList();
    }
    
    
    public void addNewsEntry(NewsEntryObject obj) {
        news.add(obj);
    }
    
    
    public String getNewsHtml() {
        StringBuilder newHTML = new StringBuilder();
        newHTML.append("<div>");
        for (int i = 0; i < news.size(); i++) {
            newHTML.append(news.get(i).getHTML() );
            newHTML.append("<hr>");
        }
        newHTML.append("</div>");
        return newHTML.toString();
    }
}
