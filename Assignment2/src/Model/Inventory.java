package Model;

public class Inventory {
	private long id, warehouseId, partId;
	private Part part;
	private Warehouse warehouse;
	private double quantity;
	
	public Inventory(long id, long warehouseId, long partId, double quantity) {
		this.id = id;
		this.setPartId(partId);
		this.setWarehouseId(warehouseId);
		this.setQuantity(quantity);
	}
	
	public void setPart(Part part) {
		this.part = part;
	}

	public Part getPart() {
		return part;
	}
	
	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
	
	public Warehouse getWarehouse() {
		return warehouse;
	}
	
	public long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public long getPartId() {
		return partId;
	}

	public void setPartId(long partId) {
		this.partId = partId;
	}
}

