# Friends Mod (NeoForge 1.21.1)

Мод додає "anomaly"-ботів: **anomaly006, anomaly008, anomaly009, anomaly010, anomaly011**.
Вони:

- виглядають як гравці (кастомна модель-скін через ресурспак мода);
- переслідують гравця в темних місцях і тихо телепортуються ближче, поза полем зору;
- лякають скримером: звук + fullscreen флеш + різка "тряска" камери + повідомлення в чат
  + короткочасні реальні ефекти (нудота/сліпота);
- **з'являються за ніком у списку гравців (клавіша Tab)**, поки хтось із гравців неподалік.

## Структура

```
friendsmod/
  build.gradle, settings.gradle, gradle.properties   — Gradle NeoForge (ModDevGradle)
  src/main/java/com/friendsmod/
    FriendsMod.java                — головний клас мода
    config/FriendsConfig.java      — конфіг (config/friendsmod-common.toml після першого запуску)
    entity/AnomalyEntity.java      — сутність-бот (спільна для всіх варіантів)
    entity/ai/StalkPlayerGoal.java — переслідування + телепорт у темряві
    entity/ai/JumpscareGoal.java   — тригер скримера
    network/JumpscarePayload.java  — пакет сервер->клієнт для скримера
    taglist/FakeTabListManager.java — показ ніку бота в Tab-списку гравців
    registry/ModEntities.java, ModItems.java
    client/...                     — рендерер, HUD-оверлей, обробка пакета
    event/ServerEvents.java        — команда /friendsmod summon <variant>
  src/main/resources/
    META-INF/neoforge.mods.toml
    assets/friendsmod/textures/entity/anomalyXXX.png  — ПЛЕЙСХОЛДЕР-скіни (згенеровані шумом)
```

## Збірка

Потрібні: JDK 21, інтернет-доступ (Gradle сам підтягне Minecraft/NeoForge з maven.neoforged.net).
Gradle wrapper вже в архіві (`gradlew`, `gradlew.bat`, `gradle/wrapper/...`) — окремо
встановлювати Gradle не треба.

```bash
cd friendsmod
./gradlew build      # macOS/Linux
gradlew.bat build    # Windows
```

Версії в проєкті (`gradle.properties`, `build.gradle`) звірені з офіційним
NeoForge MDK для 1.21.1 (net.neoforged.moddev 2.0.141, neo_version 21.1.235).

Готовий джар з'явиться в `build/libs/friendsmod-1.0.0.jar`. Кинь його в `mods/` NeoForge 1.21.1
клієнта та/або сервера (мод потрібен на обох сторонах, бо він мережевий).

Для розробки/дебагу: `gradle runClient` або `gradle runServer`.

> Якщо збірка впаде через дрібні відмінності мапінгів (Mojang-мапінги іноді трохи змінюються
> між патч-версіями 1.21.1), найчастіше досить поправити 1-2 імпорти/сигнатури методів —
> решта архітектури (реєстрація, AI, пакети, tab-list) правильна і не зміниться.

## Як викликати бота

```
/friendsmod summon anomaly011
```
(потрібні права оператора, рівень 2). Бот з'явиться за кілька блоків від гравця.

Або видай собі яйце-спавнер: `/give @s friendsmod:anomaly011_spawn_egg`.

## Скіни ботів

`assets/friendsmod/textures/entity/anomalyXXX.png` зараз — це **згенеровані шумом
плейсхолдери** (64×64, стандартний layout скіна гравця), щоб мод одразу працював
і нічого не крешило. Заміни їх на свої моторошні PNG 64×64 з тим самим layout'ом
(як у стандартного скіна Steve) — рендерер підхопить автоматично, назва файлу
має збігатися з ніком боту (`anomaly011.png` для `anomaly011` і т.д.).

## Про Tab-список

Технічно бот у Tab-списку — це `FakePlayer` (офіційний механізм NeoForge для
"гравця без мережевого підключення"), для якого сервер шле ті самі пакети
`ClientboundPlayerInfoUpdatePacket`, якими оновлюється список звичайних гравців.
**Обмеження протоколу:** без підписаної Mojang-сервером skin-property в
GameProfile сам маленький аватар у Tab-списку буде дефолтний Steve/Alex —
підписати текстуру без офіційного акаунта Mojang неможливо. Вигляд бота
**в самій грі** (сутність, яку видно у світі) від цього не залежить і використовує
кастомну текстуру мода.

## Конфіг

`config/friendsmod-common.toml`: увімкнення/вимкнення мода, ліміт ботів на світ,
кулдаун скримера, які варіанти дозволені, дальність показу в Tab-списку, які типи
лякання активні.

## Нотатка

Мод — суто ігровий "хоррор"-контент (як популярні "Anomaly"/"Friends" моди на
модпаках жахів) для одиночної гри чи приватного сервера з друзями. Немає жодних
реальних зловмисних функцій — не збирає дані, не робить нічого поза грою.
