package testPack;

import processHtml.UtilityPasswdTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;

class Test
{
	public static void main(String[] args) throws Exception
	{
		/*
		 * -uf users.txt 
		 * -pf passwords_test.txt 
		 * -l http://127.0.0.1/zen-cart-v1.5.1-full/index.php?main_page=login&action=process 
		 * -p securityToken=%%%securityToken%%%&email_address=%%%username%%%&password=%%%password%%%&securityToken=%%%securityToken%%%&x=0&y=0 
		 * -c zenid=%%%zenid%%% 
		 * -i "Log Out"
		 */
		
		String docu = "";
		System.out.println("------------------------------------------");
		HttpURLConnection.setFollowRedirects(false);
		String email = "adhoc@abcd.com";
		String password = "aditya001";
		
		email = URLEncoder.encode(email, "UTF-8");
		password = URLEncoder.encode(password, "UTF-8");
		
		String post = "securityToken=%%%securityToken%%%&email_address="+email+"&password="+password+"&securityToken=%%%securityToken%%%&x=0&y=0 ";  
		String url = "http://127.0.0.1/Sugar/index.php?action=Login&module=Users";
		String cookie_tem = "zenid=%%%zenid%%%";
		
		// Set up the connection
		URL login = new URL(url);
		Proxy proxy = new Proxy(Proxy.Type.HTTP, 
				new InetSocketAddress("127.0.0.1", 8080));
		HttpURLConnection connection = (HttpURLConnection)login.openConnection(proxy);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		
		connection.setRequestMethod("POST");
		// Create the cookie. 
		String cookie1 = cookie_tem, key;
		// Retrieve the cookie key
		
		HttpURLConnection conn = (HttpURLConnection)login.openConnection();
		
		
		//Set up the post and then work with the completed post
		
		
		while(post.contains("%%%") || cookie1.contains("%%%"))
		{
			String x = post.substring(post.indexOf("%%%")+3);
			x = x.substring(0, x.indexOf("%%%"));
			String val = UtilityPasswdTest.getPHPSSID(conn, x);
			post = post.replace("%%%"+x+"%%%", val);
			key = cookie_tem.substring(cookie_tem.indexOf("%%%")+3);
			key = key.substring(0, key.indexOf("%%%"));
			cookie1 = UtilityPasswdTest.getPHPSSID(conn, key);
			cookie1 = cookie_tem.replace("%%%"+key+"%%%", cookie1);
		}
		connection.setRequestProperty("Cookie", cookie1);
		
		
		
		OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
		writer.write(post);
		
		writer.flush();
		writer.close();
		
		
		int response = connection.getResponseCode();
		HttpURLConnection connection2;
		
		/*
		 * I am done the first part of the connection, no I need to get the second cookie and
		 * the move on from there. 
		 * For that I need to chect whether I have a 302 or a 200
		 */
		if(response == 302)
		{
			int i = 1 ;
			String location = connection.getHeaderField("Location");
			URL url2 = new URL(location);
			while(!connection.getHeaderFieldKey(i).contains("Cookie"))
			{				
				i++;
			}
			String cookie2 = connection.getHeaderField(i);
			cookie2 = cookie2.substring(0, cookie2.indexOf(";"));
			connection2 = (HttpURLConnection) url2.openConnection(proxy); 
			
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
		String login_iden = "Log Out";
		//Look for html parsers
		if(docu.contains(login_iden))
		{
			System.out.println("=================================================================================");
			System.out.println("Awesome, It works !!!!!");
			System.out.println();
			System.out.println("Login successfull, "+"The username is \""+email+"\" and your password is \""+ password+"\".");
			System.out.println();
			System.out.println("			HAPPY ETHICAL HACKING");
			System.out.println("			    :>			");
			System.out.println("=================================================================================");
			connection.disconnect();
		}
		else
		{
			System.out.println("Attempt failed with username \""+email+ "\" and password \""+password+"\".");
		}
		// ===============================================
	}
	
}