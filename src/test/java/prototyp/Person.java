package prototyp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="person")
public class Person {

	public Person() {
		super();
	}

	public Person(String lastname, String firstname, Date birth) {
		this.lastname = lastname;
		this.firstname = firstname;
		this.birth = birth;
	}

	@Id
	@GeneratedValue
    private int personid;

	@Column(name="lastname")
	private String lastname;

	@Column(name="firstname")
	private String firstname;

	@Column(name="birth")
	private Date birth;

	public int getPersonid() {
		return personid;
	}

	public void setPersonid(int personid) {
		this.personid = personid;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	@Override
	public String toString() {
		return "personid=" + personid + ",lastname=" + lastname + ",firstname=" + firstname + ",birth=" + birth;
	}

}
