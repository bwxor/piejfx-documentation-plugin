package com.bwxor.piejfxsdk.service;

import com.bwxor.piejfxsdk.controller.impl.SRSRevisionEntryViewController;
import com.bwxor.piejfxsdk.dto.SRSRevision;
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
import java.util.function.Consumer;

public class SRSRevisionEntryViewService {

    public void showView(String title, SRSRevision existing, Consumer<SRSRevision> onSave) {
        ServiceState serviceState = ServiceState.instance;
        StylesheetState stylesheetState = StylesheetState.instance;

        FXMLLoader loader = new FXMLLoader(
                serviceState.getResourceService().getResourceByName("views/srs-revision-entry-view.fxml")
        );
        loader.setClassLoader(SRSRevisionEntryViewController.class.getClassLoader());

        try {
            Parent root = loader.load();
            SRSRevisionEntryViewController controller = loader.getController();
            controller.setWindowTitle(title);
            controller.setOnSave(onSave);
            controller.populate(existing);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(stylesheetState.getThemeURL().toExternalForm());
            scene.getStylesheets().add(stylesheetState.getDefaultStylesheetURL().toExternalForm());
            scene.setFill(Color.TRANSPARENT);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            serviceState.getNotificationService().showNotificationOk(
                    "Error while trying to load the Revision Entry window.");
            e.printStackTrace();
        }
    }
}
