package gui.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import javafx.util.StringConverter;

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
	
	public static <T> void formatTableColumnDate(TableColumn<T, Date> tableColumn, String format) {
		tableColumn.setCellFactory(Column -> {
			TableCell<T, Date> cell = new TableCell<T, Date>() {
				private SimpleDateFormat sdf = new SimpleDateFormat(format);
				
				@Override
				protected void updateItem(Date item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						setText(sdf.format(item));
					}
				}
			};
			
			return cell;
		});
	}
	
	public static <T> void formatTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces) {
		tableColumn.setCellFactory(column -> {
			TableCell <T, Double> cell = new TableCell<T, Double>(){
				
				@Override
				protected void updateItem(Double item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						Locale.setDefault(Locale.US);
						setText(String.format("%." + decimalPlaces + "f", item));
					}
				}
			};
			return cell;
		});
	}
	
	//formata a data no formato desejado
	public static void formatDatePicker(DatePicker datePiker, String format) {
		datePiker.setConverter(new StringConverter<LocalDate>() {
			
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
			
			@Override
			public String toString(LocalDate date) {
				if(date != null) {
					return dateFormatter.format(date);
				} else {
					return "";					
				}
			}
			
			@Override
			public LocalDate fromString(String string) {
				if(string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateFormatter);
				}
				return null;
			}
		});
		
		
	}
	
}
