# Effective-Mobile

Учебно-демо Android-приложение: каталог курсов с поиском, сортировкой, избранным и базовой навигацией.  
Архитектура — **Clean Architecture + MVVM**, DI через Hilt, реактивные стейты на Coroutines/Flow.

---

## Цели и функциональность
- Каталог курсов с картинками, описанием и датами
- Поиск по заголовку (с дебаунсом)
- Сортировка (новые/старые)
- Добавление/удаление в «Избранное»
- Экран «Избранное» с отдельным списком
- Экран «Профиль» (UI-заглушка)

---

## Технологический стек
- **Язык:** Kotlin (KTX, coroutines, Flow)
- **DI:** Hilt (Dagger)
- **Сеть:** Retrofit + OkHttp, Moshi (+ KotlinJsonAdapterFactory)
- **Асинхронность:** Coroutines, structured concurrency, viewModelScope
- **UI:** XML + ViewBinding, RecyclerView + DiffUtil + ListAdapter, Material Components
- **Навигация:** Jetpack Navigation
- **Локальные данные:** SharedPreferences (заменимы на DataStore/Room)
- **Загрузка изображений:** Glide
- **Качество кода:** Detekt/ktlint, Gradle Kotlin DSL, конвенции именования

---

## Архитектура
Слоистая структура: `data → domain → presentation`

- **data** — DTO, мапперы, источники данных (remote/local), реализации репозиториев  
- **domain** — use-case, интерфейсы репозиториев, бизнес-модели (`Course`)  
- **presentation** — ViewModel (MVVM) + UiState (sealed), фрагменты, адаптеры  

Единый поток данных:  
`Repository → ViewModel (Flow/suspend) → UI (рендер по UiState)`  

Принципы: SOLID, чистые интерфейсы, неизменяемые модели.

---

## Модульная структура
- **app** — навигация, DI-модули, экраны, ViewModel’ы  
- **courses** — UI и модели каталога, адаптеры (без зависимостей на app)  
- **network** — Retrofit/Moshi/OkHttp-конфиг и API  
- **core** (опционально) — утилиты, base-компоненты, логгирование  

---

## DI и конфигурация
- `SingletonComponent`: Retrofit, OkHttp, Moshi, SharedPreferences  
- ViewModel-инжект через Hilt (репозитории и юзкейсы в конструкторе)  
- Вью-слой зависит только от интерфейсов `domain`  

---

## Сеть и данные
- Retrofit + Moshi (с Kotlin-reflect адаптером для data-классов)  
- OkHttp: логирование (debug), таймауты, перехватчики  
- Ошибки маппятся в `HomeUiState.Error`  

---

## UI и состояние
- `UiState` (Loading / Success / Empty / Error)  
- Центр-прогресс при первичной загрузке, SwipeRefresh для обновления  
- Поиск: дебаунс 300 мс + IME_ACTION_SEARCH  
- Сортировка по `publishDate`  
- Экран «Профиль» — UI-заглушка с кнопками-тостами  

---

## Избранное
- Хранение: `SharedPreferences` (сет id’шников)  
- Абстракция: `BookmarkChecker` / `BookmarkToggler` (в модуле courses)  
- UI обновляет иконку без перезагрузки экрана  
- Снятие из избранного сразу удаляет карточку из списка  

---

## Навигация
- Jetpack Navigation (single activity)  
- Graph: Home → Favorites → Profile  

---

## Качество кода
- Чистота: без циклических зависимостей  
- Расширяемость: миграция на DataStore/Room прозрачна для интерфейсов  
- Тестируемость: бизнес-логика в ViewModel/репозиториях, UI — тонкий слой  
- Надёжность: строгая обработка состояний, fail-safe парсинг дат  
- Производительность: DiffUtil/ListAdapter, lazy-рендер, дебаунс поиска  

---

## Трейд-оффы
- SharedPreferences вместо Room/DataStore (для скорости)  
- Поиск выполняется на клиенте (по заголовку), при необходимости легко вынести на сервер  

---

## Файлы, на которые можно смотреть
- [HomeFragment](app/src/main/java/com/effective/effectivemobile/ui/home/HomeFragment.kt) — set-up, render(UiState), дебаунс поиска  
- [HomeViewModel](app/src/main/java/com/effective/effectivemobile/ui/home/HomeViewModel.kt) — сортировка/фильтрация  
- [CoursesAdapter](courses/src/main/java/com/effective/courses/ui/CoursesAdapter.kt) — независим от app  
- [BookmarksPrefs](app/src/main/java/com/effective/effectivemobile/data/BookmarksPrefs.kt) — хранилище избранного  
- [FavoritesFragment](app/src/main/java/com/effective/effectivemobile/ui/favorites/FavoritesFragment.kt) — рендер списка избранного  

---

## Сборка и запуск
```bash
git clone https://github.com/username/effective-mobile.git
cd effective-mobile
./gradlew assembleDebug
