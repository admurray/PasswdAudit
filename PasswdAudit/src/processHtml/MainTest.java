package processHtml;

import java.util.LinkedList;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

/*
 * Arguements for Sugar CRM
 * -uf users.txt -pf passwords_test.txt 
 * -l http://127.0.0.1/SugarCE-Full-6.5.15/index.php?action=Login&module=Users 
 * -p module=Users&action=Authenticate&return_module=Users&return_action=Login&cant_login=&login_module=&login_action=&login_record=&login_token=&login_oauth_token=&login_mobile=&user_name=%%%username%%%&user_password=%%%password%%%&Login=Log+In 
 * -c PHPSESSID%%%PHPSESSID%%% 
 * -i "Log Out"
 */


public class MainTest
{
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		String username = "";
		System.out.println("------------------------------------------");
		
		CommandLine cmd = MainTest.setCommandLine(args);
		String posting = cmd.getOptionValue('p');
		// username 
		String username_file = cmd.getOptionValue("uf");
		// password file 
		String password_file = cmd.getOptionValue("pf");
		// login_page
		String login_page = cmd.getOptionValue('l');
		// the template for the cookie	
		String login_cookie = cmd.getOptionValue('c');	
		String login_iden = cmd.getOptionValue('i');
		
		LinkedList<String> user_list = Pass.makeListFromFile(username_file);
		
		PasswdTest testIt = new PasswdTest();
		while(!user_list.isEmpty()) {
		try
		{
			//The linkedList is created within the while, so that for each user all the passwords 
			//are tested. 
			LinkedList<String> password_list = Pass.makeListFromFile(password_file);

			username = user_list.remove();
			
			/*
			 * Must not modify the existing post since it has to be set 
			 * back to the one with the percentages for the next user.								
			 */
			String post = posting;
			//This statement needs to be put within the thread itself.
			//post = UtilityPasswdTest.replaceUserPass(post, username, 
			//		password);
			//System.out.println(post);
			//URLEncoder.encode(password, "UTF-8");
			testIt.tryIt(post, username, password_list ,login_page, password_file,
					login_cookie, login_iden);
		}
		catch (Exception e)
		{
			System.out.println("Something is wrong in the main method. ");
			e.printStackTrace();
		}
	}
		
		//testIt.stop();
	}
	
	/**
	 * 
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public static CommandLine setCommandLine(String[] args) throws Exception
	{
		/*
		 * The most necessary things that I need are the LOGIN PAGE, the POST 
		 * STATEMENT, and the PASSWORD LIST. If I do not provide the password 
		 * list then the PASSWORD is a must. Also the user name must be 
		 * provided in each case, if not then there must be a similar file 
		 * for the users.
		 * 
		 */
		
		/*
		 * There needs to be one change here and that is that once I get a string 
		 * and pass it to the methods dealing with the bruteforce and each connection 
		 * separately, I need to replace anything of the form "%%%-----%%%" where ----- 
		 * is the key required, for example it could be zenid or phpssid. I have a method 
		 * that deals with all this all I need to do here is get it working, filter 
		 * the %%% get the value between the two %%% and then find that value 
		 * from the headers or the html and then replace it in the place where it 
		 * is required. I keep doing this till no % sign remains.
		 * This needs to be done for the "post_statement" and the "login_cookie"
		 * I say need to do this separately for each and every connection since 
		 * from one connection to the other the values of the zenid and phpssid
		 * will surely change. 
		 * Hence that needs to be done in the metod where I create a connection 
		 * for a particular password and username and that is in the PASS class in 
		 * the method " getPostLoginHtml". I seriously need to change the names of 
		 * my methods as well.
		 */
		Options options = new Options();

		options.addOption("pf", "password_file",true, "the file that stores the " +
				"passwords to bruteforce our application with" );
		options.addOption("uf", "user_file", true, "the file that stores the list" +
				"of all the users");
		options.addOption("l", "login_page", true, "The url of the login" +
				" page  that we wish to attack");
		options.addOption("p", "post_statement", true, "The statement " +
				"to be posted.");
		options.addOption("c", "login_cookie", true,"The cookie statement " +
				"required for the connection");
		options.addOption("d", "difference", true, "The test that idendifies" +
				" uniquily whether login was successful or not.");
		//Login Identification
		options.addOption("i", "login_identification", true, "This is the login" +
				" identification, which is a unique identification for the login page ");
		
		CommandLineParser parser = new PosixParser();
		CommandLine cmd = parser.parse(options, args);
		
		return cmd;
		
	}
	
}
