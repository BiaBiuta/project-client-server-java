package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.gui.ApplicationFXML;
import org.example.gui.StartPageXML;
import org.example.rpcprotocol.CompetitionServicesRpcProxy;

import java.io.IOException;
import java.util.Properties;

public class StartRpcClientFx extends Application {
    private Stage primaryStage;

    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRpcClientFx.class.getResourceAsStream("/competitionclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("competition.server.host", defaultServer);
        int serverPort = defaultChatPort;
        try {
            serverPort = Integer.parseInt(clientProps.getProperty("competition.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);
        ICompetitionServices server = new CompetitionServicesRpcProxy(serverIP, serverPort);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/pagini/application.fxml"));
        AnchorPane anchorPane = loader.load();
        primaryStage.setScene(new Scene(anchorPane));
        ApplicationFXML ctrl = loader.getController();
        ctrl.setOrganizingService(server);
        FXMLLoader startPageLoader =new FXMLLoader();
        startPageLoader.setLocation(getClass().getResource("/pagini/paginaStart.fxml"));
        AnchorPane anchorPane2=startPageLoader.load();
//        Stage stage =new Stage();
//        stage.setScene(new Scene(anchorPane2));
        StartPageXML startPageXML=startPageLoader.getController();
        startPageXML.setPageService(server,primaryStage);
        ctrl.setStartPageXMLCtrl(startPageXML);
        ctrl.setAnchorPane(anchorPane2);
        primaryStage.show();
    }
}
