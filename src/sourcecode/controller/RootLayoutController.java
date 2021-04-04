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
import javafx.scene.text.Text;
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
    @FXML private Text txtMyPoint;
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
   
    /*
	private DAOCategory daoCategory;
	private DAOCompany daoCompany;
	private DAOProduct daoProduct;
	*/
	private CustomerMySelf myInfo;
    static final private int segment = 3;
    static final private String[] fxmlPath = {"view/fxml/ProductLayout.fxml"
    		, "view/fxml/MyProductLayout.fxml"
    		, "view/fxml/RankChartLayout.fxml"};
    
    
    @FXML
    private void OnBtnClickedAllProduct(ActionEvent event) {
    	mainApp.procGetProductInfo();
    	allProductController.loadProduct(true);
    	paneAllProduct.toFront();
    	System.out.println("AllProduct");
    }
    
    @FXML
    private void onBtnClickedMyProduct(ActionEvent event) {
    	mainApp.procGetProductInfo();
    	myProductController.loadProduct_sel(true);
    	myProductController.loadProduct_buy(true);
    	paneMyProduct.toFront();
    	System.out.println("MyProduct");
    }
    
    @FXML
    private void onClickedRankChart(ActionEvent event) {
    	mainApp.procGetProductInfo();
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
    	
    }
    
    public void lazyInitialize() {
    	mainApp.procGetCategoryInfo();
    	mainApp.procGetCompanyInfo();
    	mainApp.procGetProductInfo();
    	
    	myInfo = CustomerMySelf.getInstance();
    	txtMyPoint.setText(Integer.toString(myInfo.getCustomer().getCoin()));
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
					myProductController.loadProduct_sel(true);
					myProductController.loadProduct_buy(true);
					
					break;
				case 2:
					paneRankChart = fxmlLoader[i].load();
					rankChartController = fxmlLoader[i].getController();
					rankChartController.setMainApp(mainApp);
					
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
   
	
    @FXML
    private void onBtnClickedCheckPoint(ActionEvent event) {
    	mainApp.procCallCustomerInfo(myInfo.getCustomer().getName());
    	txtMyPoint.setText(Integer.toString(myInfo.getCustomer().getCoin()));
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
