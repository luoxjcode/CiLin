package com.example.cilin.ui.search

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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.example.cilin.network.models.DictWordDetail
import com.example.cilin.network.models.DictWordItem
import com.example.cilin.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize().background(PaperBg)) {
        Column(modifier = Modifier.padding(24.dp)) {
            // Search Bar
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.searchWords(it) },
                placeholder = { Text("搜词语、释义、拼音...", color = Gray400, fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Gray400) },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.searchWords("") }) {
                            Icon(Icons.Filled.Cancel, contentDescription = "Clear", tint = Gray400)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = OrangePrimary.copy(alpha = 0.2f),
                    cursorColor = OrangePrimary
                ),
                singleLine = true
            )

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = OrangePrimary)
                }
            } else {
                // Results List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.searchResults) { word ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.fetchWordDetail(word.id)
                                },
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = word.word,
                                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                                        color = Slate700
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = word.pinyin ?: "",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Gray400
                                    )
                                }
                                Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color(0xFFE2E8F0), modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }

        // Detail Overlay
        AnimatedVisibility(
            visible = uiState.showDetail,
            enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(300)) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(300)) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            uiState.selectedWordDetail?.let { detail ->
                DetailView(detail = detail, onDismiss = { viewModel.dismissDetail() })
            }
        }

        if (uiState.isDetailLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = OrangePrimary)
            }
        }
    }
}

@Composable
fun DetailView(detail: DictWordDetail, onDismiss: () -> Unit) {
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
                text = detail.word,
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 40.sp),
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
                    text = detail.pinyin ?: "",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
                    fontStyle = FontStyle.Italic,
                    color = Gray500
                )
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.VolumeUp, contentDescription = null, tint = OrangePrimary.copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
            }

            Spacer(Modifier.height(32.dp))

            // Meaning Card
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Orange50),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("【释义】", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = OrangePrimary), modifier = Modifier.padding(bottom = 8.dp))
                    Text(detail.explanation ?: "暂无释义", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp, lineHeight = 22.sp), color = Gray500)
                }
            }

            // Derivation Card (if exists)
            detail.derivation?.let { derivation ->
                if (derivation.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text("【出处】", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Slate700), modifier = Modifier.padding(bottom = 8.dp))
                            Text(derivation, style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp, lineHeight = 22.sp), color = Gray500)
                        }
                    }
                }
            }

            // Example Card
            detail.exampleList?.let { examples ->
                if (examples.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Green50),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text("【例句】", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Green600), modifier = Modifier.padding(bottom = 8.dp))
                            
                            examples.forEachIndexed { index, example ->
                                val formattedExample = buildAnnotatedString {
                                    append("${index + 1}. ")
                                    // 兼容中英文波浪号
                                    val originalText = example.content.replace("~", detail.word).replace("～", detail.word)
                                    val word = detail.word
                                    
                                    var startIndex = 0
                                    while (startIndex < originalText.length) {
                                        val wordIndex = originalText.indexOf(word, startIndex)
                                        if (wordIndex == -1) {
                                            append(originalText.substring(startIndex))
                                            break
                                        } else {
                                            append(originalText.substring(startIndex, wordIndex))
                                            withStyle(style = SpanStyle(color = Red500, fontWeight = FontWeight.Bold)) {
                                                append(word)
                                            }
                                            startIndex = wordIndex + word.length
                                        }
                                    }
                                }
                                
                                Text(
                                    text = formattedExample,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontFamily = SerifFont, fontStyle = FontStyle.Italic, fontSize = 14.sp, lineHeight = 22.sp),
                                    color = Slate700,
                                    modifier = Modifier.padding(bottom = if (index < examples.size - 1) 8.dp else 0.dp)
                                )
                            }
                        }
                    }
                }
            }

            Button(
                onClick = { /* Add to bookmarks */ },
                modifier = Modifier.fillMaxWidth().height(56.dp).shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp), spotColor = SlatePrimary.copy(alpha = 0.3f)),
                colors = ButtonDefaults.buttonColors(containerColor = SlatePrimary),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("加入生词本", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            }
            
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
            Icon(Icons.Default.Cancel, contentDescription = "Close", tint = Color(0xFFE2E8F0), modifier = Modifier.size(24.dp))
        }
    }
}
