package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import com.bwxor.piejfxsdk.dto.ChangeRequestDocument;
import com.bwxor.piejfxsdk.state.ChangeRequestDocumentState;
import com.bwxor.piejfxsdk.state.ServiceState;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ChangeRequestHeaderViewController extends MovableViewController {

    @FXML private Label windowTitle;
    @FXML private TextField changeIdField;
    @FXML private TextField titleField;
    @FXML private TextField requestedByField;
    @FXML private TextField requestDateField;
    @FXML private TextField targetVersionField;
    @FXML private ComboBox<String> priorityComboBox;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private Button buttonCancel;

    public void setWindowTitle(String title) {
        this.windowTitle.setText(title);
    }

    public void loadData() {
        ChangeRequestDocument doc = ChangeRequestDocumentState.instance.getDocument();
        if (doc == null) return;
        changeIdField.setText(doc.changeId());
        titleField.setText(doc.title());
        requestedByField.setText(doc.requestedBy());
        requestDateField.setText(doc.requestDate());
        targetVersionField.setText(doc.targetVersion());
        if (doc.priority() != null) priorityComboBox.setValue(doc.priority());
        if (doc.status() != null) statusComboBox.setValue(doc.status());
    }

    @FXML
    public void onNextButtonClick() {
        if (changeIdField.getText().trim().isEmpty()) {
            ServiceState.instance.getNotificationService().showNotificationOk("Change ID cannot be empty.");
            return;
        }
        ChangeRequestDocument existing = ChangeRequestDocumentState.instance.getDocument();
        ChangeRequestDocument updated = new ChangeRequestDocument(
                changeIdField.getText().trim(),
                titleField.getText().trim(),
                requestedByField.getText().trim(),
                requestDateField.getText().trim(),
                targetVersionField.getText().trim(),
                priorityComboBox.getValue(),
                statusComboBox.getValue(),
                existing != null ? existing.description() : "",
                existing != null ? existing.justification() : "",
                existing != null ? existing.impactedComponents() : new java.util.ArrayList<>(),
                existing != null ? existing.testingStrategy() : "",
                existing != null ? existing.rollbackPlan() : "",
                existing != null ? existing.approvedBy() : "",
                existing != null ? existing.approvalDate() : ""
        );
        ChangeRequestDocumentState.instance.setDocument(updated);
        ((Stage) buttonCancel.getScene().getWindow()).close();
        ServiceState.instance.getShowChangeRequestDetailsViewService().showView();
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
