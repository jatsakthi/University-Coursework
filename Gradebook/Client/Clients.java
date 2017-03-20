package pack;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import java.awt.GridBagLayout;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JComboBox;
import java.awt.Panel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;
import org.json.*;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JSeparator;
import javax.swing.JProgressBar;

public class Clients extends JFrame {


	private static JPanel contentPane;
	private static JComboBox comboBoxIds;
	private static JComboBox comboBoxItems;
	private static JTextField textFieldGrade;
	private static JTextField textFieldComments;
	private static JButton btnCreate;
	private static JButton btnUpdate;
	private static JButton btnDelete;
	private static final ButtonGroup buttonGroup = new ButtonGroup();
	private static JLabel lblIds;
	private static JLabel lblWorkItems;
	private static JLabel lblGrade;
	private static JLabel lblComments;
	private static Students FileData;
	private static String input = "";
	private static ActionListener[] temp = new ActionListener[2];
	private static JTextField textFieldId;
	private static JTextField textFieldWork;
	private static JLabel lbldetailsBelow;
	private static JLabel lblId;
	private static JLabel lblWork;
	private static String[] modifyData = new  String[4];
	private static JLabel label;
	private static JLabel lblResult;
	private static int counter = 1;

	/**
	 * Launch the application.
	 * @throws JSONException 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * @throws JAXBException 
	 */
	public static void main(String[] args) throws IOException, JAXBException {


		try {

			Client client = Client.create();

			WebResource webResource = client
			   .resource("http://localhost:8080/restservices/Gradebook");

	
			String path = System.getProperty("user.dir");
			input = path.split("Client")[0];
			input='1'+input;
			////System.out.println("Sending: "+input+"\n");
			ClientResponse response = webResource.type("application/xml")
			   .post(ClientResponse.class, input);
			System.out.println("Client: Sending GET Request");
			response = webResource.type("application/xml").get(ClientResponse.class);
			//System.out.println("Output from Server .... \n");
			String output = response.getEntity(String.class);
			
			if(response.getStatus()==200){
				System.out.println("Server-Client connection established..");
				System.out.println("Client: Received Response Code "+response.getStatus());	
			}
			else{
				System.out.println("Server-Client Connection Failed..");
			}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					Clients frame = new Clients();
					frame.setTitle("GradeBook");
					//frame.setBackground(Color.BLUE);
					frame.setVisible(true);
					FileData = tryunmarshal(output);
					setDialog();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});		
		
		
		
		} catch (Exception e) {

			e.printStackTrace();

		  }
		
		
	}
	private static void create() throws JAXBException{

		
		Client client = Client.create();
		System.out.println("Client: Sending POST Request");
		WebResource webResource = client
		   .resource("http://localhost:8080/restservices/Gradebook");
		//System.out.println("Sending: "+input+"\n");
		ClientResponse response = webResource.type("application/xml")
		   .post(ClientResponse.class, input);
		//System.out.println("Output from Server .... \n");
		String output = response.getEntity(String.class);
		//System.out.println(output);
		System.out.println("Client: Received Response Code "+response.getStatus());	
		if(response.getStatus()== 200){
			System.out.println("Client: Sending GET Request");
			lblResult.setText("Created");
			lblResult.setForeground(Color.getHSBColor(Color.RGBtoHSB(34, 177, 76, null)[0], Color.RGBtoHSB(34, 177, 76, null)[1], Color.RGBtoHSB(34, 177, 76, null)[2]) );
			response = webResource.type("application/xml").get(ClientResponse.class);
			output = response.getEntity(String.class);
			FileData = tryunmarshal(output);
			setDialog();
			System.out.println("Client: Received Response Code "+response.getStatus());	
		}
		else if(response.getStatus() == 409){
			lblResult.setText("Conflict");
			lblResult.setForeground(Color.RED);
		}
		else{
			lblResult.setText("Error");
			lblResult.setForeground(Color.RED);
		}
		label.setText("Result("+Integer.toString(counter)+")");
		counter++;
		}
	private static void update() throws JAXBException{
		Client client = Client.create();
		//System.out.println("Main Update Function");
		System.out.println("Client: Sending PUT Request");
		WebResource webResource = client
		   .resource("http://localhost:8080/restservices/Gradebook");
		//System.out.println("Sending: "+input+"\n");
		ClientResponse response = webResource.type("application/xml")
		   .put(ClientResponse.class, input);
		//System.out.println("Output from Server .... \n");
		String output = response.getEntity(String.class);
		//System.out.println(output);
		System.out.println("Client: Received Response Code "+response.getStatus());	
		if(response.getStatus()== 200){
			System.out.println("Client: Sending GET Request");
			lblResult.setText("Updated");
			lblResult.setForeground(Color.getHSBColor(Color.RGBtoHSB(34, 177, 76, null)[0], Color.RGBtoHSB(34, 177, 76, null)[1], Color.RGBtoHSB(34, 177, 76, null)[2]) );
			response = webResource.type("application/xml").get(ClientResponse.class);
			output = response.getEntity(String.class);
			FileData = tryunmarshal(output);
			setDialog();
			System.out.println("Client: Received Response Code "+response.getStatus());	
		}
		else if(response.getStatus() == 404){
			lblResult.setText("Not Found");
			lblResult.setForeground(Color.RED);
		}
		else{
			lblResult.setText("Error");
			lblResult.setForeground(Color.RED);
		}
		label.setText("Result("+Integer.toString(counter)+")");
		counter++;
	}
	private static void delete() throws JAXBException{
		Client client = Client.create();
		//System.out.println("Main Update Function");
		System.out.println("Client: Sending DELETE Request");
		WebResource webResource = client
		   .resource("http://localhost:8080/restservices/Gradebook");
		//System.out.println("Sending: "+input+"\n");
		ClientResponse response = webResource.type("application/xml")
		   .delete(ClientResponse.class, input);
		//System.out.println("Output from Server .... \n");
		String output = response.getEntity(String.class);
		//System.out.println(output);
		System.out.println("Client: Received Response Code "+response.getStatus());	
		if(response.getStatus()== 200){
			System.out.println("Client: Sending GET Request");
			lblResult.setText("Deleted");
			lblResult.setForeground(Color.getHSBColor(Color.RGBtoHSB(34, 177, 76, null)[0], Color.RGBtoHSB(34, 177, 76, null)[1], Color.RGBtoHSB(34, 177, 76, null)[2]) );
			response = webResource.type("application/xml").get(ClientResponse.class);
			output = response.getEntity(String.class);
			FileData = tryunmarshal(output);
			setDialog();
			System.out.println("Client: Received Response Code "+response.getStatus());	
		}
		else if(response.getStatus() == 404){
			lblResult.setText("Not Found");
			lblResult.setForeground(Color.RED);
		}
		else{
			lblResult.setText("Error");
			lblResult.setForeground(Color.RED);
		}
		label.setText("Result("+Integer.toString(counter)+")");
		counter++;
		
	}
	private static void setDialog() {
		// TODO Auto-generated method stub
		
		temp[0] = comboBoxItems.getActionListeners()[0];
		comboBoxItems.removeActionListener(temp[0]);
		temp[1] = comboBoxIds.getActionListeners()[0];
		comboBoxIds.removeActionListener(temp[1]);
		comboBoxItems.removeActionListener(temp[0]);
		comboBoxIds.removeAllItems();
		comboBoxItems.removeAllItems();
		
		if(FileData.students == null){
			comboBoxIds.addItem("Create New");
			comboBoxItems.addItem("Create New");
			textFieldId.setText("None");
			textFieldWork.setText("None");
			textFieldGrade.setText("None");
			textFieldComments.setText("None");
			comboBoxItems.disable();
			textFieldId.enable();
			textFieldWork.enable();
			textFieldGrade.enable();
			textFieldComments.enable();
			comboBoxIds.disable();
			btnCreate.setEnabled(true);
			btnUpdate.setEnabled(false);
			btnDelete.setEnabled(false);
		}
		else{
			//comboBoxItems.removeAllItems();
			
		for(int i=0;i<FileData.students.size();i++)
			comboBoxIds.addItem(FileData.students.get(i).getId());
		comboBoxIds.addItem("Create New");
		comboBoxIds.enable();
		if(FileData.students.get(comboBoxIds.getSelectedIndex()).items!= null){
			
		for(int i=0;i<FileData.students.get(comboBoxIds.getSelectedIndex()).items.size();i++)
			comboBoxItems.addItem(FileData.getStudents().get(comboBoxIds.getSelectedIndex()).getWorkItems().get(i).getName());
		comboBoxItems.addItem("Create New");
		
		textFieldId.setText(comboBoxIds.getSelectedItem().toString());
		textFieldWork.setText(comboBoxItems.getSelectedItem().toString());
		
		if(FileData.students.get(comboBoxIds.getSelectedIndex()).items.get(comboBoxItems.getSelectedIndex()).getGrade()==null)
			textFieldGrade.setText("None");
		else
			textFieldGrade.setText(FileData.students.get(comboBoxIds.getSelectedIndex()).items.get(comboBoxItems.getSelectedIndex()).getGrade());
		if(FileData.students.get(comboBoxIds.getSelectedIndex()).items.get(comboBoxItems.getSelectedIndex()).getComments()==null)
			textFieldComments.setText("None");
		else
			textFieldComments.setText(FileData.students.get(comboBoxIds.getSelectedIndex()).items.get(comboBoxItems.getSelectedIndex()).getComments());
		
		/*textFieldId.disable();
		textFieldWork.disable();
		textFieldGrade.disable();
		textFieldComments.disable();*/
		btnCreate.setEnabled(false);
		btnUpdate.setEnabled(true);
		btnDelete.setEnabled(true);
		comboBoxItems.enable();
		}

		else{
			comboBoxItems.addItem("Create New");
			textFieldId.setText(comboBoxIds.getSelectedItem().toString());
			textFieldWork.setText("None");
			textFieldGrade.setText("None");
			textFieldComments.setText("None");
			comboBoxItems.disable();
			textFieldId.enable();
			textFieldWork.enable();
			textFieldGrade.enable();
			textFieldComments.enable();
			btnUpdate.setEnabled(false);
			btnDelete.setEnabled(false);
		}
		
		}
		
		comboBoxItems.addActionListener(temp[0]);
		comboBoxIds.addActionListener(temp[1]);
	}
	private static Students tryunmarshal(String data) throws JAXBException{
		
		JAXBContext jaxbContext = JAXBContext.newInstance(Students.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		StringReader reader = new StringReader(data);
		Students FileData = (Students) jaxbUnmarshaller.unmarshal(reader);
		return FileData;
	}

	private static Students unmarshal(File file) throws JAXBException{
		JAXBContext jaxbContext = JAXBContext.newInstance(Students.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		Students FileData = (Students) jaxbUnmarshaller.unmarshal(file);
		return FileData;
	}
	
	private static void marshal(File file, Students listOfstudents) throws JAXBException {
		// TODO Auto-generated method stub
		JAXBContext jaxbContext = JAXBContext.newInstance(Students.class);
		 Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		 jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		 jaxbMarshaller.marshal(listOfstudents, file);
		 
	}
	
	private static void setmodifyData(){
		
		modifyData[0] = textFieldId.getText();
		modifyData[1] = textFieldWork.getText();
		modifyData[2] = textFieldGrade.getText();
		modifyData[3] = textFieldComments.getText();
	}


	/**
	 * Create the frame.
	 * @return 
	 * @throws JAXBException 
	 */
	public  Clients() throws JAXBException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{45, 55, 60, 65, 30, 79, 67, 0};
		gbl_contentPane.rowHeights = new int[]{20, 20, 14, 20, 20, 20, 20, 23, 23, 23, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		comboBoxItems = new JComboBox();
		comboBoxItems.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				temp[0] = comboBoxItems.getActionListeners()[0];
				comboBoxItems.removeActionListener(temp[0]);
				temp[1] = comboBoxIds.getActionListeners()[0];
				comboBoxIds.removeActionListener(temp[1]);
			//System.out.println("clicked");
			
			if(comboBoxItems.getSelectedIndex() == comboBoxItems.getItemCount()-1){
				// user selected Create new
				textFieldId.setText(comboBoxIds.getSelectedItem().toString());
				textFieldWork.setText("None");
				textFieldGrade.setText("None");
				textFieldComments.setText("None");
				btnCreate.setEnabled(true);
				btnUpdate.setEnabled(false);
				btnDelete.setEnabled(false);
				textFieldId.enable();
				textFieldWork.enable();
				textFieldGrade.enable();
				textFieldComments.enable();
				
			}
			else{
				// user selected a valid option
				textFieldId.setText(comboBoxIds.getSelectedItem().toString());
				textFieldWork.setText(comboBoxItems.getSelectedItem().toString());
				textFieldGrade.setText(FileData.getStudents().get(comboBoxIds.getSelectedIndex()).getWorkItems().get(comboBoxItems.getSelectedIndex()).getGrade());
				textFieldComments.setText(FileData.getStudents().get(comboBoxIds.getSelectedIndex()).getWorkItems().get(comboBoxItems.getSelectedIndex()).getComments());
				/*textFieldId.disable();
				textFieldWork.disable();
				textFieldGrade.disable();
				textFieldComments.disable();*/
				btnCreate.setEnabled(false);
				btnUpdate.setEnabled(true);
				btnDelete.setEnabled(true);
				
			}
			
			comboBoxItems.addActionListener(temp[0]);
			comboBoxIds.addActionListener(temp[1]);
			
			
			}
		});
		//Students FileData = unmarshal(file);
		lblIds = new JLabel("Ids");
		GridBagConstraints gbc_lblIds = new GridBagConstraints();
		gbc_lblIds.anchor = GridBagConstraints.EAST;
		gbc_lblIds.insets = new Insets(0, 0, 5, 5);
		gbc_lblIds.gridx = 1;
		gbc_lblIds.gridy = 1;
		contentPane.add(lblIds, gbc_lblIds);
		
		
		comboBoxIds = new JComboBox();
		GridBagConstraints gbc_comboBoxIds = new GridBagConstraints();
		gbc_comboBoxIds.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxIds.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxIds.gridwidth = 4;
		gbc_comboBoxIds.gridx = 2;
		gbc_comboBoxIds.gridy = 1;
		contentPane.add(comboBoxIds, gbc_comboBoxIds);
		comboBoxIds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				temp[0] = comboBoxItems.getActionListeners()[0];
				comboBoxItems.removeActionListener(temp[0]);
				temp[1] = comboBoxIds.getActionListeners()[0];
				comboBoxIds.removeActionListener(temp[1]);
				
				//System.out.println("You Clicked me");
				
				
					//comboBoxItems.removeActionListener(comboBoxItems.getActionListeners()[0]);
					
				comboBoxItems.removeAllItems();
				if(comboBoxIds.getSelectedIndex()==comboBoxIds.getItemCount()-1){
					// user selected "create new"
					comboBoxItems.addItem("Create New");
					comboBoxItems.disable();
					textFieldId.setText("None");
					textFieldWork.setText("None");
					textFieldGrade.setText("None");
					textFieldComments.setText("None");
					btnCreate.setEnabled(true);
					btnUpdate.setEnabled(false);
					btnDelete.setEnabled(false);
					textFieldId.enable();
					textFieldWork.enable();
					textFieldGrade.enable();
					textFieldComments.enable();
				}
				else{
					// user selected one of the valid options
					if(FileData.students.get(comboBoxIds.getSelectedIndex()).items!= null){
						for(int i=0;i<FileData.students.get(comboBoxIds.getSelectedIndex()).items.size();i++)
							comboBoxItems.addItem(FileData.getStudents().get(comboBoxIds.getSelectedIndex()).getWorkItems().get(i).getName());
							comboBoxItems.addItem("Create New");
							comboBoxItems.enable();
							textFieldId.setText(comboBoxIds.getSelectedItem().toString());
							textFieldWork.setText(comboBoxItems.getSelectedItem().toString());
							textFieldGrade.setText(FileData.getStudents().get(comboBoxIds.getSelectedIndex()).getWorkItems().get(comboBoxItems.getSelectedIndex()).getGrade());
							textFieldComments.setText(FileData.getStudents().get(comboBoxIds.getSelectedIndex()).getWorkItems().get(comboBoxItems.getSelectedIndex()).getComments());
							/*textFieldId.disable();
							textFieldWork.disable();
							textFieldGrade.disable();
							textFieldComments.disable();*/
							btnCreate.setEnabled(false);
							btnUpdate.setEnabled(true);
							btnDelete.setEnabled(true);
							
							
						}
						else{
							comboBoxItems.addItem("Create New");
							comboBoxItems.disable();
							textFieldId.setText(comboBoxIds.getSelectedItem().toString());
							textFieldWork.setText("None");
							textFieldGrade.setText("None");
							textFieldComments.setText("None");
							btnCreate.setEnabled(true);
							btnUpdate.setEnabled(false);
							btnDelete.setEnabled(false);
							textFieldId.enable();
							textFieldWork.enable();
							textFieldGrade.enable();
							textFieldComments.enable();
						}
				}
				
				comboBoxItems.addActionListener(temp[0]);
				comboBoxIds.addActionListener(temp[1]);
			}
			
		});
		
		
		
		lblWorkItems = new JLabel("Work Items");
		GridBagConstraints gbc_lblWorkItems = new GridBagConstraints();
		gbc_lblWorkItems.anchor = GridBagConstraints.EAST;
		gbc_lblWorkItems.insets = new Insets(0, 0, 5, 5);
		gbc_lblWorkItems.gridx = 1;
		gbc_lblWorkItems.gridy = 2;
		contentPane.add(lblWorkItems, gbc_lblWorkItems);
		GridBagConstraints gbc_comboBoxItems = new GridBagConstraints();
		gbc_comboBoxItems.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxItems.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxItems.gridwidth = 4;
		gbc_comboBoxItems.gridx = 2;
		gbc_comboBoxItems.gridy = 2;
		contentPane.add(comboBoxItems, gbc_comboBoxItems);
		
		lbldetailsBelow = new JLabel("--Details Below--");
		GridBagConstraints gbc_lbldetailsBelow = new GridBagConstraints();
		gbc_lbldetailsBelow.insets = new Insets(0, 0, 5, 5);
		gbc_lbldetailsBelow.gridx = 3;
		gbc_lbldetailsBelow.gridy = 3;
		contentPane.add(lbldetailsBelow, gbc_lbldetailsBelow);
		
		lblId = new JLabel("Id");
		GridBagConstraints gbc_lblId = new GridBagConstraints();
		gbc_lblId.insets = new Insets(0, 0, 5, 5);
		gbc_lblId.anchor = GridBagConstraints.EAST;
		gbc_lblId.gridx = 1;
		gbc_lblId.gridy = 4;
		contentPane.add(lblId, gbc_lblId);
		
		textFieldId = new JTextField();
		GridBagConstraints gbc_textFieldId = new GridBagConstraints();
		gbc_textFieldId.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldId.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldId.gridwidth = 4;
		gbc_textFieldId.gridx = 2;
		gbc_textFieldId.gridy = 4;
		contentPane.add(textFieldId, gbc_textFieldId);
		textFieldId.setColumns(10);
		
		lblWork = new JLabel("Work");
		GridBagConstraints gbc_lblWork = new GridBagConstraints();
		gbc_lblWork.insets = new Insets(0, 0, 5, 5);
		gbc_lblWork.anchor = GridBagConstraints.EAST;
		gbc_lblWork.gridx = 1;
		gbc_lblWork.gridy = 5;
		contentPane.add(lblWork, gbc_lblWork);
		
		textFieldWork = new JTextField();
		GridBagConstraints gbc_textFieldWork = new GridBagConstraints();
		gbc_textFieldWork.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldWork.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldWork.gridwidth = 4;
		gbc_textFieldWork.gridx = 2;
		gbc_textFieldWork.gridy = 5;
		contentPane.add(textFieldWork, gbc_textFieldWork);
		textFieldWork.setColumns(10);
		
		
		
		
		lblGrade = new JLabel("Grade");
		GridBagConstraints gbc_lblGrade = new GridBagConstraints();
		gbc_lblGrade.anchor = GridBagConstraints.EAST;
		gbc_lblGrade.insets = new Insets(0, 0, 5, 5);
		gbc_lblGrade.gridx = 1;
		gbc_lblGrade.gridy = 6;
		contentPane.add(lblGrade, gbc_lblGrade);
		
		textFieldGrade = new JTextField();
		GridBagConstraints gbc_textFieldGrade = new GridBagConstraints();
		gbc_textFieldGrade.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldGrade.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldGrade.gridwidth = 4;
		gbc_textFieldGrade.gridx = 2;
		gbc_textFieldGrade.gridy = 6;
		contentPane.add(textFieldGrade, gbc_textFieldGrade);
		textFieldGrade.setColumns(10);
		
		lblComments = new JLabel("Comments");
		GridBagConstraints gbc_lblComments = new GridBagConstraints();
		gbc_lblComments.anchor = GridBagConstraints.EAST;
		gbc_lblComments.insets = new Insets(0, 0, 5, 5);
		gbc_lblComments.gridx = 1;
		gbc_lblComments.gridy = 7;
		contentPane.add(lblComments, gbc_lblComments);
		
		textFieldComments = new JTextField();
		GridBagConstraints gbc_textFieldComments = new GridBagConstraints();
		gbc_textFieldComments.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldComments.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldComments.gridwidth = 4;
		gbc_textFieldComments.gridx = 2;
		gbc_textFieldComments.gridy = 7;
		contentPane.add(textFieldComments, gbc_textFieldComments);
		textFieldComments.setColumns(10);
		
		btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//System.out.println("Create");
				setmodifyData();
			    if(modifyData[0].equals("None")){
			    	label.setText("Result("+Integer.toString(counter)+")");
			    	counter++;
			    	lblResult.setText("Provide Valid Id");
			    	lblResult.setForeground(Color.RED);
			    }
			    else{
			    	input = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<Gradebook>\n";
			    	input = input + "<Student Id=\"" + modifyData[0] + "\">\n";
			    	if(!modifyData[1].equals("None")){
			    		input = input + "<WorkItem name=\"" + modifyData[1] + "\">\n";
			    		input = input + "<Comments>"+modifyData[3]+"</Comments>\n";
			    		input = input + "<Grade>"+modifyData[2]+"</Grade>\n";
			    		input = input + "</WorkItem>\n";
			    	}
			    	input += "</Student>\n</Gradebook>";
			    	////System.out.println(input);
			    	try {
						create();
					} catch (JAXBException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
			}
		});
		
		GridBagConstraints gbc_btnCreate = new GridBagConstraints();
		gbc_btnCreate.anchor = GridBagConstraints.WEST;
		gbc_btnCreate.insets = new Insets(0, 0, 5, 5);
		gbc_btnCreate.gridx = 2;
		gbc_btnCreate.gridy = 8;
		contentPane.add(btnCreate, gbc_btnCreate);
		
		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setmodifyData();
				input = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<Gradebook>\n";
		    	input = input + "<Student Id=\"" + modifyData[0] + "\">\n";
	    		input = input + "<WorkItem name=\"" + modifyData[1] + "\">\n";
	    		input = input + "<Comments>"+modifyData[3]+"</Comments>\n";
	    		input = input + "<Grade>"+modifyData[2]+"</Grade>\n";
	    		input = input + "</WorkItem>\n";
	    		input += "</Student>\n</Gradebook>";
	    		try {
					update();
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		GridBagConstraints gbc_btnUpdate = new GridBagConstraints();
		gbc_btnUpdate.anchor = GridBagConstraints.WEST;
		gbc_btnUpdate.insets = new Insets(0, 0, 5, 5);
		gbc_btnUpdate.gridx = 3;
		gbc_btnUpdate.gridy = 8;
		contentPane.add(btnUpdate, gbc_btnUpdate);
		
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setmodifyData();
				input = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<Gradebook>\n";
		    	input = input + "<Student Id=\"" + modifyData[0] + "\">\n";
	    		input = input + "<WorkItem name=\"" + modifyData[1] + "\">\n";
	    		input = input + "<Comments>"+modifyData[3]+"</Comments>\n";
	    		input = input + "<Grade>"+modifyData[2]+"</Grade>\n";
	    		input = input + "</WorkItem>\n";
	    		input += "</Student>\n</Gradebook>";
	    		
					try {
						delete();
					} catch (JAXBException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
				
			}
		});
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.anchor = GridBagConstraints.WEST;
		gbc_btnDelete.insets = new Insets(0, 0, 5, 5);
		gbc_btnDelete.gridx = 4;
		gbc_btnDelete.gridy = 8;
		contentPane.add(btnDelete, gbc_btnDelete);
		
		label = new JLabel("Result(0)");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 0, 5);
		gbc_label.gridx = 1;
		gbc_label.gridy = 9;
		contentPane.add(label, gbc_label);
		
		lblResult = new JLabel("None");
		GridBagConstraints gbc_lblResult = new GridBagConstraints();
		gbc_lblResult.gridwidth = 4;
		gbc_lblResult.insets = new Insets(0, 0, 0, 5);
		gbc_lblResult.gridx = 2;
		gbc_lblResult.gridy = 9;
		contentPane.add(lblResult, gbc_lblResult);
	
	}

}
