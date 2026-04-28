package com.example.android.playground.graphql.presentation.mapper

import com.example.android.playground.graphql.domain.model.Repo
import com.example.android.playground.graphql.presentation.model.RepoUiModel

fun Repo.toUiModel(): RepoUiModel = RepoUiModel(
    name = name,
    nameWithOwner = nameWithOwner,
    description = description ?: "No description provided.",
    starsDisplay = formatStars(stars),
    language = language,
    languageColor = languageColor,
    url = url,
    ownerLogin = ownerLogin,
)

private fun formatStars(count: Int): String = when {
    count >= 1_000 -> "${"%.1f".format(count / 1_000.0)}k"
    else -> count.toString()
}
