import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * This class used to backup database
 */
public class DbBackup extends DbBasic {
	private DatabaseMetaData data;
	private String dbName;
	private DbBasic db_backup;
	
	/** Constructor
	 *  @param dbName the name of the database
	 */
	DbBackup(String dbName) throws SQLException {
		super(dbName);
		this.dbName = dbName.split("\\." )[0];
		data = con.getMetaData();
		dropFile();
	}
	
	/** 
	 *  This method is used to backup database
	 *  @throws SQLException
	 *  @throws IOException
	 */
	public void backUp() throws SQLException, IOException {	
		showDatabaseInfo();
		System.out.println("Processing...");
		File file = new File(dbName+"-backup.db");
		file.createNewFile();
		db_backup = new DbBasic(dbName+"-backup.db");

		ArrayList<String> tableNames = getOrderedCreateTableNames();
		ArrayList<String> indexInfo = new ArrayList<String>();
		// CREATE tables and get INDEX info
		for(String table : tableNames) {
			createTable(table);
			getIndexTo(table,indexInfo);		
		}
		if(indexInfo.size() > 0) 
			indexInfo.set(indexInfo.size() - 1, indexInfo.get(indexInfo.size() - 1) + ");");
		
		for(String index: indexInfo) {
			saveToFile(index, dbName + "-backup.sql");
			db_backup.con.createStatement().executeUpdate(index);
		}
		saveToFile("\n\n", dbName + "-backup.sql");
		for(String table : tableNames) 
			createInsertStatements(table);	
		db_backup.con.commit(); 
		db_backup.con.close();
	}
	
	/** This method is used to create tables of database 
	 * 
	 * @param tableName the name of the table
	 * @throws SQLException
	 * @throws IOException
	 */
	private void createTable(String tableName) throws SQLException, IOException {
		// Get table infomation
		String statement = "CREATE TABLE \"" + tableName + "\" (\n";
		ResultSet columnRS = data.getColumns(null, null, tableName, null);
		boolean first = true;
		while (columnRS.next()) {
			String column=columnRS.getString("COLUMN_NAME");
			String Type=columnRS.getString("TYPE_NAME");
			String nullorNot=columnRS.getString("NULLABLE");
			if (first) {
				statement += "\t" + column + " " + Type;
				first = false;
			}
			else 
				statement += ",\n\t" + column + " " + Type;
			if (nullorNot.equals("0")) 
				statement +=" not null";
		}

		ArrayList<String> primaryKeys = new ArrayList<String>();
		ResultSet keyRS = data.getPrimaryKeys(null, null, tableName);
		while (keyRS.next()) 
			primaryKeys.add(keyRS.getString("COLUMN_NAME"));
		// Add primary key(s)
		for(int i = 0; i < primaryKeys.size(); i++) {
			if (i == 0)
				statement += ",\n\tPRIMARY KEY (" + primaryKeys.get(i);
			else
				statement += ", " + primaryKeys.get(i);
		}
		if(primaryKeys.size() > 0)
			statement += ")";
		keyRS.close();
		// Add foreign key(s)
		ResultSet foreignKeysRS = data.getImportedKeys(null, null, tableName);
		first = true;
		while (foreignKeysRS.next()) {
			String referPKTable = foreignKeysRS.getString("PKTABLE_NAME");
			String referPKColumn = foreignKeysRS.getString("PKCOLUMN_NAME");
			String foreignKey = foreignKeysRS.getString("FKCOLUMN_NAME");
			if(first) {
				statement += ",\n\tFOREIGN KEY ("+ foreignKey+") REFERENCES \"" + referPKTable + "\"(" + referPKColumn + ")";
				first = false;
			}else  //For other foreign keys
				statement += ",\n\tFOREIGN KEY ("+ foreignKey+") REFERENCES \"" + referPKTable + "\"(" + referPKColumn + ")";
			String deleteRule = foreignKeysRS.getString("DELETE_RULE");
			statement += " ON DELETE";
			if(deleteRule.equals("0"))
				statement += " CASCADE";
			else if(deleteRule.equals("1"))
				statement += " RESTRICT";
			else if(deleteRule.equals("2"))
				statement += " SET NULL";
			else if(deleteRule.equals("3"))	
				statement += " NO ACTION";
			else if(deleteRule.equals("4"))
				statement += " SET DEFAULT";
			String updateRule = foreignKeysRS.getString("UPDATE_RULE");
			statement += " ON UPDATE";
			if(updateRule.equals("0"))
				statement += " CASCADE";
			else if(updateRule.equals("1"))
				statement += " RESTRICT";
			else if(updateRule.equals("2"))
				statement += " SET NULL";
			else if(updateRule.equals("3"))
				statement += " NO ACTION";
			else if(updateRule.equals("4"))	
				statement += " SET DEFAULT";
		}	
		foreignKeysRS.close();
		statement += "\n);\n";
		
		// Save to the file
		saveToFile(statement, dbName + "-backup.sql");
		Statement stmt = db_backup.con.createStatement();
		stmt.executeUpdate(statement);
	}
	
	/** This method is used to create a reference relation tree with dfs, by getting the depth of each table.
	 * 
	 * @param i the i-th table
	 * @param tabelNames the list of table names.
	 * @param depth the maximum depth of each table.
	 * @param d current depth.
	 */ 
	private void dfs(int i, ArrayList<String> tabelNames,ArrayList<ArrayList<String>> tableRefers,Map<String, Integer> depth, int d){
		depth.put(tabelNames.get(i), Math.max(d, depth.get(tabelNames.get(i))));
		if(tableRefers.get(i).size() == 0)
			return;
		for(String name: tableRefers.get(i))
			dfs(tabelNames.indexOf(name), tabelNames, tableRefers, depth, d+1);
		return;
	}
	
	/** Get the ordered table names considering Foreign Keys 
	 * 
	 *  @return the ordered table names
	 *  @throws SQLException
	 */
	private ArrayList<String> getOrderedCreateTableNames() throws SQLException {
		ArrayList<String> orderedTableNames = new ArrayList<String>();
		ResultSet tableRS = data.getTables(null, null, null, new String[] { "TABLE", "VIEW" });
		// Get table names
		ArrayList<String> tabelNames = new ArrayList<String>();
		ArrayList<ArrayList<String>> tableRefers = new ArrayList<ArrayList<String>>();
		Map<String, Integer> depth = new HashMap<String, Integer>();
		while (tableRS.next()){
			ArrayList<String> tableRefer = new ArrayList<String>();
			String tableName = tableRS.getString("TABLE_NAME");
			depth.put(tableName, 0);
			tabelNames.add(tableName);
			ResultSet fks = data.getImportedKeys(null, null, tableName);
			while (fks.next()) {
				if(!fks.getString("PKTABLE_NAME").equals(tableName)) // not self-reference
					tableRefer.add(fks.getString("PKTABLE_NAME"));
			}
			tableRefers.add(tableRefer);
		}
		for(int i = 0; i < tableRefers.size(); i++) 
			dfs(i, tabelNames, tableRefers, depth, 0);
		ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>(depth.entrySet());
		Collections.sort(list,new Comparator<Map.Entry<String,Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,Map.Entry<String, Integer> o2) {
                return -o1.getValue().compareTo(o2.getValue());
            }
        });
		for(Map.Entry<String, Integer> entry: list) 
			orderedTableNames.add(entry.getKey());
		tableRS.close();
		return orderedTableNames;
	}
	
	/** Get the INDEX statement from Table 
	 * 
	 *  @param tableName the name of the table
	 *  @param indexInfo the index infomation
	 * 	@throws SQLException 
	 */
	private void getIndexTo(String table, ArrayList<String> indexInfo) throws SQLException {
		ResultSet indexRS = data.getIndexInfo(null, null, table, false, true);
		String index = "";
		while(indexRS.next()) {
			// Skip auto-index
			if(indexRS.getString("INDEX_NAME").contains("sqlite_autoindex_"))
				continue;
			if(indexRS.getString("ORDINAL_POSITION").equals("1")) { // first column
				// Add ");\n" to the previous index
				if(!(indexInfo.size() == 0))
					indexInfo.set(indexInfo.size() - 1, indexInfo.get(indexInfo.size() - 1) + ");\n");
				
				// CREATE INDEX statement
				if(indexRS.getString("NON_UNIQUE").equals("1")) 
					index = "CREATE INDEX " + indexRS.getString("INDEX_NAME") + " ON " +indexRS.getString("TABLE_NAME") + " (" + indexRS.getString("COLUMN_NAME");
				else 
					index = "CREATE UNIQUE INDEX " + indexRS.getString("INDEX_NAME") + " ON " + indexRS.getString("TABLE_NAME") + " (" + indexRS.getString("COLUMN_NAME");
				// deal with ASC/DESC
				String ASC_OR_DESC = indexRS.getString("ASC_OR_DESC");
				if(ASC_OR_DESC == null)
					index += "";
				else if(ASC_OR_DESC.equals("D"))
					index += " DESC";
				else if(ASC_OR_DESC.equals("A"))
					index += " ASC";
				
				indexInfo.add(index);
			}else { // rest columns
				String ASC_OR_DESC = indexRS.getString("ASC_OR_DESC");
				if(ASC_OR_DESC == null)
					indexInfo.set(indexInfo.size() - 1, indexInfo.get(indexInfo.size() - 1) + ", " + indexRS.getString("COLUMN_NAME") + "");
				else if(ASC_OR_DESC.equals("A"))
					indexInfo.set(indexInfo.size() - 1, indexInfo.get(indexInfo.size() - 1) + ", " + indexRS.getString("COLUMN_NAME") + " ASC");
				else if(ASC_OR_DESC.equals("D"))
					indexInfo.set(indexInfo.size() - 1, indexInfo.get(indexInfo.size() - 1) + ", " + indexRS.getString("COLUMN_NAME") + " DESC");
			}
		}
		indexRS.close();
	}
	
	/** Create the INSERT statement 
	 * 
	 *  @param table the name of the table
	 *  @throws SQLException
	 *  @throws IOException
	 */
	private void createInsertStatements(String table) throws SQLException, IOException {
		String sql = "SELECT * FROM \"" + table + "\";";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);	
		ResultSetMetaData resultSetMetaData = rs.getMetaData();		
		int columnCount = resultSetMetaData.getColumnCount();
		
		while(rs.next()) {
			String insert = "INSERT INTO \"" + table + "\" VALUES (";
			for(int i = 1; i <= columnCount; i++) {
				String result = rs.getString(i);
				// avoid mistake about ' and "
				if(result != null)
					result = result.replaceAll("\"", "\'");
				if(i == columnCount) {
					insert += "\"" + result + "\");\n";
				}
				else {
					insert = insert + "\"" + result +"\", ";
				}
			}
			saveToFile(insert, dbName + "-backup.sql");
			db_backup.con.createStatement().executeUpdate(insert);
		}
		rs.close();
	}
	
	/** Drop the existed files of backup info in the directory
	 */
	private void dropFile() {
		System.out.println("=================Check file existent================");		
		drop(dbName + "-backup.sql");	
		drop(dbName + "-backup.db");
	}
	
	/** Drop the file if it exist 
	 * 
	 * @param fileName the name of the file
	 */
	private void drop(String fileName) {
		File f = new File(fileName);
		if(f.exists()) {
			f.delete();
			System.out.println("Dropped " + fileName);
		}
	}
	
	/** Show the information of this Database 
	 * 
	 *  @throws SQLException
	 */
	private void showDatabaseInfo() throws SQLException {
		System.out.println("==================Database Info==================");
		System.out.println("Database Product: " + data.getDatabaseProductName() + " "+ data.getDatabaseProductVersion());
		System.out.println("Database directory: "+dbName + ".db");
		// get the directory of  database
		String temp = dbName.split("\\\\")[dbName.split("\\\\").length - 1];
		String output = temp.length()==dbName.length()? "Current folder" : dbName.split(temp)[0];
		System.out.println("Output directory: " + output);
		System.out.println("=================================================");	
	}
	
	/** Save data to file.
	 * 
	 *  @param data the data to be saved
	 *  @param file the name of the file to save the data
	 *  @throws IOException
	 */
	private void saveToFile(String data, String file) throws IOException {
		File f =new File(file);
		f.createNewFile();
		FileWriter fileWritter = new FileWriter(file, true);
		fileWritter.write(data);
		fileWritter.close();
	}
}
