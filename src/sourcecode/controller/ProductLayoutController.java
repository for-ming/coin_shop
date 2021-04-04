package sourcecode.controller;

import java.net.URL;

import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import sourcecode.controller.*;
import sourcecode.MainApp;

import sourcecode.model.CustomerMySelf;
import sourcecode.model.CustomerTheOther;
import sourcecode.model.Customer;
import sourcecode.model.Product;
import sourcecode.model.DAOCategory;
import sourcecode.model.Category;
import sourcecode.model.DAOCompany;
import sourcecode.model.Company;
import sourcecode.model.DAOProduct;
public class ProductLayoutController implements Initializable {

    @FXML private JFXComboBox<String> cbCategoryList;

    @FXML private TextField tfSearch;
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> columnImage;
    @FXML private TableColumn<Product, String> columnProductName;
    @FXML private TableColumn<Product, String> columnPrice;
    @FXML private TableColumn<Product, String> columnSellerName;
    @FXML private TableColumn<Product, String> columnCategory; 
    @FXML private TableColumn<Product, String> columnProductStatus; 
    @FXML private Label lblNote;  
    @FXML private Label lblError;
    
    private List<Product> productList = new ArrayList();
    private ObservableList<Product> observablelistProduct;
    
    private DAOCategory categoryList;
    
    MainApp mainApp;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadCombobox();
        definingColumn();
        //loadAutoComplete();
		/*
		 * try{ productTable.getSelectionModel().selectedItemProperty().addListener(
		 * (observable, oldValue, newValue) -> showNote(newValue));
		 * 
		 * //loadAutoComplete();
		 * 
		 * }catch(NullPointerException npe){ lblNote.setText(""); }
		 */
    }    
    
    @FXML
    void actionRegisterProduct(ActionEvent event) {
    	mainApp.showRegisterProductDialog();
    	loadProduct(true);
    }

    @FXML
    void actionSearch(ActionEvent event) {
        try{
            if (cbCategoryList.getValue().equals("All")){
            	loadProduct(true);
            }else{
                List<Product> product = new ArrayList();
                product = DAOProduct.getInstance().findByCategory(tfSearch.getText());
               
                loadProduct(product);
            }
        }catch(NumberFormatException ime){
            lblError.setText("Enter the valid value type");
        }catch(NullPointerException npe){
            lblError.setText("Enter some value");
        }
    }
    
    @FXML
    void keyPressed(KeyEvent event) {
        lblError.setText("");
            
    }
    
    @FXML
    void actionCombobox(ActionEvent event) {
    	try{
            if (cbCategoryList.getValue().equals("All")){
            	loadProduct(true);
            }else{
                
                List<Product> product = new ArrayList();
                String strCategoryName = cbCategoryList.getValue();
                product = DAOProduct.getInstance().findByCategory(strCategoryName);
               
                loadProduct(product);
            }
        }catch(NumberFormatException ime){
            lblError.setText("Enter the valid value type");
        }catch(NullPointerException npe){
            lblError.setText("Enter some value");
        }
    }
    
    
    public void refresh() {
    	loadCombobox();
     	loadProduct(false);
    }
    @FXML
    private void onClickedTable(MouseEvent event) {
    	Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
    	if(selectedProduct == null) {
    		Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning !!");
            alert.setHeaderText("등록된 제품이 없습니다 !");
            alert.showAndWait();
    		return;
    	}

    	mainApp.showBuyProductDialog(selectedProduct);
    	
    	
    }
    
    
    public boolean loadProduct(boolean cleanTable){
        
        try {
            if(cleanTable) {
                cleanTable();
            }
            
            
            definingColumn();
            setListProduct(DAOProduct.getInstance().getProduct());
            observablelistProduct = FXCollections.observableArrayList(productList);
            productTable.setItems(observablelistProduct);
            
        }catch(Exception e) {
        	e.printStackTrace();
        	alert("Error", null, "An error occurred while retrieving data", Alert.AlertType.ERROR);
            return false;
        }
        
        return true;
    }
    
    
    public void loadProduct(List<Product> arrayListProduct) {
         try {
            cleanTable();
            
            observablelistProduct = FXCollections.observableList(arrayListProduct);
            productTable.setItems(observablelistProduct);
        }catch(Exception e) {
            alert("Error", null, "An error occurred while retrieving data", Alert.AlertType.ERROR);
        }
    }
    
    public void definingColumn() {
		
		  columnImage.setCellValueFactory(new PropertyValueFactory<Product, String>("imagePath"));
		  columnProductName.setCellValueFactory(new PropertyValueFactory<Product, String>("productName")); 
		  columnPrice.setCellValueFactory(new PropertyValueFactory<Product, String>("price")); 
		  columnSellerName.setCellValueFactory(new PropertyValueFactory<Product, String>("sellerId")); 
		  columnCategory.setCellValueFactory(new PropertyValueFactory<Product, String>("categoryName"));
		  columnProductStatus.setCellValueFactory(new PropertyValueFactory<Product, String>("status"));
		 
		}

    private void cleanTable() {
        productTable.getItems().clear();
    }
    
    private void alert(String titulo, String headerText, String contentText, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(titulo);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    
    public void loadCombobox() {
    	categoryList = DAOCategory.getInstance();
        List<String> values = new ArrayList<String>();
        int categorySize = categoryList.getCategorySize();
        
        values.add("All");
        for(int idx=0; idx<categorySize; idx++) {
        	values.add(categoryList.getCategory(idx).getCategoryName());
        }
        
        ObservableList<String> obsValues = FXCollections.observableList(values);
        cbCategoryList.setItems(obsValues);
    }
    

    public void loadAutoComplete() {
        
        // Variables for autosuggestion :)

    	AutoCompletionBinding<String> acb;
        Set<String> ps;
        
        ArrayList<String> values = new ArrayList();
		for (int i = 0; i < productList.size(); i++){ 
			values.add(productList.get(i).getImagePath());
			values.add(productList.get(i).getProductName());
			values.add(Integer.toString(productList.get(i).getPrice()));
			values.add(productList.get(i).getSellerId());
			values.add(productList.get(i).getCategoryName());
			values.add(productList.get(i).getStatus());
			}
		
		String[] _possibleSuggestions = values.toArray(new String[0]); 
		ps = new HashSet<>(Arrays.asList(_possibleSuggestions));
		TextFields.bindAutoCompletion(tfSearch, _possibleSuggestions);
		 
    }
    
	/*
	 * public void showNote(Customer person) { lblNote.setText(person.getNote()); }
	 */
    public List<Product> getlistProduct() {
        return productList;
    }

    public void setListProduct(List<Product> productList) {
        this.productList = productList;
    }
    
    
    public MainApp getMainApp() {
        return mainApp;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
   
}