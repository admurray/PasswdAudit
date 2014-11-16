package getHtml;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.LinkedList;

/**
 * Ideally I would want this class to deal with just the file. I mean I wish 
 * to provide the clients with certain functionality that help them to deal 
 * with the files that have been created and those that store the html for the 
 * specified URLs.
 * Example methods are below.
 * I need to confirm the directory structure with my professors.
 *			
 * @author adityam
 *
 * @TODO : This file doesnt have any specific functionality and hence it should be renamed
 * as a utility filr, with additional functions. 
 */

public class Reads {
	
	/*
	 * This method reads the urls stored in a file and puts them in a LinkedList. 
	*/
	
	protected static LinkedList<String>  ReadUrl(String filename) 
			throws Exception {
		/*
		 * I need a list and then each time I add something to the list 
		 * just do it via the lists add method. Gives me more flexibility
		*/
		//Creating an empty linked list, that will store the urls from 
		//the specified file.
		LinkedList<String> urls = new LinkedList<String>();
		
		//Open the data stream to the url to get ready to obtain the urls 
		//from the file.
		DataInputStream in = new DataInputStream(new FileInputStream(filename));
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine = null;
		
		//Gets the urls from the file.
		while((strLine = br.readLine()) != null) {
			urls.add(strLine);
		}
		in.close();
		return urls;
	}
	
	/*
	 * Helper Method;
	 * I needed a way to create a name for the file where I wanted to store the 
	 * html, so I cut the url to remove the "http://www" which is 11 th index. 
	 * The name ends at ".com". The only problem here is that my filenames may
	 * be really long, or invalid due to certain character that  cannot be used
	 * in filenames, but can be used in a url eg "%.
	 * My basic aim here is to create a name for the file that will store the 
	 * html of the particular file. 
	 * I would rather than create indexed files, keep the name of the file the 
	 * same as the url the only. This gives me the future functionality to trace
	 * where the file came from. In case I wish to go back to the site
	 * and check that.
	 * 
	 * I also need to get rid of the http:// or https:// or ftp://. In other 
	 * words my file name must begin with www or de, or any other sub-domain. 
	 * 
	 * The last thing that I need is to actually get rid of the . In the url 
	 * since a . is not accepted as a valid character in the name of a file.
	 */
	public static String cutUrl(String str)
	{
		// Getting rid of the protocol and the subdomain.
		String updated_str = str.substring(str.indexOf("/")+2, str.length()-1);
		
		// Replacing all the "/" in the url and putting a "_" .
		updated_str = updated_str.replace('/', '_').replace('?', '(');
		
		return updated_str;
				
	}
	
	
	//Returns the url for the certain connection, mainly for testing purposes.
	public static String  printHTML(URLConnection connection) throws IOException {
		String html = "";
		String str;

		InputStream in= connection.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		while ((str = br.readLine()) != null) { 
			html +=str+"\n";  
		}
		return html;		
	}
}
