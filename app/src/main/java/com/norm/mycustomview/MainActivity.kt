package com.norm.mycustomview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.norm.mycustomview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), TestView.Listener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.testView.listener = this
    }

    override fun onClick(index: Int) {
        binding.textView.text = TextUtils.menuList[index]
    }
}