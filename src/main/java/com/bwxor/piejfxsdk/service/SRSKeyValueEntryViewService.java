package com.bwxor.piejfxsdk.service;

import com.bwxor.piejfxsdk.controller.impl.SRSKeyValueEntryViewController;
import com.bwxor.piejfxsdk.state.ServiceState;
import com.bwxor.piejfxsdk.state.StylesheetState;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

import java.io.IOException;
import java.util.function.Consumer;

public class SRSKeyValueEntryViewService {

    public void showView(String title, String keyLabel, String valueLabel,
                         Pair<String, String> existing, Consumer<Pair<String, String>> onSave) {
        ServiceState serviceState = ServiceState.instance;
        StylesheetState stylesheetState = StylesheetState.instance;

        FXMLLoader loader = new FXMLLoader(
                serviceState.getResourceService().getResourceByName("views/srs-key-value-entry-view.fxml")
        );
        loader.setClassLoader(SRSKeyValueEntryViewController.class.getClassLoader());

        try {
            Parent root = loader.load();
            SRSKeyValueEntryViewController controller = loader.getController();
            controller.setWindowTitle(title);
            controller.setLabels(keyLabel, valueLabel);
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
                    "Error while trying to load the Key-Value Entry window.");
            e.printStackTrace();
        }
    }
}
