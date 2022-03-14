package br.com.eventslist.data.model

import com.google.gson.annotations.SerializedName

data class EventCheckInRequest(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String
)
