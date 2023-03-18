package com.google.android.systemui.smartspace;

import android.app.smartspace.SmartspaceTarget;
import android.app.smartspace.SmartspaceTargetEvent;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.R;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.wm.shell.pip.phone.PipMotionHelper$$ExternalSyntheticLambda2;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes2.dex */
public final class BcSmartspaceDataProvider implements BcSmartspaceDataPlugin {
    public static final boolean DEBUG = Log.isLoggable(BcSmartspaceDataPlugin.TAG, 3);
    public final HashSet mSmartspaceTargetListeners = new HashSet();
    public final ArrayList mSmartspaceTargets = new ArrayList();
    public HashSet mViews = new HashSet();
    public HashSet mAttachListeners = new HashSet();
    public BcSmartspaceDataPlugin.SmartspaceEventNotifier mEventNotifier = null;
    public AnonymousClass1 mStateChangeListener = new View.OnAttachStateChangeListener() { // from class: com.google.android.systemui.smartspace.BcSmartspaceDataProvider.1
        @Override // android.view.View.OnAttachStateChangeListener
        public final void onViewAttachedToWindow(View view) {
            BcSmartspaceDataProvider.this.mViews.add(view);
            Iterator it = BcSmartspaceDataProvider.this.mAttachListeners.iterator();
            while (it.hasNext()) {
                ((View.OnAttachStateChangeListener) it.next()).onViewAttachedToWindow(view);
            }
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public final void onViewDetachedFromWindow(View view) {
            BcSmartspaceDataProvider.this.mViews.remove(view);
            view.removeOnAttachStateChangeListener(this);
            Iterator it = BcSmartspaceDataProvider.this.mAttachListeners.iterator();
            while (it.hasNext()) {
                ((View.OnAttachStateChangeListener) it.next()).onViewDetachedFromWindow(view);
            }
        }
    };

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public final void addOnAttachStateChangeListener(View.OnAttachStateChangeListener onAttachStateChangeListener) {
        this.mAttachListeners.add(onAttachStateChangeListener);
        Iterator it = this.mViews.iterator();
        while (it.hasNext()) {
            onAttachStateChangeListener.onViewAttachedToWindow((View) it.next());
        }
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public final void notifySmartspaceEvent(SmartspaceTargetEvent smartspaceTargetEvent) {
        BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier = this.mEventNotifier;
        if (smartspaceEventNotifier != null) {
            smartspaceEventNotifier.notifySmartspaceEvent(smartspaceTargetEvent);
        }
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public final void onTargetsAvailable(List<SmartspaceTarget> list) {
        if (DEBUG) {
            Log.d(BcSmartspaceDataPlugin.TAG, this + " onTargetsAvailable called. Callers = " + Debug.getCallers(3));
            StringBuilder sb = new StringBuilder();
            sb.append("    targets.size() = ");
            sb.append(list.size());
            Log.d(BcSmartspaceDataPlugin.TAG, sb.toString());
            Log.d(BcSmartspaceDataPlugin.TAG, "    targets = " + list.toString());
        }
        this.mSmartspaceTargets.clear();
        for (SmartspaceTarget smartspaceTarget : list) {
            if (smartspaceTarget.getFeatureType() != 15) {
                this.mSmartspaceTargets.add(smartspaceTarget);
            }
        }
        this.mSmartspaceTargetListeners.forEach(new PipMotionHelper$$ExternalSyntheticLambda2(2, this));
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public final void registerListener(BcSmartspaceDataPlugin.SmartspaceTargetListener smartspaceTargetListener) {
        this.mSmartspaceTargetListeners.add(smartspaceTargetListener);
        smartspaceTargetListener.onSmartspaceTargetsUpdated(this.mSmartspaceTargets);
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public final void unregisterListener(BcSmartspaceDataPlugin.SmartspaceTargetListener smartspaceTargetListener) {
        this.mSmartspaceTargetListeners.remove(smartspaceTargetListener);
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public final BcSmartspaceDataPlugin.SmartspaceView getView(ViewGroup viewGroup) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.smartspace_enhanced, viewGroup, false);
        inflate.addOnAttachStateChangeListener(this.mStateChangeListener);
        return (BcSmartspaceDataPlugin.SmartspaceView) inflate;
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public final void registerSmartspaceEventNotifier(BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier) {
        this.mEventNotifier = smartspaceEventNotifier;
    }
}
