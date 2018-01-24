/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers.XML;

/**
 *
 * @author irit
 */
public class FieldObject {
    String fieldName;
    String type;
    String comment;
    String value;
    
    
    public FieldObject() {
        this.fieldName = "";
        this.type = "";
        this.comment = "";
        this.value = "";
    }
    
    
    public FieldObject(String name, String type, String comment, String value) {
        this.fieldName = (name != null?name:"");
        this.type = (type != null?type:"text");
        this.comment = (comment != null?comment:"");
        this.value = (value != null?value:"");
    }
    
    
    public String getName() { return this.fieldName; }
    public String getType() {return this.type; }
    public String getComment() {return this.comment; }
    public String getValue() {return this.value; }
}
