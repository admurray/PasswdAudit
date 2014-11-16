package processHtml;

import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
/**
 * 
 * This class takes in a file that stores all the passwords and checks each of 
 * these passwords against the web-site. As soon as the password lets the user 
 * log in the password is returned to the user. This class is also the outer 
 * class to the inner class Pass which forms the basis for the tryIt method. 
 * The tryIt method is the one that posts the POST and checks if a successful
 * login has taken place. To do this is uses other methods too.
 * 
 * @author adityam	:
 * 
 */


public class PasswdTest 
{		
		/*
		 *  The executor service required to manage the threads that have been 
		 *  created. I have basically done this because we wish to limit the 
		 *  number of threads that we have created. If I keep creating threads 
		 *  it seems to crash the program. Hence I need to limit the number 
		 *  of threads using a ThreadPool which is a static method within 
		 *  Executor, hence the executor is required.
		 */ 
		ExecutorService executor ;
		Pass posting = new Pass();
		FutureTask<Boolean> future;
		
		/**
		 * Basic constructor, just initializes the thread pool. I have given it
		 * the limit 100 but it may and most probably will be user specified.
		 */
		PasswdTest(){
			executor = Executors.newFixedThreadPool(10);
		}
		
		/**
		 * Stops the executor, when required to do so.
		 */
		void stop () {
			executor.shutdown();
		}
		
		/**
		 * 
		 * @param post Its the post that you need to POST to the web-site.
		 * @param username The username used in the post to attempt the login
		 * @param password It stores the password for the thread that is 
		 * running.This is required in order to be able to print the 
		 * password and let the user know which one has passed 	
		 * @param site The site where the POST is to be made.
		 * @param filename The filename where the passwords are stored. These 
		 * stored passwords are used for the brute force
		 * @param login_cookie The format of the cookie required for the POST 
		 * to be successful.
		 * @throws InterruptedException Haven't handled the exception as yet.
		 */
		void tryIt(final String post, final String username, 
				final LinkedList<String> password_list, final String login_page, 
				final String password_file, final String login_cookie, final String login_iden) 
						throws InterruptedException 
		{
			while(!password_list.isEmpty())
			{
				final String password = password_list.remove();
				//I am creating a future task
				future = new FutureTask<Boolean>(
						new Callable<Boolean>()
						{
							boolean flag;
							public Boolean call()
							{
								try {
									final String post_modified = UtilityPasswdTest.replaceUserPass(post, username, password);
									//System.out.println("The username from the list : "+username+" and the password : "+password);
									//System.out.println("The modified post  : "+post_modified);
									//System.out.println("The username for the segment : "+username);
									flag = posting.getPostLoginHtml(post_modified, username, 
											password, login_page, password_file, 
											login_cookie, login_iden);
									System.out.println("Username : "+username+"	Password : "+password);
									if (flag == true)
										stop();//System.exit(0);
									return flag;
								} catch (Exception e) {
									e.printStackTrace();
									System.out.println(password);
									//System.out.println("Something wrong my friend");
									//Here instead of System.exit I need to stop the current thread pool.
									executor.shutdown();
									return true;
								}
							}
						});
				this.executor.execute(future);
			}
		}
}