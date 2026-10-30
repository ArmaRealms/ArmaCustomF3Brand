# Custom F3 Brand

### Edit your server name in debug screen!
#### It's simple, it's powerful, it's fully customizable!
Set your custom message or animation in the debug screen (F3) of your server. You just need to edit the config of the plugin, 
it's easy and intuitive.

## Features
- [x] Paper version
    - PlaceholderAPI support
- [x] Bungeecord version
- [x] Velocity version
  - Velocity version is in alpha stage, please report any issue you find.

## Requirements
- Paper 26.2 for the server implementation.
- Java 25+ for all implementations.
- A [ProtocolLib development build](https://github.com/dmulloy2/ProtocolLib/releases/tag/dev-build)
  with Minecraft 26.2 support (currently 5.5.0-SNAPSHOT) is required on Paper.
  The stable 5.4.0 release is used only as the compile-time API; it does not support 26.2 at runtime.
- PlaceholderAPI 2.12.3 is optional on Paper.

## Building

Build with JDK 25 and Maven 3.9+:

```bash
mvn -U clean verify
```

The plugin JAR is `target/CustomF3Brand-1.2.1.jar`.
The Paper dependency uses `[26.2.build.0,26.2.1)`, Maven's equivalent of
`26.2.build.+`, excluding 26.3 prereleases, following the
[MCPets reference commit](https://github.com/Nocsy-Workshop/mcpets/commit/1127c577e1e4de547a490b524a95a76ff28ccef4).
The resolved build can change; inspect it with `mvn dependency:tree`.

Text formatting uses Adventure 5 directly. The existing BungeeCord and Velocity
implementations remain included; their behavior still needs to be verified on the
proxy versions used in your network. Compilation and unit tests do not validate
the F3 display or proxy packet interception in a running client.

## Installation
Build this fork and put the generated JAR in your plugins folder.
For Paper, also install the ProtocolLib build described above, then restart the server.
The plugin name, configuration directory, commands and permissions are unchanged.

## Commands
- ```/f3r``` to reload the config (Paper)
- ```/bf3r``` to reload the config (bungeecord)
- ```/vf3r``` to reload the config (velocity)

## Permissions
- ```cf3.reload``` to reload the config

## Warnings
- On Paper, `update_period` remains in milliseconds and is rounded up to whole server ticks
  (50 ms at 20 TPS), with a minimum of one tick. Animations and PlaceholderAPI run on the
  server thread. The default 100 ms interval is two ticks.
- Do not reload the plugin in a velocity server! If you want to reload the config use the command ```/vf3r```.
- The configuration file is a JSON file with json5 extension. This is done to allow comments in the file but is parsed as a json. 
When editing the file make sure to follow the json syntax.

## Known issues
- Some plugins and mods to communicate needs certain brand names and may not work properly with this plugin.

## Contributing
To contribute to this repository just fork this repository make your changes or add your code and make a pull request.

## License
CustomF3Brand is released under "The 3-Clause BSD License". You can find a copy [here](https://github.com/MultiCoreNetwork/CustomF3Brand/blob/master/LICENSE)
