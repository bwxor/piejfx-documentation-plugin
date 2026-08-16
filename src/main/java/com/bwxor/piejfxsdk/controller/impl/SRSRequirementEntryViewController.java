package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import com.bwxor.piejfxsdk.dto.SRSRequirement;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class SRSRequirementEntryViewController extends MovableViewController {

    @FXML private Label windowTitle;
    @FXML private TextField idField;
    @FXML private TextField requirementField;
    @FXML private TextField priorityField;
    @FXML private Button buttonCancel;

    private Consumer<SRSRequirement> onSave;

    public void setWindowTitle(String title) {
        windowTitle.setText(title);
    }

    public void setOnSave(Consumer<SRSRequirement> onSave) {
        this.onSave = onSave;
    }

    public void populate(SRSRequirement existing) {
        if (existing == null) return;
        idField.setText(existing.id());
        requirementField.setText(existing.requirement());
        priorityField.setText(existing.priority());
    }

    @FXML
    public void onSaveButtonClick() {
        if (onSave != null) {
            onSave.accept(new SRSRequirement(
                    idField.getText().trim(),
                    requirementField.getText().trim(),
                    priorityField.getText().trim()
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
