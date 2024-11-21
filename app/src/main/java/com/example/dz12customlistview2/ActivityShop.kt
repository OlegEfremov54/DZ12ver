package com.example.dz12customlistview2

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.io.IOException

class ActivityShop : AppCompatActivity(), Removable, Updatable {
    private lateinit var photoPickerLauncher: ActivityResultLauncher<Intent>
    var listAdapter : ListAdapter? = null
    var product:Product? = null
    var chek = true

    private val GALLERY_REQUEST = 302
    private lateinit var toolbarShop: Toolbar
    private lateinit var editImageIV: ImageView
    private lateinit var productNameET: EditText
    private lateinit var productPriceET: EditText
    private lateinit var addBTN: Button

    private lateinit var listViewLV: ListView
    var fotoUri: Uri? = null
    var item: Int? = null
    private lateinit var productViewModel: ProductViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shop)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        extracted()

        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        val adapter = ListAdapter(this@ActivityShop, productViewModel.products)
        listViewLV.adapter = adapter

        productViewModel.listProduct.observe(this, Observer { it ->
            val adapter = ListAdapter(this@ActivityShop, productViewModel.products)
            adapter.notifyDataSetChanged()
        })

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

        addBTN.setOnClickListener {
            if (productNameET.text.isEmpty() || productPriceET.text.isEmpty()) return@setOnClickListener
            val name = productNameET.text.toString()
            val price = productPriceET.text.toString()
            val image = fotoUri.toString()
            val product = Product(name, price, image)
            productViewModel.addProduct(product)
            listAdapter = ListAdapter(this@ActivityShop, productViewModel.products)
            listViewLV.adapter = listAdapter
            listAdapter?.notifyDataSetChanged()
            productNameET.text.clear()
            productPriceET.text.clear()
            editImageIV.setImageResource(R.drawable.baseline_shopping_cart_checkout_24)
        }

        listViewLV.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                //val listAdapter = ListAdapter(this@ActivityShop, productViewModel.products)
                //listViewLV.adapter = listAdapter
                val product = listAdapter!!.getItem(position)
                item = position
                val dialog = MyAlertDialog()
                val args = Bundle()
                args.putSerializable("product", product)
                dialog.arguments=args
                dialog.show(supportFragmentManager,"custom")


            }
    }

    private fun extracted() {
        toolbarShop = findViewById(R.id.toolbarShop)
        setSupportActionBar(toolbarShop)
        title = " Магазин продуктов"
        toolbarShop.subtitle = "  Версия 2. Страница магазина"
        toolbarShop.setLogo(R.drawable.shop)

        editImageIV = findViewById(R.id.editImageIV)
        productNameET = findViewById(R.id.productNameET)
        productPriceET = findViewById(R.id.productPriceET)
        addBTN = findViewById(R.id.addBTN)

        listViewLV = findViewById(R.id.listViewLV)

    }

    override fun remove(product: Product) {
        //listAdapter?.remove(product)
        val listAdapter = ListAdapter(this@ActivityShop, productViewModel.products)
        listViewLV.adapter = listAdapter
        productViewModel.products.remove(product)
        Toast.makeText(this, "Продукт ${product} удалён", Toast.LENGTH_LONG).show()
    }

    override fun updata(product: Product) {
        val intent= Intent(this, DetalsActivity::class.java)
        intent.putExtra("product", product)
        intent.putExtra("products", this.productViewModel.products as ArrayList<Product> )
        intent.putExtra("positions", item)
        intent.putExtra("chek", chek)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.infoMenuMain -> {
                Toast.makeText(applicationContext, "Автор Ефремов О.В. Создан 21.11.2024",
                    Toast.LENGTH_LONG).show()
            }
            R.id.exitMenuMain ->{
                Toast.makeText(applicationContext, "Работа приложения завершена",
                    Toast.LENGTH_LONG).show()
                finishAffinity()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}