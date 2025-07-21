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

package com.android.systemui.statusbar.policy.ui.dialog.viewmodel

import com.android.systemui.common.shared.model.Icon
import com.android.systemui.dagger.SysUISingleton
import com.android.systemui.dagger.qualifiers.Background
import com.android.systemui.res.R
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Viewmodel for the priority ("zen") modes dialog that can be opened from quick settings. It allows
 * the user to quickly toggle modes.
 */
@SysUISingleton
class AmbientMusicModesDialogViewModel
@Inject
constructor(
    @Background val bgDispatcher: CoroutineDispatcher,
) {
    val tiles: Flow<List<AmbientMusicModeTileViewModel>> =
        flowOf(
            listOf(
                AmbientMusicModeTileViewModel(
                    id = "placeholder_1",
                    icon = Icon.Resource(R.drawable.ic_qs_ambient_music_modes, null),
                    text = "Placeholder 1",
                    subtext = "This is a placeholder",
                    subtextDescription = "This is a placeholder",
                    enabled = false,
                    stateDescription = "Off",
                    onClick = {},
                    onLongClick = {},
                    onLongClickLabel = "Long press for settings",
                ),
                AmbientMusicModeTileViewModel(
                    id = "placeholder_2",
                    icon = Icon.Resource(R.drawable.ic_qs_ambient_music_modes, null),
                    text = "Placeholder 2",
                    subtext = "This is a placeholder",
                    subtextDescription = "This is a placeholder",
                    enabled = false,
                    stateDescription = "Off",
                    onClick = {},
                    onLongClick = {},
                    onLongClickLabel = "Long press for settings",
                ),
                AmbientMusicModeTileViewModel(
                    id = "placeholder_3",
                    icon = Icon.Resource(R.drawable.ic_qs_ambient_music_modes, null),
                    text = "Placeholder 3",
                    subtext = "This is a placeholder",
                    subtextDescription = "This is a placeholder",
                    enabled = false,
                    stateDescription = "Off",
                    onClick = {},
                    onLongClick = {},
                    onLongClickLabel = "Long press for settings",
                ),
                AmbientMusicModeTileViewModel(
                    id = "placeholder_4",
                    icon = Icon.Resource(R.drawable.ic_qs_ambient_music_modes, null),
                    text = "Placeholder 4",
                    subtext = "This is a placeholder",
                    subtextDescription = "This is a placeholder",
                    enabled = false,
                    stateDescription = "Off",
                    onClick = {},
                    onLongClick = {},
                    onLongClickLabel = "Long press for settings",
                ),
            )
        )
}
