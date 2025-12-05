package com.advancedsca. plugin

import com.intellij. openapi.project.Project
import com.intellij.openapi. wm.ToolWindow
import com.intellij.openapi. wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

class SCAToolWindowFactory : ToolWindowFactory {
    
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val panel = SCAToolWindowPanel(project)
        val content = ContentFactory.getInstance().createContent(panel, "", false)
        toolWindow.contentManager.addContent(content)
    }
}