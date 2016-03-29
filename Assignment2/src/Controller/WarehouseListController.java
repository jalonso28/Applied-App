package Controller;

import View.InventoryListView;
import View.WarehouseDetailView;
import View.WarehouseListView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JDesktopPane;

import Database.GatewayException;
import Database.InventoryTableGateway;
import Database.WarehouseTableGateway;
import Model.Warehouse;
import Observer.InventoryObserver;
import Observer.WarehouseObserver;

public class WarehouseListController implements ActionListener{

	private ArrayList<Warehouse> warehouses;
	
	private Warehouse sWarehouse;
	
	WarehouseListView wListView;
	
	ArrayList<String> list;
	
	private WarehouseDetailView detailView;
	
	private InventoryListController iListController;
	
	private InventoryListView inventoryView;
	
	WarehouseTableGateway wTableGateway;
	
	private InventoryTableGateway iTableGateway;
	
	private InventoryObserver iObserver;
	
	
	private WarehouseObserver wObserver;
	
	JDesktopPane jDesktopPane;
	
	private long invalidID = 0;


	public WarehouseListController(JDesktopPane jDesktopPane, WarehouseObserver wObserver, 
			WarehouseTableGateway wTableGateway, InventoryObserver o, InventoryTableGateway i) throws GatewayException {
		this.iTableGateway = i;
		this.iObserver = o;
		this.wTableGateway = wTableGateway;
		this.jDesktopPane = jDesktopPane; 
		this.wObserver = wObserver;
		list = new ArrayList<String>();
		warehouses = wTableGateway.fetchWarehouses();
		this.wTableGateway = wTableGateway;
		InventoryListController iListController = 
				new InventoryListController(jDesktopPane, iObserver, iTableGateway);
	}
	
	public ArrayList<String> getWarehouseList(){
		
		for(int i = 0; i < warehouses.size(); i++){
			list.add(Long.toString(warehouses.get(i).getId()) + "   " + warehouses.get(i).getName());
		}
		
		return list;	
		
	}
	
	public int addWarehouse(String name, String address, String city, String state, String zip, int capacity) throws GatewayException {
		for(int i = 0; i < warehouses.size(); i++) {
			if(warehouses.get(i).getName().equals(name)) {
				return 1;
			}
		}
		Warehouse w = new Warehouse(invalidID, name, address, city, state, zip, capacity);
		wTableGateway.insertWarehouse(w);
		wObserver.addToList(Long.toString(w.getId()) + "   " + name );
		return 0;
	}
	
	public int editWarehouse(long id, String name, String address, String city, String state, String zip, int capacity) {
		int i;
		if (name == null || address == null || city == null || state == null || zip == null) {
			return 1;
		}
		for(i = 0; i < warehouses.size(); i++) {
			if (warehouses.get(i).getId() == id) {
				warehouses.get(i).setAddress(address);
				warehouses.get(i).setName(name);
				warehouses.get(i).setCapacity(capacity);
				warehouses.get(i).setCity(city);
				warehouses.get(i).setState(state);
				warehouses.get(i).setZip(zip);
				try {
					wTableGateway.saveWarehouse(warehouses.get(i));
				} catch (GatewayException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
		wObserver.updateList(i, Long.toString(warehouses.get(i).getId()) + "   " + name);
		wObserver.updateDetailViews(warehouses.get(i).getId());
		return 0;
	}


	public void actionPerformed(ActionEvent e) {
		if ("add".equals(e.getActionCommand())){
			detailView = new WarehouseDetailView(jDesktopPane, this);
			wObserver.addDetailView(detailView);
		} else if ("edit".equals(e.getActionCommand()) && (wListView.getSelected() != null)){
			for(int i = 0; i < warehouses.size(); i++) {
				if(warehouses.get(i).getId() == Long.parseLong(wListView.getSelected())) {
					sWarehouse = warehouses.get(i);
					break;
				}
			}
			detailView = new WarehouseDetailView(jDesktopPane, this, sWarehouse);
		} else if ("delete".equals(e.getActionCommand()) && (wListView.getSelected() != null)){
			for(int i = 0; i < warehouses.size(); i++) {
				if(warehouses.get(i).getId() == Long.parseLong(wListView.getSelected())) {
					try {
						wTableGateway.deleteWarehouse(warehouses.get(i).getId());
					} catch (GatewayException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					wListView.remove(i);
					warehouses.remove(i);
					break;
				}
			}
			wListView.setSelected(null);
		} else if("inventory".equals(e.getActionCommand()) && (wListView.getSelected() != null)) {
			for(int i = 0; i < warehouses.size(); i++) {
				if(warehouses.get(i).getId() == Long.parseLong(wListView.getSelected())) {
					inventoryView = new InventoryListView(jDesktopPane, iListController, warehouses.get(i));
				}
			}
		}
	}

	public void setList(WarehouseListView wListView) {
		this.wListView = wListView;
	}
}
