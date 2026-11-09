package com.thelightphone.dictionary

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.input.TextFieldState
import com.thelightphone.sdk.rememberKeyboardOptions
import androidx.compose.runtime.*
import kotlinx.coroutines.Dispatchers
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Modifier
import com.thelightphone.sdk.InitialScreen
import com.thelightphone.sdk.LightScreen
import com.thelightphone.sdk.SealedLightActivity
import com.thelightphone.sdk.ui.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@InitialScreen
class DictionaryHomeScreen(private val sealedActivity: SealedLightActivity) :
    LightScreen<Unit, DictionaryViewModel>(sealedActivity) {

    override val viewModelClass: Class<DictionaryViewModel>
        get() = DictionaryViewModel::class.java

    override fun createViewModel(): DictionaryViewModel = DictionaryViewModel(lightContext.dataStore)

    override fun willShow() {
    }

    private var _lastQuery: String by mutableStateOf("")

    @Composable
    override fun Content() {
        val themeColors by LightThemeController.colors.collectAsState()
        val state by viewModel.state.collectAsState(Dispatchers.Main)
        val showRecents by viewModel.showRecents.collectAsState()
        val recentWords by viewModel.recentWords.collectAsState()

        val searchState = remember { TextFieldState("") }
        val kbFlow = rememberKeyboardOptions()
        var recentsExpanded by remember { mutableStateOf(false) }

        LightTheme(colors = themeColors) {
            when (state) {
                is DictState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(LightThemeTokens.colors.background),
                    ) {
                        LightTopBar(
                            leftButton = LightBarButton.LightIcon(
                                icon = LightIcons.BACK,
                                onClick = { viewModel.clear() },
                                contentDescription = "Back",
                            ),
                            center = LightTopBarCenter.Text("Search Dictionary"),
                            modifier = Modifier.padding(bottom = 0.25f.gridUnitsAsDp()),
                        )
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
                }

                is DictState.Found -> {
                    val r = (state as DictState.Found).result
                    key(r.word) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(LightThemeTokens.colors.background)
                            .verticalScroll(rememberScrollState()),
                    ) {
                        LightTopBar(
                            leftButton = LightBarButton.LightIcon(
                                icon = LightIcons.BACK,
                                onClick = { viewModel.clear() },
                                contentDescription = "Back",
                            ),
                            center = LightTopBarCenter.Text(r.word),
                            modifier = Modifier.padding(bottom = 0.25f.gridUnitsAsDp()),
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 2f.gridUnitsAsDp()),
                        ) {
                            if (r.pronunciation != null) {
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
                                    Row(
                                        modifier = Modifier.padding(vertical = 0.5f.gridUnitsAsDp()),
                                    ) {
                                        LightText(
                                            text = "${i + 1}.",
                                            variant = LightTextVariant.Detail,
                                            lighten = true,
                                            modifier = Modifier.padding(end = 0.5f.gridUnitsAsDp()),
                                        )
                                        LightText(
                                            text = g,
                                            variant = LightTextVariant.Copy,
                                        )
                                    }
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
                                            .lightClickable { viewModel.searchSynonym(syn) },
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
                                            .lightClickable { viewModel.searchSynonym(ant) },
                                    )
                                }
                            }
                        }
                    }
                    }
                }

                is DictState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(LightThemeTokens.colors.background),
                    ) {
                        LightTopBar(
                            leftButton = LightBarButton.LightIcon(
                                icon = LightIcons.BACK,
                                onClick = { viewModel.clear() },
                                contentDescription = "Back",
                            ),
                            center = LightTopBarCenter.Text("Search Dictionary"),
                            modifier = Modifier.padding(bottom = 0.25f.gridUnitsAsDp()),
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center,
                        ) {
                            LightText(
                                text = (state as DictState.Error).message,
                                variant = LightTextVariant.Copy,
                                align = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 2f.gridUnitsAsDp()),
                            )
                        }
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(LightThemeTokens.colors.background),
                    ) {
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
                            onBack = { /* stay on home screen */ },
                            showBackButton = false,
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

                        // Recents — all-caps RECENTS text button (expandable).
                        if (showRecents && recentWords.isNotEmpty()) {
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(start = 2f.gridUnitsAsDp(), bottom = 1f.gridUnitsAsDp()),
                            ) {
                                LightText(
                                    text = "RECENTS",
                                    variant = LightTextVariant.Subheading,
                                    modifier = Modifier.clickable(onClick = { recentsExpanded = !recentsExpanded }),
                                )
                                if (recentsExpanded) {
                                    recentWords.forEach { word ->
                                        LightText(
                                            text = word,
                                            variant = LightTextVariant.Detail,
                                            lighten = true,
                                            modifier = Modifier
                                                .padding(vertical = 0.5f.gridUnitsAsDp())
                                                .clickable(onClick = { openRecent(word) }),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun openRecent(word: String) {
        _lastQuery = word
        viewModel.search(word)
    }
}