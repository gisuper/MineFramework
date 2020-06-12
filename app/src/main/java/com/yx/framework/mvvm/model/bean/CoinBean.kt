package com.yx.framework.mvvm.model.bean

data class CoinData(val rank: Int = 0,
                val userId: Int = 0,
                val coinCount: Int = 0,
                val username: String = "")


data class CoinBean(val data: CoinData,
                    val errorCode: Int = 0,
                    val errorMsg: String = "")


