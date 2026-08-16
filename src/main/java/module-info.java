module com.bwxor.piejfxsdk {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.bwxor.plugin;
    requires org.apache.pdfbox;
    opens com.bwxor.piejfxsdk to javafx.fxml;
    opens com.bwxor.piejfxsdk.controller.impl to javafx.fxml;
    exports com.bwxor.piejfxsdk;
}