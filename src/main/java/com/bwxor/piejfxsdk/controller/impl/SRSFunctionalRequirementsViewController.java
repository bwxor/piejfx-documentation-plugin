package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import com.bwxor.piejfxsdk.dto.SRSDocumentFunctionalRequirements;
import com.bwxor.piejfxsdk.dto.SRSRequirement;
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

public class SRSFunctionalRequirementsViewController extends MovableViewController {

    @FXML private Label windowTitle;
    @FXML private ListView<SRSRequirement> requirementsList;
    @FXML private Button buttonCancel;

    private final ObservableList<SRSRequirement> requirements = FXCollections.observableArrayList();

    public void setWindowTitle(String title) {
        this.windowTitle.setText(title);
    }

    @FXML
    public void initialize() {
        requirementsList.setItems(requirements);
        requirementsList.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(SRSRequirement item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.id() + " — " + item.requirement());
            }
        });
    }

    public void loadData() {
        SRSDocumentFunctionalRequirements funcReqs = SRSDocumentState.instance.getSrsDocumentFunctionalRequirements();
        requirements.clear();
        if (funcReqs != null && funcReqs.requirements() != null) {
            requirements.addAll(funcReqs.requirements());
        }
    }

    @FXML
    public void onAddButtonClick() {
        ServiceState.instance.getSrsRequirementEntryViewService()
                .showView("Add Requirement", null, requirements::add);
    }

    @FXML
    public void onListMouseClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            SRSRequirement selected = requirementsList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                ServiceState.instance.getSrsRequirementEntryViewService()
                        .showView("Edit Requirement", selected,
                                updated -> requirements.set(requirements.indexOf(selected), updated));
            }
        }
    }

    @FXML
    public void onRemoveButtonClick() {
        SRSRequirement selected = requirementsList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            ServiceState.instance.getNotificationService().showNotificationOk("Please select a requirement to remove.");
            return;
        }
        requirements.remove(selected);
    }

    @FXML
    public void onBackButtonClick() {
        ((Stage) buttonCancel.getScene().getWindow()).close();
        ServiceState.instance.getShowSRSOverallDescriptionViewService().showView();
    }

    @FXML
    public void onNextButtonClick() {
        SRSDocumentState.instance.setSrsDocumentFunctionalRequirements(
                new SRSDocumentFunctionalRequirements(new ArrayList<>(requirements)));
        ((Stage) buttonCancel.getScene().getWindow()).close();
        ServiceState.instance.getShowSRSNonFunctionalRequirementsViewService().showView();
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
