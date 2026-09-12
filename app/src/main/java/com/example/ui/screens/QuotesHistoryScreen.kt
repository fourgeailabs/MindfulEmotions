package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MindfulQuote
import com.example.data.QuotesRepository
import com.example.data.model.EmotionCheckLog
import com.example.ui.theme.PastelCream
import com.example.ui.theme.PastelPeach
import com.example.ui.theme.PastelPeachLight
import com.example.ui.theme.PastelSage
import com.example.ui.theme.PastelSageLight
import com.example.ui.theme.PastelSurface
import com.example.ui.theme.PastelTextPrimary
import com.example.ui.theme.PastelTextSecondary

@Composable
fun QuotesHistoryScreen(
    logs: List<EmotionCheckLog>
) {
    var selectedTab by remember { mutableStateOf(0) }
    var selectedCategory by remember { mutableStateOf("All") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PastelCream)
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = PastelSurface,
            contentColor = PastelPeach
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Wisdom Quotes", fontWeight = FontWeight.Medium) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Check-in History (${logs.size})", fontWeight = FontWeight.Medium) }
            )
        }

        if (selectedTab == 0) {
            // Quotes Library
            val categories = listOf("All", "Anger", "Life", "Love", "Peace", "Gratitude")
            val filteredQuotes = remember(selectedCategory) {
                if (selectedCategory == "All") QuotesRepository.quotes
                else QuotesRepository.quotes.filter { it.theme == selectedCategory }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Curated Reflections",
                        style = MaterialTheme.typography.titleMedium,
                        color = PastelTextPrimary
                    )
                    Text(
                        text = "Insights on why negative emotions do not serve you, and why positive emotions uplift all.",
                        style = MaterialTheme.typography.bodySmall,
                        color = PastelTextSecondary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categories) { category ->
                            FilterChip(
                                selected = selectedCategory == category,
                                onClick = { selectedCategory = category },
                                label = { Text(category, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PastelPeachLight,
                                    selectedLabelColor = PastelPeach
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }

                items(filteredQuotes, key = { it.id }) { quote ->
                    QuoteCard(quote = quote)
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        } else {
            // Emotion Check History
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Your Mindful Pauses",
                        style = MaterialTheme.typography.titleMedium,
                        color = PastelTextPrimary
                    )
                    Text(
                        text = "A gentle timeline of emotions you observed throughout your days.",
                        style = MaterialTheme.typography.bodySmall,
                        color = PastelTextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (logs.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = PastelSurface)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = null,
                                    tint = PastelPeach.copy(alpha = 0.5f),
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "No Check-ins Logged Yet",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = PastelTextPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Whenever you respond to a scheduled emotion check-in, it will be saved here.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = PastelTextSecondary
                                )
                            }
                        }
                    }
                } else {
                    items(logs, key = { it.id }) { log ->
                        EmotionLogCard(log = log)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun QuoteCard(quote: MindfulQuote) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = PastelSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEE6DF))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(PastelSageLight)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = quote.theme,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = PastelSage
                    )
                }
                Icon(
                    imageVector = Icons.Default.FormatQuote,
                    contentDescription = null,
                    tint = PastelPeach.copy(alpha = 0.5f),
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "\"${quote.text}\"",
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                color = PastelTextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "— ${quote.author}",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                color = PastelPeach
            )
        }
    }
}

@Composable
fun EmotionLogCard(log: EmotionCheckLog) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = PastelSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEE6DF))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(PastelPeachLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = PastelPeach,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = log.selectedEmotion,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = PastelTextPrimary
                    )
                }

                Text(
                    text = log.formattedDate(),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = PastelTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "\"${log.quoteText}\"",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                color = PastelTextSecondary
            )
        }
    }
}
