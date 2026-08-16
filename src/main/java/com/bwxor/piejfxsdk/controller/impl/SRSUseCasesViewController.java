package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import com.bwxor.piejfxsdk.dto.SRSDocumentUseCases;
import com.bwxor.piejfxsdk.dto.SRSUseCase;
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

public class SRSUseCasesViewController extends MovableViewController {

    @FXML private Label windowTitle;
    @FXML private ListView<SRSUseCase> useCasesList;
    @FXML private Button buttonCancel;

    private final ObservableList<SRSUseCase> useCases = FXCollections.observableArrayList();

    public void setWindowTitle(String title) {
        this.windowTitle.setText(title);
    }

    @FXML
    public void initialize() {
        useCasesList.setItems(useCases);
        useCasesList.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(SRSUseCase item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.id() + " — " + item.actor());
            }
        });
    }

    public void loadData() {
        SRSDocumentUseCases uc = SRSDocumentState.instance.getSrsDocumentUseCases();
        useCases.clear();
        if (uc != null && uc.useCases() != null) {
            useCases.addAll(uc.useCases());
        }
    }

    @FXML
    public void onAddButtonClick() {
        ServiceState.instance.getSrsUseCaseEntryViewService()
                .showView("Add Use Case", null, useCases::add);
    }

    @FXML
    public void onListMouseClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            SRSUseCase selected = useCasesList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                ServiceState.instance.getSrsUseCaseEntryViewService()
                        .showView("Edit Use Case", selected,
                                updated -> useCases.set(useCases.indexOf(selected), updated));
            }
        }
    }

    @FXML
    public void onRemoveButtonClick() {
        SRSUseCase selected = useCasesList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            ServiceState.instance.getNotificationService().showNotificationOk("Please select a use case to remove.");
            return;
        }
        useCases.remove(selected);
    }

    @FXML
    public void onBackButtonClick() {
        ((Stage) buttonCancel.getScene().getWindow()).close();
        ServiceState.instance.getShowSRSExternalInterfaceRequirementsViewService().showView();
    }

    @FXML
    public void onNextButtonClick() {
        SRSDocumentState.instance.setSrsDocumentUseCases(
                new SRSDocumentUseCases(new ArrayList<>(useCases)));
        ((Stage) buttonCancel.getScene().getWindow()).close();
        ServiceState.instance.getShowSRSAppendicesViewService().showView();
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
