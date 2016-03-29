package Database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import Model.Part;

public class PartTableGateway {
	private static final boolean DEBUG = true;

	/**
	 * external DB connection
	 */
	private Connection conn = null;
	
	/**
	 * Constructor: creates database connection
	 * @throws GatewayException
	 */
	public PartTableGateway() throws GatewayException {
		//read the properties file to establish the db connection
		DataSource ds = null;
		try {
			ds = getDataSource();
		} catch (RuntimeException | IOException e1) {
			throw new GatewayException(e1.getMessage());
		}
		if(ds == null) {
        	throw new GatewayException("Datasource is null!");
        }
		try {
        	conn = ds.getConnection();
		} catch (SQLException e) {
			throw new GatewayException("SQL Error: " + e.getMessage());
		}
	}

	/**
	 * Returns new part instance from db using part's id
	 * @param id Id of the part in the db to fetch
	 * @return new Part instance with that part's data from db
	 */
	public Part fetchPart(long id) throws GatewayException {
		Part s = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			//fetch part
			st = conn.prepareStatement("select * from Parts where partId = ? ");
			st.setLong(1, id);
			rs = st.executeQuery();
			rs.next();
			s = new Part(rs.getLong("partId"), rs.getString("partNum"), rs.getString("partName"), rs.getString("unit"));
			//give part object a reference to this gateway
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new GatewayException(e.getMessage());
		} finally {
			//clean up
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException e) {
				throw new GatewayException("SQL Error: " + e.getMessage());
			}
		}
		return s;
	}
	
	/**
	 * Saves existing part to database.
	 * To add a new part, call the addPart method 
	 * @param w
	 * @throws GatewayException
	 */
	public void savePart(Part s) throws GatewayException {
		//execute the update and throw exception if any problem
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update Parts "
					+ " set partId = ?, partNum = ?, partName= ?, unit = ?"
					+ " where partId = ? ");
			st.setLong(1, s.getId());
			st.setString(2, s.getPartNum());
			st.setString(3,  s.getPartName());
			st.setString(4, s.getUnit());
			st.setLong(5, s.getId());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new GatewayException(e.getMessage());
		} finally {
			//clean up
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				throw new GatewayException("SQL Error: " + e.getMessage());
			}
		}
	}
	
	/**
	 * Fetch a resultset of all part rows in db and populate a collection with them.
	 * All part instances are given a reference to this Gateway.
	 * @return a List of Part objects (ArrayList)
	 * @throws GatewayException
	 */
	public ArrayList<Part> fetchParts() throws GatewayException {
		ArrayList<Part> ret = new ArrayList<Part>();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			//fetch parts
			st = conn.prepareStatement("select * from Parts");
			rs = st.executeQuery();
			//add each to list of people to return
			while(rs.next()) {
				Part s = new Part(rs.getLong("partId"), rs.getString("partNum"), rs.getString("partName"), rs.getString("unit"));
				//give each Part object a reference to this gateway
				//makes model-level saving much simpler (model can tell the gateway to save to the database)
				//instead of having to go back through the PartList and then back to the model for notifying observers
				ret.add(s);
			}
		} catch (SQLException e) {
			throw new GatewayException(e.getMessage());
		} finally {
			//clean up
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException e) {
				throw new GatewayException("SQL Error: " + e.getMessage());
			}
		}
		
		return ret;
	}
	
	public void close() {
		if(DEBUG)
			System.out.println("Closing db connection...");
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * create a MySQL datasource with credentials and DB URL in db.properties file
	 * @return
	 * @throws RuntimeException
	 * @throws IOException
	 */
	public DataSource getDataSource() throws RuntimeException, IOException {
		//read db credentials from properties file
		Properties props = new Properties();
		FileInputStream fis = null;
        fis = new FileInputStream("db.properties");
        props.load(fis);
        fis.close();
        
        //create the datasource
        MysqlDataSource mysqlDS = new MysqlDataSource();
        mysqlDS.setURL(props.getProperty("MYSQL_DB_URL"));
        mysqlDS.setUser(props.getProperty("MYSQL_DB_USERNAME"));
        mysqlDS.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
        return mysqlDS;
	}
	
	public long insertPart(Part s) throws GatewayException {
		//init new id to invalid
		long newId = 0;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("insert into Parts (partId, partNum, partName, unit) "
					+ " values (?,?,?,?) ", PreparedStatement.RETURN_GENERATED_KEYS);
			st.setLong(1, s.getId());
			st.setString(2, s.getPartNum().trim());
			st.setString(3,  s.getPartName().trim());
			st.setString(4, s.getUnit().trim());
			st.executeUpdate();
			//get the generated key
			rs = st.getGeneratedKeys();
			if(rs != null && rs.next()) {
			    newId = rs.getLong(1);
			    s.setId(newId);
			} else {
				throw new GatewayException("Could not fetch new record Id");
			}
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new GatewayException(e.getMessage());
		} finally {
			//clean up
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				throw new GatewayException("SQL Error: " + e.getMessage());
			}
		}
		return newId;
	}
	
	public void deletePart(long id) throws GatewayException {
		PreparedStatement st = null;
		try {
			//turn off autocommit to start the tx
			conn.setAutoCommit(false);

			st = conn.prepareStatement("delete from Parts where partId = ? ");
			st.setLong(1, id);
			st.executeUpdate();
			
			//if we get here, everything worked without exception so commit the changes
			conn.commit();
		} catch (SQLException e) {
			//roll the tx back
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new GatewayException(e1.getMessage());
			}
			throw new GatewayException(e.getMessage());
		} finally {
			//clean up
			try {
				if(st != null)
					st.close();
				//turn autocommit on again regardless if commit or rollback
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				throw new GatewayException(e.getMessage());
			}
		}
	}

}
