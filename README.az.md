# Effective-Mobile

Android üçün demo-tətbiq: kurs kataloqu, axtarış, çeşidləmə, sevimlilər və əsas naviqasiya.  
Arxitektura — **Clean Architecture + MVVM**, Hilt vasitəsilə DI, Coroutines/Flow ilə reaktiv vəziyyət idarəsi.

---

## 🌟 Məqsədlər və funksionallıq
- Şəkillərlə kurs kataloqu, təsvir və tarixlər  
- Başlığa görə axtarış (debounce ilə)  
- Çeşidləmə (yenilər / köhnələr)  
- “Sevimlilərə” əlavə/çıxarış  
- Ayrı “Sevimlilər” ekranı  
- “Profil” ekranı (UI-zagluşka)  

---

## ⚙️ Texnoloji stek
- **Dil:** Kotlin (KTX, coroutines, Flow)  
- **DI:** [Hilt](app/src/main/java/com/effective/effectivemobile/di/NetworkModule.kt)  
- **Şəbəkə:** [Retrofit](network/src/main/java/com/effective/core/network/api/CoursesApi.kt) + OkHttp, [Moshi](network/src/main/java/com/effective/core/network/di/NetworkModule.kt)  
- **Asinxronluq:** [Kotlin Coroutines](app/src/main/java/com/effective/effectivemobile/ui/home/HomeViewModel.kt), structured concurrency, viewModelScope  
- **UI:** XML + ViewBinding, RecyclerView + DiffUtil + ListAdapter, Material Components  
- **Naviqasiya:** Jetpack Navigation (single-activity)  
- **Yerli data:** [SharedPreferences](app/src/main/java/com/effective/effectivemobile/data/BookmarksPrefs.kt) (asanlıqla DataStore/Room ilə əvəzlənir)  
- **Şəkil yükləmə:** [Glide](courses/src/main/java/com/effective/courses/ui/CoursesAdapter.kt)  
- **Kod keyfiyyəti:** Detekt/ktlint, Gradle Kotlin DSL  

---

## 🏗 Arxitektura
Laylar: `data → domain → presentation`

- **data** — DTO, mapper-lər, mənbələr (remote/local), repository-lərin implementasiyası  
- **domain** — use-case, repository interfeysləri, biznes modelləri (`Course`)  
- **presentation** — ViewModel (MVVM) + UiState (sealed), fragmentlər, adapterlər  

Məlumat axını:  
`Repository → ViewModel (Flow/suspend) → UI (UiState ilə render)`  

---

## 📦 Modul strukturu
- **app** — naviqasiya, DI modulları, ekranlar, ViewModel-lər  
- **courses** — kataloq UI və modellər, adapterlər (app-dan asılı deyil)  
- **network** — Retrofit/Moshi/OkHttp konfiqurasiyası və API  
- **core** (opsional) — util-lər, base komponentlər, logging  

---

## 📸 Skreenshotlar

| Login | Home | Profile |
|-------|------|---------|
| ![Login](1.jpeg) | ![Home](3.jpeg) | ![Profile](2.jpeg) |

| Login | Favorite |
|-------|----------|
| ![Login](4.jpeg) | ![Favorite](5.jpeg) |

---

## 🚀 Qurulma və işə salma
```bash
git clone https://github.com/username/effective-mobile.git
cd effective-mobile
./gradlew assembleDebug
