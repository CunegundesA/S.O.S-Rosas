package com.example.sosrosas.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.example.sosrosas.R
import kotlinx.android.synthetic.main.activity_pos_cadastro.*
import org.w3c.dom.Text
import java.text.FieldPosition

class PosCadastroActivity : AppCompatActivity(), View.OnClickListener, ViewPager.OnPageChangeListener {

    private lateinit var slideAdapterPosCadastro: SlideAdapterPosCadastro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pos_cadastro)

        if(supportActionBar != null){
            supportActionBar!!.hide()
        }

        slideAdapterPosCadastro = SlideAdapterPosCadastro(this)
        view_pager_pos_cadastro.adapter = slideAdapterPosCadastro

        addIndicator(0)
        getListeners()
    }

    private fun getListeners(){
        view_pager_pos_cadastro.setOnPageChangeListener(this)
        button_avanca_pos_cadastro.setOnClickListener(this)
        button_skip_pos_cadastro.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        val id = view.id

        if(id == R.id.button_avanca_pos_cadastro){
            if(view_pager_pos_cadastro.currentItem < 5) {
                view_pager_pos_cadastro.setCurrentItem(view_pager_pos_cadastro.currentItem + 1)
            }else{
                startActivity(Intent(application, PrincipalActivity::class.java))
                finish()
            }
        }else if(id == R.id.button_skip_pos_cadastro){
            startActivity(Intent(application, PrincipalActivity::class.java))
            finish()
        }

    }

    private fun addIndicator(position: Int){
        val mTextView = ArrayList<TextView>()
        indicator_view_pager.removeAllViews()

        for(x in 0 until 6){
            mTextView.add(TextView(this))
            mTextView.get(x).setText(Html.fromHtml("&#8226;"))
            mTextView.get(x).textSize = 35f
            mTextView.get(x).setTextColor(resources.getColor(R.color.grayColor))

            indicator_view_pager.addView(mTextView.get(x))
        }

        if(mTextView.size > 0){
            mTextView.get(position).setTextColor(resources.getColor(R.color.colorTextDefaultRed))
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        addIndicator(position)
    }

    override fun onPageScrollStateChanged(state: Int) {
    }
}
