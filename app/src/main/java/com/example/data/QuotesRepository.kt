package com.example.data

data class MindfulQuote(
    val id: Int,
    val text: String,
    val author: String,
    val theme: String // "Anger", "Life", "Love", "Peace", "Gratitude"
)

object QuotesRepository {
    val quotes = listOf(
        MindfulQuote(
            1,
            "Holding onto anger is like drinking poison and expecting the other person to die.",
            "Saint Augustine",
            "Anger"
        ),
        MindfulQuote(
            2,
            "You will not be punished for your anger; your anger itself will punish you.",
            "Buddha",
            "Anger"
        ),
        MindfulQuote(
            3,
            "How much more grievous are the consequences of anger than the causes of it.",
            "Marcus Aurelius",
            "Anger"
        ),
        MindfulQuote(
            4,
            "Love and kindness are never wasted; they always make a difference, blessing both the giver and the receiver.",
            "Barbara De Angelis",
            "Love"
        ),
        MindfulQuote(
            5,
            "Peace comes from within. Do not seek it without, for a calm mind heals both yourself and those around you.",
            "Buddha",
            "Peace"
        ),
        MindfulQuote(
            6,
            "Bitterness imprisons the heart, but forgiveness and compassion open wide the door to freedom.",
            "Mindful Reflection",
            "Life"
        ),
        MindfulQuote(
            7,
            "Every minute you spend in anger is sixty seconds of serenity and happiness you can never get back.",
            "Ralph Waldo Emerson",
            "Anger"
        ),
        MindfulQuote(
            8,
            "When you choose kindness over pride, you protect your own sanctuary of peace and lift the spirits of everyone you meet.",
            "Mindful Reflection",
            "Love"
        ),
        MindfulQuote(
            9,
            "Anger is an acid that does more harm to the vessel in which it is stored than to anything on which it is poured.",
            "Mark Twain",
            "Anger"
        ),
        MindfulQuote(
            10,
            "Gratitude turns what we already have into abundance, creating a ripple of joy that touches everyone near you.",
            "Melody Beattie",
            "Gratitude"
        ),
        MindfulQuote(
            11,
            "To be angry is to let another's actions determine your own inner state. Reclaim your tranquility.",
            "Marcus Aurelius",
            "Anger"
        ),
        MindfulQuote(
            12,
            "Love is not something you protect; it is something that protects you whenever you give it freely.",
            "Mindful Reflection",
            "Love"
        ),
        MindfulQuote(
            13,
            "Worrying does not empty tomorrow of its sorrow; it empties today of its gentle strength.",
            "Corrie ten Boom",
            "Life"
        ),
        MindfulQuote(
            14,
            "A peaceful heart creates a quiet sanctuary wherever you walk, offering rest to weary souls around you.",
            "Mindful Reflection",
            "Peace"
        ),
        MindfulQuote(
            15,
            "Resentment is an anchor that keeps you chained to the past. Let it go and watch how effortlessly you rise.",
            "Mindful Reflection",
            "Anger"
        ),
        MindfulQuote(
            16,
            "Where there is loving-kindness in your heart, judgment melts away and harmony takes its place.",
            "Mindful Reflection",
            "Love"
        ),
        MindfulQuote(
            17,
            "Speak or act with an angry mind and trouble will follow you as the wheel follows the ox that draws the cart.",
            "Dhammapada",
            "Anger"
        ),
        MindfulQuote(
            18,
            "Cultivating compassion is the greatest medicine; it calms our own pulse and brings gentleness to our relationships.",
            "Dalai Lama",
            "Love"
        ),
        MindfulQuote(
            19,
            "Nothing external has power over you when you choose patience and refuse to surrender your calm.",
            "Epictetus",
            "Peace"
        ),
        MindfulQuote(
            20,
            "Gentle thoughts nourish the body like warm sunshine, dissolving tension and inviting ease into your day.",
            "Mindful Reflection",
            "Life"
        ),
        MindfulQuote(
            21,
            "When fury knocks at your door, pause and breathe; reacting only deepens the wound you wish to heal.",
            "Mindful Reflection",
            "Anger"
        ),
        MindfulQuote(
            22,
            "Joy is contagious; when you choose warmth, everyone in the room breathes a little easier.",
            "Mindful Reflection",
            "Life"
        ),
        MindfulQuote(
            23,
            "He who conquers his anger has conquered his greatest enemy.",
            "Confucius",
            "Anger"
        ),
        MindfulQuote(
            24,
            "Patience with others is love, patience with self is hope, and patience with life is profound peace.",
            "Adel Bestavros",
            "Peace"
        ),
        MindfulQuote(
            25,
            "Letting go of a grievance does not mean someone else was right; it means you cherish your peace more than being vindicated.",
            "Mindful Reflection",
            "Anger"
        )
    )

    fun getRandomQuote(): MindfulQuote {
        return quotes.random()
    }

    fun getQuoteById(id: Int): MindfulQuote {
        return quotes.find { it.id == id } ?: quotes.first()
    }
}
