package com.example.freenettunnel

import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var status: TextView
    private val requestCodeVpn = 7001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(36, 56, 36, 36)
        }
        val title = TextView(this).apply {
            text = "Hamo VPN"
            textSize = 28f
        }
        status = TextView(this).apply {
            text = "الحالة: غير متصل"
            textSize = 18f
            setPadding(0, 32, 0, 24)
        }
        val importButton = Button(this).apply {
            text = "استيراد ملف إعدادات"
            setOnClickListener {
                val pick = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "*/*"
                }
                startActivityForResult(pick, 7002)
            }
        }
        val connectButton = Button(this).apply {
            text = "طلب إذن VPN"
            setOnClickListener {
                val intent = VpnService.prepare(this@MainActivity)
                if (intent != null) startActivityForResult(intent, requestCodeVpn)
                else onActivityResult(requestCodeVpn, RESULT_OK, null)
            }
        }
        val note = TextView(this).apply {
            text = "\nملاحظة: هذه نسخة أولية للواجهة واستيراد الإعدادات. لم يتم دمج محرك نفق WireGuard/SSH بعد، لذلك لن تنشئ اتصال إنترنت بمجرد الضغط على الزر."
            textSize = 14f
        }
        layout.addView(title)
        layout.addView(status)
        layout.addView(importButton)
        layout.addView(connectButton)
        layout.addView(note)
        setContentView(layout)
    }

    @Deprecated("Deprecated by Android; retained for broad compatibility in this starter")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 7002 && resultCode == RESULT_OK) {
            status.text = "الحالة: تم اختيار ملف إعدادات — يحتاج التحقق والربط بمحرك VPN"
            Toast.makeText(this, "تم اختيار الملف", Toast.LENGTH_SHORT).show()
        } else if (requestCode == requestCodeVpn && resultCode == RESULT_OK) {
            status.text = "الحالة: تم منح إذن VPN فقط — النفق غير مفعّل بعد"
            Toast.makeText(this, "تم منح الإذن. يلزم دمج محرك النفق.", Toast.LENGTH_LONG).show()
        }
    }
}
