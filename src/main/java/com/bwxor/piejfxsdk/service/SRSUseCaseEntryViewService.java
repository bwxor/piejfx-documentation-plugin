package com.bwxor.piejfxsdk.service;

import com.bwxor.piejfxsdk.controller.impl.SRSUseCaseEntryViewController;
import com.bwxor.piejfxsdk.dto.SRSUseCase;
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

public class SRSUseCaseEntryViewService {

    public void showView(String title, SRSUseCase existing, Consumer<SRSUseCase> onSave) {
        ServiceState serviceState = ServiceState.instance;
        StylesheetState stylesheetState = StylesheetState.instance;

        FXMLLoader loader = new FXMLLoader(
                serviceState.getResourceService().getResourceByName("views/srs-use-case-entry-view.fxml")
        );
        loader.setClassLoader(SRSUseCaseEntryViewController.class.getClassLoader());

        try {
            Parent root = loader.load();
            SRSUseCaseEntryViewController controller = loader.getController();
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
                    "Error while trying to load the Use Case Entry window.");
            e.printStackTrace();
        }
    }
}
