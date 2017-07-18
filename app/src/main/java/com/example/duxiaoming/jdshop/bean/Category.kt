package com.example.duxiaoming.jdshop.bean

/**
 * Created by Ivan on 15/9/24.
 */
open class Category : BaseBean {


    constructor()

    constructor(name: String) {

        this.name = name
    }

    constructor(id: Long, name: String) {
        this.id = id
        this.name = name
    }

    var name: String? = null
}
