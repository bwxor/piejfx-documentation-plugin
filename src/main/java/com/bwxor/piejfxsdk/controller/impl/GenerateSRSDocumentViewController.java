package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import com.bwxor.piejfxsdk.dto.SRSDocumentTitlePage;
import com.bwxor.piejfxsdk.state.SRSDocumentState;
import com.bwxor.piejfxsdk.state.ServiceState;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class GenerateSRSDocumentViewController extends MovableViewController {
    private static final String EMPTY_STRING = "";

    @FXML
    private Label windowTitle;

    @FXML
    private TextField projectNameField;
    @FXML
    private TextField documentVersionField;
    @FXML
    private TextField dateField;
    @FXML
    private TextField authorsField;
    @FXML
    private TextField statusField;

    @FXML
    private Button browseButton;

    @FXML
    private Button buttonCreate;

    public void setWindowTitle(String title) {
        this.windowTitle.setText(title);
    }

    public void loadProjectMetadata() {
        SRSDocumentState srsDocumentState = SRSDocumentState.instance;
        SRSDocumentTitlePage titlePage = srsDocumentState.getSrsDocumentTitlePage();

        projectNameField.setText(titlePage != null ? titlePage.projectName() : EMPTY_STRING);
        documentVersionField.setText(titlePage != null ? titlePage.documentVersion() : EMPTY_STRING);
        dateField.setText(titlePage != null ? titlePage.date() : EMPTY_STRING);
        authorsField.setText(titlePage != null ? titlePage.authors() : EMPTY_STRING);
        statusField.setText(titlePage != null ? titlePage.status() : EMPTY_STRING);
    }

    @FXML
    public void onNextButtonClick() {
        ServiceState serviceState = ServiceState.instance;

        if (projectNameField.getText().trim().isEmpty()) {
            serviceState.getNotificationService().showNotificationOk("Project Name cannot be empty.");
        } else if (documentVersionField.getText().trim().isEmpty()) {
            serviceState.getNotificationService().showNotificationOk("Document Version cannot be empty.");
        } else if (dateField.getText().trim().isEmpty()) {
            serviceState.getNotificationService().showNotificationOk("Date cannot be empty.");
        } else if (authorsField.getText().trim().isEmpty()) {
            serviceState.getNotificationService().showNotificationOk("Authors cannot be empty.");
        } else if (statusField.getText().trim().isEmpty()) {
            serviceState.getNotificationService().showNotificationOk("Status cannot be empty.");
        } else {
            SRSDocumentState srsDocumentState = SRSDocumentState.instance;

            srsDocumentState.setSrsDocumentTitlePage(new SRSDocumentTitlePage(
                    projectNameField.getText().trim(),
                    documentVersionField.getText().trim(),
                    dateField.getText().trim(),
                    authorsField.getText().trim(),
                    statusField.getText().trim()
            ));

            // Call the next service that shows the next view of the documentation generation process
        }
    }

    @FXML
    public void onCancelButtonClick() {
        ((Stage) buttonCreate.getScene().getWindow()).close();
    }

    @FXML
    public void onKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ESCAPE)) {
            ((Stage) buttonCreate.getScene().getWindow()).close();
        }
    }
}
