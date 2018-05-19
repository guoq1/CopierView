package com.guoqi.copierview

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.guoqi.copierview.autocomplete.FilteredArrayAdapter
import com.guoqi.copierview.autocomplete.TokenCompleteTextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), TokenCompleteTextView.TokenListener<UserBean> {
    val COPIER: String = "copier" //如果有多个抄送控件,最好区分下当前获取的焦点在哪个控件上
    var isSearch = false
    var nowFocusAuto = ""//当前自动补全是焦点
    var searchCopierKey = ""
    private lateinit var adapter: ArrayAdapter<UserBean>
    private var copierList = ArrayList<UserBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initListener()
        initData()
    }


    private fun initListener() {
        //抄送人监听
        auto_copier.setOnFocusChangeListener { _, b ->
            if (b) {
                nowFocusAuto = COPIER
            }
        }
        auto_copier.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!isSearch) {
                    var newKey = s.toString().replace(searchCopierKey, "").replace(",", "").replace(" ", "")
                    if (newKey.isNotEmpty()) {
                        isSearch = true
                        getUserFromNet(newKey)
                    }
                }
            }
        })
        auto_copier.setTokenListener(this)
        //设置是否换行 人数过多是否显示+n字样
        auto_copier.allowCollapse(false)
        auto_copier.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select)
    }

    private fun getUserFromNet(newKey: String) {
        isSearch = false
        //根据newKey 到服务器模糊查询 userList,以下是测试数据
        var userBeanList = ArrayList<UserBean>()
        for (i in 1..10) {
            var userBean = UserBean()
            userBean.id = i.toString()
            userBean.name = "用户$i"
            userBeanList.add(userBean)
        }
        showChooseSpinner(userBeanList)
    }

    private fun initData() {
        var defaultCopier = UserBean()
        defaultCopier.id = "1"
        defaultCopier.name = "用户1"
        copierList.add(defaultCopier)

        //自动带出收件人和抄送人
        auto_copier.addObject(defaultCopier)
    }

    private fun showChooseSpinner(userBeanList: List<UserBean>) {
        var userArray = arrayOfNulls<UserBean>(userBeanList.size)
        for (i in userBeanList.indices) {
            userArray[i] = userBeanList[i]
        }
        adapter = object : FilteredArrayAdapter<UserBean>(this, R.layout.view_auto_complete_item, userArray) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                var convertView = convertView
                if (convertView == null) {
                    val l = context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    convertView = l.inflate(R.layout.view_auto_complete_item, parent, false)
                }

                val userBean = getItem(position)
                (convertView!!.findViewById(R.id.tv) as TextView).text = userBean.name
                return convertView
            }

            override fun keepObject(person: UserBean, mask: String): Boolean {
                var mask = mask
                mask = mask.toLowerCase()
                return person.name.toLowerCase().startsWith(mask) || person.name.toLowerCase().startsWith(mask)
            }
        }
        if (nowFocusAuto == COPIER) {
            auto_copier.setAdapter(adapter)
            auto_copier.showDropDown()
        }
    }

    private fun updateTokenConfirmation() {
        if (nowFocusAuto == COPIER) {
            copierList.clear()
            for (userBean in auto_copier.objects) {
                copierList.add(userBean)
            }
            searchCopierKey = auto_copier.text.toString()
        }
    }

    override fun onTokenAdded(token: UserBean?) {
        updateTokenConfirmation()
    }

    override fun onTokenRemoved(token: UserBean?) {
        updateTokenConfirmation()
    }

    override fun onDuplicateRemoved(token: UserBean?) {
    }
}
