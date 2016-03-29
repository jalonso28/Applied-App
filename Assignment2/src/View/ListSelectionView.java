package View;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import Controller.InventoryListController;
import Controller.PartsListController;
import Controller.WarehouseListController;
import Observer.InventoryObserver;
import Observer.PartObserver;
import Observer.WarehouseObserver;

public class ListSelectionView extends JInternalFrame{

	public ListSelectionView(JDesktopPane jDesktopPane, WarehouseListController wListController,
			PartsListController pListController, WarehouseObserver wObserver, PartObserver pObserver) {
		WarehouseListView wListView = new WarehouseListView(jDesktopPane, wListController);
		PartsListView pListView = new PartsListView(jDesktopPane, pListController);
		wListController.setList(wListView);
		pListController.setList(pListView);
		wObserver.setWarehouseList(wListView);
		pObserver.setPartsList(pListView);
	}

}
