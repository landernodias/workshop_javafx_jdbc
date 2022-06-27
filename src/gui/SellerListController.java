package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
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
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener{
	
	private SellerService service;
	
	@FXML
	private TableView<Seller> tableViewSeller;
	
	@FXML
	private TableColumn<Seller, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Seller, String> tableColumnName;
	
	@FXML
	private TableColumn<Seller, String> tableColumnEmail;
	
	@FXML
	private TableColumn<Seller, Date> tableColumnBirthDate;
	
	@FXML
	private TableColumn<Seller, Double > tableColumnBaseSalary;
	
	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT;
	
	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Seller> obsList;
		
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Seller obj = new Seller();
		createDialogForm(obj,"/gui/SellerForm.fxml",parentStage);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	public void setSellerService(SellerService service) {
		this.service = service;
	}

	//inicia componente da tela
	private void initializeNodes() {
		// inicia o comportamento das colunas da tabela
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Utils.formatTableColumnDate(tableColumnBirthDate, "ddMM/yyyy");
		tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);
		
		//fazer a tableview acompanhar a largura e altura da janela
		Stage stage = (Stage) Main.getMainScene().getWindow(); // pega referencia para a janela
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty()); //faz o table view acompanha a altura da janela

	}
	
	public void updateTableView() {
		if (service == null) { // verifica se a o serviço é nulo
			throw new IllegalStateException("Service was null");
		}
		List<Seller> list = service.findAll(); // pega a lista de departamento
		//carregando a lista no observableList
		obsList = FXCollections.observableArrayList(list);
		tableViewSeller.setItems(obsList);//coloca na tableview
		initEditButtons();// acrescenta novo botão com texto edit em cada linha da tabela
		initRemoveButtons();// remove uma linha da tabela
	}
	
	//chama a janela para prencher/cadastrar um novo departamento
	private void createDialogForm(Seller obj,String absoluteName, Stage parentStage) {// informa quem é o stage que crio a janela de dialogo
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();// carrega a view
			
			SellerFormController controller = loader.getController();// pega o controller
			// injeta no controller o departamento
			controller.setSeller(obj);// injeção de dependencia da entidade
			controller.setSellerService(new SellerService());//injeção de dependencia do serviço
			controller.subscribeDataCHangeListener(this);// inscreve para receber o evento
			controller.updateFormData();//carrega os dados do objeto no formulário
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Seller data");//titulo da scene
			dialogStage.setScene(new Scene(pane));// a cena do stage
			dialogStage.setResizable(false);// redimencionamento da janela
			dialogStage.initOwner(parentStage);//stage pai da janela
			dialogStage.initModality(Modality.WINDOW_MODAL);// define que a janela vai ser um modal: qunado não fechar ela não acessa a outra
			dialogStage.showAndWait();
			
			
		}catch (IOException e) {
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>(){//instância e configura o evento do botão
			private final Button button = new Button("edit");
			
			@Override
			protected void updateItem(Seller obj, boolean empty) {//obj: department da linha clicada
				super.updateItem(obj, empty);
			
				if (obj == null) {
					setGraphic(null);
					return;
				}
				
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));// gera a janela do forms com objeto preenchido
			}
			
		});
	}

	//função do button edit
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>(){//instância e configura o evento do botão
			private final Button button = new Button("remove");
			
			@Override
			protected void updateItem(Seller obj, boolean empty) {//obj: department da linha clicada
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

	private void removeEntity(Seller obj) {
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
