package sourcecode.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import java.io.File;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import sourcecode.util.SendEmail;
import sourcecode.model.CustomerMySelf;
import sourcecode.model.DAOCategory;
import sourcecode.model.DAOCompany;
import sourcecode.model.Product;
import sourcecode.MainApp;
import sourcecode.model.Category;
import sourcecode.model.Company;
import sourcecode.model.Customer;

public class RegisterProductLayoutController implements Initializable {
   Stage currentStage;
   private MainApp mainApp;
    @FXML private ComboBox<String> cbCategory;
    @FXML private ComboBox<String> cbShipmentCompany;
    @FXML private JFXTextField tfProductName;
    @FXML private JFXTextField tfProductPrice;
    @FXML private JFXTextField tfProductImagePath;
    @FXML private JFXTextArea tfaProductInformation;
    
    
	private String strCategory;
	private String strShipmentCompany;
	private String strProductName;
	private String strProductInformation;
	private String strProductImagePath;
	private int nProductPrice;

	private ArrayList<Category<Integer, String>> categoryList;
	private ArrayList<Company<Integer, String>> companyList;

	private DAOCategory categorys;
	private DAOCompany companys;
	private Product product;
	private CustomerMySelf customerMySelf;
	private List<String> emails;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		loadComboboxCategory();
		loadComboboxShipmentCompany();

		tfProductPrice.setOnKeyPressed(event -> {
			String productPrice = "";

			String value = event.getText();
			try {
				if (Character.isDigit(value.charAt(0))) {
					return;
				} else {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning!!");
					alert.setHeaderText("숫자만 입력하세요");
					alert.showAndWait();
					productPrice = tfProductPrice.getText();
					productPrice = productPrice.substring(0, productPrice.length() - 1);

				}
			} catch (NullPointerException e) {

			} finally {
				if (productPrice.length() == 0)
					return;
				tfProductPrice.setText(productPrice);
			}

		});
	}

    @FXML
    public void onBtnClickedRegisterProductSubmit(ActionEvent event) {
       if(isValidInput()) {
          //제품등록 CallableStatement
    	   if(procRegisterProduct()) {
    		   Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    		   alert.setTitle("Product");
    		   alert.setHeaderText("물품 등록");
    		   alert.setContentText("판매 등록되었습니다.");
    		   alert.showAndWait();
    		   System.out.println("이미지 url : "+ tfProductImagePath.getText());
    	       mainApp.procGetProductInfo();
    		   currentStage.close();
           }
       }
    }
    
 private boolean procRegisterProduct() {
    	
		String runP = "{ call product_insert(?, ?, ?, ?, ?, ?) }";

		try {
			Connection conn = DBConnection.getConnection();
			Statement stmt = conn.createStatement();
			CallableStatement callableStatement = conn.prepareCall(runP.toString());
			callableStatement.setInt(1, CustomerMySelf.getInstance().getCustomer().getId());
			callableStatement.setString(2, strProductName);
			callableStatement.setString(3, strProductInformation);
			callableStatement.setInt(4, nProductPrice);
			callableStatement.setString(5, strCategory);
			callableStatement.setString(6, strShipmentCompany);
			callableStatement.executeUpdate();

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@FXML
	public void onBtnClickedRegisterProductCancel(ActionEvent event) {
		currentStage.close();
	}

	@FXML
	public void onBtnClickedAttachImage(ActionEvent event) {
		Stage imageStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		String attach = "";
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Get Image Path");

		File file = fileChooser.showOpenDialog(imageStage);

		if (file != null) {

			String nameFile = "";
			char[] fileC = (file + "").toCharArray();
			for (int i = 0; i < (file + "").length(); i++) {
				if (i == (file + "").length()) {
					nameFile += "...";
					break;
				}
				nameFile += fileC[i];
			}
			attach = file.toString();
			tfProductImagePath.setText(nameFile.toString());
		} else {
			attach = "";
		}
	}

	public boolean isValidInput() {

		String errorMessage = "";

		strProductName = tfProductName.getText();
		strCategory = cbCategory.getSelectionModel().getSelectedItem().toString();

		nProductPrice = Integer.parseInt(tfProductPrice.getText());
		strShipmentCompany = cbShipmentCompany.getSelectionModel().getSelectedItem().toString();

		strProductInformation = tfaProductInformation.getText();
		strProductImagePath = tfProductImagePath.getText();

		if (strProductName == null) {
			errorMessage += "상품명을 입력하세요!\n";
		}
		if (nProductPrice <= 0) {
			errorMessage += "가격을 설정해주세요!\n";
		}
		if (cbCategory.getValue() == null) {
			errorMessage += "카테고리를 선택해주세요!\n";
		}
		if (cbShipmentCompany.getValue() == null) {
			errorMessage += "배송 업체를 선택해주세요!\n";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please, correct the fields!");
			alert.setContentText(errorMessage);
			alert.showAndWait();
			return false;
		}
	}

	public void clearFields() {
		tfProductName.setText("");
		tfProductPrice.setText("");
		tfaProductInformation.setText("");
	}

	public void loadComboboxShipmentCompany() {
		companys = DAOCompany.getInstance();
		if (companys.getCompanySize() == 0) {
			return;
		}
		int companySize = companys.getCompanySize();

		List<String> values = new ArrayList<String>();
		for (int idx = 0; idx < companySize; idx++) {
			values.add(companys.getCompany(idx).getCompanyName());
		}

		ObservableList<String> obsValues = FXCollections.observableArrayList(values);
		cbShipmentCompany.setItems(obsValues);
	}

	public void loadComboboxCategory() {
		categorys = DAOCategory.getInstance();
		if (categorys.getCategorySize() == 0) {
			return;
		}

		int categorySize = categorys.getCategorySize();
		List<String> values = new ArrayList<String>();
		values.add("All");
		for (int idx = 0; idx < categorySize; idx++) {
			values.add(categorys.getCategory(idx).getCategoryName());
		}

		ObservableList<String> obsValues = FXCollections.observableArrayList(values);
		cbCategory.setItems(obsValues);

	}

	public void setDialogStage(Stage dialogStage) {
		this.currentStage = dialogStage;
	}
	
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}