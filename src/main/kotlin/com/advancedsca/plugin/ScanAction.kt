package com.advancedsca.plugin

import com.intellij.notification. Notification
import com.intellij. notification.NotificationType
import com.intellij. notification. Notifications
import com.intellij. openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com. intellij.openapi.progress. Task
import com.intellij. openapi.ui.Messages
import com.intellij.openapi.wm. ToolWindowManager

class ScanAction : AnAction() {
    
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        
        ProgressManager.getInstance().run(
            object : Task.Backgroundable(project, "🛡️ Running Security Scan.. .", true) {
                override fun run(indicator: ProgressIndicator) {
                    indicator.text = "Scanning dependencies..."
                    indicator.isIndeterminate = true
                    
                    val service = project.getService(SCAService::class.java)
                    
                    try {
                        val results = service.scanProject(quick = false)
                        
                        ApplicationManager.getApplication().invokeLater {
                            showResults(project, results)
                        }
                        
                    } catch (ex: Exception) {
                        ApplicationManager.getApplication().invokeLater {
                            Messages.showErrorDialog(project, ex.message, "Scan Error")
                        }
                    }
                }
            }
        )
    }
    
    private fun showResults(project: com.intellij.openapi.project.Project, results: SCAService.ScanResult) {
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("SCA Scanner")
        
        toolWindow?.show {
            val panel = toolWindow.contentManager.getContent(0)?. component as? SCAToolWindowPanel
            panel?.displayResults(results)
        }
        
        val notificationType = when {
            results.summary.critical > 0 -> NotificationType.ERROR
            results. summary.high > 0 -> NotificationType.WARNING
            else -> NotificationType.INFORMATION
        }
        
        val notification = Notification(
            "SCA Scanner",
            "🛡️ Scan Complete",
            "Found ${results.summary.total} vulnerabilities\n" +
            "🔴 Critical: ${results.summary. critical} " +
            "🟠 High: ${results.summary.high}",
            notificationType
        )
        
        Notifications. Bus.notify(notification, project)
    }
}