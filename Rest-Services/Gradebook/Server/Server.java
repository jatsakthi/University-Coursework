package restservices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.tomcat.jni.Directory;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import restservices.Students;

@Path("Gradebook")
public class Server {
	@Context ServletContext context;
	private static String Path = "";
	private String greeting;
	private static Students FileData,inputData;
	
	
	@GET
	@Produces(MediaType.APPLICATION_XML)	//consumes
	//@Path("/post")
	public Response getHello() throws Exception{
		
		
		
		File file = new File(this.Path);
		BufferedReader br = new BufferedReader(new FileReader(this.Path));
		StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            sb.append("\n");
            line = br.readLine();
        }
        Response response = Response.status(200).entity(sb.toString()).build();
		return response;
	}
	@PUT
	@Consumes(MediaType.APPLICATION_XML)	//consumes
	//@Path("/post")
	public Response putHello( String track) throws Exception{
		
		// Main Update Function
		File file = new File(this.Path);
		FileData = unmarshal(file);
		inputData = tryunmarshal(track);
		
		int Idfound=-1,workfound=-1;
		
		for(int i=0;i<FileData.students.size();i++){
			if(FileData.students.get(i).getId().equals(inputData.students.get(0).getId()) ){
				// Equal
				Idfound = i;
			}	
		}
		if(Idfound<0){
			// return 404
			Response response = Response.status(404).entity("Not Found").build();
			return response;
		}
		for(int i=0;i<FileData.getStudents().get(Idfound).getWorkItems().size();i++){
			if(FileData.getStudents().get(Idfound).getWorkItems().get(i).getName().equals(inputData.getStudents().get(0).getWorkItems().get(0).getName()))
				workfound = i;
		}
		if(workfound<0){
			// return 404
			Response response = Response.status(404).entity("Not Found").build();
			return response;
		}
		
		FileData.getStudents().get(Idfound).getWorkItems().get(workfound).setGrade(inputData.getStudents().get(0).getWorkItems().get(0).getGrade());
		FileData.getStudents().get(Idfound).getWorkItems().get(workfound).setComments(inputData.getStudents().get(0).getWorkItems().get(0).getComments());
		marshal(file,FileData);
		
		Response response = Response.status(200).entity("OK").build();
		return response;
	
	}
	
	@DELETE
	@Consumes(MediaType.APPLICATION_XML)	//consumes
	//@Path("/post")
	public Response deleteHello( String track) throws Exception{
		// Main Update Function
				File file = new File(this.Path);
				FileData = unmarshal(file);
				inputData = tryunmarshal(track);
				
				int Idfound=-1,workfound=-1;
				
				for(int i=0;i<FileData.students.size();i++){
					if(FileData.students.get(i).getId().equals(inputData.students.get(0).getId()) ){
						// Equal
						Idfound = i;
					}	
				}
				if(Idfound<0){
					// return 404
					Response response = Response.status(404).entity("Not Found").build();
					return response;
				}
				for(int i=0;i<FileData.getStudents().get(Idfound).getWorkItems().size();i++){
					if(FileData.getStudents().get(Idfound).getWorkItems().get(i).getName().equals(inputData.getStudents().get(0).getWorkItems().get(0).getName()))
						workfound = i;
				}
				if(workfound<0){
					// return 404
					Response response = Response.status(404).entity("Not Found").build();
					return response;
				}
				if(!FileData.getStudents().get(Idfound).getWorkItems().get(workfound).getGrade().equals(inputData.getStudents().get(0).getWorkItems().get(0).getGrade()))
				{
					// return 404
					Response response = Response.status(404).entity("Not Found").build();
					return response;
				}
				if(!FileData.getStudents().get(Idfound).getWorkItems().get(workfound).getComments().equals(inputData.getStudents().get(0).getWorkItems().get(0).getComments()))
				{
					// return 404
					Response response = Response.status(404).entity("Not Found").build();
					return response;
				}
				
				if(FileData.getStudents().get(Idfound).getWorkItems().get(workfound).getGrade().equals("None")){
					Response response = Response.status(404).entity("Not Found").build();
					return response;
				}
				
				FileData.getStudents().get(Idfound).getWorkItems().get(workfound).setGrade("None");
				FileData.getStudents().get(Idfound).getWorkItems().get(workfound).setComments("None");
				marshal(file,FileData);
				
				Response response = Response.status(200).entity("OK").build();
				return response;
				
				
				
	}
	
	/**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @param content
     * @return String that will be returned as a text/xml response.
     * @throws Exception 
     */
	@POST
	//@Path("/post")
	//C:/Users/Sakthi/workspace/restservices/src/input.xml
	@Consumes(MediaType.APPLICATION_XML)
	public Response postHello(String track) throws Exception{
		if(track.startsWith("1")){
			this.Path = track.substring(1)+"Server\\res\\user.xml";
			Response response = Response.status(200).entity(this.Path).build();
			return response;
			
		}
		else{
			
			Students listOfStudents = new Students();
			List<Student> data = new ArrayList<Student>();
			Student student = new Student();
			List<WorkItem> workitems = new ArrayList<WorkItem>();
			WorkItem w = new WorkItem();
			
			
			// Main create Function
			File file = new File(this.Path);
			FileData = unmarshal(file);
			inputData = tryunmarshal(track);
			
			int Idfound=-1,workgiven=0,workfound=0;
			if(inputData.students.get(0).items != null){
				workgiven = 1;
			}
			if(FileData.students == null)
			{
				Idfound = -1; 
			
			}
			else{
			for(int i=0;i<FileData.students.size();i++){
				if(FileData.students.get(i).getId().equals(inputData.students.get(0).getId()) ){
					// Equal
					Idfound = i;
				}	
			}
			}
			if(Idfound >= 0 ){
				if(workgiven == 0){
					Response response = Response.status(409).entity("Conflict").build();
					return response;
				}
				else{
					workfound = 0;
					if(FileData.getStudents().get(Idfound).items != null){
					for(int i=0;i<FileData.getStudents().get(Idfound).getWorkItems().size();i++){
						if(FileData.getStudents().get(Idfound).getWorkItems().get(i).getName().equals(inputData.getStudents().get(0).getWorkItems().get(0).getName()))
							workfound = 1;
					}
					if(workfound ==1 ){
						Response response = Response.status(409).entity("Conflict").build();
						return response;
					}
					else{
						FileData.getStudents().get(Idfound).addWorkItem(inputData.getStudents().get(0).getWorkItems().get(0));
						marshal(file,FileData);
						
						Response response = Response.status(200).entity("OK").build();
						return response;
					}
					}
					else{
						FileData.getStudents().get(Idfound).addWorkItem(inputData.getStudents().get(0).getWorkItems().get(0));
						marshal(file,FileData);
						
						Response response = Response.status(200).entity("OK").build();
						return response;
					}
				}
				
				
			}
			else{
				// simply insert the new id in the list and marshal the file
				if(workgiven == 1){
					w.setComments(inputData.getStudents().get(0).getWorkItems().get(0).getComments());
					w.setGrade(inputData.getStudents().get(0).getWorkItems().get(0).getGrade());
					w.setName(inputData.getStudents().get(0).getWorkItems().get(0).getName());
					workitems.add(w);
					student.setWorkItems(workitems);
				}
				
				student.setId(inputData.students.get(0).getId());
				FileData.add(student);
				marshal(file,FileData);
				
				Response response = Response.status(200).entity("OK").build();
				return response;
			}
		}
		
		
	}
		

	private static Students unmarshal(File file) throws JAXBException{
		JAXBContext jaxbContext = JAXBContext.newInstance(Students.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		Students FileData = (Students) jaxbUnmarshaller.unmarshal(file);
		return FileData;
	}
		private static Students tryunmarshal(String data) throws JAXBException{
			
			JAXBContext jaxbContext = JAXBContext.newInstance(Students.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(data);
			Students FileData = (Students) jaxbUnmarshaller.unmarshal(reader);
			return FileData;
		}

		private static void marshal(File file, Students listOfstudents) throws JAXBException {
			// TODO Auto-generated method stub
			JAXBContext jaxbContext = JAXBContext.newInstance(Students.class);
			 Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			 jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			 jaxbMarshaller.marshal(listOfstudents, file);
			 
		}


}
