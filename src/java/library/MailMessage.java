/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.mail.Message;

/**
 *
 * @author lucas
 */
public class MailMessage {
    public List<String> addresses;
    public String subject;
    public String body;
    public Boolean isHtml;
    public List<FileMessage> files;
    public Message originalMessage;
    
    public MailMessage(){
        addresses = new ArrayList<>();
        files = new ArrayList<>();
    }
    
    public void AddFile(String name, File file){
        FileMessage f = new FileMessage();
        f.file = file;
        f.name = name;
        files.add(f);
    }
    
    public void addTemplate(String template, Map<String,String> tokens){
        for(Map.Entry<String, String> token : tokens.entrySet()){
            String key = token.getKey();
            String value = token.getValue();
            template = template.replace(key, value);
        }
        
        body = template;
        isHtml = true;
    }
}
