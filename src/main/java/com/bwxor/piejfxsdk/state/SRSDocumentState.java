package com.bwxor.piejfxsdk.state;

import com.bwxor.piejfxsdk.dto.*;
import com.bwxor.piejfxsdk.type.WatermarkPosition;

import java.io.File;

public class SRSDocumentState {
    public static final SRSDocumentState instance = new SRSDocumentState();
    private SRSDocumentTitlePage srsDocumentTitlePage;
    private SRSDocumentRevisionHistory srsDocumentRevisionHistory;
    private SRSDocumentIntroduction srsDocumentIntroduction;
    private SRSDocumentOverallDescription srsDocumentOverallDescription;
    private SRSDocumentFunctionalRequirements srsDocumentFunctionalRequirements;
    private SRSDocumentNonFunctionalRequirements srsDocumentNonFunctionalRequirements;
    private SRSDocumentExternalInterfaceRequirements srsDocumentExternalInterfaceRequirements;
    private SRSDocumentUseCases srsDocumentUseCases;
    private SRSDocumentAppendices srsDocumentAppendices;
    private File watermarkFile;
    private WatermarkPosition watermarkPosition;
    private double watermarkScale;

    private SRSDocumentState() {}

    public SRSDocumentTitlePage getSrsDocumentTitlePage() {
        return srsDocumentTitlePage;
    }

    public void setSrsDocumentTitlePage(SRSDocumentTitlePage srsDocumentTitlePage) {
        this.srsDocumentTitlePage = srsDocumentTitlePage;
    }

    public SRSDocumentRevisionHistory getSrsDocumentRevisionHistory() {
        return srsDocumentRevisionHistory;
    }

    public void setSrsDocumentRevisionHistory(SRSDocumentRevisionHistory srsDocumentRevisionHistory) {
        this.srsDocumentRevisionHistory = srsDocumentRevisionHistory;
    }

    public SRSDocumentIntroduction getSrsDocumentIntroduction() {
        return srsDocumentIntroduction;
    }

    public void setSrsDocumentIntroduction(SRSDocumentIntroduction srsDocumentIntroduction) {
        this.srsDocumentIntroduction = srsDocumentIntroduction;
    }

    public SRSDocumentOverallDescription getSrsDocumentOverallDescription() {
        return srsDocumentOverallDescription;
    }

    public void setSrsDocumentOverallDescription(SRSDocumentOverallDescription srsDocumentOverallDescription) {
        this.srsDocumentOverallDescription = srsDocumentOverallDescription;
    }

    public SRSDocumentFunctionalRequirements getSrsDocumentFunctionalRequirements() {
        return srsDocumentFunctionalRequirements;
    }

    public void setSrsDocumentFunctionalRequirements(SRSDocumentFunctionalRequirements srsDocumentFunctionalRequirements) {
        this.srsDocumentFunctionalRequirements = srsDocumentFunctionalRequirements;
    }

    public SRSDocumentNonFunctionalRequirements getSrsDocumentNonFunctionalRequirements() {
        return srsDocumentNonFunctionalRequirements;
    }

    public void setSrsDocumentNonFunctionalRequirements(SRSDocumentNonFunctionalRequirements srsDocumentNonFunctionalRequirements) {
        this.srsDocumentNonFunctionalRequirements = srsDocumentNonFunctionalRequirements;
    }

    public SRSDocumentExternalInterfaceRequirements getSrsDocumentExternalInterfaceRequirements() {
        return srsDocumentExternalInterfaceRequirements;
    }

    public void setSrsDocumentExternalInterfaceRequirements(SRSDocumentExternalInterfaceRequirements srsDocumentExternalInterfaceRequirements) {
        this.srsDocumentExternalInterfaceRequirements = srsDocumentExternalInterfaceRequirements;
    }

    public SRSDocumentUseCases getSrsDocumentUseCases() {
        return srsDocumentUseCases;
    }

    public void setSrsDocumentUseCases(SRSDocumentUseCases srsDocumentUseCases) {
        this.srsDocumentUseCases = srsDocumentUseCases;
    }

    public SRSDocumentAppendices getSrsDocumentAppendices() {
        return srsDocumentAppendices;
    }

    public void setSrsDocumentAppendices(SRSDocumentAppendices srsDocumentAppendices) {
        this.srsDocumentAppendices = srsDocumentAppendices;
    }

    public File getWatermarkFile() {
        return watermarkFile;
    }

    public void setWatermarkFile(File watermarkFile) {
        this.watermarkFile = watermarkFile;
    }

    public WatermarkPosition getWatermarkPosition() {
        return watermarkPosition;
    }

    public void setWatermarkPosition(WatermarkPosition watermarkPosition) {
        this.watermarkPosition = watermarkPosition;
    }

    public double getWatermarkScale() {
        return watermarkScale;
    }

    public void setWatermarkScale(double watermarkScale) {
        this.watermarkScale = watermarkScale;
    }
}
