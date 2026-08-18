package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import com.bwxor.piejfxsdk.state.ReleaseNotesDocumentState;
import com.bwxor.piejfxsdk.state.ServiceState;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ReleaseNotesHeaderViewController extends MovableViewController {

    private static final int PREVIEW_MAX_LENGTH = 60;
    private static final String EMPTY_LABEL = "Empty";

    @FXML private Label windowTitle;
    @FXML private TextField productNameField;
    @FXML private TextField versionField;
    @FXML private TextField releaseDateField;
    @FXML private TextField releasedByField;
    @FXML private TextField summaryPreview;
    @FXML private TextField migrationNotesPreview;
    @FXML private Button buttonCancel;

    private String summaryValue = "";
    private String migrationNotesValue = "";

    public void setWindowTitle(String title) {
        this.windowTitle.setText(title);
    }

    public void loadData() {
        var doc = ReleaseNotesDocumentState.instance.getDocument();
        if (doc == null) return;
        productNameField.setText(doc.productName());
        versionField.setText(doc.version());
        releaseDateField.setText(doc.releaseDate());
        releasedByField.setText(doc.releasedBy());
        summaryValue = doc.summary() != null ? doc.summary() : "";
        migrationNotesValue = doc.migrationNotes() != null ? doc.migrationNotes() : "";
        updatePreviews();
    }

    @FXML
    public void onEditSummaryClick() {
        ServiceState.instance.getSrsTextAreaEditViewService()
                .showView("Release Summary", summaryValue, value -> { summaryValue = value; updatePreviews(); });
    }

    @FXML
    public void onEditMigrationNotesClick() {
        ServiceState.instance.getSrsTextAreaEditViewService()
                .showView("Migration Notes", migrationNotesValue, value -> { migrationNotesValue = value; updatePreviews(); });
    }

    private void updatePreviews() {
        summaryPreview.setText(preview(summaryValue));
        migrationNotesPreview.setText(preview(migrationNotesValue));
    }

    private String preview(String value) {
        if (value == null || value.isBlank()) return EMPTY_LABEL;
        String flat = value.replace('\n', ' ');
        return flat.length() > PREVIEW_MAX_LENGTH ? flat.substring(0, PREVIEW_MAX_LENGTH) + "..." : flat;
    }

    @FXML
    public void onNextButtonClick() {
        if (productNameField.getText().trim().isEmpty()) {
            ServiceState.instance.getNotificationService().showNotificationOk("Product Name cannot be empty.");
            return;
        }
        var existing = ReleaseNotesDocumentState.instance.getDocument();
        var updated = new com.bwxor.piejfxsdk.dto.ReleaseNotesDocument(
                productNameField.getText().trim(),
                versionField.getText().trim(),
                releaseDateField.getText().trim(),
                releasedByField.getText().trim(),
                summaryValue,
                existing != null ? existing.newFeatures() : new java.util.ArrayList<>(),
                existing != null ? existing.bugFixes() : new java.util.ArrayList<>(),
                existing != null ? existing.knownIssues() : new java.util.ArrayList<>(),
                migrationNotesValue
        );
        ReleaseNotesDocumentState.instance.setDocument(updated);
        ((Stage) buttonCancel.getScene().getWindow()).close();
        ServiceState.instance.getShowReleaseNotesEntriesViewService().showView();
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
