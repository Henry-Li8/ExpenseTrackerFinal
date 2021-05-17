package application;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class E {
	private LocalDate Date;
	private String Classification;
	private Double Expense;
	
	
	public E() {
		this(null);
	}
	
	public E (String Classification) {
		this.Classification = "";
		this.Expense = 0.0;
		
	}
	
	
	
	
	public String getClassification() {
		return Classification;
	}
	public void setClassification(String Classification) {
		this.Classification = Classification;
	}
	public Double getExpense() {
		return Expense;
	}
	public void setExpense(Double Expense) {
		this.Expense = Expense;
	}
	
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	public LocalDate getDate() {
		return Date;
	}

	public void setDate(LocalDate date) {
		Date = date;
	}
	
}
