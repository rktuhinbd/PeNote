# PeNote - Offline-First Notes App

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.20-blue.svg)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-API%2024%2B-green.svg)](https://developer.android.com/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2023.10.01-orange.svg)](https://developer.android.com/jetpack/compose)
[![Room](https://img.shields.io/badge/Room-2.6.1-blue.svg)](https://developer.android.com/training/data-storage/room)
[![Hilt](https://img.shields.io/badge/Hilt-2.48-purple.svg)](https://dagger.dev/hilt/)

A modern, offline-first notes application built with **Kotlin** and **Jetpack Compose** following **Clean Architecture** principles. The app provides seamless note-taking experience with automatic background synchronization when internet connectivity is available.

## 🚀 Features

- ✅ **Create, Edit, Delete, and View Notes** - Full CRUD operations for notes
- 📱 **Offline-First Architecture** - All notes stored locally in Room Database
- 🔄 **Automatic Background Sync** - Syncs with server when internet is available using WorkManager
- 🌐 **Connectivity Awareness** - Shows offline banner when no internet connection
- 🎨 **Modern UI** - Built with Jetpack Compose and Material Design 3
- 🏗️ **Clean Architecture** - Domain, Data, and UI layers with MVVM pattern
- 💉 **Dependency Injection** - Hilt for clean dependency management
- 🔄 **Reactive UI** - Coroutines + Flow for reactive data streams

## 🏗️ Architecture

The app follows **Clean Architecture** with three distinct layers:

### Domain Layer
- **Models**: `Note` data class
- **Use Cases**: 
  - `GetNotesUseCase` - Retrieve all notes
  - `AddNoteUseCase` - Create new notes
  - `UpdateNoteUseCase` - Modify existing notes
  - `DeleteNoteUseCase` - Remove notes
  - `SyncNotesUseCase` - Synchronize with server
- **Repository Interface**: `NotesRepository`

### Data Layer
- **Local Database**: Room with `NoteEntity` and `NoteDao`
- **Remote API**: Retrofit with `NotesApiService`
- **Repository Implementation**: `NotesRepositoryImpl` with Network Bound Resource pattern
- **Mappers**: `NoteMapper` for data transformation
- **Sync Management**: `SyncWorker` and `SyncManager` for background sync
- **Connectivity**: `ConnectivityManager` for network state monitoring

### UI Layer
- **Screens**: `HomeScreen`, `AddEditNoteScreen`, `NoteDetailScreen`
- **Components**: `NoteCard`, `OfflineBanner`, `SyncingIndicator`
- **ViewModels**: `NotesViewModel`, `NoteDetailViewModel`
- **State Management**: `UIState` sealed class for Loading/Success/Error states

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| **Kotlin** | 1.9.20 | Programming language |
| **Jetpack Compose** | 2023.10.01 | Modern UI toolkit |
| **Room Database** | 2.6.1 | Local data persistence |
| **Retrofit** | 2.9.0 | Network API calls |
| **OkHttp** | 4.12.0 | HTTP client |
| **Hilt** | 2.48 | Dependency injection |
| **WorkManager** | 2.9.0 | Background sync |
| **Coroutines** | 1.7.3 | Asynchronous programming |
| **Flow** | Built-in | Reactive streams |
| **DataStore** | 1.0.0 | User preferences |

## 🔄 Offline-First Sync Strategy

The app implements a sophisticated offline-first synchronization strategy:

### 1. **Local-First Data Access**
- All notes are stored locally in Room Database
- UI always reads from local cache for instant response
- No loading states for cached data

### 2. **Network Bound Resource Pattern**
```kotlin
// Repository always serves local data first
override fun getAllNotes(): Flow<List<Note>> {
    return noteDao.getAllNotes().map { entities ->
        entities.map { NoteMapper.entityToDomain(it) }
    }
}
```

### 3. **Background Synchronization**
- **WorkManager** handles background sync tasks
- Sync only when network is available
- Exponential backoff for failed sync attempts
- Periodic sync every 15 minutes when connected

### 4. **Conflict Resolution**
- **Timestamp-based**: Latest edit wins
- Server data takes precedence during sync
- Local changes marked with `isSynced` flag

### 5. **Connectivity Awareness**
- Real-time network state monitoring
- Offline banner with retry option
- Automatic sync trigger when connectivity returns

## 📱 Key Functionalities

### Note Management
- **Create**: Add new notes with title and content
- **Read**: View all notes in a scrollable list
- **Update**: Edit existing notes
- **Delete**: Remove notes with confirmation dialog

### Offline Support
- **Local Storage**: All notes persisted in Room Database
- **Offline Indicator**: Banner shows when no internet connection
- **Pending Sync**: Visual indicator for unsynced notes
- **Retry Mechanism**: Manual sync retry option

### Background Sync
- **Automatic**: Syncs when network becomes available
- **Periodic**: Regular sync every 15 minutes
- **Efficient**: Only syncs unsynced notes
- **Reliable**: Retry mechanism for failed syncs

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK API 24+
- Kotlin 1.9.20+

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/penote.git
   cd penote
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. **Configure API Endpoint**
   - Update `NetworkModule.kt` with your server URL:
   ```kotlin
   .baseUrl("https://your-api-server.com/")
   ```

4. **Build and Run**
   - Sync project with Gradle files
   - Build the project (Ctrl+F9)
   - Run on device or emulator (Shift+F10)

### Project Structure

```
app/src/main/java/com/rkt/penote/
├── data/
│   ├── connectivity/
│   │   └── ConnectivityManager.kt
│   ├── local/
│   │   ├── converter/
│   │   │   └── DateConverter.kt
│   │   ├── dao/
│   │   │   └── NoteDao.kt
│   │   ├── database/
│   │   │   └── NotesDatabase.kt
│   │   └── entity/
│   │       └── NoteEntity.kt
│   ├── mapper/
│   │   └── NoteMapper.kt
│   ├── remote/
│   │   ├── api/
│   │   │   └── NotesApiService.kt
│   │   └── dto/
│   │       ├── NoteDto.kt
│   │       └── SyncRequestDto.kt
│   ├── repository/
│   │   └── NotesRepositoryImpl.kt
│   └── sync/
│       ├── SyncManager.kt
│       └── SyncWorker.kt
├── di/
│   ├── DatabaseModule.kt
│   ├── NetworkModule.kt
│   ├── RepositoryModule.kt
│   └── WorkManagerModule.kt
├── domain/
│   ├── model/
│   │   └── Note.kt
│   ├── repository/
│   │   └── NotesRepository.kt
│   └── usecase/
│       ├── AddNoteUseCase.kt
│       ├── DeleteNoteUseCase.kt
│       ├── GetNotesUseCase.kt
│       ├── SyncNotesUseCase.kt
│       └── UpdateNoteUseCase.kt
├── ui/
│   ├── components/
│   │   ├── NoteCard.kt
│   │   ├── OfflineBanner.kt
│   │   └── SyncingIndicator.kt
│   ├── screen/
│   │   ├── AddEditNoteScreen.kt
│   │   ├── HomeScreen.kt
│   │   └── NoteDetailScreen.kt
│   ├── state/
│   │   └── UIState.kt
│   └── viewmodel/
│       ├── NoteDetailViewModel.kt
│       └── NotesViewModel.kt
├── MainActivity.kt
└── PeNoteApplication.kt
```

## 🔧 Configuration

### API Configuration
Update the base URL in `NetworkModule.kt`:
```kotlin
.baseUrl("https://your-api-server.com/")
```

### Sync Frequency
Modify sync interval in `SyncManager.kt`:
```kotlin
PeriodicWorkRequestBuilder<SyncWorker>(
    15, TimeUnit.MINUTES  // Change this value
)
```

### Database Version
Update database version in `NotesDatabase.kt` when schema changes:
```kotlin
@Database(
    entities = [NoteEntity::class],
    version = 1,  // Increment for schema changes
    exportSchema = false
)
```

## 🧪 Testing

The app is structured to support comprehensive testing:

### Unit Tests
- Use cases can be tested with mock repositories
- Repository implementation can be tested with mock DAO and API
- ViewModels can be tested with mock use cases

### Integration Tests
- Database operations can be tested with in-memory Room database
- Network operations can be tested with mock web server

### UI Tests
- Compose UI can be tested with ComposeTestRule
- Navigation can be tested with TestNavHostController

## 🚀 Future Improvements

- [ ] **Search & Filter** - Add search functionality for notes
- [ ] **Categories/Tags** - Organize notes with categories
- [ ] **Rich Text** - Support for formatting and media
- [ ] **Dark Theme** - Implement theme switching
- [ ] **Export/Import** - Backup and restore notes
- [ ] **Collaboration** - Share notes with other users
- [ ] **Attachments** - Support for images and files
- [ ] **Voice Notes** - Record and transcribe audio
- [ ] **Widgets** - Home screen widgets for quick access
- [ ] **Biometric Security** - Protect notes with fingerprint/face unlock

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [Android Developers](https://developer.android.com/) for excellent documentation
- [Jetpack Compose](https://developer.android.com/jetpack/compose) for modern UI toolkit
- [Room Database](https://developer.android.com/training/data-storage/room) for local persistence
- [Hilt](https://dagger.dev/hilt/) for dependency injection
- [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) for background processing

---

**Built with ❤️ using Kotlin and Jetpack Compose**