package processHtml;

import java.net.*;

import getHtml.*;
import java.io.*;

public class UtilityPasswdTest 
{
		/**
		 * 
		 * @param url
		 * @return String the string that represents the session ID of the sesssion
		 * @throws IOException
		 */
		public static String getPHPSSID(HttpURLConnection conn, String session_id) throws IOException {
			HttpURLConnection.setFollowRedirects(true);
			HttpURLConnection connection = conn;
			String value;
			int i = 0;
			
			while((value = connection.getHeaderField(i)) != null)
			{
				System.out.println(value);
				if(value.contains(session_id))
				{
					value = value.substring(value.indexOf("=") + 1, 
							value.indexOf(";"));
					return value;
				}
				else
					i++;
			}
			InputStreamReader ins = new InputStreamReader(connection.getInputStream());
			BufferedReader br = new BufferedReader(ins);
			String str = "";
			while((str = br.readLine()) != null)
			{
				if(str.contains(session_id))
				{
					// I need a securityToken for zenCart
					//System.out.println(str);
					value = getValue(session_id , str);
					break;
				}
			}
			
			return value;
		}
		
		/**
		 * 
		 * @param post
		 * @param username
		 * @param password
		 * @return Returns the final post statement after the changed password and username.
		 */
		public static String replaceUserPass(String post, String username, 
				String password)
		{
			String post_one = post.replaceFirst("%%%username%%%", username );
			String post_final = post_one.replaceAll("%%%password%%%", password);
			return post_final;
		}
		
		public boolean validatelogin(String redirection_page) throws Exception
		{
			boolean login = false;
			GetHTML test = new GetHTML(redirection_page);
			test.getHtml();

			return login;
			
		}
		
		public static String getValue(String key, String str)
		{
			boolean flag = false;
			String fin = "" ;
			str = str.substring(str.indexOf(key));
			
			if(str.contains(key+"="))
			{
				fin = str.substring(str.indexOf(key));
				fin = fin.substring(fin.indexOf(key)+ key.length() +1, fin.indexOf("\""));
				
			}
			//getVal =str.substring(str.indexOf(key));
			else if (str.contains("value=") && flag == false)
			{
				fin = str.substring(str.indexOf("value")+7);
				fin = fin.substring(0, fin.indexOf("\""));
			}
			return fin;
		}


//========================  MAIN FOR TESTING  =================================
/*		public static void main(String[] args) throws Exception
		{
			URL url  =  new URL("http://127.0.0.1/zen-cart-v1.5.1-full/index.php?main_page=login&action=process");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			String s = getPHPSSID(conn , "securityToken");
			System.out.println(s);
			
		}*/
}
