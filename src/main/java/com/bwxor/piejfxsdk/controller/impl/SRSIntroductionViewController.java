package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import com.bwxor.piejfxsdk.dto.SRSDocumentIntroduction;
import com.bwxor.piejfxsdk.state.SRSDocumentState;
import com.bwxor.piejfxsdk.state.ServiceState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class SRSIntroductionViewController extends MovableViewController {

    private static final int PREVIEW_MAX_LENGTH = 60;
    private static final String EMPTY_LABEL = "Empty";

    @FXML private Label windowTitle;
    @FXML private TextField purposePreview;
    @FXML private TextField scopePreview;
    @FXML private ListView<Pair<String, String>> definitionsList;
    @FXML private ListView<String> referencesList;
    @FXML private Button buttonCancel;

    private String purposeValue = "";
    private String scopeValue = "";

    private final ObservableList<Pair<String, String>> definitions = FXCollections.observableArrayList();
    private final ObservableList<String> references = FXCollections.observableArrayList();

    public void setWindowTitle(String title) {
        this.windowTitle.setText(title);
    }

    @FXML
    public void initialize() {
        definitionsList.setItems(definitions);
        definitionsList.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(Pair<String, String> item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getKey());
            }
        });
        referencesList.setItems(references);
    }

    public void loadData() {
        SRSDocumentIntroduction intro = SRSDocumentState.instance.getSrsDocumentIntroduction();
        if (intro != null) {
            purposeValue = intro.purpose() != null ? intro.purpose() : "";
            scopeValue = intro.scope() != null ? intro.scope() : "";
            definitions.clear();
            if (intro.definitionsAcronymsAbreviations() != null) {
                intro.definitionsAcronymsAbreviations().forEach((k, v) -> definitions.add(new Pair<>(k, v)));
            }
            references.clear();
            if (intro.references() != null) {
                references.addAll(intro.references());
            }
        }
        updatePreviews();
    }

    // --- Text area edits ---

    @FXML
    public void onEditPurposeClick() {
        ServiceState.instance.getSrsTextAreaEditViewService()
                .showView("Purpose", purposeValue, value -> { purposeValue = value; updatePreviews(); });
    }

    @FXML
    public void onEditScopeClick() {
        ServiceState.instance.getSrsTextAreaEditViewService()
                .showView("Scope", scopeValue, value -> { scopeValue = value; updatePreviews(); });
    }

    private void updatePreviews() {
        purposePreview.setText(preview(purposeValue));
        scopePreview.setText(preview(scopeValue));
    }

    private String preview(String value) {
        if (value == null || value.isBlank()) return EMPTY_LABEL;
        String flat = value.replace('\n', ' ');
        return flat.length() > PREVIEW_MAX_LENGTH ? flat.substring(0, PREVIEW_MAX_LENGTH) + "..." : flat;
    }

    // --- Definitions ---

    @FXML
    public void onAddDefinitionClick() {
        ServiceState.instance.getSrsKeyValueEntryViewService()
                .showView("Add Definition", "Term", "Definition", null, definitions::add);
    }

    @FXML
    public void onDefinitionsListMouseClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            Pair<String, String> selected = definitionsList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                ServiceState.instance.getSrsKeyValueEntryViewService()
                        .showView("Edit Definition", "Term", "Definition", selected,
                                updated -> definitions.set(definitions.indexOf(selected), updated));
            }
        }
    }

    @FXML
    public void onRemoveDefinitionClick() {
        Pair<String, String> selected = definitionsList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            ServiceState.instance.getNotificationService().showNotificationOk("Please select a definition to remove.");
            return;
        }
        definitions.remove(selected);
    }

    // --- References ---

    @FXML
    public void onAddReferenceClick() {
        ServiceState.instance.getSrsStringEntryViewService()
                .showView("Add Reference", "Reference", null, references::add);
    }

    @FXML
    public void onReferencesListMouseClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            String selected = referencesList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                ServiceState.instance.getSrsStringEntryViewService()
                        .showView("Edit Reference", "Reference", selected,
                                updated -> references.set(references.indexOf(selected), updated));
            }
        }
    }

    @FXML
    public void onRemoveReferenceClick() {
        String selected = referencesList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            ServiceState.instance.getNotificationService().showNotificationOk("Please select a reference to remove.");
            return;
        }
        references.remove(selected);
    }

    // --- Navigation ---

    @FXML
    public void onBackButtonClick() {
        ((Stage) buttonCancel.getScene().getWindow()).close();
        ServiceState.instance.getShowSRSRevisionHistoryViewService().showView();
    }

    @FXML
    public void onNextButtonClick() {
        Map<String, String> defsMap = new LinkedHashMap<>();
        definitions.forEach(p -> defsMap.put(p.getKey(), p.getValue()));
        SRSDocumentState.instance.setSrsDocumentIntroduction(new SRSDocumentIntroduction(
                purposeValue,
                scopeValue,
                defsMap,
                new ArrayList<>(references)
        ));
        ((Stage) buttonCancel.getScene().getWindow()).close();
        ServiceState.instance.getShowSRSOverallDescriptionViewService().showView();
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
