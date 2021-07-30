package com.hrithik.myscanner.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.hrithik.myscanner.MyApplication
import com.hrithik.myscanner.R
import com.hrithik.myscanner.databinding.ActivityMainBinding
import com.hrithik.myscanner.fragments.CreateQrFragment
import com.hrithik.myscanner.fragments.ScanQrFragment
import com.hrithik.myscanner.model.QrHistoryInfo
import com.hrithik.myscanner.room.QrInfoViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding
    val qrInfoViewModel: QrInfoViewModel by viewModels {
        QrInfoViewModel.QrInfoViewModelFactory((application as MyApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)


        b.mainBottomNavigation.setOnItemSelectedListener {
            val fragment:Fragment
            when(it.itemId){
                R.id.scanQR -> {
                    fragment = ScanQrFragment()
                    loadFragment(fragment)
                    return@setOnItemSelectedListener true
                }

                R.id.createQR -> {
                    fragment = CreateQrFragment()
                    loadFragment(fragment)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }

        loadFragment(ScanQrFragment())
    }



    private fun loadFragment(fragment: Fragment) {
        val transaction : FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameContainer,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        finish()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_action_bar,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.historyBtn){
            startActivity(Intent(this@MainActivity,QrInfoActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }


    public fun saveQrInfoInRoom(qrHistoryInfo: QrHistoryInfo){
        qrInfoViewModel.insert(qrHistoryInfo)
//        Toast.makeText(this, qrHistoryInfo.qrData,Toast.LENGTH_SHORT).show()
    }

}