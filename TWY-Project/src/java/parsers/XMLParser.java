/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import java.util.HashMap;
import javax.xml.parsers.*;
import java.io.*;
import parsers.XML.InterfaceParser;
import org.xml.sax.*;
/**
 *
 * @author irit
 */
public class XMLParser {
    
    String XMLConfig;
    HashMap<String, Object> params;
    
    public XMLParser(String xmlPath) {
        XMLConfig = xmlPath;
        params = new HashMap<String, Object>();
        parseXML();
    }
    
    
    
    public HashMap<String, Object> getParams() {
        return params;
    }
    
    
    
    private boolean parseXML() {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        factory.setValidating(true);
        factory.setNamespaceAware(false);
        SAXParser parser;

        InputStream xmlData = null;
        try
        {
            xmlData = new FileInputStream(XMLConfig);

            parser = factory.newSAXParser();
            parser.parse(xmlData, new InterfaceParser(params));

            
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return false;
            // обработки ошибки, файл не найден
        } catch (ParserConfigurationException e)
        {
            e.printStackTrace();
            return false;
            // обработка ошибки Parser
        } catch (SAXException e)
        {
            e.printStackTrace();
            return false;
            // обработка ошибки SAX
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
            // обработка ошибок ввода
        } 
        return true;
    }
}
