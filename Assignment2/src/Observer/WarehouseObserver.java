package Observer;

import java.util.ArrayList;

import javax.swing.JDesktopPane;

import View.WarehouseListView;
import View.PartDetailView;
import View.PartsListView;
import View.WarehouseDetailView;

public class WarehouseObserver {
	
	private WarehouseListView wListView;
	
	private static ArrayList<WarehouseDetailView> wDetailView;

	public WarehouseObserver() {
		wDetailView = new ArrayList<WarehouseDetailView>();
	}
	
	public void addDetailView(WarehouseDetailView DetailView) {
		wDetailView.add(DetailView);
	}
	
	public void setWarehouseList(WarehouseListView listView) {
		this.wListView = listView;
	}
	
	public void updateList(int i, String s) {
		wListView.update(i, s);
	}
	
	public void updateDetailViews(long id) {
		for (int i = 0; i < wDetailView.size(); i++) {
			if(wDetailView.get(i).getId() == id) {
				wDetailView.get(i).update();
			}
		}
	}

	public void addToList(String s) {
		wListView.add(s);		
	}
}
