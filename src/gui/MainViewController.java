package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
		System.out.println("onMenuItemDepartmentAction");
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml");
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle br) {
		
	}

	//synchronized: esse processamento não vai ser interrompido
	private synchronized void loadView(String absoluteName) {
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
			
			
		} catch (IOException e) {
			//alert personalizado
			Alerts.showAlert("IO Execption", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
