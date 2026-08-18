package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import com.bwxor.piejfxsdk.dto.ChangeRequestDocument;
import com.bwxor.piejfxsdk.dto.ChangeRequestImpactItem;
import com.bwxor.piejfxsdk.state.ChangeRequestDocumentState;
import com.bwxor.piejfxsdk.state.ServiceState;
import com.bwxor.piejfxsdk.type.WatermarkPosition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ChangeRequestDetailsViewController extends MovableViewController {

    private static final int PREVIEW_MAX_LENGTH = 60;
    private static final String EMPTY_LABEL = "Empty";

    @FXML private Label windowTitle;
    @FXML private TextField descriptionPreview;
    @FXML private TextField justificationPreview;
    @FXML private ListView<ChangeRequestImpactItem> impactedComponentsList;
    @FXML private TextField testingStrategyPreview;
    @FXML private TextField rollbackPlanPreview;
    @FXML private TextField approvedByField;
    @FXML private TextField approvalDateField;
    @FXML private Button buttonCancel;

    private String descriptionValue = "";
    private String justificationValue = "";
    private String testingStrategyValue = "";
    private String rollbackPlanValue = "";

    private final ObservableList<ChangeRequestImpactItem> impactItems = FXCollections.observableArrayList();

    public void setWindowTitle(String title) {
        this.windowTitle.setText(title);
    }

    @FXML
    public void initialize() {
        impactedComponentsList.setItems(impactItems);
        impactedComponentsList.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(ChangeRequestImpactItem item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.component() + ": " + item.description());
            }
        });
    }

    public void loadData() {
        ChangeRequestDocument doc = ChangeRequestDocumentState.instance.getDocument();
        if (doc == null) return;
        descriptionValue = doc.description() != null ? doc.description() : "";
        justificationValue = doc.justification() != null ? doc.justification() : "";
        if (doc.impactedComponents() != null) impactItems.setAll(doc.impactedComponents());
        testingStrategyValue = doc.testingStrategy() != null ? doc.testingStrategy() : "";
        rollbackPlanValue = doc.rollbackPlan() != null ? doc.rollbackPlan() : "";
        approvedByField.setText(doc.approvedBy());
        approvalDateField.setText(doc.approvalDate());
        updatePreviews();
    }

    @FXML
    public void onEditDescriptionClick() {
        ServiceState.instance.getSrsTextAreaEditViewService()
                .showView("Description", descriptionValue, value -> { descriptionValue = value; updatePreviews(); });
    }

    @FXML
    public void onEditJustificationClick() {
        ServiceState.instance.getSrsTextAreaEditViewService()
                .showView("Justification / Business Case", justificationValue, value -> { justificationValue = value; updatePreviews(); });
    }

    @FXML
    public void onEditTestingStrategyClick() {
        ServiceState.instance.getSrsTextAreaEditViewService()
                .showView("Testing Strategy", testingStrategyValue, value -> { testingStrategyValue = value; updatePreviews(); });
    }

    @FXML
    public void onEditRollbackPlanClick() {
        ServiceState.instance.getSrsTextAreaEditViewService()
                .showView("Rollback Plan", rollbackPlanValue, value -> { rollbackPlanValue = value; updatePreviews(); });
    }

    private void updatePreviews() {
        descriptionPreview.setText(preview(descriptionValue));
        justificationPreview.setText(preview(justificationValue));
        testingStrategyPreview.setText(preview(testingStrategyValue));
        rollbackPlanPreview.setText(preview(rollbackPlanValue));
    }

    private String preview(String value) {
        if (value == null || value.isBlank()) return EMPTY_LABEL;
        String flat = value.replace('\n', ' ');
        return flat.length() > PREVIEW_MAX_LENGTH ? flat.substring(0, PREVIEW_MAX_LENGTH) + "..." : flat;
    }

    @FXML
    public void onAddImpactClick() {
        ServiceState.instance.getSrsKeyValueEntryViewService()
                .showView("Add Impacted Component", "Component", "Description", null,
                        p -> impactItems.add(new ChangeRequestImpactItem(p.getKey(), p.getValue())));
    }

    @FXML
    public void onRemoveImpactClick() {
        ChangeRequestImpactItem selected = impactedComponentsList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            ServiceState.instance.getNotificationService().showNotificationOk("Please select a component to remove.");
            return;
        }
        impactItems.remove(selected);
    }

    @FXML
    public void onBackButtonClick() {
        ((Stage) buttonCancel.getScene().getWindow()).close();
        ServiceState.instance.getShowChangeRequestHeaderViewService().showView();
    }

    @FXML
    public void onNextButtonClick() {
        ChangeRequestDocument existing = ChangeRequestDocumentState.instance.getDocument();
        ChangeRequestDocument updated = new ChangeRequestDocument(
                existing.changeId(), existing.title(), existing.requestedBy(),
                existing.requestDate(), existing.targetVersion(),
                existing.priority(), existing.status(),
                descriptionValue,
                justificationValue,
                new ArrayList<>(impactItems),
                testingStrategyValue,
                rollbackPlanValue,
                approvedByField.getText().trim(),
                approvalDateField.getText().trim()
        );
        ChangeRequestDocumentState.instance.setDocument(updated);
        ((Stage) buttonCancel.getScene().getWindow()).close();

        ChangeRequestDocumentState crState = ChangeRequestDocumentState.instance;
        ServiceState.instance.getShowOutputFolderViewService().showView(
                "Generate Change Request (3/3)",
                () -> ServiceState.instance.getShowChangeRequestDetailsViewService().showView(),
                outputFolder -> ServiceState.instance.getGenerateChangeRequestPDFService().generatePdf(outputFolder),
                (file, position) -> {
                    crState.setWatermarkFile(file);
                    crState.setWatermarkPosition(file == null ? WatermarkPosition.UNKNOWN : position);
                },
                crState::setWatermarkScale
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
