package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JDesktopPane;

import Database.GatewayException;
import Database.InventoryTableGateway;
import Model.Inventory;
import Observer.InventoryObserver;
import View.InventoryDetailView;
import View.InventoryListView;
import View.WarehouseListView;

public class InventoryListController implements ActionListener{
	
	private ArrayList<Inventory> inventories;
	
	private Inventory sInventory;
	
	private InventoryListView iListView;
	
	private ArrayList<String> list;
	
	private InventoryDetailView detailView;
	
	private InventoryObserver iObserver;
	
	private JDesktopPane jDesktopPane;
	
	private InventoryTableGateway iTableGateway;
	
	private long nextID;

	public InventoryListController(JDesktopPane jDesktopPane, InventoryObserver iObserver, 
			InventoryTableGateway iTableGateway) {
		this.jDesktopPane = jDesktopPane;
		this.iObserver = iObserver;
		this.iTableGateway = iTableGateway;
		list = new ArrayList<String>();	
		try {
			inventories = iTableGateway.fetchInventories();
			inventories = iTableGateway.setParts(inventories);
			inventories = iTableGateway.setWarehouses(inventories);
		} catch (GatewayException e) {
			e.printStackTrace();
		}
		this.iTableGateway = iTableGateway;
		long id = 0;
	}
	
	public ArrayList<String> getInventoryList(){
		for(int i = 0; i < inventories.size(); i++){
			list.add(Long.toString(inventories.get(i).getId()) + "   " + inventories.get(i).getPart().getPartName());
		}
		return list;	
	}
	
	public int addInventory(long warehouseId, long partId, double quantity) {
		for(int i = 0; i < inventories.size(); i++) {
			if((inventories.get(i).getWarehouseId() == warehouseId) && 
					(inventories.get(i).getPartId() == partId)) {
				return 1;
			}
		}
		Inventory p = new Inventory(0, warehouseId, partId, quantity);
		try {
			iTableGateway.insertInventory(p);
		} catch (GatewayException e) {
			e.printStackTrace();
		}
		iObserver.addToList(Long.toString(p.getId()) + "   " + p.getPart().getPartName());
		return 0;
	}
	
	public int editInventory(long id, double quantity) {
		int i;
		for(i = 0; i < inventories.size(); i++) {
			if (inventories.get(i).getId() == id) {
				inventories.get(i).setQuantity(quantity);
				try {
					iTableGateway.saveInventory(inventories.get(i));
				} catch (GatewayException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
		iObserver.updateList(i, Long.toString(inventories.get(i).getId()) + "   " + 
				inventories.get(i).getPart().getPartName());
		iObserver.updateDetailViews(inventories.get(i).getId());
		return 0;
	}

	public void actionPerformed(ActionEvent e) {
		if ("add".equals(e.getActionCommand())){
			detailView = new InventoryDetailView(jDesktopPane, this);
			iObserver.addDetailView(detailView);
		} else if ("edit".equals(e.getActionCommand()) && (iListView.getSelected() != null)){
			for(int i = 0; i < inventories.size(); i++) {
				if(inventories.get(i).getId() == Long.parseLong(iListView.getSelected())) {
					sInventory = inventories.get(i);
					break;
				}
			}
			detailView = new InventoryDetailView(jDesktopPane, this, sInventory);
		} else if ("delete".equals(e.getActionCommand()) && (iListView.getSelected() != null)){
			for(int i = 0; i < inventories.size(); i++) {
				if(inventories.get(i).getId() == Long.parseLong(iListView.getSelected())) {
					try {
						iTableGateway.deleteInventory(inventories.get(i).getId());
					} catch (GatewayException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					iListView.remove(i);
					inventories.remove(i);
					break;
				}
			}
			iListView.setSelected(null);
		} 
	}

	public void setList(InventoryListView iListView) {
		this.iListView = iListView;
	}

}

