module workshop_javafx_jdbc {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires java.sql;	
	
	opens application to javafx.graphics, javafx.fxml, javafx.base;
	opens gui to javafx.fxml, javafx.controls;
	opens gui.util to javafx.controls;
	opens gui.listeners to javafx.graphics, javafx.fxml, javafx.base;
	opens model.entities to javafx.base;
	opens model.exceptions to  javafx.graphics, javafx.fxml, javafx.base;
	opens model.services to javafx.graphics, javafx.fxml, javafx.base;
	opens model.dao to javafx.graphics, javafx.fxml, javafx.base;
	//opens bd to javafx.graphics, javafx.fxml, javafx.base;
}
