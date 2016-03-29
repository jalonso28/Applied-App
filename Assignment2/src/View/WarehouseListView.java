package View;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.*;

import Controller.WarehouseListController;

public class WarehouseListView extends JInternalFrame{

	private static final long serialVersionUID = 1L;

	private JList<String> list;

	private DefaultListModel<String> listModel;

	private JButton add, edit, delete, inventory;

	private WarehouseListController wListController;

	private JPanel subPanel;
	
	private String selected;
	
	private JInternalFrame iFrame;
	
	private JDesktopPane jDesktopPane;

	public WarehouseListView(JDesktopPane jDesktopPane, WarehouseListController wListController){
		this.jDesktopPane = jDesktopPane;
		this.wListController = wListController;
		
		listModel = new DefaultListModel<String>();
		for(String s:wListController.getWarehouseList()){
		    listModel.addElement(s);
		}
		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// Adding buttons
		add = new JButton("Add");
		edit = new JButton("Edit/View");
		delete = new JButton("Delete");
		inventory = new JButton("Inventory");
		subPanel = new JPanel();
		subPanel.add(edit);
		edit.setActionCommand("edit");
		subPanel.add(add);
		add.setActionCommand("add");
		subPanel.add(delete);
		delete.setActionCommand("delete");
		subPanel.add(inventory);
		inventory.setActionCommand("inventory");
		
		
		iFrame = new JInternalFrame("Warehouse List"); 
		iFrame.setLayout(new BorderLayout());
		iFrame.setSize(250, 250);
		iFrame.setMaximizable(true);
		iFrame.setIconifiable(true);
		iFrame.setResizable(true);
		iFrame.setVisible(true);
		iFrame.setClosable(false);
		iFrame.setBackground(Color.white);
		iFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		iFrame.add(list, BorderLayout.CENTER);
		iFrame.add(subPanel, BorderLayout.SOUTH);
		jDesktopPane.add(iFrame);
		registerListener();
	}
	
	public void registerListener(){
		this.add.addActionListener(wListController);
		this.edit.addActionListener(wListController);
		this.delete.addActionListener(wListController);
		this.inventory.addActionListener(wListController);
		
	}
	
	public void update(int i, String s){
		remove(i);
		listModel.add(i, s);
	}
	
	public String getSelected() {
		if(list.isSelectionEmpty())
			return null;
		else
			selected = list.getSelectedValue().substring(0, 1);
		return selected;
	}
	
	public void remove(int i) {
		listModel.remove(i);
	}
	
	public void add(String s) {
		listModel.addElement(s);
	}
	
	public void setSelected(String selected) {
		this.selected = selected;
	}

}
	