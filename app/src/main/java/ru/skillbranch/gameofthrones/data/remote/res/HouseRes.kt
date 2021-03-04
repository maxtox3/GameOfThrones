package ru.skillbranch.gameofthrones.data.remote.res

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class HouseRes(
  val url: String,
  val name: String,
  val region: String,
  val coatOfArms: String,
  val words: String,
  val titles: List<String> = listOf(),
  val seats: List<String> = listOf(),
  val currentLord: String,
  val heir: String,
  val overlord: String,
  val founded: String,
  val founder: String,
  val diedOut: String,
  val ancestralWeapons: List<String> = listOf(),
  val cadetBranches: List<JsonElement> = listOf(),
  val swornMembers: List<String> = listOf()
)