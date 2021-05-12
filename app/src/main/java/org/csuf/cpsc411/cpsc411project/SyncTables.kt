package org.csuf.cpsc411.cpsc411project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SyncTables : AppCompatActivity() {

    fun syncWithLocalDB(list : MutableList<Item>)
    {
        val db = DataBaseHandler(this)
        db.clearInventoryTable()

        println("cleared and about to add items")

        list.forEach{
            db.insertItem(it)
        }

        val intent = Intent(this, Inventory::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sync_tables)

        val inventorySyncBack = findViewById<Button>(R.id.inventorySyncBack)

        inventorySyncBack.setOnClickListener{
            val intent = Intent(this, InventoryMenu::class.java)
            startActivity(intent)
        }

        val confirmSync = findViewById<Button>(R.id.confirmSync)

        confirmSync.setOnClickListener{

            println("Syncing Tables")
            var serverDB = ServerHandler()
            serverDB.getItemsFromServer(this)

        }
    }
}