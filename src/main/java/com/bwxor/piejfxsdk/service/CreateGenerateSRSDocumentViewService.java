package com.bwxor.piejfxsdk.service;

import com.bwxor.piejfxsdk.controller.impl.GenerateSRSDocumentViewController;
import com.bwxor.piejfxsdk.state.ServiceState;
import com.bwxor.piejfxsdk.state.StylesheetState;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;

public class CreateGenerateSRSDocumentViewService {

    public void showGenerateSRSDocumentView() {
        ServiceState serviceState = ServiceState.instance;
        StylesheetState stylesheetState = StylesheetState.instance;

        FXMLLoader loader = new FXMLLoader(
                serviceState.getResourceService().getResourceByName("views/generate-srs-document-view.fxml")
        );
        loader.setClassLoader(GenerateSRSDocumentViewController.class.getClassLoader());
        Parent root;

        try {
            root = loader.load();

            GenerateSRSDocumentViewController controller = loader.getController();

            controller.loadProjectMetadata();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(stylesheetState.getThemeURL().toExternalForm());
            scene.getStylesheets().add(stylesheetState.getDefaultStylesheetURL().toExternalForm());
            scene.setFill(Color.TRANSPARENT);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Generate SRS Document");
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            controller.setWindowTitle("Generate SRS Document");
            stage.showAndWait();
        } catch (IOException e) {
            serviceState.getNotificationService().showNotificationOk(
                    "Error while trying to load the Generate SRS Document window.");
            e.printStackTrace();
        }
    }
}
