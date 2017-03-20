package pack;


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement( name = "Gradebook" )
public class Students {
	List<Student> students;

	@XmlElement( name = "Student" )
	public void setStudents( List<Student> students )
	{
	this.students = students;
	}
	public List<Student> getStudents(){
		return this.students;
	}

	public void add( Student Student )
	{
	if( this.students == null )
	{
	this.students = new ArrayList<Student>();
	}
	this.students.add( Student );
}
}
