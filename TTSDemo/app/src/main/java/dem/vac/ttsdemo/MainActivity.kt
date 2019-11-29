package dem.vac.ttsdemo

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var tvshow: TextView
    lateinit var edtinput: EditText
    lateinit var btn1: Button
    lateinit var btn2: Button
    lateinit var mSpeech: TextToSpeech

    //检测是否安装了讯飞TTS
    fun CheckTTS(): Boolean {
        return CheckAppInstall.isAppInstalled(this, "com.iflytek.tts")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermission()

        tvshow = findViewById(R.id.tvshow)

        if (!CheckTTS()) {
            intent = Intent(this, DownloadActivity::class.java)
            startActivity(intent)
        }

        mSpeech = TextToSpeech(this, TextToSpeech.OnInitListener {
            if (it == TextToSpeech.SUCCESS) {
                val i = mSpeech.setLanguage(Locale.CHINESE)
                if (i == TextToSpeech.LANG_MISSING_DATA || i == TextToSpeech.LANG_NOT_SUPPORTED) {
                    mSpeech.setSpeechRate(1.0f)
                    tvshow.text = "设置中文语音失败"
                } else {
                    tvshow.text = "初始化成功"
                }
            } else {
                tvshow.text = "初始化失败"
            }
        })

        edtinput = findViewById(R.id.edttext)


        btn1 = findViewById(R.id.btn1)
        btn1.setOnClickListener { view ->
            var str: String = edtinput.text.toString();
            if (str != "") {
                mSpeech.speak(str, TextToSpeech.QUEUE_ADD, null)
            }
        }

        btn2 = findViewById(R.id.btn2)
        btn2.setOnClickListener { view ->
            var intent = Intent("com.android.settings.TTS_SETTINGS")
            startActivity(intent)
        }
    }


    fun requestPermission() {
        val REQUEST_CODE = 1
        if (checkSelfPermission(
                this,
                WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_CODE
            )
        }

    }
}
