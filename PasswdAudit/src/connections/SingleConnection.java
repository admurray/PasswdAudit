package connections;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class SingleConnection 
{
	private String postReq;
	private URL url;
	private HttpURLConnection connection;
	
	public SingleConnection()
	{
		this(null, null, null);
	}
	
	
	public SingleConnection(String postReq, URL url)
	{
		this.postReq = postReq;
		this.url = url;
		try {
			this.connection = (HttpURLConnection)url.openConnection();
		} catch (IOException e) {
			System.out.println("Problem in Construction of SingleConnection");
			e.printStackTrace();
		}
	}
	
	public SingleConnection(String postReq, URL url, HttpURLConnection connection)
	{
		this.postReq = postReq;
		this.url = url;
		this.connection = connection;
	}

	public String getPostReq() {
		return postReq;
	}

	public void setPostReq(String postReq) {
		this.postReq = postReq;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public HttpURLConnection getConnection() {
		return connection;
	}

	public void setConnection(HttpURLConnection connection) {
		this.connection = connection;
	}
	
	public boolean isConnected()
	{
		return (this.connection == null);
	}
	
	public HttpURLConnection setConnect()
	{
		if(this.getConnection() == null)
		{
			try {
				return (HttpURLConnection) this.url.openConnection();
			} catch (IOException e) {
				System.out.println("Error creating connection is setConnect");
				e.printStackTrace();
				return null;
			}
		}
		else
		{
			return this.getConnection();
		}
		
	}
}
