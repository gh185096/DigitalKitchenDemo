package com.example.digitalkitchendemo

data class Order(
    var orderDescription: String? = null,
    var orderNum: Int = 0,
    var orderStatus: String? = null,
    var orderType: String? = null
)