package Observer;

import java.util.ArrayList;

import javax.swing.JDesktopPane;

import View.InventoryDetailView;
import View.InventoryListView;

public class InventoryObserver {
	
	private InventoryListView iListView;

	private static ArrayList<InventoryDetailView> iDetailView;
	

	public InventoryObserver() {
		iDetailView = new ArrayList<InventoryDetailView>();
	}
	
	public void addDetailView(InventoryDetailView DetailView) {
		iDetailView.add(DetailView);
	}
	
	public void setInventoryList(InventoryListView listView) {
		this.iListView = listView;
	}
	
	public void updateList(int i, String s) {
		iListView.update(i, s);
	}
	
	public void updateDetailViews(long id) {
		for (int i = 0; i < iDetailView.size(); i++) {
			if(iDetailView.get(i).getId() == id) {
				iDetailView.get(i).update();
			}
		}
	}

	public void addToList(String s) {
		iListView.add(s);	
	}
}
