package Model;

public class Warehouse {
	private long id;
	
	private String name, address, city, state, zip;
	
	private double capacity;
	
	public Warehouse(long id, String name, String address, String city, String state, String zip, double capacity){
		this.id = id;
		this.name = name;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.capacity = capacity;		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public double getCapacity() {
		return capacity;
	}
	
	public void setID(long id) {
		this.id = id;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
}
