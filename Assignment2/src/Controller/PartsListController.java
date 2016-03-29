package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JDesktopPane;

import Database.GatewayException;
import Database.PartTableGateway;
import Model.Part;
import Observer.PartObserver;
import View.PartDetailView;
import View.PartsListView;
import View.WarehouseListView;

public class PartsListController implements ActionListener{
	
	private ArrayList<Part> parts;
	
	private Part sParts;
	
	private PartsListView pListView;
	
	private ArrayList<String> list;
	
	private PartDetailView detailView;
	
	private PartObserver pObserver;
	
	private JDesktopPane jDesktopPane;
	
	private PartTableGateway pTableGateway;
	
	private long nextID;

	public PartsListController(JDesktopPane jDesktopPane, PartObserver pObserver, 
			PartTableGateway pTableGateway) {
		this.jDesktopPane = jDesktopPane;
		this.pObserver = pObserver;
		this.pTableGateway = pTableGateway;
		list = new ArrayList<String>();
		try {
			parts = pTableGateway.fetchParts();
		} catch (GatewayException e) {
			e.printStackTrace();
		}
		this.pTableGateway = pTableGateway;
		long id = 0;
	}
	
	public ArrayList<String> getPartsList(){
		
		for(int i = 0; i < parts.size(); i++){
			list.add(Long.toString(parts.get(i).getId()) + "   " + parts.get(i).getPartName());
		}
		
		return list;	
		
	}
	
	public int addPart(String partNum, String partName, String vendor, String unit, String vendorNum) {
		for(int i = 0; i < parts.size(); i++) {
			if(parts.get(i).getPartName().equals(partName)) {
				return 1;
			}
		}
		
		parts.add(new Part(nextID, partNum, partName, vendor, unit, vendorNum));
		pObserver.addToList(Long.toString(nextID) + "   " + partName );
		nextID++; 
		return 0;
	}
	
	public int addPart(String partNum, String partName, String unit) {
		for(int i = 0; i < parts.size(); i++) {
			if(parts.get(i).getPartName().equals(partName)) {
				return 1;
			}
		}
		Part p = new Part(0, partNum, partName, unit);
		try {
			pTableGateway.insertPart(p);
		} catch (GatewayException e) {
			e.printStackTrace();
		}
		pObserver.addToList(Long.toString(p.getId()) + "   " + partName );
		return 0;
	}
	
	public int editPart(long id, String partNum, String partName, String unit) {
		int i;
		if (partName == null || partNum == null || unit == null) {
			return 1;
		}
		for(i = 0; i < parts.size(); i++) {
			if (parts.get(i).getId() == id) {
				parts.get(i).setPartName(partName);
				parts.get(i).setPartNum(partNum);
				parts.get(i).setUnit(unit);
				try {
					pTableGateway.savePart(parts.get(i));
				} catch (GatewayException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
		pObserver.updateList(i, Long.toString(parts.get(i).getId()) + "   " + partName);
		pObserver.updateDetailViews(parts.get(i).getId());
		return 0;
	}

	public void actionPerformed(ActionEvent e) {
		if ("add".equals(e.getActionCommand())){
			detailView = new PartDetailView(jDesktopPane, this);
			pObserver.addDetailView(detailView);
		} else if ("edit".equals(e.getActionCommand()) && (pListView.getSelected() != null)){
			for(int i = 0; i < parts.size(); i++) {
				if(parts.get(i).getId() == Long.parseLong(pListView.getSelected())) {
					sParts = parts.get(i);
					break;
				}
			}
			detailView = new PartDetailView(jDesktopPane, this, sParts);
		} else if ("delete".equals(e.getActionCommand()) && (pListView.getSelected() != null)){
			for(int i = 0; i < parts.size(); i++) {
				if(parts.get(i).getId() == Long.parseLong(pListView.getSelected())) {
					try {
						pTableGateway.deletePart(parts.get(i).getId());
					} catch (GatewayException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					pListView.remove(i);
					parts.remove(i);
					break;
				}
			}
			pListView.setSelected(null);
		} 
		
	}

	public void setList(PartsListView pListView) {
		this.pListView = pListView;
	}

}
