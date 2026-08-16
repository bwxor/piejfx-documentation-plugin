package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import com.bwxor.piejfxsdk.dto.SRSDocumentOverallDescription;
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

import java.util.LinkedHashMap;
import java.util.Map;

public class SRSOverallDescriptionViewController extends MovableViewController {

    private static final int PREVIEW_MAX_LENGTH = 60;
    private static final String EMPTY_LABEL = "Empty";

    @FXML private Label windowTitle;
    @FXML private Label productPerspectivePreview;
    @FXML private ListView<Pair<String, String>> userClassesList;
    @FXML private Label operatingEnvironmentPreview;
    @FXML private Label constraintsPreview;
    @FXML private Button buttonCancel;

    private String productPerspectiveValue = "";
    private String operatingEnvironmentValue = "";
    private String constraintsValue = "";

    private final ObservableList<Pair<String, String>> userClasses = FXCollections.observableArrayList();

    public void setWindowTitle(String title) {
        this.windowTitle.setText(title);
    }

    @FXML
    public void initialize() {
        userClassesList.setItems(userClasses);
        userClassesList.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(Pair<String, String> item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getKey());
            }
        });
    }

    public void loadData() {
        SRSDocumentOverallDescription desc = SRSDocumentState.instance.getSrsDocumentOverallDescription();
        if (desc != null) {
            productPerspectiveValue = desc.productPerspective() != null ? desc.productPerspective() : "";
            operatingEnvironmentValue = desc.operatingEnvironment() != null ? desc.operatingEnvironment() : "";
            constraintsValue = desc.constraints() != null ? desc.constraints() : "";
            userClasses.clear();
            if (desc.userClassesAndCharacteristics() != null) {
                desc.userClassesAndCharacteristics().forEach((k, v) -> userClasses.add(new Pair<>(k, v)));
            }
        }
        updatePreviews();
    }

    // --- Text area edits ---

    @FXML
    public void onEditProductPerspectiveClick() {
        ServiceState.instance.getSrsTextAreaEditViewService()
                .showView("Product Perspective", productPerspectiveValue,
                        value -> { productPerspectiveValue = value; updatePreviews(); });
    }

    @FXML
    public void onEditOperatingEnvironmentClick() {
        ServiceState.instance.getSrsTextAreaEditViewService()
                .showView("Operating Environment", operatingEnvironmentValue,
                        value -> { operatingEnvironmentValue = value; updatePreviews(); });
    }

    @FXML
    public void onEditConstraintsClick() {
        ServiceState.instance.getSrsTextAreaEditViewService()
                .showView("Constraints", constraintsValue,
                        value -> { constraintsValue = value; updatePreviews(); });
    }

    private void updatePreviews() {
        productPerspectivePreview.setText(preview(productPerspectiveValue));
        operatingEnvironmentPreview.setText(preview(operatingEnvironmentValue));
        constraintsPreview.setText(preview(constraintsValue));
    }

    private String preview(String value) {
        if (value == null || value.isBlank()) return EMPTY_LABEL;
        String flat = value.replace('\n', ' ');
        return flat.length() > PREVIEW_MAX_LENGTH ? flat.substring(0, PREVIEW_MAX_LENGTH) + "..." : flat;
    }

    // --- User classes list ---

    @FXML
    public void onAddUserClassClick() {
        ServiceState.instance.getSrsKeyValueEntryViewService()
                .showView("Add User Class", "User Class", "Characteristics", null, userClasses::add);
    }

    @FXML
    public void onListMouseClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            Pair<String, String> selected = userClassesList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                ServiceState.instance.getSrsKeyValueEntryViewService()
                        .showView("Edit User Class", "User Class", "Characteristics", selected,
                                updated -> userClasses.set(userClasses.indexOf(selected), updated));
            }
        }
    }

    @FXML
    public void onRemoveUserClassClick() {
        Pair<String, String> selected = userClassesList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            ServiceState.instance.getNotificationService().showNotificationOk("Please select a user class to remove.");
            return;
        }
        userClasses.remove(selected);
    }

    // --- Navigation ---

    @FXML
    public void onBackButtonClick() {
        ((Stage) buttonCancel.getScene().getWindow()).close();
        ServiceState.instance.getShowSRSIntroductionViewService().showView();
    }

    @FXML
    public void onNextButtonClick() {
        Map<String, String> userClassesMap = new LinkedHashMap<>();
        userClasses.forEach(p -> userClassesMap.put(p.getKey(), p.getValue()));
        SRSDocumentState.instance.setSrsDocumentOverallDescription(new SRSDocumentOverallDescription(
                productPerspectiveValue,
                userClassesMap,
                operatingEnvironmentValue,
                constraintsValue
        ));
        ((Stage) buttonCancel.getScene().getWindow()).close();
        ServiceState.instance.getShowSRSFunctionalRequirementsViewService().showView();
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
