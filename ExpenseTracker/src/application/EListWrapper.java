package application;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="etudiants")
public class EListWrapper {
	private List<E> E;
	@XmlElement(name = "etudiant")
	public List<E> getEs(){
		return E;
	}
	public void setEs(List<E> etudiants) {
		this.E = etudiants;
	}
}
