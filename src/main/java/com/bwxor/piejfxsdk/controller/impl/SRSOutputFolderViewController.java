package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import com.bwxor.piejfxsdk.state.SRSDocumentState;
import com.bwxor.piejfxsdk.state.ServiceState;
import com.bwxor.piejfxsdk.type.WatermarkPosition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class SRSOutputFolderViewController extends MovableViewController {

    @FXML
    private Label windowTitle;
    @FXML
    private TextField watermarkFileField;
    @FXML
    private TextField watermarkScaleField;
    @FXML
    private ComboBox<String> watermarkPositionComboBox;
    @FXML
    private TextField outputFolderField;
    @FXML
    private Button buttonCancel;

    public void setWindowTitle(String title) {
        this.windowTitle.setText(title);
    }

    @FXML
    public void onBrowseOutputFolderButtonClick() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Output Folder");
        Stage stage = (Stage) buttonCancel.getScene().getWindow();
        File selected = chooser.showDialog(stage);
        if (selected != null) {
            outputFolderField.setText(selected.getAbsolutePath());
        }
    }

    @FXML
    public void onClearWatermarkFileButtonClick() {
        SRSDocumentState srsDocumentState = SRSDocumentState.instance;

        watermarkFileField.clear();
        srsDocumentState.setWatermarkFile(null);
    }

    @FXML
    public void onBrowseWatermarkFileButtonClick() {
        SRSDocumentState srsDocumentState = SRSDocumentState.instance;

        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.JPG", "*.PNG");
        chooser.getExtensionFilters().add(extFilter);
        chooser.setTitle("Select Watermark File");
        Stage stage = (Stage) buttonCancel.getScene().getWindow();
        File selected = chooser.showOpenDialog(stage);
        if (selected != null) {
            watermarkFileField.setText(selected.getAbsolutePath());
            srsDocumentState.setWatermarkFile(selected);
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
        SRSDocumentState srsDocumentState = SRSDocumentState.instance;

        if (outputFolderField.getText().trim().isEmpty()) {
            serviceState.getNotificationService().showNotificationOk("Please select an output folder.");
            return;
        }

        srsDocumentState.setWatermarkPosition(WatermarkPosition.match(watermarkPositionComboBox.getValue()));
        srsDocumentState.setWatermarkScale(parseDoubleOrDefault(watermarkScaleField.getText()));

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

    private double parseDoubleOrDefault(String text) {
        if (text == null || text.isBlank()) {
            return 0.5;
        }
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException e) {
            return 0.5;
        }
    }
}
