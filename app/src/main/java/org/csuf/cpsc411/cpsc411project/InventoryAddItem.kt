package org.csuf.cpsc411.cpsc411project

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.*
import java.text.NumberFormat

class InventoryAddItem : AppCompatActivity() {

    /*
     This function is called when the post message from the server is received.
     It it takes in a item, if the item's ID is not -1 then adding item was good in the server.
     If item's ID is -1 then adding item was not good on the server. So if its good add it to local
     DB, if its not good don't add it to local DB
    */
    fun addItemToLocalDB(item: Item) {
        if (item.itemId != -1) {
            val db = DataBaseHandler(this)
            db.insertItem(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory_add_item)

        val itemName = findViewById<EditText>(R.id.itemNameField)
        val itemQty = findViewById<EditText>(R.id.itemQtyField)
        val itemPrice = findViewById<EditText>(R.id.itemPriceField)

        itemPrice.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            var current: String = ""
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() != current && s.isNotBlank()) {
                    itemPrice.removeTextChangedListener(this)

                    val cleanString: String = s.replace("""[$,.]""".toRegex(), "")

                    val parsed = cleanString.toDouble()
                    val formatted = NumberFormat.getCurrencyInstance().format((parsed / 100))

                    current = formatted
                    itemPrice.setText(formatted)
                    itemPrice.setSelection(formatted.length)

                    itemPrice.addTextChangedListener(this)
                }
            }
        })

        val inventoryAddItemBack = findViewById<Button>(R.id.addItemBack)

        inventoryAddItemBack.setOnClickListener{
            val intent = Intent(this, InventoryMenu::class.java)
            startActivity(intent)
        }

        val addItem = findViewById<Button>(R.id.addItem)



        addItem.setOnClickListener{
            val itemNameString = itemName.text.toString()
            val itemQtyString = itemQty.text.toString()
            val priceCleanString: String = itemPrice.text.toString().replace("""[$,.]""".toRegex(), "")

            when {
                itemNameString.trim().isEmpty() -> Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show()
                itemQtyString.trim().isEmpty() -> Toast.makeText(this, "Please enter a quantity", Toast.LENGTH_SHORT).show()
                priceCleanString.trim().isEmpty() -> Toast.makeText(this, "Please enter a price", Toast.LENGTH_SHORT).show()
                else -> {
                    val item = Item(itemNameString, itemQtyString.toInt(), priceCleanString.toInt())
                    val db = DataBaseHandler(this)
                    if(db.checkItemExists(item)){
                        Toast.makeText(this, "Item already exists", Toast.LENGTH_SHORT).show()
//                    db.insertItem(item)
                    }
                    else{
                        var serverDB = ServerHandler()

                        serverDB.addItem(item, this)

                        itemName.text.clear()
                        itemQty.text.clear()
                        itemPrice.text.clear()
                    }
                }
            }
        }

        val clearItem = findViewById<Button>(R.id.clearItem)

        clearItem.setOnClickListener{
            itemName.text.clear()
            itemQty.text.clear()
            itemPrice.text.clear()
        }
    }
}