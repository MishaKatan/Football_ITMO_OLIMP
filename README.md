# Football Stats

## Что умеет приложение

- Главный экран: матчи ближайших 2 дней (дата/время, команды, лига, страна).
- Экран поиска: выбор лиги + период (день/неделя/месяц), и отдельная кнопка «Архив» для завершённых матчей выбранной лиги.
- Экран матча: подробная карточка матча + ссылки на API‑эндпоинты.

## Как запустить

1. Открой проект в Android Studio.
2. Дай Gradle синхронизироваться.
3. Запусти на эмуляторе или физическом устройстве.

## Важные заметки (про сеть)

Если приложение «вечно грузит», чаще всего это SSL‑ошибка на эмуляторе:

SSLHandshakeException: Trust anchor for certification path not found

Для удобства в debug‑сборке включён обход SSL (только в debug). В release его быть не должно.
Если хочешь без обхода — используй свежий образ эмулятора (Android 11+ Google Play image) и проверь прокси/антивирус.

## Структура проекта

- app/src/main/java/com/sstats/footballstats/api — Retrofit и API интерфейсы
- app/src/main/java/com/sstats/footballstats/model — модели для JSON
- app/src/main/java/com/sstats/footballstats/ui — экраны и адаптер
- app/src/main/java/com/sstats/footballstats/util — даты и часовой пояс
- app/src/main/res — разметки, меню, иконки, строки
