package com.example.dz12customlistview2

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
    private lateinit var productInfoET: EditText

    private lateinit var photoPickerLauncher: ActivityResultLauncher<Intent>
    private var fotoUri: Uri? = null
    private var product: Product? = null
    private lateinit var products: MutableList<Product>
    private var item: Int? = 0
    private var chek: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detals)

        // Настройка окна
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Настройка Toolbar
        toolbarDetals = findViewById(R.id.toolbarDetals)
        setSupportActionBar(toolbarDetals)
        title = " Магазин продуктов"
        toolbarDetals.subtitle = "  Версия 2. Страница Продукта"
        toolbarDetals.setLogo(R.drawable.shop)

        // Инициализация Views
        editImageIV = findViewById(R.id.editImageIV)
        productNameET = findViewById(R.id.productNameET)
        productPriceET = findViewById(R.id.productPriceET)
        reversBTN = findViewById(R.id.reversBTN)
        productInfoET = findViewById(R.id.productInfoET)
// Получение данных из Intent
        product = intent.extras?.getSerializable("product") as? Product
        products = intent.getSerializableExtra("products") as? ArrayList<Product> ?: arrayListOf()
        item = intent.extras?.getInt("positions")!!
        chek = intent.getBooleanExtra("chek", false)

        // Заполнение данных продукта
        product?.let {
            productNameET.setText(it.name)
            productPriceET.setText(it.price)
            productInfoET.setText(it.productInfo)
            val imageUri = Uri.parse(it.image)
            editImageIV.setImageURI(imageUri)
        }

        // Настройка выбора изображения
        photoPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                fotoUri = result.data?.data
                editImageIV.setImageURI(fotoUri)
            }
        }

        editImageIV.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            photoPickerLauncher.launch(photoPickerIntent)
        }

        // Кнопка сохранения и возвращения
        reversBTN.setOnClickListener {
            val updatedProduct = Product(
                name = productNameET.text.toString(),
                price = productPriceET.text.toString(),
                image = fotoUri?.toString() ?: product?.image ?: "",
                productInfo = productInfoET.text.toString()
            )
            if (item in products.indices) {
                swap(item, updatedProduct, products)
            }
            val intent = Intent(this, ActivityShop::class.java).apply {
                putExtra("list", ArrayList(products))
                putExtra("newChek", false)
            }
            startActivity(intent)
            finish()
        }
    }

    // Swap-функция
    private fun swap(item: Int?, product: Product, products: MutableList<Product>) {
        products.add(item!! + 1, product)
        products.removeAt(item!!)
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