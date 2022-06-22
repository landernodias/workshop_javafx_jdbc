package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
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

}
