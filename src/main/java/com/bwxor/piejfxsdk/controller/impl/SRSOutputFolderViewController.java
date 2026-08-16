package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import com.bwxor.piejfxsdk.state.ServiceState;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class SRSOutputFolderViewController extends MovableViewController {

    @FXML private Label windowTitle;
    @FXML private TextField outputFolderField;
    @FXML private Button buttonCancel;

    public void setWindowTitle(String title) {
        this.windowTitle.setText(title);
    }

    @FXML
    public void onBrowseButtonClick() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Output Folder");
        Stage stage = (Stage) buttonCancel.getScene().getWindow();
        File selected = chooser.showDialog(stage);
        if (selected != null) {
            outputFolderField.setText(selected.getAbsolutePath());
        }
    }

    @FXML
    public void onBackButtonClick() {
        ((Stage) buttonCancel.getScene().getWindow()).close();
        ServiceState.instance.getShowSRSAppendicesViewService().showView();
    }

    @FXML
    public void onGenerateButtonClick() {
        ServiceState serviceState = ServiceState.instance;

        if (outputFolderField.getText().trim().isEmpty()) {
            serviceState.getNotificationService().showNotificationOk("Please select an output folder.");
            return;
        }

        boolean success = serviceState.getGenerateSRSPDFService().generatePdf(outputFolderField.getText().trim());
        if (success) {
            serviceState.getNotificationService().showNotificationOk("Successfully generated.");
            ((Stage) buttonCancel.getScene().getWindow()).close();
        } else {
            serviceState.getNotificationService().showNotificationOk("An error occurred while generating the PDF.");
            ((Stage) buttonCancel.getScene().getWindow()).close();
        }
    }

    @FXML
    public void onCancelButtonClick() {
        ((Stage) buttonCancel.getScene().getWindow()).close();
    }

    @FXML
    public void onKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ESCAPE)) {
            ((Stage) buttonCancel.getScene().getWindow()).close();
        }
    }
}
