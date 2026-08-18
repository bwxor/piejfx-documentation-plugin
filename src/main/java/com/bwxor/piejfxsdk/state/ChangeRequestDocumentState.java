package com.bwxor.piejfxsdk.state;

import com.bwxor.piejfxsdk.dto.ChangeRequestDocument;
import com.bwxor.piejfxsdk.type.WatermarkPosition;

import java.io.File;

public class ChangeRequestDocumentState {
    public static final ChangeRequestDocumentState instance = new ChangeRequestDocumentState();

    private ChangeRequestDocument document;
    private File watermarkFile;
    private WatermarkPosition watermarkPosition;
    private double watermarkScale;

    private ChangeRequestDocumentState() {}

    public ChangeRequestDocument getDocument() { return document; }
    public void setDocument(ChangeRequestDocument document) { this.document = document; }

    public File getWatermarkFile() { return watermarkFile; }
    public void setWatermarkFile(File watermarkFile) { this.watermarkFile = watermarkFile; }

    public WatermarkPosition getWatermarkPosition() { return watermarkPosition; }
    public void setWatermarkPosition(WatermarkPosition watermarkPosition) { this.watermarkPosition = watermarkPosition; }

    public double getWatermarkScale() { return watermarkScale; }
    public void setWatermarkScale(double watermarkScale) { this.watermarkScale = watermarkScale; }
}
