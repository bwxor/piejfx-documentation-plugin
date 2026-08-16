package com.bwxor.piejfxsdk;

import com.bwxor.piejfxsdk.service.*;
import com.bwxor.piejfxsdk.state.ServiceState;
import com.bwxor.piejfxsdk.state.StylesheetState;
import com.bwxor.plugin.Plugin;
import com.bwxor.plugin.input.PluginContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.net.URL;

public class DocumentationPlugin implements Plugin {
    private PluginContext pluginContext;

    @Override
    public void onLoad(PluginContext pluginContext) {
        this.pluginContext = pluginContext;

        ServiceState serviceState = ServiceState.instance;
        StylesheetState stylesheetState = StylesheetState.instance;

        serviceState.setNotificationService(pluginContext.getServiceContainer().getNotificationService());
        serviceState.setResourceService(new ResourceService());
        serviceState.setShowSRSTitlePageViewService(new ShowSRSTitlePageViewService());
        serviceState.setShowSRSRevisionHistoryViewService(new ShowSRSRevisionHistoryViewService());
        serviceState.setShowSRSIntroductionViewService(new ShowSRSIntroductionViewService());
        serviceState.setShowSRSOverallDescriptionViewService(new ShowSRSOverallDescriptionViewService());
        serviceState.setShowSRSFunctionalRequirementsViewService(new ShowSRSFunctionalRequirementsViewService());
        serviceState.setShowSRSNonFunctionalRequirementsViewService(new ShowSRSNonFunctionalRequirementsViewService());
        serviceState.setShowSRSExternalInterfaceRequirementsViewService(new ShowSRSExternalInterfaceRequirementsViewService());
        serviceState.setShowSRSUseCasesViewService(new ShowSRSUseCasesViewService());
        serviceState.setShowSRSAppendicesViewService(new ShowSRSAppendicesViewService());
        serviceState.setShowSRSOutputFolderViewService(new ShowSRSOutputFolderViewService());
        serviceState.setGenerateSRSPDFService(new GenerateSRSPDFService());
        serviceState.setSrsRevisionEntryViewService(new SRSRevisionEntryViewService());
        serviceState.setSrsKeyValueEntryViewService(new SRSKeyValueEntryViewService());
        serviceState.setSrsStringEntryViewService(new SRSStringEntryViewService());
        serviceState.setSrsRequirementEntryViewService(new SRSRequirementEntryViewService());
        serviceState.setSrsUseCaseEntryViewService(new SRSUseCaseEntryViewService());
        serviceState.setSrsTextAreaEditViewService(new SRSTextAreaEditViewService());

        stylesheetState.setThemeURL(pluginContext.getStylesheets().getThemeURL());
        stylesheetState.setDefaultStylesheetURL(pluginContext.getStylesheets().getDefaultStylesURL());
        stylesheetState.setDefaultMaximizedStylesheetURL(pluginContext.getStylesheets().getDefaultMaximizedURL());

        Menu menu = new Menu("Documentation");
        var generateSRSDocumentMenuItem = new MenuItem("Generate SRS Document");
        menu.getItems().add(generateSRSDocumentMenuItem);
        generateSRSDocumentMenuItem.setOnAction(_ -> serviceState.getShowSRSTitlePageViewService().showView());
        pluginContext.getApplicationWindow().getMenuBar().getMenus().add(menu);
    }

    @Override
    public void onKeyPress(KeyEvent keyEvent) {
        // ToDo: Edit or remove
    }

    @Override
    public void onSaveFile(File file) {
        // ToDo: Handle hook
    }

    @Override
    public void onOpenFile(File file) {
        // ToDo: Handle hook
    }

    @Override
    public void onOpenFolder(java.io.File file) {
        // ToDo: Handle hook
    }

    @Override
    public void onCreateFile(File file) {
        // ToDo: Handle hook
    }

    @Override
    public void onCreateFolder(File file) {
        // ToDo: Handle hook
    }

    @java.lang.Override
    public void onRenameFile(java.io.File file) {
        // ToDo: Handle hook
    }

    @Override
    public void onDeleteFile(File file) {
        // ToDo: Handle hook
    }

    @Override
    public void onThemeChange(URL url) {
        StylesheetState.instance.setThemeURL(url);
    }
}
