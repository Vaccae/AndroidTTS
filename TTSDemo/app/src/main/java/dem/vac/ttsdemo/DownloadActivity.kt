package dem.vac.ttsdemo

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import dem.vac.ttsdemo.DownloadHelper.StaticFun.OnDownloadListener
import java.io.File

class DownloadActivity : AppCompatActivity() {

    lateinit var btndo: Button
    lateinit var progress: ProgressBar
    lateinit var tvstatus: TextView
    lateinit var actionBar: ActionBar

    private val downloadurl: String = "http://www.sumsoft.cn/apk/TTSChina.apk"
    private val filename: String = "TTSChina.apk"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_download)

        initControl()
        startdownload()
    }

    private fun initControl() {
        tvstatus = findViewById(R.id.tvstatus)
        progress = findViewById(R.id.progressbar)
        btndo = findViewById(R.id.btndo)
    }

    private fun startdownload() {
        var localpath: String =
            Environment.getExternalStorageDirectory().absolutePath + File.separator + "SUM" + File.separator + filename
        DownloadHelper.download(
            downloadurl, localpath, object : OnDownloadListener {
                override fun onStart() {
                    tvstatus.text = "正在下载中....."
                    btndo.visibility = View.GONE
                    progress.progress = 0
                }

                override fun onSuccess(file: File) {
                    tvstatus.text = "下载完成！"
                    btndo.visibility = View.VISIBLE
                    btndo.text = "点击安装"
                    btndo.setOnClickListener {
                        var intent = Intent(Intent.ACTION_VIEW)
                        var uri: Uri
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            uri = FileProvider.getUriForFile(
                                applicationContext,
                                applicationContext.packageName + ".provider",
                                File(localpath)
                            )
                        } else {
                            uri = Uri.fromFile(File(localpath))
                        }

                        intent.setDataAndType(
                            uri,
                            "application/vnd.android.package-archive"
                        )

                        startActivity(intent)
                    }

                }

                override fun onFail(file: File, failInfo: String) {
                    tvstatus.text = "下载失败！" + failInfo
                    btndo.visibility = View.GONE

                }

                override fun onProgress(pro: Int) {
                    tvstatus.text = "正在下载中..... $pro%"
                    progress.progress = pro
                }

            })

    }

}

