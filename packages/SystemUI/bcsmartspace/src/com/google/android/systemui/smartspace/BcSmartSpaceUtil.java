package com.google.android.systemui.smartspace;

import android.app.PendingIntent;
import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.app.smartspace.SmartspaceTargetEvent;
import android.app.smartspace.uitemplatedata.TapAction;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import com.android.systemui.R;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.google.android.systemui.smartspace.BcSmartSpaceUtil;
import com.google.android.systemui.smartspace.logging.BcSmartspaceCardLogger;
import com.google.android.systemui.smartspace.logging.BcSmartspaceCardLoggingInfo;
import com.google.android.systemui.smartspace.logging.BcSmartspaceSubcardLoggingInfo;
/* loaded from: classes2.dex */
public final class BcSmartSpaceUtil {
    public static FalsingManager sFalsingManager;
    public static BcSmartspaceDataPlugin.IntentStarter sIntentStarter;

    /* renamed from: com.google.android.systemui.smartspace.BcSmartSpaceUtil$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public final class AnonymousClass1 implements BcSmartspaceDataPlugin.IntentStarter {
        public final /* synthetic */ String val$tag;

        public AnonymousClass1(String str) {
            this.val$tag = str;
        }

        @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.IntentStarter
        public final void startIntent(View view, Intent intent, boolean z) {
            try {
                view.getContext().startActivity(intent);
            } catch (ActivityNotFoundException | NullPointerException | SecurityException e) {
                Log.e(this.val$tag, "Cannot invoke smartspace intent", e);
            }
        }

        @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.IntentStarter
        public final void startPendingIntent(PendingIntent pendingIntent, boolean z) {
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                Log.e(this.val$tag, "Cannot invoke canceled smartspace intent", e);
            }
        }
    }

    public static void setOnClickListener(View view, final SmartspaceTarget smartspaceTarget, final SmartspaceAction smartspaceAction, final BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier, final String str, final BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo, final int i) {
        if (view != null && smartspaceAction != null) {
            final boolean z = smartspaceAction.getExtras() != null && smartspaceAction.getExtras().getBoolean("show_on_lockscreen");
            final boolean z2 = smartspaceAction.getIntent() == null && smartspaceAction.getPendingIntent() == null;
            BcSmartspaceDataPlugin.IntentStarter intentStarter = sIntentStarter;
            if (intentStarter == null) {
                intentStarter = new AnonymousClass1(str);
            }
            final BcSmartspaceDataPlugin.IntentStarter intentStarter2 = intentStarter;
            view.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.systemui.smartspace.BcSmartSpaceUtil$$ExternalSyntheticLambda1
                public final /* synthetic */ View.OnClickListener f$6 = null;

                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo2 = BcSmartspaceCardLoggingInfo.this;
                    int i2 = i;
                    boolean z3 = z2;
                    BcSmartspaceDataPlugin.IntentStarter intentStarter3 = intentStarter2;
                    SmartspaceAction smartspaceAction2 = smartspaceAction;
                    boolean z4 = z;
                    View.OnClickListener onClickListener = this.f$6;
                    BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier2 = smartspaceEventNotifier;
                    String str2 = str;
                    SmartspaceTarget smartspaceTarget2 = smartspaceTarget;
                    FalsingManager falsingManager = BcSmartSpaceUtil.sFalsingManager;
                    if (falsingManager == null || !falsingManager.isFalseTap(1)) {
                        if (bcSmartspaceCardLoggingInfo2 != null) {
                            BcSmartspaceSubcardLoggingInfo bcSmartspaceSubcardLoggingInfo = bcSmartspaceCardLoggingInfo2.mSubcardInfo;
                            if (bcSmartspaceSubcardLoggingInfo != null) {
                                bcSmartspaceSubcardLoggingInfo.mClickedSubcardIndex = i2;
                            }
                            BcSmartspaceCardLogger.log(BcSmartspaceEvent.SMARTSPACE_CARD_CLICK, bcSmartspaceCardLoggingInfo2);
                        }
                        if (!z3) {
                            intentStarter3.startFromAction(smartspaceAction2, view2, z4);
                        }
                        if (onClickListener != null) {
                            onClickListener.onClick(view2);
                        }
                        if (smartspaceEventNotifier2 == null) {
                            Log.w(str2, "Cannot notify target interaction smartspace event: event notifier null.");
                        } else {
                            smartspaceEventNotifier2.notifySmartspaceEvent(new SmartspaceTargetEvent.Builder(1).setSmartspaceTarget(smartspaceTarget2).setSmartspaceActionId(smartspaceAction2.getId()).build());
                        }
                    }
                }
            });
            return;
        }
        Log.e(str, "No tap action can be set up");
    }

    public static String getDimensionRatio(Bundle bundle) {
        if (bundle.containsKey("imageRatioWidth") && bundle.containsKey("imageRatioHeight")) {
            int i = bundle.getInt("imageRatioWidth");
            int i2 = bundle.getInt("imageRatioHeight");
            if (i > 0 && i2 > 0) {
                return i + ":" + i2;
            }
            return null;
        }
        return null;
    }

    public static Drawable getIconDrawable(Context context, Icon icon) {
        Drawable bitmapDrawable;
        if (icon == null) {
            return null;
        }
        if (icon.getType() != 1 && icon.getType() != 5) {
            bitmapDrawable = icon.loadDrawable(context);
        } else {
            bitmapDrawable = new BitmapDrawable(context.getResources(), icon.getBitmap());
        }
        if (bitmapDrawable != null) {
            int dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.enhanced_smartspace_icon_size);
            bitmapDrawable.setBounds(0, 0, dimensionPixelSize, dimensionPixelSize);
        }
        return bitmapDrawable;
    }

    public static Intent getOpenCalendarIntent() {
        return new Intent("android.intent.action.VIEW").setData(ContentUris.appendId(CalendarContract.CONTENT_URI.buildUpon().appendPath("time"), System.currentTimeMillis()).build()).addFlags(270532608);
    }

    public static void setOnClickListener$1(View view, final SmartspaceTarget smartspaceTarget, final TapAction tapAction, final BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier, final String str, final BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo, final int i) {
        if (view != null && tapAction != null) {
            final boolean shouldShowOnLockscreen = tapAction.shouldShowOnLockscreen();
            view.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.systemui.smartspace.BcSmartSpaceUtil$$ExternalSyntheticLambda0
                public final /* synthetic */ View.OnClickListener f$5 = null;

                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    boolean z;
                    BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo2 = BcSmartspaceCardLoggingInfo.this;
                    int i2 = i;
                    String str2 = str;
                    TapAction tapAction2 = tapAction;
                    boolean z2 = shouldShowOnLockscreen;
                    View.OnClickListener onClickListener = this.f$5;
                    BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier2 = smartspaceEventNotifier;
                    SmartspaceTarget smartspaceTarget2 = smartspaceTarget;
                    FalsingManager falsingManager = BcSmartSpaceUtil.sFalsingManager;
                    if (falsingManager == null || !falsingManager.isFalseTap(1)) {
                        if (bcSmartspaceCardLoggingInfo2 != null) {
                            BcSmartspaceSubcardLoggingInfo bcSmartspaceSubcardLoggingInfo = bcSmartspaceCardLoggingInfo2.mSubcardInfo;
                            if (bcSmartspaceSubcardLoggingInfo != null) {
                                bcSmartspaceSubcardLoggingInfo.mClickedSubcardIndex = i2;
                            }
                            BcSmartspaceCardLogger.log(BcSmartspaceEvent.SMARTSPACE_CARD_CLICK, bcSmartspaceCardLoggingInfo2);
                        }
                        BcSmartspaceDataPlugin.IntentStarter intentStarter = BcSmartSpaceUtil.sIntentStarter;
                        if (intentStarter == null) {
                            intentStarter = new BcSmartSpaceUtil.AnonymousClass1(str2);
                        }
                        if (tapAction2 != null && (tapAction2.getIntent() != null || tapAction2.getPendingIntent() != null)) {
                            z = false;
                        } else {
                            z = true;
                        }
                        if (!z) {
                            intentStarter.startFromAction(tapAction2, view2, z2);
                        }
                        if (onClickListener != null) {
                            onClickListener.onClick(view2);
                        }
                        if (smartspaceEventNotifier2 == null) {
                            Log.w(str2, "Cannot notify target interaction smartspace event: event notifier null.");
                        } else {
                            smartspaceEventNotifier2.notifySmartspaceEvent(new SmartspaceTargetEvent.Builder(1).setSmartspaceTarget(smartspaceTarget2).setSmartspaceActionId(tapAction2.getId().toString()).build());
                        }
                    }
                }
            });
            return;
        }
        Log.e(str, "No tap action can be set up");
    }

    public static int getLoggingDisplaySurface(String str, boolean z, float f) {
        str.getClass();
        if (!str.equals("com.google.android.apps.nexuslauncher")) {
            if (!str.equals(ThemeOverlayApplier.SYSUI_PACKAGE)) {
                return 0;
            }
            if (z) {
                return 5;
            }
            if (f == 1.0f) {
                return 3;
            }
            if (f == 0.0f) {
                return 2;
            }
            return -1;
        }
        return 1;
    }

    public static void setOnClickListener(BcSmartspaceCardSecondary bcSmartspaceCardSecondary, SmartspaceTarget smartspaceTarget, TapAction tapAction, BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier, String str, BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo) {
        setOnClickListener$1(bcSmartspaceCardSecondary, smartspaceTarget, tapAction, smartspaceEventNotifier, str, bcSmartspaceCardLoggingInfo, 0);
    }
}
