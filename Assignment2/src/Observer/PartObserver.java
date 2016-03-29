package Observer;

import java.util.ArrayList;

import javax.swing.JDesktopPane;

import View.PartDetailView;
import View.PartsListView;

public class PartObserver {
	
	private PartsListView pListView;

	private static ArrayList<PartDetailView> pDetailView;
	

	public PartObserver() {
		pDetailView = new ArrayList<PartDetailView>();
	}
	
	public void addDetailView(PartDetailView DetailView) {
		pDetailView.add(DetailView);
	}
	
	public void setPartsList(PartsListView listView) {
		this.pListView = listView;
	}
	
	public void updateList(int i, String s) {
		pListView.update(i, s);
	}
	
	public void updateDetailViews(long id) {
		for (int i = 0; i < pDetailView.size(); i++) {
			if(pDetailView.get(i).getId() == id) {
				pDetailView.get(i).update();
			}
		}
	}

	public void addToList(String s) {
		pListView.add(s);	
	}
}