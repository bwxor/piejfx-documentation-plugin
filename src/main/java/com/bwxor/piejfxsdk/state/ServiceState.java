package com.bwxor.piejfxsdk.state;

import com.bwxor.piejfxsdk.service.CreateGenerateSRSDocumentViewService;
import com.bwxor.piejfxsdk.service.ResourceService;
import com.bwxor.plugin.service.PluginFileService;
import com.bwxor.plugin.service.PluginNotificationService;
import com.bwxor.plugin.service.PluginTerminalTabPaneService;

public class ServiceState {
    private PluginNotificationService notificationService;
    private ResourceService resourceService;
    private CreateGenerateSRSDocumentViewService createGenerateSRSDocumentViewService;
    public static final ServiceState instance = new ServiceState();

    private ServiceState(){}

    public PluginNotificationService getNotificationService() {
        return notificationService;
    }

    public void setNotificationService(PluginNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public ResourceService getResourceService() {
        return resourceService;
    }

    public void setResourceService(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    public CreateGenerateSRSDocumentViewService getCreateGenerateSRSDocumentViewService() {
        return createGenerateSRSDocumentViewService;
    }

    public void setCreateGenerateSRSDocumentViewService(CreateGenerateSRSDocumentViewService createGenerateSRSDocumentViewService) {
        this.createGenerateSRSDocumentViewService = createGenerateSRSDocumentViewService;
    }
}
