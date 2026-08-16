package com.bwxor.piejfxsdk.controller.impl;

import com.bwxor.piejfxsdk.controller.MovableViewController;
import com.bwxor.piejfxsdk.dto.SRSDocumentNonFunctionalRequirements;
import com.bwxor.piejfxsdk.state.SRSDocumentState;
import com.bwxor.piejfxsdk.state.ServiceState;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class SRSNonFunctionalRequirementsViewController extends MovableViewController {

    private static final int PREVIEW_MAX_LENGTH = 60;
    private static final String EMPTY_LABEL = "Empty";

    @FXML private Label windowTitle;
    @FXML private Label performancePreview;
    @FXML private Label securityPreview;
    @FXML private Label usabilityPreview;
    @FXML private Label reliabilityPreview;
    @FXML private Label scalabilityPreview;
    @FXML private Label compliancePreview;
    @FXML private Button buttonCancel;

    private String performanceValue = "";
    private String securityValue = "";
    private String usabilityValue = "";
    private String reliabilityValue = "";
    private String scalabilityValue = "";
    private String complianceValue = "";

    public void setWindowTitle(String title) {
        this.windowTitle.setText(title);
    }

    public void loadData() {
        SRSDocumentNonFunctionalRequirements nfr = SRSDocumentState.instance.getSrsDocumentNonFunctionalRequirements();
        if (nfr != null) {
            performanceValue = nfr.performanceRequirements() != null ? nfr.performanceRequirements() : "";
            securityValue = nfr.securityRequirements() != null ? nfr.securityRequirements() : "";
            usabilityValue = nfr.usabilityRequirements() != null ? nfr.usabilityRequirements() : "";
            reliabilityValue = nfr.reliabilityRequirements() != null ? nfr.reliabilityRequirements() : "";
            scalabilityValue = nfr.scalabilityRequirements() != null ? nfr.scalabilityRequirements() : "";
            complianceValue = nfr.complianceRequirements() != null ? nfr.complianceRequirements() : "";
        }
        updatePreviews();
    }

    // --- Text area edits ---

    @FXML public void onEditPerformanceClick() {
        ServiceState.instance.getSrsTextAreaEditViewService()
                .showView("Performance Requirements", performanceValue,
                        v -> { performanceValue = v; updatePreviews(); });
    }

    @FXML public void onEditSecurityClick() {
        ServiceState.instance.getSrsTextAreaEditViewService()
                .showView("Security Requirements", securityValue,
                        v -> { securityValue = v; updatePreviews(); });
    }

    @FXML public void onEditUsabilityClick() {
        ServiceState.instance.getSrsTextAreaEditViewService()
                .showView("Usability Requirements", usabilityValue,
                        v -> { usabilityValue = v; updatePreviews(); });
    }

    @FXML public void onEditReliabilityClick() {
        ServiceState.instance.getSrsTextAreaEditViewService()
                .showView("Reliability Requirements", reliabilityValue,
                        v -> { reliabilityValue = v; updatePreviews(); });
    }

    @FXML public void onEditScalabilityClick() {
        ServiceState.instance.getSrsTextAreaEditViewService()
                .showView("Scalability Requirements", scalabilityValue,
                        v -> { scalabilityValue = v; updatePreviews(); });
    }

    @FXML public void onEditComplianceClick() {
        ServiceState.instance.getSrsTextAreaEditViewService()
                .showView("Compliance Requirements", complianceValue,
                        v -> { complianceValue = v; updatePreviews(); });
    }

    private void updatePreviews() {
        performancePreview.setText(preview(performanceValue));
        securityPreview.setText(preview(securityValue));
        usabilityPreview.setText(preview(usabilityValue));
        reliabilityPreview.setText(preview(reliabilityValue));
        scalabilityPreview.setText(preview(scalabilityValue));
        compliancePreview.setText(preview(complianceValue));
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
        ServiceState.instance.getShowSRSFunctionalRequirementsViewService().showView();
    }

    @FXML
    public void onNextButtonClick() {
        SRSDocumentState.instance.setSrsDocumentNonFunctionalRequirements(new SRSDocumentNonFunctionalRequirements(
                performanceValue,
                securityValue,
                usabilityValue,
                reliabilityValue,
                scalabilityValue,
                complianceValue
        ));
        ((Stage) buttonCancel.getScene().getWindow()).close();
        ServiceState.instance.getShowSRSExternalInterfaceRequirementsViewService().showView();
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
