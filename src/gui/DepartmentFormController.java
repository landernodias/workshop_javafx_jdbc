package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable{

	private Department entity; // associação com departamento
	
	@FXML
	private TextField txId;
	
	@FXML
	private TextField txName;
	
	@FXML
	private Label labelErronName;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	//instanciação de um departamento
	public void setDepartment(Department entity) {
		this.entity = entity;
	}
	
	@FXML
	public void onBtSaveAction() {
		System.out.println("onBtSaveAction");
	}
	
	@FXML
	public void onBtCancelAction() {
		System.out.println("onBtCancelAction");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {	
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txId);
		Constraints.setTextFieldMaxLengt(txName, 30);
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txId.setText(String.valueOf(entity.getId()));//converte um inteiro para uma string
		txName.setText(entity.getName());
	}

}
