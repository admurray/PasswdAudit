package main;

import java.io.IOException;
import java.net.URL;

import authentication.Auth;

public class AuthMain 
{
	public static void main(String[] args) throws IOException
	{
		URL page = new URL("http://localhost/zen-cart/index.php?main_page=login&zenid=jdt6d99b3i5pqp3esvbr5vj532");
		//These are test variables, the true variable will be pulled from a file/db
		String username = "juhi@juhi.com";
		String password = "password";
		Auth auth = new Auth(username, password, page);
		//We can create a single connection here id we need to have a post
		//statement to go with the conSnection. Using authenticator, I hardly
		//think we will need it.
		String val = auth.authenticate();
		System.out.println("The value is : "+val);
	
	}

}
