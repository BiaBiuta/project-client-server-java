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
    AnchorPane anchorPane;

    public void setAnchorPane(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
    }

    private ICompetitionServices service;
    private StartPageXML startPageXMLCtrl;

    public void setOrganizingService(ICompetitionServices service){
        this.service=service;
    }

    public void handleLogin(ActionEvent ev) throws IOException, CompetitionException {



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
        login(org,actionEvent);
        if(org==null){
            ControllerGuiAlert.showErrorMessage(null,"Nu exista organizatorul");
            return;
        }
//        FXMLLoader startPageLoader =new FXMLLoader();
//        startPageLoader.setLocation(getClass().getResource("/pagini/paginaStart.fxml"));
//        AnchorPane anchorPane=startPageLoader.load();
        //service.login(org,startPageXMLCtrl);
        Stage stage =new Stage();
        stage.setScene(new Scene(anchorPane));
        //StartPageXML startPageXML=startPageLoader.getController();
        //startPageXML.setPageService(service,stage);
//        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent event) {
//                startPageXMLCtrl.handleLogout();
//                System.exit(0);
//            }
//        });
        startPageXMLCtrl.setUser(org);
        stage.show();
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    private void login(Organizing org, javafx.event.ActionEvent actionEvent) throws CompetitionException {
        Stage stage = new Stage();
        stage.setTitle("User account : " + org.getName());
        stage.setScene(new Scene(anchorPane));

        stage.setOnCloseRequest(event -> {
            startPageXMLCtrl.logout();
            System.exit(0);
        });

        stage.show();
        startPageXMLCtrl.setUser(org);
        startPageXMLCtrl.initModel();

        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }
}
