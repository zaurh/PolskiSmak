package com.zaurh.polskismak.data.source

import com.zaurh.polskismak.domain.model.LocalizedText
import com.zaurh.polskismak.domain.model.Quote

internal object BundledQuotes {
    val all: List<Quote> = listOf(
        quote(
            "One cannot think well, love well, sleep well, if one has not dined well.",
            "Nie można dobrze myśleć, dobrze kochać ani dobrze spać, jeśli się dobrze nie zjadło.",
            "Virginia Woolf",
        ),
        quote(
            "Tell me what you eat, and I will tell you what you are.",
            "Powiedz mi, co jesz, a powiem ci, kim jesteś.",
            "Anthelme Brillat-Savarin",
        ),
        quote("The secret ingredient is always love.", "Sekretnym składnikiem zawsze jest miłość.", "Wolfgang Puck"),
        quote(
            "There is no sincerer love than the love of food.",
            "Nie ma szczerszej miłości niż miłość do jedzenia.",
            "George Bernard Shaw",
        ),
        quote("First we eat, then we do everything else.", "Najpierw jemy, potem robimy całą resztę.", "M.F.K. Fisher"),
        quote(
            "A good cook is like a sorceress who dispenses happiness.",
            "Dobra kucharka jest jak czarodziejka, która rozdaje szczęście.",
            "Elsa Schiaparelli",
        ),
        quote(
            "Good food is the foundation of genuine happiness.",
            "Dobre jedzenie jest fundamentem prawdziwego szczęścia.",
            "Auguste Escoffier",
        ),
        quote(
            "A recipe has no soul. You, as the cook, must bring soul to the recipe.",
            "Przepis nie ma duszy. To ty, jako kucharz, musisz tchnąć w niego duszę.",
            "Thomas Keller",
        ),
        quote(
            "Food is symbolic of love when words are inadequate.",
            "Jedzenie wyraża miłość, gdy brakuje słów.",
            "Alan D. Wolfelt",
        ),
        quote("Cooking is a philosophy; it's not a recipe.", "Gotowanie to filozofia, a nie przepis.", "Marco Pierre White"),
    )

    private fun quote(en: String, pl: String, author: String) = Quote(LocalizedText(en, pl), author)
}
