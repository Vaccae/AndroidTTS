package dem.vac.ttsdemo

import android.os.AsyncTask
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.net.URL

/**
 * 作者：Vaccae
 * 邮箱：3657447@qq.com
 * 创建时间：2019-11-26 12:07
 * 功能模块说明：
 */

class DownloadHelper {

    companion object StaticFun {

        fun download(url: String, localPath: String, listener: OnDownloadListener) {
            var task = DownloadAsyncTask(url, localPath, listener)
            task.execute()
        }

        class DownloadAsyncTask(mUrl: String, mFilepath: String, Listener: OnDownloadListener) : AsyncTask<String, Int, Boolean>() {
            lateinit var mFailInfo: String
            private var mUrl: String = mUrl
            private var mFilePath: String = mFilepath
            private var mListener: OnDownloadListener = Listener

            override fun onPreExecute() {
                super.onPreExecute()
                this.mListener.onStart()
            }

            override fun onProgressUpdate(vararg values: Int?) {
                super.onProgressUpdate(*values)
                if (values.isNotEmpty()) {
                    values[0]?.let { mListener.onProgress(it) }
                }
            }

            override fun doInBackground(vararg p0: String?): Boolean {
                var pdfurl: String = mUrl
                try {
                    var url = URL(pdfurl)
                    var urlConnection = url.openConnection()
                    var inputStream = urlConnection.getInputStream()
                    var contentlen = urlConnection.contentLength
                    var pdffile = File(mFilePath)
                    //如果存在直接提示安装
                    if (pdffile.exists()) {
                        var result = pdffile.delete()
                        if (!result) {
                            mFailInfo = "存储路径下的同名文件删除失败！"
                            return false
                        }
                    }
                    var downloadSize = 0
                    var bytes = ByteArray(1024)
                    var length : Int
                    var outputStream = FileOutputStream(mFilePath)
                    do {
                        length = inputStream.read(bytes)
                        if (length == -1) break
                        outputStream.write(bytes, 0, length)
                        downloadSize += length
                        publishProgress(downloadSize * 100 / contentlen)
                    } while (true)

                    inputStream.close()
                    outputStream.close()

                } catch (ex: Exception) {
                    ex.printStackTrace()
                    mFailInfo = ex.message.toString()
                    return false
                }
                return true
            }

            override fun onPostExecute(result: Boolean?) {
                super.onPostExecute(result)
                if (result!!) {
                    mListener.onSuccess(File(mFilePath))
                } else {
                    mListener.onFail(File(mFilePath), mFailInfo)
                }
            }

        }

        interface OnDownloadListener {
            fun onStart()
            fun onSuccess(file: File)
            fun onFail(file: File, failInfo: String)
            fun onProgress(progress: Int)
        }
    }
}