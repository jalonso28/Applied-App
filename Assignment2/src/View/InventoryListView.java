package View;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.*;

import Controller.InventoryListController;
import Model.Warehouse;

public class InventoryListView extends JInternalFrame{

	private static final long serialVersionUID = 1L;

	private JList<String> list;

	private Warehouse warehouse;
	
	private DefaultListModel<String> listModel;

	private JButton add, edit, delete;

	private InventoryListController iListController;

	private JPanel subPanel;
	
	private String selected;
	
	private JInternalFrame iFrame;
	
	private JDesktopPane jDesktopPane;

	public InventoryListView(JDesktopPane jDesktopPane, InventoryListController iListController, Warehouse warehouse){
		this.jDesktopPane = jDesktopPane;
		this.iListController = iListController;
		this.warehouse = warehouse;
		
		listModel = new DefaultListModel<String>();
		for(String s:iListController.getInventoryList()){
		    listModel.addElement(s);
		}
		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// Adding buttons
		add = new JButton("Add");
		edit = new JButton("Edit/View");
		delete = new JButton("Delete");
		subPanel = new JPanel();
		subPanel.add(edit);
		edit.setActionCommand("edit");
		subPanel.add(add);
		add.setActionCommand("add");
		subPanel.add(delete);
		delete.setActionCommand("delete");
		
		
		iFrame = new JInternalFrame("Inventory List"); 
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
		iFrame.setLocation(250, 0);
		jDesktopPane.add(iFrame);
		registerListener();
	}
	
	public void registerListener(){
		this.add.addActionListener(iListController);
		this.edit.addActionListener(iListController);
		this.delete.addActionListener(iListController);	
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

