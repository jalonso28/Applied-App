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

import Controller.PartsListController;
import Database.GatewayException;
import Model.Part;

public class PartDetailView implements ActionListener{
	
	private JFormattedTextField idField, partNameField, partNumField;
	
	private JComboBox unitField;
	
	private MaskFormatter nameFormat, numberFormat;
	
	private JLabel idLabel, partNumLabel, partNameLabel, unitLabel;
	
	private String idString = "ID:", partNumString = "Part Number:", partNameString = "Part Name:", 
			unitString = "Unit of Quantity:";
	
	private JPanel mainPanel, subPanel;
	
	private JButton close, save;
	
	private long id;
	
	private PartsListController pListController;
	
	private JDesktopPane jDesktopPane;
	
	private JInternalFrame iFrame;
	
	private String[] unitStrings = { "Linear Ft.", "Pieces" };
	
	private Part part;
	
	public PartDetailView(JDesktopPane jDesktopPane, PartsListController pListController){		
		this.pListController = pListController;
		this.jDesktopPane = jDesktopPane;
		
		buildInternalPanelAdd();
		registerListener();
	}
	
	public PartDetailView(JDesktopPane jDesktopPane, PartsListController pListController,
			Part part) {
		this.pListController = pListController;
		this.jDesktopPane = jDesktopPane;
		this.part = part;
		
		buildInternalPanelEdit();
		registerListener();
		
	}
	
	private void buildInternalPanelEdit() {
		iFrame = new JInternalFrame(part.getPartName()); 
		
		id = part.getId();
		
		setupFormats();

		idField = new JFormattedTextField();
		partNameField = new JFormattedTextField(nameFormat);
		partNumField = new JFormattedTextField(numberFormat);
		unitField = new JComboBox(unitStrings);
		unitField.setSelectedIndex(0);
		
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
		mainPanel.add(partNumLabel);
		mainPanel.add(partNameField);
		mainPanel.add(partNameLabel);
		mainPanel.add(partNumField);
		mainPanel.add(unitLabel);
		mainPanel.add(unitField);
		
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
		iFrame = new JInternalFrame("New Part"); 
		
		setupFormats();

		partNameField = new JFormattedTextField(nameFormat);
		partNumField = new JFormattedTextField(numberFormat);
		unitField = new JComboBox(unitStrings);
		unitField.setSelectedIndex(0);
		
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

		mainPanel.add(partNumLabel);
		mainPanel.add(partNameField);
		mainPanel.add(partNameLabel);
		mainPanel.add(partNumField);
		mainPanel.add(unitLabel);
		mainPanel.add(unitField);		
		
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
		partNumLabel = new JLabel(partNumString);
		partNumLabel.setLabelFor(partNameField);
		partNameLabel = new JLabel(partNameString);
		partNameLabel.setLabelFor(partNumField);
		unitLabel = new JLabel(unitString);
		unitLabel.setLabelFor(unitField);
	}


	private void setupFormats(){
		StringBuilder mask = new StringBuilder();
		StringBuilder mask2 = new StringBuilder();
		for( int i = 0; i < 255; i++) {
		  mask.append("*");
		}
		for( int i = 0; i < 20; i++) {
			  mask2.append("*");	
			}
		try {
			
			numberFormat = new MaskFormatter(mask.toString());
			nameFormat = new MaskFormatter(mask.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadInfo() {
		idField.setEditable(false);
		idField.setText(Long.toString(part.getId()));
		partNameField.setText(part.getPartName().trim());
		partNumField.setText(part.getPartNum().trim());
		int index;
		if(part.getUnit().equals("Linear Ft."))
			index = 0;
		else
			index = 1;
		unitField.setSelectedIndex(index);
	}
	
	public void registerListener(){
		this.save.addActionListener(this);	
		this.close.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if ("confirm".equals(e.getActionCommand())) {
			if(pListController.editPart(part.getId(), partNumField.getText().trim(), partNameField.getText().trim(), 
					unitField.getSelectedItem().toString()) == 1) {
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
				if(pListController.addPart(partNameField.getText().trim(), partNumField.getText().trim(), 
						unitField.getSelectedItem().toString()) == 1) {
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
		idField.setText(Long.toString(part.getId()).trim());
		partNameField.setText(part.getPartName().trim());
		partNumField.setText(part.getPartNum().trim());
		int index;
		if(part.getUnit().equals("Linear Ft."))
			index = 0;
		else
			index = 1;
		unitField.setSelectedIndex(index);
	}
	
	public long getId() {
		return id;
	}

}
