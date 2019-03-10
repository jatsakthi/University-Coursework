/*
 * 					DESCRIPTION OF THE SCENARIO
 * 
 * Problem Statement: 
 * ------------------
 * To design and simulate a disk which supports segregation of files into separate
 * folder based on their types. For e.g. a file of type document must go into Documents Folder,
 * a movie into Movies Folder and a picture into Pictures folder. Also must keep in mind, that 
 * the design must be able to easily support future addition of new Folders which have other types
 * of files in them according to the need of the client.
 * 
 * Solution:
 * ---------
 *		Design Patters Used: FACTORY METHOD & SINGLETON
 *		-----------------------------------------------
 *
 *			      Folder
 *			   	|
 *		----------------|----------------
 *		|		|		|
 *	     Pictures	     Movies	     Documents		<-------> FolderFactory <--------> SimulateDisk
 *		^		^		^		|
 *		|		|		|		|
 *		-------------------------------------------<-----
 *
 *	The design pattern used to solve the problem is Factory method. The reasons are:
 *		1.	The Folder class cannot anticipate the type of object it is supposed to crate.
 *		2.	This way, Folder class lets its subclasses to decide which type of class to instantiate.
 *
 *	Thus, here the SimulateDisk provides the FileType of the new file (to be put in or deleted) to the
 *	the FolderFactory. The FolderFactory fetches the appropriate type of Folder to SimulateDisk. 
 *
 *	Also, importantly, as only one folder must exist relate to a particular name, Hence each Subclass of the
 *	Folder class must be of Singleton type. This ensures the above mentioned constraint and prevents the design
 *	of multiple folder objects of the same type/name.
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

enum FileType {
	Picture,Movie,Document;
}

class File {
	protected String name;
	private String date,time;
	private int size;
	protected List<String> otherDetails;
	protected FileType type;
	
	public File(String Name, String Date, String Time, int Size, FileType type){
		name = Name;
		date = Date;
		time = Time;
		size = Size;
		this.type = type;
		otherDetails = new ArrayList<String>();
	}
	public void printDetails(){
		System.out.println("Name:"+ name);
		System.out.println("Created Date:"+ date);
		System.out.println("Created Time:"+ time);
		System.out.println("Created Size:"+ size);
	}
}

abstract class Folder {
	private Map<String, File> files;
	private String folderName;
	
	public Folder(){
		files = new HashMap<>();
	}
	public abstract String getFolderName();
	public abstract void printFileDetails(File file);
	public abstract void getFileDetails(File file);
	
	public boolean isFilePresent(String fileName){
		return files.containsKey(fileName);
	}
	public void addFile(File file) throws Exception{
		getFileDetails(file);
		if(isFilePresent(file.name)){
			throw new Exception("File with same  name exists");
		}
		files.put(file.name, file);
	}
	public void deleteFile(String fileName) throws Exception{
		if(!isFilePresent(fileName)){
			throw new Exception("File doesn't exist");
		}
		files.remove(fileName);
	}
	public void showFiles(){
		System.out.println("-----------------------------------");
		System.out.println("Contents of the Folder: "+getFolderName());
		System.out.println("-----------------------------------");
		for(Map.Entry<String, File> e: files.entrySet()){
			File f = e.getValue();
			f.printDetails();
			printFileDetails(f);
			System.out.println("-----------------------------------");
		}
		System.out.println("--------------END---------------------");
		
	}
	
}

class FolderFactory {
	public static Folder getFolderForFile(FileType filetype){
		if(filetype == null) return null;
		if(filetype == FileType.Picture) return Pictures.getInstance();
		else if(filetype == FileType.Movie) return Movies.getInstance();
		else if(filetype == FileType.Document) return Documents.getInstance();
		else return null;
	}
}

class Movies extends Folder{
	private static final Movies soleInstance = new Movies();
	public static Folder getInstance(){
		return soleInstance;
	}
	
	@Override
	public void printFileDetails(File file) {
		System.out.println("Duration: "+file.otherDetails.get(0));
		System.out.println("Genre: "+file.otherDetails.get(1));
	}

	@Override
	public void getFileDetails(File file) {
		String duration,genre;
		Scanner s = new Scanner(System.in);
		System.out.println("Enter Duration of the Movie");
		duration = s.next();
		System.out.println("Enter Genre of the Movie?");
		genre = s.next();
		file.otherDetails.add(duration);
		file.otherDetails.add(genre);

	}

	@Override
	public String getFolderName() {
		return "Movies";
	}

}
class Pictures extends Folder{
	private static Pictures soleInstance = new Pictures();
	public static Pictures getInstance(){
		return soleInstance;
	}
	@Override
	public void printFileDetails(File file) {
		System.out.println("Dimension: "+file.otherDetails.get(0));
		System.out.println("Is Colored: "+file.otherDetails.get(1));

	}

	@Override
	public void getFileDetails(File file) {
		String dimension,isColor;
		Scanner s = new Scanner(System.in);
		System.out.println("Enter Dimension of the Picture");
		dimension = s.next();
		System.out.println("Enter if the Picture is Colored? Yes/No");
		isColor = s.next();
		file.otherDetails.add(dimension);
		file.otherDetails.add(isColor);

	}

	@Override
	public String getFolderName() {
		return "Pictures";
	}

}

class Documents extends Folder{
	private static final Documents soleInstance = new Documents();
	public static Folder getInstance(){
		return soleInstance;
	}
	
	@Override
	public void printFileDetails(File file) {
		System.out.println("Author: "+file.otherDetails.get(0));
		System.out.println("Type: "+file.otherDetails.get(1));
	}

	@Override
	public void getFileDetails(File file) {
		String author,type;
		Scanner s = new Scanner(System.in);
		System.out.println("Enter Author of the File");
		author = s.next();
		System.out.println("Enter Type of the Document?");
		type = s.next();
		file.otherDetails.add(author);
		file.otherDetails.add(type);
	}
	
	@Override
	public String getFolderName() {
		return "Documents";
	}

}

public class SimulateDisk {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner s = new Scanner(System.in);
		File file;
		Folder folder;
		String name="",date="",time="",temp="";
		int type=0,size=0;
		while(true){
			System.out.println("Enter 1: For Addition of File 2: For Deletion of File");
			try{
				int choice = Integer.parseInt(s.nextLine());
				switch(choice){
				case 1: {
					try{
						System.out.println("Enter Name of the File");
						name = s.nextLine();
						System.out.println("Enter Date of creation of File (mm/dd/yyyy)");
						date = s.nextLine();
						System.out.println("Enter Time of creation of File (24 Hr format)");
						time = s.nextLine();
						System.out.println("Enter size of the File (integer)");
						size = Integer.parseInt(s.nextLine());
						System.out.println("Enter File type\n(1 for: Picture)\n(2 for: Movie)\n(3 for: Document)");
						type = Integer.parseInt(s.nextLine());
						if(type<=0 || type>FileType.values().length){
							System.out.println("Please enter valid type 1-"+(FileType.values().length));
							break;
						}
						file = new File(name,date,time,size,FileType.values()[type-1]);
						folder = FolderFactory.getFolderForFile(file.type);
						folder.addFile(file);
						folder.showFiles();
						break;
					}catch(Exception e){
						e.printStackTrace();				
					}
					break;
				}
				case 2: {
					try{	
						System.out.println("Enter Name of the File");
						name = s.nextLine();
						System.out.println("Enter File type\n(1 for: Picture)\n(2 for: Movie)\n(3 for: Document)");
						type = Integer.parseInt(s.nextLine());
						if(type<=0 || type>FileType.values().length){
							System.out.println("Please enter valid type 1-"+(FileType.values().length));
							break;
						}
						folder = FolderFactory.getFolderForFile(FileType.values()[type-1]);
						folder.deleteFile(name);
						folder.showFiles();
					}catch(Exception e){
						e.printStackTrace();				
					}
					break;
				}
				default: break;
				}
			}catch(Exception e){
				e.printStackTrace();			
			}
		}


	}

}
