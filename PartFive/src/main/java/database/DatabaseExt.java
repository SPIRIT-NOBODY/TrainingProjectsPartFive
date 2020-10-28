package database;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import part_four.task_one.database.Database;

public class DatabaseExt extends Database {

	public DatabaseExt() throws SQLException, IOException {
		super();
	}	
	
	/**
	 * Delete row from table by conditions, user into popup frame
	 * 
	 * @param condition string sql condition use with 'WHERE'
	 * @param values    string array with string value for delete
	 * 
	 * @throws SQLException {@inheritDoc}
	 */
	@Override
	public void deleteByCondition(String condition, String[] values) throws SQLException {

		String [] prepareQue = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			prepareQue[i] = "?";
		}
		String sql = String.format("DELETE FROM %s WHERE %s %s", super.tableName,condition,  "(" + String.join(", ", prepareQue) + ")");
		PreparedStatement preparedStatement = super.connection.prepareStatement(sql);
		for (int i = 0; i < values.length; i++) {
			preparedStatement.setString(i + 1, values[i]);
		}		
		preparedStatement.executeUpdate();

	}
	

}
