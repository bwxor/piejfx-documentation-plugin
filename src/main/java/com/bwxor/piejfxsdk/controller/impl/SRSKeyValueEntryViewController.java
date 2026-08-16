package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.function.Consumer;

public class SRSKeyValueEntryViewController extends MovableViewController {

    @FXML private Label windowTitle;
    @FXML private Label keyLabel;
    @FXML private Label valueLabel;
    @FXML private TextField keyField;
    @FXML private TextField valueField;
    @FXML private Button buttonCancel;

    private Consumer<Pair<String, String>> onSave;

    public void setWindowTitle(String title) {
        windowTitle.setText(title);
    }

    public void setLabels(String key, String value) {
        keyLabel.setText(key);
        valueLabel.setText(value);
    }

    public void setOnSave(Consumer<Pair<String, String>> onSave) {
        this.onSave = onSave;
    }

    public void populate(Pair<String, String> existing) {
        if (existing == null) return;
        keyField.setText(existing.getKey());
        valueField.setText(existing.getValue());
    }

    @FXML
    public void onSaveButtonClick() {
        if (onSave != null) {
            onSave.accept(new Pair<>(keyField.getText().trim(), valueField.getText().trim()));
        }
        close();
    }

    @FXML
    public void onCancelButtonClick() {
        close();
    }

    @FXML
    public void onKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ESCAPE)) close();
    }

    private void close() {
        ((Stage) buttonCancel.getScene().getWindow()).close();
    }
}
