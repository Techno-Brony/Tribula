package io.github.techno_brony.tribula.plugin

import java.io.File

class Template : JavaPlugin() {

    override fun onEnable() {
        Listener(this)
    }

    override fun onDisable() {

    }

    override fun onCommand(e: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (command.name.equals("templatecmd", ignoreCase = true)) {
            if (e is Player) {
                e.sendMessage("This command works!")
            }
            return true
        }
        return false
    }

    private fun createConfig() {
        try {
            if (!dataFolder.exists()) {
                dataFolder.mkdirs()
            }
            val file = File(dataFolder, "config.yml")
            if (!file.exists()) {
                logger.info("Creating config.yml ...")
                saveDefaultConfig()
            } else {
                logger.info("Loading config.yml ...")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}
