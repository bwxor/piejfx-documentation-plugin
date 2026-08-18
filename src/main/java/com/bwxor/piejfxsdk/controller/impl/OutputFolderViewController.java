package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
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
import java.util.function.BiConsumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;

/**
 * Generic output-folder / watermark view reused by all document generators.
 * Callers must inject a back-action and a generate-function via
 * {@link #configure(String, Runnable, Function, BiConsumer)}.
 */
public class OutputFolderViewController extends MovableViewController {

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

    private Runnable backAction;
    private Function<String, Boolean> generateAction;
    /** Called after the user picks a watermark file (file, or null when cleared). */
    private BiConsumer<File, WatermarkPosition> watermarkFileConsumer;
    /** Called with the parsed scale value just before generate is invoked. */
    private DoubleConsumer watermarkScaleConsumer;

    private File selectedWatermarkFile;

    /**
     * @param title               Window title text.
     * @param backAction          Invoked when the user clicks Back.
     * @param generateAction      Accepts the chosen output folder path; returns true on success.
     * @param watermarkConsumer   Receives the chosen watermark File + position (null file = cleared).
     * @param scaleConsumer       Receives the parsed watermark scale just before generation.
     */
    public void configure(String title,
                          Runnable backAction,
                          Function<String, Boolean> generateAction,
                          BiConsumer<File, WatermarkPosition> watermarkConsumer,
                          DoubleConsumer scaleConsumer) {
        this.windowTitle.setText(title);
        this.backAction = backAction;
        this.generateAction = generateAction;
        this.watermarkFileConsumer = watermarkConsumer;
        this.watermarkScaleConsumer = scaleConsumer;
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
        watermarkFileField.clear();
        selectedWatermarkFile = null;
        if (watermarkFileConsumer != null) {
            watermarkFileConsumer.accept(null, resolveWatermarkPosition());
        }
    }

    @FXML
    public void onBrowseWatermarkFileButtonClick() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.JPG", "*.PNG"));
        chooser.setTitle("Select Watermark File");
        Stage stage = (Stage) buttonCancel.getScene().getWindow();
        File selected = chooser.showOpenDialog(stage);
        if (selected != null) {
            selectedWatermarkFile = selected;
            watermarkFileField.setText(selected.getAbsolutePath());
        }
    }

    @FXML
    public void onBackButtonClick() {
        ((Stage) buttonCancel.getScene().getWindow()).close();
        if (backAction != null) {
            backAction.run();
        }
    }

    @FXML
    public void onGenerateButtonClick() {
        ServiceState serviceState = ServiceState.instance;

        if (outputFolderField.getText().trim().isEmpty()) {
            serviceState.getNotificationService().showNotificationOk("Please select an output folder.");
            return;
        }

        if (watermarkFileConsumer != null) {
            watermarkFileConsumer.accept(selectedWatermarkFile, resolveWatermarkPosition());
        }
        if (watermarkScaleConsumer != null) {
            watermarkScaleConsumer.accept(getWatermarkScale());
        }

        boolean success = generateAction.apply(outputFolderField.getText().trim());
        if (success) {
            serviceState.getNotificationService().showNotificationOk("Successfully generated.");
        } else {
            serviceState.getNotificationService().showNotificationOk("An error occurred while generating the PDF.");
        }
        ((Stage) buttonCancel.getScene().getWindow()).close();
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

    private WatermarkPosition resolveWatermarkPosition() {
        return WatermarkPosition.match(watermarkPositionComboBox.getValue());
    }

    private double parseDoubleOrDefault(String text) {
        if (text == null || text.isBlank()) return 0.5;
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException e) {
            return 0.5;
        }
    }

    public double getWatermarkScale() {
        return parseDoubleOrDefault(watermarkScaleField.getText());
    }
}
