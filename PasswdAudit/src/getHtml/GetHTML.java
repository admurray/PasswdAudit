package getHtml;


import java.io.*;
import java.net.*;


/**
 * This class so far deals with the html downloading, seeing if the connection
 * redirects, and then get the html from the page that the connection was 
 * redirected to.
 * @author adityam
 * 
 */
	
	

public class GetHTML
{	
	//Single attribute for the class.
	private String url;
	private String cookie;
	//Constructor
	public GetHTML(String url) {
		this.url  = url;
		this.cookie = null;
	}
	
	// A constructor that also takes in the cookie value when required.
	public GetHTML(String url, String cookie)
	{
		this.url = url;
		this.cookie = cookie;
	}
	
	/*Accessor 
	 * Accesses the url of the GetHTML.
	 */
	private String getUrl() {
		return this.url;
	}
	
	
	/*
	 * This method generalizes the 3XX Case it prints a message 
	 * "[CASE 3XX] : Redirection ". In each of the following methods I shall be
	 *  passing URLConnection as the parameter since it is the URLConnection
	 * instance that returns the attributes of the connection, including the 
	 * connection CODE that determines the STATUS of the connection.
	 * It returns the modified or the redirected url. We have a specified 
	 * method for the case 3XX that is the only place the connection actually 
	 * needs to return something. In the case 2XX "ALL IS WELL", in 4xx and 
	 * 5XX we need to set the url to null, hence no need for an extra method.
	 */
	private static URL caseThree(URLConnection connection) 
			throws MalformedURLException {
		//Print the case statement, get the redirected url and return
		System.out.println("[CASE 3XX] : Redirection ");
		URL url = new URL(connection.getHeaderField("Location"));
		return url;
	}
	
	//Report client error and return null.
	private static URL caseFour(URLConnection connection) throws Exception {
		System.out.println("[ CASE 4XX ] : Client Error");
		return null;
	}
	
	//Report server error and return null.
	private static URL caseFive(URLConnection connection) throws Exception {
		System.out.println("[ CASE 5XX ] : Server Error");
		return null;
	}

	
	   /*
	    * This method takes in a string, it the opens a http connection to 
	    * the site and checks it as to what code is returned by the server. 
	    * If 3XX is returned it returns the url that the site has been 
	    * redirected to. In the case of a 5XX and a 4XX it simply returns 
	    * a error message and returns null as before. For 2xx it does nothing. 
	    */
	   
	   private URL processURL(String site) throws Exception
	   {
		   URL conn  = new URL(site);
		   HttpURLConnection connection = (HttpURLConnection)conn.openConnection();
		   int return_code  = connection.getResponseCode();
		   System.out.println(conn.toString()+" returned [" +return_code +"] ");
		   switch(return_code)
		   {
		   case HttpURLConnection.HTTP_ACCEPTED:			//202	
			   //do noting i.e., no change in the url
			   break;
		   case HttpURLConnection.HTTP_BAD_GATEWAY:			//502
			   conn = GetHTML.caseFive(connection);
			    break;
		   case HttpURLConnection.HTTP_BAD_METHOD:			//405
			   conn = GetHTML.caseFour(connection);
			   break; 
		   case HttpURLConnection.HTTP_BAD_REQUEST:			//400
			   conn = GetHTML.caseFour(connection);
			   break;
		   case HttpURLConnection.HTTP_CLIENT_TIMEOUT:		//408
			   conn = GetHTML.caseFour(connection);
			   break;
		   case HttpURLConnection.HTTP_CONFLICT:			//409	
			   conn = GetHTML.caseFour(connection);
			   break;
		   case HttpURLConnection.HTTP_CREATED:				//201
			   //do noting i.e., no change in the url
			   break;																	
		   case HttpURLConnection.HTTP_ENTITY_TOO_LARGE:	//413			
			   conn = GetHTML.caseFour(connection);
			   break;															
		   case HttpURLConnection.HTTP_FORBIDDEN:			//403			
			   conn = GetHTML.caseFour(connection);
			   break;
		   case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:		//504			
			   conn = GetHTML.caseFive(connection);
			   break;
		   case HttpURLConnection.HTTP_GONE:				//410			
			   conn = GetHTML.caseFour(connection);
			   break;														
		   case HttpURLConnection.HTTP_INTERNAL_ERROR:		//500			
			   conn = GetHTML.caseFive(connection);
			   break;											   
		   case HttpURLConnection.HTTP_LENGTH_REQUIRED:		//411			
			   conn = GetHTML.caseFour(connection);
			   break;																
		   case HttpURLConnection.HTTP_MOVED_PERM:			//301
			   conn = GetHTML.caseThree(connection);
			   break;
		   case HttpURLConnection.HTTP_MOVED_TEMP:			//302			
			   conn = GetHTML.caseThree(connection);
			   break;
		   case HttpURLConnection.HTTP_MULT_CHOICE:			//300			
			   conn = GetHTML.caseThree(connection);
			   break;
		   case HttpURLConnection.HTTP_NO_CONTENT:			//204
			   //do noting i.e., no change in the url
			   break;													
		   case HttpURLConnection.HTTP_NOT_ACCEPTABLE:		//406			
			   conn = GetHTML.caseFour(connection);
			   break;														
		   case HttpURLConnection.HTTP_NOT_AUTHORITATIVE:	//203			
			 //do noting i.e., no change in the url
			   break;															
		   case HttpURLConnection.HTTP_NOT_FOUND:			//404			
			   conn = GetHTML.caseFour(connection);
			   break;														
		   case HttpURLConnection.HTTP_NOT_IMPLEMENTED:		//501			
			   conn = GetHTML.caseFive(connection);
			   break;																	
		   case HttpURLConnection.HTTP_NOT_MODIFIED:		//304			
			   conn = GetHTML.caseThree(connection);
			   break;																		
		   case HttpURLConnection.HTTP_OK:					//200			
			  //do noting i.e., no change in the url
			   break;																			
		   case HttpURLConnection.HTTP_PARTIAL:				//206			
			  //do noting i.e., no change in the url
			   break;																			
		   case HttpURLConnection.HTTP_PAYMENT_REQUIRED:	//402			
			   conn = GetHTML.caseFour(connection);
			   break;																		
		   case HttpURLConnection.HTTP_PRECON_FAILED:		//412			
			   conn = GetHTML.caseFour(connection);
			   break;																					
		   case HttpURLConnection.HTTP_PROXY_AUTH:			//407			
			   conn = GetHTML.caseFour(connection);
			   break;																	
		   case HttpURLConnection.HTTP_REQ_TOO_LONG:		//414			
			   conn = GetHTML.caseFour(connection);
			   break;																		
		   case HttpURLConnection.HTTP_RESET:				//205			
			  //do noting i.e., no change in the url
			   break;																						
		   case HttpURLConnection.HTTP_SEE_OTHER:			//303			
			   conn = GetHTML.caseThree(connection);
			   break;														
		   //case HttpURLConnection.HTTP_SERVER_ERROR:		Deprecated. it is misplaced and shouldn't have existed.
			 //break;																	
		   case HttpURLConnection.HTTP_UNAUTHORIZED:		//401			
			   conn = GetHTML.caseFour(connection);
			   break;																		
		   case HttpURLConnection.HTTP_UNAVAILABLE:			//503			
			   conn = GetHTML.caseFive(connection);
			   break;																		
		   case HttpURLConnection.HTTP_UNSUPPORTED_TYPE:	//415			
			   conn = GetHTML.caseFour(connection);
			   break;															
		   case HttpURLConnection.HTTP_USE_PROXY:			//305			
			   conn = GetHTML.caseThree(connection);
			   break;															
		   case HttpURLConnection.HTTP_VERSION:				//505			
			   conn = GetHTML.caseFive(connection);
			   break;															
		   default:		;														
			   break;												   
		   }
		   return conn;
	   }
		
	   
	   
	   
	   /**
		 * Connects to the specified url and get the html for the page. It stores
		 * the downloaded html in .txt file.
		 * 
		 * @param url The Url of the site to be indexed/read
		 */
		public void getHtml() throws Exception
		{
			String site = this.getUrl();
			String str;			
					
			// Opens the connection to the site and starts the stream of data from 
			// the site.
			URL url  = this.processURL(site);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			if(cookie != null)
			{
				connection.setRequestProperty("Cookie", this.cookie);
			}
			InputStream stream = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));

			
			/*
			 * Getting the writer ready to write file containing the html of the
			 *  specified site. The name of the file is dependent of the String 
			 *  representation of the url. The cut URL does nothing but cuts the 
			 *  url short to use it for the file name.
			 *  For each url the Writer must create a new file. The filename is 
			 *  the same as the url.
			*/
			BufferedWriter write = 
					new BufferedWriter(new FileWriter(Reads.cutUrl(site.toString()).concat(".txt")));
			
			// Reads the line one by one to get the html of the page the url connection 
			// has been opened to.
			while((str = br.readLine()) != null)
			{
				// Writer writing to the file one line at a time.
				write.append(str+"\n");
				//System.out.println(str);
			}
			//Close the streams.
			write.close();
			stream.close();
		}
}
