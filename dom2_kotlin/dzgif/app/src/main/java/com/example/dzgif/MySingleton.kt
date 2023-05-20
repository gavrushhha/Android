package com.example.dzgif

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class MySingleton private constructor(context: Context) {
    private var requestQueue: RequestQueue? = null

    init {
        ctx = context.applicationContext
        requestQueue = getRequestQueue()
    }

    companion object {
        @Volatile
        private var instance: MySingleton? = null
        private lateinit var ctx: Context

        fun getInstance(context: Context): MySingleton {
            return instance ?: synchronized(this) {
                instance ?: MySingleton(context).also { instance = it }
            }
        }
    }

    private fun getRequestQueue(): RequestQueue {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.applicationContext)
        }
        return requestQueue!!
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        getRequestQueue().add(req)
    }
}
