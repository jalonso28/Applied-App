package Model;

import Database.PartTableGateway;

public class Part {
	private long id;
	
	private String partNum, partName, vendor, unit, vendorPart;
	
	public Part(long id, String partNum, String partName, String unit) {
		this.id = id;
		this.setPartName(partName);
		this.setPartNum(partNum);
		this.setUnit(unit);
	}
	public Part(long id, String partNum, String partName, String vendor, String unit, String vendorPart) {
		this.id = id;
		this.setPartName(partName);
		this.setPartNum(partNum);
		this.setVendor(vendor);
		this.setUnit(unit);
		this.setVendorPart(vendorPart);
	}

	public String getPartNum() {
		return partNum;
	}

	public void setPartNum(String partNum) {
		this.partNum = partNum;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getVendorPart() {
		return vendorPart;
	}

	public void setVendorPart(String vendorPart) {
		this.vendorPart = vendorPart;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}
}
