package connections;

import authentication.Auth;

public class SingleConnection 
{
	private String postReq;
	private Auth auth;
	
	public SingleConnection()
	{
		this(null, null);
	}
	
	
	/*
	 * The reson I would like to have connection being handled by the 
	 * Connection specific class, as opposed to the Auth class, is that
	 * I would like to reuse a connection if the past connection attempt failed.
	 * I want auth to just have the class dealing with a single auth. For now
	 * I will let auth open the connection.
	 */
	public SingleConnection(String postReq, Auth auth)
	{
		this.postReq = postReq;
		this.auth = auth;
	}
	
	public String getPostReq() {
		return postReq;
	}

	public void setPostReq(String postReq) {
		this.postReq = postReq;
	}

	public Auth getAuth()
	{
		return this.getAuth();
	}
	
	public void setAuth(Auth auth)
	{
		this.auth = auth;
	}
}
