package com.srs.elearningtnd.Utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.StrictMode
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.view.drawToBitmap
import java.io.*
import java.nio.charset.Charset
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class FileMan {

    fun offlineInputStream(context: Context): String? {
        val d = context.getExternalFilesDir(null)?.absolutePath +"/OFFLINE/"+"data_youtube.json"
        val json: String?
        val charset: Charset = Charsets.UTF_8
        try {
            val `is` = FileInputStream(d)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, charset)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    fun unzipAsset(f: String, c: Context) {
        val file = "${f.subSequence(0, f.length - 3)}zip"
        val d = c.getExternalFilesDir(null)?.absolutePath +"/OFFLINE/"
        val destDir = File(d)
        if (!destDir.exists()){
            destDir.mkdirs()
        }
        val buffer = ByteArray(1024)
        val am: AssetManager = c.assets
        val zis = ZipInputStream(am.open(file))
        var zipEntry = zis.nextEntry
        while (zipEntry != null) {
            val newFile = newFile(destDir, zipEntry)
            val fos = FileOutputStream(newFile)
            var len: Int
            while (zis.read(buffer).also { len = it } > 0) {
                fos.write(buffer, 0, len)
            }
            fos.close()
            zipEntry = zis.nextEntry
        }
        zis.closeEntry()
        zis.close()
    }


    fun onlineInputStream(context: Context): String{
        val dirMain = context.getExternalFilesDir(null)?.absolutePath +"/MAIN/"+"data_youtube.json"
        //UpdateMan().setLastUpdateText(tv_tanggal, dirMain)
        val json: String?
        val charset: Charset = Charsets.UTF_8
        val `is`: InputStream = FileInputStream(dirMain)
        val size = `is`.available()
        val buffer = ByteArray(size)
        `is`.read(buffer)
        `is`.close()
        json = String(buffer, charset)
        return json
    }

    fun deleteFiles(type: String, context: Context){
        val fDeleteDBC = File(context.getExternalFilesDir(null)?.absolutePath + "/$type/")
        if (fDeleteDBC.exists()) {
            fDeleteDBC.delete()
        }
    }

    @Throws(IOException::class)
    fun unzip(f: String, d: String) {
        val destDir = File(d)
        val buffer = ByteArray(1024)
        val zis = ZipInputStream(FileInputStream(f))
        var zipEntry = zis.nextEntry
        while (zipEntry != null) {
            val newFile = newFile(destDir, zipEntry)
            val fos = FileOutputStream(newFile)
            var len: Int
            while (zis.read(buffer).also { len = it } > 0) {
                fos.write(buffer, 0, len)
            }
            fos.close()
            zipEntry = zis.nextEntry
        }
        zis.closeEntry()
        zis.close()
    }

    @Throws(IOException::class)
    fun newFile(destinationDir: File, zipEntry: ZipEntry): File {
        val destFile = File(destinationDir, zipEntry.name)
        val destDirPath = destinationDir.canonicalPath
        val destFilePath = destFile.canonicalPath
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw IOException("Entry is outside of the target dir: " + zipEntry.name)
        }
        return destFile
    }

    fun dirChecker(_targetLocation: String) = try {
        val f = File(_targetLocation)
        f.exists()
    } catch (e: Exception){

    }

    fun shareScreenshot(context: Context, parentLayout: RelativeLayout){
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        val file = File(takeScreenshot(context, parentLayout))
        val uri = Uri.fromFile(file)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        (context as Activity).startActivity(intent)
    }

    fun takeScreenshot(context: Context, parentLayout: RelativeLayout/*, iv_SS: ImageView*/):String {
        var filePath = "test"
        val now = Date()
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)

        try {
            // image naming and path  to include sd card  appending name you choose for file
            val mPath = context.externalCacheDir

            // create bitmap screen capture
            val bitmap = parentLayout.drawToBitmap()

            if (!mPath?.exists()!!){
                mPath.mkdirs()
            }

            val outputFile = File(mPath, "Screenshot PalmSentry $now.JPEG")
            val outputStream = FileOutputStream(outputFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            //setting screenshot in imageview
            filePath = outputFile.path

            /*val ssbitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            iv_SS.setImageBitmap(ssbitmap)*/
            Toast.makeText(context, "Screenshot Tersimpan di $filePath!", Toast.LENGTH_SHORT).show()

        } catch (e: Throwable) {
            // Several error may come out with file handling or DOM
            e.printStackTrace()
        }
        return filePath
    }

}