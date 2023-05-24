package application.view;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.data.Emprunt;

import java.net.URL;
import java.util.ResourceBundle;

public class EmpruntDetailController {



    @FXML
    private TableView<Emprunt> emprunt;

    @FXML
    private TableColumn<Emprunt, Integer> idCol;

    @FXML
    private TableColumn<Emprunt, Integer> typeCol;

    @FXML
    private TableColumn<Emprunt, Integer> nameCol;


    @FXML
    private TextField inputId;

    @FXML
    private TextField inputType;

    @FXML
    private TextField inputName;


    public void initialize(URL url, ResourceBundle resourceBundle) {
        idCol.setCellValueFactory(new PropertyValueFactory<Emprunt, Integer>("id"));
        typeCol.setCellValueFactory(new PropertyValueFactory<Emprunt, Integer>("Taux"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Emprunt, Integer>("Capital"));
        setupTable();
    }

    @FXML
    void submit(ActionEvent event) {
        ObservableList<Emprunt> currentTableData = emprunt.getItems();
        emprunt.setItems(currentTableData);
        
            }
        
    

    private void setupTable(){
    	Emprunt emprunt0 = new Emprunt(0,2,10000,32, null, 454);
 
    	emprunt.getItems().addAll(emprunt0);
    }
}

