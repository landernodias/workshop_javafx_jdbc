module workshop_javafx_jdbc {
	requires javafx.controls;
	
	opens application to javafx.graphics, javafx.fxml;
}
