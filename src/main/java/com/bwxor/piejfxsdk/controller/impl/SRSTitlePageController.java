package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import com.bwxor.piejfxsdk.dto.SRSDocumentTitlePage;
import com.bwxor.piejfxsdk.state.SRSDocumentState;
import com.bwxor.piejfxsdk.state.ServiceState;
import com.bwxor.piejfxsdk.type.Status;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class SRSTitlePageController extends MovableViewController {
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
    private ComboBox<String> statusField;

    @FXML
    private Button buttonNext;
    @FXML
    private Button buttonCancel;

    public void setWindowTitle(String title) {
        this.windowTitle.setText(title);
    }

    public void loadProjectMetadata() {
        SRSDocumentTitlePage titlePage = SRSDocumentState.instance.getSrsDocumentTitlePage();

        projectNameField.setText(titlePage != null ? titlePage.projectName() : EMPTY_STRING);
        documentVersionField.setText(titlePage != null && !"Empty".equals(titlePage.documentVersion()) ? titlePage.documentVersion() : EMPTY_STRING);
        dateField.setText(titlePage != null && !"Empty".equals(titlePage.date()) ? titlePage.date() : EMPTY_STRING);
        authorsField.setText(titlePage != null && !"Empty".equals(titlePage.authors()) ? titlePage.authors() : EMPTY_STRING);
        statusField.setValue(titlePage != null ? titlePage.status().getValue() : "Draft");
        if (statusField.getValue() == null || statusField.getValue().isEmpty()) {
            statusField.setValue("Draft");
        }
    }

    @FXML
    public void onNextButtonClick() {
        ServiceState serviceState = ServiceState.instance;

        if (projectNameField.getText().trim().isEmpty()) {
            serviceState.getNotificationService().showNotificationOk("Project Name cannot be empty.");
        } else {
            String version = documentVersionField.getText().trim();
            String date = dateField.getText().trim();
            String authors = authorsField.getText().trim();
            String statusVal = statusField.getValue() != null ? statusField.getValue().trim() : "Draft";

            SRSDocumentState.instance.setSrsDocumentTitlePage(new SRSDocumentTitlePage(
                    projectNameField.getText().trim(),
                    version.isEmpty() ? "Empty" : version,
                    date.isEmpty() ? "Empty" : date,
                    authors.isEmpty() ? "Empty" : authors,
                    Status.match(statusVal.isEmpty() ? "Draft" : statusVal)
            ));

            ((Stage) buttonNext.getScene().getWindow()).close();
            serviceState.getShowSRSRevisionHistoryViewService().showView();
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
}
