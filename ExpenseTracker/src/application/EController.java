package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EController implements Initializable {

	@FXML
	private TableColumn<E, String> dateC;

	@FXML
	private DatePicker dateI;

	@FXML
	private TableView<E> table;

	@FXML
	private TextField expenseI;

	@FXML
	private Button erase;

	@FXML
	private TableColumn<E, Double> classificationC;

	@FXML
	private Button add;

	@FXML
	private TableColumn<E, String> expenseC;

	@FXML
	private ComboBox<String> classificationI;

	@FXML
	private Button restart;

	@FXML
	private Button modify;
	
	static int a = 0;
	static int b = 0;
	static int c = 0;

	


	private ObservableList<String> list = (ObservableList<String>) FXCollections.observableArrayList("Home Expenses", "Entertainment/Eating out", "Gifts/Donations");

	public ObservableList <E> EData = FXCollections.observableArrayList();

	public ObservableList <E> getEData(){

		return EData;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		classificationI.setItems(list);
		dateC.setCellValueFactory(new PropertyValueFactory<>("Date"));
		classificationC.setCellValueFactory(new PropertyValueFactory<>("Classification"));
		expenseC.setCellValueFactory(new PropertyValueFactory<>("Expense"));
		table.setItems(EData);

		modify.setDisable(true);
		erase.setDisable(true);
		restart.setDisable(true);

		showEs(null);
		table.getSelectionModel().selectedItemProperty().addListener((
				observable, oldValue, newValue)-> showEs(newValue));


	}

	@FXML
	void add() {
		
		if (noEmptyInput()) {
		E tmp = new E ();
		tmp = new E();
		tmp.setDate(dateI.getValue());
		tmp.setExpense(Double.parseDouble(expenseI.getText()));
		tmp.setClassification(classificationI.getValue());
		if (classificationI.getValue() == "Home expenses") {
			a++;
		}
		else if (classificationI.getValue() == "Entertainment/Eating out") {
			b++;
		}else {
			c++;
		}
		EData.add(tmp);
		clearFields();
		}


	}

	@FXML
	void clearFields() {
		classificationI.setValue(null);
		dateI.setPromptText("");
		expenseI.setText("");
	}

	@FXML
	public void showEs(E e) {
		if (e != null) {
			classificationI.setValue(e.getClassification());
			dateI.setValue(e.getDate());
			expenseI.setText(Double.toString(e.getExpense()));
			modify.setDisable(false);
			erase.setDisable(false);
			restart.setDisable(false);
		}
		else {
			clearFields();
		}
	}
	

	@FXML
	public void updateE() {
		
		if(noEmptyInput()) {
		E e = table.getSelectionModel().getSelectedItem();
		e.setDate(dateI.getValue());
		e.setExpense(Double.parseDouble(expenseI.getText()));
		e.setClassification(classificationI.getValue());
		table.refresh();
		}
		
	}
	
	public void deleteE() {

		
		int selectedIndex = table.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Effacer");
			alert.setContentText("confirmer la suppression");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK)
				table.getItems().remove(selectedIndex);
		}
	}
	
	private boolean noEmptyInput() {
		String errorMessage = "";
		
		if (dateI.getPromptText() == null){
			errorMessage+="Le champ prenom ne doit pas etre vide \n";
		}
		if (expenseI.getText() == null){
			errorMessage+="Le champ age ne doit pas etre vide \n";
		}
		if (classificationI.getValue() == null){
			errorMessage+="Le champ departement ne doit pas etre vide \n";
		}
		
		if (errorMessage.length()==0) {
			return true;
		}else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Champs manquants");
			alert.setHeaderText("Completer les champs manquants");
			alert.setContentText(errorMessage);
			alert.showAndWait();
			return false;
		}
		
	}
	@FXML
	/*
	void handleStats(){
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("EStat.FXML"));
			AnchorPane pane = loader.load();
			Scene scene = new Scene (pane);
			EStat estat = loader.getController();
			estat.SetStats(EData);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Expense Graph");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/

	public File getEFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String filePath = prefs.get("filePath", null);
		if (filePath != null) {
			return new File(filePath);
		}else {
			return null;
		}
	}
	public void setEFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		if (file != null) {
			prefs.put("filePath", file.getPath());
		}else {
			prefs.remove("filePath");
		}
	}
	public void loadEDataFromFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(EListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();
			
			EListWrapper wrapper = (EListWrapper) um.unmarshal(file);
			EData.clear();
			EData.addAll(wrapper.getEs());
			setEFilePath(file);
			
			Stage pStage = (Stage) table.getScene().getWindow();
			pStage.setTitle(file.getName());
			
		} catch (Exception e) {
			Alert alert = new Alert (AlertType.ERROR);
			alert.setTitle("Erreur");
			alert.setHeaderText("les donnees n'ont pas ete trouvees");
			alert.setContentText("Les donnees no pouvaient pas etre trouvees dans le fichier : \n" + file.getPath());
			alert.showAndWait(); 
		}
	}
	
	 public void saveEDataToFile(File file) {
		 try {
			 JAXBContext context = JAXBContext.newInstance(EListWrapper.class);
			 Marshaller m = context.createMarshaller();
			 m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			 EListWrapper wrapper = new EListWrapper();
			 wrapper.setEs(EData);
			 
			 m.marshal(wrapper, file);
			 setEFilePath(file);
			 Stage pStage = (Stage) table.getScene().getWindow();
			 pStage.setTitle(file.getName());
			 
		 }catch (Exception e) {
			 Alert alert = new Alert(AlertType.ERROR);
			 alert.setTitle("Erreur");
			 alert.setHeaderText("Donnees no sauvegardees");
			 alert.setContentText("Les donnees ne pouvait pas etre sauvegardees dans le fichier: \n" + file.getPath());
			 alert.showAndWait();
		 }
	 }
	 
	 @FXML
	 private void handleNew() {
		 getEData().clear();
		 setEFilePath(null);
		 
	 }
	 
	 @FXML
	 private void handleOpen() {
		 FileChooser fileChooser = new FileChooser();
		 FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		 
		 fileChooser.getExtensionFilters().add(extFilter);
		 File file = fileChooser.showOpenDialog(null);
		 
		 if (file != null) {
			 loadEDataFromFile(file);
		 }
	 }
	 @FXML
	 private void handleSave() {
		 File EFile = getEFilePath();
		 if (EFile != null) {
			 saveEDataToFile(EFile);
			
		 }else {
			 handleSaveAs(); 
		 }
	 }
	 @FXML
	 private void handleSaveAs() {
		 FileChooser fileChooser = new FileChooser();
		 FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		 
		 fileChooser.getExtensionFilters().add(extFilter);
		 File file = fileChooser.showSaveDialog(null);
		 
		 if (file != null) {
			 if (!file.getPath().endsWith(".xml")){
				 file = new File(file.getPath()+ ".xml");
			 }
			 saveEDataToFile(file);
		 }
	 }
	 
	 @FXML
	 public void verifNum() {
		 expenseI.textProperty().addListener((observable, oldValue, newValue)->
		 {
			 if (!newValue.matches("^[0-9](\\.[0-9]+)?$")) {
				 expenseI.setText(newValue.replaceAll("[^\\d*\\.]", ""));
			 }
		 });
	 }
	
	
	
	
	
	
	
}

