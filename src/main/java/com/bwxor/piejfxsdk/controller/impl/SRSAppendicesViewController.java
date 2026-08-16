package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import com.bwxor.piejfxsdk.dto.SRSDocumentAppendices;
import com.bwxor.piejfxsdk.state.SRSDocumentState;
import com.bwxor.piejfxsdk.state.ServiceState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

public class SRSAppendicesViewController extends MovableViewController {

    @FXML
    private Label windowTitle;
    @FXML
    private ListView<Pair<String, String>> glossaryList;
    @FXML
    private ListView<String> openIssuesList;
    @FXML
    private Button buttonCancel;

    private final ObservableList<Pair<String, String>> glossaryEntries = FXCollections.observableArrayList();
    private final ObservableList<String> openIssues = FXCollections.observableArrayList();

    public void setWindowTitle(String title) {
        this.windowTitle.setText(title);
    }

    @FXML
    public void initialize() {
        glossaryList.setItems(glossaryEntries);
        glossaryList.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(Pair<String, String> item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getKey());
            }
        });
        openIssuesList.setItems(openIssues);
    }

    public void loadData() {
        SRSDocumentAppendices appendices = SRSDocumentState.instance.getSrsDocumentAppendices();
        if (appendices == null) return;
        glossaryEntries.clear();
        if (appendices.glossary() != null) {
            appendices.glossary().forEach((k, v) -> glossaryEntries.add(new Pair<>(k, v)));
        }
        openIssues.clear();
        if (appendices.openIssues() != null) {
            openIssues.addAll(appendices.openIssues());
        }
    }

    // --- Glossary ---

    @FXML
    public void onAddGlossaryClick() {
        ServiceState.instance.getSrsKeyValueEntryViewService()
                .showView("Add Glossary Entry", "Term", "Definition", null, glossaryEntries::add);
    }

    @FXML
    public void onGlossaryListMouseClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            Pair<String, String> selected = glossaryList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                ServiceState.instance.getSrsKeyValueEntryViewService()
                        .showView("Edit Glossary Entry", "Term", "Definition", selected,
                                updated -> glossaryEntries.set(glossaryEntries.indexOf(selected), updated));
            }
        }
    }

    @FXML
    public void onRemoveGlossaryClick() {
        Pair<String, String> selected = glossaryList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            ServiceState.instance.getNotificationService().showNotificationOk("Please select a glossary entry to remove.");
            return;
        }
        glossaryEntries.remove(selected);
    }

    // --- Open Issues ---

    @FXML
    public void onAddIssueClick() {
        ServiceState.instance.getSrsStringEntryViewService()
                .showView("Add Open Issue", "Issue", null, openIssues::add);
    }

    @FXML
    public void onOpenIssuesListMouseClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            String selected = openIssuesList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                ServiceState.instance.getSrsStringEntryViewService()
                        .showView("Edit Open Issue", "Issue", selected,
                                updated -> openIssues.set(openIssues.indexOf(selected), updated));
            }
        }
    }

    @FXML
    public void onRemoveIssueClick() {
        String selected = openIssuesList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            ServiceState.instance.getNotificationService().showNotificationOk("Please select an issue to remove.");
            return;
        }
        openIssues.remove(selected);
    }

    // --- Navigation ---

    @FXML
    public void onBackButtonClick() {
        ((Stage) buttonCancel.getScene().getWindow()).close();
        ServiceState.instance.getShowSRSUseCasesViewService().showView();
    }

    @FXML
    public void onNextButtonClick() {
        Map<String, String> glossaryMap = new LinkedHashMap<>();
        glossaryEntries.forEach(p -> glossaryMap.put(p.getKey(), p.getValue()));
        SRSDocumentState.instance.setSrsDocumentAppendices(new SRSDocumentAppendices(
                glossaryMap,
                new ArrayList<>(openIssues)
        ));
        ((Stage) buttonCancel.getScene().getWindow()).close();
        ServiceState.instance.getShowSRSOutputFolderViewService().showView();
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
