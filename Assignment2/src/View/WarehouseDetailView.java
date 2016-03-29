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

import javax.swing.*;
import javax.swing.text.MaskFormatter;

import Controller.WarehouseListController;
import Database.GatewayException;
import Model.Warehouse;

public class WarehouseDetailView implements ActionListener{
	
	private JFormattedTextField idField, nameField, addressField, cityField, stateField, zipField, capacityField;	
	
	private MaskFormatter nameFormat, addressFormat, cityFormat, stateFormat, zipFormat;

	NumberFormat capacityFormat;
	
	private JLabel idLabel, nameLabel, addressLabel, cityLabel, stateLabel, zipLabel, capacityLabel;
	
	private String idString = "ID:", nameString = "Name:", addressString = "Adress", 
			cityString = "City:", stateString = "State:", zipString = "Zip:", capacityString = "Capacity";
	
	private JPanel mainPanel, subPanel;
	
	private JButton close, save;
	
	private long id;
	
	private WarehouseListController wListController;
	
	JDesktopPane jDesktopPane;
	
	JInternalFrame iFrame;
	
	Warehouse warehouse;
	     
	
	public WarehouseDetailView(JDesktopPane jDesktopPane, WarehouseListController wListController){		
		this.wListController = wListController;
		this.jDesktopPane = jDesktopPane;
		
		buildInternalPanelAdd();
		registerListener();
	}
	
	public WarehouseDetailView(JDesktopPane jDesktopPane, WarehouseListController wListController,
			Warehouse warehouse) {
		this.wListController = wListController;
		this.jDesktopPane = jDesktopPane;
		this.warehouse = warehouse;
		
		buildInternalPanelEdit();
		registerListener();
		
	}
	
	private void buildInternalPanelEdit() {
		iFrame = new JInternalFrame(warehouse.getName()); 
		
		id = warehouse.getId();
		
		setupFormats();

		idField = new JFormattedTextField();
		nameField = new JFormattedTextField(nameFormat);
		addressField = new JFormattedTextField(addressFormat);
		cityField = new JFormattedTextField(cityFormat);
		stateField = new JFormattedTextField(stateFormat);
		zipField = new JFormattedTextField(zipFormat);
		capacityField = new JFormattedTextField(capacityFormat);
		
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
		
		mainPanel = new JPanel(new GridLayout(7,2));

		mainPanel.add(idLabel);
		mainPanel.add(idField);
		mainPanel.add(nameLabel);
		mainPanel.add(nameField);
		mainPanel.add(addressLabel);
		mainPanel.add(addressField);
		mainPanel.add(cityLabel);
		mainPanel.add(cityField);
		mainPanel.add(stateLabel);
		mainPanel.add(stateField);
		mainPanel.add(zipLabel);
		mainPanel.add(zipField);
		mainPanel.add(capacityLabel);
		mainPanel.add(capacityField);
		
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
		iFrame.setLocation(0, 250);
		iFrame.moveToFront();	
	}

	private void buildInternalPanelAdd() {
		iFrame = new JInternalFrame("New Warehouse"); 
		
		setupFormats();

		idField = new JFormattedTextField();
		nameField = new JFormattedTextField(nameFormat);
		addressField = new JFormattedTextField(addressFormat);
		cityField = new JFormattedTextField(cityFormat);
		stateField = new JFormattedTextField(stateFormat);
		zipField = new JFormattedTextField(zipFormat);
		capacityField = new JFormattedTextField(capacityFormat);
		
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
		
		mainPanel = new JPanel(new GridLayout(6,2));

		mainPanel.add(nameLabel);
		mainPanel.add(nameField);
		mainPanel.add(addressLabel);
		mainPanel.add(addressField);
		mainPanel.add(cityLabel);
		mainPanel.add(cityField);
		mainPanel.add(stateLabel);
		mainPanel.add(stateField);
		mainPanel.add(zipLabel);
		mainPanel.add(zipField);
		mainPanel.add(capacityLabel);
		mainPanel.add(capacityField);			
		
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
		nameLabel = new JLabel(nameString);
		nameLabel.setLabelFor(nameField);
		addressLabel = new JLabel(addressString);
		addressLabel.setLabelFor(addressField);
		cityLabel = new JLabel(cityString);
		cityLabel.setLabelFor(cityField);
		stateLabel = new JLabel(stateString);
		stateLabel.setLabelFor(stateField);
		zipLabel = new JLabel(zipString);
		zipLabel.setLabelFor(zipField);
		capacityLabel = new JLabel(capacityString);
		capacityLabel.setLabelFor(capacityField);
	}


	private void setupFormats(){
		StringBuilder mask = new StringBuilder();
		StringBuilder mask1 = new StringBuilder();
		StringBuilder mask2 = new StringBuilder();
		StringBuilder mask3 = new StringBuilder();
		for( int i = 0; i < 255; i++) {
		  mask.append("*");
		}
		for( int i = 0; i < 100; i++) {
			  mask2.append("*");	
			}
		for( int i = 0; i < 50; i++) {
			  mask3.append("*");
			}
		try {
			
			nameFormat = new MaskFormatter(mask.toString());
			addressFormat = new MaskFormatter(mask.toString());
			cityFormat = new MaskFormatter(mask2.toString());
			stateFormat = new MaskFormatter(mask3.toString());
			zipFormat = new MaskFormatter("#####");
			capacityFormat = NumberFormat.getNumberInstance();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadInfo() {
		idField.setEditable(false);
		idField.setText(Long.toString(warehouse.getId()));
		nameField.setText(warehouse.getName().trim());
		addressField.setText(warehouse.getAddress().trim());
		cityField.setText(warehouse.getCity().trim());
		stateField.setText(warehouse.getState().trim());
		zipField.setText(warehouse.getZip().trim());
		capacityField.setText(Double.toString(warehouse.getCapacity()));
	}
	
	public void registerListener(){
		this.save.addActionListener(this);	
		this.close.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if ("confirm".equals(e.getActionCommand()) && capacityField.getText() != null) {
			if(wListController.editWarehouse(warehouse.getId(), nameField.getText(), addressField.getText(), 
					cityField.getText(), stateField.getText(), zipField.getText(), Integer.parseInt(capacityField.getText().replace(",", ""))) == 1) {
				JOptionPane.showMessageDialog(mainPanel, "Invalid entry", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				try {
					iFrame.setClosed(true);
				} catch (PropertyVetoException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} else if ("save".equals(e.getActionCommand()) && capacityField.getText() != null) {
			try {
				if(wListController.addWarehouse(nameField.getText(), addressField.getText(), 
						cityField.getText(), stateField.getText(), zipField.getText(), 
							Integer.parseInt(capacityField.getText().replace(",", ""))) == 1) {
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
			} catch (GatewayException e1) {
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
		idField.setText(Long.toString(warehouse.getId()).trim());
		nameField.setText(warehouse.getName().trim());
		addressField.setText(warehouse.getAddress().trim());
		cityField.setText(warehouse.getCity().trim());
		stateField.setText(warehouse.getState().trim());
		zipField.setText(warehouse.getZip());
		capacityField.setText(Double.toString(warehouse.getCapacity()).trim());
	}
	
	public long getId() {
		return id;
	}

}
