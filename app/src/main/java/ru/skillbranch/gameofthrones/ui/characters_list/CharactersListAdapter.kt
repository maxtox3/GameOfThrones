package ru.skillbranch.gameofthrones.ui.characters_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.avatarByHouseName
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem

class CharactersListAdapter(
  private val onItemClick: (CharacterItem) -> Unit
) : RecyclerView.Adapter<CharactersListAdapter.ViewHolder>() {

  private var items: List<CharacterItem> = emptyList()

  fun setItems(items: List<CharacterItem>) {
    val oldItems = this.items
    this.items = items
    diff(oldItems, items, this)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.list_item, parent, false)

    return ViewHolder(view, onItemClick)
  }

  override fun getItemCount(): Int = items.size

  override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
//
//  interface Listener {
//    fun onItemClick(id: String)
//  }

  private companion object {
    private fun diff(
      oldItems: List<CharacterItem>,
      newItems: List<CharacterItem>,
      adapter: CharactersListAdapter
    ) {
      DiffUtil
        .calculateDiff(
          object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = oldItems.size

            override fun getNewListSize(): Int = newItems.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
              oldItems[oldItemPosition].id == newItems[newItemPosition].id

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
              oldItems[oldItemPosition] == newItems[newItemPosition]
          }
        )
        .dispatchUpdatesTo(adapter)
    }
  }

  class ViewHolder(
    view: View,
    onItemClick: (CharacterItem) -> Unit
  ) : RecyclerView.ViewHolder(view) {

    private lateinit var boundItem: CharacterItem

    init {
      itemView.setOnClickListener {
        onItemClick(boundItem)
      }
    }

    private val name = itemView.findViewById<TextView>(R.id.name_tv)
    private val info = itemView.findViewById<TextView>(R.id.info_tv)
    private val avatar = itemView.findViewById<ImageView>(R.id.character_house_iv)

    fun bind(item: CharacterItem) {
      boundItem = item
      val unknownInfoText = "Information is unknown"
      avatar.setImageResource(avatarByHouseName(item.house))
      name.text = if (item.name.isEmpty()) unknownInfoText else item.name
      val infoText = listOf(item.titles, item.aliases).flatten()
      info.text = if (infoText.isEmpty()) unknownInfoText else "${infoText.map { "$it•" }}"
      //todo(remove dot from last)
    }
  }
}