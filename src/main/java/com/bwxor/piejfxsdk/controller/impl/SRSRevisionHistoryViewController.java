package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import com.bwxor.piejfxsdk.dto.SRSDocumentRevisionHistory;
import com.bwxor.piejfxsdk.dto.SRSRevision;
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

import java.util.ArrayList;

public class SRSRevisionHistoryViewController extends MovableViewController {

    @FXML private Label windowTitle;
    @FXML private ListView<SRSRevision> revisionsList;
    @FXML private Button buttonCancel;

    private final ObservableList<SRSRevision> revisions = FXCollections.observableArrayList();

    public void setWindowTitle(String title) {
        this.windowTitle.setText(title);
    }

    @FXML
    public void initialize() {
        revisionsList.setItems(revisions);
        revisionsList.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(SRSRevision item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.version() + " — " + item.author());
            }
        });
    }

    public void loadData() {
        SRSDocumentRevisionHistory history = SRSDocumentState.instance.getSrsDocumentRevisionHistory();
        revisions.clear();
        if (history != null && history.srsRevisions() != null) {
            revisions.addAll(history.srsRevisions());
        }
    }

    @FXML
    public void onAddButtonClick() {
        ServiceState.instance.getSrsRevisionEntryViewService()
                .showView("Add Revision", null, revisions::add);
    }

    @FXML
    public void onRemoveButtonClick() {
        SRSRevision selected = revisionsList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            ServiceState.instance.getNotificationService().showNotificationOk("Please select a revision to remove.");
            return;
        }
        revisions.remove(selected);
    }

    @FXML
    public void onListMouseClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            SRSRevision selected = revisionsList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                ServiceState.instance.getSrsRevisionEntryViewService()
                        .showView("Edit Revision", selected, updated -> revisions.set(revisions.indexOf(selected), updated));
            }
        }
    }

    @FXML
    public void onBackButtonClick() {
        ((Stage) buttonCancel.getScene().getWindow()).close();
        ServiceState.instance.getShowSRSTitlePageViewService().showView();
    }

    @FXML
    public void onNextButtonClick() {
        SRSDocumentState.instance.setSrsDocumentRevisionHistory(
                new SRSDocumentRevisionHistory(new ArrayList<>(revisions)));
        ((Stage) buttonCancel.getScene().getWindow()).close();
        ServiceState.instance.getShowSRSIntroductionViewService().showView();
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
