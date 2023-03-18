package com.google.android.systemui.smartspace;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.graphics.Rect;
import android.util.IndentingPrintWriter;
import android.view.CompositionSamplingListener;
import android.view.SurfaceControl;
import android.view.View;
import com.android.systemui.shared.navigationbar.RegionSamplingHelper;
import com.android.systemui.statusbar.notification.SourceType$Companion$from$1;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableOutlineView;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import com.android.systemui.statusbar.notification.stack.ExpandableViewState;
import java.io.PrintWriter;
import java.util.List;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class SmartSpaceController$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ Object f$2;

    public /* synthetic */ SmartSpaceController$$ExternalSyntheticLambda0(Object obj, Object obj2, int i, Object obj3) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
        this.f$2 = obj3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        boolean z;
        boolean z2 = false;
        switch (this.$r8$classId) {
            case 0:
                SmartSpaceController smartSpaceController = (SmartSpaceController) this.f$0;
                SmartSpaceCard smartSpaceCard = (SmartSpaceCard) this.f$2;
                smartSpaceController.getClass();
                if (((NewCardInfo) this.f$1).mIsPrimary) {
                    smartSpaceController.mData.mCurrentCard = smartSpaceCard;
                } else {
                    smartSpaceController.mData.mWeatherCard = smartSpaceCard;
                }
                smartSpaceController.mData.handleExpire();
                smartSpaceController.update();
                return;
            case 1:
                RegionSamplingHelper regionSamplingHelper = (RegionSamplingHelper) this.f$0;
                SurfaceControl surfaceControl = (SurfaceControl) this.f$1;
                Rect rect = (Rect) this.f$2;
                if (surfaceControl != null) {
                    regionSamplingHelper.getClass();
                    if (!surfaceControl.isValid()) {
                        return;
                    }
                }
                RegionSamplingHelper.SysuiCompositionSamplingListener sysuiCompositionSamplingListener = regionSamplingHelper.mCompositionSamplingListener;
                RegionSamplingHelper.AnonymousClass3 anonymousClass3 = regionSamplingHelper.mSamplingListener;
                sysuiCompositionSamplingListener.getClass();
                CompositionSamplingListener.register(anonymousClass3, 0, surfaceControl, rect);
                return;
            default:
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) this.f$0;
                PrintWriter printWriter = (IndentingPrintWriter) this.f$1;
                String[] strArr = (String[]) this.f$2;
                SourceType$Companion$from$1 sourceType$Companion$from$1 = ExpandableNotificationRow.BASE_VALUE;
                expandableNotificationRow.getClass();
                printWriter.print("visibility: " + expandableNotificationRow.getVisibility());
                printWriter.print(", alpha: " + expandableNotificationRow.getAlpha());
                printWriter.print(", translation: " + expandableNotificationRow.getTranslation());
                printWriter.print(", Entry isDismissable: " + expandableNotificationRow.mEntry.isDismissable());
                StringBuilder sb = new StringBuilder();
                sb.append(", mOnUserInteractionCallback null: ");
                if (expandableNotificationRow.mOnUserInteractionCallback == null) {
                    z = true;
                } else {
                    z = false;
                }
                sb.append(z);
                printWriter.print(sb.toString());
                printWriter.print(", removed: false");
                printWriter.print(", expandAnimationRunning: " + expandableNotificationRow.mExpandAnimationRunning);
                NotificationContentView showingLayout = expandableNotificationRow.getShowingLayout();
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline0.m(", privateShowing: ");
                if (showingLayout == expandableNotificationRow.mPrivateLayout) {
                    z2 = true;
                }
                m.append(z2);
                printWriter.print(m.toString());
                printWriter.println();
                showingLayout.getClass();
                printWriter.print("contentView visibility: " + showingLayout.getVisibility());
                printWriter.print(", alpha: " + showingLayout.getAlpha());
                printWriter.print(", clipBounds: " + showingLayout.getClipBounds());
                printWriter.print(", contentHeight: " + showingLayout.mContentHeight);
                printWriter.print(", visibleType: " + showingLayout.mVisibleType);
                View viewForVisibleType = showingLayout.getViewForVisibleType(showingLayout.mVisibleType);
                printWriter.print(", visibleView ");
                if (viewForVisibleType != null) {
                    StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline0.m(" visibility: ");
                    m2.append(viewForVisibleType.getVisibility());
                    printWriter.print(m2.toString());
                    printWriter.print(", alpha: " + viewForVisibleType.getAlpha());
                    printWriter.print(", clipBounds: " + viewForVisibleType.getClipBounds());
                } else {
                    printWriter.print("null");
                }
                printWriter.println();
                ExpandableViewState expandableViewState = expandableNotificationRow.mViewState;
                if (expandableViewState != null) {
                    expandableViewState.dump(printWriter, strArr);
                    printWriter.println();
                } else {
                    printWriter.println("no viewState!!!");
                }
                StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline0.m("Roundness: ");
                m3.append(((ExpandableOutlineView) expandableNotificationRow).mRoundableState.debugString());
                printWriter.println(m3.toString());
                if (expandableNotificationRow.mIsSummaryWithChildren) {
                    printWriter.println();
                    printWriter.print("ChildrenContainer");
                    printWriter.print(" visibility: " + expandableNotificationRow.mChildrenContainer.getVisibility());
                    printWriter.print(", alpha: " + expandableNotificationRow.mChildrenContainer.getAlpha());
                    printWriter.print(", translationY: " + expandableNotificationRow.mChildrenContainer.getTranslationY());
                    printWriter.println();
                    List<ExpandableNotificationRow> attachedChildren = expandableNotificationRow.getAttachedChildren();
                    StringBuilder m4 = VendorAtomValue$$ExternalSyntheticOutline0.m("Children: ");
                    m4.append(attachedChildren.size());
                    printWriter.println(m4.toString());
                    printWriter.print("{");
                    printWriter.increaseIndent();
                    for (ExpandableNotificationRow expandableNotificationRow2 : attachedChildren) {
                        printWriter.println();
                        expandableNotificationRow2.dump(printWriter, strArr);
                    }
                    printWriter.decreaseIndent();
                    printWriter.println("}");
                    return;
                }
                NotificationContentView notificationContentView = expandableNotificationRow.mPrivateLayout;
                if (notificationContentView != null) {
                    if (notificationContentView.mHeadsUpSmartReplyView != null) {
                        printWriter.println("HeadsUp SmartReplyView:");
                        printWriter.increaseIndent();
                        notificationContentView.mHeadsUpSmartReplyView.dump(printWriter);
                        printWriter.decreaseIndent();
                    }
                    if (notificationContentView.mExpandedSmartReplyView != null) {
                        printWriter.println("Expanded SmartReplyView:");
                        printWriter.increaseIndent();
                        notificationContentView.mExpandedSmartReplyView.dump(printWriter);
                        printWriter.decreaseIndent();
                        return;
                    }
                    return;
                }
                return;
        }
    }
}
