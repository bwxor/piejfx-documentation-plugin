package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class SRSTextAreaEditViewController extends MovableViewController {

    @FXML private Label windowTitle;
    @FXML private TextArea textField;
    @FXML private Button buttonCancel;

    private Consumer<String> onSave;

    public void setWindowTitle(String title) {
        windowTitle.setText(title);
    }

    public void setOnSave(Consumer<String> onSave) {
        this.onSave = onSave;
    }

    public void populate(String existing) {
        textField.setText(existing != null ? existing : "");
    }

    @FXML
    public void onSaveButtonClick() {
        if (onSave != null) {
            onSave.accept(textField.getText().trim());
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
