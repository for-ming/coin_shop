package sourcecode.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import sourcecode.MainApp;


public class RankChartLayoutController implements Initializable {
    MainApp mainApp;
	@FXML
	private BarChart<String, Integer> barChart;
	@FXML
	private CategoryAxis xAxis;

	@FXML
	private NumberAxis yAxis;

	private ObservableList<String> monthNames = FXCollections.observableArrayList();
	// private RankPerson<String,Integer> rankPerson ;
	private ObservableList<String> rankNames = FXCollections.observableArrayList();
	private ObservableList<Integer> rankScore = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		OracleCallableStatement ocstmt = null;

		String runP = "{ call select_coinRanking(?) }";
		try {
			Connection conn = DBConnection.getConnection();
			Statement stmt = conn.createStatement();
			CallableStatement callableStatement = conn.prepareCall(runP.toString());
			callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
			callableStatement.executeUpdate();
			ocstmt = (OracleCallableStatement) callableStatement;

			ResultSet rs = ocstmt.getCursor(1);

			int count = 0;

			while (rs.next()) {
				String field1 = rs.getString(1);
				String name = rs.getString("name");
				int coin = rs.getInt("coin");
				rankNames.add(name);
				rankScore.add(coin);

				if (count == 10) {
					break;
				}
			}

			xAxis.setCategories(rankNames);

			XYChart.Series<String, Integer> series = new XYChart.Series<>();

			for (int i = 0; i < rankNames.size(); i++) {
				series.getData().add(new XYChart.Data<>(rankNames.get(i), rankScore.get(i)));

			}
			barChart.getData().add(series);

		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

}
