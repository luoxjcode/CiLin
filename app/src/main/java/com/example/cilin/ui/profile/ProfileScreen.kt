package com.example.cilin.ui.profile

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cilin.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var emailInput by remember { mutableStateOf("") }
    var codeInput by remember { mutableStateOf("") }

    Box(modifier = modifier.fillMaxSize().background(PaperBg)) {
        AnimatedContent(targetState = uiState.isLoggedIn) { loggedIn ->
            if (!loggedIn) {
                // Logged Out View
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(Orange50, RoundedCornerShape(24.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = null,
                            tint = OrangePrimary,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    
                    Spacer(Modifier.height(40.dp))
                    
                    Text(
                        text = "欢迎归林",
                        style = MaterialTheme.typography.displayLarge.copy(fontSize = 32.sp),
                        color = SlatePrimary
                    )
                    Text(
                        text = "邮箱登录以同步学习进度",
                        style = MaterialTheme.typography.labelSmall,
                        color = Gray400,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    
                    Spacer(Modifier.height(40.dp))
                    
                    OutlinedTextField(
                        value = emailInput,
                        onValueChange = { emailInput = it },
                        placeholder = { Text("请输入您的电子邮箱", color = Gray400) },
                        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null, tint = Color(0xFFE2E8F0)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.White,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = OrangePrimary.copy(alpha = 0.2f),
                            cursorColor = OrangePrimary
                        )
                    )
                    
                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = codeInput,
                        onValueChange = { codeInput = it },
                        placeholder = { Text("请输入验证码", color = Gray400) },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = Color(0xFFE2E8F0)) },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        shape = RoundedCornerShape(20.dp),
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.White,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = OrangePrimary.copy(alpha = 0.2f),
                            cursorColor = OrangePrimary
                        )
                    )
                    
                    Button(
                        onClick = { 
                            if (!uiState.showCodeInput && codeInput.isBlank()) {
                                viewModel.getVerificationCode(emailInput)
                            } else {
                                viewModel.login(emailInput, codeInput)
                            }
                        },
                        enabled = !uiState.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp), spotColor = SlatePrimary.copy(alpha = 0.3f)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SlatePrimary,
                            disabledContainerColor = SlatePrimary.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                        } else {
                            val buttonText = if (uiState.countdown > 0) {
                                "立即登录 (${uiState.countdown}s)"
                            } else if (!uiState.showCodeInput && codeInput.isBlank()) {
                                "获取验证码"
                            } else {
                                "立即登录"
                            }
                            Text(buttonText, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        }
                    }
                    
                    if (uiState.showCodeInput && uiState.countdown == 0) {
                        TextButton(
                            onClick = { viewModel.getVerificationCode(emailInput) },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("重新获取验证码", color = OrangePrimary)
                        }
                    }
                }
            } else {
                // Logged In View
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    // Profile Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                            .shadow(elevation = 20.dp, shape = RoundedCornerShape(40.dp), spotColor = Color(0xFF7C2D12).copy(alpha = 0.05f)),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(40.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(Color(0xFFF1F5F9))
                                        .border(4.dp, Orange50, RoundedCornerShape(20.dp))
                                ) {
                                    Icon(
                                        Icons.Filled.Person,
                                        contentDescription = null,
                                        tint = Gray400,
                                        modifier = Modifier.fillMaxSize().padding(16.dp)
                                    )
                                }
                                
                                Spacer(Modifier.width(16.dp))
                                
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = uiState.userInfo?.name?.takeIf { it.isNotBlank() } ?: emailInput.ifBlank { "辞林书生" },
                                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                                        color = SlatePrimary,
                                        maxLines = 1
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(top = 4.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .background(Color(0xFF4ADE80), CircleShape)
                                        )
                                        Spacer(Modifier.width(6.dp))
                                        Text(
                                            text = "ONLINE STUDENT",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, letterSpacing = 1.sp),
                                            color = Gray400,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                            
                            Spacer(Modifier.height(24.dp))
                            Divider(color = Color(0xFFF8FAFC))
                            Spacer(Modifier.height(24.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("12", style = MaterialTheme.typography.titleLarge, color = SlatePrimary)
                                    Text("坚持天数", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = Gray400)
                                }
                                
                                Box(modifier = Modifier.width(1.dp).height(32.dp).background(Color(0xFFF1F5F9)))
                                
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Filled.MilitaryTech, contentDescription = null, tint = Color(0xFFE2E8F0), modifier = Modifier.size(20.dp))
                                    Text("辞林成就", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = Gray400)
                                }
                            }
                        }
                    }

                    // Menu
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(32.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF8FAFC))
                    ) {
                        Column {
                            MenuItem(Icons.Filled.Bookmark, "我的收藏", OrangePrimary)
                            Divider(color = Color(0xFFF8FAFC), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
                            MenuItem(Icons.Filled.History, "练习记录", Color(0xFF3B82F6))
                        }
                    }
                    
                    Spacer(Modifier.weight(1f))
                    
                    TextButton(
                        onClick = { viewModel.logout() },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            "退出账号",
                            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.sp),
                            color = Red300,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MenuItem(icon: ImageVector, label: String, iconColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Navigate */ }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = SlatePrimary,
            modifier = Modifier.weight(1f)
        )
        Icon(
            Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.size(16.dp)
        )
    }
}
