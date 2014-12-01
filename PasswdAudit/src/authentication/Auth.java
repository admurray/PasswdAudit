package authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;

public class Auth extends Authenticator 
{    
    private String username;    
    private String password;
    
    public Auth(String username, String password) 
    {    
        super();    
        this.username = username;    
        this.password = password;    
    }    
    
    
    public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String authenticate(String username, String password) throws IOException 
	{
		String appPage = "http://www.cse.yorku.ca/~cse92012/4413/login.cgi";
		BufferedReader br;
		String name = null;
		try {    
	            Authenticator.setDefault(new SConnAuthenticator(username, password));    
	            URL url = new URL(appPage);    
	            int responseCode = 0;
	            HttpURLConnection connection = (HttpURLConnection)url.openConnection(); 
	            connection.setConnectTimeout(2000);
	            
	            try
	            {
	            	long a = System.currentTimeMillis();
	            	connection.connect();
	            	responseCode = connection.getResponseCode();

	            	long b = System.currentTimeMillis();
	            	System.out.println("The time taken is"+(b-a));
	            	System.out.println(responseCode);
	            }
	            catch(Exception e)
	            {
	            	e.printStackTrace();
	            }
	            if(responseCode == 200)
	            {
	            	String line;
	            	br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	            	while ((line = br.readLine()) != null) 
	            	{
	            		if(line.contains("Logout"))
	            			name = line.trim();
	            	}
	            	System.out.println(name);
	            	connection.disconnect();
	            	return name;
	            }
	            connection.disconnect();
	            return name;
		 	} catch (Exception e) {    
	            e.printStackTrace();    
	        }    
		 return name;
	}
}