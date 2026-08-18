package com.bwxor.piejfxsdk.service;

import com.bwxor.piejfxsdk.controller.impl.ChangeRequestDetailsViewController;
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

public class ShowChangeRequestDetailsViewService {

    public void showView() {
        ServiceState serviceState = ServiceState.instance;
        StylesheetState stylesheetState = StylesheetState.instance;

        FXMLLoader loader = new FXMLLoader(
                serviceState.getResourceService().getResourceByName("views/change-request-details-view.fxml")
        );
        loader.setClassLoader(ChangeRequestDetailsViewController.class.getClassLoader());

        try {
            Parent root = loader.load();

            ChangeRequestDetailsViewController controller = loader.getController();
            controller.setWindowTitle("Generate Change Request (2/3)");
            controller.loadData();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(stylesheetState.getThemeURL().toExternalForm());
            scene.getStylesheets().add(stylesheetState.getDefaultStylesheetURL().toExternalForm());
            scene.setFill(Color.TRANSPARENT);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Generate Change Request (2/3)");
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            serviceState.getNotificationService().showNotificationOk(
                    "Error while trying to load the Change Request details window.");
            e.printStackTrace();
        }
    }
}
