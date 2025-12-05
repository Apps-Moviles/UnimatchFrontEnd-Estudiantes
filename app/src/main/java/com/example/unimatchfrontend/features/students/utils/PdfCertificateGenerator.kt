package com.example.unimatchfrontend.features.students.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.provider.MediaStore

object PdfCertificateGenerator {

    fun generateCertificate(
        context: Context,
        projectTitle: String,
        studentName: String,
        date: String
    ): Boolean {
        return try {
            val pdf = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
            val page = pdf.startPage(pageInfo)
            val canvas = page.canvas

            // ---------------------------
            // COLORES Y PEN
            // ---------------------------
            val backgroundColor = Paint().apply {
                color = Color.rgb(245, 240, 232) // color arena
            }

            val borderPaint = Paint().apply {
                style = Paint.Style.STROKE
                strokeWidth = 6f
                color = Color.rgb(200, 180, 120) // dorado suave
            }

            val ornamentPaint = Paint().apply {
                style = Paint.Style.STROKE
                strokeWidth = 3f
                color = Color.rgb(200, 180, 120)
            }

            val titlePaint = Paint().apply {
                textAlign = Paint.Align.CENTER
                textSize = 30f
                typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
                color = Color.BLACK
            }

            val subtitlePaint = Paint().apply {
                textAlign = Paint.Align.CENTER
                textSize = 20f
                typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            }

            val bodyPaint = Paint().apply {
                textAlign = Paint.Align.CENTER
                textSize = 17f
                typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL)
            }

            // Fondo
            canvas.drawRect(0f, 0f, pageInfo.pageWidth.toFloat(), pageInfo.pageHeight.toFloat(), backgroundColor)

            // Marco elegante
            canvas.drawRect(
                40f, 40f,
                (pageInfo.pageWidth - 40).toFloat(),
                (pageInfo.pageHeight - 40).toFloat(),
                borderPaint
            )

            // Ornamento superior (línea + arco estilo diploma)
            canvas.drawLine(120f, 100f, 475f, 100f, ornamentPaint)
            canvas.drawArc(
                200f, 50f,
                395f, 150f,
                0f, 180f,
                false, ornamentPaint
            )

            // ---------------------------
            // TEXTO
            // ---------------------------

            canvas.drawText(projectTitle, 297f, 180f, titlePaint)

            canvas.drawText(
                "Certificado de Finalización",
                297f,
                230f,
                subtitlePaint
            )

            canvas.drawText(
                "Se otorga este certificado a:",
                297f,
                300f,
                bodyPaint
            )

            canvas.drawText(
                studentName,
                297f,
                340f,
                subtitlePaint
            )

            canvas.drawText(
                "Por haber completado exitosamente el proyecto:",
                297f,
                400f,
                bodyPaint
            )

            canvas.drawText(
                projectTitle,
                297f,
                440f,
                bodyPaint
            )

            canvas.drawText(
                "Fecha de finalización: $date",
                297f,
                520f,
                bodyPaint
            )

            // Línea inferior decorativa
            canvas.drawLine(120f, 570f, 475f, 570f, ornamentPaint)

            pdf.finishPage(page)

            // Guardado en Downloads/UniMatch
            val cv = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, "Certificado_$projectTitle.pdf")
                put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/UniMatch")
            }

            val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, cv)
            val output = context.contentResolver.openOutputStream(uri!!)

            pdf.writeTo(output)
            pdf.close()
            output?.close()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
