package io.github.techno_brony.tribula.plugin

internal class Listener(private val plugin: JavaPlugin) : Listener {

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }


}
