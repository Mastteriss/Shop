fun main() {
    val app = Application()  // Создаем экземпляр приложения
    app.run()                // Запускаем приложение
}

// Абстрактный класс для представления телефона
abstract class Phone(val model: String, var price: Double)

// Класс Smartphone наследует от абстрактного класса Phone
class Smartphone(model: String, price: Double) : Phone(model, price)

// Класс Store представляет магазин
class Store(val city: String) {
    // Список телефонов, доступных в магазине
    private val phones = mutableListOf<Phone>()
    // Статистика продаж (модель телефона → пара (количество, общая сумма))
    private val salesStatistics = mutableMapOf<String, Pair<Int, Double>>()
    // Флаг, указывающий, был ли использован сервис ремонта
    private var hasUsedRepairService = false

    // Инициализация магазина и добавление телефонов
    init {
        phones.add(Smartphone("iPhone 14", 999.99))
        phones.add(Smartphone("Samsung Galaxy S22", 899.99))
        phones.add(Smartphone("Google Pixel 7", 699.99))
    }

    // Метод для отображения статистики продаж
    fun showStatistics() {
        println("Статистика продаж в магазине $city:")
        if (salesStatistics.isEmpty()) {
            println("Нет проданных телефонов.")
        } else {
            // Перебираем статистику по каждой модели
            for ((model, stats) in salesStatistics) {
                val (count, total) = stats
                println("Модель $model: продано $count шт., общая сумма $total")
            }
        }
    }

    // Метод для покупки телефона
    fun buyPhone(model: String) {
        // Находим телефон по модели
        val phone = phones.find { it.model == model }
        if (phone != null) {
            // Обновляем статистику продаж
            salesStatistics[model] = salesStatistics.getOrDefault(model, Pair(0, 0.0)).let { (count, total) ->
                Pair(count + 1, total + phone.price)
            }
            println("Вы купили телефон: ${phone.model}, цена: ${phone.price}")
        } else {
            println("Телефон не найден.")
        }
    }

    // Метод для ремонта телефона
    fun repairPhone() {
        if (!hasUsedRepairService) {
            println("Ваш телефон сломался? (да/нет)")
            val response = readLine() // Считываем ответ пользователя
            if (response == "да") {
                println("Телефон отремонтирован.")
                hasUsedRepairService = true // Устанавливаем флаг, что ремонт был использован
            } else {
                println("Не нуждаетесь в ремонте.")
            }
        } else {
            println("Вы уже воспользовались услугами ремонта.")
        }
    }

    // Метод для отображения доступных телефонов
    fun showPhones() {
        println("Доступные телефоны в магазине $city:")
        phones.forEach { phone ->
            println("${phone.model}: ${phone.price}")
        }
    }
}

// Интерфейс для пользовательского взаимодействия
interface UserInterface {
    fun greetUser() // Метод для приветствия пользователя
    fun showStores() // Метод для отображения доступных магазинов
}

// Класс Application реализует интерфейс UserInterface и управляет приложением
class Application : UserInterface {
    // Предопределенный список магазинов
    private val stores = listOf(Store("Город A"), Store("Город B"))

    // Запуск приложения
    fun run() {
        greetUser() // Приветствие пользователя
        while (true) {
            showStores() // Показать список магазинов
            println("Выберите номер магазина (или 0 для выхода):")
            val selectedStoreIndex = readLine()?.toIntOrNull() // Считываем выбор пользователя
            if (selectedStoreIndex == 0) break // Выход из цикла, если 0
            if (selectedStoreIndex != null && selectedStoreIndex in 1..stores.size) {
                val selectedStore = stores[selectedStoreIndex - 1] // Получаем выбранный магазин
                storeMenu(selectedStore) // Открываем меню магазина
            } else {
                println("Неверный выбор, попробуйте снова.") // Обработка неверного ввода
            }
        }
    }

    // Реализация метода приветствия
    override fun greetUser() {
        println("Добро пожаловать в интернет-магазин телефонов!")
    }

    // Реализация метода показа магазинов
    override fun showStores() {
        println("Доступные магазины:")
        stores.forEachIndexed { index, store ->
            println("${index + 1} - ${store.city}") // Показать каждый магазин
        }
    }

    // Метод для управления действиями в конкретном магазине
    private fun storeMenu(store: Store) {
        while (true) {
            println("Вы находитесь в магазине ${store.city}. Выберите действие:")
            println("1 - Посмотреть телефоны")
            println("2 - Купить телефон")
            println("3 - Посмотреть статистику продаж")
            println("4 - Ремонт телефона")
            println("5 - Вернуться в меню магазинов")
            println("Введите номер действия:")
            // Обработка выбора действия
            when (readLine()?.toIntOrNull()) {
                1 -> store.showPhones() // Показать телефоны
                2 -> {
                    println("Введите модель телефона для покупки:")
                    val model = readLine() // Считываем модель телефона
                    if (model != null && model.isNotEmpty()) {
                        store.buyPhone(model) // Пытаемся купить телефон
                    } else {
                        println("Модель не может быть пустой.") // Обработка пустого ввода
                    }
                }
                3 -> store.showStatistics() // Показать статистику
                4 -> store.repairPhone() // Запросить ремонт
                5 -> return // Возврат в меню магазинов
                else -> println("Неверный выбор, попробуйте снова.") // Обработка неверного ввода
            }
        }
    }
}
