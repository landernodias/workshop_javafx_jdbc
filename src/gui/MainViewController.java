package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class MainViewController implements Initializable{

	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}
	
	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> { // janela com ação
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {}); // janela que não tem ação
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle br) {
		
	}

	/*
	 * A utilização do Consumer: é para não pricisar criar uma duplicação de codigo loadView
	 * 1 - a Instanciação do serviço é feito fia uma função callback passada por parametro ao loadview
	 * 2 - O generics: o consumer pega essa função e executa dentro do loadView
	 * */
	
	//synchronized: esse processamento não vai ser interrompido
	//parametrização com Consumer <T> para deixar a função genérica
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {// loadview é uma função gererica do tipo T
		try {
			//carregar uma tela
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			//Mostra view dentro da janela principal
			Scene mainScene = Main.getMainScene();// referência para a scena.
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();// pega referência para vbox da janela principal
			
			//referencia para o menu
			Node mainMenu = mainVBox.getChildren().get(0); // primeiro filho do vBox da janela principal mainmenu
			mainVBox.getChildren().clear(); // limpa os filhos do mainVbox
			mainVBox.getChildren().add(mainMenu); // adciona o main menu
			mainVBox.getChildren().addAll(newVBox.getChildren()); //add os filhos da janela que está sendo aberto
			
			// ativar a função passado por parametro (Executa)
			T controller = loader.getController(); // retorna um controller do tipo definino na passagem de parametro
			initializingAction.accept(controller); // função do consumer
			
		} catch (IOException e) {
			//alert personalizado
			Alerts.showAlert("IO Execption", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
