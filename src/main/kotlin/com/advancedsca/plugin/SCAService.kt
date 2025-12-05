package com.advancedsca.plugin

import com.google.gson.Gson
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util. concurrent.TimeUnit

@Service(Service.Level.PROJECT)
class SCAService(private val project: Project) {
    
    private val gson = Gson()
    
    data class ScanResult(
        val timestamp: String,
        val summary: ScanSummary,
        val reportPath: String? 
    )
    
    data class ScanSummary(
        val total: Int,
        val critical: Int,
        val high: Int,
        val medium: Int,
        val low: Int,
        val duration: String
    )
    
    fun scanProject(quick: Boolean = false): ScanResult {
        val projectPath = project.basePath 
            ?: throw Exception("Project path is null")
        
        val nodePath = findNodeExecutable()
            ?: throw Exception("Node. js not found!  Install from: https://nodejs.org/")
        
        val cliPath = findCLIScript()
            ?: throw Exception("CLI not found! Please compile VS Code extension: npm run compile")
        
        val command = mutableListOf(
            nodePath,
            cliPath,
            "--project", projectPath,
            "--format", "json",
            "--output", "sca-report.json"
        )
        
        if (quick) {
            command.add("--quick")
        }
        
        return executeScanner(command, projectPath)
    }
    
    private fun findNodeExecutable(): String? {
        val candidates = listOf("node", "node.exe", "/usr/local/bin/node", "/usr/bin/node")
        
        for (candidate in candidates) {
            try {
                val process = ProcessBuilder(candidate, "--version")
                    .redirectErrorStream(true)
                    .start()
                
                if (process.waitFor(5, TimeUnit.SECONDS) && process.exitValue() == 0) {
                    println("✅ Found Node.js: $candidate")
                    return candidate
                }
            } catch (e: Exception) {
                continue
            }
        }
        
        return null
    }
    
    private fun findCLIScript(): String? {
        val projectPath = project.basePath ?: return null
        
        // Look in parent folder
        val parentCLI = File(projectPath, "../out/cli-simple.js")
        if (parentCLI. exists()) {
            return parentCLI.canonicalPath
        }
        
        // Look locally
        val localCLI = File(projectPath, "out/cli-simple.js")
        if (localCLI. exists()) {
            return localCLI.absolutePath
        }
        
        return null
    }
    
    private fun executeScanner(command: List<String>, projectPath: String): ScanResult {
        val processBuilder = ProcessBuilder(command)
        processBuilder.directory(File(projectPath))
        
        val process = processBuilder.start()
        
        BufferedReader(InputStreamReader(process.errorStream)).use { reader ->
            reader.lines().forEach { line ->
                println("[Scanner] $line")
            }
        }
        
        process.waitFor(300, TimeUnit.SECONDS)
        
        val reportFile = File(projectPath, "sca-report.json")
        val reportJson = reportFile.readText()
        val data = gson.fromJson(reportJson, Map::class.java)
        
        val summary = data["summary"] as?  Map<*, *> ?: emptyMap<Any, Any>()
        
        return ScanResult(
            timestamp = data["timestamp"] as? String ?: "Unknown",
            summary = ScanSummary(
                total = (summary["total"] as? Number)?.toInt() ?: 0,
                critical = (summary["critical"] as? Number)?. toInt() ?: 0,
                high = (summary["high"] as? Number)?.toInt() ?: 0,
                medium = (summary["medium"] as? Number)?.toInt() ?: 0,
                low = (summary["low"] as? Number)?.toInt() ?: 0,
                duration = summary["duration"] as? String ?: "Unknown"
            ),
            reportPath = reportFile.absolutePath
        )
    }
}