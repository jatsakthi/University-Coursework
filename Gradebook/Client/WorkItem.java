package pack;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "WorkItem" )
public class WorkItem {
private String name;
private String grade;
private String comments;

@XmlAttribute (name = "name")
public void setName(String name){
	this.name = name;
}
public String getName(){
	return name;
}

@XmlElement( name = "Grade" )
public void setGrade( String grade )
{
this.grade = grade;
}

public String getGrade(){
	
	return this.grade;
}

@XmlElement( name = "Comments" )
public void setComments( String comments )
{
this.comments = comments;
}

public String getComments(){
	
	return this.comments;
}
}
