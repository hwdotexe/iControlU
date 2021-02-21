## Overview
iControlU is a popular Minecraft trolling plugin that aims to add mind control to your Spigot server. With just one simple command, players are able to take control over other online players. Currently, iControlU supports controlling movements, chats, inventories, commands, health, hunger, and damage.

This plugin was originally a Premium resource of mine on Spigot, and having moved on from plugin development, I recently decided to publish the source code for free.

## Usage
### Commands
/icu control <player> [player]  
/icu controlnearest  
/icu stop [player]  
/icu forcechat <player> <message>  
/icu reload  
/icu simple  

### Permissions
You may add each permission node individually, or use the parent nodes as shown below.

- icu.use - Allows access to the "/icu" command  
- icu.control.* - Grants access to every control command  
  - icu.use - (see above)  
  - icu.control - Can use "/icu control <player>"  
  - icu.control.other - Can use "/icu control <player> [player]"  
  - icu.control.nearest - Can use "/icu controlnearest"  
  - icu.forcechat - Can use "/icu forcechat <player> <message>  
  - icu.readchat - Can read the target's chatbox activity  
  - icu.simple - Enables Simple Mode  
- icu.admin - Allows players to control without being controlled  
  - icu.exempt - Exempt ("immune") from being controlled  
  - icu.control.* - See above  
  - icu.reload - Can reload the plugin's configuration  
  - icu.nocooldown - Disables the cooldown for this player  
  - icu.notimelimit - Disables a maximum control time for this player  

_Simple Mode removes time limits, cooldowns, helmet effectiveness, and sound effects from your troll. Additionally, the controller will teleport back to their original location when the troll is over._

## Installation (Server)
To use the plugin, simply download a `.jar` binary from the [Releases](https://github.com/hwdotexe/iControlU/releases) page for your server's version. You will also need the applicable version of [ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/). Place both of these files in your server's `plugins` folder and restart.

## Installation (Development)
If you'd like to install the codebase and make changes, simply follow these instructions:

### Step 1: Clone
In a terminal, use `git clone https://github.com/hwdotexe/iControlU.git`

### Step 2: Import as a Maven Project
This plugin uses Maven as its build system. As such, you'll need to import it using Maven as well. This process varies by IDE, so please seek out their instructions if needed.

### Step 3: Building
When you're ready to compile the plugin locally, you can use `mvn clean package` to generate your `.jar` file. 

## Status Notes
The plugin has gone through a large number of revisions over the years, particularly because I was learning Java at the same time. As such, the code is not pretty, and although the plugin can compile, it encounters runtime issues on the latest version of Minecraft. I've posted releases of past versions of the plugin so that it can continue to bring enjoyment to players, however it is in need of some maintenance to work with newer versions of the game.

## Contributing
If you like this plugin and want to contribute, please do! I welcome any and all Pull Requests from eager developers who want to help out. 
