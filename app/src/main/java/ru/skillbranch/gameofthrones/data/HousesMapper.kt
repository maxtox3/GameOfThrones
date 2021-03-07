package ru.skillbranch.gameofthrones.data

import ru.skillbranch.gameofthrones.data.local.entities.House
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes
import ru.skillbranch.gameofthrones.parseId

fun HouseRes.toHouse(): House {
  return House(
    id = parseId(url),
    name = name,
    region = region,
    coatOfArms = coatOfArms,
    words = words,
    titles = titles,
    seats = seats,
    currentLord = currentLord,
    heir = heir,
    overlord = overlord,
    founded = founded,
    founder = founder,
    diedOut = diedOut,
    ancestralWeapons = ancestralWeapons,
  )
}