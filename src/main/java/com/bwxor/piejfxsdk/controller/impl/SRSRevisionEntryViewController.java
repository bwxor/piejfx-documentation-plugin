package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import com.bwxor.piejfxsdk.dto.SRSRevision;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class SRSRevisionEntryViewController extends MovableViewController {

    @FXML private Label windowTitle;
    @FXML private TextField versionField;
    @FXML private TextField dateField;
    @FXML private TextField authorField;
    @FXML private TextField descriptionField;
    @FXML private Button buttonCancel;

    private Consumer<SRSRevision> onSave;

    public void setWindowTitle(String title) {
        windowTitle.setText(title);
    }

    public void setOnSave(Consumer<SRSRevision> onSave) {
        this.onSave = onSave;
    }

    public void populate(SRSRevision existing) {
        if (existing == null) return;
        versionField.setText(existing.version());
        dateField.setText(existing.date());
        authorField.setText(existing.author());
        descriptionField.setText(existing.description());
    }

    @FXML
    public void onSaveButtonClick() {
        if (onSave != null) {
            onSave.accept(new SRSRevision(
                    versionField.getText().trim(),
                    dateField.getText().trim(),
                    authorField.getText().trim(),
                    descriptionField.getText().trim()
            ));
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
