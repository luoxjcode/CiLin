package com.example.yulin.ui.search

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yulin.ui.theme.*

data class Word(val name: String, val pinyin: String, val meaning: String, val example: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    var searchQuery by remember { mutableStateOf("") }
    var showDetail by remember { mutableStateOf(false) }
    var selectedWord by remember { mutableStateOf<Word?>(null) }

    val allWords = listOf(
        Word("醍醐灌顶", "tí hú guàn dǐng", "比喻听了高明的意见使人受到很大启发。也形容清凉。醍醐：酥酪上凝聚的油。", "“听了王老师的话，我感到醍醐灌顶，一切困惑都迎刃而解了。”"),
        Word("春风化雨", "chūn fēng huà yǔ", "指适宜于草木生长的风雨。后用以比喻良好的教育。", "“王老师对学生的教育，犹如春风化雨，滋润着每一个学生的心田。”"),
        Word("厚积薄发", "hòu jī bó fā", "形容只有准备充分，才能办好事情。", "“他多年潜心钻研，终于在这次比赛中厚积薄发，一举夺魁。”"),
        Word("举一反三", "jǔ yī fǎn sān", "指从一件事情类推而知道其他许多事情。", "“我们要学会举一反三，这样才能提高学习效率。”")
    )

    val filteredWords = allWords.filter { it.name.contains(searchQuery) }

    Box(modifier = modifier.fillMaxSize().background(PaperBg)) {
        Column(modifier = Modifier.padding(24.dp)) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("搜词语、释义、拼音...", color = Gray400, fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Gray400) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = OrangePrimary.copy(alpha = 0.2f),
                    cursorColor = OrangePrimary
                )
            )

            // Results List
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(filteredWords) { word ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedWord = word
                                showDetail = true
                            },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = word.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = SlatePrimary
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = word.pinyin,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Gray400
                                )
                            }
                            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        // Detail Overlay
        AnimatedVisibility(
            visible = showDetail,
            enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(300)) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(300)) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            selectedWord?.let { word ->
                DetailView(word = word, onDismiss = { showDetail = false })
            }
        }
    }
}

@Composable
fun DetailView(word: Word, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PaperBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
                .padding(top = 40.dp)
        ) {
            Text(
                text = word.name,
                style = MaterialTheme.typography.displayLarge,
                fontSize = 40.sp, // Slightly larger than default displayLarge
                color = SlatePrimary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .border(1.dp, Color.White, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = word.pinyin,
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                    color = Gray500
                )
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.VolumeUp, contentDescription = null, tint = OrangePrimary, modifier = Modifier.size(16.dp))
            }

            Spacer(Modifier.height(24.dp))

            // Meaning Card
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("【释义】", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = OrangePrimary, modifier = Modifier.padding(bottom = 8.dp))
                    Text(word.meaning, style = MaterialTheme.typography.bodyLarge, color = Gray800, lineHeight = 22.sp)
                }
            }

            // Example Card
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("【例句】", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = Green600, modifier = Modifier.padding(bottom = 8.dp))
                    Text(word.example, style = MaterialTheme.typography.bodyLarge, color = SlatePrimary, fontStyle = FontStyle.Italic, lineHeight = 22.sp)
                }
            }

            Button(
                onClick = { /* Add to bookmarks */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SlatePrimary),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("加入生词本", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            }
            
            // Add extra space at the bottom to ensure the button is fully visible above nav bar if needed
            Spacer(Modifier.height(40.dp))
        }

        // Close button fixed at top right
        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp)
                .padding(top = 16.dp)
        ) {
            Icon(Icons.Default.Cancel, contentDescription = "Close", tint = Gray400, modifier = Modifier.size(24.dp))
        }
    }
}
