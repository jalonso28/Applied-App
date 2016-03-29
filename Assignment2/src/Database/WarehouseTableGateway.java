package Database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import Model.Warehouse;

/**
 * Table Data Gateway for Warehouse DB table
 * DB interface between WarehouseList and Warehouse DB table
 *
 */
public class WarehouseTableGateway {
	private static final boolean DEBUG = true;

	/**
	 * external DB connection
	 */
	private Connection conn = null;
	
	/**
	 * Constructor: creates database connection
	 * @throws GatewayException
	 */
	public WarehouseTableGateway() throws GatewayException {
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
	 * Returns new warehouse instance from db using warehouse's id
	 * @param id Id of the warehouse in the db to fetch
	 * @return new Warehouse instance with that warehouse's data from db
	 */
	public Warehouse fetchWarehouse(long id) throws GatewayException {
		Warehouse w = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			//fetch warehouse
			st = conn.prepareStatement("select * from Warehouses where id = ? ");
			st.setLong(1, id);
			rs = st.executeQuery();
			rs.next();
			w = new Warehouse(rs.getLong("id"), rs.getString("name"), rs.getString("address"), 
					rs.getString("city"), rs.getString("state"), rs.getString("zip"), rs.getInt("capacity"));
			//give warehouse object a reference to this gateway
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
		return w;
	}
	
	/**
	 * Saves existing warehouse to database.
	 * To add a new warehouse, call the addWarehouse method 
	 * @param w
	 * @throws GatewayException
	 */
	public void saveWarehouse(Warehouse w) throws GatewayException {
		//execute the update and throw exception if any problem
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update Warehouses "
					+ " set id = ?, name = ?, address= ?, city = ?, state = ?, zip = ?, capacity = ?"
					+ " where id = ? ");
			st.setLong(1, w.getId());
			st.setString(2, w.getName());
			st.setString(3,  w.getAddress());
			st.setString(4, w.getCity());
			st.setString(5, w.getState());
			st.setString(6, w.getZip());
			st.setDouble(7, w.getCapacity());
			st.setLong(8, w.getId());
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
	 * Fetch a resultset of all warehouse rows in db and populate a collection with them.
	 * All warehouse instances are given a reference to this Gateway.
	 * @return a List of Warehouse objects (ArrayList)
	 * @throws GatewayException
	 */
	public ArrayList<Warehouse> fetchWarehouses() throws GatewayException {
		ArrayList<Warehouse> ret = new ArrayList<Warehouse>();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			//fetch warehouse
			st = conn.prepareStatement("select * from Warehouses");
			rs = st.executeQuery();
			//add each to list of warehouse to return
			while(rs.next()) {
				Warehouse w = new Warehouse(rs.getLong("id"), rs.getString("name"), rs.getString("address"), 
						rs.getString("city"), rs.getString("state"), rs.getString("zip"), rs.getInt("capacity"));
				//give each Warehouse object a reference to this gateway
				//makes model-level saving much simpler (model can tell the gateway to save to the database)
				//instead of having to go back through the WarehouseList and then back to the model for notifying observers
				ret.add(w);
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
	
	public long insertWarehouse(Warehouse w) throws GatewayException {
		//init new id to invalid
		long newId = 0;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("insert into Warehouses (id, name, address, city, state, zip, capacity) "
					+ " values (?,?,?,?,?,?,?) ", PreparedStatement.RETURN_GENERATED_KEYS);
			st.setLong(1, w.getId());
			st.setString(2, w.getName().trim());
			st.setString(3,  w.getAddress().trim());
			st.setString(4, w.getCity().trim());
			st.setString(5, w.getState().trim());
			st.setString(6, w.getZip().trim());
			st.setDouble(7, w.getCapacity());
			st.executeUpdate();
			//get the generated key
			rs = st.getGeneratedKeys();
			if(rs != null && rs.next()) {
			    newId = rs.getLong(1);
			    w.setId(newId);
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
	
	public void deleteWarehouse(long id) throws GatewayException {
		PreparedStatement st = null;
		try {
			//turn off autocommit to start the tx
			conn.setAutoCommit(false);

			st = conn.prepareStatement("delete from Warehouses where id = ? ");
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
