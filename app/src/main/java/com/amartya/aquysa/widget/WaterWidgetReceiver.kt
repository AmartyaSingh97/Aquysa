package com.amartya.aquysa.widget

import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WaterWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = WaterWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.action == "com.amartya.aquysa.ACTION_UPDATE_WIDGET") {

            val pendingResult = goAsync()
            val coroutineScope = CoroutineScope(Dispatchers.IO)
            coroutineScope.launch {
                try{
                val manager = GlanceAppWidgetManager(context)
                val glanceIds = manager.getGlanceIds(WaterWidget::class.java)
                glanceIds.forEach { glanceId ->
                     glanceAppWidget.update(context, glanceId)
                    }
                }finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
