# Дипломный проект по профессии «Тестировщик»

___

## 📌 Описание проекта

Данный проект представляет собой автоматизированное тестирование веб-сервиса **"Путешествие дня"**, который предлагает
покупку туров двумя способами:

- **Обычная оплата по дебетовой карте** (Payment Gate)
- **Выдача кредита по данным банковской карты** (Credit Gate)

___

## 📋 Требования к системе

Перед началом работы убедитесь, что у вас установлено:

- **Docker Desktop** (с поддержкой WSL2 на Windows)
- **Java 17** или выше
- **IntelliJ IDEA Ultimate** (или другая IDE с поддержкой Gradle)
- **Git** (для клонирования репозитория)
- **Google Chrome** (для запуска UI-тестов)

___

## 🚀 Быстрый старт

#### 1. Клонирование репозитория

``
git clone https://github.com/GnusovDmitrii/Qa_Diplom
``

#### 2. Запустить IntelliJ IDEA.

#### 3. Открыть проект в IntelliJ IDEA.

#### 4. Запуск инфраструктуры (**Docker**)

___

## Doker

1.
    - Запустить все контейнеры в фоновом режиме

`` docker-compose up -d ``

2.
    - Проверить статус контейнеров

``docker-compose ps``

3.
    - Посмотреть логи SUT

``docker-compose logs sut --tail=30``
___

## Запуск тестов

- Запустить все тесты
  <br>``./gradlew clean test``


- Запустить только тесты оплаты
  <br>``./gradlew test --tests PaymentTest``


- Запустить только тесты кредита
  <br>``./gradlew test --tests CreditTest``

## Генерация отчетов

- Сгенерировать Allure отчет
  <br> ``./gradlew allureReport``


- Открыть Allure отчет в браузере
<br>``./gradlew allureServe``


- Открыть HTML отчет Gradle
<br>``start build/reports/tests/test/index.html``

## Остановка инфраструктуры

- Остановить все контейнеры
<br>``docker-compose down``


- Остановить и удалить все данные (включая БД)
<br>``docker-compose down -v``

##  Отчеты
### После запуска тестов доступны следующие отчеты:

<br>**Allure**	`build/reports/allure-report/`	--- _Наглядный отчет с диаграммами, шагами и скриншотами_.
<br> **Gradle**	`build/reports/tests/test/index.html` --- _Стандартный отчет Gradle_.
<br> **Документация**	`Report.md, Summary.md`	--- _Отчеты о тестировании и автоматизации_.