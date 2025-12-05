package com.advancedsca.plugin

import com.intellij.openapi.project.Project
import com.intellij.ui.components. JBScrollPane
import com.intellij.ui.jcef.JBCefBrowser
import java.awt.BorderLayout
import javax.swing.JPanel

class SCAToolWindowPanel(private val project: Project) : JPanel(BorderLayout()) {
    
    private val browser = JBCefBrowser()
    
    init {
        add(JBScrollPane(browser. component), BorderLayout.CENTER)
        showWelcome()
    }
    
    private fun showWelcome() {
        browser.loadHTML("""
            <! DOCTYPE html>
            <html>
            <head>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        color: white;
                        padding: 60px;
                        text-align: center;
                    }
                    h1 { font-size: 3em; }
                </style>
            </head>
            <body>
                <h1>🛡️ Advanced SCA Scanner</h1>
                <p>Click <strong>Tools → SCA Scanner → Run Security Scan</strong></p>
                <p>or press <kbd>Ctrl+Alt+Shift+S</kbd></p>
            </body>
            </html>
        """.trimIndent())
    }
    
    fun displayResults(results: SCAService.ScanResult) {
        browser.loadHTML("""
            <! DOCTYPE html>
            <html>
            <head>
                <style>
                    body {
                        font-family: Arial;
                        background: #1e1e1e;
                        color: #e0e0e0;
                        padding: 20px;
                    }
                    .header {
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        padding: 30px;
                        border-radius: 12px;
                    }
                    .summary { display: flex; gap: 20px; margin-top: 20px; }
                    .card { background: rgba(255,255,255,0.1); padding: 20px; border-radius: 8px; flex: 1; text-align: center; }
                    .card h2 { font-size: 2.5em; margin: 0; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>🛡️ Scan Results</h1>
                    <p>${results.timestamp}</p>
                    <div class="summary">
                        <div class="card"><h2>${results.summary.total}</h2><p>Total</p></div>
                        <div class="card"><h2 style="color:#ff4757">${results.summary.critical}</h2><p>Critical</p></div>
                        <div class="card"><h2 style="color:#ffa502">${results.summary.high}</h2><p>High</p></div>
                        <div class="card"><h2 style="color:#ffc107">${results.summary.medium}</h2><p>Medium</p></div>
                    </div>
                </div>
            </body>
            </html>
        """.trimIndent())
    }
}