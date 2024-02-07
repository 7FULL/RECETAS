package com.full.recetas.network

class DataResponse<T> {

    val code: Int? = null

    val data: T? = null

    override fun toString(): String {
        return "DataResponse(code=$code, data=$data)"
    }
}