package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;

import Controller.InventoryListController;
import Database.GatewayException;
import Model.Inventory;

public class InventoryDetailView implements ActionListener{
	
	private JFormattedTextField idField, warehouseNameField, partNumField, quantityField;
	
	private MaskFormatter nameFormat, numberFormat;
	
	private JLabel idLabel, warehouseNameLabel, partNumLabel, quantityLabel;
	private String idString = "ID:", warehouseNameString = "Warehouse Name:", partNumString = "Part Number:", 
			quantityString = "Quantity:";
	
	private JPanel mainPanel, subPanel;
	
	private JButton close, save;
	
	private long id;
	
	private InventoryListController iListController;
	
	private JDesktopPane jDesktopPane;
	
	private JInternalFrame iFrame;
	
	private Inventory inventory;
	
	public InventoryDetailView(JDesktopPane jDesktopPane, InventoryListController iListController){		
		this.iListController = iListController;
		this.jDesktopPane = jDesktopPane;
		
		buildInternalPanelAdd();
		registerListener();
	}
	
	public InventoryDetailView(JDesktopPane jDesktopPane, InventoryListController iListController,
			Inventory inventory) {
		this.iListController = iListController;
		this.jDesktopPane = jDesktopPane;
		this.inventory = inventory;
		
		buildInternalPanelEdit();
		registerListener();
		
	}
	
	private void buildInternalPanelEdit() {
		iFrame = new JInternalFrame(inventory.getPart().getPartName()); 
		
		id = inventory.getId();
		
		setupFormats();

		idField = new JFormattedTextField();
		warehouseNameField = new JFormattedTextField(numberFormat);
		partNumField = new JFormattedTextField(numberFormat);
		quantityField = new JFormattedTextField(numberFormat);
		
		iFrame.setLayout(new BorderLayout());
		iFrame.setSize(250, 250);
		iFrame.setMaximizable(true);
		iFrame.setIconifiable(true);
		iFrame.setResizable(true);
		iFrame.setVisible(true);
		iFrame.setClosable(true);
		iFrame.setBackground(Color.white);
		iFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		setupLabels();
		
		mainPanel = new JPanel(new GridLayout(4,2));

		mainPanel.add(idLabel);
		mainPanel.add(idField);
		mainPanel.add(warehouseNameLabel);
		mainPanel.add(warehouseNameField);
		mainPanel.add(partNumLabel);
		mainPanel.add(partNumField);
		mainPanel.add(quantityLabel);
		mainPanel.add(quantityField);
		
		loadInfo();
		
		iFrame.add(mainPanel, BorderLayout.CENTER);
		
		subPanel = new JPanel();
		
		close = new JButton("Close");
		save = new JButton("Save");
		subPanel.add(close);
		close.setActionCommand("close");
		subPanel.add(save);
		save.setActionCommand("confirm");
		iFrame.add(subPanel, BorderLayout.SOUTH);
		
		jDesktopPane.add(iFrame);
		iFrame.setLocation(200, 200);
		iFrame.moveToFront();	
	}
	
	private void buildInternalPanelAdd() {
		iFrame = new JInternalFrame("New Inventory"); 
		
		setupFormats();

		warehouseNameField = new JFormattedTextField(nameFormat);
		partNumField = new JFormattedTextField(numberFormat);
		quantityField = new JFormattedTextField(numberFormat);
		
		iFrame.setLayout(new BorderLayout());
		iFrame.setSize(250, 250);
		iFrame.setMaximizable(true);
		iFrame.setIconifiable(true);
		iFrame.setResizable(true);
		iFrame.setVisible(true);
		iFrame.setClosable(true);
		iFrame.setBackground(Color.white);
		iFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		setupLabels();
		
		mainPanel = new JPanel(new GridLayout(3,2));

		mainPanel.add(warehouseNameLabel);
		mainPanel.add(warehouseNameField);
		mainPanel.add(partNumLabel);
		mainPanel.add(partNumField);
		mainPanel.add(quantityLabel);
		mainPanel.add(quantityField);		
		
		iFrame.add(mainPanel, BorderLayout.CENTER);
		
		subPanel = new JPanel();
		
		close = new JButton("Close");
		save = new JButton("Save");
		subPanel.add(close);
		close.setActionCommand("close");
		subPanel.add(save);
		save.setActionCommand("save");
		iFrame.add(subPanel, BorderLayout.SOUTH);
		
		jDesktopPane.add(iFrame);
		iFrame.setLocation(200, 200);
		iFrame.moveToFront();	
	}
	
	
	private void setupLabels() {
		idLabel = new JLabel(idString);
		idLabel.setLabelFor(idField);
		warehouseNameLabel = new JLabel(warehouseNameString);
		warehouseNameLabel.setLabelFor(warehouseNameField);
		partNumLabel = new JLabel(partNumString);
		partNumLabel.setLabelFor(partNumField);
		quantityLabel = new JLabel(quantityString);
		quantityLabel.setLabelFor(quantityField);
	}


	private void setupFormats(){
		StringBuilder mask = new StringBuilder();
		for( int i = 0; i < 20; i++) {
			  mask.append("*");	
			}
		try {
			
			numberFormat = new MaskFormatter(mask.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadInfo() {
		idField.setEditable(false);
		idField.setText(Long.toString(inventory.getId()));
		warehouseNameField.setEditable(false);
		warehouseNameField.setText(inventory.getWarehouse().getName().trim());
		partNumField.setEditable(false);
		partNumField.setText(inventory.getPart().getPartNum().trim());
		quantityField.setText(Double.toString(inventory.getQuantity()).trim());
	}
	
	public void registerListener(){
		this.save.addActionListener(this);	
		this.close.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if ("confirm".equals(e.getActionCommand())) {
			if(iListController.editInventory(inventory.getId(), Double.parseDouble(quantityField.getText().trim())) == 1) {
				JOptionPane.showMessageDialog(mainPanel, "Invalid entry", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				try {
					iFrame.setClosed(true);
				} catch (PropertyVetoException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} else if ("save".equals(e.getActionCommand())) {
			try {
				if(iListController.addInventory(warehouseNameField.getText().trim(), partNumField.getText().trim(), 
						quantityField.getSelectedItem().toString()) == 1) {
					JOptionPane.showMessageDialog(mainPanel, "Invalid entry", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					iFrame.setClosed(true);
				}
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (HeadlessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (PropertyVetoException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if ("close".equals(e.getActionCommand())) {
			try {
				iFrame.setClosed(true);
			} catch (PropertyVetoException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	public void update() {
		idField.setEditable(false);
		idField.setText(Long.toString(inventory.getId()).trim());
		warehouseNameField.setText(Long.toString(inventory.getWarehouseId().trim()));
		partNumField.setText(Long.toString(inventory.getPartId().trim()));
		quantityField.setText(Double.toString(inventory.getQuantity()));
	}
	
	public long getId() {
		return id;
	}

}

