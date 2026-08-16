package com.bwxor.piejfxsdk.service;

import com.bwxor.piejfxsdk.controller.impl.SRSFunctionalRequirementsViewController;
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

public class ShowSRSFunctionalRequirementsViewService {

    public void showView() {
        ServiceState serviceState = ServiceState.instance;
        StylesheetState stylesheetState = StylesheetState.instance;

        FXMLLoader loader = new FXMLLoader(
                serviceState.getResourceService().getResourceByName("views/srs-functional-requirements-view.fxml")
        );
        loader.setClassLoader(SRSFunctionalRequirementsViewController.class.getClassLoader());
        Parent root;

        try {
            root = loader.load();

            SRSFunctionalRequirementsViewController controller = loader.getController();
            controller.loadData();
            controller.setWindowTitle("Generate SRS Document (5/10)");

            Scene scene = new Scene(root);
            scene.getStylesheets().add(stylesheetState.getThemeURL().toExternalForm());
            scene.getStylesheets().add(stylesheetState.getDefaultStylesheetURL().toExternalForm());
            scene.setFill(Color.TRANSPARENT);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Generate SRS Document (5/10)");
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            serviceState.getNotificationService().showNotificationOk(
                    "Error while trying to load the Functional Requirements window.");
            e.printStackTrace();
        }
    }
}
