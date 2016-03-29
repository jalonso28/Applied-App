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

import Model.Inventory;
import Model.Part;
import Model.Warehouse;

public class InventoryTableGateway {
		private static final boolean DEBUG = true;
		
	private Part part;

	/**
	 * external DB connection
	 */
	private Connection conn = null;
	
	/**
	 * Constructor: creates database connection
	 * @throws GatewayException
	 */
	public InventoryTableGateway() throws GatewayException {
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
	 * Returns new inventory instance from db using inventory's id
	 * @param id Id of the inventory in the db to fetch
	 * @return new Inventory instance with that inventory's data from db
	 */
	public Inventory fetchInventory(long id) throws GatewayException {
		Inventory s = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			//fetch inventory
			st = conn.prepareStatement("select * from Inventory where inventoryId = ? ");
			st.setLong(1, id);
			rs = st.executeQuery();
			rs.next();
			s = new Inventory(rs.getLong("inventoryId"), rs.getLong("warehouseId"), 
					rs.getLong("partId"), rs.getDouble("quantity"));
			//give inventory object a reference to this gateway
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
	 * Saves existing inventory to database.
	 * To add a new inventory, call the addInventory method 
	 * @param w
	 * @throws GatewayException
	 */
	public void saveInventory(Inventory s) throws GatewayException {
		//execute the update and throw exception if any problem
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update Inventory "
					+ " set inventoryId = ?, warehouseId = ?, partId= ?, quantity = ?"
					+ " where inventoryId = ? ");
			st.setLong(1, s.getId());
			st.setLong(2, s.getWarehouseId());
			st.setLong(3,  s.getPartId());
			st.setDouble(4, s.getQuantity());
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
	
	public ArrayList<Inventory> setWarehouses(ArrayList<Inventory> i) {
		int index;
		for(index = 0; index < i.size(); index++) {
			Warehouse w = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				//fetch warehouse
				st = conn.prepareStatement("select * from Warehouses where id = ? ");
				st.setLong(1, i.get(index).getWarehouseId());
				rs = st.executeQuery();
				rs.next();
				w = new Warehouse(rs.getLong("id"), rs.getString("name"), rs.getString("address"), 
						rs.getString("city"), rs.getString("state"), rs.getString("zip"), rs.getInt("capacity"));
				//give warehouse object a reference to this gateway
				i.get(index).setWarehouse(w);
			} catch (SQLException e) {
				//e.printStackTrace();
				try {
					throw new GatewayException(e.getMessage());
				} catch (GatewayException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} finally {
				//clean up
				try {
					if(rs != null)
						rs.close();
					if(st != null)
						st.close();
				} catch (SQLException e) {
					try {
						throw new GatewayException("SQL Error: " + e.getMessage());
					} catch (GatewayException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
		return i;
	}
	
	public ArrayList<Inventory> setParts(ArrayList<Inventory> i) {
		int index;
		for(index = 0; index < i.size(); index++) {
			Part s = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				//fetch part
				st = conn.prepareStatement("select * from Parts where partId = ? ");
				st.setLong(1, i.get(index).getPartId());
				rs = st.executeQuery();
				rs.next();
				s = new Part(rs.getLong("partId"), rs.getString("partNum"), rs.getString("partName"), rs.getString("unit"));
				//give part object a reference to this gateway
				i.get(index).setPart(s);
			} catch (SQLException e) {
				//e.printStackTrace();
				try {
					throw new GatewayException(e.getMessage());
				} catch (GatewayException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} finally {
				//clean up
				try {
					if(rs != null)
						rs.close();
					if(st != null)
						st.close();
				} catch (SQLException e) {
					try {
						throw new GatewayException("SQL Error: " + e.getMessage());
					} catch (GatewayException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
		return i;
	}
	
	/**
	 * Fetch a resultset of all inventory rows in db and populate a collection with them.
	 * All inventory instances are given a reference to this Gateway.
	 * @return a List of Inventory objects (ArrayList)
	 * @throws GatewayException
	 */
	public ArrayList<Inventory> fetchInventories() throws GatewayException {
		ArrayList<Inventory> ret = new ArrayList<Inventory>();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			//fetch inventories
			st = conn.prepareStatement("select * from Inventory");
			rs = st.executeQuery();
			//add each to list of people to return
			while(rs.next()) {
				Inventory s = new Inventory(rs.getLong("inventoryId"), 
						rs.getLong("warehouseId"), rs.getLong("partId"), rs.getDouble("quantity"));
				//give each Inventory object a reference to this gateway
				//makes model-level saving much simpler (model can tell the gateway to save to the database)
				//instead of having to go back through the InventoryList and then back to the model for notifying observers
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
	
	public long insertInventory(Inventory s) throws GatewayException {
		//init new id to invalid
		long newId = 0;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("insert into Inventory (inventoryId, warehouseId, partId, quantity) "
					+ " values (?,?,?,?) ", PreparedStatement.RETURN_GENERATED_KEYS);
			st.setLong(1, s.getId());
			st.setLong(2, s.getWarehouseId());
			st.setLong(3,  s.getPartId());
			st.setDouble(4, s.getQuantity());
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
	
	public void deleteInventory(long id) throws GatewayException {
		PreparedStatement st = null;
		try {
			//turn off autocommit to start the tx
			conn.setAutoCommit(false);

			st = conn.prepareStatement("delete from Inventory where inventoryId = ? ");
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
