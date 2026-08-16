package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import com.bwxor.piejfxsdk.dto.SRSDocumentExternalInterfaceRequirements;
import com.bwxor.piejfxsdk.state.SRSDocumentState;
import com.bwxor.piejfxsdk.state.ServiceState;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class SRSExternalInterfaceRequirementsViewController extends MovableViewController {

    private static final int PREVIEW_MAX_LENGTH = 60;
    private static final String EMPTY_LABEL = "Empty";

    @FXML private Label windowTitle;
    @FXML private Label userInterfacesPreview;
    @FXML private Label hardwareInterfacesPreview;
    @FXML private Label softwareInterfacesPreview;
    @FXML private Label communicationInterfacesPreview;
    @FXML private Button buttonCancel;

    private String userInterfacesValue = "";
    private String hardwareInterfacesValue = "";
    private String softwareInterfacesValue = "";
    private String communicationInterfacesValue = "";

    public void setWindowTitle(String title) {
        this.windowTitle.setText(title);
    }

    public void loadData() {
        SRSDocumentExternalInterfaceRequirements eir = SRSDocumentState.instance.getSrsDocumentExternalInterfaceRequirements();
        if (eir != null) {
            userInterfacesValue = eir.userInterfaces() != null ? eir.userInterfaces() : "";
            hardwareInterfacesValue = eir.hardwareInterfaces() != null ? eir.hardwareInterfaces() : "";
            softwareInterfacesValue = eir.softwareInterfaces() != null ? eir.softwareInterfaces() : "";
            communicationInterfacesValue = eir.communicationInterfaces() != null ? eir.communicationInterfaces() : "";
        }
        updatePreviews();
    }

    // --- Text area edits ---

    @FXML public void onEditUserInterfacesClick() {
        ServiceState.instance.getSrsTextAreaEditViewService()
                .showView("User Interfaces", userInterfacesValue,
                        v -> { userInterfacesValue = v; updatePreviews(); });
    }

    @FXML public void onEditHardwareInterfacesClick() {
        ServiceState.instance.getSrsTextAreaEditViewService()
                .showView("Hardware Interfaces", hardwareInterfacesValue,
                        v -> { hardwareInterfacesValue = v; updatePreviews(); });
    }

    @FXML public void onEditSoftwareInterfacesClick() {
        ServiceState.instance.getSrsTextAreaEditViewService()
                .showView("Software Interfaces", softwareInterfacesValue,
                        v -> { softwareInterfacesValue = v; updatePreviews(); });
    }

    @FXML public void onEditCommunicationInterfacesClick() {
        ServiceState.instance.getSrsTextAreaEditViewService()
                .showView("Communication Interfaces", communicationInterfacesValue,
                        v -> { communicationInterfacesValue = v; updatePreviews(); });
    }

    private void updatePreviews() {
        userInterfacesPreview.setText(preview(userInterfacesValue));
        hardwareInterfacesPreview.setText(preview(hardwareInterfacesValue));
        softwareInterfacesPreview.setText(preview(softwareInterfacesValue));
        communicationInterfacesPreview.setText(preview(communicationInterfacesValue));
    }

    private String preview(String value) {
        if (value == null || value.isBlank()) return EMPTY_LABEL;
        String flat = value.replace('\n', ' ');
        return flat.length() > PREVIEW_MAX_LENGTH ? flat.substring(0, PREVIEW_MAX_LENGTH) + "..." : flat;
    }

    // --- Navigation ---

    @FXML
    public void onBackButtonClick() {
        ((Stage) buttonCancel.getScene().getWindow()).close();
        ServiceState.instance.getShowSRSNonFunctionalRequirementsViewService().showView();
    }

    @FXML
    public void onNextButtonClick() {
        SRSDocumentState.instance.setSrsDocumentExternalInterfaceRequirements(new SRSDocumentExternalInterfaceRequirements(
                userInterfacesValue,
                hardwareInterfacesValue,
                softwareInterfacesValue,
                communicationInterfacesValue
        ));
        ((Stage) buttonCancel.getScene().getWindow()).close();
        ServiceState.instance.getShowSRSUseCasesViewService().showView();
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
