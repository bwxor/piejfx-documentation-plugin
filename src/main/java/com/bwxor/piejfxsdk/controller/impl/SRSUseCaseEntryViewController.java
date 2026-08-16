package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import com.bwxor.piejfxsdk.dto.SRSUseCase;
import com.bwxor.piejfxsdk.state.ServiceState;
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

    private String preconditionsValue = "";
    private String mainFlowValue = "";
    private String alternateFlowValue = "";
    private String postConditionsValue = "";

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
        preconditionsValue = existing.preconditions() != null ? existing.preconditions() : "";
        mainFlowValue = existing.mainFlow() != null ? existing.mainFlow() : "";
        alternateFlowValue = existing.alternateFlow() != null ? existing.alternateFlow() : "";
        postConditionsValue = existing.postConditions() != null ? existing.postConditions() : "";
        updatePreviews();
    }

    // --- Edit button handlers ---

    @FXML
    public void onEditPreconditionsClick() {
        ServiceState.instance.getSrsTextAreaEditViewService()
                .showView("Preconditions", preconditionsValue,
                        v -> { preconditionsValue = v; updatePreviews(); });
    }

    @FXML
    public void onEditMainFlowClick() {
        ServiceState.instance.getSrsTextAreaEditViewService()
                .showView("Main Flow", mainFlowValue,
                        v -> { mainFlowValue = v; updatePreviews(); });
    }

    @FXML
    public void onEditAlternateFlowClick() {
        ServiceState.instance.getSrsTextAreaEditViewService()
                .showView("Alternate Flow", alternateFlowValue,
                        v -> { alternateFlowValue = v; updatePreviews(); });
    }

    @FXML
    public void onEditPostConditionsClick() {
        ServiceState.instance.getSrsTextAreaEditViewService()
                .showView("Post Conditions", postConditionsValue,
                        v -> { postConditionsValue = v; updatePreviews(); });
    }

    private void updatePreviews() {
        preconditionsField.setText(preview(preconditionsValue));
        mainFlowField.setText(preview(mainFlowValue));
        alternateFlowField.setText(preview(alternateFlowValue));
        postConditionsField.setText(preview(postConditionsValue));
    }

    private String preview(String value) {
        if (value == null || value.isBlank()) return "";
        String flat = value.replace('\n', ' ');
        return flat.length() > 60 ? flat.substring(0, 60) + "..." : flat;
    }

    @FXML
    public void onSaveButtonClick() {
        if (onSave != null) {
            onSave.accept(new SRSUseCase(
                    idField.getText().trim(),
                    actorField.getText().trim(),
                    preconditionsValue,
                    mainFlowValue,
                    alternateFlowValue,
                    postConditionsValue
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
