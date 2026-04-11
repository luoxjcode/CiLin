package com.example.yulin.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yulin.ui.theme.*

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PaperBg)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(color = OrangePrimary)
            }
            uiState.error != null -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = uiState.error!!, color = Red500)
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.fetchDailyQuiz() },
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                    ) {
                        Text("重试")
                    }
                }
            }
            uiState.questions.isNotEmpty() && uiState.currentIndex != -1 -> {
                val currentQuestion = uiState.questions[uiState.currentIndex]
                val selectedOptionIndex = uiState.answers[uiState.currentIndex]
                
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Header
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(bottom = 32.dp)
                    ) {
                        Text(
                            text = "辞林",
                            style = MaterialTheme.typography.headlineMedium,
                            color = SlatePrimary
                        )
                        Text(
                            text = "DAILY WISDOM",
                            style = MaterialTheme.typography.labelSmall,
                            color = Gray500,
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Quiz Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                            .shadow(elevation = 20.dp, shape = RoundedCornerShape(32.dp), spotColor = OrangePrimary.copy(alpha = 0.1f)),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(32.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Orange50)
                    ) {
                        key(uiState.currentIndex) {
                            Column(
                                modifier = Modifier.padding(24.dp)
                            ) {
                                Text(
                                    text = "今日练习 ${uiState.currentIndex + 1}/${uiState.questions.size}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = OrangePrimary.copy(alpha = 0.8f),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                
                                Text(
                                    text = currentQuestion.word,
                                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 40.sp),
                                    color = SlatePrimary,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                
                                Text(
                                    text = currentQuestion.pinyin,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontStyle = FontStyle.Italic,
                                    color = Gray400,
                                    modifier = Modifier.padding(bottom = 32.dp)
                                )

                                currentQuestion.options.forEachIndexed { index, option ->
                                    val isSelected = selectedOptionIndex == index
                                    val isCorrect = option.isCorrect()
                                    
                                    val borderColor = when {
                                        isSelected && isCorrect -> Green600
                                        isSelected && !isCorrect -> Red500
                                        else -> Color(0xFFF1F5F9)
                                    }
                                    val bgColor = when {
                                        isSelected && isCorrect -> Green50
                                        isSelected && !isCorrect -> Color(0xFFFEF2F2)
                                        else -> Color.White
                                    }

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 12.dp)
                                            .border(2.dp, borderColor, RoundedCornerShape(20.dp))
                                            .background(bgColor, RoundedCornerShape(20.dp))
                                            .clickable {
                                                viewModel.selectOption(index)
                                            }
                                            .padding(20.dp)
                                    ) {
                                        val annotatedText = buildAnnotatedString {
                                            val content = option.content
                                            val keyword = currentQuestion.word
                                            var startIndex = 0
                                            while (startIndex < content.length) {
                                                val foundIndex = content.indexOf(keyword, startIndex)
                                                if (foundIndex == -1) {
                                                    append(content.substring(startIndex))
                                                    break
                                                } else {
                                                    append(content.substring(startIndex, foundIndex))
                                                    pushStringAnnotation(tag = "keyword", annotation = "keyword")
                                                    append(keyword)
                                                    pop()
                                                    startIndex = foundIndex + keyword.length
                                                }
                                            }
                                        }
                                        
                                        var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
                                        val textColor = if (isSelected) SlatePrimary else Slate700

                                        Text(
                                            text = annotatedText,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Medium,
                                            color = textColor,
                                            onTextLayout = { textLayoutResult = it },
                                            modifier = Modifier.drawBehind {
                                                textLayoutResult?.let { layoutResult ->
                                                    annotatedText.getStringAnnotations("keyword", 0, annotatedText.length)
                                                        .forEach { annotation ->
                                                            val startOffset = annotation.start
                                                            val endOffset = annotation.end
                                                            val startLine = layoutResult.getLineForOffset(startOffset)
                                                            val endLine = layoutResult.getLineForOffset(endOffset - 1)
                                                            
                                                            for (line in startLine..endLine) {
                                                                val lineStart = if (line == startLine) startOffset else layoutResult.getLineStart(line)
                                                                val lineEnd = if (line == endLine) endOffset else layoutResult.getLineEnd(line)
                                                                val left = layoutResult.getHorizontalPosition(lineStart, true)
                                                                val right = layoutResult.getHorizontalPosition(lineEnd, true)
                                                                val bottom = layoutResult.getLineBottom(line) - 2.dp.toPx()
                                                                
                                                                drawLine(
                                                                    color = textColor,
                                                                    start = Offset(left, bottom),
                                                                    end = Offset(right, bottom),
                                                                    strokeWidth = 1.dp.toPx(),
                                                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                                                                )
                                                            }
                                                        }
                                                }
                                            }
                                        )
                                    }
                                }

                                // Feedback
                                AnimatedVisibility(
                                    visible = selectedOptionIndex != -1,
                                    enter = fadeIn() + slideInVertically(),
                                    exit = fadeOut()
                                ) {
                                    val selectedOption = currentQuestion.options[selectedOptionIndex.coerceAtLeast(0)]
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 16.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (selectedOption.isCorrect()) {
                                            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Green600, modifier = Modifier.size(16.dp))
                                            Spacer(Modifier.width(8.dp))
                                            Text("太棒了！理解正确", color = Green600, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        } else {
                                            Icon(Icons.Filled.Cancel, contentDescription = null, tint = Red500, modifier = Modifier.size(16.dp))
                                            Spacer(Modifier.width(8.dp))
                                            Text("错误原因：${selectedOption.getErrorReason()}", color = Red500, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Navigation Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(
                            onClick = { viewModel.previousQuestion() },
                            enabled = uiState.currentIndex > 0,
                            colors = ButtonDefaults.textButtonColors(contentColor = SlatePrimary)
                        ) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("上一题")
                        }

                        TextButton(
                            onClick = { viewModel.nextQuestion() },
                            enabled = uiState.currentIndex < uiState.questions.size - 1,
                            colors = ButtonDefaults.textButtonColors(contentColor = SlatePrimary)
                        ) {
                            Text("下一题")
                            Spacer(Modifier.width(8.dp))
                            Icon(Icons.Filled.ArrowForward, contentDescription = null)
                        }
                    }
                }
            }
            else -> {
                Text(text = "暂无题目", color = SlatePrimary)
            }
        }
    }
}
