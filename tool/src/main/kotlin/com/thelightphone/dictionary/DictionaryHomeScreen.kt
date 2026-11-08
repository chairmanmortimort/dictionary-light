package com.thelightphone.dictionary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.input.TextFieldState
import com.thelightphone.sdk.rememberKeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.thelightphone.sdk.InitialScreen
import com.thelightphone.sdk.LightScreen
import com.thelightphone.sdk.SealedLightActivity
import com.thelightphone.sdk.ui.*


@InitialScreen
class DictionaryHomeScreen(private val sealedActivity: SealedLightActivity) :
    LightScreen<Unit, DictionaryViewModel>(sealedActivity) {

    override val viewModelClass: Class<DictionaryViewModel>
        get() = DictionaryViewModel::class.java

    override fun createViewModel(): DictionaryViewModel = DictionaryViewModel(lightContext.dataStore)

    override fun willShow() {
        // Live mode: nothing to preload. State starts at Empty (no query). The
        // tool opens straight into the search field.
    }

    @Composable
    override fun Content() {
        val themeColors by LightThemeController.colors.collectAsState()
        val state by viewModel.state.collectAsState()
        val recentWords by viewModel.recentWords.collectAsState()
        val showRecents by viewModel.showRecents.collectAsState()

        val searchState = remember { TextFieldState("") }
        val kbFlow = rememberKeyboardOptions()
        var recentsExpanded by remember { mutableStateOf(false) }

        LightTheme(colors = themeColors) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightThemeTokens.colors.background),
            ) {
                // Search editor — title states the action ("Search"), the field is
                // the primary interaction. No separate "Tap to search" screen.
                LightTextInputEditor(
                    title = "Search Dictionary",
                    state = searchState,
                    keyboardOptionsFlow = kbFlow,
                    onSubmit = { result: CharSequence ->
                        val q = result.toString().trim()
                        if (q.isNotBlank()) {
                            _lastQuery = q
                            viewModel.search(q)
                        }
                    },
                    onBack = { /* stay on this screen */ },
                    submitIcon = LightIcons.SEARCH,
                    singleLine = true,
                    leftBottomBarItem = LightBarButton.LightIcon(
                        icon = LightIcons.SETTINGS,
                        onClick = {
                            navigateTo(
                                screenFactory = { activity ->
                                    CreditsScreen(
                                        activity,
                                        showRecentsFlow = viewModel.showRecents,
                                        onToggleShowRecents = viewModel::toggleShowRecents,
                                        onClearRecents = viewModel::clearRecents,
                                    )
                                },
                            )
                        },
                        contentDescription = "Settings",
                    ),
                    modifier = Modifier.fillMaxSize(),
                )

                // "Looking up" — larger font, no settings icon.
                if (state is DictState.Loading) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        LightText(
                            text = "Looking up…",
                            variant = LightTextVariant.Subheading,
                        )
                    }
                }

                // "No entry" screen
                if (state is DictState.Empty) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (_lastQuery.isNotEmpty()) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.lightClickable {
                                    _lastQuery = ""
                                    viewModel.clear()
                                },
                            ) {
                                LightText(
                                    text = "No entry for \"${_lastQuery}\".",
                                    variant = LightTextVariant.Copy,
                                )
                                Spacer(modifier = Modifier.height(1f.gridUnitsAsDp()))
                                LightText(
                                    text = "Tap to go back",
                                    variant = LightTextVariant.Detail,
                                    lighten = true,
                                )
                            }
                        }
                    }
                }

                // "Found" — entry shown in place, no settings icon.
                if (state is DictState.Found) {
                    val r = (state as DictState.Found).result
                    LightScrollView(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 1f.gridUnitsAsDp()),
                    ) {
                        LightText(
                            text = r.word,
                            variant = LightTextVariant.Subheading,
                            modifier = Modifier.padding(top = 1f.gridUnitsAsDp(), bottom = 0.5f.gridUnitsAsDp()),
                            maxLines = 2,
                        )
                        if (r.pronunciation != null) {
                            Spacer(modifier = Modifier.height(1f.gridUnitsAsDp()))
                            LightText(
                                text = r.pronunciation,
                                variant = LightTextVariant.Detail,
                                lighten = true,
                                modifier = Modifier.padding(bottom = 1f.gridUnitsAsDp()),
                            )
                        }
                        r.sections.forEach { sec ->
                            if (sec.pos.isNotBlank()) {
                                LightText(
                                    text = sec.pos,
                                    variant = LightTextVariant.Subheading,
                                    modifier = Modifier.padding(
                                        top = 2f.gridUnitsAsDp(),
                                        bottom = 0.5f.gridUnitsAsDp(),
                                    ),
                                )
                            }
                            sec.glosses.forEachIndexed { i, g ->
                                LightText(
                                    text = "${i + 1}. $g",
                                    variant = LightTextVariant.Copy,
                                    modifier = Modifier.padding(vertical = 0.5f.gridUnitsAsDp()),
                                )
                            }
                        }
                        if (r.synonyms.isNotEmpty()) {
                            LightText(
                                text = "Synonyms",
                                variant = LightTextVariant.Subheading,
                                modifier = Modifier.padding(
                                    top = 2f.gridUnitsAsDp(),
                                    bottom = 0.5f.gridUnitsAsDp(),
                                ),
                            )
                            r.synonyms.forEach { syn ->
                                LightText(
                                    text = syn,
                                    variant = LightTextVariant.Detail,
                                    lighten = true,
                                    modifier = Modifier
                                        .padding(vertical = 0.5f.gridUnitsAsDp())
                                        .lightClickable { openWord(syn) },
                                )
                            }
                        }
                        if (r.antonyms.isNotEmpty()) {
                            LightText(
                                text = "Antonyms",
                                variant = LightTextVariant.Subheading,
                                modifier = Modifier.padding(
                                    top = 2f.gridUnitsAsDp(),
                                    bottom = 0.5f.gridUnitsAsDp(),
                                ),
                            )
                            r.antonyms.forEach { ant ->
                                LightText(
                                    text = ant,
                                    variant = LightTextVariant.Detail,
                                    lighten = true,
                                    modifier = Modifier
                                        .padding(vertical = 0.5f.gridUnitsAsDp())
                                        .lightClickable { openWord(ant) },
                                )
                            }
                        }
                    }
                }

                // Error screen
                if (state is DictState.Error) {
                    LightText(text = (state as DictState.Error).message, variant = LightTextVariant.Copy)
                }

            }
        }
    }

    private fun openWord(word: String) {
        _lastQuery = word
        viewModel.search(word)
    }

    private fun openRecent(word: String) {
        _lastQuery = word
        viewModel.search(word)
    }

    private var _lastQuery: String by mutableStateOf("")
}
