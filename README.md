# 辞林 (CiLin)

**辞林 (CiLin)** 是一款基于现代 Android 技术栈开发的词典与成语学习应用。它旨在通过优雅的 UI 设计、智能的搜索体验以及每日趣味练习，帮助用户更好地探索和掌握中华词语的魅力。

***

## 📱 真机效果

<div align="center">
  <img src="image/effect.png" width="80%" alt="CiLin App Effect"/>
</div>


***

## ✨ 核心功能

- **🔍 智能搜索**：支持词语、释义、拼音的实时模糊查询，快速定位所需词汇。
- **📖 词语详情**：深度同步后端数据，提供完整的拼音、详细释义、典故出处及丰富例句。
- **📝 每日练习**：每天推送 10 道精心挑选的题目，支持多种错误类型反馈（褒贬误用、望文生义等），巩固学习成果。
- **📂 分类浏览**：科学的词语分类体系，支持多级目录探索。
- **🎨 现代体验**：全量使用 Jetpack Compose 构建，支持沉浸式启动页与流畅的 Material 3 交互动画。

***

## 🛠️ 技术架构

本项目遵循 Google 推荐的现代 Android 开发最佳实践：

- **架构模式**：MVVM (Model-View-ViewModel) + 响应式编程流。
- **UI 框架**：Jetpack Compose (声明式 UI)。
- **网络通信**：Retrofit 2 + OkHttp 3 + Kotlin Coroutines (协程)。
- **依赖注入**：ViewModel Compose 注入。
- **资源管理**：Android Core SplashScreen 官方启动方案。

***

## 🚀 运行环境

| 关键组件                      | 版本               |
| :------------------------ | :--------------- |
| **Gradle**                | 8.7              |
| **Android Gradle Plugin** | 8.3.2            |
| **Kotlin**                | 1.9.23           |
| **JDK**                   | 21               |
| **Compile SDK**           | 34 (Android 14)  |
| **Min SDK**               | 24 (Android 7.0) |

***

## 📦 项目结构

```text
.
├── app/
│   └── src/main/java/com/example/yulin/
│       ├── network/        # API 定义、模型 (Models) 及网络配置
│       ├── ui/             # UI 界面 (Home, Search, Categories, Profile)
│       ├── utils/          # 工具类 (Toast 等)
│       └── CiLinApp.kt     # Application 类
├── gradle/                 # Gradle 相关配置
├── image/                  # 文档图片资源
└── build.gradle.kts        # 根项目构建脚本
```

***

## ⚙️ 快速开始

1. **克隆项目**：
   ```bash
   git clone https://github.com/your-username/cilin-android.git
   ```
2. **打开项目**：
   使用 **Android Studio (Iguana 或更高版本)** 打开根目录。
3. **构建与运行**：
   等待 Gradle 同步完成后，点击 **Run** 按钮安装到真机或模拟器即可。

***

## 📄 开源协议

本项目采用 [MIT License](LICENSE) 协议。
