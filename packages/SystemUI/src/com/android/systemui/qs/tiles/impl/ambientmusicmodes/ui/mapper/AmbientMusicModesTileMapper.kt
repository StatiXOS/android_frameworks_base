/*
 * Copyright (C) 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.qs.tiles.impl.ambientmusicmodes.ui.mapper

import android.content.res.Resources
import android.widget.Button
import com.android.systemui.qs.tiles.base.shared.model.QSTileConfig
import com.android.systemui.qs.tiles.base.shared.model.QSTileState
import com.android.systemui.qs.tiles.base.ui.model.QSTileDataToStateMapper
import com.android.systemui.qs.tiles.impl.ambientmusicmodes.domain.model.AmbientMusicModesTileModel
import com.android.systemui.res.R
import com.android.systemui.shade.ShadeDisplayAware
import javax.inject.Inject

class AmbientMusicModesTileMapper
@Inject
constructor(@ShadeDisplayAware private val resources: Resources, val theme: Resources.Theme) :
    QSTileDataToStateMapper<AmbientMusicModesTileModel> {
    override fun map(config: QSTileConfig, data: AmbientMusicModesTileModel): QSTileState =
        QSTileState.build(resources, theme, config.uiConfig) {
            icon = data.icon
            activationState =
                if (data.isActivated) {
                    QSTileState.ActivationState.ACTIVE
                } else {
                    QSTileState.ActivationState.INACTIVE
                }
            secondaryLabel = resources.getString(R.string.quick_settings_ambient_music_modes_label)
            contentDescription = "$label. $secondaryLabel"
            supportedActions =
                setOf(
                    QSTileState.UserAction.CLICK,
                    QSTileState.UserAction.LONG_CLICK,
                    QSTileState.UserAction.TOGGLE_CLICK,
                )
            sideViewIcon = QSTileState.SideViewIcon.Chevron
            expandedAccessibilityClass = Button::class
        }
}
