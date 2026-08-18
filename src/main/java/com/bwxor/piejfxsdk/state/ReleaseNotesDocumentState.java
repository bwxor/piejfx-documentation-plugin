package com.bwxor.piejfxsdk.state;

import com.bwxor.piejfxsdk.dto.ReleaseNotesDocument;
import com.bwxor.piejfxsdk.type.WatermarkPosition;

import java.io.File;

public class ReleaseNotesDocumentState {
    public static final ReleaseNotesDocumentState instance = new ReleaseNotesDocumentState();

    private ReleaseNotesDocument document;
    private File watermarkFile;
    private WatermarkPosition watermarkPosition;
    private double watermarkScale;

    private ReleaseNotesDocumentState() {}

    public ReleaseNotesDocument getDocument() { return document; }
    public void setDocument(ReleaseNotesDocument document) { this.document = document; }

    public File getWatermarkFile() { return watermarkFile; }
    public void setWatermarkFile(File watermarkFile) { this.watermarkFile = watermarkFile; }

    public WatermarkPosition getWatermarkPosition() { return watermarkPosition; }
    public void setWatermarkPosition(WatermarkPosition watermarkPosition) { this.watermarkPosition = watermarkPosition; }

    public double getWatermarkScale() { return watermarkScale; }
    public void setWatermarkScale(double watermarkScale) { this.watermarkScale = watermarkScale; }
}
