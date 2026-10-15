package com.thelightphone.dictionary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.thelightphone.sdk.SealedLightActivity
import com.thelightphone.sdk.SimpleLightScreen
import com.thelightphone.sdk.ui.*

/**
 * Settings screen: preferences (recents) and credits/license info for the live
 * data source. Opens from the gear icon on the home screen top bar. Layout and
 * attribution follow the Wikipedia Light tool's About page.
 */
class CreditsScreen(
    sealedActivity: SealedLightActivity,
    private val showRecentsFlow: kotlinx.coroutines.flow.StateFlow<Boolean>,
    private val onToggleShowRecents: () -> Unit = {},
    private val onClearRecents: () -> Unit = {},
) : SimpleLightScreen<Unit>(sealedActivity) {

    @Composable
    override fun Content() {
        val themeColors by LightThemeController.colors.collectAsState()
        var confirmClear by remember { mutableStateOf(false) }
        val showRecents by showRecentsFlow.collectAsState()

        LightTheme(colors = themeColors) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightThemeTokens.colors.background),
            ) {
                LightTopBar(
                    center = LightTopBarCenter.Text("About"),
                    leftButton = LightBarButton.LightIcon(
                        icon = LightIcons.BACK,
                        onClick = { goBack(null) },
                    ),
                )
                LightScrollView(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(1f.gridUnitsAsDp()),
                ) {
                    // Preferences
                    ToggleRow(
                        label = "Show recents",
                        checked = showRecents,
                        onClick = onToggleShowRecents,
                    )
                    LightText(
                        text = if (confirmClear) "Clear recents — tap again to confirm" else "Clear recents",
                        variant = LightTextVariant.Copy,
                        lighten = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .lightClickable {
                                if (confirmClear) {
                                    onClearRecents()
                                    confirmClear = false
                                } else {
                                    confirmClear = true
                                }
                            }
                            .padding(vertical = 0.75f.gridUnitsAsDp()),
                    )

                    // Attribution sections
                    Spacer(modifier = Modifier.height(1f.gridUnitsAsDp()))

                    LightText(
                        text = "Definitions",
                        variant = LightTextVariant.Subheading,
                        modifier = Modifier.padding(top = 1f.gridUnitsAsDp()),
                    )
                    LightText(
                        text = "English Wiktionary content is licensed under " +
                            "CC BY-SA 4.0 by the Wiktionary contributors.",
                        variant = LightTextVariant.Detail,
                        lighten = true,
                        modifier = Modifier.padding(vertical = 0.5f.gridUnitsAsDp()),
                    )
                    LightText(
                        text = "Data source",
                        variant = LightTextVariant.Subheading,
                        modifier = Modifier.padding(top = 2f.gridUnitsAsDp()),
                    )
                    LightText(
                        text = "Fetched live from the English Wiktionary " +
                            "(en.wiktionary.org) via the MediaWiki API. No account required.",
                        variant = LightTextVariant.Detail,
                        lighten = true,
                        modifier = Modifier.padding(vertical = 0.5f.gridUnitsAsDp()),
                    )
                    LightText(
                        text = "Tool",
                        variant = LightTextVariant.Subheading,
                        modifier = Modifier.padding(top = 2f.gridUnitsAsDp()),
                    )
                    LightText(
                        text = "Built with the Light Phone SDK. An independent, " +
                            "non-commercial tool; not affiliated with the Wikimedia Foundation.",
                        variant = LightTextVariant.Detail,
                        lighten = true,
                        modifier = Modifier.padding(vertical = 0.5f.gridUnitsAsDp()),
                    )
                }
            }
        }
    }
}

@Composable
private fun ToggleRow(label: String, checked: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .lightClickable(onClick = onClick)
            .padding(vertical = 0.75f.gridUnitsAsDp()),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LightIcon(
            icon = if (checked) LightIcons.TOGGLE_STATE_ON else LightIcons.TOGGLE_STATE_OFF,
        )
        LightText(
            text = label,
            variant = LightTextVariant.Copy,
            modifier = Modifier
                .weight(1f)
                .padding(start = 1f.gridUnitsAsDp()),
        )
    }
}
