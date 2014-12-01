package authentication;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class SConnAuthenticator extends Authenticator 
{    
    private String username;    
    private String password;
    
    public SConnAuthenticator(String username, String password) 
    {    
        super();    
        this.username = username;    
        this.password = password;    
    }    
    
    public PasswordAuthentication getPasswordAuthentication() 
    {       
        return new PasswordAuthentication(username, password.toCharArray());    
    } 
}