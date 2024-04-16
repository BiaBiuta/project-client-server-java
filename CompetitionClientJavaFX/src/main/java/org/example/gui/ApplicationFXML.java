package org.example.gui;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.CompetitionException;
import org.example.ICompetitionServices;
import org.example.Organizing;
import org.example.controllerGuiAlert.ControllerGuiAlert;
import org.example.dto.DTOUtils;
import org.example.dto.OrganizingDTO;


import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ApplicationFXML implements Initializable {
    @FXML
    Button buttonLogin;
    //private StartPageXML startPageXML;
    @FXML
    TextField textFieldUsername;
    @FXML
     TextField textFieldPassword;
    @FXML
    Label labelUsername;
    @FXML
     Label labelPassword;
    @FXML
    AnchorPane anchorPane2;

    public void setAnchorPane(AnchorPane anchorPane) {
        this.anchorPane2 = anchorPane;
    }

    private ICompetitionServices service;
    private StartPageXML startPageXMLCtrl;

    public void setOrganizingService(ICompetitionServices service){
        this.service=service;
    }


    public void setConcursService(ICompetitionServices concursService) {
    }

    public void setStartPageXMLCtrl(StartPageXML startPageXMLCtrl) {
        this.startPageXMLCtrl = startPageXMLCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }



    public void handleLogin(javafx.event.ActionEvent actionEvent) throws IOException, CompetitionException {
        if(textFieldUsername.getText().equals("")|| textFieldPassword.equals("")){
            ControllerGuiAlert.showErrorMessage(null,"Nu ati introdus datele");
            return;
        }
        String username=textFieldUsername.getText();
        String password=textFieldPassword.getText();
        OrganizingDTO orgDTO=new OrganizingDTO(username,password);
        Organizing org= DTOUtils.getFromDTO(orgDTO);
        if(org==null){
            ControllerGuiAlert.showErrorMessage(null,"Nu exista organizatorul");
            return;
        }
        org=service.login(org,startPageXMLCtrl);
        Stage stage=new Stage();
        stage.setTitle("Chat Window for " +org.getUsername());
        stage.setScene(new Scene(anchorPane2));
        startPageXMLCtrl.setUser(org);
        stage.show();
        //((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }


}
