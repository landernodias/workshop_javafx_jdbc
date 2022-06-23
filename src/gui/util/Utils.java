package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	public static Stage currentStage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow(); // Acessa o Stage onde o controler que recebeu o evento está
	}
	// testa se o dados são integer
	public static Integer tryParseTOInt(String str) {
		try {
			return Integer.parseInt(str);			
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
