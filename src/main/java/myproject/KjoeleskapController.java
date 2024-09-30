package myproject;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import myproject.Kjoeleskap.CSVKjoeleskap;

public class KjoeleskapController{

    private KjoeleskapBehandler behandler;
    
    @FXML
    private TextField matvareTextField;

    @FXML   
    private ChoiceBox<String> kategoriChoiceBox;

    @FXML
    private Spinner<Integer> antallSpinner;

    @FXML
    private TableView<Matvare> matvareTable; 

    @FXML
    private TextArea statisticsTextArea;

    @FXML
    public void initialize() {
        this.behandler = new KjoeleskapBehandler();

        this.matvareTextField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.matches("[a-zA-ZæøåÆØÅ\\-]*")) {
                return change;
            }

            else {
                return null;
            }
        }));

        this.kategoriChoiceBox.getItems().addAll(Matvare.lovligeKategorier);

        this.antallSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50));

        ObservableList<TableColumn<Matvare,?>> columns = this.matvareTable.getColumns();

        TableColumn<Matvare, String> matvareNavnColumn = (TableColumn<Matvare, String>) columns.get(0);
        matvareNavnColumn.setCellValueFactory(new PropertyValueFactory<Matvare, String>("matvareNavn"));

        TableColumn<Matvare, String> kategoriColumn = (TableColumn<Matvare, String>) columns.get(1);
        kategoriColumn.setCellValueFactory(new PropertyValueFactory<Matvare, String>("kategori"));

        TableColumn<Matvare, Integer> antallColumn = (TableColumn<Matvare, Integer>) columns.get(2);
        antallColumn.setCellValueFactory(new PropertyValueFactory<Matvare, Integer>("antall"));

        this.updateKjoeleskap();
    }

    private void updateKjoeleskap() {
        this.matvareTable.getItems().setAll(this.behandler.getMatvarer());

        StringBuilder builder = new StringBuilder("Matvarer i kjøleskapet: \n\n")
            .append("Antall matvarer: ").append(this.behandler.getAntallMatvarer()).append("\n");

        for (String kategori : Matvare.lovligeKategorier) {
            builder.append(kategori).append(": ").append(this.behandler.getAntallIKategori(kategori)).append("\n");
        }

        this.statisticsTextArea.setText(builder.toString());
    }

    @FXML
    public void handleAddMatvare() {
        try {
        String matvareNavn = this.matvareTextField.getText();
        String kategori = this.kategoriChoiceBox.getSelectionModel().getSelectedItem();
        int antall = this.antallSpinner.getValue();

        Matvare matvare = new Matvare(matvareNavn, kategori, antall);
        this.behandler.leggTilMatvare(matvare);
        System.out.println("Lagt til ny matvare");

        this.updateKjoeleskap();
        }

        catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("OBS!");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void handleRemoveMatvare() {
        Matvare selectedMatvare = this.matvareTable.getSelectionModel().getSelectedItem();

        if (selectedMatvare == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("OBS!");
            alert.setContentText("Trykk på en matvare for å kunne fjerne den");
            alert.showAndWait();

            return;
        }

        if (selectedMatvare.getAntall() > 1) {
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Velg antall");
            dialog.setContentText("Velg antall av denne matvaren du ønsker å fjerne");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(antall -> {
                try {
                    int antallToRemove = Integer.parseInt(antall);
                    boolean removed = this.behandler.fjerneMatvare(selectedMatvare, antallToRemove);
                    if (removed) {
                        this.updateKjoeleskap();
                    }

                    else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("OBS!");
                        alert.setContentText("Ugyldig antall");
                        alert.showAndWait();
                    }
                }

                catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("OBS!");
                    alert.setContentText("Ugyldig antall");
                    alert.showAndWait();
                }
            });
        }

        else {
            boolean removed = this.behandler.fjerneMatvare(selectedMatvare, 1);

            if (removed) {
                System.out.println("Matvaren: " + selectedMatvare + " er nå fjernet");
                this.updateKjoeleskap();
            }
    
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("OBS!");
                alert.setContentText("Kunne ikke fjerne matvaren fra listen");
                alert.showAndWait();
            }
        }

    }

    @FXML
    public void handleLagreKjoeleskap() { 
        if (this.behandler.getAntallMatvarer() <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("OBS!");
            alert.setContentText("Du må legge til minst 1 matvare først");
            alert.showAndWait();
            return;
        }

        try { 
            this.behandler.lagreKjoeleskap();
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Lagret!");
            alert.setContentText("Matvaren(e) ble lagret og lagt til kjøleskapet");
            alert.showAndWait();
        } 
        
        catch (IOException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("OBS!");
            alert.setContentText("Det skjedde en feil ved lagring til kjøleskapet");
            alert.showAndWait();
        }
    }

    public void handleOpenKjoeleskap() {
        try {
            this.behandler.openKjoeleskap();
            
            if (this.behandler.getAntallMatvarer() == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Tomt kjøleskap");
                alert.setContentText("Kjøleskapet er tomt. Du må på handletur");
                alert.showAndWait();
            }

            else {
                this.updateKjoeleskap();
            }
        }

        catch (IOException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("OBS!");
            alert.setContentText("Det skjedde en feil ved lasting fra kjøleskapet");
            alert.showAndWait();
        }
        
    }

}
