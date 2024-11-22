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
    private lateinit var productInfoET:EditText
    var product:Product? = null
    var products: MutableList<Product> = mutableListOf()
    private lateinit var productViewModel: ProductViewModel
    var item: Int? = null
    private lateinit var photoPickerLauncher: ActivityResultLauncher<Intent>
    var fotoUri: Uri? = null

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
        productInfoET=findViewById(R.id.productInfoET)

        product = intent.extras?.getSerializable("product") as Product
        var products = intent.getStringExtra("products")
        val item = intent.extras?.getInt("pozitions")
        val chek = intent.extras?.getBoolean("chek")

        val image:Uri = Uri.parse(product!!.image)
        val name = product!!.name
        val price = product?.price
        val info = product?.productInfo

        productNameET.setText(name)
        productPriceET.setText(price)
        editImageIV.setImageURI(image)
        productInfoET.setText(info)

        photoPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                fotoUri = result.data?.data  // selectedImage для загрузки изображения
                editImageIV.setImageURI(fotoUri)
            }
        }

        editImageIV.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            photoPickerLauncher.launch(photoPickerIntent)
        }


          reversBTN.setOnClickListener {
            val product:Product(
                productNameET.text.toString(),
                productPriceET.text.toString(),
                fotoUri.toString(),
                productInfoET.text.toString()
            )
            val list:MutableList<Product> = products as MutableList<Product>
            if(item!=0){
                swap(item,product,products)
            }
            chek=false
            val intent = Intent(this, ActivityShop::class.java)
              intent.putExtra("list",list as ArrayList<Product>)
              intent.putExtra("newChek", chek)
            startActivity(intent)
            finish()
          }

    }

   fun swap(item:Int,product: Product,products: MutableList<Product> ){
        products.add(item+1,product)
        products.removeAt(item)
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

