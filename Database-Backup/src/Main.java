import java.io.IOException;
import java.sql.SQLException;

/**
 * This class is define the database which would be used and execute the backup
 */
public class Main {
	DbBackup myDbUser = null;

	private void go(String dbName) throws SQLException, IOException
	{	
		System.out.println("In go...");
		myDbUser = new DbBackup(dbName);	
		myDbUser.backUp();	
		System.out.println("Processing over");

		myDbUser.close();
	}; // end of method "go"

	public static void main(String[] args) throws SQLException, IOException
	{
		if(args.length == 0)
		{
			System.out.println("Please attach the database address using \"java -jar tool.jar xxx.db\"");
			System.exit(0);
		}
		Main myMain = new Main();
		myMain.go(args[0]);
	} // end of method "main"

} // end of class "Main"
