
fun main() {
    val app = Application()
    app.run()
}

abstract class Phone(val model: String, var price: Double)


class Smartphone(model: String, price: Double) : Phone(model, price)


class Store(val city: String) {
    private val phones = mutableListOf<Phone>()
    private val salesStatistics = mutableMapOf<String, Pair<Int, Double>>()
    private var hasUsedRepairService = false

    init {

        phones.add(Smartphone("iPhone 14", 999.99))
        phones.add(Smartphone("Samsung Galaxy S22", 899.99))
        phones.add(Smartphone("Google Pixel 7", 699.99))
    }

    fun showStatistics() {
        println("Статистика продаж в магазине $city:")
        if (salesStatistics.isEmpty()) {
            println("Нет проданных телефонов.")
        } else {
            for ((model, stats) in salesStatistics) {
                val (count, total) = stats
                println("Модель $model: продано $count шт., общая сумма $total")
            }
        }
    }

    fun buyPhone(model: String) {
        val phone = phones.find { it.model == model }
        if (phone != null) {
            salesStatistics[model] = salesStatistics.getOrDefault(model, Pair(0, 0.0)).let { (count, total) ->
                Pair(count + 1, total + phone.price)
            }
            println("Вы купили телефон: ${phone.model}, цена: ${phone.price}")
        } else {
            println("Телефон не найден.")
        }
    }

    fun repairPhone() {
        if (!hasUsedRepairService) {
            println("Ваш телефон сломался? (да/нет)")
            val response = readLine()
            if (response == "да") {
                println("Телефон отремонтирован.")
                hasUsedRepairService = true
            } else {
                println("Не нуждаетесь в ремонте.")
            }
        } else {
            println("Вы уже воспользовались услугами ремонта.")
        }
    }

    fun showPhones() {
        println("Доступные телефоны в магазине $city:")
        phones.forEach { phone ->
            println("${phone.model}: ${phone.price}")
        }
    }
}


interface UserInterface {
    fun greetUser()
    fun showStores()
}


class Application : UserInterface {
    private val stores = listOf(Store("Город A"), Store("Город B"))

    fun run() {
        greetUser()
        while (true) {
            showStores()
            println("Выберите номер магазина (или 0 для выхода):")
            val selectedStoreIndex = readLine()?.toIntOrNull()
            if (selectedStoreIndex == 0) break
            if (selectedStoreIndex != null && selectedStoreIndex in 1..stores.size) {
                val selectedStore = stores[selectedStoreIndex - 1]
                storeMenu(selectedStore)
            } else {
                println("Неверный выбор, попробуйте снова.")
            }
        }
    }

    override fun greetUser() {
        println("Добро пожаловать в интернет-магазин телефонов!")
    }

    override fun showStores() {
        println("Доступные магазины:")
        stores.forEachIndexed { index, store ->
            println("${index + 1} - ${store.city}")
        }
    }

    private fun storeMenu(store: Store) {
        while (true) {
            println("Вы находитесь в магазине ${store.city}. Выберите действие:")
            println("1 - Посмотреть телефоны")
            println("2 - Купить телефон")
            println("3 - Посмотреть статистику продаж")
            println("4 - Ремонт телефона")
            println("5 - Вернуться в меню магазинов")
            println("Введите номер действия:")
            when (readLine()?.toIntOrNull()) {
                1 -> store.showPhones()
                2 -> {
                    println("Введите модель телефона для покупки:")
                    val model = readLine()
                    if (model != null && model.isNotEmpty()) {
                        store.buyPhone(model)
                    } else {
                        println("Модель не может быть пустой.")
                    }
                }
                3 -> store.showStatistics()
                4 -> store.repairPhone()
                5 -> return
                else -> println("Неверный выбор, попробуйте снова.")
            }
        }
    }
}


