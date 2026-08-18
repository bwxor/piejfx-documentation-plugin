package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import com.bwxor.piejfxsdk.dto.ReleaseNotesDocument;
import com.bwxor.piejfxsdk.dto.ReleaseNotesEntry;
import com.bwxor.piejfxsdk.state.ReleaseNotesDocumentState;
import com.bwxor.piejfxsdk.state.ServiceState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ReleaseNotesEntriesViewController extends MovableViewController {

    @FXML private Label windowTitle;
    @FXML private ListView<ReleaseNotesEntry> newFeaturesList;
    @FXML private ListView<ReleaseNotesEntry> bugFixesList;
    @FXML private ListView<ReleaseNotesEntry> knownIssuesList;
    @FXML private Button buttonCancel;

    private final ObservableList<ReleaseNotesEntry> features = FXCollections.observableArrayList();
    private final ObservableList<ReleaseNotesEntry> fixes = FXCollections.observableArrayList();
    private final ObservableList<ReleaseNotesEntry> issues = FXCollections.observableArrayList();

    public void setWindowTitle(String title) {
        this.windowTitle.setText(title);
    }

    @FXML
    public void initialize() {
        newFeaturesList.setItems(features);
        bugFixesList.setItems(fixes);
        knownIssuesList.setItems(issues);
        configureCellFactory(newFeaturesList);
        configureCellFactory(bugFixesList);
        configureCellFactory(knownIssuesList);
    }

    private void configureCellFactory(ListView<ReleaseNotesEntry> lv) {
        lv.setCellFactory(_ -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(ReleaseNotesEntry item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null
                        : (item.category() != null && !item.category().isBlank()
                                ? "[" + item.category() + "] " : "") + item.description());
            }
        });
    }

    public void loadData() {
        ReleaseNotesDocument doc = ReleaseNotesDocumentState.instance.getDocument();
        if (doc == null) return;
        if (doc.newFeatures() != null) features.setAll(doc.newFeatures());
        if (doc.bugFixes() != null) fixes.setAll(doc.bugFixes());
        if (doc.knownIssues() != null) issues.setAll(doc.knownIssues());
    }

    // -- New Features --

    @FXML
    public void onAddFeatureClick() {
        ServiceState.instance.getSrsKeyValueEntryViewService()
                .showView("Add New Feature", "Category", "Description", null,
                        p -> features.add(new ReleaseNotesEntry(p.getKey(), p.getValue())));
    }

    @FXML
    public void onRemoveFeatureClick() {
        ReleaseNotesEntry selected = newFeaturesList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            ServiceState.instance.getNotificationService().showNotificationOk("Please select a feature to remove.");
            return;
        }
        features.remove(selected);
    }

    // -- Bug Fixes --

    @FXML
    public void onAddBugFixClick() {
        ServiceState.instance.getSrsKeyValueEntryViewService()
                .showView("Add Bug Fix", "Category", "Description", null,
                        p -> fixes.add(new ReleaseNotesEntry(p.getKey(), p.getValue())));
    }

    @FXML
    public void onRemoveBugFixClick() {
        ReleaseNotesEntry selected = bugFixesList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            ServiceState.instance.getNotificationService().showNotificationOk("Please select a bug fix to remove.");
            return;
        }
        fixes.remove(selected);
    }

    // -- Known Issues --

    @FXML
    public void onAddKnownIssueClick() {
        ServiceState.instance.getSrsKeyValueEntryViewService()
                .showView("Add Known Issue", "Category", "Description", null,
                        p -> issues.add(new ReleaseNotesEntry(p.getKey(), p.getValue())));
    }

    @FXML
    public void onRemoveKnownIssueClick() {
        ReleaseNotesEntry selected = knownIssuesList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            ServiceState.instance.getNotificationService().showNotificationOk("Please select an issue to remove.");
            return;
        }
        issues.remove(selected);
    }

    // -- Navigation --

    @FXML
    public void onBackButtonClick() {
        ((Stage) buttonCancel.getScene().getWindow()).close();
        ServiceState.instance.getShowReleaseNotesHeaderViewService().showView();
    }

    @FXML
    public void onNextButtonClick() {
        ReleaseNotesDocument existing = ReleaseNotesDocumentState.instance.getDocument();
        ReleaseNotesDocument updated = new ReleaseNotesDocument(
                existing.productName(), existing.version(), existing.releaseDate(),
                existing.releasedBy(), existing.summary(),
                new ArrayList<>(features),
                new ArrayList<>(fixes),
                new ArrayList<>(issues),
                existing.migrationNotes()
        );
        ReleaseNotesDocumentState.instance.setDocument(updated);
        ((Stage) buttonCancel.getScene().getWindow()).close();

        ReleaseNotesDocumentState rnState = ReleaseNotesDocumentState.instance;
        ServiceState.instance.getShowOutputFolderViewService().showView(
                "Generate Release Notes (3/3)",
                () -> ServiceState.instance.getShowReleaseNotesEntriesViewService().showView(),
                outputFolder -> ServiceState.instance.getGenerateReleaseNotesPDFService().generatePdf(outputFolder),
                (file, position) -> {
                    rnState.setWatermarkFile(file);
                    rnState.setWatermarkPosition(file == null
                            ? com.bwxor.piejfxsdk.type.WatermarkPosition.UNKNOWN : position);
                },
                rnState::setWatermarkScale
        );
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
