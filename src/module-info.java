module workshop_javafx_jdbc {
	requires javafx.controls;
	requires javafx.fxml;
	
	opens application to javafx.graphics, javafx.fxml;
	opens gui to javafx.fxml, javafx.controls;
	opens gui.util to javafx.controls;
}
