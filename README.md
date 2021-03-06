# DarChat
DarChat is a custom chat plugin for my Minecraft server DarCraft.

DarCraft is a game server I created in 2011 for the popular game Minecraft. 

Website: https://darcraft.net/

## Plugin Information

This plugin was initially created in 2017 to replace the popular [HeroChat](https://www.spigotmc.org/resources/herochat.19264/) plugin that lacked implementation of new Minecraft chat features, specifically [Spigot](https://www.spigotmc.org/wiki/about-spigot/)'s [Chat Component API](https://www.spigotmc.org/wiki/the-chat-component-api/).

Please note that this plugin was created specifically for my server, therefore no customization is available through the typical use of configuration files. Any additions such as adding chat channels, editing permission nodes, or changing the colors of a channel are not possible without directly editing the hard coded chat channels. 

I plan to make this a plugin that is more usable to the general public in the future, likely under a new name. 

For now, this plugin is unusable without the custom plugin DarUUID created specifically for DarCraft by [x128](https://www.spigotmc.org/resources/authors/x128.3013/).

### Features

This is a chatting plugin. The purpose of this plugin is to allow users to communicate with each other through the use of different chat channels. The features implemented include:

- 5 Different Chat Channels
  - Global - (/g) default channel, sends to all players, all players have access
  - Local - (/l) send a message to players around you, all players have access
  - Donator - (/d) send a message to those with a Donator or Staff rank
  - RolePlay - (/rp) send a message to those with this chat channel enabled (strictly for "Role Playing")
  - Staff - (/st) send a message to fellow staff members
- Permissions-based channels
- The ability to "focus" in on a channel, meaning any regular chat will be automatically sent to the focused channel.
- Distance-based channels
  - Will only send to players in a certain radius
  - Message will not send if no players are nearby to receive the message (also notifies player of this)
- Display Name options (options to show/hide user ranks and custom nicknames)
- Options to mute players in specific channels
- Option to hide chat from certain channels (ex. the "Role Play" channel)
- Ability to implement chat filtering plugins
  - Use [EventPriority.HIGH](https://bukkit.gamepedia.com/Event_API_Reference#Event_Priorities) or lower.
  - Built-in caps check (Example: "HELLO FRIENDS" ⇒ "Hello friends")
- Permissions-based chat colors
- [HoverEvent](https://ci.md-5.net/job/BungeeCord/ws/chat/target/apidocs/net/md_5/bungee/api/chat/HoverEvent.html) implementation
  - Hovering over different parts of a line of chat will display different things
    - Rank/Name ⇒ Show Username, Rank, and a "Click to Send a Private Message" option
    - Chat Channel Indicator ⇒ Show Channel Name
    - Chat Message ⇒ Ø
- [ClickEvent](https://ci.md-5.net/job/BungeeCord/ws/chat/target/apidocs/net/md_5/bungee/api/chat/ClickEvent.html) implementation
  - Clicking different parts of a line of chat will perform different actions
    - Rank/Name ⇒ Utilize [SuggestCommand](https://ci.md-5.net/job/BungeeCord/ws/chat/target/apidocs/net/md_5/bungee/api/chat/ClickEvent.Action.html#SUGGEST_COMMAND) to autofill the command to message the player, minus the actual message.
    - Chat Channel Indicator ⇒ Utilize [RunCommand](https://ci.md-5.net/job/BungeeCord/ws/chat/target/apidocs/net/md_5/bungee/api/chat/ClickEvent.Action.html#RUN_COMMAND) to run a command on the user's behalf to automatically switch to that channel
    - Chat Message ⇒ Ø
- Moderation Commands
  - Send a local message to players near specified coordinates (ex. /slm 150 68 -234 hello!)
  - Log local chat and send it to online staff (implements HoverEvent and ClickEvent)
  - Ability to mute players from Global Chat, preventing them from typing in that channel.
- Admin Logging
  - Log all chat in every channel to one .log file
    - This is externally saved and then cleared each night
  - Shows proper details, such as exact date and time the message was sent, and player location if it is a Local chat message
    
    
 // README.md is not complete
