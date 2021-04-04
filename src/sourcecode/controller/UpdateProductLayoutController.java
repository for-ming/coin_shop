package sourcecode.controller;

import java.io.File;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sourcecode.MainApp;
import sourcecode.model.CustomerMySelf;
import sourcecode.model.DAOCategory;
import sourcecode.model.DAOCompany;
import sourcecode.model.DAOProduct;
import sourcecode.model.Product;

public class UpdateProductLayoutController implements Initializable{
	private Stage currentStage;
	private MainApp mainApp;
	@FXML private TextField tfProductName;
	@FXML private Text txtProductCategory;
	@FXML private ComboBox<String> cbProductCategory;
	@FXML private TextField tfProductPrice;
	@FXML private Text txtShipmentCompany;
	@FXML private ComboBox<String> cbShipmentCompany;
	@FXML private TextArea taProductInformation;
	@FXML private ImageView imgProductImage;
	@FXML private Text txtImagePath;
	
	private String strProductName;
	private String strCategoryName;
	private int nProductPrice;
	private String strShipmentName;
	private String strProductInformation;
	private String strImagePath;

	Product product;
	
	private DAOProduct daoProduct;
	private DAOCategory categorys;
	private DAOCompany companys;
	
	private CustomerMySelf customerMyself = CustomerMySelf.getInstance();
	
	//private Product product;
	/*
	 * public BuyProductLayoutController() {
	 * 
	 * }
	 * 
	 * public BuyProductLayoutController(Product product) {
	 * 
	 * }
	 */
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		//get product info
		
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
		
		
		cbProductCategory.setOnAction(event -> {
			txtProductCategory.setText(cbProductCategory.getSelectionModel().getSelectedItem().toString());
		});
		
		cbShipmentCompany.setOnAction(event -> {
			txtShipmentCompany.setText(cbShipmentCompany.getSelectionModel().getSelectedItem().toString());
		});
	
		
	}
	
	@FXML
	private void onBtnClickedOpenImage(ActionEvent event) {
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
			txtImagePath.setText(nameFile.toString());
		} else {
			attach = "";
		}
	}
	
	@FXML 
	private void onBtnClickedUpdateProduct(ActionEvent event) {
		int productID_pk = product.getProductId();
		
		strProductName = tfProductName.getText();
		strCategoryName = txtProductCategory.getText();
		nProductPrice = Integer.parseInt(tfProductPrice.getText());
		strShipmentName = txtShipmentCompany.getText();
		strProductInformation = taProductInformation.getText();
		strImagePath = txtImagePath.getText();
	
		if(strProductName == null || strCategoryName == null 
				|| tfProductPrice.getText() == null || strShipmentName == null) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("값을 기입하세요!");
			alert.showAndWait();
		} else {
			//callable statement update
			if(procProductUpdate()) {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("상품 정보 수정");
				alert.setHeaderText("수정되었습니다.");
				alert.showAndWait();
			}
			currentStage.close();
			mainApp.procGetProductInfo();
		}

	}
	
	@FXML 
	private void onBtnClickedDeleteProduct(ActionEvent event) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		if(product.getStatus() != "READY") {
			alert.setTitle("WARNING!!");
			alert.setHeaderText("거래 대기 상태가 아닙니다.");
			alert.showAndWait();
			return;
		}
		int productID_pk = product.getProductId();
		//callable statement delete
		if(procProductCancel()) {
			alert.setTitle("취소 확인");
			alert.setHeaderText("취소되었습니다.");
			alert.showAndWait();
		}
		mainApp.procGetProductInfo();
	}
	
	private boolean procProductUpdate() {

		
		String runP = "{ call mysell_product_update(?, ?, ?, ?, ?, ?, ?) }";
		System.out.println(strProductName+ strCategoryName+ strShipmentName+"수정");
		try {
			Connection conn = DBConnection.getConnection();
			CallableStatement callableStatement = conn.prepareCall(runP.toString());
			callableStatement.setInt(1, product.getProductId());
			callableStatement.setInt(2, customerMyself.getCustomer().getId()); //판매자 이름(string -> int)
			callableStatement.setString(3, strProductName);
			callableStatement.setString(4, strProductInformation);
			callableStatement.setInt(5, nProductPrice);
			callableStatement.setString(6, strCategoryName);
			callableStatement.setString(7, strShipmentName);
			callableStatement.executeUpdate();

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("- - Update - -");
		return true;
	}
	
	
	@FXML
	private void onBtnClickedUpdateProductCancel(ActionEvent event) {
		currentStage.close();
		mainApp.procGetProductInfo();
	}
	
	private boolean procProductCancel() {
		
		String runP = "{ call mysell_product_cancel(?) }";
		System.out.println(product.getProductName()+" 취소 ");
		try {
			Connection conn = DBConnection.getConnection();
			CallableStatement callableStatement = conn.prepareCall(runP.toString());
			callableStatement.setInt(1, product.getProductId());
			callableStatement.executeUpdate();

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("- - cancel - -");
		return true;
	}
	
	public void setData(Product selectedProduct) {
		product = selectedProduct;
		if(product == null) {
			currentStage.close();
		}
		
		tfProductName.setText(selectedProduct.getProductName());
		txtProductCategory.setText(selectedProduct.getCategoryName());
		tfProductPrice.setText(Integer.toString(selectedProduct.getPrice()));
		txtShipmentCompany.setText(selectedProduct.getShipmentCompanyName());
		taProductInformation.setText(selectedProduct.getInfo());
		txtImagePath.setText(selectedProduct.getImagePath());
		
		try {
			Image productImage = new Image(selectedProduct.getImagePath());
			imgProductImage.setImage(productImage);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setDialogStage(Stage dialogStage) {
		this.currentStage = dialogStage;
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
	
    public void loadComboboxCategory(){
    	categorys = DAOCategory.getInstance();
    	if(categorys.getCategorySize() == 0) {
    		return;
    	}
    	
    	int categorySize = categorys.getCategorySize();
        List<String> values = new ArrayList<String>();
        values.add("All");
        for(int idx=0; idx<categorySize; idx++) {
        	values.add(categorys.getCategory(idx).getCategoryName());
        }
        
        ObservableList<String> obsValues = FXCollections.observableArrayList(values);
        cbProductCategory.setItems(obsValues);
        
        
    }
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
