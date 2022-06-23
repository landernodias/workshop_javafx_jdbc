package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable{
	
	private DepartmentService service;
	
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Department> obsList;
		
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Department obj = new Department();
		createDialogForm(obj,"/gui/DepartmentForm.fxml",parentStage);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	//inicia componente da tela
	private void initializeNodes() {
		// inicia o comportamento das colunas da tabela
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		//fazer a tableview acompanhar a largura e altura da janela
		Stage stage = (Stage) Main.getMainScene().getWindow(); // pega referencia para a janela
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty()); //faz o table view acompanha a altura da janela

	}
	
	public void updateTableView() {
		if (service == null) { // verifica se a o serviço é nulo
			throw new IllegalStateException("Service was null");
		}
		List<Department> list = service.findAll(); // pega a lista de departamento
		//carregando a lista no observableList
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(obsList);//coloca na tableview
	}
	
	//chama a janela para prencher/cadastrar um novo departamento
	private void createDialogForm(Department obj,String absoluteName, Stage parentStage) {// informa quem é o stage que crio a janela de dialogo
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();// carrega a view
			
			DepartmentFormController controller = loader.getController();// pega o controller
			// injeta no controller o departamento
			controller.setDepartment(obj);// injeção de dependencia da entidade
			controller.setDepartmentService(new DepartmentService());//injeção de dependencia do serviço
			controller.updateFormData();//carrega os dados do objeto no formulário
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data");//titulo da scene
			dialogStage.setScene(new Scene(pane));// a cena do stage
			dialogStage.setResizable(false);// redimencionamento da janela
			dialogStage.initOwner(parentStage);//stage pai da janela
			dialogStage.initModality(Modality.WINDOW_MODAL);// define que a janela vai ser um modal: qunado não fechar ela não acessa a outra
			dialogStage.showAndWait();
			
			
		}catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

}
