package com.example.inventory

import androidx.lifecycle.*
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class InventoryViewModel(private val itemDao: ItemDao): ViewModel() {

    val allItems: LiveData<List<Item>> = itemDao.getItems().asLiveData()

    fun retrieveItem(id: Int): LiveData<Item> {
        return itemDao.getItem(id).asLiveData()
    }
    private fun updateItem(item: Item) {
        viewModelScope.launch {
            itemDao.update(item)
        }
    }
    fun isStockAvailable(item: Item): Boolean = item.quantityInStock > 0
    fun sellItem(item: Item) {
        if (item.quantityInStock > 0) {
            val newItem = item.copy(quantityInStock = item.quantityInStock - 1)
            updateItem(newItem)
        }
    }
    fun updateItem(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String
    ) {
        val updatedItem = Item(itemId, itemName, itemPrice.replace(',', '.').toDouble(), itemCount.toInt())
        updateItem(updatedItem)
    }
    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemDao.delete(item)
        }
    }

    private fun insertItem(item: Item) {
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }
    private fun getNewItemEntry(itemName: String
                                 ,itemPrice: String
                                 ,itemCount: String): Item {
        return Item(itemName = itemName, itemPrice = itemPrice.toDouble(), quantityInStock = itemCount.toInt())
    }
    public fun addNewItem(itemName: String
                          ,itemPrice: String
                          ,itemCount: String) {
        val newItem = getNewItemEntry(itemName, itemPrice, itemCount)
        insertItem(newItem)
    }
    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }

}
class InventoryViewModelFactory(private val itemDao: ItemDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewODEL CLASS")
    }
}