package com.google.android.systemui.smartspace;

import android.content.ComponentName;
import android.content.Context;
import android.media.MediaMetadata;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
/* compiled from: KeyguardMediaViewController.kt */
/* loaded from: classes2.dex */
public final class KeyguardMediaViewController {
    public CharSequence artist;
    public final Context context;
    public final ComponentName mediaComponent;
    public final KeyguardMediaViewController$mediaListener$1 mediaListener = new NotificationMediaManager.MediaListener() { // from class: com.google.android.systemui.smartspace.KeyguardMediaViewController$mediaListener$1
        @Override // com.android.systemui.statusbar.NotificationMediaManager.MediaListener
        public final void onPrimaryMetadataOrStateChanged(final MediaMetadata mediaMetadata, final int i) {
            final KeyguardMediaViewController keyguardMediaViewController = KeyguardMediaViewController.this;
            keyguardMediaViewController.uiExecutor.execute(new Runnable() { // from class: com.google.android.systemui.smartspace.KeyguardMediaViewController$mediaListener$1$onPrimaryMetadataOrStateChanged$1
                /* JADX WARN: Removed duplicated region for block: B:27:0x00a8  */
                /* JADX WARN: Removed duplicated region for block: B:33:? A[RETURN, SYNTHETIC] */
                @Override // java.lang.Runnable
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public final void run() {
                    /*
                        r6 = this;
                        com.google.android.systemui.smartspace.KeyguardMediaViewController r0 = com.google.android.systemui.smartspace.KeyguardMediaViewController.this
                        android.media.MediaMetadata r1 = r2
                        int r6 = r3
                        r0.getClass()
                        boolean r6 = com.android.systemui.statusbar.NotificationMediaManager.isPlayingState(r6)
                        r2 = 0
                        if (r6 != 0) goto L1d
                        r0.title = r2
                        r0.artist = r2
                        com.android.systemui.plugins.BcSmartspaceDataPlugin$SmartspaceView r6 = r0.smartspaceView
                        if (r6 == 0) goto Lb3
                        r6.setMediaTarget(r2)
                        goto Lb3
                    L1d:
                        if (r1 == 0) goto L39
                        java.lang.String r6 = "android.media.metadata.TITLE"
                        java.lang.CharSequence r6 = r1.getText(r6)
                        boolean r3 = android.text.TextUtils.isEmpty(r6)
                        if (r3 == 0) goto L3a
                        android.content.Context r6 = r0.context
                        android.content.res.Resources r6 = r6.getResources()
                        r3 = 2131952976(0x7f130550, float:1.954241E38)
                        java.lang.String r6 = r6.getString(r3)
                        goto L3a
                    L39:
                        r6 = r2
                    L3a:
                        if (r1 == 0) goto L43
                        java.lang.String r3 = "android.media.metadata.ARTIST"
                        java.lang.CharSequence r1 = r1.getText(r3)
                        goto L44
                    L43:
                        r1 = r2
                    L44:
                        java.lang.CharSequence r3 = r0.title
                        boolean r3 = android.text.TextUtils.equals(r3, r6)
                        if (r3 == 0) goto L55
                        java.lang.CharSequence r3 = r0.artist
                        boolean r3 = android.text.TextUtils.equals(r3, r1)
                        if (r3 == 0) goto L55
                        goto Lb3
                    L55:
                        r0.title = r6
                        r0.artist = r1
                        if (r6 == 0) goto La5
                        android.app.smartspace.SmartspaceAction$Builder r1 = new android.app.smartspace.SmartspaceAction$Builder
                        java.lang.String r6 = r6.toString()
                        java.lang.String r3 = "deviceMediaTitle"
                        r1.<init>(r3, r6)
                        java.lang.CharSequence r6 = r0.artist
                        android.app.smartspace.SmartspaceAction$Builder r6 = r1.setSubtitle(r6)
                        com.android.systemui.statusbar.NotificationMediaManager r1 = r0.mediaManager
                        android.graphics.drawable.Icon r1 = r1.getMediaIcon()
                        android.app.smartspace.SmartspaceAction$Builder r6 = r6.setIcon(r1)
                        android.app.smartspace.SmartspaceAction r6 = r6.build()
                        com.android.systemui.settings.UserTracker r1 = r0.userTracker
                        int r1 = r1.getUserId()
                        android.os.UserHandle r1 = android.os.UserHandle.of(r1)
                        android.app.smartspace.SmartspaceTarget$Builder r3 = new android.app.smartspace.SmartspaceTarget$Builder
                        android.content.ComponentName r4 = r0.mediaComponent
                        java.lang.String r5 = "deviceMedia"
                        r3.<init>(r5, r4, r1)
                        r1 = 41
                        android.app.smartspace.SmartspaceTarget$Builder r1 = r3.setFeatureType(r1)
                        android.app.smartspace.SmartspaceTarget$Builder r6 = r1.setHeaderAction(r6)
                        android.app.smartspace.SmartspaceTarget r6 = r6.build()
                        com.android.systemui.plugins.BcSmartspaceDataPlugin$SmartspaceView r1 = r0.smartspaceView
                        if (r1 == 0) goto La5
                        r1.setMediaTarget(r6)
                        kotlin.Unit r6 = kotlin.Unit.INSTANCE
                        goto La6
                    La5:
                        r6 = r2
                    La6:
                        if (r6 != 0) goto Lb3
                        r0.title = r2
                        r0.artist = r2
                        com.android.systemui.plugins.BcSmartspaceDataPlugin$SmartspaceView r6 = r0.smartspaceView
                        if (r6 == 0) goto Lb3
                        r6.setMediaTarget(r2)
                    Lb3:
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.smartspace.KeyguardMediaViewController$mediaListener$1$onPrimaryMetadataOrStateChanged$1.run():void");
                }
            });
        }
    };
    public final NotificationMediaManager mediaManager;
    public final BcSmartspaceDataPlugin plugin;
    public BcSmartspaceDataPlugin.SmartspaceView smartspaceView;
    public CharSequence title;
    public final DelayableExecutor uiExecutor;
    public final UserTracker userTracker;

    @VisibleForTesting
    public static /* synthetic */ void getSmartspaceView$annotations() {
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [com.google.android.systemui.smartspace.KeyguardMediaViewController$mediaListener$1] */
    public KeyguardMediaViewController(Context context, UserTracker userTracker, BcSmartspaceDataPlugin bcSmartspaceDataPlugin, DelayableExecutor delayableExecutor, NotificationMediaManager notificationMediaManager) {
        this.context = context;
        this.userTracker = userTracker;
        this.plugin = bcSmartspaceDataPlugin;
        this.uiExecutor = delayableExecutor;
        this.mediaManager = notificationMediaManager;
        this.mediaComponent = new ComponentName(context, KeyguardMediaViewController.class);
    }
}
