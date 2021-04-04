package sourcecode.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import sourcecode.MainApp;
import sourcecode.model.*;
import sourcecode.controller.*;
public class RootLayoutController implements Initializable {
    
    MainApp mainApp;
    Stage currentStage;
   
    @FXML private JFXButton btnAllProduct;
    @FXML private JFXButton btnMyProduct;
    @FXML private JFXButton btnRank;
    
    @FXML private Pane paneSegment;
    private AnchorPane paneAllProduct;
    private AnchorPane paneMyProduct;
    private AnchorPane paneRankChart;
    
    private Stage[] stageData;
    private AnchorPane layoutData[];
    private Scene[] sceneData;
    private FXMLLoader[] fxmlLoader;
    
    private ProductLayoutController allProductController;
    private MyProductLayoutController myProductController;
    private RankChartLayoutController rankChartController;
   
	private DAOCategory daoCategory;
	private DAOCompany daoCompany;
	private DAOProduct daoProduct;
	
    static final private int segment = 3;
    static final private String[] fxmlPath = {"view/fxml/ProductLayout.fxml"
    		, "view/fxml/MyProductLayout.fxml"
    		, "view/fxml/RankChartLayout.fxml"};
    
    
    @FXML
    private void OnBtnClickedAllProduct(ActionEvent event) {
    	allProductController.loadProduct(true);
    	paneAllProduct.toFront();
    	System.out.println("AllProduct");
    }
    
    @FXML
    private void onBtnClickedMyProduct(ActionEvent event) {
    	paneMyProduct.toFront();
    	System.out.println("MyProduct");
    }
    
    @FXML
    private void onClickedRankChart(ActionEvent event) {
    	paneRankChart.toFront();
    	System.out.println("RankChart");
    }
 
    
    @FXML
    void closeApplication(MouseEvent event) {
        try{
            //DAOCustomer.getInstance().closeEntityManager();
        }catch(Exception e){
            System.out.println("Error when closing the application");
        }finally {
        	System.exit(0);
        }
    }
    
    
    /*
     * modeless dialog
     * create allproduct
     * create myproduct
     * create rankchart
	*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {  	
    	procGetCategoryInfo();
		procGetCompanyInfo();
		procGetProductInfo();
    }    
    public void createSegment() {
    	fxmlLoader = new FXMLLoader[segment];
    	
    	/*
    	stageData = new Stage[segment];
    	for(int i=0; i<stageData.length; i++) {
    		stageData[i] = new Stage(StageStyle.UNDECORATED);
    		stageData[i].initOwner(currentStage);
    	}
    	*/
    	//layoutData = new AnchorPane[segment];
    	
    	for(int i=0; i<segment; i++) {
    		try {
    			String strFXMLPath = fxmlPath[i];
    			fxmlLoader[i] = new FXMLLoader();
    			
    			fxmlLoader[i].setLocation(MainApp.class.getResource(strFXMLPath));
				//layoutData[i] = fxmlLoader[i].load();
				//rootLayout.setCenter(layoutData[i])
				switch(i) {
				case 0:
					paneAllProduct = fxmlLoader[i].load();
					allProductController = fxmlLoader[i].getController();
					allProductController.setMainApp(mainApp);
					allProductController.loadProduct(true);
					break;
				case 1:
					paneMyProduct = fxmlLoader[i].load();
					myProductController = fxmlLoader[i].getController();
					myProductController.setMainApp(mainApp);
					myProductController.loadMyInfo();
					break;
				case 2:
					paneRankChart = fxmlLoader[i].load();
					rankChartController = fxmlLoader[i].getController();
					break;
				default :
					break;
				}
    		} catch (IOException e) {
				e.printStackTrace();
			}  
    	}
    	
    	paneSegment.getChildren().add(paneAllProduct);
    	paneSegment.getChildren().add(paneMyProduct);
    	paneSegment.getChildren().add(paneRankChart);
    	//paneSegment.toBack();
    	
    }
    private boolean procGetCategoryInfo() {
		   OracleCallableStatement ocstmt = null;
		   
		   daoCategory = DAOCategory.getInstance();
		   String runP = "{ call get_category_info(?)}";
		   
		   try {
			   Connection conn = DBConnection.getConnection();
			   Statement stmt = conn.createStatement();
			   CallableStatement callableStatement = conn.prepareCall(runP.toString());
			   callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
			   callableStatement.executeUpdate();	
			   ocstmt = (OracleCallableStatement)callableStatement;

			   ResultSet rs =  ocstmt.getCursor(1);
			   while (rs.next()) {
				   Category<Integer, String> categorys = new Category<>();
				   categorys.setCategory(rs.getInt("id"), rs.getString("name"));
				   System.out.println(categorys.getCategoryID()+" "+categorys.getCategoryName());
				   daoCategory.addCategory(categorys);
			   }
			   
		   } catch(Exception e) {
			   e.printStackTrace();
			   return false;
		   }
		   return true;
	   }
	   
	   private boolean procGetCompanyInfo() {
		   OracleCallableStatement ocstmt = null;
		   
		   daoCompany = DAOCompany.getInstance();
		   String runP = "{ call get_company_info(?)}";
		   
		   try {
			   Connection conn = DBConnection.getConnection();
			   Statement stmt = conn.createStatement();
			   CallableStatement callableStatement = conn.prepareCall(runP.toString());
			   callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
			   callableStatement.executeUpdate();	
			   ocstmt = (OracleCallableStatement)callableStatement;

			   ResultSet rs =  ocstmt.getCursor(1);
			   while (rs.next()) {
				   Company<Integer, String> companys = new Company<>();
				   companys.setCompany(rs.getInt("id"), rs.getString("name"));
				   System.out.println(companys.getCompanyID()+" "+companys.getCompanyName());
				   daoCompany.addCompany(companys);
			   }
			   
		   } catch(Exception e) {
			   e.printStackTrace();
			   return false;
		   }
		   return true;
	   }
	   
	private boolean procGetProductInfo() {
		 OracleCallableStatement ocstmt = null;
		   
		   daoProduct = DAOProduct.getInstance();
		   String runP = "{ call ALL_product(?)}";
		   
		   try {
			   Connection conn = DBConnection.getConnection();
			   Statement stmt = conn.createStatement();
			   CallableStatement callableStatement = conn.prepareCall(runP.toString());
			   callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
			   callableStatement.executeUpdate();	
			   ocstmt = (OracleCallableStatement)callableStatement;

			   ResultSet rs =  ocstmt.getCursor(1);
			   while (rs.next()) {
				   Product products = new Product();
				   products.setProductId(rs.getInt("id"));
				   products.setProductName(rs.getString("name"));
				   products.setInfo(rs.getString("information"));
				   products.setPrice(rs.getInt("price"));
				   products.setSellerId(rs.getString(5));
				   products.setCategoryName(rs.getString("category_name"));
				   products.setStatus(rs.getString("product_status"));
				   products.setShipmentCompanyName(rs.getString(8));
				   daoProduct.addProduct(products);
			   }
			   
		   } catch(Exception e) {
			   e.printStackTrace();
			   return false;
		   }
		   System.out.println("상품 정보 불러오기 완료!");
		   return true;
	}
	
    @FXML
    private void onBtnClickedCheckPoint(ActionEvent event) {
    	System.out.println("내마일리지 확인");
    }
    
    public MainApp getMainApp() {
        return mainApp;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    public void setDialogStage(Stage dialog) {
    	this.currentStage = dialog;
    }
}
