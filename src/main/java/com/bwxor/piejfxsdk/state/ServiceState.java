package com.bwxor.piejfxsdk.state;

import com.bwxor.piejfxsdk.service.*;
import com.bwxor.plugin.service.PluginNotificationService;

public class ServiceState {
    private PluginNotificationService notificationService;
    private ResourceService resourceService;

    // SRS page-navigation services
    private ShowSRSTitlePageViewService showSRSTitlePageViewService;
    private ShowSRSRevisionHistoryViewService showSRSRevisionHistoryViewService;
    private ShowSRSIntroductionViewService showSRSIntroductionViewService;
    private ShowSRSOverallDescriptionViewService showSRSOverallDescriptionViewService;
    private ShowSRSFunctionalRequirementsViewService showSRSFunctionalRequirementsViewService;
    private ShowSRSNonFunctionalRequirementsViewService showSRSNonFunctionalRequirementsViewService;
    private ShowSRSExternalInterfaceRequirementsViewService showSRSExternalInterfaceRequirementsViewService;
    private ShowSRSUseCasesViewService showSRSUseCasesViewService;
    private ShowSRSAppendicesViewService showSRSAppendicesViewService;
    private ShowSRSOutputFolderViewService showSRSOutputFolderViewService;
    private GenerateSRSPDFService generateSRSPDFService;

    // Shared output-folder view service
    private ShowOutputFolderViewService showOutputFolderViewService;

    // Release Notes services
    private ShowReleaseNotesHeaderViewService showReleaseNotesHeaderViewService;
    private ShowReleaseNotesEntriesViewService showReleaseNotesEntriesViewService;
    private GenerateReleaseNotesPDFService generateReleaseNotesPDFService;

    // Change Request services
    private ShowChangeRequestHeaderViewService showChangeRequestHeaderViewService;
    private ShowChangeRequestDetailsViewService showChangeRequestDetailsViewService;
    private GenerateChangeRequestPDFService generateChangeRequestPDFService;

    // Entry sub-view services
    private SRSRevisionEntryViewService srsRevisionEntryViewService;
    private SRSKeyValueEntryViewService srsKeyValueEntryViewService;
    private SRSStringEntryViewService srsStringEntryViewService;
    private SRSRequirementEntryViewService srsRequirementEntryViewService;
    private SRSUseCaseEntryViewService srsUseCaseEntryViewService;
    private SRSTextAreaEditViewService srsTextAreaEditViewService;

    public static final ServiceState instance = new ServiceState();

    private ServiceState() {}

    public PluginNotificationService getNotificationService() { return notificationService; }
    public void setNotificationService(PluginNotificationService v) { this.notificationService = v; }

    public ResourceService getResourceService() { return resourceService; }
    public void setResourceService(ResourceService v) { this.resourceService = v; }

    // SRS
    public ShowSRSTitlePageViewService getShowSRSTitlePageViewService() { return showSRSTitlePageViewService; }
    public void setShowSRSTitlePageViewService(ShowSRSTitlePageViewService v) { this.showSRSTitlePageViewService = v; }

    public ShowSRSRevisionHistoryViewService getShowSRSRevisionHistoryViewService() { return showSRSRevisionHistoryViewService; }
    public void setShowSRSRevisionHistoryViewService(ShowSRSRevisionHistoryViewService v) { this.showSRSRevisionHistoryViewService = v; }

    public ShowSRSIntroductionViewService getShowSRSIntroductionViewService() { return showSRSIntroductionViewService; }
    public void setShowSRSIntroductionViewService(ShowSRSIntroductionViewService v) { this.showSRSIntroductionViewService = v; }

    public ShowSRSOverallDescriptionViewService getShowSRSOverallDescriptionViewService() { return showSRSOverallDescriptionViewService; }
    public void setShowSRSOverallDescriptionViewService(ShowSRSOverallDescriptionViewService v) { this.showSRSOverallDescriptionViewService = v; }

    public ShowSRSFunctionalRequirementsViewService getShowSRSFunctionalRequirementsViewService() { return showSRSFunctionalRequirementsViewService; }
    public void setShowSRSFunctionalRequirementsViewService(ShowSRSFunctionalRequirementsViewService v) { this.showSRSFunctionalRequirementsViewService = v; }

    public ShowSRSNonFunctionalRequirementsViewService getShowSRSNonFunctionalRequirementsViewService() { return showSRSNonFunctionalRequirementsViewService; }
    public void setShowSRSNonFunctionalRequirementsViewService(ShowSRSNonFunctionalRequirementsViewService v) { this.showSRSNonFunctionalRequirementsViewService = v; }

    public ShowSRSExternalInterfaceRequirementsViewService getShowSRSExternalInterfaceRequirementsViewService() { return showSRSExternalInterfaceRequirementsViewService; }
    public void setShowSRSExternalInterfaceRequirementsViewService(ShowSRSExternalInterfaceRequirementsViewService v) { this.showSRSExternalInterfaceRequirementsViewService = v; }

    public ShowSRSUseCasesViewService getShowSRSUseCasesViewService() { return showSRSUseCasesViewService; }
    public void setShowSRSUseCasesViewService(ShowSRSUseCasesViewService v) { this.showSRSUseCasesViewService = v; }

    public ShowSRSAppendicesViewService getShowSRSAppendicesViewService() { return showSRSAppendicesViewService; }
    public void setShowSRSAppendicesViewService(ShowSRSAppendicesViewService v) { this.showSRSAppendicesViewService = v; }

    public ShowSRSOutputFolderViewService getShowSRSOutputFolderViewService() { return showSRSOutputFolderViewService; }
    public void setShowSRSOutputFolderViewService(ShowSRSOutputFolderViewService v) { this.showSRSOutputFolderViewService = v; }

    public GenerateSRSPDFService getGenerateSRSPDFService() { return generateSRSPDFService; }
    public void setGenerateSRSPDFService(GenerateSRSPDFService v) { this.generateSRSPDFService = v; }

    // Shared output folder
    public ShowOutputFolderViewService getShowOutputFolderViewService() { return showOutputFolderViewService; }
    public void setShowOutputFolderViewService(ShowOutputFolderViewService v) { this.showOutputFolderViewService = v; }

    // Release Notes
    public ShowReleaseNotesHeaderViewService getShowReleaseNotesHeaderViewService() { return showReleaseNotesHeaderViewService; }
    public void setShowReleaseNotesHeaderViewService(ShowReleaseNotesHeaderViewService v) { this.showReleaseNotesHeaderViewService = v; }

    public ShowReleaseNotesEntriesViewService getShowReleaseNotesEntriesViewService() { return showReleaseNotesEntriesViewService; }
    public void setShowReleaseNotesEntriesViewService(ShowReleaseNotesEntriesViewService v) { this.showReleaseNotesEntriesViewService = v; }

    public GenerateReleaseNotesPDFService getGenerateReleaseNotesPDFService() { return generateReleaseNotesPDFService; }
    public void setGenerateReleaseNotesPDFService(GenerateReleaseNotesPDFService v) { this.generateReleaseNotesPDFService = v; }

    // Change Request
    public ShowChangeRequestHeaderViewService getShowChangeRequestHeaderViewService() { return showChangeRequestHeaderViewService; }
    public void setShowChangeRequestHeaderViewService(ShowChangeRequestHeaderViewService v) { this.showChangeRequestHeaderViewService = v; }

    public ShowChangeRequestDetailsViewService getShowChangeRequestDetailsViewService() { return showChangeRequestDetailsViewService; }
    public void setShowChangeRequestDetailsViewService(ShowChangeRequestDetailsViewService v) { this.showChangeRequestDetailsViewService = v; }

    public GenerateChangeRequestPDFService getGenerateChangeRequestPDFService() { return generateChangeRequestPDFService; }
    public void setGenerateChangeRequestPDFService(GenerateChangeRequestPDFService v) { this.generateChangeRequestPDFService = v; }

    // Entry sub-view services
    public SRSRevisionEntryViewService getSrsRevisionEntryViewService() { return srsRevisionEntryViewService; }
    public void setSrsRevisionEntryViewService(SRSRevisionEntryViewService v) { this.srsRevisionEntryViewService = v; }

    public SRSKeyValueEntryViewService getSrsKeyValueEntryViewService() { return srsKeyValueEntryViewService; }
    public void setSrsKeyValueEntryViewService(SRSKeyValueEntryViewService v) { this.srsKeyValueEntryViewService = v; }

    public SRSStringEntryViewService getSrsStringEntryViewService() { return srsStringEntryViewService; }
    public void setSrsStringEntryViewService(SRSStringEntryViewService v) { this.srsStringEntryViewService = v; }

    public SRSRequirementEntryViewService getSrsRequirementEntryViewService() { return srsRequirementEntryViewService; }
    public void setSrsRequirementEntryViewService(SRSRequirementEntryViewService v) { this.srsRequirementEntryViewService = v; }

    public SRSUseCaseEntryViewService getSrsUseCaseEntryViewService() { return srsUseCaseEntryViewService; }
    public void setSrsUseCaseEntryViewService(SRSUseCaseEntryViewService v) { this.srsUseCaseEntryViewService = v; }

    public SRSTextAreaEditViewService getSrsTextAreaEditViewService() { return srsTextAreaEditViewService; }
    public void setSrsTextAreaEditViewService(SRSTextAreaEditViewService v) { this.srsTextAreaEditViewService = v; }
}
