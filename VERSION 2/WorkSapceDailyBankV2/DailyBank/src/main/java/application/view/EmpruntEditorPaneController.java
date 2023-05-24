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

public class EmpruntEditorPaneController {



    @FXML
    private TableView<Emprunt> animals;

    @FXML
    private TableColumn<Emprunt, Integer> idCol;

    @FXML
    private TableColumn<Emprunt, String> typeCol;

    @FXML
    private TableColumn<Emprunt, String> nameCol;


    @FXML
    private TextField inputId;

    @FXML
    private TextField inputType;

    @FXML
    private TextField inputName;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idCol.setCellValueFactory(new PropertyValueFactory<Animal, Integer>("id"));
        typeCol.setCellValueFactory(new PropertyValueFactory<Animal, String>("type"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Animal, String>("name"));
        setupTable();
    }

    @FXML
    void submit(ActionEvent event) {
        ObservableList<Emprunt> currentTableData = animals.getItems();
        int currentAnimalId = Integer.parseInt(inputId.getText());

        for (Emprunt animal : currentTableData) {
            if(animal.getId() == currentAnimalId) {
                animal.setTaux(inputType.get);
                animal.setCapital(inputName);

                animals.setItems(currentTableData);
                animals.refresh();
                break;
            }
        }
    }

    @FXML
    void rowClicked(MouseEvent event) {
    	Emprunt clickedAnimal = animals.getSelectionModel().getSelectedItem();
        inputId.setText(String.valueOf(clickedAnimal.getId()));
        inputType.setText(String.valueOf(clickedAnimal.getType()));
        inputName.setText(String.valueOf(clickedAnimal.getName()));
    }

    private void setupTable(){
    	Emprunt animal0 = new Emprunt(0,2,10000,32,'23/05/2022', 454);
 
        animals.getItems().addAll(animal0);
    }
}
}
