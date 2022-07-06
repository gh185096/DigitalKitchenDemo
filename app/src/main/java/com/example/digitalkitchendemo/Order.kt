package com.example.digitalkitchendemo

data class Order(
    var orderDescription: String? = null,
    var orderNum: Int = 0,
    var orderStatus: String? = null,
    var orderType: String? = null,
    var items: MutableList<String> = mutableListOf("Thai Pad"),
    var items1: MutableList<String> = mutableListOf("Prime Rib"),
    var items2: MutableList<String> = mutableListOf("Fettuccini Alfredo"),
    var items3: MutableList<String> = mutableListOf("Chick-n"),
    var timer: MutableList<String> = mutableListOf("18:30"),
    var name: MutableList<String> = mutableListOf("James"),
)