package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener{
	
	private DepartmentService service;
	
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private TableColumn<Department, Department> tableColumnEDIT;
	
	@FXML
	private TableColumn<Department, Department> tableColumnREMOVE;
	
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
		initEditButtons();// acrescenta novo botão com texto edit em cada linha da tabela
		initRemoveButtons();// remove uma linha da tabela
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
			controller.subscribeDataCHangeListener(this);// inscreve para receber o evento
			controller.updateFormData();//carrega os dados do objeto no formulário
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data");//titulo da scene
			dialogStage.setScene(new Scene(pane));// a cena do stage
			dialogStage.setResizable(false);// redimencionamento da janela
			dialogStage.initOwner(parentStage);//stage pai da janela
			dialogStage.initModality(Modality.WINDOW_MODAL);// define que a janela vai ser um modal: qunado não fechar ela não acessa a outra
			dialogStage.showAndWait();
			
			
		}catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		//executa quando os dados são alterado
		updateTableView();
	}
	
	//função do button edit
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>(){//instância e configura o evento do botão
			private final Button button = new Button("edit");
			
			@Override
			protected void updateItem(Department obj, boolean empty) {//obj: department da linha clicada
				super.updateItem(obj, empty);
			
				if (obj == null) {
					setGraphic(null);
					return;
				}
				
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));// gera a janela do forms com objeto preenchido
			}
			
		});
	}

	//função do button edit
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Department, Department>(){//instância e configura o evento do botão
			private final Button button = new Button("remove");
			
			@Override
			protected void updateItem(Department obj, boolean empty) {//obj: department da linha clicada
				super.updateItem(obj, empty);
			
				if (obj == null) {
					setGraphic(null);
					return;
				}
				
				setGraphic(button);
				button.setOnAction(
						event -> removeEntity(obj));// gera a janela do forms com objeto preenchido
			}
			
		});
	}

	private void removeEntity(Department obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		
		if (result.get() == ButtonType.OK) {
			if(service == null) { //dependencia não foi injetada
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				updateTableView();//atualiza os dados da tabela
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}
