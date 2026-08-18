package com.bwxor.piejfxsdk.service;

import com.bwxor.piejfxsdk.controller.impl.OutputFolderViewController;
import com.bwxor.piejfxsdk.state.ServiceState;
import com.bwxor.piejfxsdk.state.StylesheetState;
import com.bwxor.piejfxsdk.type.WatermarkPosition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;

/**
 * Generic service that opens the shared output-folder / watermark view.
 * Callers supply a title, a back-action, a generate-function, and an optional
 * watermark consumer so each document type can store its own watermark settings.
 */
public class ShowOutputFolderViewService {

    public void showView(String title,
                         Runnable backAction,
                         Function<String, Boolean> generateAction,
                         BiConsumer<File, WatermarkPosition> watermarkConsumer,
                         DoubleConsumer scaleConsumer) {
        ServiceState serviceState = ServiceState.instance;
        StylesheetState stylesheetState = StylesheetState.instance;

        FXMLLoader loader = new FXMLLoader(
                serviceState.getResourceService().getResourceByName("views/output-folder-view.fxml")
        );
        loader.setClassLoader(OutputFolderViewController.class.getClassLoader());

        try {
            Parent root = loader.load();

            OutputFolderViewController controller = loader.getController();
            controller.configure(title, backAction, generateAction, watermarkConsumer, scaleConsumer);

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
            stage.show();
        } catch (IOException e) {
            serviceState.getNotificationService().showNotificationOk(
                    "Error while trying to load the Output Folder window.");
            e.printStackTrace();
        }
    }
}
