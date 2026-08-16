package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import com.bwxor.piejfxsdk.dto.SRSUseCase;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class SRSUseCaseEntryViewController extends MovableViewController {

    @FXML private Label windowTitle;
    @FXML private TextField idField;
    @FXML private TextField actorField;
    @FXML private TextField preconditionsField;
    @FXML private TextField mainFlowField;
    @FXML private TextField alternateFlowField;
    @FXML private TextField postConditionsField;
    @FXML private Button buttonCancel;

    private Consumer<SRSUseCase> onSave;

    public void setWindowTitle(String title) {
        windowTitle.setText(title);
    }

    public void setOnSave(Consumer<SRSUseCase> onSave) {
        this.onSave = onSave;
    }

    public void populate(SRSUseCase existing) {
        if (existing == null) return;
        idField.setText(existing.id());
        actorField.setText(existing.actor());
        preconditionsField.setText(existing.preconditions());
        mainFlowField.setText(existing.mainFlow());
        alternateFlowField.setText(existing.alternateFlow());
        postConditionsField.setText(existing.postConditions());
    }

    @FXML
    public void onSaveButtonClick() {
        if (onSave != null) {
            onSave.accept(new SRSUseCase(
                    idField.getText().trim(),
                    actorField.getText().trim(),
                    preconditionsField.getText().trim(),
                    mainFlowField.getText().trim(),
                    alternateFlowField.getText().trim(),
                    postConditionsField.getText().trim()
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
