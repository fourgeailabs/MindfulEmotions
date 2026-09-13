# Mindful Emotions

[![Android Build & Release APK](https://github.com/fourgeailabs/mindfulemotions/actions/workflows/build.yml/badge.svg)](https://github.com/fourgeailabs/mindfulemotions/actions/workflows/build.yml)
**Version:** `1.05.01` | **Package ID:** `com.fourgeailabs.mindfulemotions`

A peaceful, calming mindfulness reminder application designed with soft warm pastel aesthetics and minimalist typography. Its primary purpose is to present full-screen check-ins at scheduled times throughout the day, prompting users to pause, become aware of their emotional state, reflect on curated wisdom quotes, and dismiss notifications via an intuitive tactile swipe-away gesture.

---

## 🌸 Key Features

- **Calm Corner & Guided Breathing Exercise:**
  - Dedicated de-escalation sanctuary featuring a guided expanding and shrinking breathing orb animation.
  - Smooth visual transitions synchronized with inhalation, breath retention, exhalation, and stillness pauses.
  - Multiple evidence-based breathing techniques:
    - **Box Breathing (4-4-4-4):** Used for instant focus, calming heart rate, and resetting the nervous system.
    - **4-7-8 Relaxing Breath:** Powerful vagal nerve activation to rapidly de-escalate acute anxiety and distress.
    - **Calm Balance (4-4):** Simple equal rhythm for effortless centering anytime during the day.
    - **Deep Sigh (4-2-6):** Extended exhalation rhythm to release physical and emotional tension.
  - Gentle sensory haptic cues on breath phase transitions, enabling eyes-closed mindfulness practice.
  - Collapsible Body De-escalation Checklist with 3 somatic physical resets: dropping shoulders, unclenching the jaw, and grounding feet.
  - Instant one-tap access directly from the Home Screen.
- **Home Page Top-Center Wisdom Quotes:**
  - Featured prominent wisdom reflections positioned front-and-center on the app's home screen.
  - Centered theme badges, quotation marks, and calming typography for daily encouragement.
  - Interactive shuffle action and card-tap gesture with tactile micro-haptics to discover new quotes on anger, love, life, peace, and gratitude anytime.
- **Full-Screen Emotion Check-In:**
  - **Top Center Mindful Inquiry:** Displays gentle, customizable mindfulness prompts (e.g., *"How are you feeling right now in this moment?"*, *"Take a deep breath. What emotion is present?"*).
  - **Center Swipe-Away Circle:** Calming, glowing breathing disc in the center with smooth gesture physics and micro-tick tactile feedback when reaching drag thresholds. Swipe or drag outwards to mindfully dismiss the alert with confirmation haptics.
  - **Bottom Center Curated Wisdom Quotes:** Curated 1–2 sentence reflections on emotions, life, love, and anger—focusing on why destructive negative emotions are not worth holding onto, and why cultivating positive emotions nurtures you and those around you.
- **Subtle Tactile & Haptic Feedback:**
  - Subtle, soothing sensory vibration triggers when swiping away an emotion check-in notification from Android's status bar.
  - Interactive haptic micro-ticks during swipe gestures and distinct, soft completion feedback on dismissal.
  - Gentle sensory feedback upon completing interactions: saving mood reflections, scheduling check-ins, cycling quotes, breathing cues, or selecting emotion feeling chips.
  - Fully customizable via a dedicated "Subtle Haptic Feedback" switch in Settings.
- **1,000 Daily Mindfulness Prompts:**
  - Built-in library of 1,000 gentle, introspective prompts encouraging users to reflect on their day, emotions, growth, and gratitude.
  - Formatted for automatic daily rotation, random inspiration, or mindful journal prompts.
- **Private Mood Journaling:**
  - **Post-Dismissal Reflection:** After swiping away an emotion check, an unobtrusive prompt gently invites users to log their feeling and optional note without interrupting their peaceful state.
  - **Dedicated Journal Screen:** A quiet, private haven within the app to browse past entries, filter reflections by emotion, and write impromptu mindful thoughts.
  - **Offline & Private:** All thoughts are persisted locally using encrypted on-device SQLite (Room) with zero tracking.
- **Zero Preset Schedules & Pure Flexible Scheduling:**
  - No pre-configured times, days, or dates out of the box—users have complete autonomy.
  - **Specific Date Mode:** Schedule a one-time emotion check for a chosen calendar date (with date picker) and exact time.
  - **Repeating Days Mode:** Choose any combination of days (Mon–Sun) with no days selected by default.
  - One-time specific date schedules automatically deactivate once fired to keep the reminder list clean.
- **System Alarm Integration:**
  - Option to trigger check-ins with Android's built-in alarm audio stream and wake lock (`AlarmClockInfo` / `STREAM_ALARM`), ensuring prompt delivery even when the device is in deep sleep.
- **Warm, Kind, Pastel Aesthetics:**
  - Light mode only with soothing blush, warm peach, sage green, and lavender pastel tones.
  - Clean, minimalist typography adhering to Material 3 design guidelines.
- **FourgeAI LABS Integration & Release Notes:**
  - Built-in "What's New" accordion with version-by-version release history.
  - Interactive "About" section attributing creation to [FourgeAI LABS](https://github.com/fourgeailabs).

---

## 📋 What's New & Release History

### Version 1.05.01 (Current Release)
- **GitHub Actions Keystore Signing Fix**: Resolved `validateSigningDebug` keystore validation failure on CI by introducing dual-method keystore provisioning and graceful fallback checks in Gradle configuration.
- **Universal CI/CD Triggers**: Configured automatic triggers for all branches, pull requests, and manual workflow dispatches.

### Version 1.05.00
- **Calm Corner Section & Guided Breathing Exercise:**
  - Added a dedicated Calm Corner tab and Home page entry card for de-escalating stress and anxiety.
  - Implemented an animated shrinking and expanding breathing circle with soothing pastel radiance and real-time seconds countdown.
  - Included 4 evidence-based breathing presets: Box Breathing (4-4-4-4), 4-7-8 Relaxing Breath, Calm Balance (4-4), and Deep Sigh (4-2-6).
  - Integrated gentle sensory haptic cues on breath phase changes (inhale, hold, exhale) for eyes-closed relaxation.
  - Added an interactive Body De-escalation Checklist with 3 somatic grounding cues (drop shoulders, unclench jaw, ground feet).
  - Track total breath cycles completed during each session.

### Version 1.04.00
- **Top-Center Wisdom Quotes on Home Page:** Placed curated quotes front-and-center on the main home screen, providing an uplifting daily reminder every time the user opens the app.
- **Interactive Quote Cycling & Shuffle:** Users can tap the card or the refresh button to effortlessly cycle through wisdom reflections on peace, gratitude, love, life, and anger.
- **Haptic Tactile Polish:** Added gentle micro-ticks when cycling quotes on the home page for a soothing physical sensation.
- **Visual Design Harmony:** Framed the top quote in a gentle, warm surface card with centered badges and typography aligned with Material 3.

### Version 1.03.00
- **Subtle Notification Dismissal Haptics:** Integrated gentle tactile feedback triggered when the user swipes away an emotion check-in notification directly from Android's notification drawer or heads-up banner.
- **In-App Swipe Dismissal Physics & Haptic Micro-Ticks:** Added threshold feedback while dragging the floating breathing circle and gentle confirmation haptics on successful dismissal.
- **Interaction Completion Feedback:** Added responsive micro-haptics when saving reflection entries, creating schedules, cycling prompts, and selecting emotion chips.
- **Subtle Haptics Setting:** Introduced a "Subtle Haptic Feedback" preference toggle in Settings, allowing users to customize tactile sensations to their liking.
- **Notification Delete Intent Integration:** Added `.setDeleteIntent()` handling to ensure dismissal haptics reliably fire whenever a notification is cleared.

### Version 1.02.00
- **Zero Preset Schedules:** Removed all preset schedules, default times, and pre-selected dates/days. The app starts completely clean and empowers the user to schedule precisely when they want.
- **Specific Calendar Date Scheduling:** Users can now pick an exact calendar date (Year, Month, Day) using an interactive DatePickerDialog or convenient "Today" and "Tomorrow" shortcuts.
- **Flexible Repeating Days:** Pick custom combinations of days (Mon–Sun) with all days unchecked by default.
- **Automatic One-Time Schedule Deactivation:** When a date-specific check-in triggers and is dismissed, it is automatically marked inactive so that completed one-time reminders do not linger as active.
- **Legacy Preset Cleanup:** Automatically detects and purges legacy preset schedules from earlier versions on first startup.
- **Room Database Migration v3:** Upgraded Room database to version 3 with `MIGRATION_2_3` supporting nullable `specificYear`, `specificMonth`, and `specificDay` fields.

### Version 1.01.00
- **1,000 Daily Mindfulness Prompts:** Generated and integrated a rich repository of 1,000 one-to-two sentence contemplative prompts covering gratitude, resilience, kindness, and self-compassion.
- **Mood Journaling Engine:** Implemented an intuitive, private mood tracking experience. After dismissing a check-in, users receive a gentle option to record their emotion and write a brief note.
- **Dedicated Journal Tab:** Added a peaceful dedicated tab to view past reflections, filter by emotion category, and record spontaneous mindful notes.
- **Removed Watch Companion:** Removed the smartwatch companion layer as requested to streamline and declutter the user experience, focusing entirely on pure mobile mindfulness.
- **Updated Navigation & Experience:** Smooth transitions from the swipe-away check-in to optional journal reflection.

### Version 1.00.00 (Initial Release)
- **Mindful Schedule Engine:** Full Monday through Sunday schedule customization with multi-time daily reminders.
- **Full-Screen Emotion Check:** Implemented immersive wake-up dialog with customizable check-in questions, tactile swipe-away circle dismissal, and dynamic quote engine.
- **Curated Wisdom Quotes:** 50+ hand-curated quotes about anger release, peace, love, and emotional balance.
- **Android Alarm Integration:** Support for system alarm stream and exact wake scheduling.
- **Warm Pastel Theme:** Exclusive light-mode design with warm tones, zero dark mode clutter, and accessible touch targets.
- **What's New & About Menus:** Accordion-based update notes and FourgeAI LABS creator links.

---

## 🛠️ Building & Installation

### Prerequisites
- Android Studio Ladybug or newer
- JDK 17
- Android SDK 36 (Minimum SDK 24)

### Build APK via Gradle
```bash
./gradlew assembleDebug
```
The resulting APK is generated at:
`app/build/outputs/apk/debug/app-debug.apk`

---

## 👨‍💻 Creator & Attribution
- **App Creator:** [FourgeAI LABS](https://github.com/fourgeailabs)
- **Repository:** [https://github.com/fourgeailabs/mindfulemotions](https://github.com/fourgeailabs)
