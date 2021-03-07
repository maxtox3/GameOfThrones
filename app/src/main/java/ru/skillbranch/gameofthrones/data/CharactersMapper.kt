package ru.skillbranch.gameofthrones.data

import ru.skillbranch.gameofthrones.data.local.entities.*
import ru.skillbranch.gameofthrones.data.remote.res.CharacterRes
import ru.skillbranch.gameofthrones.parseId

fun CharacterRes.toCharacter(): Character {
  return Character(
    id = parseId(url),
    name = name,
    gender = gender,
    culture = culture,
    born = born,
    died = died,
    titles = titles,
    aliases = aliases,
    father = father,
    mother = mother,
    spouse = spouse,
    houseId = parseId(allegiances.first())
  )
}

fun Character.toCharacterItem(houseName: String): CharacterItem {
  return CharacterItem(
    id = id,
    name = name,
    house = houseName,
    titles = titles,
    aliases = aliases
  )
}

fun Character.toCharacterFull(
  house: House?,
  father: RelativeCharacter?,
  mother: RelativeCharacter?
): CharacterFull {
  return CharacterFull(
    id = id,
    name = name,
    words = house?.words ?: "",
    born = born,
    died = died,
    titles = titles,
    aliases = aliases,
    house = house?.name ?: "",
    father = father,
    mother = mother,
  )
}


fun Character.toRelativeCharacter(house: House?): RelativeCharacter {
  return RelativeCharacter(
    id,
    name,
    house = house?.name ?: ""
  )
}