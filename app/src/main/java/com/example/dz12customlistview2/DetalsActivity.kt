package com.example.dz12customlistview2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetalsActivity : AppCompatActivity() {
    private lateinit var toolbarDetals: Toolbar
    private lateinit var editImageIV: ImageView
    private lateinit var productNameET: EditText
    private lateinit var productPriceET: EditText
    private lateinit var reversBTN: Button
    var product:Product? = null
    private lateinit var productViewModel: ProductViewModel
    var item: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detals)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        toolbarDetals = findViewById(R.id.toolbarDetals)
        setSupportActionBar(toolbarDetals)
        title = " Магазин продуктов"
        toolbarDetals.subtitle = "  Версия 2. Страница Продукта"
        toolbarDetals.setLogo(R.drawable.shop)

        editImageIV = findViewById(R.id.editImageIV)
        productNameET = findViewById(R.id.productNameET)
        productPriceET = findViewById(R.id.productPriceET)
        reversBTN = findViewById(R.id.reversBTN)

        product = intent.getStringExtra("product")




        reversBTN.setOnClickListener {
            val intent = Intent(this, ActivityShop::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.infoMenuMain -> {
                Toast.makeText(
                    applicationContext, "Автор Ефремов О.В. Создан 21.11.2024",
                    Toast.LENGTH_LONG
                ).show()
            }

            R.id.exitMenuMain -> {
                Toast.makeText(
                    applicationContext, "Работа приложения завершена",
                    Toast.LENGTH_LONG
                ).show()
                finishAffinity()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

