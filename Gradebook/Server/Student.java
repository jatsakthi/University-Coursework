package restservices;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement( name = "Student" )
public class Student
{
List<WorkItem> items;
private String id;

@XmlAttribute( name = "Id" )
public void setId( String id )
{
this.id = id;
}

public String getId(){
	
	return this.id;
}
@XmlElement( name = "WorkItem" )
public void setWorkItems(List<WorkItem> a) {
	// TODO Auto-generated method stub
	this.items = a;
}
public List<WorkItem> getWorkItems(){
	
	return this.items;
}

public void addWorkItem(WorkItem a){
	if(this.items == null){
		this.items = new ArrayList<WorkItem>();
		
	}
	this.items.add(a);
	
}



}