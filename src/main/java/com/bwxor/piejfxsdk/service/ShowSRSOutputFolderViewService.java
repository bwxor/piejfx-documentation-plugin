package com.bwxor.piejfxsdk.service;

import com.bwxor.piejfxsdk.state.SRSDocumentState;
import com.bwxor.piejfxsdk.state.ServiceState;
import com.bwxor.piejfxsdk.type.WatermarkPosition;

/**
 * Bridges the SRS wizard's last step to the shared output-folder view.
 * Injects SRS-specific back navigation and PDF generation callbacks.
 */
public class ShowSRSOutputFolderViewService {

    public void showView() {
        SRSDocumentState srsState = SRSDocumentState.instance;

        ServiceState.instance.getShowOutputFolderViewService().showView(
                "Generate SRS Document (10/10)",
                () -> ServiceState.instance.getShowSRSAppendicesViewService().showView(),
                outputFolder -> ServiceState.instance.getGenerateSRSPDFService().generatePdf(outputFolder),
                (file, position) -> {
                    srsState.setWatermarkFile(file);
                    srsState.setWatermarkPosition(file == null ? WatermarkPosition.UNKNOWN : position);
                },
                srsState::setWatermarkScale
        );
    }
}
