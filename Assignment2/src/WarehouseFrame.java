
import java.awt.Color;

import javax.swing.*;

import Controller.InventoryListController;
import Controller.PartsListController;
import Controller.WarehouseListController;
import Database.GatewayException;
import Database.InventoryTableGateway;
import Database.PartTableGateway;
import Database.WarehouseTableGateway;
import Observer.InventoryObserver;
import Observer.PartObserver;
import Observer.WarehouseObserver;
import View.ListSelectionView;
import View.WarehouseListView;

/*
 * CS 4743 Assignment 2 by Juan A Alonso
 */
@SuppressWarnings("serial")
public class WarehouseFrame extends JFrame{
	
	private ListSelectionView listSelectView;
	WarehouseTableGateway wTableGateway;
	PartTableGateway pTableGateway;
	InventoryTableGateway iTableGateway;

	public static void main(String[] args) {
		try {
			new WarehouseFrame("Warehouse Frame");
		} catch (GatewayException e) {
			e.printStackTrace();
		}

	}
	
	public WarehouseFrame(String title) throws GatewayException{
		super(title);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(500, 500);
		setBackground(Color.gray);
		
		wTableGateway = new WarehouseTableGateway();
		pTableGateway = new PartTableGateway();
		iTableGateway = new InventoryTableGateway();

		JDesktopPane jDesktopPane = new JDesktopPane();
		this.add(jDesktopPane);
		WarehouseObserver wObserver = new WarehouseObserver();
		PartObserver pObserver = new PartObserver();
		InventoryObserver iObserver = new InventoryObserver();
		WarehouseListController wListController = 
				new WarehouseListController(jDesktopPane, wObserver, wTableGateway, iObserver, iTableGateway);
		PartsListController pListController =
				new PartsListController(jDesktopPane, pObserver, pTableGateway);
		listSelectView = new ListSelectionView(jDesktopPane, wListController, pListController, wObserver, pObserver);
		setVisible(true);

	}

}

