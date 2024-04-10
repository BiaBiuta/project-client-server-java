package org.example.gui;


import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.*;

import javax.swing.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class StartPageXML implements Initializable, ICompetitionObserver {
    @FXML
    public TableView<Sample> tableView;
    @FXML
    public TableColumn<Sample,String> tableColumnNume;
    @FXML
    public TableColumn<Sample,String> tableColumnVarsta;
    @FXML
    public TableColumn<Sample,Integer> tableColumnProba;
    @FXML
    public TableView<Child> tableViewChildRegistration;
    @FXML
    public TableColumn<Child,String> tableColumnNameChild;
    @FXML
    public TableColumn<Child,Integer> tableColumnAgeChild;
    @FXML
    public Button participants;
    @FXML
    public Button logout;
    @FXML
    public Button register;
    @FXML
    public TextField textFieldName;
    @FXML
    public TextField textFieldAge;
    @FXML
    public CheckBox checkBoxDesen;
    @FXML
    public CheckBox checkBoxPoezie;
    @FXML
    public CheckBox checkBoxCautareComoara;
    private ICompetitionServices service;
    private Organizing user;

    public Organizing getUser() {
        return user;
    }

    public void setUser(Organizing user) {
        this.user = user;
    }

    ObservableList<Sample> model = FXCollections.observableArrayList();
    ObservableList<Child> modelChildren = FXCollections.observableArrayList();

    private Sample sample;
    private Stage stage;
//    @FXML
//    public void initialize(){
//        setTableAllSamples();
//        //tableView.setItems(model);
//    }

    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }
    @FXML
    private void initialize() {
        tableColumnNume.setCellValueFactory(cellData -> {
            String name= cellData.getValue().getSampleCategory().toString();

            return new SimpleObjectProperty<>(name);
        });
        tableColumnVarsta.setCellValueFactory(cellData -> {
            String name= cellData.getValue().getAgeCategory().toString();

            return new SimpleObjectProperty<>(name);
        });
        tableColumnProba.setCellValueFactory(cellData -> {
            Sample sample = new Sample(cellData.getValue().getSampleCategory(),cellData.getValue().getAgeCategory());
            int id=cellData.getValue().getId();
            sample.setId(id);
            int reg =service.numberOfChildrenForSample(sample);
            System.out.println(reg);
            return new SimpleObjectProperty<>(reg);
        });
        tableViewChildRegistration.visibleProperty().set(false);
    }
    public void  initModel() throws CompetitionException {
        Iterable<Sample> samples = service.findAllSamples();
        List<Sample> sampleList = StreamSupport.stream(samples.spliterator(),false)
                .collect(Collectors.toList());
        model.setAll(sampleList);
        tableView.setItems(model);
    }
    public void handleParticipantsButton(ActionEvent ev) throws CompetitionException {
        Sample selectedSample =tableView.getSelectionModel().getSelectedItem();
        tableViewChildRegistration.visibleProperty().set(true);
        tableColumnNameChild.setCellValueFactory(cellData -> {
            String name= cellData.getValue().getName();
            return new SimpleObjectProperty<>(name);
        });
        tableColumnAgeChild.setCellValueFactory(cellData -> {
            Integer age= cellData.getValue().getAge();
            return new SimpleObjectProperty<>(age);
        });
        setSample(selectedSample);
         initModel2(selectedSample);
    }
    public void handleRegistration(ActionEvent ev) throws CompetitionException {
        Sample selectedSample = tableView.getSelectionModel().getSelectedItem();
        String nameChild = textFieldName.getText();
        String ageChild = textFieldAge.getText();

        if (ageChild.isEmpty() || nameChild.isEmpty() ) {
            JOptionPane.showMessageDialog(null, "Completați toate câmpurile.");
            return;
        }

        int age = Integer.parseInt(ageChild);
        String ageCategory;

        if (age < 6) {
            JOptionPane.showMessageDialog(null, "Varsta este prea mica.");
            return;
        } else if (age < 9) {
            ageCategory = "6-8 ani";
        } else if (age < 12) {
            ageCategory = "9-11 ani";
        } else if (age < 16) {
            ageCategory = "12-15 ani";
        } else {
            ageCategory = null;
        }

        // Verificarea stării checkbox-urilor pentru a determina sample-urile selectate
        List<Sample> selectedSamples = new ArrayList<>();

        if (checkBoxDesen.isSelected()) {
            selectedSamples.add(service.findSample(ageCategory, "Desen"));
        }
        if (checkBoxCautareComoara.isSelected()) {
            selectedSamples.add(service.findSample(ageCategory, "Cautare comoara"));
        }
        if (checkBoxPoezie.isSelected()) {
            selectedSamples.add(service.findSample(ageCategory, "Poezie"));
        }

        if (selectedSamples.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Selectați cel puțin o probă.");
            return;
        }
        for (Sample sample : selectedSamples) {
            Registration reg = service.registerChild(new Child(nameChild, age), sample);

        }
    }
    public void initModel2(Sample sample) throws CompetitionException {
        List<Child> children=service.listChildrenForSample(sample);
        modelChildren.setAll(children);
        tableViewChildRegistration.setItems(modelChildren);

    }

    public void setPageService(ICompetitionServices service, Stage stage) throws CompetitionException {
        this.service = service;
        this.stage=stage;
        //initModel();
    }
    public void handleLogout(ActionEvent ev){
        stage.close();
    }





//    @Override
//    public void update(ChangeEventRegister changeEventRegister) {
//        initModel();
//        if(tableViewChildRegistration.visibleProperty().get()) {
//            initModel2(sample);
//        }
//    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void participantsRegistered(Registration org) throws CompetitionException {

    }
    @FXML
    private void handleButtonLogout(ActionEvent actionEvent) {
        logout();
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    public void logout() {
        try {
            service.logout(user, this);
        } catch (CompetitionException e) {
            System.out.println("Logout error " + e);
        }
    }
}
